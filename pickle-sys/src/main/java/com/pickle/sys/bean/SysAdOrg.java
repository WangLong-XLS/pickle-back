package com.pickle.sys.bean;

import com.pickle.utils.base.BaseBean;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* @ClassName: SysAdOrg
* @Description: sys_ad_org（机构表）
* @author: wanglong
* @date 2026-05-26 13:29:13
*/

@Data
public class SysAdOrg extends BaseBean implements Serializable {
    /**
     * 机构UUID
     */
    private String orgUuid;

    /**
     * 上级机构UUID
     */
    @Length(max = 32, message = "上级机构UUID长度不能超过32")
    private String parentOrgUuid;

    /**
     * 机构代码
     */
    @Length(max = 32, message = "机构代码长度不能超过32")
    private String orgCode;

    /**
     * 机构名称
     */
    @Length(max = 50, message = "机构名称长度不能超过50")
    private String orgName;

    /**
     * 机构简称
     */
    @Length(max = 50, message = "机构简称长度不能超过50")
    private String orgNameShort;

    /**
     * 机构英文名称
     */
    @Length(max = 100, message = "机构英文名称长度不能超过100")
    private String orgEngName;

    /**
     * 国家地区代码
     */
    @Length(max = 10, message = "国家地区代码长度不能超过10")
    private String areaCd;

    /**
     * 省级代码
     */
    @Length(max = 6, message = "省级代码长度不能超过6")
    private String sjDm;

    /**
     * 市级代码
     */
    @Length(max = 6, message = "市级代码长度不能超过6")
    private String shijDm;

    /**
     * 县级代码
     */
    @Length(max = 6, message = "县级代码长度不能超过6")
    private String xjDm;

    /**
     * 详细地址
     */
    @Length(max = 255, message = "详细地址长度不能超过255")
    private String orgAdress;

    /**
     * 邮编
     */
    @Length(max = 20, message = "邮编长度不能超过20")
    private String postCode;

    /**
     * 电子邮箱
     */
    @Length(max = 30, message = "电子邮箱长度不能超过30")
    private String email;

    /**
     * 联系电话
     */
    @Length(max = 11, message = "联系电话长度不能超过11")
    private String contactTel;

    /**
     * 成立日期
     */
    private Date foundTime;

    /**
     * 法定代表人
     */
    @Length(max = 20, message = "法定代表人长度不能超过20")
    private String legalPerson;

    /**
     * 法人机构Y/N
     */
    @Length(max = 1, message = "法人机构Y/N长度不能超过1")
    private String isCorporate;

    /**
     * 集团Y/N
     */
    @Length(max = 1, message = "集团Y/N长度不能超过1")
    private String isGroup;

    /**
     * 所属法人机构
     */
    @Length(max = 32, message = "所属法人机构长度不能超过32")
    private String corpOrg;

    /**
     * 所属集团
     */
    @Length(max = 32, message = "所属集团长度不能超过32")
    private String orgStatus;

    /**
     * 机构排序号
     */
    @Length(max = 10, message = "机构排序号长度不能超过10")
    private Long sortNo;

    private static final long serialVersionUID = 1L;

    //机构UUIDIn
    private List<String> orgUuidIn;

    //省级名称
    private String sjMc;

    //市级名称
    private String shijMc;

    //县级名称
    private String xjMc;

    //法人机构Y/N
    private String isCorporateName;

    //集团Y/N
    private String isGroupName;
}