package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysRoleMenuEntity {

	@ExcelProperty("角色UUID")
	private String roleUuid;

	@ExcelProperty("菜单UUID")
	private String menuUuid;
}
