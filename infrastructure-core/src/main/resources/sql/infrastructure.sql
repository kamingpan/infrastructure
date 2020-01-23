-- CREATE DATABASE
-- CREATE DATABASE IF NOT EXISTS infrastructure DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

-- SET NAMES utf8mb4;
-- SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for system_admin
-- ----------------------------
-- DROP TABLE IF EXISTS `system_admin`;
CREATE TABLE IF NOT EXISTS `system_admin` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `status` int(2) DEFAULT NULL COMMENT '状态（0：正常，1：禁用）',
  `full_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `superior` varchar(32) DEFAULT NULL COMMENT '上级管理员',
  `portrait` varchar(256) DEFAULT NULL COMMENT '头像',
  `gender` int(2) DEFAULT NULL COMMENT '性别（0：女，1：男）',
  `email` varchar(128) DEFAULT NULL COMMENT '电子邮箱',
  `restrict_ip` varchar(32) DEFAULT NULL COMMENT '限制ip',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_username` (`username`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员';

-- ----------------------------
-- Records of system_admin
-- ----------------------------
BEGIN;
INSERT INTO `system_admin`(`id`, `username`, `password`, `status`, `full_name`, `phone`, `superior`, `portrait`, `gender`, `email`, `restrict_ip`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('0', 'admin', '903e33f151415bf32f598ecb0c0436e9', 0, '超级管理员', NULL, '0', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for system_admin_login_log
-- ----------------------------
-- DROP TABLE IF EXISTS `system_admin_login_log`;
CREATE TABLE IF NOT EXISTS `system_admin_login_log` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `admin_id` varchar(32) NOT NULL COMMENT '管理员主键',
  `login_time` datetime(3) NOT NULL COMMENT '登录时间',
  `status` int(2) NOT NULL COMMENT '登录状态（0：成功，1：失败）',
  `result` varchar(256) DEFAULT NULL COMMENT '登录结果描述',
  `ip` varchar(32) DEFAULT NULL COMMENT '登录ip',
  `address` varchar(256) DEFAULT NULL COMMENT '登录ip真实地址',
  `mac` varchar(64) DEFAULT NULL COMMENT '登录mac',
  `device` varchar(32) DEFAULT NULL COMMENT '登录设备',
  `browser` varchar(64) DEFAULT NULL COMMENT '登录浏览器',
  PRIMARY KEY (`id`),
  KEY `index_login_time` (`login_time`) USING BTREE COMMENT '登录时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员登录日志';

-- ----------------------------
-- Table structure for system_admin_operate_log
-- ----------------------------
-- DROP TABLE IF EXISTS `system_admin_operate_log`;
CREATE TABLE IF NOT EXISTS `system_admin_operate_log` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `belong` varchar(32) DEFAULT NULL COMMENT '所属对象',
  `belong_id` varchar(32) DEFAULT NULL COMMENT '所属主键',
  `type` varchar(16) DEFAULT NULL COMMENT '操作类型（登录，登出，创建，修改，删除，启用，禁用，其它）',
  `target` varchar(32) DEFAULT NULL COMMENT '操作目标',
  `ip` varchar(16) DEFAULT NULL COMMENT '操作ip',
  `url` varchar(128) DEFAULT NULL COMMENT '操作链接地址',
  `parameter` varchar(512) DEFAULT NULL COMMENT '请求参数',
  `method` varchar(128) DEFAULT NULL COMMENT '方法名',
  `content` text DEFAULT NULL COMMENT '操作内容',
  `remark` varchar(256) DEFAULT NULL COMMENT '操作描述',
  `operator_id` varchar(32) DEFAULT NULL COMMENT '操作人主键',
  `operator` varchar(32) DEFAULT NULL COMMENT '操作人',
  `operator_portrait` varchar(32) DEFAULT NULL COMMENT '操作人头像',
  `operate_time` datetime(3) DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `index_blong_is` (`belong_id`) USING BTREE COMMENT '所属主键索引',
  KEY `index_operate_time` (`operate_time`) USING BTREE COMMENT '操作时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理端操作日志';

-- ----------------------------
-- Table structure for system_admin_role
-- ----------------------------
-- DROP TABLE IF EXISTS `system_admin_role`;
CREATE TABLE IF NOT EXISTS `system_admin_role` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `admin_id` varchar(32) NOT NULL COMMENT '管理员主键',
  `role_id` varchar(32) NOT NULL COMMENT '角色主键',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_admin_role` (`admin_id`,`role_id`) USING BTREE COMMENT '管理员-角色组合唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员-角色关联';

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
-- DROP TABLE IF EXISTS `system_role`;
CREATE TABLE IF NOT EXISTS `system_role` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(16) DEFAULT NULL COMMENT '角色名称',
  `superior` varchar(32) DEFAULT NULL COMMENT '上级角色',
  `level` int(2) DEFAULT NULL COMMENT '级别',
  `status` int(2) DEFAULT NULL COMMENT '状态（0：正常，1：禁用）',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_name` (`name`) USING BTREE COMMENT '角色名称唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

-- ----------------------------
-- Records of system_role
-- ----------------------------
BEGIN;
INSERT INTO `system_role`(`id`, `name`, `superior`, `level`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('1', '超级管理员', '0', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for system_role_permission
-- ----------------------------
-- DROP TABLE IF EXISTS `system_role_permission`;
CREATE TABLE IF NOT EXISTS `system_role_permission` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `role_id` varchar(32) NOT NULL COMMENT '角色主键',
  `permission_id` varchar(32) NOT NULL COMMENT '模块权限主键',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_role_permission` (`role_id`,`permission_id`) USING BTREE COMMENT '角色-权限组合唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关联';

-- ----------------------------
-- Table structure for system_permission
-- ----------------------------
-- DROP TABLE IF EXISTS `system_permission`;
CREATE TABLE IF NOT EXISTS `system_permission` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(16) DEFAULT NULL COMMENT '权限名称',
  `module_id` varchar(32) DEFAULT NULL COMMENT '所属模块主键',
  `superior` varchar(32) DEFAULT NULL COMMENT '上级权限',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `authentication` varchar(64) DEFAULT NULL COMMENT '权限字符串',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `method` varchar(8) DEFAULT NULL COMMENT '请求方法',
  `status` int(2) DEFAULT NULL COMMENT '状态（0：正常，1：禁用；暂时无用）',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `index_sort` (`sort`) USING BTREE COMMENT '排序索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限';

-- ----------------------------
-- Records of system_permission
-- ----------------------------
BEGIN;
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('01', '管理员列表', '02', '0', 'fa-list', 'admin:list', 1, 'get', 0, '管理员列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('02', '管理员详情', '02', '01', 'fa-info-circle', 'admin:info', 2, 'get', 0, '管理员详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('03', '管理员新增', '02', '01', 'fa-plus', 'admin:insert', 3, 'post', 0, '管理员新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('04', '管理员修改', '02', '01', 'fa-edit', 'admin:update', 4, 'put', 0, '管理员修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('05', '管理员删除', '02', '01', 'fa-trash', 'admin:delete', 5, 'delete', 0, '管理员删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('06', '管理员启用', '02', '01', 'fa-check-circle-o', 'admin:enable', 6, 'patch', 0, '管理员启用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('07', '管理员禁用', '02', '01', 'fa-ban', 'admin:disable', 7, 'patch', 0, '管理员禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('08', '重置密码', '02', '01', 'fa-undo', 'admin:reset-password', 8, 'patch', 0, '重置密码', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('09', '角色分配', '02', '01', 'fa-id-card', 'admin:role', 9, 'post', 0, '角色分配', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('11', '角色列表', '03', '0', 'fa-list', 'role:list', 1, 'get', 0, '角色列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('12', '角色详情', '03', '11', 'fa-info-circle', 'role:info', 2, 'get', 0, '角色详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('13', '角色新增', '03', '11', 'fa-plus', 'role:insert', 3, 'post', 0, '角色新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('14', '角色修改', '03', '11', 'fa-edit', 'role:update', 4, 'put', 0, '角色修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('15', '角色删除', '03', '11', 'fa-trash', 'role:delete', 5, 'delete', 0, '角色删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('16', '角色启用', '03', '11', 'fa-check-circle-o', 'role:enable', 6, 'patch', 0, '角色启用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('17', '角色禁用', '03', '11', 'fa-ban', 'role:disable', 7, 'patch', 0, '角色禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('18', '权限授予', '03', '11', 'fa-user-secret', 'role:permission', 8, 'post', 0, '权限授予', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('19', '管理员关联', '03', '11', 'fa-user-circle', 'role:admin', 9, 'post', 0, '管理员关联', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('21', '权限列表', '04', '0', 'fa-list', 'permission:list', 1, 'get', 0, '权限列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('22', '权限详情', '04', '21', 'fa-info-circle', 'permission:info', 2, 'get', 0, '权限详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('23', '权限新增', '04', '21', 'fa-plus', 'permission:insert', 3, 'post', 0, '权限新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('24', '权限修改', '04', '21', 'fa-edit', 'permission:update', 4, 'put', 0, '权限修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('25', '权限删除', '04', '21', 'fa-trash', 'permission:delete', 5, 'delete', 0, '权限删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('26', '角色关联', '04', '21', 'fa-id-card', 'permission:role', 6, 'post', 0, '角色关联', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('31', '模块列表', '05', '0', 'fa-list', 'module:list', 1, 'get', 0, '模块列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('32', '模块详情', '05', '31', 'fa-info-circle', 'module:info', 2, 'get', 0, '模块详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('33', '模块新增', '05', '31', 'fa-plus', 'module:insert', 3, 'post', 0, '模块新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('34', '模块修改', '05', '31', 'fa-edit', 'module:update', 4, 'put', 0, '模块修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('35', '模块删除', '05', '31', 'fa-trash', 'module:delete', 5, 'delete', 0, '模块删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('36', '模块启用', '05', '32', 'fa-check-circle-o', 'module:enable', 6, 'patch', 0, '模块启用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('37', '模块禁用', '05', '32', 'fa-ban', 'module:disable', 7, 'patch', 0, '模块禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('38', '权限关联', '05', '31', 'fa-user-secret', 'module:permission', 8, 'post', 0, '权限关联', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('41', '数据字典列表', '06', '0', 'fa-list', 'data-dictionary:list', 1, 'get', 0, '数据字典列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('42', '数据字典详情', '06', '41', 'fa-info-circle', 'data-dictionary:info', 2, 'get', 0, '数据字典详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('43', '数据字典新增', '06', '41', 'fa-plus', 'data-dictionary:insert', 3, 'post', 0, '数据字典新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('44', '数据字典修改', '06', '41', 'fa-edit', 'data-dictionary:update', 4, 'put', 0, '数据字典修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('45', '数据字典删除', '06', '41', 'fa-trash', 'data-dictionary:delete', 5, 'delete', 0, '数据字典删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('51', '配置信息列表', '07', '0', 'fa-list', 'configuration:list', 1, 'get', 0, '配置信息列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('52', '配置信息详情', '07', '51', 'fa-info-circle', 'configuration:info', 2, 'get', 0, '配置信息详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('53', '配置信息新增', '07', '51', 'fa-plus', 'configuration:insert', 3, 'post', 0, '配置信息新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('54', '配置信息修改', '07', '51', 'fa-edit', 'configuration:update', 4, 'put', 0, '配置信息修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('55', '配置信息删除', '07', '51', 'fa-trash', 'configuration:delete', 5, 'delete', 0, '配置信息删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('61', '系统信息详情', '08', '0', 'fa-info-circle', 'management-information:info', 1, 'get', 0, '系统信息详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('62', '系统信息修改', '08', '61', 'fa-edit', 'management-information:update', 1, 'put', 0, '系统信息修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('71', '登录日志列表', '12', '0', 'fa-list', 'admin-login-log:list', 1, 'get', 0, '登录日志列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('72', '登录日志详情', '12', '71', 'fa-info-circle', 'admin-login-log:info', 2, 'get', 0, '登录日志详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('73', '登录日志导出', '12', '71', 'fa-file-excel-o', 'admin-login-log:export', 3, 'get', 0, '登录日志导出', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('81', 'java melody', '22', '0', 'fa-tachometer', 'java-melody:list', 1, 'get', 0, 'java melody', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('91', 'druid', '23', '0', 'fa-file-text-o', 'druid:list', 1, 'get', 0, 'druid', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('101', 'redis列表', '24', '0', 'fa-list', 'redis:list', 1, 'get', 0, 'redis列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('102', 'redis查询', '24', '101', 'fa-info-circle', 'redis:get', 2, 'get', 0, 'redis查询', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('103', 'redis修改', '24', '101', 'fa-edit', 'redis:update', 3, 'post', 0, 'redis修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('104', 'redis删除', '24', '101', 'fa-trash', 'redis:delete', 4, 'delete', 0, 'redis删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('111', '注册用户列表', '32', '0', 'fa-list', 'registered-user:list', 1, 'get', 0, '注册用户列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('112', '注册用户详情', '32', '111', 'fa-info-circle', 'registered-user:info', 2, 'get', 0, '注册用户详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('115', '注册用户删除', '32', '111', 'fa-trash', 'registered-user:delete', 5, 'delete', 0, '注册用户删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('116', '用户启用', '32', '111', 'fa-check-circle-o', 'user:enable', 6, 'patch', 0, '用户启用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('117', '用户禁用', '32', '111', 'fa-ban', 'user:disable', 7, 'patch', 0, '用户禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('121', '公众号用户列表', '33', '0', 'fa-list', 'subscription-user:list', 1, 'get', 0, '公众号用户列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('122', '公众号用户详情', '33', '121', 'fa-info-circle', 'subscription-user:info', 2, 'get', 0, '公众号用户详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('125', '公众号用户删除', '33', '121', 'fa-trash', 'subscription-user:delete', 3, 'delete', 0, '公众号用户删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('131', '小程序用户列表', '34', '0', 'fa-list', 'mini-program-user:list', 1, 'get', 0, '小程序用户列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('132', '小程序用户详情', '34', '131', 'fa-info-circle', 'mini-program-user:info', 2, 'get', 0, '小程序用户详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('135', '小程序用户删除', '34', '131', 'fa-trash', 'mini-program-user:delete', 3, 'delete', 0, '小程序用户删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('141', '授权客户端列表', '41', '0', 'fa-list', 'oauth-client:list', 1, 'get', 0, '授权客户端列表', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('142', '授权客户端详情', '41', '141', 'fa-info-circle', 'oauth-client:info', 2, 'get', 0, '授权客户端详情', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('143', '授权客户端新增', '41', '141', 'fa-plus', 'oauth-client:insert', 3, 'post', 0, '授权客户端新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('144', '授权客户端修改', '41', '141', 'fa-edit', 'oauth-client:update', 4, 'put', 0, '授权客户端修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_permission`(`id`, `name`, `module_id`, `superior`, `icon`, `authentication`, `sort`, `method`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('145', '授权客户端删除', '41', '141', 'fa-trash', 'oauth-client:delete', 5, 'delete', 0, '授权客户端删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for system_module
-- ----------------------------
-- DROP TABLE IF EXISTS `system_module`;
CREATE TABLE IF NOT EXISTS `system_module` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(16) DEFAULT NULL COMMENT '模块名称',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `type` varchar(16) DEFAULT NULL COMMENT '路由类型（load：组件加载，iframe：内嵌页面）',
  `route` varchar(128) DEFAULT NULL COMMENT '路由路径',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `superior` varchar(32) DEFAULT NULL COMMENT '上级模块',
  `status` int(2) DEFAULT NULL COMMENT '状态（0：正常，1：禁用）',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `index_sort` (`sort`) USING BTREE COMMENT '排序索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块';

-- ----------------------------
-- Records of system_module
-- ----------------------------
BEGIN;
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('01', '系统设置', 'fa-wrench', NULL, NULL, 99, '0', 0, '系统设置', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('02', '管理员', 'fa-user-circle', 'load', '/admin/list', 1, '01', 0, '管理员', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('03', '角色', 'fa-id-card', 'load', '/role/list', 2, '01', 0, '角色', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('04', '权限', 'fa-user-secret', 'load', '/permission/list', 3, '01', 0, '权限', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('05', '模块', 'fa-th-list', 'load', '/module/list', 4, '01', 0, '模块', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('06', '数据字典', 'fa-book', 'load', '/data-dictionary/list', 5, '01', 0, '数据字典', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('07', '配置信息', 'fa-cog', 'load', '/configuration/list', 6, '01', 0, '配置信息', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('08', '系统信息', 'fa-television', 'load', '/management-information', 6, '01', 0, '系统信息', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('11', '日志', 'fa-book', NULL, NULL, 96, '0', 0, '日志', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('12', '登录日志', 'fa-address-book', 'load', '/admin-login-log/list', 1, '11', 0, '登录日志', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('13', '操作日志', 'fa-gamepad', 'load', NULL, 2, '11', 1, '操作日志', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('21', '系统监控', 'fa-binoculars', NULL, NULL, 97, '0', 0, '系统监控', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('22', 'java melody', 'fa-tachometer', 'iframe', 'https://www.kamingpan.com/management/monitoring', 1, '21', 0, 'java melody', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('23', 'druid', 'fa-file-text-o', 'iframe', 'https://www.kamingpan.com/management/druid/index.html', 2, '21', 0, 'druid', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('24', 'redis', 'fa-database', 'load', '/redis', 3, '21', 1, 'redis', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('31', '用户管理', 'fa-user', NULL, NULL, 95, '0', 0, '用户管理', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('32', '注册用户', 'fa-user', 'load', '/registered-user/list', 1, '31', 0, '注册用户', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('33', '公众号用户', 'fa-wechat', 'load', '/subscription-user/list', 2, '31', 0, '公众号用户', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('34', '小程序用户', 'fa-wechat', 'load', '/mini-program-user/list', 3, '31', 0, '小程序用户', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_module`(`id`, `name`, `icon`, `type`, `route`, `sort`, `superior`, `status`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('41', '授权客户端', 'fa-mobile', 'load', '/oauth-client/list', 98, '0', 0, '授权客户端', NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for system_data_dictionary
-- ----------------------------
-- DROP TABLE IF EXISTS `system_data_dictionary`;
CREATE TABLE IF NOT EXISTS `system_data_dictionary` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `label` varchar(16) DEFAULT NULL COMMENT '标签',
  `value` varchar(16) DEFAULT NULL COMMENT '值',
  `type` varchar(16) DEFAULT NULL COMMENT '类型',
  `clazz` varchar(32) DEFAULT NULL COMMENT '类/表',
  `variable` varchar(32) DEFAULT NULL COMMENT '变量/字段',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `index_clazz` (`clazz`) USING BTREE COMMENT '类索引',
  KEY `index_variable` (`variable`) USING BTREE COMMENT '变量索引',
  KEY `index_sort` (`sort`) USING BTREE COMMENT '排序索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典（管理所有实体用键表示的值）';

-- ----------------------------
-- Records of system_data_dictionary
-- ----------------------------
BEGIN;
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00001', '字符串（string）', '字符串（string）', '字符串（string）', 'DataDictionary', 'type', 1, '数据字典类型 - 字符串', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00002', '整型（int）', '整型（int）', '字符串（string）', 'DataDictionary', 'type', 2, '数据字典类型 - 整型', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00003', '长整型（long）', '长整型（long）', '字符串（string）', 'DataDictionary', 'type', 3, '数据字典类型 - 长整型', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00004', '短整型（short）', '短整型（short）', '字符串（string）', 'DataDictionary', 'type', 4, '数据字典类型 - 短整型', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00005', '布尔型（boolean）', '布尔型（boolean）', '字符串（string）', 'DataDictionary', 'type', 5, '数据字典类型 - 布尔型', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00101', '正常', '0', '整型（int）', 'Admin', 'status', 1, '管理员状态 - 正常', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00102', '禁用', '1', '整型（int）', 'Admin', 'status', 2, '管理员状态 - 禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00111', '男', '1', '整型（int）', 'Admin', 'gender', 1, '管理员性别 - 男', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00112', '女', '0', '整型（int）', 'Admin', 'gender', 2, '管理员性别 - 女', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00201', '成功', '0', '整型（int）', 'AdminLoginLog', 'status', 1, '管理员登录日志 - 成功', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00202', '失败', '1', '整型（int）', 'AdminLoginLog', 'status', 2, '管理员登录日志 - 失败', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00301', '正常', '0', '整型（int）', 'Role', 'status', 1, '角色 - 正常', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00302', '禁用', '1', '整型（int）', 'Role', 'status', 2, '角色 - 禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00401', '正常', '0', '整型（int）', 'Permission', 'status', 1, '权限 - 正常', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00402', '禁用', '1', '整型（int）', 'Permission', 'status', 2, '权限 - 禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00411', 'get(查询)', 'get', '字符串（string）', 'Permission', 'method', 1, '权限 - 查询', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00412', 'post(新增)', 'post', '字符串（string）', 'Permission', 'method', 2, '权限 - 新增', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00413', 'delete(删除)', 'delete', '字符串（string）', 'Permission', 'method', 3, '权限 - 删除', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00414', 'put(修改)', 'put', '字符串（string）', 'Permission', 'method', 4, '权限 - 修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00415', 'patch(部分修改)', 'patch', '字符串（string）', 'Permission', 'method', 5, '权限 - 部分修改', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00501', '正常', '0', '整型（int）', 'Module', 'status', 1, '模块 - 正常', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00502', '禁用', '1', '整型（int）', 'Module', 'status', 2, '模块 - 禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00511', '组件加载', 'load', '字符串（string）', 'Module', 'type', 1, '模块 - 组件加载', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00512', '内嵌页面', 'iframe', '字符串（string）', 'Module', 'type', 2, '模块 - 内嵌页面', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00601', '显示logo', '1', '整型（int）', 'ManagementInformation', 'type', 1, '系统信息 - 显示logo', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00602', '显示文字', '2', '整型（int）', 'ManagementInformation', 'type', 2, '系统信息 - 显示文字', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00701', '正常', '0', '整型（int）', 'User', 'status', 1, '用户 - 正常', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00702', '禁用', '1', '整型（int）', 'User', 'status', 2, '用户 - 禁用', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00711', '系统注册', '0', '整型（int）', 'User', 'registered_source', 1, '用户 - 系统注册', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00712', '公众号授权', '1', '整型（int）', 'User', 'registered_source', 2, '用户 - 公众号授权', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00713', '小程序授权', '2', '整型（int）', 'User', 'registered_source', 2, '用户 - 小程序授权', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00801', '女', '0', '整型（int）', 'SubscriptionUser', 'gender', 1, '公众号用户 - 女', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00802', '男', '1', '整型（int）', 'SubscriptionUser', 'gender', 2, '公众号用户 - 男', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00803', '未知', '2', '整型（int）', 'SubscriptionUser', 'gender', 3, '公众号用户 - 未知', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00811', '取消关注', '0', '布尔型（boolean）', 'SubscriptionUser', 'subscribed', 1, '公众号用户 - 取消关注', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00812', '已关注', '1', '布尔型（boolean）', 'SubscriptionUser', 'subscribed', 2, '公众号用户 - 已关注', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00901', '女', '0', '整型（int）', 'MiniProgramUser', 'gender', 1, '小程序用户 - 女', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00902', '男', '1', '整型（int）', 'MiniProgramUser', 'gender', 2, '小程序用户 - 男', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `system_data_dictionary`(`id`, `label`, `value`, `type`, `clazz`, `variable`, `sort`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('00903', '未知', '2', '整型（int）', 'MiniProgramUser', 'gender', 3, '小程序用户 - 未知', NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for system_configuration
-- ----------------------------
-- DROP TABLE IF EXISTS `system_configuration`;
CREATE TABLE IF NOT EXISTS `system_configuration` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `title` varchar(32) DEFAULT NULL COMMENT '标题',
  `config_key` varchar(32) DEFAULT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值（json格式）',
  `description` varchar(64) DEFAULT NULL COMMENT '描述',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '最后修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '最后修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_ config_key` (`config_key`) USING BTREE COMMENT '配置键唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置信息';

-- ----------------------------
-- Records of system_configuration
-- ----------------------------

-- ----------------------------
-- Table structure for system_management_information
-- ----------------------------
-- DROP TABLE IF EXISTS `system_management_information`;
CREATE TABLE IF NOT EXISTS `system_management_information` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(32) DEFAULT NULL COMMENT '系统名称',
  `logo` varchar(32) DEFAULT NULL COMMENT '系统logo',
  `logo_word` varchar(8) DEFAULT NULL COMMENT '系统logo文字',
  `type` int(2) DEFAULT NULL COMMENT '显示类型（1：显示logo，2：显示文字）',
  `route` varchar(64) DEFAULT NULL COMMENT '主页路由',
  `version` varchar(32) DEFAULT NULL COMMENT '系统版本',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理端信息';

-- ----------------------------
-- Records of system_management_information
-- ----------------------------
BEGIN;
INSERT INTO `system_management_information`(`id`, `name`, `logo`, `logo_word`, `type`, `route`, `version`, `remark`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('0', '基础框架', NULL, 'KM', 1, '/index', 'V1.0.0', '基础框架', NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for system_upload_file
-- ----------------------------
-- DROP TABLE IF EXISTS `system_upload_file`;
CREATE TABLE IF NOT EXISTS `system_upload_file` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `key_word` varchar(64) DEFAULT NULL COMMENT '关键字',
  `belong` varchar(32) DEFAULT NULL COMMENT '所属对象',
  `belong_id` varchar(32) DEFAULT NULL COMMENT '所属主键',
  `belong_variable` varchar(32) DEFAULT NULL COMMENT '所属字段',
  `filename` varchar(64) DEFAULT NULL COMMENT '文件名',
  `type` int(4) DEFAULT NULL COMMENT '文件类型',
  `content_type` varchar(128) DEFAULT NULL COMMENT '文件内容类型',
  `size` bigint(16) DEFAULT NULL COMMENT '文件大小（单位：K）',
  `url` varchar(512) DEFAULT NULL COMMENT '链接',
  `the_group` varchar(32) DEFAULT NULL COMMENT '分组（FastDFS专用）',
  `path` varchar(512) DEFAULT NULL COMMENT '路径',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_key_word` (`key_word`) USING BTREE COMMENT '关键字唯一索引',
  KEY `index_belong` (`belong`) USING BTREE COMMENT '所属对象索引',
  KEY `index_belong_id` (`belong_id`) USING BTREE COMMENT '所属主键索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上传文件';

-- ----------------------------
-- Table structure for system_oauth_client
-- ----------------------------
-- DROP TABLE IF EXISTS `system_oauth_client`;
CREATE TABLE IF NOT EXISTS `system_oauth_client` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(32) DEFAULT NULL COMMENT '客户端名称',
  `client_id` varchar(32) DEFAULT NULL COMMENT '客户端id',
  `client_secret` varchar(64) DEFAULT NULL COMMENT '客户端密钥',
  `client_uri` varchar(256) DEFAULT NULL COMMENT '客户端地址',
  `redirect_uri` varchar(256) DEFAULT NULL COMMENT '重定向地址',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授权客户端';

-- ----------------------------
-- Table structure for system_oauth_token
-- ----------------------------
-- DROP TABLE IF EXISTS `system_oauth_token`;
CREATE TABLE IF NOT EXISTS `system_oauth_token` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户主键',
  `client_id` varchar(32) DEFAULT NULL COMMENT '客户端id',
  `access_token` varchar(256) DEFAULT NULL COMMENT '登录token',
  `access_token_validity` bigint(16) DEFAULT NULL COMMENT '登录token有效时长（单位：秒）',
  `refresh_time` datetime(3) DEFAULT NULL COMMENT 'token刷新时间',
  `refresh_token` varchar(256) DEFAULT NULL COMMENT '刷新token',
  `refresh_token_validity` bigint(16) DEFAULT NULL COMMENT '刷新token有效时长（单位：秒）',
  `login_time` datetime(3) DEFAULT NULL COMMENT '登录时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user_client_id` (`user_id`,`client_id`) USING BTREE COMMENT '用户-客户端id组合唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授权token';

-- ----------------------------
-- Table structure for system_subscription_user
-- ----------------------------
-- DROP TABLE IF EXISTS `system_subscription_user`;
CREATE TABLE IF NOT EXISTS `system_subscription_user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户主键',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `email` varchar(32) DEFAULT NULL COMMENT '电子邮箱',
  `mobile` char(11) DEFAULT NULL COMMENT '手机号码',
  `portrait` varchar(256) DEFAULT NULL COMMENT '头像',
  `gender` int(2) DEFAULT NULL COMMENT '性别（0：女，1：男，2：未知）',
  `country` varchar(32) DEFAULT NULL COMMENT '国家',
  `province` varchar(32) DEFAULT NULL COMMENT '省份',
  `city` varchar(32) DEFAULT NULL COMMENT '城市',
  `language` varchar(8) DEFAULT NULL COMMENT '语言',
  `openid` varchar(32) DEFAULT NULL COMMENT 'openid',
  `union_id` varchar(32) DEFAULT NULL COMMENT 'union id',
  `subscribed` tinyint(1) DEFAULT NULL COMMENT '公众号关注状态（0：取消关注，1：已关注）',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user` (`user_id`) USING BTREE COMMENT '用户主键唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众号用户';

-- ----------------------------
-- Table structure for system_subscription_user_location
-- ----------------------------
-- DROP TABLE IF EXISTS `system_subscription_user_location`;
CREATE TABLE IF NOT EXISTS `system_subscription_user_location` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户主键',
  `openid` varchar(32) DEFAULT NULL COMMENT 'openid',
  `longitude` double DEFAULT NULL COMMENT '地理位置经度',
  `latitude` double DEFAULT NULL COMMENT '地理位置纬度',
  `location_precision` double DEFAULT NULL COMMENT '地理位置精度',
  `record_time` datetime(3) DEFAULT NULL COMMENT '记录时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `index_user` (`user_id`) USING BTREE COMMENT '用户主键索引',
  KEY `index_record_time` (`record_time`) USING BTREE COMMENT '记录时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众号用户地理位置';

-- ----------------------------
-- Table structure for system_mini_program_user
-- ----------------------------
-- DROP TABLE IF EXISTS `system_mini_program_user`;
CREATE TABLE IF NOT EXISTS `system_mini_program_user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户主键',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `email` varchar(32) DEFAULT NULL COMMENT '电子邮箱',
  `mobile` char(11) DEFAULT NULL COMMENT '手机号码',
  `portrait` varchar(256) DEFAULT NULL COMMENT '头像',
  `gender` int(2) DEFAULT NULL COMMENT '性别（0：女，1：男，2：未知）',
  `country` varchar(32) DEFAULT NULL COMMENT '国家',
  `province` varchar(32) DEFAULT NULL COMMENT '省份',
  `city` varchar(32) DEFAULT NULL COMMENT '城市',
  `language` varchar(8) DEFAULT NULL COMMENT '语言',
  `openid` varchar(32) DEFAULT NULL COMMENT 'openid',
  `union_id` varchar(32) DEFAULT NULL COMMENT 'union id',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user` (`user_id`) USING BTREE COMMENT '用户主键唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序用户';

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
-- DROP TABLE IF EXISTS `system_user`;
CREATE TABLE IF NOT EXISTS `system_user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `status` int(2) DEFAULT NULL COMMENT '状态（0：正常，1：禁用）',
  `registered_time` datetime(3) DEFAULT NULL COMMENT '注册时间',
  `registered_source` int(2) DEFAULT NULL COMMENT '注册来源（0：系统注册，1：公众号授权，2：小程序授权）',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- ----------------------------
-- Records of system_user
-- ----------------------------
BEGIN;
INSERT INTO `system_user`(`id`, `status`, `registered_time`, `registered_source`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('0', 0, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for system_registered_user
-- ----------------------------
-- DROP TABLE IF EXISTS `system_registered_user`;
CREATE TABLE IF NOT EXISTS `system_registered_user` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户主键',
  `mobile` char(11) DEFAULT NULL COMMENT '手机号码',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `creator_id` varchar(32) DEFAULT NULL COMMENT '创建人主键',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `updater_id` varchar(32) DEFAULT NULL COMMENT '修改人主键',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user_username` (`username`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='注册用户';

-- ----------------------------
-- Records of system_registered_user
-- ----------------------------
BEGIN;
INSERT INTO `system_registered_user`(`id`, `user_id`, `username`, `password`, `creator_id`, `creator`, `create_time`, `updater_id`, `updater`, `update_time`, `deleted`) VALUES ('0', '0', 'user', '2d25c1cf23d8744caf146016187572ad', NULL, NULL, NULL, NULL, NULL, NULL, 0);
COMMIT;


-- SET FOREIGN_KEY_CHECKS = 1;
