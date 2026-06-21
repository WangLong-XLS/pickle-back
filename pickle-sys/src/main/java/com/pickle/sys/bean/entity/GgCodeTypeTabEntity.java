package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class GgCodeTypeTabEntity {

	@ExcelProperty("编码")
	private String code;

	@ExcelProperty("名称")
	private String name;

	@ExcelProperty("是否树")
	private String isTree;

	@ExcelProperty("是否区间")
	private String isInterval;

	@ExcelProperty("是否是系统预制")
	private String isSystem;

	@ExcelProperty("模型")
	private String moduleId;
}
