alter table `tree`.`comment`
add column `addition` tinyint(1) null default 0 comment '是否加积分' after `target_id`,
add column `question_id` bigint(20) null comment '帖子id' after `addition`;

alter table `tree`.`user_like`
add column `question_id` bigint(20) null comment '所属帖子id' after `gmt_motified`;
