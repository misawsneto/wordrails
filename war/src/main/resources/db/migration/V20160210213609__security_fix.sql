ALTER TABLE person ADD COLUMN `networkAdmin` BIT(1) NOT NULL;

UPDATE person
	JOIN person_network_role ON person.id = person_network_role.person_id
SET networkAdmin = TRUE
WHERE person_network_role.admin IS TRUE;

DROP TABLE authorities;
DROP TABLE person_network_role;

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- --------------------------- DELETE DUPLICATE USERS --------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

DELETE userconnection FROM userconnection
WHERE user_id IN (SELECT n1.id
									FROM users n1, users n2
									WHERE n1.id > n2.id AND n1.username = n2.username AND n1.tenantId = n2.tenantId);

DELETE person_station_role FROM person_station_role
WHERE person_id IN (SELECT id
										FROM person
										WHERE user_id IN (SELECT n1.id
																			FROM users n1, users n2
																			WHERE n1.id > n2.id AND n1.username = n2.username AND n1.tenantId = n2.tenantId));

DELETE person FROM person
WHERE user_id IN (SELECT n1.id
									FROM users n1, users n2
									WHERE n1.id > n2.id AND n1.username = n2.username AND n1.tenantId = n2.tenantId);

DELETE n1 FROM users n1, users n2
WHERE n1.id > n2.id AND n1.username = n2.username AND n1.tenantId = n2.tenantId;

-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- --------------------------- INSERT INTO ACL TABLES --------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------
-- -----------------------------------------------------------------------------

INSERT INTO acl_class (class) VALUES ('co.xarx.trix.domain.Station');
INSERT INTO acl_class (class) VALUES ('co.xarx.trix.domain.Post');
INSERT INTO acl_class (class) VALUES ('co.xarx.trix.domain.Comment');

INSERT INTO acl_sid (principal, sid, tenantId) SELECT 1, username, tenantId
																							 FROM users;

