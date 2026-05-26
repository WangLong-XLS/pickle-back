package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class SysAdOrgEntity {

	@ExcelProperty("上级机构UUID")
	private String parentOrgUuid;

	@ExcelProperty("机构代码")
	private String orgCode;

	@ExcelProperty("机构名称")
	private String orgName;

	@ExcelProperty("机构简称")
	private String orgNameShort;

	@ExcelProperty("机构英文名称")
	private String orgEngName;

	@ExcelProperty("国家地区代码")
	private String areaCd;

	@ExcelProperty("省级代码")
	private String sjDm;

	@ExcelProperty("市级代码")
	private String shijDm;

	@ExcelProperty("县级代码")
	private String xjDm;

	@ExcelProperty("详细地址")
	private String orgAdress;

	@ExcelProperty("邮编")
	private String postCode;

	@ExcelProperty("电子邮箱")
	private String email;

	@ExcelProperty("联系电话")
	private String contactTel;

	@ExcelProperty("成立日期")
	private Date foundTime;

	@ExcelProperty("法定代表人")
	private String legalPerson;

	@ExcelProperty("法人机构Y/N")
	private String isCorporate;

	@ExcelProperty("集团Y/N")
	private String isGroup;

	@ExcelProperty("所属法人机构")
	private String corpOrg;

	@ExcelProperty("所属集团")
	private String orgStatus;

	@ExcelProperty("机构排序号")
	private Long sortNo;
}
