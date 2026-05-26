package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class DmSjXzqhEntity {

	@ExcelProperty("省级行政区划名称")
	private String sjXzqhMc;

	@ExcelProperty("选用标志")
	private String xyBz;

	@ExcelProperty("有效标志")
	private String yxBz;

	@ExcelProperty("有效起始日期")
	private Date yxqsRq;

	@ExcelProperty("有效终止日期")
	private Date yxzzRq;
}
