package com.pickle.procedure.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* @ClassName: WxCdCcxx
* @Description: wx_cd_ccxx（微信场次信息表）
* @author: wanglong
* @date 2026-05-24 16:51:16
*/

@Data
public class WxCdCcxx extends BaseBean implements Serializable {
    /**
     * 场次信息id
     */
    private String ccyyUuid;

    /**
     * 场地信息id
     */
    @Length(max = 32, message = "场地信息id长度不能超过32")
    private String cdxxUuid;

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
     * 是否黄金天
     */
    @Length(max = 1, message = "是否黄金天长度不能超过1")
    private String hjtBz;

    /**
     * 排序号
     */
    @Length(max = 2, message = "排序号长度不能超过2")
    private String pxh;

    private static final long serialVersionUID = 1L;

    /**
     * 场地名称
     */
    private String cdMc;

    /**
     * 场地规定人数
     */
    private BigDecimal cdGdRs;

    //当前日期
    private Date dqRq;
}