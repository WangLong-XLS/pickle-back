package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* @ClassName: SysRoleMenu
* @Description: sys_role_menu（角色菜单关联表）
* @author: wanglong
* @date 2026-06-18 10:57:12
*/

@Data
public class SysRoleMenu extends BaseBean implements Serializable {
    /**
     * 角色菜单关联UUID
     */
    private String roleMenuUuid;

    /**
     * 角色UUID
     */
    @NotNull(message = "角色UUID不能为空")
    @Length(max = 32, message = "角色UUID长度不能超过32")
    private String roleUuid;

    /**
     * 菜单UUID
     */
    @NotNull(message = "菜单UUID不能为空")
    @Length(max = 32, message = "菜单UUID长度不能超过32")
    private String menuUuid;

    private static final long serialVersionUID = 1L;
}