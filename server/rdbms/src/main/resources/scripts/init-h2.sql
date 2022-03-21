
-- ----------------------------
-- Table structure for t_account
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_account (
    id varchar(64) NOT NULL COMMENT 'ID',
    user_type varchar(100) NOT NULL DEFAULT 'SysUser' COMMENT '用户类型',
    user_id varchar(64) NOT NULL COMMENT '用户ID',
    auth_type varchar(20) NOT NULL DEFAULT 'PWD' COMMENT '认证方式',
    auth_account varchar(100) NOT NULL COMMENT '用户名',
    auth_secret varchar(64) DEFAULT NULL COMMENT '密码',
    secret_salt varchar(32) DEFAULT NULL COMMENT '加密盐',
    status int(11) NOT NULL DEFAULT '1' COMMENT '用户状态',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_account
-- ----------------------------
BEGIN;
INSERT INTO t_account VALUES ('1457995482310680578', 'SysUser', '1457995481966747649', 'PWD', 'admin', '$2a$10$vZk9P3XtbD2KrdLbQYPvBuPAkkUda0OlkDg7io1Q6VEtfFPig/tqO', NULL, 1, 0, '2021-11-09 16:56:26', NULL, '2022-02-01 23:57:27', '1457995481966747649');
COMMIT;

-- ----------------------------
-- Table structure for t_answer
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_answer (
    id varchar(64)  NOT NULL,
    project_id varchar(64)  DEFAULT NULL,
    answer text  COMMENT '问卷答案',
    attachment varchar(1024)  DEFAULT NULL COMMENT '问卷元数据',
    meta_info text  COMMENT '问卷元数据',
    temp_save int(11) DEFAULT NULL COMMENT '0暂存 1已完成',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_answer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_dashboard
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_dashboard (
    id varchar(64) NOT NULL COMMENT 'ID',
    key varchar(256) NOT NULL COMMENT '仪表盘组件key',
    type int(2) DEFAULT NULL COMMENT '仪表盘分类',
    project_id varchar(64) DEFAULT NULL COMMENT '项目ID',
    setting varchar(1024)   DEFAULT NULL COMMENT '仪表盘设置',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_dashboard
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_dept (
    id varchar(64) NOT NULL COMMENT 'ID',
    parent_id varchar(64) NOT NULL,
    name varchar(64) DEFAULT NULL COMMENT '名称',
    short_name varchar(64) NOT NULL COMMENT '简称',
    code varchar(64) DEFAULT NULL COMMENT '数据权限类型',
    manager_id varchar(64) DEFAULT NULL COMMENT '扩展字段',
    sort_code int(11) DEFAULT NULL,
    property_json varchar(256) DEFAULT NULL COMMENT '扩展字段',
    status varchar(20) DEFAULT NULL COMMENT '扩展字段',
    remark varchar(256) DEFAULT NULL COMMENT '扩展字段',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_dept
-- ----------------------------
BEGIN;
INSERT INTO t_dept VALUES ('1', '0', '卷王', 'juanwang', 'juanwang', '1457995481966747649', NULL, NULL, NULL, NULL, 0, '2021-11-21 14:12:08', NULL, '2021-11-21 14:22:58', '1457995481966747649');
COMMIT;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_file (
    id varchar(64)  NOT NULL,
    original_name varchar(256)  DEFAULT NULL,
    file_name varchar(256)  DEFAULT NULL,
    file_path varchar(512)  DEFAULT NULL,
    thumb_file_path varchar(512)  DEFAULT NULL,
    storage_type int(11) DEFAULT NULL,
    shared int(11) DEFAULT '0',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_flow_entry
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_flow_entry (
    id varchar(64)  NOT NULL COMMENT '主键',
    project_id varchar(64)  DEFAULT NULL COMMENT '流程定义key',
    process_definition_id varchar(64)  DEFAULT NULL COMMENT '流程定义 id',
    deploy_id varchar(64)  DEFAULT NULL COMMENT '部署id',
    bpmn_xml longtext  COMMENT '流程XML',
    nodes longtext  COMMENT '流程节点',
    icon varchar(256)  DEFAULT NULL COMMENT '流程图标',
    status int(11) DEFAULT '0' COMMENT '0未发布 1已发布',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_flow_entry
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_flow_entry_node
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_flow_entry_node (
    id varchar(64)  NOT NULL COMMENT '节点id',
    name varchar(256)  DEFAULT NULL COMMENT '节点名称',
    project_id varchar(64)  DEFAULT NULL COMMENT '项目id',
    task_type int(11) DEFAULT NULL COMMENT '流程节点类型',
    field_permission text  COMMENT '字段权限',
    setting text  COMMENT '流程设置',
    identity text  COMMENT '授权用户',
    expression text  COMMENT '表达式',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_flow_entry_node
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_flow_entry_publish
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_flow_entry_publish (
    id varchar(64)  NOT NULL COMMENT '流程部署Id',
    entry_id bigint(20) NOT NULL COMMENT '流程Id',
    process_definition_id varchar(64)  NOT NULL COMMENT '流程引擎的定义Id',
    publish_version int(11) NOT NULL COMMENT '发布版本',
    active_status bit(1) NOT NULL COMMENT '激活状态',
    main_version bit(1) NOT NULL COMMENT '是否为主版本',
    publish_time datetime NOT NULL COMMENT '发布时间',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    ) ;

-- ----------------------------
-- Records of t_flow_entry_publish
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_flow_instance
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_flow_instance (
    id varchar(64)  NOT NULL COMMENT '流程实例ID',
    project_id varchar(64)  DEFAULT NULL COMMENT '项目id',
    answer_id varchar(64)  DEFAULT NULL COMMENT '答案id',
    status varchar(64)  DEFAULT NULL COMMENT '当前状态',
    approval_stage varchar(256)  DEFAULT NULL COMMENT '审批阶段',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_flow_instance
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_flow_operation
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_flow_operation (
    id varchar(64)  NOT NULL COMMENT '主键id',
    instance_id varchar(64)  DEFAULT NULL COMMENT '流程实例id',
    project_id varchar(64)  DEFAULT NULL COMMENT '项目id',
    answer_id varchar(64)  DEFAULT NULL COMMENT '答案id',
    activity_id varchar(64)  DEFAULT NULL COMMENT '任务对应的xml节点id',
    task_id varchar(64)  DEFAULT NULL COMMENT '任务id',
    task_name varchar(64)  DEFAULT NULL COMMENT '任务名称',
    task_type int(11) DEFAULT NULL COMMENT '任务类型',
    approval_type varchar(64)  DEFAULT NULL COMMENT '审批类型',
    comment varchar(64)  DEFAULT NULL COMMENT '注释内容',
    delegate_assignee varchar(64)  DEFAULT NULL COMMENT '委托指定人',
    answer varchar(1024)  DEFAULT NULL COMMENT '当前节点答案',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    new_activity_id varchar(64)  DEFAULT NULL COMMENT '新的任务节点id',
    latest bit(1) DEFAULT NULL COMMENT '当前流程最新操作',
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_flow_operation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_flow_operation_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_flow_operation_user (
    id varchar(64)  NOT NULL COMMENT '节点id',
    operation_id varchar(64)  DEFAULT NULL COMMENT '操作id',
    user_id varchar(64)  DEFAULT NULL COMMENT '用户id',
    group_id varchar(256)  DEFAULT NULL COMMENT '组id',
    link_type varchar(64)  DEFAULT NULL COMMENT '用户类型',
    latest TINYINT NOT NULL DEFAULT 1 COMMENT '是否最新记录',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    ) ;

-- ----------------------------
-- Records of t_flow_operation_user
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_position
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_position (
    id varchar(64) NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL,
    code varchar(20) DEFAULT NULL,
    is_virtual tinyint(1) NOT NULL COMMENT '是否虚拟岗',
    data_permission_type varchar(256) DEFAULT NULL COMMENT '数据权限类型',
    property_json varchar(20) DEFAULT NULL COMMENT '扩展字段',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_position
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_project
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_project (
    id varchar(64)  NOT NULL,
    short_id varchar(32)  DEFAULT NULL,
    name varchar(64)  DEFAULT NULL COMMENT '项目名称',
    survey longtext  COMMENT '问卷',
    setting text  DEFAULT NULL COMMENT '问卷设置',
    status int(11) DEFAULT '0' COMMENT '0未发布 1已发布',
    belong_group varchar(256)  DEFAULT NULL,
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY short_id (short_id)
    )  ;

-- ----------------------------
-- Records of t_project
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_project_partner
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_project_partner (
    id varchar(64)  NOT NULL,
    project_id varchar(64)  DEFAULT NULL COMMENT '项目id',
    type int(2) DEFAULT NULL COMMENT '参与者类型',
    user_id varchar(64)  DEFAULT NULL COMMENT '参与者id',
    group_id varchar(64)  DEFAULT NULL COMMENT '参与组id',
    data_permission text  COMMENT '数据权限',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    ) ;

-- ----------------------------
-- Records of t_project_partner
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_role (
    id varchar(64) NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT '名称',
    code varchar(50) NOT NULL COMMENT '编码',
    remark varchar(100) DEFAULT NULL COMMENT '备注',
    authority varchar(3000) DEFAULT NULL COMMENT '权限列表',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO t_role VALUES ('1457995481928998914', 'Admin', 'admin', '系统初始化角色', 'answer,answer:list,answer:detail,answer:create,answer:update,answer:delete,answer:export,file,file:detail,file:list,file:import,file:delete,project,project:list,project:detail,project:create,project:update,project:delete,project:report,system,system:role,system:role:list,system:user,system:user:list,system:role:create,system:role:update,system:role:delete,system:user:create,system:user:update,system:user:updatePosition,system:user:delete,position,position:list,position:create,system:position,system:position:update,system:position:delete,system:org,system:org:list,system:org:create,system:org:update,system:org:delete,template,template:list,template:create,template:update,template:delete,system:position:list,system:position:create,system:dept,system:dept:list,system:dept:create,system:dept:update,system:dept:delete', 0, '2021-11-09 16:56:26', NULL, '2022-02-01 23:53:28', '1457995481966747649');
INSERT INTO t_role VALUES ('1462366121347792897', '普通用户', 'role', NULL, 'answer,answer:export,answer:list,answer:detail,answer:create,answer:update,answer:delete,file,file:detail,file:import,file:list,file:delete,project,project:list,project:detail,project:create,project:update,project:delete,project:report,system,system:user,system:user:list,system:role,system:role:list,system:role:create,system:role:update,system:role:delete,system:user:create,system:user:update,system:user:updatePosition,system:user:delete,system:position,system:position:list,system:position:create,system:position:update,system:position:delete,system:org,system:org:list,system:org:create,system:org:update,system:org:delete,template,template:list,template:create,template:update,template:delete', 1, '2021-11-21 18:23:47', '1457995481966747649', '2022-01-27 14:08:14', '1457995481966747649');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_info
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_sys_info (
    id varchar(64)  NOT NULL COMMENT '主键',
    name varchar(64)  DEFAULT NULL COMMENT '系统名称',
    description varchar(128)  DEFAULT NULL COMMENT '系统描述信息',
    avatar varchar(64)  DEFAULT NULL COMMENT '图标',
    locale varchar(64)  DEFAULT NULL COMMENT '默认语言',
    version varchar(64)  DEFAULT NULL COMMENT '默认语言',
    is_default tinyint(1) DEFAULT NULL COMMENT '是否默认设置',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_sys_info
-- ----------------------------
BEGIN;
INSERT INTO t_sys_info VALUES ('1', '卷王', '做更好的调查问卷系统', NULL, 'zh-CN', NULL, 1, '2022-02-11 10:13:19', NULL, '2022-02-11 14:29:03', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_template
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_template (
    id varchar(64)  NOT NULL,
    name varchar(64)  DEFAULT NULL COMMENT '模板标题',
    question_type varchar(64)  DEFAULT NULL COMMENT '问题类型',
    template longtext  COMMENT '模板',
    category varchar(256)  DEFAULT NULL COMMENT '模板分类',
    tag varchar(512)  DEFAULT NULL COMMENT '标签',
    priority int(11) DEFAULT NULL COMMENT '排序优先级',
    preview_url varchar(512)  DEFAULT NULL COMMENT '预览地址',
    shared int(11) DEFAULT '0',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256)  DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256)  DEFAULT NULL,
    PRIMARY KEY (id)
    ) ;

-- ----------------------------
-- Records of t_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_user (
    id varchar(64) NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT '真实姓名',
    dept_id varchar(20) DEFAULT NULL,
    gender varchar(10) NOT NULL COMMENT '性别',
    birthday date DEFAULT NULL COMMENT '出生日期',
    phone varchar(20) DEFAULT NULL COMMENT '手机号',
    email varchar(50) DEFAULT NULL COMMENT 'Email',
    avatar varchar(200) DEFAULT NULL COMMENT '头像地址',
    status int(11) NOT NULL DEFAULT '1' COMMENT '用户状态',
    is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    profile varchar(255) DEFAULT NULL COMMENT '个人简介',
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO t_user VALUES ('1457995481966747649', 'Admin', '1', 'F', NULL, '13800138000', 'sqlzero@sqlzero.com', '1492007810605539329', 1, 0, '2021-11-09 16:56:26', NULL, '2022-02-11 13:29:17', '1457995481966747649', 'hello world');
COMMIT;

-- ----------------------------
-- Table structure for t_user_position
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_user_position (
    id varchar(64) NOT NULL COMMENT 'ID',
    user_id varchar(64) NOT NULL,
    dept_id varchar(64) DEFAULT NULL,
    position_id varchar(64) DEFAULT NULL COMMENT '数据权限类型',
    is_primary_position tinyint(1) DEFAULT NULL COMMENT '是否主岗',
    propertyJson varchar(256) DEFAULT NULL COMMENT '扩展字段',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_user_position
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------

CREATE TABLE IF NOT EXISTS t_user_role (
    id varchar(64) NOT NULL COMMENT 'ID',
    user_type varchar(100) NOT NULL DEFAULT 'SysUser' COMMENT '用户类型',
    user_id bigint(20) NOT NULL COMMENT '用户ID',
    role_id bigint(20) NOT NULL COMMENT '角色ID',
    create_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(256) DEFAULT NULL,
    update_at timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by varchar(256) DEFAULT NULL,
    PRIMARY KEY (id)
    )  ;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
BEGIN;
INSERT INTO t_user_role VALUES ('1488542015867121666', 'SysUser', 1457995481966747649, 1457995481928998914,  '2022-02-01 23:57:27', '1457995481966747649', NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
