-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- ------------------------------ GENERAL CHANGES ------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

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

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -------------------------------- CONSTRAINTS --------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

ALTER TABLE network DROP FOREIGN KEY UK_nflo9f6ndf7e7s0ohaguxv13y;
DROP INDEX UK_nflo9f6ndf7e7s0ohaguxv13y ON network;
ALTER TABLE network ADD CONSTRAINT FK_nflo9f6ndf7e7s0ohaguxv13y FOREIGN KEY (favicon_id) REFERENCES image (id);

DROP INDEX UK_rqf8v0h4ficqija7ihgxcrle5 ON person;
DROP INDEX UK_3bm7h73jen16vshmhxmdvw2t8 ON person;

ALTER TABLE image DROP FOREIGN KEY FK_47t3yd2jjthshd9fo7akkstbx;
DROP INDEX FK_47t3yd2jjthshd9fo7akkstbx ON image;
ALTER TABLE image DROP FOREIGN KEY FK_aedika0r5f8e3v0o7mxp9mbjj;
DROP INDEX FK_aedika0r5f8e3v0o7mxp9mbjj ON image;
ALTER TABLE image DROP FOREIGN KEY FK_jlmrknjsp36q8dsgmedn7dvc6;
DROP INDEX FK_jlmrknjsp36q8dsgmedn7dvc6 ON image;
ALTER TABLE image DROP FOREIGN KEY FK_t2gabdofdotdaxia4yamfwve5;
DROP INDEX FK_t2gabdofdotdaxia4yamfwve5 ON image;

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- --------------------------- DELETE DUPLICATE POSTS --------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

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

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -------------------------------- MULTITENANT --------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

#--------------------------------- ADD TENANT ID ---------------------------------#
ALTER TABLE station ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE post ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE users ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE authorities ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE cell ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE comment ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE file ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE image ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE invitation ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE network CHANGE subdomain tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE notification ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE passwordreset CHANGE networkSubdomain tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE person_network_role ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE person ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE person_station_role ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE personnetworkregid ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE personnetworktoken ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE picture ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE postread ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE promotion ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE recommend ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE term_perspective ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE row ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE section ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE sponsor ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE station_perspective ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE taxonomy ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE term ADD tenantId VARCHAR(255) DEFAULT '';
ALTER TABLE userconnection ADD tenantId VARCHAR(255) DEFAULT '';

#--------------------------------- SET TENANT ID ---------------------------------#
UPDATE station
	JOIN network ON network.id = station.network_id
SET station.tenantId = network.tenantId;

UPDATE post
	JOIN station ON station.id = post.station_id
SET post.tenantId = station.tenantId;

UPDATE users
	JOIN network ON network.id = users.networkId
SET users.tenantId = network.tenantId;

UPDATE authorities
	JOIN users ON users.id = authorities.user_id
SET authorities.tenantId = users.tenantId;

UPDATE cell
	JOIN post ON post.id = cell.post_id
SET cell.tenantId = post.tenantId;

UPDATE comment
	JOIN post ON post.id = comment.post_id
SET comment.tenantId = post.tenantId;

UPDATE file
	JOIN network ON network.id = file.networkId
SET file.tenantId = network.tenantId;

UPDATE invitation
	JOIN network ON network.id = invitation.network_id
SET invitation.tenantId = network.tenantId;

UPDATE notification
	JOIN post ON post.id = notification.post_id
SET notification.tenantId = post.tenantId;
UPDATE person
	JOIN users ON users.id = person.user_id
SET person.tenantId = users.tenantId;

UPDATE person_network_role
	JOIN person ON person.id = person_network_role.person_id
SET person_network_role.tenantId = person.tenantId;

UPDATE person_station_role
	JOIN person ON person.id = person_station_role.person_id
SET person_station_role.tenantId = person.tenantId;

UPDATE personnetworkregid
	JOIN network ON network.id = personnetworkregid.networkId
SET personnetworkregid.tenantId = network.tenantId;

UPDATE personnetworktoken
	JOIN network ON network.id = personnetworktoken.network_id
SET personnetworktoken.tenantId = network.tenantId;

UPDATE picture
	JOIN network ON network.id = picture.networkId
SET picture.tenantId = network.tenantId;

UPDATE postread
	JOIN post ON post.id = postread.post_id
SET postread.tenantId = post.tenantId;

UPDATE promotion
	JOIN post ON post.id = promotion.post_id
SET promotion.tenantId = post.tenantId;

UPDATE recommend
	JOIN post ON post.id = recommend.post_id
SET recommend.tenantId = post.tenantId;

UPDATE term_perspective
	JOIN station ON station.id = term_perspective.stationId
SET term_perspective.tenantId = station.tenantId;

UPDATE row
	JOIN term_perspective
		ON (term_perspective.id = row.featuring_perspective AND row.featuring_perspective IS NOT NULL)
			 OR (term_perspective.id = row.home_perspective AND row.home_perspective IS NOT NULL)
			 OR (term_perspective.id = row.perspective_id AND row.perspective_id IS NOT NULL)
			 OR (term_perspective.id = row.splashed_perspective AND row.splashed_perspective IS NOT NULL)
SET row.tenantId = term_perspective.tenantId;

UPDATE section
	JOIN network ON network.id = section.network_id
SET section.tenantId = network.tenantId;

UPDATE sponsor
	JOIN network ON network.id = sponsor.network_id
SET sponsor.tenantId = network.tenantId;

UPDATE station_perspective
	JOIN station ON station.id = station_perspective.station_id
SET station_perspective.tenantId = station.tenantId;

UPDATE taxonomy
	JOIN network ON (network.id = taxonomy.owningNetwork_id AND taxonomy.owningNetwork_id IS NOT NULL)
SET taxonomy.tenantId = network.tenantId;
UPDATE taxonomy
	JOIN station ON (station.id = taxonomy.owningStation_id AND taxonomy.owningStation_id IS NOT NULL)
SET taxonomy.tenantId = station.tenantId;

UPDATE term
	JOIN taxonomy ON taxonomy.id = term.taxonomy_id
SET term.tenantId = taxonomy.tenantId;

UPDATE userconnection
	JOIN users ON users.id = userconnection.user_id
SET userconnection.tenantId = users.tenantId;


-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- ---------------------------------- IMAGES -----------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

-- ------------ SAVE FILES BEING USED IN BODIES OF POSTS IN A TEMP TABLE ------------

DROP TABLE IF EXISTS temp_file;
CREATE TABLE temp_file (
	id       INT,
	tenantId VARCHAR(80),
	UNIQUE (id)
);

INSERT INTO temp_file (id, tenantId) SELECT CONVERT(
																								SUBSTRING_INDEX(SUBSTRING_INDEX(body, '/api/files/', -n.N), '/contents',
																																1),
																								UNSIGNED INTEGER) value, tenantId
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

-- ------------ SAVE ALL IMAGES BEING USED IN A TEMP TABLE ------------

DELETE FROM post_image;
DELETE FROM image_hash;
DELETE FROM image_picture;
DELETE FROM picture;

DROP TABLE IF EXISTS temp_image;
CREATE TABLE temp_image (
	id       INT,
	tenantId VARCHAR(80),
	UNIQUE (id)
);

INSERT INTO temp_image (id, tenantId) SELECT featuredImage_id, tenantId
																			FROM post
																			WHERE featuredImage_id IS NOT NULL AND featuredImage_id NOT IN (SELECT id
																																																			FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT favicon_id, tenantId
																			FROM network
																			WHERE favicon_id IS NOT NULL AND favicon_id NOT IN (SELECT id
																																													FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT splashImage_id, tenantId
																			FROM network
																			WHERE splashImage_id IS NOT NULL AND splashImage_id NOT IN (SELECT id
																																																	FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT loginImage_id, tenantId
																			FROM network
																			WHERE loginImage_id IS NOT NULL AND loginImage_id NOT IN (SELECT id
																																																FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT image_id, tenantId
																			FROM person
																			WHERE image_id IS NOT NULL AND image_id NOT IN (SELECT id
																																											FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT cover_id, tenantId
																			FROM person
																			WHERE cover_id IS NOT NULL AND cover_id NOT IN (SELECT id
																																											FROM temp_image);
INSERT INTO temp_image (id, tenantId) SELECT logo_id, tenantId
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

-- ------------ REMOVE IMAGES AND FILES THAT ARE NOT BEING USED AND UPDATE THE TENANT ID ------------

UPDATE image
	JOIN temp_image ON image.id = temp_image.id
SET image.tenantId = temp_image.tenantId;

UPDATE network
SET network.logo_id = NULL;

DELETE image FROM image
WHERE tenantId IS NULL OR image.tenantId = '';

DROP TABLE IF EXISTS temp_file;
DROP TABLE IF EXISTS temp_image;

UPDATE file
	JOIN image img
		ON (file.id = img.original_id)
			 OR (file.id = img.large_id)
			 OR (file.id = img.medium_id)
			 OR (file.id = img.small_id)
SET file.tenantId = img.tenantId;

DELETE file FROM file
WHERE tenantId IS NULL OR file.tenantId = '';

-- ------------ DELETE ALL FILES WITHOUT CONTENT OR HASH (USELESS) ------------

UPDATE network
	JOIN image ON network.loginImage_id = image.id
	JOIN file ON image.original_id = file.id
SET network.loginImage_id = NULL
WHERE file.hash IS NULL AND file.contents IS NULL;

UPDATE network
	JOIN image ON network.splashImage_id = image.id
	JOIN file ON image.original_id = file.id
SET network.splashImage_id = NULL
WHERE file.hash IS NULL AND file.contents IS NULL;

UPDATE network
	JOIN image ON network.favicon_id = image.id
	JOIN file ON image.original_id = file.id
SET network.favicon_id = NULL
WHERE file.hash IS NULL AND file.contents IS NULL;


DELETE image FROM image
	JOIN file ON image.original_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL);

DELETE image FROM image
	JOIN file ON image.large_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL);

DELETE image FROM image
	JOIN file ON image.medium_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL);

DELETE image FROM image
	JOIN file ON image.small_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL);

DELETE file FROM file
WHERE contents IS NULL AND hash IS NULL;

-- ------------ MAKE IMAGES UNIQUE AND ONE TO MANY ------------

DROP TABLE IF EXISTS temp_image;
CREATE TABLE temp_image (
	id   INT,
	hash VARCHAR(100),
	UNIQUE (id)
);

DROP TABLE IF EXISTS temp_image_duplicated;
CREATE TABLE temp_image_duplicated (
	id         INT,
	originalId INT,
	hash       VARCHAR(100),
	UNIQUE (id)
);

INSERT INTO temp_image (id, hash) SELECT id, originalHash
																	FROM image
																	WHERE originalHash IS NOT NULL
																	GROUP BY originalHash HAVING count(originalHash) > 1;

INSERT INTO temp_image_duplicated (id, originalId, hash) SELECT image.id, temp_image.id, originalHash
																												 FROM image
																													 JOIN temp_image ON image.originalHash = temp_image.hash
																												 WHERE image.id > temp_image.id;


UPDATE post
	JOIN temp_image_duplicated img ON post.featuredImage_id = img.id
SET post.featuredImage_id = img.originalId;

UPDATE network
	JOIN temp_image_duplicated img ON network.favicon_id = img.id
SET network.favicon_id = img.originalId;

UPDATE network
	JOIN temp_image_duplicated img ON network.splashImage_id = img.id
SET network.splashImage_id = img.originalId;

UPDATE network
	JOIN temp_image_duplicated img ON network.loginImage_id = img.id
SET network.loginImage_id = img.originalId;

UPDATE person
	JOIN temp_image_duplicated img ON person.image_id = img.id
SET person.image_id = img.originalId;

UPDATE person
	JOIN temp_image_duplicated img ON person.cover_id = img.id
SET person.cover_id = img.originalId;

UPDATE station
	JOIN temp_image_duplicated img ON station.logo_id = img.id
SET station.logo_id = img.originalId;

DELETE image FROM image
	JOIN temp_image_duplicated ON image.id = temp_image_duplicated.id;

DELETE file FROM file
WHERE file.id NOT IN (SELECT *
											FROM (SELECT file.id
														FROM image img JOIN file
																ON (file.id = img.original_id)
																	 OR (file.id = img.large_id)
																	 OR (file.id = img.medium_id)
																	 OR (file.id = img.small_id)) AS p);

-- ------------ MAKE FILES UNIQUE AND ONE TO MANY ------------

DROP TABLE IF EXISTS temp_file;
CREATE TABLE temp_file (
	id   INT,
	hash VARCHAR(100),
	UNIQUE (id)
);

DROP TABLE IF EXISTS temp_file_duplicated;
CREATE TABLE temp_file_duplicated (
	id         INT,
	originalId INT,
	hash       VARCHAR(100),
	UNIQUE (id)
);

INSERT INTO temp_file (id, hash) SELECT id, hash
																 FROM file
																 WHERE hash IS NOT NULL
																 GROUP BY hash HAVING count(hash) > 1;

INSERT INTO temp_file_duplicated (id, originalId, hash) SELECT file.id, temp_file.id, file.hash
																												FROM file
																													JOIN temp_file ON file.hash = temp_file.hash
																												WHERE file.id > temp_file.id;

UPDATE image
	JOIN temp_file_duplicated file ON image.original_id = file.id
SET image.original_id = file.originalId;
UPDATE image
	JOIN temp_file_duplicated file ON image.large_id = file.id
SET image.large_id = file.originalId;
UPDATE image
	JOIN temp_file_duplicated file ON image.medium_id = file.id
SET image.medium_id = file.originalId;
UPDATE image
	JOIN temp_file_duplicated file ON image.small_id = file.id
SET image.small_id = file.originalId;

DELETE file FROM file
	JOIN temp_file_duplicated ON file.id = temp_file_duplicated.id;

-- ------------ POPULATE THE TABLES PICTURES, IMAGE_HASH AND IMAGE_PICTURE ------------

DROP TABLE IF EXISTS post_image;
DELETE FROM image_hash;
DELETE FROM image_picture;
DELETE FROM picture;

INSERT INTO image_hash (image_id, hash, sizeTag)
	SELECT img.id, tf.hash, 'original'
	FROM image img
		JOIN file tf ON tf.id = img.original_id;
INSERT INTO image_hash (image_id, hash, sizeTag)
	SELECT img.id, tf.hash, 'large'
	FROM image img
		JOIN file tf ON tf.id = img.large_id;
INSERT INTO image_hash (image_id, hash, sizeTag)
	SELECT img.id, tf.hash, 'medium'
	FROM image img
		JOIN file tf ON tf.id = img.medium_id;
INSERT INTO image_hash (image_id, hash, sizeTag)
	SELECT img.id, tf.hash, 'small'
	FROM image img
		JOIN file tf ON tf.id = img.small_id;


INSERT INTO picture (tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.tenantId, tf.id, 'original', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.original_id;
INSERT INTO picture (tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.tenantId, tf.id, 'large', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.large_id;
INSERT INTO picture (tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.tenantId, tf.id, 'medium', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.medium_id;
INSERT INTO picture (tenantId, file_id, sizeTag, createdAt, updatedAt)
	SELECT img.tenantId, tf.id, 'small', img.createdAt, img.updatedAt
	FROM image img
		JOIN file tf ON tf.id = img.small_id;


INSERT INTO image_picture (image_id, pictures_id)
	SELECT img.id, pic.id
	FROM image img
		JOIN picture pic
			ON pic.file_id = img.original_id
				 OR pic.file_id = img.large_id
				 OR pic.file_id = img.medium_id
				 OR pic.file_id = img.small_id;


ALTER TABLE image DROP COLUMN original_id;
ALTER TABLE image DROP COLUMN large_id;
ALTER TABLE image DROP COLUMN medium_id;
ALTER TABLE image DROP COLUMN small_id;