DELETE FROM applecertificate
WHERE tenantId = 'previ';

ALTER TABLE applecertificate
	ADD COLUMN `network_id` INT(11) NOT NULL;

UPDATE applecertificate
	INNER JOIN network ON applecertificate.tenantId = network.tenantId
SET network_id = network.id;

ALTER TABLE applecertificate
	ADD KEY FK_ngyy30j2okv2ie58m3id0yib2 (network_id);
ALTER TABLE applecertificate
	ADD CONSTRAINT FK_ngyy30j2okv2ie58m3id0yib2 FOREIGN KEY (network_id) REFERENCES network (id);