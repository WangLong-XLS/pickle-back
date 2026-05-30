package com.pickle.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tika.metadata.HttpHeaders;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

// 修改 Builder 类，去掉类型限制
public class ExcelExportBuilder {

    private HttpServletResponse response;
    private String fileName;
    private List<?> dataList;  // 改为通配符类型
    private Class<?> clazz;     // 改为通配符类型
    private List<String> includeFieldNames;
    private Integer freezePaneColSplit = 0;
    private Integer freezePaneRowSplit = 1;

    public ExcelExportBuilder response(HttpServletResponse response) {
        this.response = response;
        return this;
    }

    public ExcelExportBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ExcelExportBuilder dataList(List<?> dataList) {
        this.dataList = dataList;
        return this;
    }

    public ExcelExportBuilder clazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public ExcelExportBuilder includeFields(List<String> fields) {
        this.includeFieldNames = fields;
        return this;
    }

    public ExcelExportBuilder includeFields(String... fields) {
        this.includeFieldNames = Arrays.asList(fields);
        return this;
    }

    public ExcelExportBuilder freezePane(int colSplit, int rowSplit) {
        this.freezePaneColSplit = colSplit;
        this.freezePaneRowSplit = rowSplit;
        return this;
    }

    public void export() {
        try {
            String encodedName = URLEncoder.encode(fileName, "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename*=" + encodedName + ".xlsx");

            // 构建导出
            ExcelWriter writer = EasyExcel.write(response.getOutputStream(), clazz).build();
            WriteSheet sheet = EasyExcel.writerSheet()
                    .registerWriteHandler(new FreezeAndFilter(freezePaneColSplit, freezePaneRowSplit, 0, 1))
                    .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 50, (short) 20))
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(new BaseCellStyleStrategy())
                    .head(clazz)
                    .includeColumnFieldNames(includeFieldNames)
                    .build();

            writer.write(dataList, sheet);
            writer.finish();
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }
}

