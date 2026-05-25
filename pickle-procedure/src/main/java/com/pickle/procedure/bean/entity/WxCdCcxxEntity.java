package com.pickle.procedure.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class WxCdCcxxEntity {

	@ExcelProperty("场地信息id")
	private String cdxxUuid;

	@ExcelProperty("场次时段")
	private String ccSd;

	@ExcelProperty("场地单价")
	private BigDecimal cdDj;

	@ExcelProperty("是否黄金天")
	private String hjtBz;

	@ExcelProperty("排序号")
	private String pxh;
}
