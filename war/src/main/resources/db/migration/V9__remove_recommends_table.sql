CREATE TABLE IF NOT EXISTS `person_recommend` (
	`person_id` INT(11) NOT NULL,
	`post_id`   INT(11) DEFAULT NULL,
	KEY `FK_uisabuidsbauiobduasuada` (`person_id`),
	CONSTRAINT `FK_uisabuidsbauiobduasuada` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8;


INSERT INTO person_recommend (person_id, post_id)
	SELECT person_id, post_id
	FROM recommend;

DROP TABLE IF EXISTS recommend;