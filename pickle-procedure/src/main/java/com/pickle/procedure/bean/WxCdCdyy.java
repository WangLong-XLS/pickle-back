package com.pickle.procedure.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @ClassName: WxCdCdyy
* @Description: wx_cd_cdyy（微信场地预约表）
* @author: wanglong
* @date 2026-05-23 18:05:45
*/

@Data
public class WxCdCdyy extends BaseBean implements Serializable {
    /**
     * 场地预约id
     */
    private String cdyyUuid;

    /**
     * 场地号
     */
    @Length(max = 10, message = "场地号长度不能超过10")
    private String cdh;

    /**
     * 场次时段
     */
    @Length(max = 20, message = "场次时段长度不能超过20")
    private String ccSd;

    /**
     * 场地单价
     */
    @Length(max = 28, message = "场地单价长度不能超过28")
    private BigDecimal cdDj;

    /**
     * 场地规定人数
     */
    @Length(max = 28, message = "场地规定人数长度不能超过28")
    private BigDecimal cdGdRs;

    /**
     * 是否黄金天
     */
    @Length(max = 1, message = "是否黄金天长度不能超过1")
    private String hjtBz;

    /**
     * 排序号
     */
    @Length(max = 2, message = "排序号长度不能超过2")
    private String pxh;

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
}