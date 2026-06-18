package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* @ClassName: SysUserRole
* @Description: sys_user_role（用户角色管理表）
* @author: wanglong
* @date 2026-06-18 01:28:41
*/

@Data
public class SysUserRole extends BaseBean implements Serializable {
    /**
     * 用户角色关联UUID
     */
    private String userRoleUuid;

    /**
     * 用户UUID
     */
    @Length(max = 32, message = "用户UUID长度不能超过32")
    private String userUuid;

    /**
     * 角色UUID
     */
    @Length(max = 32, message = "角色UUID长度不能超过32")
    private String roleUuid;

    private static final long serialVersionUID = 1L;
}