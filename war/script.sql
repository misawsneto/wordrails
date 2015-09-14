drop database wordrails2;

create database wordrails2;
grant all on wordrails2.* to 'wordrails'@'localhost';

use wordrails2;
insert into users(username, password, enabled) values ('wordrails', 'wordrails', true);
insert into authorities(username, authority) values ('wordrails', 'ROLE_USER');
insert into person(name, username) values ('Wordrails', 'wordrails');

insert into taxonomy (name, type) values ('Categoria', 'N');
insert into network (name, trackingId, defaultTaxonomy_id) values ('wordRAILS', 'UA-53975232-1', 1);
insert into person_network_role (admin, network_id, person_id) values (1, 1, 1);
update taxonomy set owningNetwork_id = 1 where id = 1;
insert into network_taxonomy (networks_id, taxonomies_id) values (1, 1);

-- insert into taxonomy (name, type) values ('Categoria', 'N');
-- insert into network (name, subdomain, defaultTaxonomy_id) values ('portodigital', 'portodigital', 34);
-- insert into person_network_role (admin, network_id, person_id) values (1, 26, 2);
-- insert into person_network_role (admin, network_id, person_id) values (0, 26, 1);
-- update taxonomy set owningNetwork_id = 19 where id = 34;
-- insert into network_taxonomy (networks_id, taxonomies_id) values (19, 34);

alter table network drop allowComments;

-- drop network defaultTaxonomy_id;
alter table network drop foreign key FK_aq4tkatkrdlk0q9chpve3yl9l;

-- drop network defaultTaxonomy_id;
alter table network drop index FK_aq4tkatkrdlk0q9chpve3yl9l;

-- Person -> UNIQUE KEY `UK_mjvhigut8lgjs8a6c1df6qt5u` (`username`)
alter table person drop index UK_mjvhigut8lgjs8a6c1df6qt5u;

-- Person -> UNIQUE KEY `UK_585qcyc8qh7bg1fwgm1pj4fus` (`email`)
alter table person drop index UK_585qcyc8qh7bg1fwgm1pj4fus

-- Person -> UNIQUE KEY `UK_t1smi3rfq846y5teyf1gcv8ir` (`username`, `user_id`)
alter table person drop index UK_t1smi3rfq846y5teyf1gcv8ir;

alter table postread modify column person_id int(11) default null;

--------------------------------------------------------------------------
use wordrails2;
drop table invitation;
alter table post drop column imageLandscape;

ALTER TABLE person
DROP INDEX UK_585qcyc8qh7bg1fwgm1pj4fus;

------------------------------ 11/09/2015

alter table invitation modify column email varchar(255) DEFAULT NULL;
alter table person modify column email varchar(255) DEFAULT NULL;