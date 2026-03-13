package com.pickle.sys.bean.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
* @ClassName: SsJbxx
* @Description: ss_jbxx（赛事信息表）
* @author: wanglong
* @date 2025-09-17 14:18:43
*/

@Data
public class SsJbxxEntity {
    /**
     * 赛事日期
     */
    @ExcelProperty("赛事日期")
    @DateTimeFormat("yyyy-MM-dd")
    private Date ssRq;
    /**
     * 主办方名称
     */
    @ExcelProperty("主办方名称")
    private String zbfMc;

    /**
     * 赛事名称
     */
    @ExcelProperty("赛事名称")
    private String ssMc;

    /**
     * 投资方名称
     */
    @ExcelProperty("投资方名称")
    private String tzfMc;

    /**
     * 规章制度
     */
    @ExcelProperty("规章制度")
    private String gzZd;
    /**
     * 赛事预算金额
     */
    @ExcelProperty("赛事预算金额")
    private BigDecimal ssYsJe;

}