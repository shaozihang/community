create table `tree`.`follow_and_fans` (
  `id` bigint(20) not null auto_increment,
  `from_user_id` bigint(20) null,
  `to_user_id` bigint(20) null,
  `status` int(1) null,
  `gmt_create` bigint(20) null,
  `gmt_modified` bigint(20) null,
  primary key (`id`));
alter table `tree`.`user`
add column `sex` int(1) null default 0 after `gmt_motified`,
add column `sign` varchar(60) null after `sex`,
add column `grade` int(1) null default 1 after `sign`,
add column `follow_count` int(4) null default 0 after `grade`,
add column `fans_count` int(11) null default 0 after `follow_count`;
