-- MySQL dump 10.15  Distrib 10.0.22-MariaDB, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: trix_dev
-- ------------------------------------------------------
-- Server version	10.0.22-MariaDB-0+deb8u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `mobiledevice`
--

DROP TABLE IF EXISTS `mobiledevice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mobiledevice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `tenantId` varchar(255) NOT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `active` bit(1) NOT NULL,
  `deviceCode` varchar(255) NOT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ra0b9pf2esqesmc9ba40kfi1b` (`tenantId`,`deviceCode`),
  KEY `FK_bp0e0gt2xa419i44uv4t0lind` (`person_id`),
  CONSTRAINT `FK_bp0e0gt2xa419i44uv4t0lind` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9215 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO mobiledevice (createdAt, tenantId, updatedAt, active, deviceCode, lat, lng, type, person_id) SELECT
 createdAt, tenantId, updatedAt, TRUE, token, lat, lng, 1, person_id FROM personnetworktoken;

 INSERT into mobiledevice(createdAt, tenantId, updatedAt, version, active, deviceCode, lat, lng, type, person_id) SELECT
createdAt, tenantId, updatedAt, version, TRUE, regId, lat, lng, 0, person_id from personnetworkregid;

drop table notification;

drop table personnetworkregid;
drop table personnetworktoken;

ALTER TABLE `trix_dev`.`network` 
DROP COLUMN `certificate_password`,
DROP COLUMN `certificate_ios`;
