/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50727
Source Host           : localhost:3333
Source Database       : shop-product

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2020-10-06 11:58:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `business_bank_account`
-- ----------------------------
DROP TABLE IF EXISTS `business_bank_account`;
CREATE TABLE `business_bank_account` (
  `business_id` int(11) NOT NULL AUTO_INCREMENT,
  `business_amount` int(11) NOT NULL,
  `freezed_amount` int(11) NOT NULL,
  PRIMARY KEY (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of business_bank_account
-- ----------------------------

-- ----------------------------
-- Table structure for `merchants_bank_account`
-- ----------------------------
DROP TABLE IF EXISTS `merchants_bank_account`;
CREATE TABLE `merchants_bank_account` (
  `merchants_id` int(10) NOT NULL AUTO_INCREMENT,
  `merchants_amount` int(10) DEFAULT NULL COMMENT '账号余额',
  `freezed_amount` int(10) DEFAULT NULL COMMENT '冻结金额',
  PRIMARY KEY (`merchants_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000011 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of merchants_bank_account
-- ----------------------------
INSERT INTO `merchants_bank_account` VALUES ('1000010', '500', '300');

-- ----------------------------
-- Table structure for `shop_order`
-- ----------------------------
DROP TABLE IF EXISTS `shop_order`;
CREATE TABLE `shop_order` (
  `oid` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `pname` varchar(255) DEFAULT NULL,
  `pprice` double DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_order
-- ----------------------------

-- ----------------------------
-- Table structure for `shop_product`
-- ----------------------------
DROP TABLE IF EXISTS `shop_product`;
CREATE TABLE `shop_product` (
  `pid` int(10) NOT NULL AUTO_INCREMENT,
  `pname` varchar(20) NOT NULL,
  `pprice` double(30,0) NOT NULL,
  `stock` int(20) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_product
-- ----------------------------
INSERT INTO `shop_product` VALUES ('1', '小米', '1000', '5000');
INSERT INTO `shop_product` VALUES ('2', '华为', '2000', '5000');
INSERT INTO `shop_product` VALUES ('3', '苹果', '3000', '5000');
INSERT INTO `shop_product` VALUES ('4', 'OPPO', '4000', '5000');

-- ----------------------------
-- Table structure for `undo_log`
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
