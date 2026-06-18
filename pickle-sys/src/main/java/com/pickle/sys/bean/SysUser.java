package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName: SysUser
* @Description: sys_user（用户表）
* @author: wanglong
* @date 2026-05-26 00:01:17
*/

@Data
public class SysUser extends BaseBean implements Serializable {
    /**
     * 用户id
     */
    private String userUuid;

    /**
     * 用户名
     */
    @Length(max = 50, message = "用户名长度不能超过50")
    private String userName;

    /**
     * 用户名密码
     */
    @Length(max = 200, message = "用户名密码长度不能超过200")
    private String userPassword;

    /**
     * 用户年龄
     */
    @Length(max = 3, message = "用户年龄长度不能超过3")
    private String userAge;

    /**
     * 用户性别
     */
    @Length(max = 2, message = "用户性别长度不能超过2")
    private String userSex;

    /**
     * 用户手机号
     */
    @Length(max = 11, message = "用户手机号长度不能超过11")
    private String userPhone;

    /**
     * 角色ID
     */
    @Length(max = 32, message = "角色ID长度不能超过32")
    private String roleUuid;

    /**
     * 机构代码
     */
    @Length(max = 32, message = "机构代码长度不能超过32")
    private String orgCode;

    private static final long serialVersionUID = 1L;

    private String token;

    /**
     * 用户idIn
     */
    private List<String> userUuidIn;

    //用户性别名称
    private String userSexName;

    //机构名称
    private String orgName;

    //角色名称
    private String roleName;

    /**
     * 角色权限IDIn
     */
    private List<String> roleUuidIn;

    /**
     * 机构权限IDIn
     */
    private List<String> orgUuidIn;
}