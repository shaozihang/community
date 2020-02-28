create table `tree`.`role` (
  `id` bigint(20) not null auto_increment,
  `name` varchar(45) null,
  primary key (`id`));
create table `tree`.`user_role` (
  `id` bigint(20) not null auto_increment,
  `user_id` bigint(20) null,
  `role_id` bigint(20) null,
  primary key (`id`));