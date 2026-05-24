package com.pickle.procedure.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WxUserEntity {

	@ExcelProperty("用户名")
	private String userName;

	@ExcelProperty("用户账号")
	private String userCode;

	@ExcelProperty("用户头像")
	private String userImage;

	@ExcelProperty("用户性别")
	private String userSex;

	@ExcelProperty("用户手机号")
	private String userPhone;

	@ExcelProperty("小程序登录code")
	private String wxCode;

	@ExcelProperty("小程序返回openid")
	private String openid;

	@ExcelProperty("小程序登录返回session_key")
	private String sessionKey;
}
