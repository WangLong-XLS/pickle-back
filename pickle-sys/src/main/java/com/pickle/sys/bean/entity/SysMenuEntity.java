package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysMenuEntity {

	@ExcelProperty("菜单名称")
	private String menuName;

	@ExcelProperty("路由路径，如 /sysUser")
	private String menuPath;

	@ExcelProperty("Element Plus 图标名称")
	private String menuIcon;

	@ExcelProperty("父菜单ID，顶级为NULL")
	private String parentUuid;

	@ExcelProperty("排序号，越小越靠前")
	private String menuOrder;

	@ExcelProperty("类型：0=菜单项，1=子菜单分组")
	private String menuType;

	@ExcelProperty("是否显示：Y/N")
	private String visible;

	@ExcelProperty("Vue组件路径，如 views/sys/SysUserView.vue")
	private String component;
}
