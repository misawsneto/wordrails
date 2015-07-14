
use wordrails2;

ALTER TABLE PersonNetworkRegId MODIFY person_id INT(11) default NULL;
ALTER TABLE PersonNetworkToken MODIFY person_id INT(11) default NULL;
ALTER TABLE PostRead MODIFY person_id INT(11) default NULL;

ALTER TABLE Network MODIFY defaultTaxonomy_id INT(11) default NULL;

ALTER TABLE station drop column social;

delete from person_network_role where person_id = 125;
delete from person_station_role where person_id = 125;
delete from person where id = 125;

delete from person_network_role where person_id = 126;
delete from person_station_role where person_id = 126;
delete from person where id = 126;

-- select person.* from person_network_role pnr join Person person on person.id = pnr.person_id join users users on person.username = users.username;
-- select p.id from person p where p.username not in (select u.username from users u);
-- delete from person_station_role where person_id in (select person.id from person person where person.username not in (select u.username from users u));
-- delete from person_network_role where person_id in (select person.id from person person where person.username not in (select u.username from users u));
-- delete from authorities where username in (select person.username from person person where person.username not in (select u.username from users u));
-- delete from person where username not in (select u.username from users u);
-- delete from users where username not in (select a.username from authorities a);
-- select count(*) from users;
-- select count(*) from authorities;

--update person set username = 'adriano' where username = 'Adriano';
--delete from authorities where username = 'Adriano';
--insert into authorities (username, authority) values ('adriano', 'ROLE_USER');
--update users set username = 'adriano' where username = 'Adriano';
--
--update person set username = 'fabiana' where username = 'Fabiana';
--delete from authorities where username = 'Fabiana';
--insert into authorities (username, authority) values ('fabiana', 'ROLE_USER');
--update users set username = 'fabiana' where username = 'Fabiana';
--
--update person set username = 'styvensena' where username = 'StyvenSena';
--delete from authorities where username = 'StyvenSena';
--insert into authorities (username, authority) values ('styvensena', 'ROLE_USER');
--update users set username = 'styvensena' where username = 'StyvenSena';
--
--update person set username = 'raphael' where username = 'Raphael';
--delete from authorities where username = 'Raphael';
--insert into authorities (username, authority) values ('raphael', 'ROLE_USER');
--update users set username = 'raphael' where username = 'Raphael';
--
--update person set username = 'kaciomello' where username = 'KacioMello';
--delete from authorities where username = 'KacioMello';
--insert into authorities (username, authority) values ('kaciomello', 'ROLE_USER');
--update users set username = 'kaciomello' where username = 'KacioMello';
--
--update person set username = 'marcoscosta' where username = 'marcos costa';
--delete from authorities where username = 'marcos costa';
--insert into authorities (username, authority) values ('marcoscosta', 'ROLE_USER');
--update users set username = 'marcoscosta' where username = 'marcos costa';

--  NEW LOGIN --

DROP TABLE authorities;

ALTER TABLE users DROP PRIMARY KEY;
ALTER TABLE users ADD id INT PRIMARY KEY AUTO_INCREMENT;
ALTER TABLE users
ADD COLUMN `network_id` int(11) DEFAULT NULL,
ADD FOREIGN KEY (`network_id`) REFERENCES `network` (`id`);

ALTER TABLE person
ADD COLUMN `user_id` int(11) DEFAULT NULL,
ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
delete from person_network_role where person_id = 1;


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
INSERT INTO authorities (authority, network_id, user_id) SELECT DISTINCT "ROLE_USER", network_id, id FROM users;
