# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.19-log)
# Database: zhushou_init
# Generation Time: 2018-04-22 09:59:00 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table app
# ------------------------------------------------------------

CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `projectId` bigint(20) DEFAULT NULL COMMENT '项目id',
  `name` varchar(30) DEFAULT NULL COMMENT '名称',
  `userId` bigint(20) DEFAULT NULL COMMENT '负责人Id',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `log` varchar(500) DEFAULT NULL COMMENT '日志',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `codeName` varchar(50) DEFAULT NULL COMMENT 'git代码名称',
  `domain` varchar(255) DEFAULT NULL COMMENT '域名',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `front` tinyint(4) DEFAULT NULL COMMENT '是否前端发布（1 是，0 否）',
  `compileProperty` varchar(1000) DEFAULT NULL COMMENT '编译变量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用';



# Dump of table appdownloadrecords
# ------------------------------------------------------------

CREATE TABLE `appdownloadrecords` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `appVersionId` bigint(20) NOT NULL COMMENT '应用版本id',
  `deviceId` varchar(20) DEFAULT '' COMMENT '设备唯一标识',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户id',
  `ip` varchar(20) NOT NULL COMMENT 'IP',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用下载记录';



# Dump of table appfront
# ------------------------------------------------------------

CREATE TABLE `appfront` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `name` varchar(30) DEFAULT NULL COMMENT '名称',
  `userId` bigint(20) DEFAULT NULL COMMENT '负责人Id',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `log` varchar(500) DEFAULT NULL COMMENT '日志',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `codeName` varchar(50) DEFAULT NULL COMMENT 'git代码名称',
  `domain` varchar(255) DEFAULT NULL COMMENT '域名',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `testBranch` varchar(50) DEFAULT NULL COMMENT '测试分支名称',
  `prodBranch` varchar(255) DEFAULT NULL COMMENT '线上分支名称',
  `testAndroidVersion` varchar(50) DEFAULT NULL COMMENT '测试Android版本',
  `testIOSVersion` varchar(50) DEFAULT NULL COMMENT '测试IOS版本',
  `prodAndroidVersion` varchar(50) DEFAULT NULL COMMENT '线上Android版本',
  `prodIOSVersion` varchar(50) DEFAULT NULL COMMENT '线上IOS版本',
  `projectId` bigint(20) DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用';



# Dump of table apphost
# ------------------------------------------------------------

CREATE TABLE `apphost` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `appId` bigint(20) DEFAULT NULL COMMENT '应用ID',
  `hostId` bigint(20) DEFAULT NULL COMMENT '主机ID',
  `model` varchar(10) DEFAULT NULL COMMENT '模式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用主机';



# Dump of table appversion
# ------------------------------------------------------------

CREATE TABLE `appversion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `companyId` bigint(20) NOT NULL COMMENT '公司id',
  `appName` varchar(50) NOT NULL COMMENT '应用名称',
  `model` varchar(10) NOT NULL COMMENT '模式（test or prod）',
  `type` varchar(10) NOT NULL COMMENT '类型（android or ios）',
  `version` varchar(10) NOT NULL COMMENT '版本号（x.y.z）',
  `appUrl` varchar(200) DEFAULT NULL COMMENT 'app下载链接地址',
  `forceUpdate` tinyint(4) NOT NULL COMMENT '是否强制更新',
  `updateLog` varchar(1000) DEFAULT NULL COMMENT '更新日志',
  `status` int(4) NOT NULL COMMENT '状态（0 就绪，1 启动，2 下线）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_unique_version` (`companyId`,`appName`,`model`,`type`,`version`,`delState`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用版本';



# Dump of table atactive
# ------------------------------------------------------------

CREATE TABLE `atactive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `templateId` bigint(20) DEFAULT NULL COMMENT '模板ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `url` varchar(500) DEFAULT NULL COMMENT 'URL',
  `method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `reqContentType` varchar(100) DEFAULT NULL COMMENT '类型',
  `headerRow` mediumblob COMMENT '请求头',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除（1：没有；0；已删除）',
  `logStackId` varchar(100) DEFAULT NULL COMMENT '日志栈id',
  `orderIndex` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动模板';



# Dump of table atactiveinst
# ------------------------------------------------------------

CREATE TABLE `atactiveinst` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `processInstId` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `status` int(11) DEFAULT NULL COMMENT '状态（0 失败，1：成功）',
  `httpStatus` int(11) DEFAULT NULL COMMENT 'http返回状态',
  `errMsg` mediumtext COMMENT '错误消息',
  `result` mediumtext COMMENT '返回结果',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除（1：没有；0；已删除）',
  `param` mediumtext COMMENT '参数',
  `reqHeader` varchar(2000) DEFAULT '' COMMENT '请求header',
  `resHeader` varchar(2000) DEFAULT '' COMMENT '返回header',
  `activeId` bigint(20) DEFAULT NULL COMMENT '活动ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动实例';



# Dump of table atparameter
# ------------------------------------------------------------

CREATE TABLE `atparameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activeId` bigint(20) DEFAULT NULL COMMENT '活动ID',
  `code` varchar(100) DEFAULT NULL COMMENT '编码',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `dataType` varchar(20) DEFAULT NULL COMMENT '数据类型',
  `dataValue` mediumtext COMMENT '数据值',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除（1：没有；0；已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板参数';



# Dump of table atprocessinst
# ------------------------------------------------------------

CREATE TABLE `atprocessinst` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `templateId` bigint(20) DEFAULT NULL COMMENT '模板ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `status` int(11) DEFAULT NULL COMMENT '状态（0 失败，1：成功）',
  `errMsg` mediumtext COMMENT '错误消息',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除（1：没有；0；已删除）',
  `userId` bigint(20) DEFAULT NULL COMMENT '运行用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程实例';



# Dump of table attachment
# ------------------------------------------------------------

CREATE TABLE `attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `name` varchar(500) NOT NULL DEFAULT '' COMMENT '名称',
  `content` mediumblob NOT NULL COMMENT '内容',
  `downloadUrl` varchar(500) DEFAULT NULL COMMENT '下载链接',
  `companyId` bigint(20) NOT NULL COMMENT '公司id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件';



# Dump of table attemplate
# ------------------------------------------------------------

CREATE TABLE `attemplate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `userName` varchar(20) DEFAULT NULL COMMENT '用户名称',
  `model` varchar(10) DEFAULT NULL COMMENT '模式（test,prod）',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除（1:没有；0：已删除）',
  `companyId` bigint(20) NOT NULL COMMENT '企业id',
  `projectId` bigint(20) NOT NULL COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试模板';



# Dump of table atvariable
# ------------------------------------------------------------

CREATE TABLE `atvariable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `key` varchar(100) NOT NULL COMMENT 'key',
  `value` varchar(1000) NOT NULL COMMENT '值',
  `status` int(4) NOT NULL COMMENT '状态（0 未启用，1 启用）',
  `scope` int(4) NOT NULL COMMENT '范围（0 局域，1 全局）',
  `templateId` bigint(20) DEFAULT NULL COMMENT '模板id',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `companyId` bigint(20) NOT NULL COMMENT '企业id',
  `projectId` bigint(20) NOT NULL COMMENT '项目id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_key_templateId` (`key`,`templateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试变量';



# Dump of table authres
# ------------------------------------------------------------

CREATE TABLE `authres` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(20) DEFAULT NULL COMMENT '编码',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `url` varchar(100) DEFAULT NULL COMMENT 'URL',
  `tplUrl` varchar(100) DEFAULT NULL COMMENT '模板URL',
  `menu` tinyint(4) DEFAULT NULL COMMENT '是否菜单（0：否，1：是）',
  `display` tinyint(4) DEFAULT NULL COMMENT '是否仅展示（0：否，1：是）',
  `actionId` varchar(20) DEFAULT NULL COMMENT '动作ID',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `level` int(4) DEFAULT NULL COMMENT '级别',
  `status` int(4) DEFAULT NULL COMMENT '状态',
  `parentId` bigint(20) DEFAULT NULL COMMENT '父ID',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  `orderBy` int(4) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限资源';

LOCK TABLES `authres` WRITE;
/*!40000 ALTER TABLE `authres` DISABLE KEYS */;

INSERT INTO `authres` (`id`, `code`, `name`, `url`, `tplUrl`, `menu`, `display`, `actionId`, `icon`, `level`, `status`, `parentId`, `gmtCreate`, `gmtModify`, `delStatus`, `orderBy`)
VALUES
	(1,'appList','应用发布','/app/list','/appList.html',1,0,'enterAppList','icon-legal',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,1),
	(2,'deployLog','发布日志','/deployLog/list','/deployLog.html',1,0,'enterDeployLog','icon-comments',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,2),
	(3,'appLog','应用日志','/appLog/list','/log/appLog.html',1,0,'enterAppLog','icon-comments-alt',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,3),
	(4,'taskLog','Task日志','/taskLog/list','/taskLog/list.html',1,0,'enterTaskLog','icon-comments-alt',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,4),
	(5,'idoc','接口文档','/idocUrl/list','/idoc/list.html',1,0,'enterIdoc','icon-file-text',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,6),
	(6,'settings','系统设置','/settings/info','/settings/info.html',1,0,'enterSettings','icon-cog',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,7),
	(7,'autoTest','自动化测试','/atTemplate/list','/at/template.html',1,0,'enterAutoTest','icon-eye-open',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,5),
	(8,'newTemplate','创建接口','','/idoc/bind.html',0,0,'btnNewTemplate','icon-building',2,1,5,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,1),
	(9,'del','删除','/idocUrl/deleteById','',0,0,'btnDel','icon-eye-open',2,1,5,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,2),
	(10,'update','修改','/idocUrl/enterEdit','/idocUrl/edit.html',0,0,'btnEnterEdit','icon-eye-open',2,1,5,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,2),
	(11,'listAll','查看所有','/idocUrl/list','/idoc/listAll.html',0,0,'btnListAll','icon-eye-open',2,1,5,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,2),
	(12,'reqContent','http工具','/reqContent/list','/req/list.html',1,0,'enterReqContent','icon-search',1,1,0,'2016-10-21 12:02:57','2016-10-21 12:03:01',1,7),
	(13,'proxy','代理','/proxy/list','/proxy/list.html',1,0,'enterProxy','icon-key',1,1,0,'2016-10-21 12:02:57','2016-10-21 12:03:01',1,8),
	(14,'cache','缓存管理','/cache/list','/cache/list.html',1,0,'enterCache','icon-cloud',1,1,0,'2016-10-21 12:02:57','2016-10-21 12:03:01',1,9),
	(15,'appFrontList','前端发布','/appFront/list','/appFrontList.html',1,0,'enterAppFrontList','icon-legal',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,1),
	(16,'codeBuild','代码构建','/codeBuild/list','/codeBuild/list.html',1,0,'enterCodeBuild','icon-credit-card',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,6),
	(17,'project','项目管理','/project/list','/project/list.html',1,0,'enterProject','icon-globe',1,1,0,'2016-08-07 13:24:43','2016-08-07 13:24:46',1,6);

/*!40000 ALTER TABLE `authres` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table buildRecord
# ------------------------------------------------------------

CREATE TABLE `buildRecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `projectId` bigint(20) NOT NULL COMMENT '项目id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `downloadUrl` varchar(500) DEFAULT NULL COMMENT '下载链接',
  `companyId` bigint(20) NOT NULL COMMENT '公司id',
  `attachmentId` bigint(20) NOT NULL COMMENT '模版id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='构建记录';



# Dump of table codebuildsql
# ------------------------------------------------------------

CREATE TABLE `codebuildsql` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `companyId` bigint(20) NOT NULL COMMENT '企业id',
  `content` varchar(1000) NOT NULL COMMENT '内容',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `projectId` bigint(20) NOT NULL COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成sql';



# Dump of table company
# ------------------------------------------------------------

CREATE TABLE `company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `key` varchar(32) DEFAULT NULL COMMENT '标志',
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '企业名称',
  `ip` varchar(32) DEFAULT NULL COMMENT 'ip',
  `port` int(4) DEFAULT NULL COMMENT '端口',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `desc` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业信息';

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;

INSERT INTO `company` (`id`, `key`, `name`, `ip`, `port`, `gmtCreate`, `gmtModify`, `delStatus`, `type`, `status`, `desc`)
VALUES
	(3,'E42D5D379E703ABC76BE596CA75EC766','杭州XXX有限公司','localhost',28080,'2016-12-28 10:47:36','2016-12-28 10:47:41',1,0,1,'xxxx');

/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table config
# ------------------------------------------------------------

CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` int(4) DEFAULT NULL COMMENT '类型(0 私有;1 公共)',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `value` varchar(500) DEFAULT NULL COMMENT '值',
  `delStatus` int(1) DEFAULT NULL COMMENT '是否删除（1：未删除；0：已删除）',
  `projectId` bigint(20) DEFAULT NULL COMMENT '项目id',
  `orderBy` int(4) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置项';

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;

INSERT INTO `config` (`id`, `type`, `companyId`, `name`, `value`, `delStatus`, `projectId`, `orderBy`)
VALUES
	(3,1,NULL,'ver','201608051526',1,NULL,NULL),
	(4,1,NULL,'logoName','企业软件开发助手平台',1,NULL,NULL),
	(5,1,NULL,'platformName','企业软件开发助手管理公司',1,NULL,NULL),
	(8,0,3,'autoDeployAppName','member,trainer,ws,bos',1,NULL,NULL),
	(9,0,3,'autoDeployParam','{\"fix605\":\"member,trainer,ws,bos,task\",test519:\"member,trainer,ws,bos,task\"}',1,NULL,NULL),
	(10,0,4,'autoDeployParam','{\"dev611\":\"member,trainer,task,bos,orgAdmin,h5\",test519:\"member,trainer,ws,bos,task\"}',1,NULL,NULL),
	(18,2,NULL,'jdbc.driverClassName','com.mysql.jdbc.Driver',1,1,NULL),
	(19,2,NULL,'jdbc.url','jdbc:mysql://localhost:3306/test1',1,1,NULL),
	(20,2,NULL,'jdbc.username','root',1,1,NULL),
	(21,2,NULL,'jdbc.password','123456',1,1,NULL);

/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table deploylog
# ------------------------------------------------------------

CREATE TABLE `deploylog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `content` varchar(1000) DEFAULT NULL COMMENT '内容',
  `userId` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `userName` varchar(30) DEFAULT NULL COMMENT '操作人',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  `log` mediumblob COMMENT '详细日志',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `appName` varchar(20) DEFAULT NULL COMMENT '应用名称',
  `model` varchar(10) DEFAULT NULL COMMENT '模式',
  `backVer` varchar(100) DEFAULT NULL COMMENT '备份版本',
  `type` int(4) DEFAULT NULL COMMENT '发布应用类型（0 后台，1 android , 2 ios）',
  `projectId` bigint(20) DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发布日志';



# Dump of table entity
# ------------------------------------------------------------

CREATE TABLE `entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `appId` bigint(20) DEFAULT NULL COMMENT '应用id',
  `enName` varchar(50) NOT NULL COMMENT '英文名称',
  `cnName` varchar(50) NOT NULL COMMENT '中文名称',
  `projectId` bigint(20) NOT NULL COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体';



# Dump of table host
# ------------------------------------------------------------

CREATE TABLE `host` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `eth0` varchar(20) DEFAULT NULL COMMENT '内网',
  `eth1` varchar(20) DEFAULT NULL COMMENT '外网',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `username` varchar(50) DEFAULT NULL COMMENT '账号',
  `pwd` varchar(100) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='主机';



# Dump of table idocparam
# ------------------------------------------------------------

CREATE TABLE `idocparam` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(100) DEFAULT NULL COMMENT '编码',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `parentId` bigint(20) DEFAULT NULL COMMENT '主体ID',
  `rule` varchar(500) DEFAULT NULL COMMENT '规则',
  `memo` varchar(500) DEFAULT NULL COMMENT '说明',
  `status` int(4) DEFAULT NULL COMMENT '状态',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除（1：没有；0；已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口请求参数';



# Dump of table idocurl
# ------------------------------------------------------------

CREATE TABLE `idocurl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `url` varchar(100) DEFAULT NULL COMMENT 'URL',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `appName` varchar(20) DEFAULT NULL COMMENT '应用',
  `module` varchar(20) DEFAULT NULL COMMENT '模块',
  `version` varchar(20) DEFAULT NULL COMMENT '版本',
  `resultData` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '返回数据',
  `resultMockData` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '返回mock数据',
  `status` int(4) DEFAULT NULL COMMENT '状态',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除（1：没有；0；已删除）',
  `memo` varchar(500) DEFAULT NULL COMMENT '说明',
  `createUserId` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `createUserName` varchar(30) DEFAULT NULL COMMENT '创建用户名称',
  `type` int(11) DEFAULT NULL COMMENT '类别 (0:接口；1：枚举 ) ',
  `showResId` bigint(20) DEFAULT NULL COMMENT '展示资源id',
  `mockDataId` bigint(20) DEFAULT NULL COMMENT 'mock数据id',
  `mockStatus` int(4) DEFAULT NULL COMMENT 'mock状态(0 未开启，1 已开启)',
  `projectId` bigint(20) DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_urlconfig_module_url` (`url`,`appName`,`version`,`companyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口主体';



# Dump of table project
# ------------------------------------------------------------

CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `companyId` bigint(20) NOT NULL COMMENT '企业id',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `enName` varchar(200) NOT NULL COMMENT '英文名称',
  `desc` varchar(1000) NOT NULL COMMENT '描述',
  `codeName` varchar(200) DEFAULT NULL COMMENT 'git codeName',
  `packageName` varchar(200) DEFAULT NULL COMMENT '包名',
  `templateId` bigint(20) DEFAULT NULL COMMENT '模版id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目';



# Dump of table property
# ------------------------------------------------------------

CREATE TABLE `property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `entityId` bigint(20) NOT NULL COMMENT '实体id',
  `enName` varchar(50) NOT NULL COMMENT '英文名称',
  `cnName` varchar(50) NOT NULL COMMENT '中文名称',
  `dataType` varchar(50) DEFAULT NULL COMMENT '数据类型',
  `length` varchar(10) DEFAULT NULL COMMENT '长度',
  `primaryKey` tinyint(10) DEFAULT NULL COMMENT '是否主键（0：否；1：是）',
  `defaultValue` varchar(50) DEFAULT NULL COMMENT '默认值',
  `isNull` tinyint(50) DEFAULT NULL COMMENT '是否为空（0：否；1：是）',
  `orderBy` int(4) DEFAULT NULL COMMENT '排序',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性';



# Dump of table proxycontent
# ------------------------------------------------------------

CREATE TABLE `proxycontent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `clientIp` varchar(50) DEFAULT NULL COMMENT '客户端ip',
  `port` int(4) DEFAULT NULL COMMENT '客户端端口',
  `domain` varchar(100) DEFAULT NULL COMMENT '域名',
  `pathUrl` varchar(100) DEFAULT NULL COMMENT '路劲url',
  `url` varchar(2000) NOT NULL COMMENT 'URL',
  `reqHeader` varchar(2000) NOT NULL COMMENT '请求头',
  `reqData` mediumblob COMMENT '请求数据',
  `resHeader` varchar(2000) DEFAULT NULL COMMENT '返回头',
  `resData` mediumblob COMMENT '返回数据',
  `resContentType` varchar(50) DEFAULT NULL COMMENT '返回数据格式',
  `resLength` int(4) DEFAULT NULL COMMENT '返回数据长度',
  `reqMethod` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `httpStatus` int(4) DEFAULT NULL COMMENT '返回状态',
  `status` int(4) DEFAULT NULL COMMENT '状态',
  `gmtRequest` datetime NOT NULL COMMENT '请求时间',
  `gmtResponse` datetime NOT NULL COMMENT '返回时间',
  `delStatus` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `projectId` bigint(20) DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理内容';



# Dump of table proxyreqfilter
# ------------------------------------------------------------

CREATE TABLE `proxyreqfilter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `port` int(4) NOT NULL COMMENT '端口',
  `status` int(4) NOT NULL COMMENT '状态（0 未启用，1 已启用）',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `joinType` int(4) NOT NULL COMMENT '连接类型（0 and，1 or）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理请求过滤';



# Dump of table proxyreqfilteritem
# ------------------------------------------------------------

CREATE TABLE `proxyreqfilteritem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `parentId` bigint(20) NOT NULL COMMENT '父id',
  `status` int(4) NOT NULL COMMENT '状态（0 未启用，1 已启用）',
  `type` int(4) NOT NULL COMMENT '类型（0 url,1 header,2 body）',
  `matchType` varchar(100) DEFAULT NULL COMMENT '匹配类型（eq,notEq,contains,notContains,regex）',
  `key` varchar(100) DEFAULT NULL COMMENT '键',
  `value` varchar(1000) DEFAULT NULL COMMENT '值',
  `filterType` int(4) NOT NULL COMMENT '过滤类型（0 req,1 rewrite）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理请求过滤子集';



# Dump of table proxyresrewrite
# ------------------------------------------------------------

CREATE TABLE `proxyresrewrite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `port` int(4) NOT NULL COMMENT '端口',
  `status` int(4) NOT NULL COMMENT '状态（0 未启用，1 已启用）',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `resType` int(4) DEFAULT NULL COMMENT '返回类型 （0 header,1 body）',
  `resModel` int(4) DEFAULT NULL COMMENT '返回模型（ 0 静态，1 动态)',
  `data` mediumtext COMMENT '返回内容/跳转内容',
  `joinType` int(4) DEFAULT NULL COMMENT '连接类型（0 and，1 or）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理返回重写';



# Dump of table reqcontent
# ------------------------------------------------------------

CREATE TABLE `reqcontent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `url` varchar(1000) NOT NULL COMMENT 'URL',
  `reqHeader` varchar(4000) NOT NULL COMMENT '请求头',
  `reqData` mediumtext COMMENT '请求数据',
  `resHeader` varchar(4000) DEFAULT NULL COMMENT '返回头',
  `resData` mediumtext COMMENT '返回数据',
  `reqMethod` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `httpStatus` int(4) DEFAULT NULL COMMENT '返回状态',
  `userId` bigint(20) DEFAULT NULL COMMENT '操作人用户ID',
  `userName` varchar(20) DEFAULT NULL COMMENT '操作人用户姓名',
  `model` varchar(10) DEFAULT NULL COMMENT '模式（test,prod）',
  `appName` varchar(10) DEFAULT NULL COMMENT '应用',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `status` int(4) DEFAULT NULL COMMENT '状态',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delStatus` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请求内容';



# Dump of table role
# ------------------------------------------------------------

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `name`, `gmtCreate`, `gmtModify`, `delStatus`)
VALUES
	(1,'管理员','2016-08-07 13:18:07','2016-08-07 13:18:12',1),
	(2,'前端','2016-08-07 13:18:58','2016-08-07 13:19:00',1),
	(3,'测试','2016-08-07 13:18:58','2016-08-07 13:19:00',1),
	(4,'运维','2016-08-07 13:18:58','2016-08-07 13:19:00',1),
	(5,'运营','2016-08-07 13:18:58','2016-08-07 13:19:00',1),
	(6,'后台','2016-08-07 13:18:58','2016-08-07 13:19:00',1);

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roleauthres
# ------------------------------------------------------------

CREATE TABLE `roleauthres` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `roleId` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `authResId` bigint(20) DEFAULT NULL COMMENT '权限资源ID',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限资源';

LOCK TABLES `roleauthres` WRITE;
/*!40000 ALTER TABLE `roleauthres` DISABLE KEYS */;

INSERT INTO `roleauthres` (`id`, `roleId`, `authResId`, `gmtCreate`, `gmtModify`, `delStatus`)
VALUES
	(1,1,1,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(2,1,2,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(3,1,3,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(4,1,4,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(5,1,5,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(6,1,6,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(7,1,7,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(8,2,3,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(9,2,5,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(10,2,6,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(11,1,8,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(12,1,9,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(13,1,10,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(14,1,11,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(15,1,12,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(16,1,13,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(17,1,14,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(18,2,4,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(19,1,15,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(20,2,15,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(21,6,1,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(22,6,2,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(23,6,3,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(24,6,4,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(25,6,5,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(26,6,6,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(27,6,8,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(28,6,9,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(29,6,10,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(30,1,16,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(31,6,16,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(32,3,3,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(33,3,4,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(34,3,5,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(35,3,6,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(36,3,7,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(37,2,12,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(38,3,12,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(39,6,12,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(40,1,17,'2016-08-07 13:38:30','2016-08-07 13:38:34',1),
	(41,6,17,'2016-08-07 13:38:30','2016-08-07 13:38:34',1);

/*!40000 ALTER TABLE `roleauthres` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table showres
# ------------------------------------------------------------

CREATE TABLE `showres` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `type` int(4) DEFAULT NULL COMMENT '类型',
  `appName` varchar(20) DEFAULT NULL COMMENT '应用名称',
  `module` varchar(100) DEFAULT NULL COMMENT '模块',
  `imgUrl` varchar(200) NOT NULL COMMENT '图片路径',
  `h5Url` varchar(200) NOT NULL COMMENT 'h5路径',
  `version` varchar(32) DEFAULT NULL COMMENT '版本',
  `index` tinyint(1) DEFAULT NULL COMMENT '是否首页',
  `status` int(4) DEFAULT NULL COMMENT '状态',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delStatus` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='展示资源';



# Dump of table template
# ------------------------------------------------------------

CREATE TABLE `template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `projectId` bigint(20) DEFAULT NULL COMMENT '项目id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `desc` varchar(50) DEFAULT NULL COMMENT '描述',
  `companyId` bigint(20) NOT NULL COMMENT '公司id',
  `type` int(4) DEFAULT NULL COMMENT '类型（0 全部，1 增量）',
  `attachmentId` bigint(20) NOT NULL COMMENT '模版id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='构建模版';



# Dump of table user
# ------------------------------------------------------------

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `companyId` bigint(20) DEFAULT NULL COMMENT '企业id',
  `accountNo` varchar(50) DEFAULT NULL COMMENT '账号',
  `pwd` varchar(32) DEFAULT NULL COMMENT '密码',
  `nickName` varchar(30) DEFAULT NULL COMMENT '昵称',
  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',
  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',
  `delStatus` int(11) DEFAULT NULL COMMENT '是否删除',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `mobile` varchar(30) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(10) DEFAULT NULL COMMENT 'QQ',
  `role` varchar(200) DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `companyId`, `accountNo`, `pwd`, `nickName`, `gmtCreate`, `gmtModify`, `delStatus`, `level`, `type`, `status`, `mobile`, `email`, `qq`, `role`)
VALUES
	(1,3,'admin','E10ADC3949BA59ABBE56E057F20F883E','管理员','2016-05-11 16:04:32','2016-05-11 16:04:35',1,1,1,1,'','admin@xxx.com','','1'),
	(2,3,'dev','E10ADC3949BA59ABBE56E057F20F883E','开发','2016-05-11 16:04:32','2016-05-11 16:04:35',1,1,1,1,'','dev@xxx.com','','6'),
	(3,3,'front','E10ADC3949BA59ABBE56E057F20F883E','前端','2016-05-11 16:04:32','2016-05-11 16:04:35',1,1,1,1,'','front@xxx.com','','2'),
	(4,3,'test','E10ADC3949BA59ABBE56E057F20F883E','测试','2016-05-11 16:04:32','2016-05-11 16:04:35',1,1,1,1,'','test@xxx.com','','3');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table warnevent
# ------------------------------------------------------------

CREATE TABLE `warnevent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `companyId` bigint(20) NOT NULL COMMENT '公司id',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `content` varchar(1000) NOT NULL COMMENT '内容',
  `execTime` datetime NOT NULL COMMENT '计划执行时间',
  `sms` int(4) NOT NULL COMMENT '发送短信（0 否，1 是）',
  `email` int(4) NOT NULL COMMENT '发送邮件（0 否，1 是）',
  `weixin` int(4) NOT NULL COMMENT '发送微信（0 否，1 是）',
  `status` int(4) DEFAULT NULL COMMENT '操作状态 (0 未操作，1 已执行)',
  `userIds` varchar(200) DEFAULT NULL COMMENT '发送的用户ids(多个用逗号隔开)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警事件';



# Dump of table warneventresult
# ------------------------------------------------------------

CREATE TABLE `warneventresult` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmtCreate` datetime NOT NULL COMMENT '创建时间',
  `gmtModify` datetime NOT NULL COMMENT '修改时间',
  `delState` tinyint(4) NOT NULL COMMENT '删除状态（0：已删除；1：未删除）',
  `warnEventId` bigint(20) NOT NULL COMMENT '告警事件id',
  `type` int(4) NOT NULL COMMENT '类型（0 sms,1 email,2 weixin）',
  `status` int(4) NOT NULL COMMENT '状态（0 失败，1 成功）',
  `userId` bigint(20) NOT NULL COMMENT '用户id',
  `tryCount` int(4) NOT NULL COMMENT '尝试重试发送次数',
  `content` varchar(1000) DEFAULT '' COMMENT '发送内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警事件发送结果';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
