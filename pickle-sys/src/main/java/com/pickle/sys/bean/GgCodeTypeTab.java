package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName: GgCodeTypeTab
* @Description: gg_code_type_tab（码表类型）
* @author: wanglong
* @date 2026-06-20 14:02:34
*/

@Data
public class GgCodeTypeTab extends BaseBean implements Serializable {
    /**
     * 主键
     */
    private String codeTypeTabUuid;

    /**
     * 编码
     */
    @Length(max = 30, message = "编码长度不能超过30")
    private String code;

    /**
     * 名称
     */
    @Length(max = 50, message = "名称长度不能超过50")
    private String name;

    /**
     * 是否树
     */
    @Length(max = 1, message = "是否树长度不能超过1")
    private String isTree;

    /**
     * 是否区间
     */
    @Length(max = 1, message = "是否区间长度不能超过1")
    private String isInterval;

    /**
     * 是否是系统预制
     */
    @Length(max = 1, message = "是否是系统预制长度不能超过1")
    private String isSystem;

    /**
     * 模型
     */
    @Length(max = 20, message = "模型长度不能超过20")
    private String moduleId;

    private static final long serialVersionUID = 1L;

    //主键in
    private List<String> codeTypeTabUuidIn;

    //创建人员名称
    private String cjRyMc;

    //修改人员名称
    private String xgRyMc;

    //是否树名称
    private String isTreeMc;

    //是否区间名称
    private String isIntervalMc;
}