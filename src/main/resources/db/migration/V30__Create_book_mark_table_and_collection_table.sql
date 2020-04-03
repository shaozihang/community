create table `tree`.`book_mark` (
  `id` bigint(20) not null auto_increment comment '主键',
  `user_id` bigint(20) null comment '用户id',
  `name` varchar(100) null comment '收藏夹名称',
  `description` varchar(600) null comment '收藏夹描述',
  `is_private` tinyint(1) null comment '是否私密',
  `gmt_create` bigint(20) null comment '创建时间',
  primary key (`id`));

create table `tree`.`collection` (
  `id` bigint(20) not null auto_increment comment '主键',
  `user_id` bigint(20) null comment '用户id',
  `question_id` bigint(20) null comment '帖子id',
  `folder_id` bigint(20) null comment '收藏夹id',
  `gmt_create` bigint(20) null comment '创建时间',
  primary key (`id`));
