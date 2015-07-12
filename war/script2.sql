
use wordrails2;

ALTER TABLE PersonNetworkRegId MODIFY person_id INT(11) default NULL;
ALTER TABLE PersonNetworkToken MODIFY person_id INT(11) default NULL;
ALTER TABLE PostRead MODIFY person_id INT(11) default NULL;

ALTER TABLE Network defaultTaxonomy_id INT(11) default NULL;

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