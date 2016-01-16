-- ORDEM PARA RODAR OS SCRIPTS:

-- script.sql
-- constraints.sql
-- duplicate_post.sql
-- multitenant.sql
-- images.sql

drop table IF EXISTS section_fixedquery;
drop table IF EXISTS section_properties;
drop table IF EXISTS section_queryable_list;
drop table IF EXISTS section_section_container;
DROP TABLE IF EXISTS person_bookmark;

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

DROP TABLE IF EXISTS bookmark;

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