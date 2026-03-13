package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @ClassName: SsJbxx
* @Description: ss_jbxx（赛事信息表）
* @author: wanglong
* @date 2025-09-17 14:18:43
*/

@Data
public class SsJbxx extends BaseBean implements Serializable {
    /**
     * 赛事基本信息
     */
    private String ssJbxxUuid;

    /**
     * 主办方名称
     */
    private String zbfMc;

    /**
     * 投资方名称
     */
    private String tzfMc;

    /**
     * 赛事名称
     */
    private String ssMc;

    /**
     * 赛事预算金额
     */
    private BigDecimal ssYsJe;

    /**
     * 规章制度
     */
    private String gzZd;

    /**
     * 赛事日期
     */
    private Date ssRq;

    private static final long serialVersionUID = 1L;

    //赛事基本信息In
    private List<String> ssJbxxUuidIn;

}