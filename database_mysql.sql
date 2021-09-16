/*
 Navicat Premium Data Transfer

 Source Server         : 本地_mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : vertx-demo

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 16/09/2021 22:43:33
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for vx_sms_log
-- ----------------------------
DROP TABLE IF EXISTS `vx_sms_log`;
CREATE TABLE `vx_sms_log`
(
  `id`           bigint                                                  NOT NULL AUTO_INCREMENT,
  `content`      varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_time` datetime(6) NOT NULL,
  `description`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
  `mobile`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
  `status`       int                                                     NOT NULL,
  `task_id`      bigint                                                  NOT NULL,
  `template_id`  bigint                                                  NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY            `mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='短信模板';

-- ----------------------------
-- Table structure for vx_sms_task
-- ----------------------------
DROP TABLE IF EXISTS `vx_sms_task`;
CREATE TABLE `vx_sms_task`
(
  `id`           bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='短信模板';

-- ----------------------------
-- Table structure for vx_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `vx_sms_template`;
CREATE TABLE `vx_sms_template`
(
  `id`          bigint                                                 NOT NULL AUTO_INCREMENT,
  `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `params`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='短信模板';

-- ----------------------------
-- Table structure for vx_sms_template_provider
-- ----------------------------
DROP TABLE IF EXISTS `vx_sms_template_provider`;
CREATE TABLE `vx_sms_template_provider`
(
  `id`                bigint                                                  NOT NULL AUTO_INCREMENT,
  `content`           varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `provider_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
  `provider_template` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
  `template_id`       bigint                                                  NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code_tid` (`provider_code`,`template_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='服务提供方短信模板';

SET
FOREIGN_KEY_CHECKS = 1;
