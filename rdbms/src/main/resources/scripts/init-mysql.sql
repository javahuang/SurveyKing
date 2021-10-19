/*

 Source Server         : mysql5-local
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : localhost:3306
 Source Schema         : surveyking

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 14/10/2021 23:24:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `user_type` varchar(100) NOT NULL DEFAULT 'SysUser' COMMENT '用户类型',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `auth_type` varchar(20) NOT NULL DEFAULT 'PWD' COMMENT '认证方式',
  `auth_account` varchar(100) NOT NULL COMMENT '用户名',
  `auth_secret` varchar(64) DEFAULT NULL COMMENT '密码',
  `secret_salt` varchar(32) DEFAULT NULL COMMENT '加密盐',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录账号';

-- ----------------------------
-- Table structure for t_answer
-- ----------------------------
DROP TABLE IF EXISTS `t_answer`;
CREATE TABLE `t_answer` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `short_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `answer` text COLLATE utf8mb4_bin COMMENT '问卷答案',
  `attachment` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '问卷元数据',
  `meta_info` text COLLATE utf8mb4_bin COMMENT '问卷元数据',
  `temp_save` int(11) DEFAULT NULL COMMENT '0暂存 1已完成',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='答案';

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `original_name` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `file_name` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `file_path` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL,
  `thumb_file_path` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL,
  `storage_type` int(11) DEFAULT NULL,
  `shared` int(11) DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='附件';

-- ----------------------------
-- Records of t_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `short_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目名称',
  `survey` longtext COLLATE utf8mb4_bin COMMENT '问卷',
  `setting` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '问卷设置',
  `status` int(11) DEFAULT '0' COMMENT '0未发布 1已发布',
  `belong_group` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `short_id` (`short_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='项目';

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `authority` varchar(3000) DEFAULT NULL COMMENT '权限列表',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1448917371220783106 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Table structure for t_template
-- ----------------------------
DROP TABLE IF EXISTS `t_template`;
CREATE TABLE `t_template` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '模板标题',
  `question_type` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '问题类型',
  `template` longtext COLLATE utf8mb4_bin COMMENT '模板',
  `category` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '模板分类',
  `tag` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标签',
  `priority` int(11) DEFAULT NULL COMMENT '排序优先级',
  `preview_url` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '预览地址',
  `shared` int(11) DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of t_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '真实姓名',
  `gender` varchar(10) NOT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT 'Email',
  `avatar_url` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1448313054205849602 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_type` varchar(100) NOT NULL DEFAULT 'SysUser' COMMENT '用户类型',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_t_user_role` (`user_type`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1448313054692388867 DEFAULT CHARSET=utf8 COMMENT='用户角色关联';

SET FOREIGN_KEY_CHECKS = 1;
