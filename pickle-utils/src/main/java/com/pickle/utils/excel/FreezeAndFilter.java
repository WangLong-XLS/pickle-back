package com.pickle.utils.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author lvguangming
 * @version 1.0
 * @description 指定单元格位置冻结表哥
 * @date 2023/12/18 18:32
 */
public class FreezeAndFilter implements SheetWriteHandler {

    private final int colSplit;
    private final int rowSplit;
    private final int leftmostColumn;
    private final int topRow;

    public FreezeAndFilter(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        this.colSplit = colSplit;
        this.rowSplit = rowSplit;
        this.leftmostColumn = leftmostColumn;
        this.topRow = topRow;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
    }
}