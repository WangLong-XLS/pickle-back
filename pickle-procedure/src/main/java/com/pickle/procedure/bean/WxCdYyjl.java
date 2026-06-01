package com.pickle.procedure.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @ClassName: WxCdYyjl
* @Description: wx_cd_yyjl（微信场地预约记录表）
* @author: wanglong
* @date 2026-05-24 16:51:48
*/

@Data
public class WxCdYyjl extends BaseBean implements Serializable {
    /**
     * 场地预约记录id
     */
    private String yyjlUuid;

    /**
     * 微信用户id
     */
    @Length(max = 32, message = "微信用户id长度不能超过32")
    private String userUuid;

    /**
     * 场次信息id
     */
    @Length(max = 32, message = "场次信息id长度不能超过32")
    private String ccyyUuid;

    /**
     * 预约日期
     */
    private Date yyRq;

    /**
     * 预约人数
     */
    private BigDecimal yyRs;

    private static final long serialVersionUID = 1L;

    private List<String> yyjlUuidIn;
}