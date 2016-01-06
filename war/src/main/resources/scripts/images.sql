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
		SELECT img.id, tf.tenantId FROM image img
		JOIN temp_file tf
				ON (tf.id = img.original_id)
				OR (tf.id = img.large_id)
				OR (tf.id = img.medium_id)
				OR (tf.id = img.small_id)
		WHERE img.id NOT IN (SELECT id FROM temp_image);


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