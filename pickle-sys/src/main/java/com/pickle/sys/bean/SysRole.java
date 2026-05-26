package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
* @ClassName: SysRole
* @Description: sys_role（角色）
* @author: wanglong
* @date 2026-05-26 13:29:23
*/

@Data
public class SysRole extends BaseBean implements Serializable {
    /**
     * 角色UUID
     */
    private String roleUuid;

    /**
     * 角色编码
     */
    @Length(max = 50, message = "角色编码长度不能超过50")
    private String roleCode;

    /**
     * 角色名称
     */
    @NotNull(message = "角色ID不能为空")
    @Length(max = 50, message = "角色名称长度不能超过50")
    private String roleName;

    /**
     * 角色描述
     */
    @Length(max = 200, message = "角色描述长度不能超过200")
    private String roleDesc;

    /**
     * 公用标志Y/N
     */
    @Length(max = 2, message = "公用标志Y/N长度不能超过2")
    private String pubFlg;

    /**
     * 行政机构UUID
     */
    @Length(max = 32, message = "行政机构UUID长度不能超过32")
    private String orgUuid;

    private static final long serialVersionUID = 1L;

    /**
     * 角色UUIDIn
     */
    private List<String> roleUuidIn;

    /**
     * 公用标志Y/N名称
     */
    private String pubFlgName;
}