package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* @ClassName: SysUserOrg
* @Description: sys_user_org（用户机构管理表）
* @author: wanglong
* @date 2026-06-18 01:28:16
*/

@Data
public class SysUserOrg extends BaseBean implements Serializable {
    /**
     * 用户机构关联UUID
     */
    private String userOrgUuid;

    /**
     * 用户UUID
     */
    @Length(max = 32, message = "用户UUID长度不能超过32")
    private String userUuid;

    /**
     * 机构UUID
     */
    @Length(max = 32, message = "机构UUID长度不能超过32")
    private String orgUuid;

    private static final long serialVersionUID = 1L;
}