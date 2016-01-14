drop table IF EXISTS section_fixedquery;
drop table IF EXISTS section_properties;
drop table IF EXISTS section_queryable_list;
drop table IF EXISTS section_section_container;

UPDATE station
  JOIN network ON network.id = station.network_id
  SET networkId = network_id;

UPDATE person
  JOIN users ON person.user_id = users.id
  JOIN network ON network.id = users.networkId
  SET person.networkId = users.networkId;

UPDATE users
  JOIN network ON network.id = users.network_id
SET networkId = network_id;

update person_network_role set networkId = network_id;
update personnetworkregid set networkId = network_id;
update users set networkId = network_id;
update authorities set networkId = network_id;

ALTER TABLE term ADD networkId INT(11) DEFAULT '0';
update term
  join taxonomy on term.taxonomy_id = taxonomy.id
  join station on taxonomy.owningStation_id = station.id
  JOIN network ON network.id = station.networkId
  set term.networkId = station.networkId;

update term
  join taxonomy on term.taxonomy_id = taxonomy.id
  JOIN network ON network.id = taxonomy.owningNetwork_id
  set term.networkId = taxonomy.owningNetwork_id where taxonomy.owningNetwork_id is not null;

alter table post add networkId INT(11) DEFAULT '0';
UPDATE post
  JOIN station ON post.station_id = station.id
  JOIN network ON network.id = station.networkId
  SET post.networkId = station.networkId;

CREATE TABLE `person_bookmark` (
	`person_id` INT(11) NOT NULL,
	`post_id`   INT(11) DEFAULT NULL,
	KEY `FK_ockvlbcurheoy43wtnjegjoed` (`person_id`),
	CONSTRAINT `FK_ockvlbcurheoy43wtnjegjoed` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8;


INSERT INTO person_bookmark (person_id, post_id)
  SELECT person_id, post_id FROM bookmark;

drop table IF EXISTS bookmark;