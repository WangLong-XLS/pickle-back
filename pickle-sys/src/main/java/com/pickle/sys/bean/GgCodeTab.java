package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName: GgCodeTab
* @Description: gg_code_tab（码表明细）
* @author: wanglong
* @date 2026-06-20 14:01:44
*/

@Data
public class GgCodeTab extends BaseBean implements Serializable {
    /**
     * 主键
     */
    private String codeTabUuid;

    /**
     * 编码
     */
    @NotNull(message = "编码不能为空")
    @Length(max = 30, message = "编码长度不能超过30")
    private String code;

    /**
     * 名称
     */
    @Length(max = 100, message = "名称长度不能超过100")
    private String name;

    /**
     * 码表类型
     */
    @Length(max = 50, message = "码表类型长度不能超过50")
    private String codeType;

    /**
     * 父级主键
     */
    @Length(max = 50, message = "父级主键长度不能超过50")
    private String parentUuid;

    /**
     * 排序值
     */
    @Length(max = 10, message = "排序值长度不能超过10")
    private Long sortValue;

    private static final long serialVersionUID = 1L;

    //主键in
    private List<String> codeTabUuidIn;

    //创建人员名称
    private String cjRyMc;

    //修改人员名称
    private String xgRyMc;

    //父表主键in
    private List<String> codeTypeIn;
}