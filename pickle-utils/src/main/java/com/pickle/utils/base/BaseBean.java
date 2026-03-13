package com.pickle.utils.base;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseBean {
    private Integer pageNo;
    private Integer pageNum;
    private Integer pageSize;

    /**
     * 创建人员代码
     */
    private String cjRyDm;

    /**
     * 创建时间
     */
    private Date cjSj;

    /**
     * 修改人员代码
     */
    private String xgRyDm;

    /**
     * 修改时间
     */
    private Date xgSj;

    /**
     * 数据归属机构代码
     */
    private String sjgsJgDm;

    /**
     * 作废人员代码
     */
    private String zfRyDm;

    /**
     * 作废时间
     */
    private Date zfSj;
}
