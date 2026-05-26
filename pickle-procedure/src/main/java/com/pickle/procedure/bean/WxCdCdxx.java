package com.pickle.procedure.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
* @ClassName: WxCdCdxx
* @Description: wx_cd_cdxx（微信场地信息表）
* @author: wanglong
* @date 2026-05-24 16:51:37
*/

@Data
public class WxCdCdxx extends BaseBean implements Serializable {
    /**
     * 场地信息id
     */
    private String cdxxUuid;

    /**
     * 场地名称
     */
    @Length(max = 10, message = "场地名称长度不能超过10")
    private String cdMc;

    /**
     * 场地规定人数
     */
    private BigDecimal cdGdRs;

    /**
     * 备注
     */
    @Length(max = 255, message = "备注长度不能超过255")
    private String bz;

    private static final long serialVersionUID = 1L;

    private List<String> cdxxUuidIn;
}