CREATE DATABASE `uc` DEFAULT CHARACTER SET utf8 ;

USE `oa`;


-- 用户登录表
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `id` varchar(40) NOT NULL,
  `loginName1` VARCHAR(30) NOT NULL COMMENT '第一用户名',
  `loginName2` VARCHAR(30) COMMENT '第二用户名',
  `loginName3` VARCHAR(30) COMMENT '第三用户名',
  `loginName4` VARCHAR(30) COMMENT '第四用户名',
  `password` VARCHAR(50) NOT NULL COMMENT '密码',
  `realname` VARCHAR(30) NOT NULL COMMENT '真实名字',
  `tel` VARCHAR(30) NULL COMMENT '电话',
  `email` VARCHAR(30) NULL COMMENT '邮箱',
  `del` int(11) NOT NULL DEFAULT 0 COMMENT '是否删除,  0未删除/1已删除',
  `createdatetime` datetime NOT NULL COMMENT '创建时间',
  `modifydatetime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;



-- 第三方应用表
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` varchar(40) NOT NULL,
  `client_id` varchar(40) NOT NULL COMMENT '应用id',
  `client_secret` varchar(50) NOT NULL COMMENT '应用secret',
  `client_name` varchar(50) NOT NULL COMMENT '应用名称',
  `home_page` varchar(100) NOT NULL COMMENT '应用首页',
  `logo` varchar(100) NOT NULL COMMENT '应用图标',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除,  0未删除/1已删除',
  `createdatetime` datetime NOT NULL COMMENT '创建时间',
  `modifydatetime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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




