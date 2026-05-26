package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* @ClassName: DmShijXzqh
* @Description: dm_shij_xzqh（市级行政区划）
* @author: wanglong
* @date 2026-05-26 14:09:12
*/

@Data
public class DmShijXzqh extends BaseBean implements Serializable {
    /**
     * 市级行政区划代码
     */
    private String shijXzqhDm;

    /**
     * 市级行政区划名称
     */
    @NotNull
    @Length(max = 50, message = "市级行政区划名称长度不能超过50")
    private String shijXzqhMc;

    /**
     * 省级行政区划代码
     */
    @Length(max = 6, message = "省级行政区划代码长度不能超过6")
    private String sjXzqh;

    /**
     * 选用标志
     */
    @NotNull
    @Length(max = 1, message = "选用标志长度不能超过1")
    private String xyBz;

    /**
     * 有效标志
     */
    @NotNull
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

    //县级列表
    private List<DmXjXzqh> dmXjXzqhList;
}