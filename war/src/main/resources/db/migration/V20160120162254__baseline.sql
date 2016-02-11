-- MySQL dump 10.16  Distrib 10.1.10-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: trix_baseline
-- ------------------------------------------------------
-- Server version	10.1.10-MariaDB-log

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
-- Table structure for table `ad`
--

DROP TABLE IF EXISTS `ad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `imageId` int(11) DEFAULT NULL,
  `imageLargeId` int(11) DEFAULT NULL,
  `imageMediumId` int(11) DEFAULT NULL,
  `imageSmallId` int(11) DEFAULT NULL,
  `link` longtext,
  `updatedAt` datetime DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  `sponsor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1ncdkuscykt9jpaa4s9wr3mhw` (`image_id`),
  KEY `FK_ou3ex5c896sdewpt27342a70d` (`sponsor_id`),
  CONSTRAINT `FK_1ncdkuscykt9jpaa4s9wr3mhw` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_ou3ex5c896sdewpt27342a70d` FOREIGN KEY (`sponsor_id`) REFERENCES `sponsor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `androidapp`
--

DROP TABLE IF EXISTS `androidapp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `androidapp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apkUrl` varchar(255) DEFAULT NULL,
  `appName` varchar(255) DEFAULT NULL,
  `fullDescription` longtext,
  `host` varchar(255) DEFAULT NULL,
  `keyAlias` varchar(255) DEFAULT NULL,
  `packageSuffix` varchar(255) DEFAULT NULL,
  `projectName` varchar(255) DEFAULT NULL,
  `shortDescription` longtext,
  `videoUrl` varchar(255) DEFAULT NULL,
  `icon_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pmb5fniwoe5feitwx7msm65yc` (`icon_id`),
  CONSTRAINT `FK_pmb5fniwoe5feitwx7msm65yc` FOREIGN KEY (`icon_id`) REFERENCES `file` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) NOT NULL,
  `network_id` int(11) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_f2dja5uh2fyeo5s7l0t7s7ph3` (`network_id`),
  KEY `FK_cia0ypr3lttjoogbv99964fk7` (`station_id`),
  KEY `FK_s21btj9mlob1djhm3amivbe5e` (`user_id`),
  CONSTRAINT `FK_cia0ypr3lttjoogbv99964fk7` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_f2dja5uh2fyeo5s7l0t7s7ph3` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`),
  CONSTRAINT `FK_s21btj9mlob1djhm3amivbe5e` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=830 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `basesection`
--

DROP TABLE IF EXISTS `basesection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `basesection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `list_index` int(11) NOT NULL,
  `layout` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `page_id` int(11) DEFAULT NULL,
  `sections_KEY` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_923dxw7uhuir61nwlyri7op71` (`page_id`),
  CONSTRAINT `FK_923dxw7uhuir61nwlyri7op71` FOREIGN KEY (`page_id`) REFERENCES `page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bookmark`
--

DROP TABLE IF EXISTS `bookmark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bookmark` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `person_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_tbgtutgbyrs3pksgf8xqbtvhr` (`post_id`,`person_id`),
  KEY `FK_bw5bdv8tr8kxg0c19hh27j8fr` (`person_id`),
  CONSTRAINT `FK_bw5bdv8tr8kxg0c19hh27j8fr` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_ixpa8550g1sidixpff8dml0ma` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cell`
--

DROP TABLE IF EXISTS `cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cell` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `index` int(11) DEFAULT NULL,
  `post_id` int(11) NOT NULL,
  `row_id` int(11) NOT NULL,
  `term_id` int(11) DEFAULT NULL,
  `featured` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_iw1i4bkjjn6svh38aixlr19wu` (`post_id`),
  KEY `FK_is4dvu82po4bs8so5toude0mb` (`row_id`),
  KEY `FK_11y69xw2xd9u2aqwf8yhpf12x` (`term_id`),
  CONSTRAINT `FK_11y69xw2xd9u2aqwf8yhpf12x` FOREIGN KEY (`term_id`) REFERENCES `term` (`id`),
  CONSTRAINT `FK_is4dvu82po4bs8so5toude0mb` FOREIGN KEY (`row_id`) REFERENCES `row` (`id`),
  CONSTRAINT `FK_iw1i4bkjjn6svh38aixlr19wu` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=628 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `body` longtext NOT NULL,
  `date` datetime NOT NULL,
  `lastModificationDate` datetime DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `author_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `station` tinyblob,
  `station_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_j94pith5sd971k29j6ysxuk7` (`author_id`),
  KEY `FK_apirq8ka64iidc18f3k6x5tc5` (`post_id`),
  KEY `FK_itj1xiek93it8u1k5rav4air8` (`station_id`),
  CONSTRAINT `FK_apirq8ka64iidc18f3k6x5tc5` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK_itj1xiek93it8u1k5rav4air8` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_j94pith5sd971k29j6ysxuk7` FOREIGN KEY (`author_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `containersection`
--

DROP TABLE IF EXISTS `containersection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `containersection` (
  `bottomMargin` int(11) DEFAULT NULL,
  `bottomPadding` int(11) DEFAULT NULL,
  `leftMargin` int(11) DEFAULT NULL,
  `leftPadding` int(11) DEFAULT NULL,
  `orientation` varchar(255) DEFAULT NULL,
  `pctSize` int(11) DEFAULT NULL,
  `rightMargin` int(11) DEFAULT NULL,
  `rightPadding` int(11) DEFAULT NULL,
  `topMargin` int(11) DEFAULT NULL,
  `topPadding` int(11) DEFAULT NULL,
  `section_id` int(11) NOT NULL,
  `parent_section_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`section_id`),
  KEY `FK_1t1qjv0prj5xr3y6alqj7t099` (`parent_section_id`),
  CONSTRAINT `FK_1t1qjv0prj5xr3y6alqj7t099` FOREIGN KEY (`parent_section_id`) REFERENCES `containersection` (`section_id`),
  CONSTRAINT `FK_bripx6ljmab3rocu2pagy3v7` FOREIGN KEY (`section_id`) REFERENCES `basesection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `containersection_section`
--

DROP TABLE IF EXISTS `containersection_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `containersection_section` (
  `containersection_id` int(11) NOT NULL,
  `children_id` int(11) NOT NULL,
  `children_KEY` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`containersection_id`,`children_KEY`),
  UNIQUE KEY `UK_tkwg69470nrk96uwhx6yff2m9` (`children_id`),
  CONSTRAINT `FK_p0r2d765tajhecg0jjrbxn14j` FOREIGN KEY (`containersection_id`) REFERENCES `containersection` (`section_id`),
  CONSTRAINT `FK_tkwg69470nrk96uwhx6yff2m9` FOREIGN KEY (`children_id`) REFERENCES `basesection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `es_sorts`
--

DROP TABLE IF EXISTS `es_sorts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `es_sorts` (
  `query_id` int(11) NOT NULL,
  `sort_string` varchar(255) NOT NULL,
  KEY `FK_m33jnn5l5b4k15aqn9g09r7g6` (`query_id`),
  CONSTRAINT `FK_m33jnn5l5b4k15aqn9g09r7g6` FOREIGN KEY (`query_id`) REFERENCES `esquery` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `esquery`
--

DROP TABLE IF EXISTS `esquery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `esquery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `boolQueryString` longtext NOT NULL,
  `highlightedField` varchar(255) DEFAULT NULL,
  `objectName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `person_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mcfp3fhcr6mot3m4k5o32u833` (`post_id`,`person_id`),
  KEY `FK_aljh4rtshkr53wrrqrtagv83x` (`person_id`),
  CONSTRAINT `FK_aljh4rtshkr53wrrqrtagv83x` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_otylyrni8m49rr4o72tqueg9k` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mime` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(1) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `contents` longblob,
  `hash` varchar(255) DEFAULT NULL,
  `networkId` int(11) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `directory` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19719 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fixedquery`
--

DROP TABLE IF EXISTS `fixedquery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fixedquery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `query_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8pngebwntfq4ak3xwm8qgu0vp` (`query_id`),
  CONSTRAINT `FK_8pngebwntfq4ak3xwm8qgu0vp` FOREIGN KEY (`query_id`) REFERENCES `esquery` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fixedquery_indexes`
--

DROP TABLE IF EXISTS `fixedquery_indexes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fixedquery_indexes` (
  `FixedQuery_id` int(11) NOT NULL,
  `indexes` int(11) DEFAULT NULL,
  KEY `FK_5m2x350xt8mr4k99r56nevlip` (`FixedQuery_id`),
  CONSTRAINT `FK_5m2x350xt8mr4k99r56nevlip` FOREIGN KEY (`FixedQuery_id`) REFERENCES `fixedquery` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `globalparameter`
--

DROP TABLE IF EXISTS `globalparameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `globalparameter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6pkqorw838kjaji1d0nj5amak` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `comment_id` int(11) DEFAULT NULL,
  `large_id` int(11) NOT NULL,
  `medium_id` int(11) NOT NULL,
  `original_id` int(11) NOT NULL,
  `post_id` int(11) DEFAULT NULL,
  `small_id` int(11) NOT NULL,
  `caption` longtext,
  `publicitySponsor_id` int(11) DEFAULT NULL,
  `credits` longtext,
  `vertical` tinyint(1) NOT NULL DEFAULT '0',
  `commentId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `type` varchar(255) NOT NULL DEFAULT 'POST',
  `owner_id` int(11) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `largeHash` varchar(255) DEFAULT NULL,
  `mediumHash` varchar(255) DEFAULT NULL,
  `originalHash` varchar(255) DEFAULT NULL,
  `smallHash` varchar(255) DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_nmc0als7j00iqcxqk3mfkytwm` (`comment_id`),
  KEY `FK_aedika0r5f8e3v0o7mxp9mbjj` (`large_id`),
  KEY `FK_jlmrknjsp36q8dsgmedn7dvc6` (`medium_id`),
  KEY `FK_47t3yd2jjthshd9fo7akkstbx` (`original_id`),
  KEY `FK_mu8q4xsie1vbye2owoxrcfgjd` (`post_id`),
  KEY `FK_t2gabdofdotdaxia4yamfwve5` (`small_id`),
  KEY `FK_j6iqwyknjxxm2csavikoa16jv` (`publicitySponsor_id`),
  KEY `FK_5bllh5f1scyu52r3b3aaypgvd` (`owner_id`),
  KEY `FK_i3je0n1m49h1933m7tehqevnk` (`station_id`),
  CONSTRAINT `FK_47t3yd2jjthshd9fo7akkstbx` FOREIGN KEY (`original_id`) REFERENCES `file` (`id`),
  CONSTRAINT `FK_5bllh5f1scyu52r3b3aaypgvd` FOREIGN KEY (`owner_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_aedika0r5f8e3v0o7mxp9mbjj` FOREIGN KEY (`large_id`) REFERENCES `file` (`id`),
  CONSTRAINT `FK_i3je0n1m49h1933m7tehqevnk` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_j6iqwyknjxxm2csavikoa16jv` FOREIGN KEY (`publicitySponsor_id`) REFERENCES `sponsor` (`id`),
  CONSTRAINT `FK_jlmrknjsp36q8dsgmedn7dvc6` FOREIGN KEY (`medium_id`) REFERENCES `file` (`id`),
  CONSTRAINT `FK_mu8q4xsie1vbye2owoxrcfgjd` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK_nmc0als7j00iqcxqk3mfkytwm` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),
  CONSTRAINT `FK_t2gabdofdotdaxia4yamfwve5` FOREIGN KEY (`small_id`) REFERENCES `file` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5124 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image_hash`
--

DROP TABLE IF EXISTS `image_hash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_hash` (
  `image_id` int(11) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `sizeTag` varchar(255) NOT NULL,
  PRIMARY KEY (`image_id`,`sizeTag`),
  CONSTRAINT `FK_t7b7no63h2psnfs8vgaumqhvj` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image_picture`
--

DROP TABLE IF EXISTS `image_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_picture` (
  `image_id` int(11) NOT NULL,
  `pictures_id` int(11) NOT NULL,
  PRIMARY KEY (`image_id`,`pictures_id`),
  KEY `FK_3bgg9jglshvo32bx47ot026gp` (`pictures_id`),
  CONSTRAINT `FK_3bgg9jglshvo32bx47ot026gp` FOREIGN KEY (`pictures_id`) REFERENCES `picture` (`id`),
  CONSTRAINT `FK_ov14se662da31buhn3mju2m7w` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invitation`
--

DROP TABLE IF EXISTS `invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `hash` varchar(255) NOT NULL,
  `personName` varchar(255) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `invitationUrl` varchar(255) DEFAULT NULL,
  `network_id` int(11) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t1grhywialrpo1o2eeua8u5np` (`hash`),
  UNIQUE KEY `UK_af2ull33jttfseagu5rjyq19j` (`hash`,`network_id`),
  KEY `FK_4cjr2qy6517qyk2q5c8qs0kr3` (`network_id`),
  KEY `FK_8ofyq9rip50n6waobrppunfvc` (`station_id`),
  CONSTRAINT `FK_4cjr2qy6517qyk2q5c8qs0kr3` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`),
  CONSTRAINT `FK_8ofyq9rip50n6waobrppunfvc` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invitation_station`
--

DROP TABLE IF EXISTS `invitation_station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitation_station` (
  `Invitation_id` int(11) NOT NULL,
  `stations_id` int(11) NOT NULL,
  PRIMARY KEY (`Invitation_id`,`stations_id`),
  UNIQUE KEY `UK_hq41gdg76kqh6wuldfdcmcu1t` (`stations_id`),
  CONSTRAINT `FK_e91pk5b6pkpvfke5hfs05pm7t` FOREIGN KEY (`Invitation_id`) REFERENCES `invitation` (`id`),
  CONSTRAINT `FK_hq41gdg76kqh6wuldfdcmcu1t` FOREIGN KEY (`stations_id`) REFERENCES `station` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `network`
--

DROP TABLE IF EXISTS `network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `network` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `trackingId` varchar(100) DEFAULT NULL,
  `defaultTaxonomy_id` int(11) DEFAULT NULL,
  `allowSignup` bit(1) NOT NULL,
  `configured` bit(1) NOT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `subdomain` varchar(255) NOT NULL,
  `logo_id` int(11) DEFAULT NULL,
  `backgroundColor` varchar(255) DEFAULT NULL,
  `font` varchar(255) DEFAULT NULL,
  `headerFontSize` varchar(255) DEFAULT NULL,
  `navbarColor` varchar(255) DEFAULT NULL,
  `primaryColor` varchar(255) DEFAULT NULL,
  `textFontSize` varchar(255) DEFAULT NULL,
  `navbarSecondaryColor` varchar(255) DEFAULT NULL,
  `newsFontSize` decimal(10,2) DEFAULT '1.00',
  `primaryFont` varchar(255) DEFAULT NULL,
  `secondaryFont` varchar(255) DEFAULT NULL,
  `titleFontSize` decimal(10,2) DEFAULT '4.00',
  `mainColor` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `defaultOrientationMode` varchar(255) DEFAULT NULL,
  `defaultReadMode` varchar(255) DEFAULT NULL,
  `wordpressDomain` varchar(255) DEFAULT NULL,
  `wordpressPassword` varchar(255) DEFAULT NULL,
  `wordpressToken` varchar(255) DEFAULT NULL,
  `wordpressUsername` varchar(255) DEFAULT NULL,
  `logoId` int(11) DEFAULT NULL,
  `allowSocialLogin` tinyint(1) NOT NULL DEFAULT '0',
  `allowSponsors` tinyint(1) NOT NULL DEFAULT '0',
  `faviconId` int(11) DEFAULT NULL,
  `flurryKey` varchar(100) DEFAULT NULL,
  `loginFooterMessage` longtext,
  `loginImageId` int(11) DEFAULT NULL,
  `splashImageId` int(11) DEFAULT NULL,
  `favicon_id` int(11) DEFAULT NULL,
  `loginImage_id` int(11) DEFAULT NULL,
  `splashImage_id` int(11) DEFAULT NULL,
  `loginImageSmallId` int(11) DEFAULT NULL,
  `logoSmallId` int(11) DEFAULT NULL,
  `certificate_ios` longblob,
  `certificate_password` varchar(255) DEFAULT NULL,
  `networkCreationToken` varchar(255) DEFAULT NULL,
  `info` longtext,
  `appleStoreAddress` longtext,
  `playStoreAddress` longtext,
  `faviconHash` varchar(255) DEFAULT NULL,
  `loginImageHash` varchar(255) DEFAULT NULL,
  `loginImageSmallHash` varchar(255) DEFAULT NULL,
  `logoHash` varchar(255) DEFAULT NULL,
  `logoSmallHash` varchar(255) DEFAULT NULL,
  `splashImageHash` varchar(255) DEFAULT NULL,
  `flurryAppleKey` varchar(100) DEFAULT NULL,
  `categoriesTaxonomyId` int(11) DEFAULT NULL,
  `facebookAppID` varchar(255) DEFAULT NULL,
  `facebookAppSecret` varchar(255) DEFAULT NULL,
  `googleAppID` varchar(255) DEFAULT NULL,
  `googleAppSecret` varchar(255) DEFAULT NULL,
  `androidApp_id` int(11) DEFAULT NULL,
  `facebookLink` varchar(255) DEFAULT NULL,
  `googlePlusLink` varchar(255) DEFAULT NULL,
  `homeTabName` varchar(255) DEFAULT NULL,
  `stationMenuName` varchar(255) DEFAULT NULL,
  `twitterLink` varchar(255) DEFAULT NULL,
  `webFooter` varchar(255) DEFAULT NULL,
  `youtubeLink` varchar(255) DEFAULT NULL,
  `invitationMessage` longtext,
  `addStationRolesOnSignup` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_q2ej8jnlaxoxc8fqhxv032wf8` (`subdomain`),
  UNIQUE KEY `UK_iqexgsjiwolldto8tc21lghxb` (`logo_id`),
  UNIQUE KEY `UK_nflo9f6ndf7e7s0ohaguxv13y` (`favicon_id`),
  KEY `FK_iqexgsjiwolldto8tc21lghxb` (`logo_id`),
  KEY `FK_nflo9f6ndf7e7s0ohaguxv13y` (`favicon_id`),
  KEY `FK_fm9gfvjplrafwmghykepqfgqv` (`loginImage_id`),
  KEY `FK_hnqub15hlw0ijocvaix7s8oso` (`splashImage_id`),
  KEY `FK_avqdjm6prjise18s7y9yaaxf1` (`androidApp_id`),
  CONSTRAINT `FK_avqdjm6prjise18s7y9yaaxf1` FOREIGN KEY (`androidApp_id`) REFERENCES `androidapp` (`id`),
  CONSTRAINT `FK_fm9gfvjplrafwmghykepqfgqv` FOREIGN KEY (`loginImage_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_hnqub15hlw0ijocvaix7s8oso` FOREIGN KEY (`splashImage_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_iqexgsjiwolldto8tc21lghxb` FOREIGN KEY (`logo_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_nflo9f6ndf7e7s0ohaguxv13y` FOREIGN KEY (`favicon_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `network_taxonomy`
--

DROP TABLE IF EXISTS `network_taxonomy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `network_taxonomy` (
  `networks_id` int(11) NOT NULL,
  `taxonomies_id` int(11) NOT NULL,
  PRIMARY KEY (`networks_id`,`taxonomies_id`),
  KEY `FK_e9ftjnrheum3b48218fcjwin8` (`taxonomies_id`),
  CONSTRAINT `FK_e9ftjnrheum3b48218fcjwin8` FOREIGN KEY (`taxonomies_id`) REFERENCES `taxonomy` (`id`),
  CONSTRAINT `FK_r8r672dbgtxq6ip7l7iyvcrqg` FOREIGN KEY (`networks_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `message` varchar(500) NOT NULL,
  `seen` bit(1) NOT NULL,
  `type` varchar(255) NOT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `network_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  `post_id` int(11) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `hash` varchar(255) NOT NULL,
  `test` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_nmokwheuccj9by7da66vltm73` (`network_id`),
  KEY `FK_7pjvouftqrl1thla69piaaaxf` (`person_id`),
  KEY `FK_7yfwvid0troi27k14ngffe6ql` (`post_id`),
  KEY `FK_bcpqdxavufrrspek0wxrsgb60` (`station_id`),
  CONSTRAINT `FK_7pjvouftqrl1thla69piaaaxf` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_7yfwvid0troi27k14ngffe6ql` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK_bcpqdxavufrrspek0wxrsgb60` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_nmokwheuccj9by7da66vltm73` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3403211 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `page`
--

DROP TABLE IF EXISTS `page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5kyn0p2xj8q3wkdrpnuuivwjo` (`station_id`),
  CONSTRAINT `FK_5kyn0p2xj8q3wkdrpnuuivwjo` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pageablequery`
--

DROP TABLE IF EXISTS `pageablequery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pageablequery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `query_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_e8diktkhax7pgs9m3f62tx495` (`query_id`),
  CONSTRAINT `FK_e8diktkhax7pgs9m3f62tx495` FOREIGN KEY (`query_id`) REFERENCES `esquery` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `passwordreset`
--

DROP TABLE IF EXISTS `passwordreset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `passwordreset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `networkSubdomain` varchar(255) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `invite` bit(1) NOT NULL,
  `networkName` varchar(255) DEFAULT NULL,
  `personName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mfrv75snxtise541rv943otm2` (`hash`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bio` varchar(2048) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `passwordReseted` bit(1) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `image_id` int(11) DEFAULT NULL,
  `twitterHandle` varchar(255) DEFAULT NULL,
  `cover_id` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `imageId` int(11) DEFAULT NULL,
  `imageLargeId` int(11) DEFAULT NULL,
  `imageMediumId` int(11) DEFAULT NULL,
  `imageSmallId` int(11) DEFAULT NULL,
  `coverId` int(11) DEFAULT NULL,
  `coverLargeId` int(11) DEFAULT NULL,
  `wordpressId` int(11) DEFAULT NULL,
  `coverMediumId` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `lastLogin` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT NULL,
  `coverHash` varchar(255) DEFAULT NULL,
  `coverLargeHash` varchar(255) DEFAULT NULL,
  `coverMediumHash` varchar(255) DEFAULT NULL,
  `imageHash` varchar(255) DEFAULT NULL,
  `imageLargeHash` varchar(255) DEFAULT NULL,
  `imageMediumHash` varchar(255) DEFAULT NULL,
  `imageSmallHash` varchar(255) DEFAULT NULL,
  `coverUrl` varchar(255) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_p1ih97l44l6ddssidk989ep8q` (`username`,`networkId`),
  UNIQUE KEY `UK_t1smi3rfq846y5teyf1gcv8ir` (`user_id`,`username`),
  KEY `FK_pfar63s78dw410jn5ub6b7105` (`image_id`),
  KEY `FK_rng9j4lteh8qu750nr38eko2r` (`cover_id`),
  CONSTRAINT `FK_pfar63s78dw410jn5ub6b7105` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_rng9j4lteh8qu750nr38eko2r` FOREIGN KEY (`cover_id`) REFERENCES `image` (`id`),
  CONSTRAINT `person_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=538 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person_network_role`
--

DROP TABLE IF EXISTS `person_network_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_network_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin` bit(1) NOT NULL,
  `network_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ck690vwdp06tj4n58o9cqad8b` (`person_id`,`network_id`),
  KEY `FK_cwx6ha0jh5sm4n0sg6bax4ce3` (`network_id`),
  CONSTRAINT `FK_42hinxrhsp91vpbmuktpfsujj` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_cwx6ha0jh5sm4n0sg6bax4ce3` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=480 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person_person`
--

DROP TABLE IF EXISTS `person_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_person` (
  `Person_id` int(11) NOT NULL,
  `following_id` int(11) NOT NULL,
  PRIMARY KEY (`Person_id`,`following_id`),
  UNIQUE KEY `UK_oc4oltj7rd0ck68r4qtrmj5c0` (`following_id`),
  UNIQUE KEY `UK_ilysd182v26hqlwrbipy2r1` (`following_id`),
  CONSTRAINT `FK_inq4xm3i524cmia3rp65fx39w` FOREIGN KEY (`Person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_oc4oltj7rd0ck68r4qtrmj5c0` FOREIGN KEY (`following_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person_station_role`
--

DROP TABLE IF EXISTS `person_station_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_station_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin` bit(1) NOT NULL,
  `editor` bit(1) NOT NULL,
  `writer` bit(1) NOT NULL,
  `person_id` int(11) NOT NULL,
  `station_id` int(11) NOT NULL,
  `wordpress_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_17d5wlk08uhoe9seed4f9dmna` (`person_id`,`station_id`),
  KEY `FK_nwbsv3ubow5yo53viwx5cmf8u` (`station_id`),
  KEY `FK_b5bn0626atkb37wk1fblq03pd` (`wordpress_id`),
  CONSTRAINT `FK_b5bn0626atkb37wk1fblq03pd` FOREIGN KEY (`wordpress_id`) REFERENCES `wordpress` (`id`),
  CONSTRAINT `FK_nwbsv3ubow5yo53viwx5cmf8u` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_r1vfys238vojtx92wr33lg1hn` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=826 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personnetworkregid`
--

DROP TABLE IF EXISTS `personnetworkregid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personnetworkregid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `regId` varchar(255) DEFAULT NULL,
  `network_id` int(11) NOT NULL,
  `person_id` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5gxr3ln7jp4avfvl1sn14u8ei` (`person_id`,`network_id`,`regId`),
  UNIQUE KEY `UK_66ucw8s78581mapmajvvcuy4h` (`network_id`,`regId`),
  CONSTRAINT `FK_15ku2e704vuq11eilqsh4x6ya` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`),
  CONSTRAINT `FK_ccl1tsc49s5hf4ce0ilx1169b` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24150 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- 
-- Table structure for table `personnetworktoken`
-- 

DROP TABLE IF EXISTS `personnetworktoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personnetworktoken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `network_id` int(11) NOT NULL,
  `person_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhaydrew3p73y5oxxe9xvu2f3` (`network_id`,`token`),
  KEY `FK_mpk030j2e4p5dyjtw2mj5a81n` (`person_id`),
  CONSTRAINT `FK_1gttfb6y3fmlg1j2nhvftqfbu` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`),
  CONSTRAINT `FK_mpk030j2e4p5dyjtw2mj5a81n` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1192 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `height` int(11) DEFAULT NULL,
  `sizeTag` varchar(255) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `file_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_nyaueynxfn5mabp6kopbsi8bt` (`file_id`),
  CONSTRAINT `FK_nyaueynxfn5mabp6kopbsi8bt` FOREIGN KEY (`file_id`) REFERENCES `file` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=877 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `body` longtext,
  `date` datetime NOT NULL,
  `lastModificationDate` datetime DEFAULT NULL,
  `sponsor` tinyint(1) NOT NULL DEFAULT '0',
  `title` longtext,
  `author_id` int(11) NOT NULL,
  `featuredImage_id` int(11) DEFAULT NULL,
  `station_id` int(11) NOT NULL,
  `topper` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `originalSlug` longtext CHARACTER SET latin1,
  `slug` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `state` varchar(15) CHARACTER SET latin1 DEFAULT NULL,
  `sponsor_id` int(11) DEFAULT NULL,
  `wordpressId` int(11) DEFAULT NULL,
  `imageId` int(11) DEFAULT NULL,
  `imageLandscape` bit(1) NOT NULL,
  `imageLargeId` int(11) DEFAULT NULL,
  `imageMediumId` int(11) DEFAULT NULL,
  `imageSmallId` int(11) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `readsCount` int(11) NOT NULL,
  `recommendsCount` int(11) NOT NULL,
  `commentsCount` int(11) NOT NULL,
  `externalFeaturedImgUrl` varchar(1024) CHARACTER SET latin1 DEFAULT NULL,
  `externalVideoUrl` varchar(1024) CHARACTER SET latin1 DEFAULT NULL,
  `imageCaptionText` longtext CHARACTER SET latin1,
  `imageCreditsText` longtext CHARACTER SET latin1,
  `bookmarksCount` int(11) DEFAULT NULL,
  `imageTitleText` longtext CHARACTER SET latin1,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `notify` tinyint(1) DEFAULT '0',
  `originalPostId` int(11) DEFAULT NULL,
  `readTime` int(11) DEFAULT '0',
  `scheduledDate` datetime DEFAULT NULL,
  `stationId` int(11) DEFAULT NULL,
  `subheading` longtext,
  `createdAt` datetime DEFAULT NULL,
  `post_id` int(11) DEFAULT NULL,
  `imageHash` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `imageLargeHash` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `imageMediumHash` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `imageSmallHash` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `featuredAudioHash` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `featuredVideoHash` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `network` tinyblob,
  `network_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hcinco2ixvx2ksj63rvsf5o11` (`slug`),
  KEY `FK_m7j5gwmpa7dklv5bnc41ertmi` (`author_id`),
  KEY `FK_eolhsgjjrm4w3rxg9q84vebfn` (`featuredImage_id`),
  KEY `FK_88khc3cifjc6rf9tfhs6rb6w5` (`station_id`),
  KEY `FK_2lio2nnyxe8b458ovcwroy6bh` (`sponsor_id`),
  KEY `FK_gwcb138yhbt9s8g7tvpogj2nr` (`post_id`),
  KEY `FK_opm6t21mbtl0bp2v6n94gymlk` (`network_id`),
  CONSTRAINT `FK_2lio2nnyxe8b458ovcwroy6bh` FOREIGN KEY (`sponsor_id`) REFERENCES `sponsor` (`id`),
  CONSTRAINT `FK_88khc3cifjc6rf9tfhs6rb6w5` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_eolhsgjjrm4w3rxg9q84vebfn` FOREIGN KEY (`featuredImage_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_gwcb138yhbt9s8g7tvpogj2nr` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK_m7j5gwmpa7dklv5bnc41ertmi` FOREIGN KEY (`author_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_opm6t21mbtl0bp2v6n94gymlk` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6054 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post_image`
--

DROP TABLE IF EXISTS `post_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_image` (
  `post_id` int(11) NOT NULL,
  `images_id` int(11) NOT NULL,
  PRIMARY KEY (`post_id`,`images_id`),
  UNIQUE KEY `UK_4ic6tenmrv0ri94vq2vh95qnm` (`images_id`),
  CONSTRAINT `FK_4ic6tenmrv0ri94vq2vh95qnm` FOREIGN KEY (`images_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_af6whs3lmpc7q6p1xnyv9kpux` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post_tags`
--

DROP TABLE IF EXISTS `post_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_tags` (
  `post_id` int(11) NOT NULL,
  `tags` varchar(255) DEFAULT NULL,
  KEY `FK_rf0kr7eqk5xoalmc4gigdwg3p` (`post_id`),
  CONSTRAINT `FK_rf0kr7eqk5xoalmc4gigdwg3p` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post_term`
--

DROP TABLE IF EXISTS `post_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_term` (
  `posts_id` int(11) NOT NULL,
  `terms_id` int(11) NOT NULL,
  PRIMARY KEY (`posts_id`,`terms_id`),
  KEY `FK_o8ft1b1sg0mu1neetvuv6tnb3` (`terms_id`),
  CONSTRAINT `FK_kvww9yqidq1h5tg2pglp1ishl` FOREIGN KEY (`posts_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK_o8ft1b1sg0mu1neetvuv6tnb3` FOREIGN KEY (`terms_id`) REFERENCES `term` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post_video`
--

DROP TABLE IF EXISTS `post_video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_video` (
  `post_id` int(11) NOT NULL,
  `video_id` int(11) NOT NULL,
  PRIMARY KEY (`post_id`,`video_id`),
  UNIQUE KEY `UK_o588a7mtddp0c7gism63fk8g4` (`video_id`),
  CONSTRAINT `FK_o588a7mtddp0c7gism63fk8g4` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`),
  CONSTRAINT `FK_omwwlfuhxyygsnngm7xg5t9vc` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `postread`
--

DROP TABLE IF EXISTS `postread`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `postread` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  `post_id` int(11) NOT NULL,
  `sessionid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bpyauc4sjdo8h98yq5xq1p9f5` (`post_id`,`person_id`,`sessionid`),
  KEY `FK_oqdhk6opp8yj6eu66vjb9yddi` (`person_id`),
  KEY `FK_c6u4kxrn9a49wa5ovqrlsxig1` (`post_id`),
  CONSTRAINT `FK_c6u4kxrn9a49wa5ovqrlsxig1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK_oqdhk6opp8yj6eu66vjb9yddi` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2404451 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promotion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(500) DEFAULT NULL,
  `date` datetime NOT NULL,
  `post_id` int(11) NOT NULL,
  `promoter_id` int(11) NOT NULL,
  `station_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6ua7xlc7lsi6q68r80xrweoqt` (`post_id`),
  KEY `FK_nm9l6wb55q7swfbkpbdv7ah9q` (`promoter_id`),
  KEY `FK_9w5qt7x07vry6jrueokn84klj` (`station_id`),
  CONSTRAINT `FK_6ua7xlc7lsi6q68r80xrweoqt` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `FK_9w5qt7x07vry6jrueokn84klj` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_nm9l6wb55q7swfbkpbdv7ah9q` FOREIGN KEY (`promoter_id`) REFERENCES `person` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_simprop_triggers`
--

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `queryablelistsection`
--

DROP TABLE IF EXISTS `queryablelistsection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `queryablelistsection` (
  `isPageable` bit(1) NOT NULL,
  `size` int(11) NOT NULL,
  `section_id` int(11) NOT NULL,
  `pageableQuery_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`section_id`),
  KEY `FK_7w7484idpfefpog1gtnjw9pwi` (`pageableQuery_id`),
  CONSTRAINT `FK_7w7484idpfefpog1gtnjw9pwi` FOREIGN KEY (`pageableQuery_id`) REFERENCES `pageablequery` (`id`),
  CONSTRAINT `FK_odxmjrog6lmfnljem8o48nlgi` FOREIGN KEY (`section_id`) REFERENCES `basesection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recommend`
--

DROP TABLE IF EXISTS `recommend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recommend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `person_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pa9cfr4datnqm28xcq3qsvsat` (`post_id`,`person_id`),
  KEY `FK_id02m4t85rhq1pf4amh2xwhso` (`person_id`),
  CONSTRAINT `FK_id02m4t85rhq1pf4amh2xwhso` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `FK_kjj0bfpec1biurq392wlufci` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `row`
--

DROP TABLE IF EXISTS `row`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `row` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `index` int(11) DEFAULT NULL,
  `type` varchar(1) NOT NULL,
  `featuring_perspective` int(11) DEFAULT NULL,
  `perspective_id` int(11) DEFAULT NULL,
  `splashed_perspective` int(11) DEFAULT NULL,
  `term_id` int(11) DEFAULT NULL,
  `maxPosts` int(11) NOT NULL DEFAULT '0',
  `home_perspective` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_i4ueeqofda0v1scrxhhc9mbkh` (`featuring_perspective`),
  KEY `FK_rx0b8upti0n04ibvoiwdlfjp4` (`perspective_id`),
  KEY `FK_g3imfmslim4ig85yf2bm1msxd` (`splashed_perspective`),
  KEY `FK_hcy7t9pomm1xuwylggyr3l49y` (`term_id`),
  KEY `FK_obm4fb9hsf04g1tplifa14bxt` (`home_perspective`),
  CONSTRAINT `FK_g3imfmslim4ig85yf2bm1msxd` FOREIGN KEY (`splashed_perspective`) REFERENCES `term_perspective` (`id`),
  CONSTRAINT `FK_hcy7t9pomm1xuwylggyr3l49y` FOREIGN KEY (`term_id`) REFERENCES `term` (`id`),
  CONSTRAINT `FK_i4ueeqofda0v1scrxhhc9mbkh` FOREIGN KEY (`featuring_perspective`) REFERENCES `term_perspective` (`id`),
  CONSTRAINT `FK_obm4fb9hsf04g1tplifa14bxt` FOREIGN KEY (`home_perspective`) REFERENCES `term_perspective` (`id`),
  CONSTRAINT `FK_rx0b8upti0n04ibvoiwdlfjp4` FOREIGN KEY (`perspective_id`) REFERENCES `term_perspective` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=997 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `name` varchar(255) DEFAULT NULL,
  `network_id` int(11) DEFAULT NULL,
  `anonymousUrl` longtext,
  `loggedInUrl` longtext,
  PRIMARY KEY (`id`),
  KEY `FK_2i6fe6e7fqwptdaxuxp54xaor` (`network_id`),
  CONSTRAINT `FK_2i6fe6e7fqwptdaxuxp54xaor` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section_fixedquery`
--

DROP TABLE IF EXISTS `section_fixedquery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section_fixedquery` (
  `section_id` int(11) NOT NULL,
  `query_id` int(11) NOT NULL,
  UNIQUE KEY `UK_4bftxwn59pm2doxa5p329y9iv` (`query_id`),
  KEY `FK_owgcq19haxhsu11a63xyyh4ge` (`section_id`),
  CONSTRAINT `FK_4bftxwn59pm2doxa5p329y9iv` FOREIGN KEY (`query_id`) REFERENCES `fixedquery` (`id`),
  CONSTRAINT `FK_owgcq19haxhsu11a63xyyh4ge` FOREIGN KEY (`section_id`) REFERENCES `queryablelistsection` (`section_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sponsor`
--

DROP TABLE IF EXISTS `sponsor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keywords` varchar(1000) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `logo_id` int(11) DEFAULT NULL,
  `network_id` int(11) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `link` longtext,
  `logoId` int(11) DEFAULT NULL,
  `logoLargeId` int(11) DEFAULT NULL,
  `logoMediumId` int(11) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6tkf8d1t82t633g4y98k85o9j` (`logo_id`),
  KEY `FK_1xkk0ya5j0288ys2934vwtttd` (`network_id`),
  CONSTRAINT `FK_1xkk0ya5j0288ys2934vwtttd` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`),
  CONSTRAINT `FK_6tkf8d1t82t633g4y98k85o9j` FOREIGN KEY (`logo_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sponsor_ad`
--

DROP TABLE IF EXISTS `sponsor_ad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsor_ad` (
  `Sponsor_id` int(11) NOT NULL,
  `ads_id` int(11) NOT NULL,
  PRIMARY KEY (`Sponsor_id`,`ads_id`),
  UNIQUE KEY `UK_sj7arixeae8w9jbxxk01poipw` (`ads_id`),
  CONSTRAINT `FK_16mekf0g3idaeehij0buwk33o` FOREIGN KEY (`Sponsor_id`) REFERENCES `sponsor` (`id`),
  CONSTRAINT `FK_sj7arixeae8w9jbxxk01poipw` FOREIGN KEY (`ads_id`) REFERENCES `ad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sponsorship`
--

DROP TABLE IF EXISTS `sponsorship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsorship` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `details` varchar(1000) DEFAULT NULL,
  `sponsor` varchar(200) DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_eiib6wo0beunraav2ocibpi1d` (`image_id`),
  CONSTRAINT `FK_eiib6wo0beunraav2ocibpi1d` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `main` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `visibility` varchar(255) NOT NULL,
  `writable` bit(1) NOT NULL,
  `postsTitleSize` int(11) NOT NULL,
  `sponsored` bit(1) NOT NULL,
  `topper` bit(1) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `logoId` int(11) DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `logo_id` int(11) DEFAULT NULL,
  `wordpress_id` int(11) DEFAULT NULL,
  `defaultPerspectiveId` int(11) DEFAULT NULL,
  `allowComments` tinyint(1) NOT NULL DEFAULT '0',
  `allowSignup` tinyint(1) NOT NULL DEFAULT '0',
  `allowSocialLogin` tinyint(1) NOT NULL DEFAULT '0',
  `allowSocialShare` tinyint(1) NOT NULL DEFAULT '0',
  `backgroundColor` varchar(255) DEFAULT '#ffffff',
  `navbarColor` varchar(255) DEFAULT '#ffffff',
  `primaryColor` varchar(255) DEFAULT '#5C78B0',
  `allowWritersToAddSponsors` tinyint(1) NOT NULL DEFAULT '0',
  `allowWritersToNotify` tinyint(1) NOT NULL DEFAULT '0',
  `categoriesTaxonomyId` int(11) DEFAULT NULL,
  `showAuthorSocialData` tinyint(1) NOT NULL DEFAULT '0',
  `subheading` tinyint(1) NOT NULL DEFAULT '0',
  `tagsTaxonomyId` int(11) DEFAULT NULL,
  `logoMediumId` int(11) DEFAULT NULL,
  `logoHash` varchar(255) DEFAULT NULL,
  `logoMediumHash` varchar(255) DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  `network_id` int(11) DEFAULT NULL,
  `showAuthorData` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_g3k3dbi955arf2o63kbugpfhi` (`logo_id`),
  KEY `FK_ioixd3yaijoq6klpa9y3m9swm` (`wordpress_id`),
  KEY `FK_scsurvia4osgdtellblk2nyqq` (`network_id`),
  CONSTRAINT `FK_g3k3dbi955arf2o63kbugpfhi` FOREIGN KEY (`logo_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_ioixd3yaijoq6klpa9y3m9swm` FOREIGN KEY (`wordpress_id`) REFERENCES `wordpress` (`id`),
  CONSTRAINT `FK_scsurvia4osgdtellblk2nyqq` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=185 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station_network`
--

DROP TABLE IF EXISTS `station_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station_network` (
  `stations_id` int(11) NOT NULL,
  `networks_id` int(11) NOT NULL,
  PRIMARY KEY (`stations_id`,`networks_id`),
  KEY `FK_5beeae8g58sbx8jhh98vxqm48` (`networks_id`),
  CONSTRAINT `FK_5beeae8g58sbx8jhh98vxqm48` FOREIGN KEY (`networks_id`) REFERENCES `network` (`id`),
  CONSTRAINT `FK_i498le3gfj3c89getgkhbdm3d` FOREIGN KEY (`stations_id`) REFERENCES `station` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station_perspective`
--

DROP TABLE IF EXISTS `station_perspective`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station_perspective` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `station_id` int(11) NOT NULL,
  `taxonomy_id` int(11) DEFAULT NULL,
  `stationId` int(11) DEFAULT NULL,
  `taxonomyId` int(11) DEFAULT NULL,
  `taxonomyName` varchar(255) DEFAULT NULL,
  `taxonomyType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_4vqdg15oa8gdm4ij0s3kddspw` (`station_id`),
  KEY `FK_f0qaq315pyec9vu03nws3x35w` (`taxonomy_id`),
  CONSTRAINT `FK_4vqdg15oa8gdm4ij0s3kddspw` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_f0qaq315pyec9vu03nws3x35w` FOREIGN KEY (`taxonomy_id`) REFERENCES `taxonomy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taxonomy`
--

DROP TABLE IF EXISTS `taxonomy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxonomy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(1) NOT NULL,
  `owningNetwork_id` int(11) DEFAULT NULL,
  `owningStation_id` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qxn61h7507q4emlh9mbr8ykan` (`owningNetwork_id`),
  KEY `FK_3uggep847d2ym982wai3ot5un` (`owningStation_id`),
  CONSTRAINT `FK_3uggep847d2ym982wai3ot5un` FOREIGN KEY (`owningStation_id`) REFERENCES `station` (`id`),
  CONSTRAINT `FK_qxn61h7507q4emlh9mbr8ykan` FOREIGN KEY (`owningNetwork_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=547 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `term`
--

DROP TABLE IF EXISTS `term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `taxonomy_id` int(11) NOT NULL,
  `taxonomyId` int(11) DEFAULT NULL,
  `taxonomyName` varchar(255) DEFAULT NULL,
  `wordpressId` int(11) DEFAULT NULL,
  `wordpressSlug` varchar(255) DEFAULT NULL,
  `parentTermId` int(11) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s0912yy2ycnxmo44xyigjt7q8` (`parent_id`),
  CONSTRAINT `FK_6sih8dychj51bbihofi2js43k` FOREIGN KEY (`taxonomy_id`) REFERENCES `taxonomy` (`id`),
  CONSTRAINT `FK_s0912yy2ycnxmo44xyigjt7q8` FOREIGN KEY (`parent_id`) REFERENCES `term` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1312 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `term_perspective`
--

DROP TABLE IF EXISTS `term_perspective`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `term_perspective` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `station_perspective_id` int(11) NOT NULL,
  `term_id` int(11) DEFAULT NULL,
  `stationId` int(11) DEFAULT NULL,
  `showPopular` tinyint(1) NOT NULL DEFAULT '0',
  `showRecent` tinyint(1) NOT NULL DEFAULT '0',
  `taxonomyId` int(11) DEFAULT NULL,
  `defaultImageHash` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_32s5qdaqtv229i3cddcbn00xs` (`station_perspective_id`,`term_id`),
  KEY `FK_dl7xbw33poq4lc5h7s8m8qcww` (`term_id`),
  CONSTRAINT `FK_82p2ulnxdybd04fxoarp4cwoh` FOREIGN KEY (`station_perspective_id`) REFERENCES `station_perspective` (`id`),
  CONSTRAINT `FK_dl7xbw33poq4lc5h7s8m8qcww` FOREIGN KEY (`term_id`) REFERENCES `term` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userconnection`
--

DROP TABLE IF EXISTS `userconnection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userconnection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accessToken` varchar(255) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(255) DEFAULT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) DEFAULT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1v4ck9s3dmqp92r6nolnix1k4` (`accessToken`),
  KEY `FK_gnl10v9hat0d0k59oo74w761o` (`user_id`),
  CONSTRAINT `FK_gnl10v9hat0d0k59oo74w761o` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(500) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `network_id` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `networkId` int(11) DEFAULT '0',
  `updatedAt` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `network_id` (`network_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`network_id`) REFERENCES `network` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=591 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `file` tinyblob,
  `networkId` int(11) DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wordpress`
--

DROP TABLE IF EXISTS `wordpress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wordpress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `domain` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5xfmaygrtuatt5ug0s0k6bqw3` (`station_id`),
  CONSTRAINT `FK_5xfmaygrtuatt5ug0s0k6bqw3` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-20 20:24:10
