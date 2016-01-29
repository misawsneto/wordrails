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


DELETE postread FROM postread
WHERE post_id IN (SELECT n1.id
									FROM post n1, post n2
									WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id);

DELETE post_term FROM post_term
WHERE posts_id IN (SELECT n1.id
									 FROM post n1, post n2
									 WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id);

DELETE comment FROM comment
WHERE post_id IN (SELECT n1.id
									FROM post n1, post n2
									WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id);

DELETE n1 FROM post n1, post n2
WHERE n1.id > n2.id AND n1.featuredImage_id = n2.featuredImage_id;



DROP TABLE IF EXISTS temp_file;
CREATE TABLE temp_file (
	id       INT,
	tenantId VARCHAR(80),
	UNIQUE (id)
);

INSERT INTO temp_file (id, tenantId) SELECT CONVERT(
																								SUBSTRING_INDEX(SUBSTRING_INDEX(body, '/api/files/', -n.N), '/contents',
																																1),
																								UNSIGNED INTEGER) value, networkId
																		 FROM post t CROSS JOIN
																			 (
																				 SELECT 1 AS N
																				 UNION ALL SELECT 2
																				 UNION ALL SELECT 3
																				 UNION ALL SELECT 4
																				 UNION ALL SELECT 5
																				 UNION ALL SELECT 6
																				 UNION ALL SELECT 7
																				 UNION ALL SELECT 8
																				 UNION ALL SELECT 9
																			 ) n
																		 WHERE body LIKE repeat('%/api/files/%/contents%', n.N)
																		 GROUP BY value;

DROP TABLE IF EXISTS temp_image;
CREATE TABLE temp_image (
	id       INT,
	tenantId VARCHAR(80),
	UNIQUE (id)
);

INSERT INTO temp_image (id, tenantId) SELECT featuredImage_id, networkId
																			FROM post
																			WHERE featuredImage_id IS NOT NULL AND featuredImage_id NOT IN (SELECT id
																																																			FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT favicon_id, id
																			FROM network
																			WHERE favicon_id IS NOT NULL AND favicon_id NOT IN (SELECT id
																																													FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT splashImage_id, id
																			FROM network
																			WHERE splashImage_id IS NOT NULL AND splashImage_id NOT IN (SELECT id
																																																	FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT loginImage_id, id
																			FROM network
																			WHERE loginImage_id IS NOT NULL AND loginImage_id NOT IN (SELECT id
																																																FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT image_id, networkId
																			FROM person
																			WHERE image_id IS NOT NULL AND image_id NOT IN (SELECT id
																																											FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT cover_id, networkId
																			FROM person
																			WHERE cover_id IS NOT NULL AND cover_id NOT IN (SELECT id
																																											FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT logo_id, networkId
																			FROM station
																			WHERE logo_id IS NOT NULL AND logo_id NOT IN (SELECT id
																																										FROM temp_image);

INSERT INTO temp_image (id, tenantId)
	SELECT img.id, tf.tenantId
	FROM image img
		JOIN temp_file tf
			ON (tf.id = img.original_id)
				 OR (tf.id = img.large_id)
				 OR (tf.id = img.medium_id)
				 OR (tf.id = img.small_id)
	WHERE img.id NOT IN (SELECT id
											 FROM temp_image);

UPDATE image
	JOIN temp_image ON image.id = temp_image.id
SET image.networkId = temp_image.tenantId;

DROP TABLE IF EXISTS temp_image;
DROP TABLE IF EXISTS temp_file;


DROP TABLE IF EXISTS post_image;
DELETE FROM image_hash;
DELETE FROM image_picture;
DELETE FROM picture;

INSERT INTO image_hash (image_id, hash, sizeTag)
	SELECT img.id, tf.hash, 'large'
	FROM image img
		JOIN file tf ON tf.id = img.large_id
	WHERE tf.hash IS NOT NULL;
INSERT INTO image_hash (image_id, hash, sizeTag)
	SELECT img.id, tf.hash, 'medium'
	FROM image img
		JOIN file tf ON tf.id = img.medium_id
	WHERE tf.hash IS NOT NULL;
INSERT INTO image_hash (image_id, hash, sizeTag)
	SELECT img.id, tf.hash, 'small'
	FROM image img
		JOIN file tf ON tf.id = img.small_id
	WHERE tf.hash IS NOT NULL;


INSERT INTO picture (networkId, tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.networkId, img.tenantId, tf.id, 'original', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.original_id
	WHERE tf.hash IS NOT NULL;
INSERT INTO picture (networkId, tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.networkId, img.tenantId, tf.id, 'large', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.large_id
	WHERE tf.hash IS NOT NULL;
INSERT INTO picture (networkId, tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.networkId, img.tenantId, tf.id, 'medium', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.medium_id
	WHERE tf.hash IS NOT NULL;
INSERT INTO picture (networkId, tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.networkId, img.tenantId, tf.id, 'small', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.small_id
	WHERE tf.hash IS NOT NULL;


INSERT INTO image_picture (image_id, pictures_id)
	SELECT img.id, pic.id
	FROM image img
		JOIN picture pic
			ON pic.file_id = img.original_id
				 OR pic.file_id = img.large_id
				 OR pic.file_id = img.medium_id
				 OR pic.file_id = img.small_id;