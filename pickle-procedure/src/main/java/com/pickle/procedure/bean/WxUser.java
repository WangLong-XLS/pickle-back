package com.pickle.procedure.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName: WxUser
* @Description: wx_user（用户表）
* @author: wanglong
* @date 2026-05-21 20:31:12
*/

@Data
public class WxUser extends BaseBean implements Serializable {
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
     * 用户账号
     */
    @Length(max = 50, message = "用户账号长度不能超过50")
    private String userCode;

    /**
     * 用户头像
     */
    @Length(max = 200, message = "用户头像长度不能超过200")
    private String userImage;

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
     * 小程序登录code
     */
    @Length(max = 32, message = "小程序登录code长度不能超过32")
    private String wxCode;

    /**
     * 小程序返回openid
     */
    @Length(max = 32, message = "小程序返回openid长度不能超过32")
    private String openid;

    /**
     * 小程序登录返回session_key
     */
    @Length(max = 50, message = "小程序登录返回session_key长度不能超过50")
    private String sessionKey;

    private static final long serialVersionUID = 1L;

    private String token;

    /**
     * 用户idIn
     */
    private List<String> userUuidIn;

    /**
     * 用户性别名称
     */
    private String userSexMc;

    /**
     * 上传方式
     */
    private String scFs;
}