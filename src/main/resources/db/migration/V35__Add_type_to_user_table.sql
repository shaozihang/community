alter table `tree`.`user`
add column `type` int null default 1 comment '1为用户，2为管理员' after `score`;

drop table tree.role ;
drop table tree.user_role ;