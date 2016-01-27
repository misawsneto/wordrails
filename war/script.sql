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

UPDATE users JOIN person ON person.user_id = users.id SET users.networkId = person.networkId, users.network_id = person.networkId;

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

DROP TABLE IF EXISTS `person_bookmark`;

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

DELETE FROM taxonomy
WHERE type = "A";
DELETE post_term FROM post_term
	JOIN term ON post_term.terms_id = term.id
	JOIN taxonomy ON term.taxonomy_id = taxonomy.id
WHERE taxonomy.type = "T";
DELETE term FROM term
	JOIN taxonomy ON term.taxonomy_id = taxonomy.id
WHERE taxonomy.type = "T";
DELETE FROM taxonomy
WHERE type = "T";

ALTER TABLE term DROP INDEX UK_68x4pioq3b3mu1t3jrp01bsss;
ALTER TABLE term DROP INDEX UK_kki6crlp9p5g7979h2wb4imgh;
ALTER TABLE term DROP INDEX UK_h725nbm620imfiywywc1w8jo1;
ALTER TABLE term DROP INDEX UK_lixtbau20i1s7rq5evq6gl10p;
ALTER TABLE term ADD `name_parent` VARCHAR (255) DEFAULT NULL;

select concat(term.name, concat('_', term.parent_id)) from term;
UPDATE term SET term.name_parent = concat(term.name, concat('_', term.parent_id)) where term.parent_id is not null;
UPDATE term SET term.name_parent = concat(term.name, '_0') where term.parent_id is null;