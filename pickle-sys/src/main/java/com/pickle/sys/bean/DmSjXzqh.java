package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* @ClassName: DmSjXzqh
* @Description: dm_sj_xzqh（省级行政区划）
* @author: wanglong
* @date 2026-05-26 14:20:18
*/

@Data
public class DmSjXzqh extends BaseBean implements Serializable {
    /**
     * 省级行政区划代码
     */
    private String sjXzqhDm;

    /**
     * 省级行政区划名称
     */
    @NotNull(message = "省级行政区划名称不能为空")
    @Length(max = 50, message = "省级行政区划名称长度不能超过50")
    private String sjXzqhMc;

    /**
     * 选用标志
     */
    @NotNull(message = "选用标志不能为空")
    @Length(max = 2, message = "选用标志长度不能超过2")
    private String xyBz;

    /**
     * 有效标志
     */
    @NotNull(message = "有效标志不能为空")
    @Length(max = 2, message = "有效标志长度不能超过2")
    private String yxBz;

    /**
     * 有效起始日期
     */
    private Date yxqsRq;

    /**
     * 有效终止日期
     */
    private Date yxzzRq;

    private static final long serialVersionUID = 1L;

    //市级列表
    private List<DmShijXzqh> dmShijXzqhList;
}