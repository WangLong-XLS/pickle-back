package com.pickle.procedure.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class WxCdYyjlEntity {

	@ExcelProperty("微信用户id")
	private String userUuid;

	@ExcelProperty("场次信息id")
	private String ccyyUuid;

	@ExcelProperty("预约日期")
	private Date yyRq;

	@ExcelProperty("预约人数")
	private BigDecimal yyRs;
}
