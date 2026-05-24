/*
 Navicat Premium Data Transfer

 Source Server         : 本地8.0
 Source Server Type    : MySQL
 Source Server Version : 80043
 Source Host           : localhost:3306
 Source Schema         : pickle

 Target Server Type    : MySQL
 Target Server Version : 80043
 File Encoding         : 65001

 Date: 24/05/2026 12:28:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gg_fj
-- ----------------------------
DROP TABLE IF EXISTS `gg_fj`;
CREATE TABLE `gg_fj`  (
                          `FJ_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '附件UUID',
                          `YW_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务UUID',
                          `FJ_MC` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称',
                          `FJ_LJ` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
                          `FL` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类',
                          `WJ_GS` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件格式',
                          `WJ_DX` int(0) NULL DEFAULT NULL COMMENT '文件大小(kb)',
                          `SC_FS` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传方式',
                          `AJH` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '案卷号',
                          `CJ_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人员代码',
                          `CJ_SJ` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                          `XG_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员代码',
                          `XG_SJ` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                          `SJGS_JG_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据归属机构代码',
                          `ZF_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '作废标志',
                          `ZF_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废人员代码',
                          `ZF_SJ` datetime(0) NULL DEFAULT NULL COMMENT '作废时间',
                          PRIMARY KEY (`FJ_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公共附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_jbxx
-- ----------------------------
DROP TABLE IF EXISTS `ss_jbxx`;
CREATE TABLE `ss_jbxx`  (
                            `SS_JBXX_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '赛事基本信息',
                            `ZBF_MC` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主办方名称',
                            `TZF_MC` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '投资方名称',
                            `SS_MC` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赛事名称',
                            `SS_YS_JE` decimal(28, 8) NULL DEFAULT NULL COMMENT '赛事预算金额',
                            `GZ_ZD` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规章制度',
                            `SS_RQ` date NULL DEFAULT NULL COMMENT '赛事日期',
                            `CJ_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人员代码',
                            `CJ_SJ` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                            `XG_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员代码',
                            `XG_SJ` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                            `SJGS_JG_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据归属机构代码',
                            `ZF_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '作废标志',
                            `ZF_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废人员代码',
                            `ZF_SJ` datetime(0) NULL DEFAULT NULL COMMENT '作废时间',
                            PRIMARY KEY (`SS_JBXX_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '赛事信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `USER_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                             `USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                             `USER_PASSWORD` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名密码',
                             `USER_AGE` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户年龄',
                             `USER_SEX` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户性别',
                             `USER_PHONE` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
                             `ROLE_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色ID',
                             `ORG_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '机构代码',
                             `CJ_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人员代码',
                             `CJ_SJ` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                             `XG_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员代码',
                             `XG_SJ` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                             `SJGS_JG_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据归属机构代码',
                             `ZF_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '作废标志',
                             `ZF_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废人员代码',
                             `ZF_SJ` datetime(0) NULL DEFAULT NULL COMMENT '作废时间',
                             PRIMARY KEY (`USER_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_cd_ccxx
-- ----------------------------
DROP TABLE IF EXISTS `wx_cd_ccxx`;
CREATE TABLE `wx_cd_ccxx`  (
                               `CCYY_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '场次信息id',
                               `CC_SD` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场次时段',
                               `CD_DJ` decimal(28, 8) NULL DEFAULT NULL COMMENT '场地单价',
                               `HJT_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否黄金天',
                               `PXH` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排序号',
                               `CJ_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人员代码',
                               `CJ_SJ` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                               `XG_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员代码',
                               `XG_SJ` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                               `SJGS_JG_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据归属机构代码',
                               `ZF_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '作废标志',
                               `ZF_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废人员代码',
                               `ZF_SJ` datetime(0) NULL DEFAULT NULL COMMENT '作废时间',
                               PRIMARY KEY (`CCYY_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信场次信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_cd_cdxx
-- ----------------------------
DROP TABLE IF EXISTS `wx_cd_cdxx`;
CREATE TABLE `wx_cd_cdxx`  (
                               `CDXX_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '场地信息id',
                               `CD_MC` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场地名称',
                               `CD_GD_RS` decimal(28, 8) NULL DEFAULT NULL COMMENT '场地规定人数',
                               `BZ` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                               `CJ_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人员代码',
                               `CJ_SJ` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                               `XG_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员代码',
                               `XG_SJ` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                               `SJGS_JG_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据归属机构代码',
                               `ZF_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '作废标志',
                               `ZF_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废人员代码',
                               `ZF_SJ` datetime(0) NULL DEFAULT NULL COMMENT '作废时间',
                               PRIMARY KEY (`CDXX_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信场地信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_cd_yyjl
-- ----------------------------
DROP TABLE IF EXISTS `wx_cd_yyjl`;
CREATE TABLE `wx_cd_yyjl`  (
                               `YYJL_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '场地预约记录id',
                               `USER_UUID` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信用户id',
                               `CCYY_UUID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场次信息id',
                               `YY_RQ` date NULL DEFAULT NULL COMMENT '预约日期',
                               `YY_RS` decimal(28, 8) NULL DEFAULT NULL COMMENT '预约人数',
                               `CJ_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人员代码',
                               `CJ_SJ` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                               `XG_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员代码',
                               `XG_SJ` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                               `SJGS_JG_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据归属机构代码',
                               `ZF_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '作废标志',
                               `ZF_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废人员代码',
                               `ZF_SJ` datetime(0) NULL DEFAULT NULL COMMENT '作废时间',
                               PRIMARY KEY (`YYJL_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信场地预约记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user`  (
                            `USER_UUID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                            `USER_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                            `USER_CODE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号',
                            `USER_IMAGE` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
                            `USER_SEX` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户性别',
                            `USER_PHONE` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
                            `WX_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序登录code',
                            `OPENID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序返回openid',
                            `SESSION_KEY` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序登录返回session_key',
                            `CJ_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人员代码',
                            `CJ_SJ` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                            `XG_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人员代码',
                            `XG_SJ` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                            `SJGS_JG_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据归属机构代码',
                            `ZF_BZ` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '作废标志',
                            `ZF_RY_DM` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废人员代码',
                            `ZF_SJ` datetime(0) NULL DEFAULT NULL COMMENT '作废时间',
                            PRIMARY KEY (`USER_UUID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
