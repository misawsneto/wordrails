-- Base select
-- SELECT s.id from image i, file f, station s WHERE i.original_id = f.id and f.networkId is NULL and s.logo_id = i.id;

-- Fix broken logo_id in table network
UPDATE network n, image i, file f SET n.logo_id = NULL, n.loginImage_id = NULL WHERE i.original_id = f.id and f.networkId is NULL and n.logo_id = i.id;

-- Fix broken loginImage_id in table network
UPDATE network n, image i, file f SET n.loginImage_id = NULL WHERE i.original_id = f.id and f.networkId is NULL and n.loginImage_id = i.id;

-- Fix broken favicon_id
UPDATE network n, image i, file f SET n.favicon_id = NULL WHERE i.original_id = f.id and f.networkId is NULL and n.favicon_id = i.id;

-- Fix splashImage_id
UPDATE network n, image i, file f SET n.splashImage_id = NULL WHERE i.original_id = f.id and f.networkId is NULL and n.splashImage_id = i.id;

-- Fix station logo_id reference
UPDATE station s, image i, file f SET s.logo_id = NULL WHERE i.original_id = f.id and f.networkId is NULL and s.logo_id = i.id;

-- Delete files and image with null networkId
DELETE i, f from image i, file f WHERE i.original_id = f.id and f.networkId is NULL;

ALTER TABLE post DROP FOREIGN KEY FK_eolhsgjjrm4w3rxg9q84vebfn;
ALTER TABLE post DROP INDEX FK_eolhsgjjrm4w3rxg9q84vebfn;