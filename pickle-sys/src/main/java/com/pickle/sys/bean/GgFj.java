package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
* @ClassName: GgFj
* @Description: gg_fj（公共附件表）
* @author: wanglong
* @date 2026-03-09 18:59:28
*/

@Data
public class GgFj extends BaseBean implements Serializable {
    /**
     * 附件UUID
     */
    private String fjUuid;

    /**
     * 业务UUID
     */
    @Length(max = 32, message = "业务UUID长度不能超过32")
    private String ywUuid;

    /**
     * 附件名称
     */
    @Length(max = 150, message = "附件名称长度不能超过150")
    private String fjMc;

    /**
     * 附件路径
     */
    @Length(max = 200, message = "附件路径长度不能超过200")
    private String fjLj;

    /**
     * 分类
     */
    @Length(max = 10, message = "分类长度不能超过10")
    private String fl;

    /**
     * 文件格式
     */
    @Length(max = 5, message = "文件格式长度不能超过5")
    private String wjGs;

    /**
     * 文件大小(kb)
     */
    @Length(max = 10, message = "文件大小(kb)长度不能超过10")
    private Integer wjDx;

    /**
     * 上传方式
     */
    @Length(max = 2, message = "上传方式长度不能超过2")
    private String scFs;

    /**
     * 案卷号
     */
    @Length(max = 32, message = "案卷号长度不能超过32")
    private String ajh;

    private static final long serialVersionUID = 1L;
}