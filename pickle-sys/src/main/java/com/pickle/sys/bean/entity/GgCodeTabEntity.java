package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class GgCodeTabEntity {

	@ExcelProperty("编码")
	private String code;

	@ExcelProperty("名称")
	private String name;

	@ExcelProperty("码表类型")
	private String codeType;

	@ExcelProperty("父级主键")
	private String parentUuid;

	@ExcelProperty("排序值")
	private Long sortValue;
}
