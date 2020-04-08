alter table `tree`.`notification`
add column `sender_id` bigint(20) null comment '发送人id' after `outer_title`;