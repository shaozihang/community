drop table `tree`.`user`;
create table `tree`.`user` (
  `id` bigint(20) not null auto_increment,
  `nick_name` varchar(40) null,
  `phone` varchar(30) null,
  `password` varchar(50) null,
  `email` varchar(30) null,
  `token` varchar(50) null,
  `avatar_url` varchar(100) null,
  `gmt_cteate` bigint(20) null,
  `gmt_motified` bigint(20) null,
  primary key (`id`));
create table `tree`.`userOauths` (
  `id` bigint(20) not null auto_increment,
  `uid` bigint(20) null,
  `account_id` varchar(100) null,
  `type` int null,
  primary key (`id`));