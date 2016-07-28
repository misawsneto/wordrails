CREATE TABLE `authcredential` (
	`id`                     INT(11)      NOT NULL AUTO_INCREMENT,
	`createdAt`              DATETIME              DEFAULT NULL,
	`tenantId`               VARCHAR(255) NOT NULL,
	`updatedAt`              DATETIME              DEFAULT NULL,
	`version`                INT(11)      NOT NULL DEFAULT '0',
	`facebookAppID`          VARCHAR(255)          DEFAULT NULL,
	`facebookAppSecret`      VARCHAR(255)          DEFAULT NULL,
	`googleAppID`     VARCHAR(255)          DEFAULT NULL,
	`googleAppSecret` VARCHAR(255)          DEFAULT NULL,
	`network_id`             INT(11)      NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `UK_ti5rv704qmrnxq8n9angb04aa` (`tenantId`),
	KEY `FK_rrw6xg6sog8t3ev2mbpunalxo` (`network_id`),
	CONSTRAINT `FK_rrw6xg6sog8t3ev2mbpunalxo` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = utf8;


INSERT INTO authcredential (tenantId, facebookAppID, facebookAppSecret, googleAppID, googleAppSecret, network_id)
	SELECT tenantId, facebookAppID, facebookAppSecret, googleAppID, googleAppSecret, id
	FROM
		network;