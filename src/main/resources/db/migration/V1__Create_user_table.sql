create table user
(
  id           int auto_increment not null ,
  account_id   varchar(100),
  name         varchar(50),
  token        char,
  gmt_create   bigint,
  gmt_modified bigint,
  constraint user_pk
    primary key (id)
);