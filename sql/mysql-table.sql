#sql脚本举例（sql脚本从上往下执行添加时顺序不要错了）
#2023-08-30 wanglong
#新增**表台账
#create ***
#修改**字段
#modify ***
#增加**表**字段
#alter ***
#插入**表**数据
#insert into ***

#公共字段，每张表都要有！！！
ALTER TABLE sys_user ADD COLUMN CJ_RY_DM varchar(32) NULL COMMENT '创建人员代码';
ALTER TABLE sys_user ADD COLUMN CJ_SJ datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER CJ_RY_DM;
ALTER TABLE sys_user ADD COLUMN XG_RY_DM varchar(32) NULL COMMENT '修改人员代码' AFTER CJ_SJ;
ALTER TABLE sys_user ADD COLUMN XG_SJ datetime(0) NULL COMMENT '修改时间' AFTER XG_RY_DM;
ALTER TABLE sys_user ADD COLUMN SJGS_JG_DM varchar(32) NULL COMMENT '数据归属机构代码' AFTER XG_SJ;
ALTER TABLE sys_user ADD COLUMN ZF_BZ varchar(1) NOT NULL DEFAULT 'N' COMMENT '作废标志' AFTER SJGS_JG_DM;
ALTER TABLE sys_user ADD COLUMN ZF_RY_DM varchar(32) NULL COMMENT '作废人员代码' AFTER ZF_BZ;
ALTER TABLE sys_user ADD COLUMN ZF_SJ datetime(0) NULL COMMENT '作废时间' AFTER ZF_RY_DM;
