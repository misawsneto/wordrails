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

DELETE person_station_role FROM person_station_role
	INNER JOIN station
		ON person_station_role.station_id = station.id AND station.tenantId != person_station_role.tenantId;


UPDATE person AS p1
	INNER JOIN (SELECT tenantId, id
							FROM person
							GROUP BY tenantId HAVING sum(networkAdmin = 1) = 0) AS p2 ON p1.tenantId = p2.tenantId AND p1.id = p2.id
SET p1.networkAdmin = TRUE;

INSERT INTO person_station_role (admin, editor, writer, person_id, station_id, tenantId)
	SELECT TRUE, FALSE, FALSE, person.id pid, station.id sid, person.tenantId
	FROM person, station
	WHERE person.networkAdmin IS TRUE AND person.tenantId = station.tenantId
ON DUPLICATE KEY UPDATE admin = TRUE, editor = FALSE, writer = FALSE;

INSERT INTO authorities (authority, user_id, createdAt, tenantId)
	SELECT "ROLE_USER", id, createdAt, tenantId
	FROM users;

INSERT INTO authorities (authority, user_id, createdAt, tenantId)
	SELECT "ROLE_ADMIN", user_id, createdAt, tenantId
	FROM person where networkAdmin is true;