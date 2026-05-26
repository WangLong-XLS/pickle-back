package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
* @ClassName: DmXjXzqh
* @Description: dm_xj_xzqh（县级行政区划）
* @author: wanglong
* @date 2026-05-26 14:23:05
*/

@Data
public class DmXjXzqh extends BaseBean implements Serializable {
    /**
     * 县级行政区划代码
     */
    private String xjXzqhDm;

    /**
     * 县级行政区划名称
     */
    @NotNull(message = "县级行政区划名称不能为空")
    @Length(max = 50, message = "县级行政区划名称长度不能超过50")
    private String xjXzqhMc;

    /**
     * 市级行政区划代码
     */
    @NotNull(message = "市级行政区划代码不能为空")
    @Length(max = 6, message = "市级行政区划代码长度不能超过6")
    private String shijXzqh;

    /**
     * 选用标志
     */
    @NotNull(message = "选用标志不能为空")
    @Length(max = 1, message = "选用标志长度不能超过1")
    private String xyBz;

    /**
     * 有效标志
     */
    @NotNull(message = "有效标志不能为空")
    @Length(max = 1, message = "有效标志长度不能超过1")
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
}