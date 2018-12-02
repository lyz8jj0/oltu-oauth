CREATE DATABASE `uc` DEFAULT CHARACTER SET utf8 ;

USE `oa`;

-- code 超时时间 定为10分钟
DROP TABLE IF EXISTS `access_code`;
CREATE TABLE `access_code` (
  `id` varchar(40) NOT NULL ,
  `client_id` VARCHAR(50) NOT NULL COMMENT '应用id',
  `user_id` varchar(40) NOT NULL COMMENT '用户id',
  `access_code` VARCHAR(50) NOT NULL COMMENT '授权code',
  `expires` VARCHAR (50) NOT NULL COMMENT 'code过期时间',
  `createdatetime` DATETIME NOT NULL COMMENT '创建时间',
  `modifydatetime` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

--  token 的超时时间定为24小时
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
  `id` varchar(40) NOT NULL,
  `client_id` varchar(40) NOT NULL COMMENT '应用id',
  `user_id` varchar(40) NOT NULL COMMENT '用户id',
  `access_token` VARCHAR(50) NOT NULL COMMENT '授权token',
  `expires` VARCHAR (50) NOT NULL COMMENT 'token过期时间',
  `createdatetime` DATETIME NOT NULL COMMENT '创建时间',
  `modifydatetime` DATETIME NOT NULL COMMENT '更新时间',
  `token_type` varchar(20) NOT NULL COMMENT '0是人员token  1为设备token' ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


