alter table `tree`.`comment`
add column `target_id` bigint(20) null after `comment_count`;