--
--	Update users table
-- 
alter table users add network_id int(11) default 1;
alter table users add constraint fk_network_id foreign key (network_id) references network(id);

-- Remove primary key
ALTER TABLE users DROP PRIMARY KEY;
-- Create composite unique
ALTER TABLE `wordrails2`.`users` ADD UNIQUE INDEX `user_network` (`username` ASC, `network_id` ASC);

-- Remove indexes from table Person
ALTER TABLE `wordrails2`.`person` DROP INDEX `UK_mjvhigut8lgjs8a6c1df6qt5u` , DROP INDEX `UK_585qcyc8qh7bg1fwgm1pj4fus` ;
alter table person add network_id int(11) default 1;
alter table person add constraint f_network_id foreign key (network_id) references network(id);
ALTER TABLE `wordrails2`.`person` ADD UNIQUE INDEX `person_network` (`email` ASC, `network_id` ASC, `username` ASC);

ALTER TABLE `wordrails2`.`authorities` DROP FOREIGN KEY `FK_baahryprcge2u172egph1qwur`;
ALTER TABLE `wordrails2`.`authorities` DROP INDEX `FK_baahryprcge2u172egph1qwur` ;

alter table authorities add network_id int(11) default 1;
alter table users add constraint fk_network_id foreign key (network_id) references network(id);