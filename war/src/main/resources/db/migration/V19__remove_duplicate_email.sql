UPDATE person n1, person n2
SET n1.email = CONCAT(n1.email, RAND() * 100000)
WHERE n1.id > n2.id AND n1.email = n2.email and n1.tenantId = n2.tenantId;

ALTER TABLE person
	ADD CONSTRAINT UK_drx2m68rs3cqgeppsoo34lhn9 UNIQUE (email, tenantId)