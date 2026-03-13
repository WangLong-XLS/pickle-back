package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class GgFjEntity {

	@ExcelProperty("业务UUID")
	private String ywUuid;

	@ExcelProperty("附件名称")
	private String fjMc;

	@ExcelProperty("附件路径")
	private String fjLj;

	@ExcelProperty("分类")
	private String fl;

	@ExcelProperty("文件格式")
	private String wjGs;

	@ExcelProperty("文件大小(kb)")
	private Integer wjDx;

	@ExcelProperty("上传方式")
	private String scFs;

	@ExcelProperty("案卷号")
	private String ajh;
}
