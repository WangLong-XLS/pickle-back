package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysUserEntity {

	@ExcelProperty("用户名")
	private String userName;

	@ExcelProperty("用户名密码")
	private String userPassword;

	@ExcelProperty("用户年龄")
	private String userAge;

	@ExcelProperty("用户性别")
	private String userSex;

	@ExcelProperty("用户手机号")
	private String userPhone;

	@ExcelProperty("角色ID")
	private String roleUuid;

	@ExcelProperty("机构代码")
	private String orgCode;
}
