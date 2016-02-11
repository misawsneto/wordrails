drop table notification;

drop table personnetworkregid;
drop table personnetworktoken;

ALTER TABLE `trix_dev`.`network` 
DROP COLUMN `certificate_password`,
DROP COLUMN `certificate_ios`;
