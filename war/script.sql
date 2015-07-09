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





-- NEW LOGIN --


ALTER TABLE users DROP PRIMARY KEY;
ALTER TABLE users ADD id INT PRIMARY KEY AUTO_INCREMENT;
ALTER TABLE users
ADD COLUMN `network_id` int(11) DEFAULT NULL,
ADD FOREIGN KEY (`network_id`) REFERENCES `network` (`id`);

ALTER TABLE person
ADD COLUMN `user_id` int(11) DEFAULT NULL,
ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
delete from person_network_role where person_id = 1;


DROP TABLE authorities;
CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) NOT NULL,
  `network_id` int(11) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_f2dja5uh2fyeo5s7l0t7s7ph3` (`network_id`),
  KEY `FK_cia0ypr3lttjoogbv99964fk7` (`station_id`),
  KEY `FK_s21btj9mlob1djhm3amivbe5e` (`user_id`),
  CONSTRAINT `FK_cia0ypr3lttjoogbv99964fk7` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_f2dja5uh2fyeo5s7l0t7s7ph3` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`),
  CONSTRAINT `FK_s21btj9mlob1djhm3amivbe5e` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DELETE n1 FROM person_network_role n1, person_network_role n2 WHERE n1.network_id > n2.network_id AND n1.person_id = n2.person_id;
UPDATE person JOIN users ON person.username = users.username SET person.user_id = users.id;
UPDATE users INNER JOIN (SELECT p.username, r.network_id FROM person AS p JOIN person_network_role AS r ON p.id = r.person_id) t1 ON users.username = t1.username SET users.network_id = t1.network_id;
INSERT INTO authorities (authority, network_id, user_id)
  SELECT DISTINCT "ROLE_USER", network_id, id FROM users;