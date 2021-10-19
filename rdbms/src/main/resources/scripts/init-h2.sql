-- ----------------------------
-- Table structure for t_account
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_account` (
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
) ;
COMMENT ON TABLE `t_account` IS '登录账号';

-- ----------------------------
-- Table structure for t_answer
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_answer` (
  `id` varchar(64)  NOT NULL,
  `short_id` varchar(64)  DEFAULT NULL,
  `answer` text  COMMENT '问卷答案',
  `attachment` varchar(1024)  DEFAULT NULL COMMENT '问卷元数据',
  `meta_info` text  COMMENT '问卷元数据',
  `temp_save` int(11) DEFAULT NULL COMMENT '0暂存 1已完成',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256)  DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`id`)
);
COMMENT ON TABLE `t_answer` IS '答案';
-- ----------------------------
-- Table structure for t_file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_file` (
  `id` varchar(64)  NOT NULL,
  `original_name` varchar(256)  DEFAULT NULL,
  `file_name` varchar(256)  DEFAULT NULL,
  `file_path` varchar(512)  DEFAULT NULL,
  `thumb_file_path` varchar(512)  DEFAULT NULL,
  `storage_type` int(11) DEFAULT NULL,
  `shared` int(11) DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256)  DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`id`)
);
COMMENT ON TABLE `t_file` IS '附件';
-- ----------------------------
-- Records of t_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_project` (
  `id` varchar(64)  NOT NULL,
  `short_id` varchar(32)  DEFAULT NULL,
  `name` varchar(64)  DEFAULT NULL COMMENT '项目名称',
  `survey` longtext  COMMENT '问卷',
  `setting` varchar(256)  DEFAULT NULL COMMENT '问卷设置',
  `status` int(11) DEFAULT '0' COMMENT '0未发布 1已发布',
  `belong_group` varchar(256)  DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256)  DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `short_id` (`short_id`)
) ;
COMMENT ON TABLE `t_project` IS '项目';
-- ----------------------------
-- Table structure for t_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_role` (
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
);
COMMENT ON TABLE `t_role` IS '角色';
-- ----------------------------
-- ----------------------------
-- Table structure for t_template
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_template` (
  `id` varchar(64)  NOT NULL,
  `name` varchar(64)  DEFAULT NULL COMMENT '模板标题',
  `question_type` varchar(64)  DEFAULT NULL COMMENT '问题类型',
  `template` longtext  COMMENT '模板',
  `category` varchar(256)  DEFAULT NULL COMMENT '模板分类',
  `tag` varchar(512)  DEFAULT NULL COMMENT '标签',
  `priority` int(11) DEFAULT NULL COMMENT '排序优先级',
  `preview_url` varchar(512)  DEFAULT NULL COMMENT '预览地址',
  `shared` int(11) DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256)  DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256)  DEFAULT NULL,
  PRIMARY KEY (`id`)
);
COMMENT ON TABLE `t_template` IS '模板';
-- ----------------------------
-- Records of t_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_user` (
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
);
COMMENT ON TABLE `t_user` IS '系统用户';
-- ----------------------------
-- Records of t_user
-- ----------------------------
-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_type` varchar(100) NOT NULL DEFAULT 'SysUser' COMMENT '用户类型',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
COMMENT ON TABLE `t_user_role` IS '用户角色关联';
-- ----------------------------