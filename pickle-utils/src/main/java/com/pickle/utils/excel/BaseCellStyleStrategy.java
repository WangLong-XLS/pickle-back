package com.pickle.utils.excel;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

public class BaseCellStyleStrategy extends HorizontalCellStyleStrategy {

    public BaseCellStyleStrategy() {
        super(buildHeadWriteCellStyle(), buildContentWriteCellStyle());
    }

    /**
     * 构建表头样式
     */
    private static WriteCellStyle buildHeadWriteCellStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();

        // 设置对齐方式
        headWriteCellStyle.setWrapped(true);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // 设置锁定（保护工作表时有效）
        headWriteCellStyle.setLocked(true);

        // 设置背景色
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());

        // 设置边框
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);

        // 设置字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);

        return headWriteCellStyle;
    }

    /**
     * 构建内容样式
     */
    private static WriteCellStyle buildContentWriteCellStyle() {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

        // 设置对齐方式 - 内容也居中对齐
        contentWriteCellStyle.setWrapped(true);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // 设置边框 - 内容也加边框
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);

        // 设置字体
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        return contentWriteCellStyle;
    }
}