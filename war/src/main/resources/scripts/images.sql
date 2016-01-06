-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- ---------------------------- EXECUTE /api/util/addPicturesToImages ----------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------
-- -----------------------------------------------------------------------------------------------


UPDATE network
SET network.logo_id = NULL;

UPDATE network
  JOIN image ON network.loginImage_id = image.id
  JOIN file ON image.original_id = file.id
SET network.loginImage_id = NULL
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

UPDATE network
  JOIN image ON network.splashImage_id = image.id
  JOIN file ON image.original_id = file.id
SET network.splashImage_id = NULL
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

UPDATE network
  JOIN image ON network.favicon_id = image.id
  JOIN file ON image.original_id = file.id
SET network.favicon_id = NULL
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';



DELETE image_picture FROM image_picture
  JOIN picture ON image_picture.pictures_id = picture.id
  JOIN file ON picture.file_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';



DELETE image_picture FROM image_picture
  JOIN image ON image_picture.image_id = image.id
  JOIN file ON image.original_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE image_hash FROM image_hash
  JOIN image ON image_hash.image_id = image.id
  JOIN file ON image.original_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE image FROM image
  JOIN file ON image.original_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';



DELETE image_picture FROM image_picture
  JOIN image ON image_picture.image_id = image.id
  JOIN file ON image.large_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE image_hash FROM image_hash
  JOIN image ON image_hash.image_id = image.id
  JOIN file ON image.large_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE image FROM image
  JOIN file ON image.large_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';



DELETE image_picture FROM image_picture
  JOIN image ON image_picture.image_id = image.id
  JOIN file ON image.medium_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE image_hash FROM image_hash
  JOIN image ON image_hash.image_id = image.id
  JOIN file ON image.medium_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE image FROM image
  JOIN file ON image.medium_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';


DELETE image FROM image
  JOIN file ON image.small_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE picture FROM picture
  JOIN file ON picture.file_id = file.id
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';

DELETE file FROM file
WHERE (file.hash IS NULL AND file.contents IS NULL) OR file.tenantId = '';