package com.pickle.sys.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.sys.bean.SsJbxx;
import com.pickle.sys.bean.entity.SsJbxxEntity;
import com.pickle.sys.service.ISsJbxxService;
import com.pickle.utils.base.BaseController;
import com.pickle.utils.bean.BeanUtils;
import com.pickle.utils.constant.StringConstant;
import com.pickle.utils.constant.ThreadConstant;
import com.pickle.utils.date.DateUtils;
import com.pickle.utils.excel.BaseCellStyleStrategy;
import com.pickle.utils.excel.ExcelUtils;
import com.pickle.utils.excel.FreezeAndFilter;
import com.pickle.utils.uuid.UUIDUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("/ssJbxx")
public class SsJbxxController extends BaseController<SsJbxx> {
    @Autowired
    private ISsJbxxService ssJbxxService;

    @RequestMapping("/save")
    public void save(@RequestBody SsJbxx ssJbxx) {
        ssJbxx.setSsJbxxUuid(UUIDUtil.newUUID());
        ssJbxxService.save(ssJbxx);
    }

    @RequestMapping("/update")
    public void update(@RequestBody SsJbxx ssJbxx) {
        ssJbxxService.update(ssJbxx);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SsJbxx ssJbxx) {
        if (ssJbxx.getSsJbxxUuidIn().size() > 0){
            ssJbxxService.batchDeleteByPrimaryKey(ssJbxx.getSsJbxxUuidIn());
        }
    }

    @RequestMapping("/queryPageList")
    public PageInfo<SsJbxx> queryPageList(@RequestBody SsJbxx ssJbxx) {
        PageHelper.startPage(ssJbxx.getPageNum(), ssJbxx.getPageSize());
        return ssJbxxService.getPage(ssJbxxService.queryPageList(ssJbxx), ssJbxx);
    }

    @RequestMapping("/exportExcel")
    public void exportExcel(@RequestBody SsJbxx ssJbxx, HttpServletResponse response) {
        PageHelper.startPage(ssJbxx.getPageNum(), ssJbxx.getPageSize());
        List<SsJbxx> dataList = ssJbxxService.queryPageList(ssJbxx);

        List<String> fieldNames = Arrays.asList("zbfMc", "ssRq", "ssMc", "tzfMc", "gzZd", "ssYsJe");
        ExcelWriter writer = ExcelUtils.easyExcelExport(response, "赛事基本信息");

        WriteSheet sheet1 = EasyExcel.writerSheet()
                .registerWriteHandler(new FreezeAndFilter(0, 1, 0, 1))
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 50, (short) 20))
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new BaseCellStyleStrategy())
                .head(SsJbxxEntity.class)
                .includeColumnFieldNames(fieldNames)
                .build();

        writer.write(dataList, sheet1);
        writer.finish();
    }

    @RequestMapping("/importExcel")
    public void importExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<SsJbxxEntity> dataList = EasyExcel.read(file.getInputStream())
                    .sheet()
                    .head(SsJbxxEntity.class)
                    .headRowNumber(1)
                    .doReadSync();

            List<SsJbxx> list = BeanUtils.copyPropertiesList(dataList, SsJbxx.class);
            list.forEach(e -> {
                String s = UUIDUtil.newUUID();
                e.setSsJbxxUuid(UUIDUtil.newUUID());
            });
            ssJbxxService.batchSave(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/exportBigExcel")
    public void exportBigExcel(@RequestBody SsJbxx ssJbxx, HttpServletResponse response) {
        String fileName = "赛事基本信息";
        List<String> fieldNames = Arrays.asList("zbfMc", "ssRq", "ssMc", "tzfMc", "gzZd", "ssYsJe");

        long dataCount = ssJbxxService.queryCountAll();
        int recordsPerFile = 100_000;
        int totalFiles = (int) Math.ceil((double) dataCount / recordsPerFile);

        if (totalFiles > 50) {
            recordsPerFile = (int) Math.ceil((double) dataCount / 50);
            totalFiles = 50;
        }

        try {
            String timestamp = DateUtils.date2StringTime(new Date(), DateUtils.YYYYMMDDHHMMSS);
            File tempDir = Files.createTempDirectory(StringConstant.EMPTY +timestamp).toFile();

            CountDownLatch latch = new CountDownLatch(totalFiles);
            List<Future<File>> futures = new ArrayList<>();

            for (int i = 0; i < totalFiles; i++) {
                int fileIndex = i;
                int startRow = i * recordsPerFile;
                int endRow = Math.min((i + 1) * recordsPerFile, (int) dataCount);

                Future<File> future = ThreadConstant.executorService.submit(() -> {
                    try {
                        return exportSingleFile(startRow, endRow, tempDir,
                                fileName + (fileIndex + 1) + StringConstant.UNDERLINE + (startRow + 1) + StringConstant.UNDERLINE + endRow + ".xlsx",
                                ssJbxx, fieldNames);
                    } finally {
                        latch.countDown(); // 任务完成，计数器减1
                    }
                });
                futures.add(future);
        }
            ExcelUtils.parallelExportToZip(response, fileName, latch, futures, timestamp, tempDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File exportSingleFile(int startRow, int endRow, File tempDir, String fileName, SsJbxx ssJbxx, List<String> fieldNames) {
        File file = new File(tempDir, fileName);

        try (OutputStream out = new FileOutputStream(file)) {
            // 分页查询数据（每次查5000条，避免内存过大）
            int pageSize = 5000;
            int currentStart = startRow;

            // 创建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(out, SsJbxxEntity.class)
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet()
                    .registerWriteHandler(new FreezeAndFilter(0, 1, 0, 1))
                    .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 50, (short) 20))
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(new BaseCellStyleStrategy())
                    .head(SsJbxxEntity.class)
                    .includeColumnFieldNames(fieldNames)
                    .build();

            while (currentStart < endRow) {
                int currentEnd = Math.min(currentStart + pageSize, endRow);
                List<SsJbxx> dataList = ssJbxxService.exportByRange(currentStart, currentEnd -currentStart);

                if (!dataList.isEmpty()) {
                    excelWriter.write(dataList, writeSheet);
                    log.debug("已写入 {} - {} 行数据", currentStart + 1, currentStart + dataList.size());
                }

                currentStart += pageSize;
                dataList.clear(); // 帮助GC
            }

            excelWriter.finish();
            return file;
        } catch (Exception e) {
            throw new RuntimeException("导出文件失败：" + fileName, e);
        }
    }

    @RequestMapping("/importBigExcel")
    public void importBigExcel(@RequestParam("file") MultipartFile file) {
        ssJbxxService.importBigExcel(file);
    }
}