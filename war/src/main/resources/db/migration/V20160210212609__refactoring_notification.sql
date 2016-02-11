drop table notification;

-- Moves regIds to MobileDevice
INSERT into mobiledevice(createdAt, tenantId, updatedAt, version, active, deviceCode, lat, lng, type, person_id) SELECT
createdAt, tenantId, updatedAt, version, TRUE, regId, lat, lng, 0, person_id from personnetworkregid;

-- Moves tokens to MobileDevices
INSERT INTO mobiledevice (createdAt, tenantId, updatedAt, version, active, deviceCode, lat, lng, type, person_id) SELECT 
createdAt, tenantId, updatedAt, version, TRUE, token, lat, lng, 1, person_id FROM personnetworktoken;

drop table personnetworkregid;
drop table personnetworktoken;

ALTER TABLE `trix_dev`.`network` 
DROP COLUMN `certificate_password`,
DROP COLUMN `certificate_ios`;
