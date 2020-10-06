
CREATE TABLE `business_bank_account` (
  `business_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `business_Amount` int(10) DEFAULT NULL COMMENT '账号余额',
  `freezed_Amount` int(10) DEFAULT NULL COMMENT '冻结金额',
  PRIMARY KEY (`business_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10002 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of business_bank_account
-- ----------------------------
INSERT INTO `business_bank_account` VALUES ('10001', '500', '0');

-- ----------------------------
-- Table structure for `shop_order`
-- ----------------------------
DROP TABLE IF EXISTS `shop_order`;
CREATE TABLE `shop_order` (
  `oid` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `pid` int(10) DEFAULT NULL,
  `pname` varchar(30) DEFAULT NULL,
  `pprice` double(30,0) DEFAULT NULL,
  `number` int(20) DEFAULT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_order
-- ----------------------------

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
