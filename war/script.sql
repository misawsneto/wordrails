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


use wordrails2;
drop table invitation;
alter table post drop column imageLandscape;

UPDATE person JOIN users ON person.username = users.username SET person.password = users.password;