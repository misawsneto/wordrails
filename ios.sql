ALTER TABLE `wordrails2`.`network` 
ADD COLUMN `certificate_ios` BLOB NULL AFTER `logoSmallId`,
ADD COLUMN `certificate_password` VARCHAR(45) NULL AFTER `certificate_ios`;

