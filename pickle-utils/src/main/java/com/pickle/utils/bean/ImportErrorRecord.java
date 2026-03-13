package com.pickle.utils.bean;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ImportErrorRecord {
    private int rowNum;           // 行号
    private String data;           // 错误的数据（可选）
    private String errorMessage;    // 错误信息
}