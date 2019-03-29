drop table if exists `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `register_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '注册方式, 0: 手机, 1: 微信, 2: QQ, 3: 微博',
  `username` varchar(32) NOT NULL DEFAULT '',
  `password` varchar(64) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `login_time` datetime NOT NULL COMMENT '最后登录时间',
  `login_ip` varchar(15) NOT NULL COMMENT '最后登录IP',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
create UNIQUE index udx_login on `user`(`username`, `password`);
create index idx_uid on `user`(`uid`);

drop table if exists `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(128) NOT NULL DEFAULT '' COMMENT '邮箱',
  `phone` varchar(11) NOT NULL DEFAULT '' COMMENT '手机',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户状态, 0: 正常, 1: 异常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
create index idx_uid3 on `user_info`(`uid`);
