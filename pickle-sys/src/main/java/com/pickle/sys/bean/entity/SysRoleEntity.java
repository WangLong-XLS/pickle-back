package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysRoleEntity {

	@ExcelProperty("角色编码")
	private String roleCode;

	@ExcelProperty("角色名称")
	private String roleName;

	@ExcelProperty("角色描述")
	private String roleDesc;

	@ExcelProperty("公用标志Y/N")
	private String pubFlg;

	@ExcelProperty("行政机构UUID")
	private String orgUuid;
}
