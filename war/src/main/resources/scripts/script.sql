-- ORDEM PARA RODAR OS SCRIPTS:

-- script.sql
-- constraints.sql
-- duplicate_post.sql
-- multitenant.sql
-- images.sql


DROP TABLE IF EXISTS section_fixedquery;
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