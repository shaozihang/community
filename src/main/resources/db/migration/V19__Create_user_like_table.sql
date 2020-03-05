create table `tree`.`user_like` (
  `id` bigint(20) not null auto_increment,
  `liked_user_id` bigint(20) not null,
  `liked_post_id` bigint(20) not null,
  `type` int(1) not null,
  `status` int(1) not null,
  `gmt_cteate` bigint(20) null,
  `gmt_motified` bigint(20) null,
  primary key (`id`));