package com.pickle.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.pickle.utils.constant.StringConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用Excel导入校验监听器
 */
public abstract class BaseValidationListener<T> extends AnalysisEventListener<T> {
    protected List<String> errorMessages = new ArrayList<>();
    protected int rowIndex = 0; // 从表头下一行开始
    protected List<T> validDataList = new ArrayList<>();
    protected String error;

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        rowIndex++; // 表头行
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        rowIndex++;
        List<String> errors = validateRow(data, rowIndex);
        
        if (errors.isEmpty()) {
            validDataList.add(data);
        } else {
            errorMessages.addAll(errors);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 分析结束
    }

    /**
     * 校验单行数据 - 子类必须实现
     */
    protected abstract List<String> validateRow(T entity, int rowNum);

    /**
     * 通用非空校验方法
     */
    protected void validateRequiredField(Object fieldValue, String fieldName, int rowNum, List<String> errors) {
        if (fieldValue == null || 
            (fieldValue instanceof String && StringUtils.isBlank((String) fieldValue))) {
            errors.add("第 " + rowNum + " 行：" + fieldName + "不能为空");
        }
    }

    /**
     * 通用字段长度校验
     */
    protected void validateFieldLength(String fieldValue, String fieldName, int maxLength, int rowNum, List<String> errors) {
        if (StringUtils.isNotBlank(fieldValue) && fieldValue.length() > maxLength) {
            errors.add("第 " + rowNum + " 行：" + fieldName + "长度不能超过" + maxLength + "个字符");
        }
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public List<T> getValidDataList() {
        return validDataList;
    }

    public boolean hasErrors() {
        return !errorMessages.isEmpty();
    }

    public String getError() {
        if (StringUtils.isBlank(error)){
            StringBuilder builder = new StringBuilder();
            for (String errorMessage : errorMessages) {
                builder.append(errorMessage).append(StringConstant.SEMICOLIN);
            }
            error = String.valueOf(builder);
        }
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void clear() {
        errorMessages.clear();
        validDataList.clear();
        rowIndex = 1;
    }
}