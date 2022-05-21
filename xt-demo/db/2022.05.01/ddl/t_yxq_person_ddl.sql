DROP TABLE IF EXISTS `t_yxq_person`;
CREATE TABLE `t_yxq_person` (
    `person_id` bigint(20) NOT NULL COMMENT '用户id',
    `open_id` varchar(64) NOT NULL COMMENT 'openId',
    `level` varchar(4) NOT NULL COMMENT '账号可信等级',
    `agree_policy_flag` smallint(4) DEFAULT '0' COMMENT '同意隐私协议标识,0:不同意,1:同意',
    `create_by` varchar(64) NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) NOT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`person_id`),
    KEY `idx_t_yxq_person_update_time` (`update_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='粤信签用户表';