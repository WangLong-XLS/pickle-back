package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName: SysMenu
* @Description: sys_menu（系统菜单表）
* @author: wanglong
* @date 2026-06-18 10:57:30
*/

@Data
public class SysMenu extends BaseBean implements Serializable {
    /**
     * 菜单UUID
     */
    private String menuUuid;

    /**
     * 菜单名称
     */
    @Length(max = 50, message = "菜单名称长度不能超过50")
    private String menuName;

    /**
     * 路由路径，如 /sysUser
     */
    @Length(max = 100, message = "路由路径，如 /sysUser长度不能超过100")
    private String menuPath;

    /**
     * Element Plus 图标名称
     */
    @Length(max = 50, message = "Element Plus 图标名称长度不能超过50")
    private String menuIcon;

    /**
     * 父菜单ID，顶级为NULL
     */
    @Length(max = 32, message = "父菜单ID，顶级为NULL长度不能超过32")
    private String parentUuid;

    /**
     * 排序号，越小越靠前
     */
    @Length(max = 2, message = "排序号，越小越靠前长度不能超过2")
    private String menuOrder;

    /**
     * 类型：0=菜单项，1=子菜单分组
     */
    @Length(max = 2, message = "类型：SYS_CD_LX长度不能超过2")
    private String menuType;

    /**
     * 是否显示：Y/N
     */
    @Length(max = 1, message = "是否显示：Y/N长度不能超过1")
    private String visible;

    /**
     * Vue组件路径，如 views/sys/SysUserView.vue
     */
    @Length(max = 200, message = "Vue组件路径，如 views/sys/SysUserView.vue长度不能超过200")
    private String component;

    private static final long serialVersionUID = 1L;

    //菜单IdIn
    private List<String> menuUuidIn;

    //角色IdIn
    private List<String> roleUuidIn;

    //树结构列表
    private List<SysMenu> sysMenuIn;

    //菜单类型名称
    private String menuTypeName;

    //是否显示名称
    private String visibleName;

}