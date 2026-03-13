package com.pickle.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    // 线程池配置：CPU核心数 + 1
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static ExcelWriter easyExcelExport(HttpServletResponse response, String fileName){
        try {
            String name = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=" + name + ".xlsx");

            return EasyExcel.write(response.getOutputStream()).build();
        } catch (IOException e) {
            logger.error("操作异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static ExcelWriter easyExcelExport(HttpServletResponse response){
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return EasyExcel.write(response.getOutputStream()).build();
        } catch (IOException e) {
            logger.error("操作异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param response  res
     * @param fileName  文件名称
     * @param latch     CountDownLatch线程
     * @param futures   已生成文件
     * @param timestamp 时间戳
     * @param tempDir   文件目录
     */
    public static void parallelExportToZip(HttpServletResponse response, String fileName, CountDownLatch latch,
                                           List<Future<File>> futures, String timestamp, File tempDir) {
        try {


            // 等待所有导出任务完成（最多等待30分钟）
            boolean completed = latch.await(30, TimeUnit.MINUTES);
            if (!completed) {
                throw new RuntimeException("导出超时（30分钟）");
            }

            // 收集所有生成的文件
            List<File> exportedFiles = new ArrayList<>();
            for (Future<File> future : futures) {
                exportedFiles.add(future.get());
            }

            // 打包成ZIP
            File zipFile = createZipFile(exportedFiles, tempDir, timestamp, fileName);

            // 输出ZIP文件到浏览器
            downloadZipFile(response, zipFile, timestamp, fileName);

            logger.info("并行导出完成，共生成 {} 个文件，ZIP大小：{} MB", exportedFiles.size(), zipFile.length() / (1024 * 1024));

        } catch (Exception e) {
            logger.error("并行导出失败", e);
            try {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("导出失败：" + e.getMessage());
            } catch (IOException ex) {
                logger.error("写入错误信息失败", ex);
            }
        } finally {
            // 清理临时文件
            if (tempDir != null) {
//                scheduleCleanup(tempDir);
            }
        }
    }

    /**
     * 创建ZIP文件
     */
    private static File createZipFile(List<File> files, File tempDir, String timestamp, String fileName) throws IOException {
        File zipFile = new File(tempDir, fileName+ timestamp + ".zip");

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            byte[] buffer = new byte[8192];

            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    // 创建ZIP条目
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    // 写入文件内容
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }

                    zos.closeEntry();
                }
            }
        }

        logger.info("ZIP文件创建完成：{}", zipFile.getAbsolutePath());
        return zipFile;
    }

    /**
     * 下载ZIP文件到浏览器
     */
    private static void downloadZipFile(HttpServletResponse response, File zipFile, String timestamp, String fileName) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(fileName +timestamp +".zip", StandardCharsets.UTF_8) + "\"");
        response.setContentLengthLong(zipFile.length());

        try (FileInputStream fis = new FileInputStream(zipFile);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        }
    }

    /**
     * 定时清理临时文件
     */
    private static void scheduleCleanup(File tempDir) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
                if (tempDir.exists()) {
                    FileUtils.deleteDirectory(tempDir);
                    logger.info("临时目录已清理：{}", tempDir.getAbsolutePath());
                }
            } catch (Exception e) {
                logger.error("清理临时文件失败", e);
            }
        }, 1, TimeUnit.SECONDS);
        scheduler.shutdown();
    }


}
