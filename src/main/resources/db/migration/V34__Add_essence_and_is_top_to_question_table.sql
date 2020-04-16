alter table `tree`.`question`
add column `essence` tinyint(1) null default 0 comment '精华，0为否，1为是' after `type`,
add column `is_top` tinyint(1) null default 0 comment '是否置顶，0为否，1为是' after `essence`;