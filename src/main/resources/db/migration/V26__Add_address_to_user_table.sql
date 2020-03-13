alter table `tree`.`user`
add column `address` varchar(50) null default '保密' after `fans_count`;