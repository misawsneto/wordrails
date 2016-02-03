ALTER TABLE person ADD COLUMN `networkAdmin` BIT(1) NOT NULL;

UPDATE person
	JOIN person_network_role ON person.id = person_network_role.person_id
SET networkAdmin = TRUE
WHERE person_network_role.admin IS TRUE;