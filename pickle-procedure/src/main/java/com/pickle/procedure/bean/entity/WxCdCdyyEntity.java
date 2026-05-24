package com.pickle.procedure.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class WxCdCdyyEntity {

	@ExcelProperty("场地号")
	private String cdh;

	@ExcelProperty("场次时段")
	private String ccSd;

	@ExcelProperty("场地单价")
	private BigDecimal cdDj;

	@ExcelProperty("场地规定人数")
	private BigDecimal cdGdRs;

	@ExcelProperty("是否黄金天")
	private String hjtBz;

	@ExcelProperty("排序号")
	private String pxh;

	@ExcelProperty("小程序返回openid")
	private String openid;

	@ExcelProperty("小程序登录返回session_key")
	private String sessionKey;
}
