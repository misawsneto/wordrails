-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 26, 2011 at 04:34 PM
-- Server version: 5.1.41
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `acl`
--

-- --------------------------------------------------------

--
-- Table structure for table `acl_sid`
--

CREATE TABLE IF NOT EXISTS `acl_sid` (
	`id`        BIGINT(20)   NOT NULL AUTO_INCREMENT,
	`principal` TINYINT(1)   NOT NULL,
	`sid`       VARCHAR(100) NOT NULL,
	`tenantId`  VARCHAR(100) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `unique_uk_1` (`sid`, `principal`, `tenantId`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = latin1
	AUTO_INCREMENT = 4;

-- --------------------------------------------------------

--
-- Table structure for table `acl_class`
--

CREATE TABLE IF NOT EXISTS `acl_class` (
	`id`    BIGINT(20)   NOT NULL AUTO_INCREMENT,
	`class` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `unique_uk_2` (`class`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = latin1
	AUTO_INCREMENT = 4;

-- --------------------------------------------------------

--
-- Table structure for table `acl_entry`
--

CREATE TABLE IF NOT EXISTS `acl_entry` (
	`id`                  BIGINT(20) NOT NULL AUTO_INCREMENT,
	`acl_object_identity` BIGINT(20) NOT NULL,
	`ace_order`           INT(11)    NOT NULL,
	`sid`                 BIGINT(20) NOT NULL,
	`mask`                INT(11)    NOT NULL,
	`granting`            TINYINT(1) NOT NULL,
	`audit_success`       TINYINT(1) NOT NULL,
	`audit_failure`       TINYINT(1) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `unique_uk_4` (`acl_object_identity`, `ace_order`),
	KEY `foreign_fk_5` (`sid`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = latin1
	AUTO_INCREMENT = 43;

-- --------------------------------------------------------

--
-- Table structure for table `acl_object_identity`
--

CREATE TABLE IF NOT EXISTS `acl_object_identity` (
	`id`                 BIGINT(20) NOT NULL AUTO_INCREMENT,
	`object_id_class`    BIGINT(20) NOT NULL,
	`object_id_identity` BIGINT(20) NOT NULL,
	`parent_object`      BIGINT(20)          DEFAULT NULL,
	`owner_sid`          BIGINT(20)          DEFAULT NULL,
	`entries_inheriting` TINYINT(1) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `unique_uk_3` (`object_id_class`, `object_id_identity`),
	KEY `foreign_fk_1` (`parent_object`),
	KEY `foreign_fk_3` (`owner_sid`)
)
	ENGINE = InnoDB
	DEFAULT CHARSET = latin1
	AUTO_INCREMENT = 10;

-- --------------------------------------------------------

--
-- Constraints for dumped tables
--

--
-- Constraints for table `acl_entry`
--
ALTER TABLE `acl_entry`
ADD CONSTRAINT `foreign_fk_4` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`),
ADD CONSTRAINT `foreign_fk_5` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`);

--
-- Constraints for table `acl_object_identity`
--
ALTER TABLE `acl_object_identity`
ADD CONSTRAINT `foreign_fk_1` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`),
ADD CONSTRAINT `foreign_fk_2` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
ADD CONSTRAINT `foreign_fk_3` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`);
