

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kube_app
-- ----------------------------
DROP TABLE IF EXISTS `kube_app`;
CREATE TABLE `kube_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `namespace` varchar(100) NOT NULL COMMENT '命名空间',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `type` varchar(20) NOT NULL COMMENT '应用类型',
  `state` varchar(10) DEFAULT NULL COMMENT '状态',
  `sign` varchar(200) DEFAULT NULL COMMENT '标识',
  `count` int(11) NOT NULL COMMENT '部署数量',
  `now_count` int(11) DEFAULT '0' COMMENT '当前运行数量',
  `image` varchar(255) NOT NULL COMMENT '镜像',
  `image_version` varchar(50) DEFAULT NULL COMMENT '镜像版本',
  `cpu_request` double DEFAULT NULL COMMENT 'cpu请求',
  `cpu_limit` double DEFAULT NULL COMMENT 'cpu上限',
  `memory_request` int(11) DEFAULT NULL COMMENT '内存请求',
  `memory_request_company` varchar(10) DEFAULT NULL COMMENT '内存请求单位',
  `memory_limit` int(11) DEFAULT NULL COMMENT '内存上限',
  `memory_limit_company` varchar(10) DEFAULT NULL COMMENT '内存上限单位',
  `open_env_config` tinyint(4) DEFAULT '0' COMMENT '是否打开环境变量配置文件',
  `open_custom_env` tinyint(4) DEFAULT '0' COMMENT '是否打开自定义环境变量',
  `open_config` tinyint(4) DEFAULT '0' COMMENT '是否添加配置文件',
  `update_type` varchar(10) DEFAULT NULL COMMENT '更新策略类型',
  `unavailable` int(11) DEFAULT NULL COMMENT '最大不可用',
  `excess` int(11) DEFAULT NULL COMMENT '最大超量',
  `dns_type` varchar(255) DEFAULT NULL COMMENT 'DNS 策略类型',
  `open_health` tinyint(4) DEFAULT '0' COMMENT '是否启用健康检查',
  `health_scheme` varchar(10) DEFAULT NULL COMMENT '健康检查协议',
  `health_port` int(11) DEFAULT NULL COMMENT '健康检查端口',
  `health_path` varchar(255) DEFAULT NULL COMMENT '健康检查路径',
  `failure_threshold` int(11) DEFAULT NULL COMMENT '健康检查失败次数',
  `initial_delay` int(11) DEFAULT NULL COMMENT '首次检查时间',
  `period_seconds` int(11) DEFAULT NULL COMMENT '探测周期',
  `success_threshold` int(11) DEFAULT NULL COMMENT '成功阈值',
  `timeout_seconds` int(11) DEFAULT NULL COMMENT '超时时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`),
  KEY `namespace` (`namespace`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用\r\n创建人：xiaoke\r\n创建时间：2024-08-03 15:33:53';

-- ----------------------------
-- Records of kube_app
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_config
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_config`;
CREATE TABLE `kube_app_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `version_id` int(11) NOT NULL COMMENT '配置版本ID',
  `catalog` varchar(255) DEFAULT NULL COMMENT '挂载目录',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用配置\r\n创建人：xiaoke\r\n创建时间：2024-08-03 16:31:26';

-- ----------------------------
-- Records of kube_app_config
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_custom_env
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_custom_env`;
CREATE TABLE `kube_app_custom_env` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `env_key` varchar(255) DEFAULT NULL COMMENT '键',
  `env_value` longtext COMMENT '值',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用环境变量\r\n创建人：xiaoke\r\n创建时间：2024-08-03 15:45:29';

-- ----------------------------
-- Records of kube_app_custom_env
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_domain
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_domain`;
CREATE TABLE `kube_app_domain` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `namespace` varchar(100) NOT NULL COMMENT '命名空间',
  `protocol` varchar(20) NOT NULL COMMENT '协议',
  `domain_prefix` varchar(50) NOT NULL COMMENT '域名前缀',
  `domain_id` int(11) NOT NULL COMMENT '域名ID',
  `version_id` int(11) DEFAULT NULL COMMENT '配置文件版本ID',
  `annotations` longtext COMMENT '注解',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用域名\r\n创建人：xiaoke\r\n创建时间：2024-08-19 00:39:39';

-- ----------------------------
-- Records of kube_app_domain
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_domain_rule
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_domain_rule`;
CREATE TABLE `kube_app_domain_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_domain_id` int(11) NOT NULL COMMENT '应用域名ID',
  `path` varchar(100) NOT NULL COMMENT '规则',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `app_type` varchar(255) NOT NULL COMMENT '应用类型',
  `service_sign` varchar(100) NOT NULL COMMENT '服务标识',
  `port` int(11) NOT NULL COMMENT '端口',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用域名规则\r\n创建人：xiaoke\r\n创建时间：2024-08-19 01:09:42';

-- ----------------------------
-- Records of kube_app_domain_rule
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_host
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_host`;
CREATE TABLE `kube_app_host` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `ip` varchar(100) DEFAULT NULL COMMENT 'Ip',
  `domain` varchar(100) DEFAULT NULL COMMENT '域名',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用Host\r\n创建人：xiaoke\r\n创建时间：2024-08-03 17:10:06';

-- ----------------------------
-- Records of kube_app_host
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_image_record
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_image_record`;
CREATE TABLE `kube_app_image_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `image` varchar(200) NOT NULL COMMENT '镜像',
  `image_version` varchar(50) NOT NULL COMMENT '镜像版本',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用镜像更新记录\r\n创建人：xiaoke\r\n创建时间：2024-08-14 00:03:26';

-- ----------------------------
-- Records of kube_app_image_record
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_pod_record
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_pod_record`;
CREATE TABLE `kube_app_pod_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `pod_name` varchar(255) NOT NULL COMMENT 'pod名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用pod记录\r\n创建人：xiaoke\r\n创建时间：2024-08-14 23:23:53';

-- ----------------------------
-- Records of kube_app_pod_record
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_port
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_port`;
CREATE TABLE `kube_app_port` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `service_id` int(11) NOT NULL COMMENT 'Service ID',
  `agreement` varchar(20) NOT NULL COMMENT '访问协议',
  `container` int(11) NOT NULL COMMENT '容器端口',
  `node` int(11) DEFAULT NULL COMMENT '节点端口',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用端口\r\n创建人：xiaoke\r\n创建时间：2024-08-03 16:44:15';

-- ----------------------------
-- Records of kube_app_port
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_service
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_service`;
CREATE TABLE `kube_app_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `type` varchar(10) NOT NULL COMMENT '类型',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sign` varchar(100) NOT NULL COMMENT '标识',
  `visit_type` varchar(10) DEFAULT NULL COMMENT '访问类型',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用Service\r\n创建人：xiaoke\r\n创建时间：2024-08-03 16:39:34';

-- ----------------------------
-- Records of kube_app_service
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_storage
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_storage`;
CREATE TABLE `kube_app_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `storage_id` int(11) NOT NULL COMMENT '存储ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `capacity` int(11) DEFAULT NULL COMMENT '容量',
  `path` varchar(255) NOT NULL COMMENT '挂载路径',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用存储\r\n创建人：xiaoke\r\n创建时间：2024-08-03 17:06:20';

-- ----------------------------
-- Records of kube_app_storage
-- ----------------------------

-- ----------------------------
-- Table structure for kube_app_stretch
-- ----------------------------
DROP TABLE IF EXISTS `kube_app_stretch`;
CREATE TABLE `kube_app_stretch` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `min_replicas` int(11) DEFAULT NULL COMMENT '最小副本数',
  `max_replicas` int(11) DEFAULT NULL COMMENT '最大副本数',
  `cpu` tinyint(4) DEFAULT '0' COMMENT '是否开启cpu',
  `cpu_value_type` varchar(20) DEFAULT NULL COMMENT 'cpu数值类型（value=数值，proportion=比例）',
  `cpu_value` double DEFAULT NULL COMMENT 'cpu数值',
  `memory` tinyint(4) DEFAULT '0' COMMENT '是否开启内存',
  `memory_value_type` varchar(20) DEFAULT NULL COMMENT 'memory数值类型（value=数值，proportion=比例）',
  `memory_value` double DEFAULT NULL COMMENT 'memory数值',
  `memory_company` varchar(220) DEFAULT NULL COMMENT 'memory单位',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='弹性伸缩\r\n创建人：xiaoke\r\n创建时间：2024-08-09 16:29:20';

-- ----------------------------
-- Records of kube_app_stretch
-- ----------------------------

-- ----------------------------
-- Table structure for kube_config
-- ----------------------------
DROP TABLE IF EXISTS `kube_config`;
CREATE TABLE `kube_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `namespace` varchar(100) NOT NULL COMMENT '命名空间',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `type` varchar(10) DEFAULT NULL COMMENT '类型',
  `version` int(11) DEFAULT NULL COMMENT '最新版本',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置\r\n创建人：xiaoke\r\n创建时间：2024-08-03 16:09:47';

-- ----------------------------
-- Records of kube_config
-- ----------------------------

-- ----------------------------
-- Table structure for kube_config_version
-- ----------------------------
DROP TABLE IF EXISTS `kube_config_version`;
CREATE TABLE `kube_config_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` int(11) NOT NULL COMMENT '配置ID',
  `version` int(11) NOT NULL COMMENT '版本',
  `content` longtext NOT NULL COMMENT '内容',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置版本\r\n创建人：xiaoke\r\n创建时间：2024-08-03 16:25:11';

-- ----------------------------
-- Records of kube_config_version
-- ----------------------------

-- ----------------------------
-- Table structure for kube_domain
-- ----------------------------
DROP TABLE IF EXISTS `kube_domain`;
CREATE TABLE `kube_domain` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `domain` varchar(255) NOT NULL COMMENT '域名',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='集群域名\r\n创建人：xiaoke\r\n创建时间：2024-08-18 02:17:02';

-- ----------------------------
-- Records of kube_domain
-- ----------------------------

-- ----------------------------
-- Table structure for kube_error_record
-- ----------------------------
DROP TABLE IF EXISTS `kube_error_record`;
CREATE TABLE `kube_error_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `namespace` varchar(100) DEFAULT NULL COMMENT '命名空间',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `read_flag` tinyint(4) DEFAULT '0' COMMENT '已读',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='异常记录\r\n创建人：xiaoke\r\n创建时间：2024-08-22 23:04:46';

-- ----------------------------
-- Records of kube_error_record
-- ----------------------------

-- ----------------------------
-- Table structure for kube_namespace
-- ----------------------------
DROP TABLE IF EXISTS `kube_namespace`;
CREATE TABLE `kube_namespace` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` int(11) NOT NULL COMMENT '项目id',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `sign` varchar(100) DEFAULT NULL COMMENT '标识',
  `cpu_request` double DEFAULT NULL COMMENT 'cpu请求',
  `cpu_limit` double DEFAULT NULL COMMENT 'cpu上限',
  `memory_request` int(11) DEFAULT NULL COMMENT '内存请求',
  `memory_limit` int(11) DEFAULT NULL COMMENT '内存上限',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='命名空间\r\n创建人：xiaoke\r\n创建时间：2024-08-03 15:00:11';

-- ----------------------------
-- Records of kube_namespace
-- ----------------------------

-- ----------------------------
-- Table structure for kube_project
-- ----------------------------
DROP TABLE IF EXISTS `kube_project`;
CREATE TABLE `kube_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `color` varchar(20) DEFAULT NULL COMMENT '颜色',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目\r\n创建人：xiaoke\r\n创建时间：2024-08-03 14:21:40';

-- ----------------------------
-- Records of kube_project
-- ----------------------------

-- ----------------------------
-- Table structure for kube_project_user
-- ----------------------------
DROP TABLE IF EXISTS `kube_project_user`;
CREATE TABLE `kube_project_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `follow_flag` tinyint(4) DEFAULT '0' COMMENT '是否关注',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目人员\r\n创建人：xiaoke\r\n创建时间：2024-08-27 00:01:29';

-- ----------------------------
-- Records of kube_project_user
-- ----------------------------

-- ----------------------------
-- Table structure for kube_push
-- ----------------------------
DROP TABLE IF EXISTS `kube_push`;
CREATE TABLE `kube_push` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(20) DEFAULT NULL,
  `type` varchar(255) NOT NULL COMMENT '类型（email=邮件，sms=短信，phone=电话，wx=微信）',
  `open` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='推送';

-- ----------------------------
-- Records of kube_push
-- ----------------------------
INSERT INTO `kube_push` VALUES ('1', '邮件', 'email', '1');
INSERT INTO `kube_push` VALUES ('2', '短信', 'sms', '1');
INSERT INTO `kube_push` VALUES ('3', '电话', 'phone', '1');
INSERT INTO `kube_push` VALUES ('4', '微信', 'wx', '1');

-- ----------------------------
-- Table structure for kube_push_channel
-- ----------------------------
DROP TABLE IF EXISTS `kube_push_channel`;
CREATE TABLE `kube_push_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `push_id` int(11) NOT NULL COMMENT '推送ID',
  `type` varchar(255) NOT NULL COMMENT '类型',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `params` longtext NOT NULL COMMENT '参数',
  `weight` int(11) NOT NULL COMMENT '权重',
  `open` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否开启',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送渠道';

-- ----------------------------
-- Records of kube_push_channel
-- ----------------------------

-- ----------------------------
-- Table structure for kube_push_user
-- ----------------------------
DROP TABLE IF EXISTS `kube_push_user`;
CREATE TABLE `kube_push_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `namespace` varchar(100) NOT NULL COMMENT '命名空间',
  `project_user_id` int(11) NOT NULL COMMENT '用户ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `push_type` varchar(255) NOT NULL COMMENT '推送类型',
  `content_type` int(11) DEFAULT NULL COMMENT '内容类型（0=异常，1=操作，2=全部）',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送接收人\r\n创建人：xiaoke\r\n创建时间：2024-08-28 00:33:50';

-- ----------------------------
-- Records of kube_push_user
-- ----------------------------

-- ----------------------------
-- Table structure for kube_storage
-- ----------------------------
DROP TABLE IF EXISTS `kube_storage`;
CREATE TABLE `kube_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `namespace` varchar(100) NOT NULL COMMENT '命名空间',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `capacity` int(11) NOT NULL COMMENT '容量',
  `strategy` varchar(20) NOT NULL COMMENT '读写策略',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  `tenant_id` varchar(200) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储\r\n创建人：xiaoke\r\n创建时间：2024-08-03 16:59:32';

-- ----------------------------
-- Records of kube_storage
-- ----------------------------

-- ----------------------------
-- Table structure for kube_template
-- ----------------------------
DROP TABLE IF EXISTS `kube_template`;
CREATE TABLE `kube_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `type` int(11) NOT NULL COMMENT '类型',
  `content` longtext NOT NULL COMMENT '内容',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板\r\n创建人：xiaoke\r\n创建时间：2024-08-17 21:22:43';

-- ----------------------------
-- Records of kube_template
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL,
  `system` char(1) DEFAULT '0',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=495 DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', 'log_type', '日志类型', '2019-03-19 11:06:44', '2020-07-14 00:32:07', '异常、正常', '1', '0');
INSERT INTO `sys_dict` VALUES ('11', 'param_type', '参数配置', '2019-04-29 18:20:47', '2020-07-14 00:32:07', '检索、原文、报表、安全、文档、消息、其他', '1', '0');
INSERT INTO `sys_dict` VALUES ('12', 'status_type', '状态', '2019-05-15 16:31:08', '2019-05-15 16:31:08', '状态', '1', '0');
INSERT INTO `sys_dict` VALUES ('13', 'dict_type', '字典类型', '2019-05-16 14:16:20', '2020-07-14 00:32:07', '系统类不能修改', '1', '0');
INSERT INTO `sys_dict` VALUES ('488', 'error_type', '异常类型', '2020-08-24 21:46:07', '2020-08-24 21:46:07', '异常类型', '0', '0');
INSERT INTO `sys_dict` VALUES ('489', 'dispatch_type', '调度类型', '2020-11-19 13:13:18', '2020-11-19 13:13:18', '调度类型', '0', '0');
INSERT INTO `sys_dict` VALUES ('490', 'login_type', '登录类型', '2022-02-21 11:41:40', '2022-02-21 11:41:40', '登录类型', '0', '0');
INSERT INTO `sys_dict` VALUES ('491', 'examine_state', '审核状态', '2022-11-29 10:40:08', '2022-11-29 10:40:08', '审核状态', '0', '0');
INSERT INTO `sys_dict` VALUES ('492', 'build_state', '编译构建状态', '2024-08-04 22:59:19', '2024-08-04 22:59:19', '编译构建状态', '0', '0');
INSERT INTO `sys_dict` VALUES ('493', 'use_port', '内置端口', '2024-08-07 17:14:07', '2024-08-07 17:14:07', '内置端口', '0', '0');
INSERT INTO `sys_dict` VALUES ('494', 'app_state', '应用状态', '2024-08-09 13:47:11', '2024-08-09 13:47:11', '应用状态', '0', '0');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `dict_id` int(11) NOT NULL,
  `value` varchar(100) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `sort` int(10) NOT NULL DEFAULT '0' COMMENT '排序（升序）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`) USING BTREE,
  KEY `sys_dict_label` (`label`) USING BTREE,
  KEY `sys_dict_del_flag` (`del_flag`) USING BTREE,
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3300 DEFAULT CHARSET=utf8mb4 COMMENT='字典项';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES ('1', '1', '9', '异常', 'log_type', '日志异常', '1', '2019-03-19 11:08:59', '2019-03-25 12:49:13', '', '0');
INSERT INTO `sys_dict_item` VALUES ('2', '1', '0', '正常', 'log_type', '日志正常', '0', '2019-03-19 11:09:17', '2019-03-25 12:49:18', '', '0');
INSERT INTO `sys_dict_item` VALUES ('29', '11', '1', '检索', 'param_type', '检索', '0', '2019-04-29 18:22:17', '2019-04-29 18:22:17', '检索', '0');
INSERT INTO `sys_dict_item` VALUES ('30', '11', '2', '原文', 'param_type', '原文', '0', '2019-04-29 18:22:27', '2019-04-29 18:22:27', '原文', '0');
INSERT INTO `sys_dict_item` VALUES ('31', '11', '3', '报表', 'param_type', '报表', '0', '2019-04-29 18:22:36', '2019-04-29 18:22:36', '报表', '0');
INSERT INTO `sys_dict_item` VALUES ('32', '11', '4', '安全', 'param_type', '安全', '0', '2019-04-29 18:22:46', '2019-04-29 18:22:46', '安全', '0');
INSERT INTO `sys_dict_item` VALUES ('33', '11', '5', '文档', 'param_type', '文档', '0', '2019-04-29 18:22:56', '2019-04-29 18:22:56', '文档', '0');
INSERT INTO `sys_dict_item` VALUES ('34', '11', '6', '消息', 'param_type', '消息', '0', '2019-04-29 18:23:05', '2019-04-29 18:23:05', '消息', '0');
INSERT INTO `sys_dict_item` VALUES ('35', '11', '9', '其他', 'param_type', '其他', '0', '2019-04-29 18:23:16', '2019-04-29 18:23:16', '其他', '0');
INSERT INTO `sys_dict_item` VALUES ('36', '11', '0', '默认', 'param_type', '默认', '0', '2019-04-29 18:23:30', '2019-04-29 18:23:30', '默认', '0');
INSERT INTO `sys_dict_item` VALUES ('37', '12', '0', '正常', 'status_type', '状态正常', '0', '2019-05-15 16:31:34', '2019-05-16 22:30:46', '状态正常', '0');
INSERT INTO `sys_dict_item` VALUES ('38', '12', '9', '冻结', 'status_type', '状态冻结', '1', '2019-05-15 16:31:56', '2019-05-16 22:30:50', '状态冻结', '0');
INSERT INTO `sys_dict_item` VALUES ('39', '13', '1', '系统类', 'dict_type', '系统类字典', '0', '2019-05-16 14:20:40', '2019-05-16 14:20:40', '不能修改删除', '0');
INSERT INTO `sys_dict_item` VALUES ('40', '13', '0', '业务类', 'dict_type', '业务类字典', '0', '2019-05-16 14:20:59', '2019-05-16 14:20:59', '可以修改', '0');
INSERT INTO `sys_dict_item` VALUES ('3274', '488', '0', '前端', 'error_type', 'warning', '0', '2020-08-24 21:46:24', '2020-08-24 21:46:24', '前端', '0');
INSERT INTO `sys_dict_item` VALUES ('3275', '488', '1', '后端', 'error_type', 'success', '10', '2020-08-24 21:46:38', '2020-08-24 21:46:38', '后端', '0');
INSERT INTO `sys_dict_item` VALUES ('3276', '489', 'GoodsShowHandler', '商品定时上架', 'dispatch_type', '商品定时上架', '10', '2020-11-19 13:14:12', '2020-11-19 13:14:12', '商品定时上架', '0');
INSERT INTO `sys_dict_item` VALUES ('3277', '490', 'LOGIN', '登录', 'login_type', '登录', '10', '2022-02-21 11:42:14', '2022-02-21 11:42:14', '登录', '0');
INSERT INTO `sys_dict_item` VALUES ('3278', '490', 'OUT', '登出', 'login_type', '登出', '20', '2022-02-21 11:42:32', '2022-02-21 11:42:32', '登出', '0');
INSERT INTO `sys_dict_item` VALUES ('3279', '491', '10', '审核中', 'examine_state', '审核中', '10', '2022-11-29 10:40:26', '2022-11-29 10:40:26', '审核中', '0');
INSERT INTO `sys_dict_item` VALUES ('3280', '491', '20', '审核通过', 'examine_state', '审核通过', '20', '2022-11-29 10:40:39', '2022-11-29 10:40:39', '审核通过', '0');
INSERT INTO `sys_dict_item` VALUES ('3281', '491', '30', '审核驳回', 'examine_state', '审核驳回', '30', '2022-11-29 10:40:56', '2022-11-29 10:40:56', '审核驳回', '0');
INSERT INTO `sys_dict_item` VALUES ('3282', '492', '0', '新建', 'build_state', '#1F8ECF', '0', '2024-08-04 22:59:54', '2024-08-17 23:00:54', '新建', '0');
INSERT INTO `sys_dict_item` VALUES ('3283', '492', '1', '运行中', 'build_state', '#ff9900', '1', '2024-08-04 23:00:04', '2024-08-17 22:45:37', '运行中', '0');
INSERT INTO `sys_dict_item` VALUES ('3284', '492', '2', '成功', 'build_state', '#19be6b', '2', '2024-08-04 23:00:21', '2024-08-17 22:45:43', '成功', '0');
INSERT INTO `sys_dict_item` VALUES ('3285', '492', '3', '失败', 'build_state', '#ed4014', '3', '2024-08-04 23:00:35', '2024-08-17 22:45:54', '失败', '0');
INSERT INTO `sys_dict_item` VALUES ('3286', '492', '4', '停止', 'build_state', '#808695', '4', '2024-08-04 23:00:50', '2024-08-17 22:46:03', '停止', '0');
INSERT INTO `sys_dict_item` VALUES ('3287', '492', '5', '创建中', 'build_state', '#5cadff', '5', '2024-08-04 23:01:04', '2024-08-17 22:46:12', '创建中', '0');
INSERT INTO `sys_dict_item` VALUES ('3288', '492', '6', '停止中', 'build_state', '#17233d', '6', '2024-08-04 23:01:18', '2024-08-17 22:46:15', '停止中', '0');
INSERT INTO `sys_dict_item` VALUES ('3289', '493', '30001', 'Dashboard', 'use_port', 'Dashboard', '10', '2024-08-07 17:17:21', '2024-08-07 17:17:21', 'Dashboard', '0');
INSERT INTO `sys_dict_item` VALUES ('3290', '493', '30100', 'web-terminal', 'use_port', 'web-terminal', '20', '2024-08-07 17:17:33', '2024-08-07 17:17:33', 'web-terminal', '0');
INSERT INTO `sys_dict_item` VALUES ('3291', '493', '30601', 'kibana', 'use_port', 'kibana', '30', '2024-08-07 17:17:46', '2024-08-07 17:17:46', 'kibana', '0');
INSERT INTO `sys_dict_item` VALUES ('3292', '493', '30007', 'k8s监控', 'use_port', 'k8s监控', '40', '2024-08-07 17:17:59', '2024-08-07 17:17:59', 'k8s监控', '0');
INSERT INTO `sys_dict_item` VALUES ('3293', '494', '10', '创建中', 'app_state', '#1F8ECF', '10', '2024-08-09 13:48:02', '2024-08-09 17:33:13', '创建中', '0');
INSERT INTO `sys_dict_item` VALUES ('3294', '494', '20', '运行中', 'app_state', '#19be6b', '20', '2024-08-09 13:48:59', '2024-08-09 13:48:59', '运行中', '0');
INSERT INTO `sys_dict_item` VALUES ('3295', '494', '30', '停止中', 'app_state', '#808695', '30', '2024-08-09 13:49:16', '2024-08-09 13:49:16', '停止中', '0');
INSERT INTO `sys_dict_item` VALUES ('3296', '494', '40', '已停止', 'app_state', '#808695', '40', '2024-08-09 13:49:33', '2024-08-09 13:49:33', '已停止', '0');
INSERT INTO `sys_dict_item` VALUES ('3297', '494', '50', '重启中', 'app_state', '#ff9900', '50', '2024-08-09 13:49:54', '2024-08-09 13:49:54', '重启中', '0');
INSERT INTO `sys_dict_item` VALUES ('3298', '494', '60', '异常', 'app_state', 'app_state', '60', '2024-08-09 13:50:10', '2024-08-09 13:50:10', '异常', '0');
INSERT INTO `sys_dict_item` VALUES ('3299', '494', '70', '更新中', 'app_state', '#2d8cf0', '70', '2024-08-09 13:50:22', '2024-08-09 13:50:22', '更新中', '0');

-- ----------------------------
-- Table structure for sys_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_error`;
CREATE TABLE `sys_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` longtext,
  `type` varchar(10) NOT NULL COMMENT '类型',
  `path` varchar(255) NOT NULL COMMENT '路径',
  `message` longtext NOT NULL COMMENT '异常信息',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='异常';

-- ----------------------------
-- Records of sys_error
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) CHARACTER SET utf8 DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '日志标题',
  `create_by` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remote_addr` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(1000) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作方式',
  `params` text CHARACTER SET utf8 COMMENT '操作提交的数据',
  `time` mediumtext CHARACTER SET utf8 COMMENT '执行时间',
  `del_flag` char(1) CHARACTER SET utf8 DEFAULT '0' COMMENT '删除标记',
  `exception` text CHARACTER SET utf8 COMMENT '异常信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(10) NOT NULL COMMENT '类型',
  `ip` varchar(50) NOT NULL COMMENT 'IP',
  `source` varchar(50) DEFAULT NULL COMMENT '来源',
  `token` varchar(200) NOT NULL COMMENT 'Token',
  `param` longtext COMMENT '参数',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`),
  KEY `type` (`type`) USING BTREE,
  KEY `source` (`source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` varchar(32) DEFAULT NULL,
  `permission` varchar(32) DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(200) DEFAULT NULL,
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `keep_alive` char(1) DEFAULT '0',
  `type` char(1) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10382 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1101', '用户新增', 'user_user_add', null, '10380', null, null, '0', '1', '2017-11-08 09:52:09', '2024-09-01 14:49:14', '0');
INSERT INTO `sys_menu` VALUES ('1102', '用户修改', 'user_user_edit', null, '10380', null, null, '0', '1', '2017-11-08 09:52:48', '2024-09-01 14:49:14', '0');
INSERT INTO `sys_menu` VALUES ('1103', '用户删除', 'user_user_del', null, '10380', null, null, '0', '1', '2017-11-08 09:54:01', '2024-09-01 14:49:14', '0');
INSERT INTO `sys_menu` VALUES ('1200', '菜单', 'menu', '', '10370', '', '110', '0', '1', '2017-11-08 09:57:27', '2024-09-01 14:51:30', '0');
INSERT INTO `sys_menu` VALUES ('1201', '菜单新增', 'sys_menu_add', null, '1200', null, null, '0', '1', '2017-11-08 10:15:53', '2018-09-28 09:07:16', '0');
INSERT INTO `sys_menu` VALUES ('1202', '菜单修改', 'sys_menu_edit', null, '1200', null, null, '0', '1', '2017-11-08 10:16:23', '2018-09-28 09:07:18', '0');
INSERT INTO `sys_menu` VALUES ('1203', '菜单删除', 'sys_menu_del', null, '1200', null, null, '0', '1', '2017-11-08 10:16:43', '2018-09-28 09:07:22', '0');
INSERT INTO `sys_menu` VALUES ('1300', '角色', 'role', '/auth/role', '10370', '', '120', '0', '1', '2017-11-08 10:13:37', '2024-09-01 14:52:21', '0');
INSERT INTO `sys_menu` VALUES ('1301', '角色新增', 'sys_role_add', null, '1300', null, null, '0', '1', '2017-11-08 10:14:18', '2018-09-28 09:07:46', '0');
INSERT INTO `sys_menu` VALUES ('1302', '角色修改', 'sys_role_edit', null, '1300', null, null, '0', '1', '2017-11-08 10:14:41', '2018-09-28 09:07:49', '0');
INSERT INTO `sys_menu` VALUES ('1303', '角色删除', 'sys_role_del', null, '1300', null, null, '0', '1', '2017-11-08 10:14:59', '2018-09-28 09:07:53', '0');
INSERT INTO `sys_menu` VALUES ('1304', '分配权限', 'sys_role_perm', null, '1300', null, null, '0', '1', '2018-04-20 07:22:55', '2018-09-28 09:13:23', '0');
INSERT INTO `sys_menu` VALUES ('2000', '系统管理', null, '/system', '-1', '/images/icon/system.svg', '100', '0', '0', '2017-11-07 20:56:00', '2018-09-28 08:53:18', '0');
INSERT INTO `sys_menu` VALUES ('2100', '日志管理', null, '/system/log', '2000', '', '5', '0', '0', '2017-11-20 14:06:22', '2018-09-28 09:01:52', '0');
INSERT INTO `sys_menu` VALUES ('2101', '日志删除', 'sys_log_del', null, '2100', null, null, '0', '1', '2017-11-20 20:37:37', '2018-09-28 09:08:44', '0');
INSERT INTO `sys_menu` VALUES ('2200', '字典管理', null, '/system/dict', '2000', '', '6', '0', '0', '2017-11-29 11:30:52', '2018-09-28 09:01:47', '0');
INSERT INTO `sys_menu` VALUES ('2201', '字典删除', 'sys_dict_del', null, '2200', null, null, '0', '1', '2017-11-29 11:30:11', '2018-09-28 09:09:10', '0');
INSERT INTO `sys_menu` VALUES ('2202', '字典新增', 'sys_dict_add', null, '2200', null, null, '0', '1', '2018-05-11 22:34:55', '2018-09-28 09:09:12', '0');
INSERT INTO `sys_menu` VALUES ('2203', '字典修改', 'sys_dict_edit', null, '2200', null, null, '0', '1', '2018-05-11 22:36:03', '2018-09-28 09:09:16', '0');
INSERT INTO `sys_menu` VALUES ('2210', '参数管理', null, '/system/param', '2000', '', '7', '1', '0', '2019-04-29 22:16:50', '2019-06-24 14:52:00', '0');
INSERT INTO `sys_menu` VALUES ('2211', '参数新增', 'admin_syspublicparam_add', null, '2210', null, '1', '0', '1', '2019-04-29 22:17:36', null, '0');
INSERT INTO `sys_menu` VALUES ('2212', '参数删除', 'admin_syspublicparam_del', null, '2210', null, '1', '0', '1', '2019-04-29 22:17:55', null, '0');
INSERT INTO `sys_menu` VALUES ('2213', '参数编辑', 'admin_syspublicparam_edit', null, '2210', null, '1', '0', '1', '2019-04-29 22:18:14', '2019-04-29 22:19:25', '0');
INSERT INTO `sys_menu` VALUES ('2600', '令牌管理', null, '/system/token', '2000', '', '11', '0', '0', '2018-09-04 05:58:41', '2018-09-28 09:01:38', '0');
INSERT INTO `sys_menu` VALUES ('2601', '令牌删除', 'token_token_del', null, '2600', null, '1', '0', '1', '2018-09-04 05:59:50', '2018-09-28 09:11:24', '0');
INSERT INTO `sys_menu` VALUES ('3631', '删除日志', 'daemon_execution_log_del', null, '3900', '1', '2', '0', '1', '2018-05-15 21:35:18', '2019-03-30 09:30:45', '0');
INSERT INTO `sys_menu` VALUES ('10157', '审核', 'systenant_check', null, '10052', null, '1', '0', '1', '2020-06-16 10:08:43', null, '0');
INSERT INTO `sys_menu` VALUES ('10189', '用户查看', 'user_user_view', null, '10380', null, '1', '0', '1', '2020-07-18 16:21:59', '2024-09-01 14:49:14', '0');
INSERT INTO `sys_menu` VALUES ('10190', '令牌查看', 'token_token_view', null, '2600', null, '1', '0', '1', '2020-07-18 18:47:33', null, '0');
INSERT INTO `sys_menu` VALUES ('10206', '异常管理', null, '/system/error', '2000', '', '100', '1', '0', '2020-08-24 21:41:21', '2020-12-29 13:50:45', '0');
INSERT INTO `sys_menu` VALUES ('10207', '查看', 'system_error_view', null, '10206', null, '1', '0', '1', '2020-08-24 21:41:50', null, '0');
INSERT INTO `sys_menu` VALUES ('10208', '添加', 'system_error_add', null, '10206', null, '1', '0', '1', '2020-08-24 21:42:05', null, '0');
INSERT INTO `sys_menu` VALUES ('10209', '修改', 'system_error_edit', null, '10206', null, '1', '0', '1', '2020-08-24 21:42:18', null, '0');
INSERT INTO `sys_menu` VALUES ('10210', '删除', 'system_error_del', null, '10206', null, '1', '0', '1', '2020-08-24 21:42:32', null, '0');
INSERT INTO `sys_menu` VALUES ('10265', '总览', null, '/dashboard/console', '-1', 'i-icon-kube i-icon-kube-zonglan_Default', '0', '1', '0', '2021-01-20 15:19:35', '2021-06-08 11:43:49', '0');
INSERT INTO `sys_menu` VALUES ('10266', '重置密码', 'user_user_resetPassword', null, '10380', null, '50', '0', '1', '2022-02-21 10:39:11', '2024-09-01 14:49:14', '0');
INSERT INTO `sys_menu` VALUES ('10267', '登录日志', null, '/system/login-log', '2000', '', '200', '0', '0', '2022-02-21 11:09:39', '2022-12-17 14:27:50', '0');
INSERT INTO `sys_menu` VALUES ('10268', '查看', 'system_loginLog_view', null, '10267', null, '10', '0', '1', '2022-02-21 11:10:17', null, '0');
INSERT INTO `sys_menu` VALUES ('10269', '删除', 'system_loginLog_del', null, '10267', null, '20', '0', '1', '2022-02-21 11:10:34', null, '0');
INSERT INTO `sys_menu` VALUES ('10285', '项目', 'project', '/kube/project', '10370', null, '160', '0', '1', '2024-08-03 14:15:19', '2024-09-01 14:56:59', '0');
INSERT INTO `sys_menu` VALUES ('10286', '查看项目', 'kube_project_view', null, '10285', null, '1', '0', '1', '2024-08-03 14:21:49', null, '0');
INSERT INTO `sys_menu` VALUES ('10287', '添加项目', 'kube_project_add', null, '10285', null, '1', '0', '1', '2024-08-03 14:21:49', null, '0');
INSERT INTO `sys_menu` VALUES ('10288', '编辑项目', 'kube_project_edit', null, '10285', null, '1', '0', '1', '2024-08-03 14:21:50', null, '0');
INSERT INTO `sys_menu` VALUES ('10289', '删除项目', 'kube_project_del', null, '10285', null, '1', '0', '1', '2024-08-03 14:21:50', null, '0');
INSERT INTO `sys_menu` VALUES ('10290', '命名空间', 'namespace', '/kube/namespace', '10370', null, '170', '0', '1', '2024-08-03 14:53:05', '2024-09-01 14:57:36', '0');
INSERT INTO `sys_menu` VALUES ('10291', '查看命名空间', 'kube_namespace_view', null, '10290', null, '1', '0', '1', '2024-08-03 15:00:33', null, '0');
INSERT INTO `sys_menu` VALUES ('10292', '添加命名空间', 'kube_namespace_add', null, '10290', null, '1', '0', '1', '2024-08-03 15:00:33', null, '0');
INSERT INTO `sys_menu` VALUES ('10293', '编辑命名空间', 'kube_namespace_edit', null, '10290', null, '1', '0', '1', '2024-08-03 15:00:34', null, '0');
INSERT INTO `sys_menu` VALUES ('10294', '删除命名空间', 'kube_namespace_del', null, '10290', null, '1', '0', '1', '2024-08-03 15:00:34', null, '0');
INSERT INTO `sys_menu` VALUES ('10296', '查看应用', 'kube_app_view', null, '10371', null, '30', '0', '1', '2024-08-03 15:34:00', '2024-09-01 14:37:37', '0');
INSERT INTO `sys_menu` VALUES ('10297', '添加应用', 'kube_app_add', null, '10371', null, '40', '0', '1', '2024-08-03 15:34:00', '2024-09-01 14:37:37', '0');
INSERT INTO `sys_menu` VALUES ('10298', '编辑应用', 'kube_app_edit', null, '10371', null, '50', '0', '1', '2024-08-03 15:34:01', '2024-09-01 14:37:37', '0');
INSERT INTO `sys_menu` VALUES ('10299', '删除应用', 'kube_app_del', null, '10371', null, '60', '0', '1', '2024-08-03 15:34:01', '2024-09-01 14:37:37', '0');
INSERT INTO `sys_menu` VALUES ('10300', '配置管理', null, '/kube/config', '-1', 'i-icon-kube i-icon-kube-peizhiguanli_Default', '20', '0', '0', '2024-08-03 16:03:55', '2024-08-08 01:49:06', '0');
INSERT INTO `sys_menu` VALUES ('10301', '查看配置', 'kube_config_view', null, '10372', null, '1', '0', '1', '2024-08-03 16:10:48', '2024-09-01 14:38:52', '0');
INSERT INTO `sys_menu` VALUES ('10302', '添加配置', 'kube_config_add', null, '10372', null, '1', '0', '1', '2024-08-03 16:10:49', '2024-09-01 14:38:52', '0');
INSERT INTO `sys_menu` VALUES ('10303', '编辑配置', 'kube_config_edit', null, '10372', null, '1', '0', '1', '2024-08-03 16:10:49', '2024-09-01 14:38:52', '0');
INSERT INTO `sys_menu` VALUES ('10304', '删除配置', 'kube_config_del', null, '10372', null, '1', '0', '1', '2024-08-03 16:10:50', '2024-09-01 14:38:53', '0');
INSERT INTO `sys_menu` VALUES ('10305', '存储管理', null, '/kube/storage', '-1', 'i-icon-kube i-icon-kube-cunchuguanli', '30', '0', '0', '2024-08-03 16:52:38', '2024-08-08 01:49:39', '0');
INSERT INTO `sys_menu` VALUES ('10306', '查看存储', 'kube_storage_view', null, '10373', null, '1', '0', '1', '2024-08-03 17:00:00', '2024-09-01 14:39:40', '0');
INSERT INTO `sys_menu` VALUES ('10307', '添加存储', 'kube_storage_add', null, '10373', null, '1', '0', '1', '2024-08-03 17:00:01', '2024-09-01 14:39:40', '0');
INSERT INTO `sys_menu` VALUES ('10308', '编辑存储', 'kube_storage_edit', null, '10373', null, '1', '0', '1', '2024-08-03 17:00:01', '2024-09-01 14:39:40', '0');
INSERT INTO `sys_menu` VALUES ('10309', '删除存储', 'kube_storage_del', null, '10373', null, '1', '0', '1', '2024-08-03 17:00:02', '2024-09-01 14:39:40', '0');
INSERT INTO `sys_menu` VALUES ('10316', '查看凭证', 'kube_credential_view', null, '10375', null, '1', '0', '1', '2024-08-04 23:25:48', '2024-09-01 14:42:27', '0');
INSERT INTO `sys_menu` VALUES ('10317', '添加凭证', 'kube_credential_add', null, '10375', null, '1', '0', '1', '2024-08-04 23:25:48', '2024-09-01 14:42:27', '0');
INSERT INTO `sys_menu` VALUES ('10318', '编辑凭证', 'kube_credential_edit', null, '10375', null, '1', '0', '1', '2024-08-04 23:25:49', '2024-09-01 14:42:27', '0');
INSERT INTO `sys_menu` VALUES ('10319', '删除凭证', 'kube_credential_del', null, '10375', null, '1', '0', '1', '2024-08-04 23:25:49', '2024-09-01 14:42:27', '0');
INSERT INTO `sys_menu` VALUES ('10320', '分配集群', 'kube_project_colony', null, '10285', null, '50', '0', '1', '2024-08-07 12:36:54', null, '0');
INSERT INTO `sys_menu` VALUES ('10321', '无状态应用', null, '/kube/app/Deployment', '10323', null, '10', '0', '0', '2024-08-07 16:07:17', '2024-08-08 01:48:09', '0');
INSERT INTO `sys_menu` VALUES ('10322', '有状态应用', null, '/kube/app/StatefulSet', '10323', null, '20', '0', '0', '2024-08-07 16:07:46', '2024-08-08 01:48:11', '0');
INSERT INTO `sys_menu` VALUES ('10323', '应用管理', null, '/app', '-1', 'i-icon-kube i-icon-kube-yingyongguanli_Default', '10', '0', '0', '2024-08-08 01:46:30', '2024-08-09 00:19:48', '0');
INSERT INTO `sys_menu` VALUES ('10325', '添加弹性伸缩', 'kube_appStretch_add', null, '10371', null, '70', '0', '1', '2024-08-09 23:55:38', '2024-09-01 14:37:42', '0');
INSERT INTO `sys_menu` VALUES ('10326', '关闭弹性伸缩', 'kube_appStretch_del', null, '10371', null, '80', '0', '1', '2024-08-13 23:48:59', '2024-09-01 14:37:42', '0');
INSERT INTO `sys_menu` VALUES ('10327', '停止应用', 'kube_app_stop', null, '10371', null, '90', '0', '1', '2024-08-14 01:36:00', '2024-09-01 14:37:42', '0');
INSERT INTO `sys_menu` VALUES ('10328', '启动应用', 'kube_app_start', null, '10371', null, '100', '0', '1', '2024-08-14 01:36:11', '2024-09-01 14:37:42', '0');
INSERT INTO `sys_menu` VALUES ('10329', '重启应用', 'kube_app_restart', null, '10371', null, '110', '0', '1', '2024-08-14 01:36:25', '2024-09-01 14:37:42', '0');
INSERT INTO `sys_menu` VALUES ('10330', '模板', 'template', '/kube/template', '10370', null, '130', '0', '1', '2024-08-17 21:18:52', '2024-09-01 14:53:58', '0');
INSERT INTO `sys_menu` VALUES ('10331', '查看模板管理', 'kube_template_view', null, '10330', null, '1', '0', '1', '2024-08-17 21:23:42', null, '0');
INSERT INTO `sys_menu` VALUES ('10332', '添加模板管理', 'kube_template_add', null, '10330', null, '1', '0', '1', '2024-08-17 21:23:42', null, '0');
INSERT INTO `sys_menu` VALUES ('10333', '编辑模板管理', 'kube_template_edit', null, '10330', null, '1', '0', '1', '2024-08-17 21:23:43', null, '0');
INSERT INTO `sys_menu` VALUES ('10334', '删除模板管理', 'kube_template_del', null, '10330', null, '1', '0', '1', '2024-08-17 21:23:43', null, '0');
INSERT INTO `sys_menu` VALUES ('10339', '域名', null, '/kube/domain', '-1', 'i-icon-kube i-icon-kube-yuming', '40', '0', '0', '2024-08-19 00:02:15', '2024-08-19 00:02:28', '0');
INSERT INTO `sys_menu` VALUES ('10340', '添加域名', 'kube_domain_add', null, '10374', null, '10', '0', '1', '2024-08-19 00:03:10', '2024-09-01 14:40:42', '0');
INSERT INTO `sys_menu` VALUES ('10341', '修改域名', 'kube_domain_edit', null, '10374', null, '20', '0', '1', '2024-08-19 00:03:24', '2024-09-01 14:40:42', '0');
INSERT INTO `sys_menu` VALUES ('10342', '删除域名', 'kube_domain_del', null, '10374', null, '30', '0', '1', '2024-08-19 00:03:41', '2024-09-01 14:40:42', '0');
INSERT INTO `sys_menu` VALUES ('10343', '查看应用域名', 'kube_appDomain_view', null, '10374', null, '40', '0', '1', '2024-08-19 00:43:57', '2024-09-01 14:40:42', '0');
INSERT INTO `sys_menu` VALUES ('10344', '添加应用域名', 'kube_appDomain_add', null, '10374', null, '50', '0', '1', '2024-08-19 00:44:09', '2024-09-01 14:40:42', '0');
INSERT INTO `sys_menu` VALUES ('10345', '修改应用域名', 'kube_appDomain_edit', null, '10374', null, '60', '0', '1', '2024-08-19 00:44:21', '2024-09-01 14:40:42', '0');
INSERT INTO `sys_menu` VALUES ('10346', '删除应用域名', 'kube_appDomain_del', null, '10374', null, '70', '0', '1', '2024-08-19 00:44:41', '2024-09-01 14:40:42', '0');
INSERT INTO `sys_menu` VALUES ('10352', '操作管理', null, '/handle', '-1', 'i-icon-kube i-icon-kube-caozuoguanli_Default', '70', '0', '0', '2024-08-22 22:59:27', '2024-08-22 23:12:40', '0');
INSERT INTO `sys_menu` VALUES ('10353', '异常记录', null, '/kube/error-record', '10352', null, '10', '0', '0', '2024-08-22 23:00:13', null, '0');
INSERT INTO `sys_menu` VALUES ('10354', '查看异常记录', 'kube_errorRecord_view', null, '10378', null, '1', '0', '1', '2024-08-22 23:06:00', '2024-09-01 14:46:31', '0');
INSERT INTO `sys_menu` VALUES ('10357', '删除异常记录', 'kube_errorRecord_del', null, '10378', null, '1', '0', '1', '2024-08-22 23:06:02', '2024-09-01 14:46:31', '0');
INSERT INTO `sys_menu` VALUES ('10358', '查看项目人员', 'kube_projectUser_view', null, '10285', null, '60', '0', '1', '2024-08-27 00:08:07', null, '0');
INSERT INTO `sys_menu` VALUES ('10359', '添加项目人员', 'kube_projectUser_add', null, '10285', null, '70', '0', '1', '2024-08-27 00:08:28', null, '0');
INSERT INTO `sys_menu` VALUES ('10360', '修改项目人员', 'kube_projectUser_edit', null, '10285', null, '80', '0', '1', '2024-08-27 00:08:45', null, '0');
INSERT INTO `sys_menu` VALUES ('10361', '删除项目人员', 'kube_projectUser_del', null, '10285', null, '90', '0', '1', '2024-08-27 00:09:05', null, '0');
INSERT INTO `sys_menu` VALUES ('10362', '推送设置', 'push', '/push', '10370', null, '140', '0', '1', '2024-08-28 00:05:35', '2024-09-01 14:54:36', '0');
INSERT INTO `sys_menu` VALUES ('10363', '查看推送', 'kube_push_view', null, '10362', null, '10', '0', '1', '2024-08-28 00:06:29', null, '0');
INSERT INTO `sys_menu` VALUES ('10364', '修改推送', 'kube_push_edit', null, '10362', null, '20', '0', '1', '2024-08-28 00:06:50', null, '0');
INSERT INTO `sys_menu` VALUES ('10365', '推送接收人', null, '/kube/push-user', '-1', 'i-icon-kube i-icon-kube-a-UsersGroupRounded', '80', '0', '0', '2024-08-28 00:26:07', '2024-08-28 00:37:17', '0');
INSERT INTO `sys_menu` VALUES ('10366', '查看推送接收人', 'kube_pushUser_view', null, '10379', null, '1', '0', '1', '2024-08-28 00:33:57', '2024-09-01 14:47:29', '0');
INSERT INTO `sys_menu` VALUES ('10367', '添加推送接收人', 'kube_pushUser_add', null, '10379', null, '1', '0', '1', '2024-08-28 00:33:58', '2024-09-01 14:47:29', '0');
INSERT INTO `sys_menu` VALUES ('10368', '编辑推送接收人', 'kube_pushUser_edit', null, '10379', null, '1', '0', '1', '2024-08-28 00:33:58', '2024-09-01 14:47:29', '0');
INSERT INTO `sys_menu` VALUES ('10369', '删除推送接收人', 'kube_pushUser_del', null, '10379', null, '1', '0', '1', '2024-08-28 00:33:59', '2024-09-01 14:47:29', '0');
INSERT INTO `sys_menu` VALUES ('10370', '功能权限', 'permission', null, '-1', null, '1000', '0', '1', '2024-09-01 14:35:58', null, '0');
INSERT INTO `sys_menu` VALUES ('10371', '应用', 'app', null, '10370', null, '10', '0', '1', '2024-09-01 14:36:26', null, '0');
INSERT INTO `sys_menu` VALUES ('10372', '配置', 'config', null, '10370', null, '20', '0', '1', '2024-09-01 14:38:17', null, '0');
INSERT INTO `sys_menu` VALUES ('10373', '存储', 'storage', null, '10370', null, '30', '0', '1', '2024-09-01 14:39:13', null, '0');
INSERT INTO `sys_menu` VALUES ('10374', '域名', 'kube_domain_view', null, '10370', null, '40', '0', '1', '2024-09-01 14:40:05', '2024-09-11 00:55:56', '0');
INSERT INTO `sys_menu` VALUES ('10375', '凭证', 'certificate', null, '10370', null, '50', '0', '1', '2024-09-01 14:41:30', null, '0');
INSERT INTO `sys_menu` VALUES ('10378', '异常记录', 'error-record', null, '10370', null, '80', '0', '1', '2024-09-01 14:46:07', null, '0');
INSERT INTO `sys_menu` VALUES ('10379', '推送接收人', 'push-user', null, '10370', null, '90', '0', '1', '2024-09-01 14:47:13', null, '0');
INSERT INTO `sys_menu` VALUES ('10380', '用户', 'user', null, '10370', null, '100', '0', '1', '2024-09-01 14:48:41', '2024-09-01 14:48:57', '0');
INSERT INTO `sys_menu` VALUES ('10381', '监控', 'monitor', null, '10370', null, '180', '0', '1', '2024-09-01 15:26:00', null, '0');

-- ----------------------------
-- Table structure for sys_public_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_public_param`;
CREATE TABLE `sys_public_param` (
  `public_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `public_name` varchar(128) DEFAULT NULL,
  `public_key` varchar(128) DEFAULT NULL,
  `public_value` longtext,
  `status` char(1) DEFAULT '0',
  `validate_code` varchar(64) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `public_type` char(1) DEFAULT '0',
  `system` char(1) DEFAULT '0',
  `del_flag` char(1) DEFAULT '0',
  `tenant_id` int(11) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`public_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='公共参数配置表';

-- ----------------------------
-- Records of sys_public_param
-- ----------------------------
INSERT INTO `sys_public_param` VALUES ('1', '图片上传格式', 'IMAGE_FORMAT', 'png,jpg,jpeg,JPEG', '0', null, '2020-05-13 12:00:46', '2020-07-05 03:13:24', '9', '0', '0', '1');
INSERT INTO `sys_public_param` VALUES ('2', '文件上传格式', 'FILE_FORMAT', 'txt,doc,docx,pdf,xls,xlsx,mp4,MOV', '0', null, '2020-05-13 12:01:06', null, '9', '0', '0', '1');
INSERT INTO `sys_public_param` VALUES ('3', '文件上传大小', 'FILE_SIZE', '1024000000', '0', null, '2020-05-13 12:01:26', null, '9', '0', '0', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) DEFAULT NULL,
  `role_code` varchar(64) DEFAULT NULL,
  `role_desc` varchar(255) DEFAULT NULL,
  `ds_type` char(1) DEFAULT '2',
  `ds_scope` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) DEFAULT '0',
  `create_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '系统管理员', 'ROLE_ADMIN', '系统管理员', '0', '', '2017-10-29 15:45:51', '2022-07-29 14:04:13', '0', '2');
INSERT INTO `sys_role` VALUES ('2', '测试角色', 'ROLE_TEST', '测试角色', '0', null, '2022-09-20 16:45:32', '2024-08-23 02:01:35', '1', null);
INSERT INTO `sys_role` VALUES ('3', '容器云管理员', 'ROLE_KUBE', '容器云管理员', '0', null, '2024-08-27 01:37:34', null, '0', null);
INSERT INTO `sys_role` VALUES ('4', '运维', 'ROLE_DEVOPS', '运维', '0', null, '2024-09-01 15:30:10', null, '0', null);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1101');
INSERT INTO `sys_role_menu` VALUES ('1', '1102');
INSERT INTO `sys_role_menu` VALUES ('1', '1103');
INSERT INTO `sys_role_menu` VALUES ('1', '1200');
INSERT INTO `sys_role_menu` VALUES ('1', '1201');
INSERT INTO `sys_role_menu` VALUES ('1', '1202');
INSERT INTO `sys_role_menu` VALUES ('1', '1203');
INSERT INTO `sys_role_menu` VALUES ('1', '1300');
INSERT INTO `sys_role_menu` VALUES ('1', '1301');
INSERT INTO `sys_role_menu` VALUES ('1', '1302');
INSERT INTO `sys_role_menu` VALUES ('1', '1303');
INSERT INTO `sys_role_menu` VALUES ('1', '1304');
INSERT INTO `sys_role_menu` VALUES ('1', '2000');
INSERT INTO `sys_role_menu` VALUES ('1', '2100');
INSERT INTO `sys_role_menu` VALUES ('1', '2101');
INSERT INTO `sys_role_menu` VALUES ('1', '2200');
INSERT INTO `sys_role_menu` VALUES ('1', '2201');
INSERT INTO `sys_role_menu` VALUES ('1', '2202');
INSERT INTO `sys_role_menu` VALUES ('1', '2203');
INSERT INTO `sys_role_menu` VALUES ('1', '2210');
INSERT INTO `sys_role_menu` VALUES ('1', '2211');
INSERT INTO `sys_role_menu` VALUES ('1', '2212');
INSERT INTO `sys_role_menu` VALUES ('1', '2213');
INSERT INTO `sys_role_menu` VALUES ('1', '2600');
INSERT INTO `sys_role_menu` VALUES ('1', '2601');
INSERT INTO `sys_role_menu` VALUES ('1', '10189');
INSERT INTO `sys_role_menu` VALUES ('1', '10190');
INSERT INTO `sys_role_menu` VALUES ('1', '10206');
INSERT INTO `sys_role_menu` VALUES ('1', '10207');
INSERT INTO `sys_role_menu` VALUES ('1', '10208');
INSERT INTO `sys_role_menu` VALUES ('1', '10209');
INSERT INTO `sys_role_menu` VALUES ('1', '10210');
INSERT INTO `sys_role_menu` VALUES ('1', '10265');
INSERT INTO `sys_role_menu` VALUES ('1', '10266');
INSERT INTO `sys_role_menu` VALUES ('1', '10267');
INSERT INTO `sys_role_menu` VALUES ('1', '10268');
INSERT INTO `sys_role_menu` VALUES ('1', '10269');
INSERT INTO `sys_role_menu` VALUES ('1', '10285');
INSERT INTO `sys_role_menu` VALUES ('1', '10286');
INSERT INTO `sys_role_menu` VALUES ('1', '10287');
INSERT INTO `sys_role_menu` VALUES ('1', '10288');
INSERT INTO `sys_role_menu` VALUES ('1', '10289');
INSERT INTO `sys_role_menu` VALUES ('1', '10290');
INSERT INTO `sys_role_menu` VALUES ('1', '10291');
INSERT INTO `sys_role_menu` VALUES ('1', '10292');
INSERT INTO `sys_role_menu` VALUES ('1', '10293');
INSERT INTO `sys_role_menu` VALUES ('1', '10294');
INSERT INTO `sys_role_menu` VALUES ('1', '10296');
INSERT INTO `sys_role_menu` VALUES ('1', '10297');
INSERT INTO `sys_role_menu` VALUES ('1', '10298');
INSERT INTO `sys_role_menu` VALUES ('1', '10299');
INSERT INTO `sys_role_menu` VALUES ('1', '10300');
INSERT INTO `sys_role_menu` VALUES ('1', '10301');
INSERT INTO `sys_role_menu` VALUES ('1', '10302');
INSERT INTO `sys_role_menu` VALUES ('1', '10303');
INSERT INTO `sys_role_menu` VALUES ('1', '10304');
INSERT INTO `sys_role_menu` VALUES ('1', '10305');
INSERT INTO `sys_role_menu` VALUES ('1', '10306');
INSERT INTO `sys_role_menu` VALUES ('1', '10307');
INSERT INTO `sys_role_menu` VALUES ('1', '10308');
INSERT INTO `sys_role_menu` VALUES ('1', '10309');
INSERT INTO `sys_role_menu` VALUES ('1', '10316');
INSERT INTO `sys_role_menu` VALUES ('1', '10317');
INSERT INTO `sys_role_menu` VALUES ('1', '10318');
INSERT INTO `sys_role_menu` VALUES ('1', '10319');
INSERT INTO `sys_role_menu` VALUES ('1', '10320');
INSERT INTO `sys_role_menu` VALUES ('1', '10321');
INSERT INTO `sys_role_menu` VALUES ('1', '10322');
INSERT INTO `sys_role_menu` VALUES ('1', '10323');
INSERT INTO `sys_role_menu` VALUES ('1', '10325');
INSERT INTO `sys_role_menu` VALUES ('1', '10326');
INSERT INTO `sys_role_menu` VALUES ('1', '10327');
INSERT INTO `sys_role_menu` VALUES ('1', '10328');
INSERT INTO `sys_role_menu` VALUES ('1', '10329');
INSERT INTO `sys_role_menu` VALUES ('1', '10330');
INSERT INTO `sys_role_menu` VALUES ('1', '10331');
INSERT INTO `sys_role_menu` VALUES ('1', '10332');
INSERT INTO `sys_role_menu` VALUES ('1', '10333');
INSERT INTO `sys_role_menu` VALUES ('1', '10334');
INSERT INTO `sys_role_menu` VALUES ('1', '10339');
INSERT INTO `sys_role_menu` VALUES ('1', '10340');
INSERT INTO `sys_role_menu` VALUES ('1', '10341');
INSERT INTO `sys_role_menu` VALUES ('1', '10342');
INSERT INTO `sys_role_menu` VALUES ('1', '10343');
INSERT INTO `sys_role_menu` VALUES ('1', '10344');
INSERT INTO `sys_role_menu` VALUES ('1', '10345');
INSERT INTO `sys_role_menu` VALUES ('1', '10346');
INSERT INTO `sys_role_menu` VALUES ('1', '10352');
INSERT INTO `sys_role_menu` VALUES ('1', '10353');
INSERT INTO `sys_role_menu` VALUES ('1', '10354');
INSERT INTO `sys_role_menu` VALUES ('1', '10357');
INSERT INTO `sys_role_menu` VALUES ('1', '10358');
INSERT INTO `sys_role_menu` VALUES ('1', '10359');
INSERT INTO `sys_role_menu` VALUES ('1', '10360');
INSERT INTO `sys_role_menu` VALUES ('1', '10361');
INSERT INTO `sys_role_menu` VALUES ('1', '10362');
INSERT INTO `sys_role_menu` VALUES ('1', '10363');
INSERT INTO `sys_role_menu` VALUES ('1', '10364');
INSERT INTO `sys_role_menu` VALUES ('1', '10365');
INSERT INTO `sys_role_menu` VALUES ('1', '10366');
INSERT INTO `sys_role_menu` VALUES ('1', '10367');
INSERT INTO `sys_role_menu` VALUES ('1', '10368');
INSERT INTO `sys_role_menu` VALUES ('1', '10369');
INSERT INTO `sys_role_menu` VALUES ('1', '10371');
INSERT INTO `sys_role_menu` VALUES ('1', '10372');
INSERT INTO `sys_role_menu` VALUES ('1', '10373');
INSERT INTO `sys_role_menu` VALUES ('1', '10374');
INSERT INTO `sys_role_menu` VALUES ('1', '10375');
INSERT INTO `sys_role_menu` VALUES ('1', '10378');
INSERT INTO `sys_role_menu` VALUES ('1', '10379');
INSERT INTO `sys_role_menu` VALUES ('1', '10380');
INSERT INTO `sys_role_menu` VALUES ('1', '10381');
INSERT INTO `sys_role_menu` VALUES ('3', '1101');
INSERT INTO `sys_role_menu` VALUES ('3', '1102');
INSERT INTO `sys_role_menu` VALUES ('3', '1103');
INSERT INTO `sys_role_menu` VALUES ('3', '10189');
INSERT INTO `sys_role_menu` VALUES ('3', '10265');
INSERT INTO `sys_role_menu` VALUES ('3', '10266');
INSERT INTO `sys_role_menu` VALUES ('3', '10285');
INSERT INTO `sys_role_menu` VALUES ('3', '10286');
INSERT INTO `sys_role_menu` VALUES ('3', '10287');
INSERT INTO `sys_role_menu` VALUES ('3', '10288');
INSERT INTO `sys_role_menu` VALUES ('3', '10289');
INSERT INTO `sys_role_menu` VALUES ('3', '10290');
INSERT INTO `sys_role_menu` VALUES ('3', '10291');
INSERT INTO `sys_role_menu` VALUES ('3', '10292');
INSERT INTO `sys_role_menu` VALUES ('3', '10293');
INSERT INTO `sys_role_menu` VALUES ('3', '10294');
INSERT INTO `sys_role_menu` VALUES ('3', '10296');
INSERT INTO `sys_role_menu` VALUES ('3', '10297');
INSERT INTO `sys_role_menu` VALUES ('3', '10298');
INSERT INTO `sys_role_menu` VALUES ('3', '10299');
INSERT INTO `sys_role_menu` VALUES ('3', '10300');
INSERT INTO `sys_role_menu` VALUES ('3', '10301');
INSERT INTO `sys_role_menu` VALUES ('3', '10302');
INSERT INTO `sys_role_menu` VALUES ('3', '10303');
INSERT INTO `sys_role_menu` VALUES ('3', '10304');
INSERT INTO `sys_role_menu` VALUES ('3', '10305');
INSERT INTO `sys_role_menu` VALUES ('3', '10306');
INSERT INTO `sys_role_menu` VALUES ('3', '10307');
INSERT INTO `sys_role_menu` VALUES ('3', '10308');
INSERT INTO `sys_role_menu` VALUES ('3', '10309');
INSERT INTO `sys_role_menu` VALUES ('3', '10316');
INSERT INTO `sys_role_menu` VALUES ('3', '10317');
INSERT INTO `sys_role_menu` VALUES ('3', '10318');
INSERT INTO `sys_role_menu` VALUES ('3', '10319');
INSERT INTO `sys_role_menu` VALUES ('3', '10320');
INSERT INTO `sys_role_menu` VALUES ('3', '10321');
INSERT INTO `sys_role_menu` VALUES ('3', '10322');
INSERT INTO `sys_role_menu` VALUES ('3', '10323');
INSERT INTO `sys_role_menu` VALUES ('3', '10325');
INSERT INTO `sys_role_menu` VALUES ('3', '10326');
INSERT INTO `sys_role_menu` VALUES ('3', '10327');
INSERT INTO `sys_role_menu` VALUES ('3', '10328');
INSERT INTO `sys_role_menu` VALUES ('3', '10329');
INSERT INTO `sys_role_menu` VALUES ('3', '10330');
INSERT INTO `sys_role_menu` VALUES ('3', '10331');
INSERT INTO `sys_role_menu` VALUES ('3', '10332');
INSERT INTO `sys_role_menu` VALUES ('3', '10333');
INSERT INTO `sys_role_menu` VALUES ('3', '10334');
INSERT INTO `sys_role_menu` VALUES ('3', '10339');
INSERT INTO `sys_role_menu` VALUES ('3', '10340');
INSERT INTO `sys_role_menu` VALUES ('3', '10341');
INSERT INTO `sys_role_menu` VALUES ('3', '10342');
INSERT INTO `sys_role_menu` VALUES ('3', '10343');
INSERT INTO `sys_role_menu` VALUES ('3', '10344');
INSERT INTO `sys_role_menu` VALUES ('3', '10345');
INSERT INTO `sys_role_menu` VALUES ('3', '10346');
INSERT INTO `sys_role_menu` VALUES ('3', '10352');
INSERT INTO `sys_role_menu` VALUES ('3', '10353');
INSERT INTO `sys_role_menu` VALUES ('3', '10354');
INSERT INTO `sys_role_menu` VALUES ('3', '10357');
INSERT INTO `sys_role_menu` VALUES ('3', '10358');
INSERT INTO `sys_role_menu` VALUES ('3', '10359');
INSERT INTO `sys_role_menu` VALUES ('3', '10360');
INSERT INTO `sys_role_menu` VALUES ('3', '10361');
INSERT INTO `sys_role_menu` VALUES ('3', '10362');
INSERT INTO `sys_role_menu` VALUES ('3', '10363');
INSERT INTO `sys_role_menu` VALUES ('3', '10364');
INSERT INTO `sys_role_menu` VALUES ('3', '10365');
INSERT INTO `sys_role_menu` VALUES ('3', '10366');
INSERT INTO `sys_role_menu` VALUES ('3', '10367');
INSERT INTO `sys_role_menu` VALUES ('3', '10368');
INSERT INTO `sys_role_menu` VALUES ('3', '10369');
INSERT INTO `sys_role_menu` VALUES ('3', '10371');
INSERT INTO `sys_role_menu` VALUES ('3', '10372');
INSERT INTO `sys_role_menu` VALUES ('3', '10373');
INSERT INTO `sys_role_menu` VALUES ('3', '10374');
INSERT INTO `sys_role_menu` VALUES ('3', '10375');
INSERT INTO `sys_role_menu` VALUES ('3', '10378');
INSERT INTO `sys_role_menu` VALUES ('3', '10379');
INSERT INTO `sys_role_menu` VALUES ('3', '10380');
INSERT INTO `sys_role_menu` VALUES ('3', '10381');
INSERT INTO `sys_role_menu` VALUES ('4', '10265');
INSERT INTO `sys_role_menu` VALUES ('4', '10285');
INSERT INTO `sys_role_menu` VALUES ('4', '10286');
INSERT INTO `sys_role_menu` VALUES ('4', '10287');
INSERT INTO `sys_role_menu` VALUES ('4', '10288');
INSERT INTO `sys_role_menu` VALUES ('4', '10289');
INSERT INTO `sys_role_menu` VALUES ('4', '10290');
INSERT INTO `sys_role_menu` VALUES ('4', '10291');
INSERT INTO `sys_role_menu` VALUES ('4', '10292');
INSERT INTO `sys_role_menu` VALUES ('4', '10293');
INSERT INTO `sys_role_menu` VALUES ('4', '10294');
INSERT INTO `sys_role_menu` VALUES ('4', '10296');
INSERT INTO `sys_role_menu` VALUES ('4', '10297');
INSERT INTO `sys_role_menu` VALUES ('4', '10298');
INSERT INTO `sys_role_menu` VALUES ('4', '10299');
INSERT INTO `sys_role_menu` VALUES ('4', '10300');
INSERT INTO `sys_role_menu` VALUES ('4', '10301');
INSERT INTO `sys_role_menu` VALUES ('4', '10302');
INSERT INTO `sys_role_menu` VALUES ('4', '10303');
INSERT INTO `sys_role_menu` VALUES ('4', '10304');
INSERT INTO `sys_role_menu` VALUES ('4', '10305');
INSERT INTO `sys_role_menu` VALUES ('4', '10306');
INSERT INTO `sys_role_menu` VALUES ('4', '10307');
INSERT INTO `sys_role_menu` VALUES ('4', '10308');
INSERT INTO `sys_role_menu` VALUES ('4', '10309');
INSERT INTO `sys_role_menu` VALUES ('4', '10316');
INSERT INTO `sys_role_menu` VALUES ('4', '10317');
INSERT INTO `sys_role_menu` VALUES ('4', '10318');
INSERT INTO `sys_role_menu` VALUES ('4', '10319');
INSERT INTO `sys_role_menu` VALUES ('4', '10320');
INSERT INTO `sys_role_menu` VALUES ('4', '10321');
INSERT INTO `sys_role_menu` VALUES ('4', '10322');
INSERT INTO `sys_role_menu` VALUES ('4', '10323');
INSERT INTO `sys_role_menu` VALUES ('4', '10325');
INSERT INTO `sys_role_menu` VALUES ('4', '10326');
INSERT INTO `sys_role_menu` VALUES ('4', '10327');
INSERT INTO `sys_role_menu` VALUES ('4', '10328');
INSERT INTO `sys_role_menu` VALUES ('4', '10329');
INSERT INTO `sys_role_menu` VALUES ('4', '10330');
INSERT INTO `sys_role_menu` VALUES ('4', '10331');
INSERT INTO `sys_role_menu` VALUES ('4', '10339');
INSERT INTO `sys_role_menu` VALUES ('4', '10343');
INSERT INTO `sys_role_menu` VALUES ('4', '10344');
INSERT INTO `sys_role_menu` VALUES ('4', '10345');
INSERT INTO `sys_role_menu` VALUES ('4', '10346');
INSERT INTO `sys_role_menu` VALUES ('4', '10352');
INSERT INTO `sys_role_menu` VALUES ('4', '10353');
INSERT INTO `sys_role_menu` VALUES ('4', '10354');
INSERT INTO `sys_role_menu` VALUES ('4', '10357');
INSERT INTO `sys_role_menu` VALUES ('4', '10358');
INSERT INTO `sys_role_menu` VALUES ('4', '10359');
INSERT INTO `sys_role_menu` VALUES ('4', '10360');
INSERT INTO `sys_role_menu` VALUES ('4', '10361');
INSERT INTO `sys_role_menu` VALUES ('4', '10365');
INSERT INTO `sys_role_menu` VALUES ('4', '10366');
INSERT INTO `sys_role_menu` VALUES ('4', '10367');
INSERT INTO `sys_role_menu` VALUES ('4', '10368');
INSERT INTO `sys_role_menu` VALUES ('4', '10369');
INSERT INTO `sys_role_menu` VALUES ('4', '10371');
INSERT INTO `sys_role_menu` VALUES ('4', '10372');
INSERT INTO `sys_role_menu` VALUES ('4', '10373');
INSERT INTO `sys_role_menu` VALUES ('4', '10374');
INSERT INTO `sys_role_menu` VALUES ('4', '10375');
INSERT INTO `sys_role_menu` VALUES ('4', '10378');
INSERT INTO `sys_role_menu` VALUES ('4', '10379');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `username` varchar(30) DEFAULT '' COMMENT '登录名',
  `password` varchar(255) NOT NULL DEFAULT '' COMMENT '密码',
  `name` varchar(20) DEFAULT '' COMMENT '姓名',
  `email` varchar(30) DEFAULT '' COMMENT '邮箱',
  `phone` varchar(255) DEFAULT '' COMMENT '手机',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `wx_openid` varchar(255) DEFAULT NULL COMMENT '微信openid',
  `mini_openid` varchar(255) DEFAULT NULL,
  `login_ip` varchar(20) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_flag` tinyint(4) DEFAULT '1' COMMENT '是否可登录',
  `create_by` int(11) DEFAULT '0' COMMENT '创建人',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` int(11) DEFAULT '0' COMMENT '修改人',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除(0=false,1=true)',
  PRIMARY KEY (`id`),
  KEY `login_name` (`username`) USING BTREE,
  KEY `password` (`password`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('2', '1', 'admin', '$2a$10$NHY97jnpIjsiNDV68Jw0C.2/pStmATWFFdl/cZOC7R58khc0m6emW', '超级管理员', '123456@qq.com', '15922113549', '/images/defaultAvatar.png', '', '', '127.0.0.1', '2024-09-11 23:21:56', '0', '1', '2018-06-29 11:47:36', '2', '2018-07-30 16:21:52', '0');
INSERT INTO `sys_user` VALUES ('5', '3', 'kube', '$2a$10$Um36Rgcz9epBa2tK/vEwMO/umppf80VGMmxyhXD.hvklWvY.hYE9q', '容器云管理员', '123456@qq.com', '15111111111', null, null, null, '127.0.0.1', '2024-09-01 18:29:44', '0', '0', '2024-09-01 15:01:55', '0', '2024-09-01 15:01:55', '0');
INSERT INTO `sys_user` VALUES ('6', '4', 'devops', '$2a$10$rqcnRpx4uyWpDmD2OIUA4.oK4NCiE/RxV.Vjj6.fFAF/KgLnw52oa', '运维', '12121@qq.com', '15888888888', null, null, null, '127.0.0.1', '2024-09-01 15:37:40', '0', '0', '2024-09-01 15:30:45', '0', '2024-09-01 15:30:45', '0');
