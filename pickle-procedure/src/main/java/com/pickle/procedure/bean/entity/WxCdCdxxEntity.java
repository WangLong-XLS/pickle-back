package com.pickle.procedure.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class WxCdCdxxEntity {

	@ExcelProperty("场地名称")
	private String cdMc;

	@ExcelProperty("场地规定人数")
	private BigDecimal cdGdRs;

	@ExcelProperty("备注")
	private String bz;
}
