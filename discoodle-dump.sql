-- MariaDB dump 10.19  Distrib 10.4.19-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: discoodle
-- ------------------------------------------------------
-- Server version	10.5.8-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Sequence structure for `hibernate_sequence`
--

DROP SEQUENCE IF EXISTS `hibernate_sequence`;
CREATE SEQUENCE `hibernate_sequence` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`hibernate_sequence`, 1001, 0);

--
-- Table structure for table `confirmation_token`
--

DROP TABLE IF EXISTS `confirmation_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `confirmation_token` (
  `id` bigint(20) NOT NULL,
  `confirmed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKah4p1rycwibwm6s9bsyeckq51` (`user_id`),
  CONSTRAINT `FKah4p1rycwibwm6s9bsyeckq51` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `confirmation_token`
--

LOCK TABLES `confirmation_token` WRITE;
/*!40000 ALTER TABLE `confirmation_token` DISABLE KEYS */;
INSERT INTO `confirmation_token` (`id`, `confirmed_at`, `created_at`, `expires_at`, `token`, `user_id`) VALUES (2,NULL,'2021-05-16 15:24:28.431727','2021-05-16 15:39:28.433736','65ddd96f-9624-44ca-8400-bf133b289bd0',1),(4,NULL,'2021-05-16 15:27:26.707118','2021-05-16 15:42:26.707118','9395d91d-2bad-4c78-ba11-48d5ceef3251',3),(10,NULL,'2021-05-16 15:40:48.130773','2021-05-16 15:55:48.130773','a07aff07-8a7f-486e-a2d5-1eb82381dce1',9),(12,NULL,'2021-05-16 15:42:46.632773','2021-05-16 15:57:46.632773','e39e1a5e-6db1-4a66-b364-918efeaa7d9e',11);
/*!40000 ALTER TABLE `confirmation_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `establishment_request`
--

DROP TABLE IF EXISTS `establishment_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `establishment_request` (
  `er_id` bigint(20) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type_of_group` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`er_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establishment_request`
--

LOCK TABLES `establishment_request` WRITE;
/*!40000 ALTER TABLE `establishment_request` DISABLE KEYS */;
INSERT INTO `establishment_request` (`er_id`, `description`, `name`, `status`, `type_of_group`, `user_id`) VALUES (13,'UP7','Université Paris Diderot','ACCEPTEE','FACULTY',11);
/*!40000 ALTER TABLE `establishment_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friendships`
--

DROP TABLE IF EXISTS `friendships`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friendships` (
  `friendships_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`friendships_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendships`
--

LOCK TABLES `friendships` WRITE;
/*!40000 ALTER TABLE `friendships` DISABLE KEYS */;
INSERT INTO `friendships` (`friendships_id`, `receiver_id`, `sender_id`, `status`) VALUES (51,11,9,1);
/*!40000 ALTER TABLE `friendships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `groups_id` bigint(20) NOT NULL,
  `depth` int(11) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type_of_group` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`groups_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` (`groups_id`, `depth`, `description`, `name`, `parent_id`, `token`, `type_of_group`) VALUES (5,1,'DSCL','Discoodle',NULL,'80499af7','DISCOODLE'),(14,1,'UP7','Université Paris Diderot',5,'7306c6fa','FACULTY'),(18,2,'MATH','Mathématiques',14,'1d4a5cce','SUBJECTS'),(24,2,'DSE','Département des sciences exactes',14,'ef74393f','ADMINISTRATION'),(28,3,'INFO','Licence Informatiques',24,'6c109c65','COURSE'),(32,4,'L2','L2',28,'f569c40a','GRADE'),(36,5,'EA4','Elements d\'algo 4',32,'4c2ebccd','SUBJECTS'),(40,5,'M4','Mathématiques',32,'80911b6f','SUBJECTS'),(47,5,'POO4','Programmation 4',32,'9f557203','SUBJECTS');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_group_to_roles`
--

DROP TABLE IF EXISTS `link_group_to_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_group_to_roles` (
  `groups_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`),
  KEY `FK5w984q3xyvfons7x34mhydwoc` (`groups_id`),
  CONSTRAINT `FK5w984q3xyvfons7x34mhydwoc` FOREIGN KEY (`groups_id`) REFERENCES `groups` (`groups_id`),
  CONSTRAINT `FKjc0qcbs169ng0xdjms8i9ysh5` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_group_to_roles`
--

LOCK TABLES `link_group_to_roles` WRITE;
/*!40000 ALTER TABLE `link_group_to_roles` DISABLE KEYS */;
INSERT INTO `link_group_to_roles` (`groups_id`, `role_id`) VALUES (5,6),(5,7),(14,15),(14,16),(18,19),(18,20),(24,25),(24,26),(28,29),(28,30),(32,33),(32,34),(36,37),(36,38),(40,41),(40,42),(47,48),(47,49);
/*!40000 ALTER TABLE `link_group_to_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_groups_to_group`
--

DROP TABLE IF EXISTS `link_groups_to_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_groups_to_group` (
  `groups_id` bigint(20) NOT NULL,
  `son_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_712mi61gbfees232v4e0bprxo` (`son_id`),
  KEY `FK2tongtm9r8gv37dkgohued9nv` (`groups_id`),
  CONSTRAINT `FK2tongtm9r8gv37dkgohued9nv` FOREIGN KEY (`groups_id`) REFERENCES `groups` (`groups_id`),
  CONSTRAINT `FKlyjokw3vk2l0on7qiemhlxrq4` FOREIGN KEY (`son_id`) REFERENCES `groups` (`groups_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_groups_to_group`
--

LOCK TABLES `link_groups_to_group` WRITE;
/*!40000 ALTER TABLE `link_groups_to_group` DISABLE KEYS */;
INSERT INTO `link_groups_to_group` (`groups_id`, `son_id`) VALUES (5,14),(14,18),(14,24),(24,28),(28,32),(32,36),(32,40),(32,47);
/*!40000 ALTER TABLE `link_groups_to_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_groups_to_server`
--

DROP TABLE IF EXISTS `link_groups_to_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_groups_to_server` (
  `server_id` bigint(20) NOT NULL,
  `groups_id` bigint(20) NOT NULL,
  PRIMARY KEY (`server_id`),
  KEY `FKa70w31i5umpi27b1wr4cysoaf` (`groups_id`),
  CONSTRAINT `FK5rvuyrjrkw9rq4kb7po442ftk` FOREIGN KEY (`server_id`) REFERENCES `server` (`server_id`),
  CONSTRAINT `FKa70w31i5umpi27b1wr4cysoaf` FOREIGN KEY (`groups_id`) REFERENCES `groups` (`groups_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_groups_to_server`
--

LOCK TABLES `link_groups_to_server` WRITE;
/*!40000 ALTER TABLE `link_groups_to_server` DISABLE KEYS */;
INSERT INTO `link_groups_to_server` (`server_id`, `groups_id`) VALUES (8,5),(17,14),(21,18),(27,24),(31,28),(35,32),(39,36),(43,40),(50,47);
/*!40000 ALTER TABLE `link_groups_to_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_groups_to_user`
--

DROP TABLE IF EXISTS `link_groups_to_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_groups_to_user` (
  `user_id` bigint(20) NOT NULL,
  `groups_id` bigint(20) NOT NULL,
  KEY `FKk8vy42t3f8pk1tgayo5rh5kdb` (`groups_id`),
  KEY `FK4hf258boq28yfca95obyh5khx` (`user_id`),
  CONSTRAINT `FK4hf258boq28yfca95obyh5khx` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKk8vy42t3f8pk1tgayo5rh5kdb` FOREIGN KEY (`groups_id`) REFERENCES `groups` (`groups_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_groups_to_user`
--

LOCK TABLES `link_groups_to_user` WRITE;
/*!40000 ALTER TABLE `link_groups_to_user` DISABLE KEYS */;
INSERT INTO `link_groups_to_user` (`user_id`, `groups_id`) VALUES (3,5),(11,14),(11,18),(9,14),(11,24),(11,28),(11,32),(11,36),(11,40),(9,24),(9,28),(9,40),(9,36),(9,32),(11,47),(9,47);
/*!40000 ALTER TABLE `link_groups_to_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_role_to_users`
--

DROP TABLE IF EXISTS `link_role_to_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_role_to_users` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FK9np5krooxp2ayah6mf5g4ht9l` (`role_id`),
  KEY `FKge9hmfmjss2mp1qkhn7najuox` (`user_id`),
  CONSTRAINT `FK9np5krooxp2ayah6mf5g4ht9l` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `FKge9hmfmjss2mp1qkhn7najuox` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_role_to_users`
--

LOCK TABLES `link_role_to_users` WRITE;
/*!40000 ALTER TABLE `link_role_to_users` DISABLE KEYS */;
INSERT INTO `link_role_to_users` (`user_id`, `role_id`) VALUES (3,6),(11,15),(11,19),(9,16),(11,25),(11,29),(11,33),(11,37),(11,41),(9,26),(9,38),(9,34),(9,42),(9,30),(11,48),(9,49);
/*!40000 ALTER TABLE `link_role_to_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_rooms_users`
--

DROP TABLE IF EXISTS `link_rooms_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_rooms_users` (
  `user_id` bigint(20) NOT NULL,
  `room_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  KEY `FK8n9ok5jhjskdii0iganpu98h7` (`room_id`),
  KEY `FKtm3xdq7wjq5wyy5fw7h9qsouy` (`user_id`),
  CONSTRAINT `FK8n9ok5jhjskdii0iganpu98h7` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `FKtm3xdq7wjq5wyy5fw7h9qsouy` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_rooms_users`
--

LOCK TABLES `link_rooms_users` WRITE;
/*!40000 ALTER TABLE `link_rooms_users` DISABLE KEYS */;
INSERT INTO `link_rooms_users` (`user_id`, `room_id`) VALUES (3,'ab354ca2-311d-47bd-80ce-c004861f7b07'),(11,'273f1bfd-8734-4f22-836c-deaed22ce8e1'),(11,'b544ade0-214a-4b0a-be7b-b7bb7dd65d2a'),(9,'273f1bfd-8734-4f22-836c-deaed22ce8e1'),(11,'0e6e2d27-720f-4b83-916f-87ff5852cf52'),(11,'9ebd6538-79f1-43a7-9e64-a6fe673c9a63'),(11,'83369a5b-5656-4c86-a9b9-2a0854816890'),(11,'418b1954-b64e-4476-b206-79a16b7af852'),(11,'fefd3780-d309-4b61-bffd-802707aa0986'),(9,'418b1954-b64e-4476-b206-79a16b7af852'),(9,'83369a5b-5656-4c86-a9b9-2a0854816890'),(9,'fefd3780-d309-4b61-bffd-802707aa0986'),(9,'9ebd6538-79f1-43a7-9e64-a6fe673c9a63'),(9,'0e6e2d27-720f-4b83-916f-87ff5852cf52'),(11,'c961b960-c04e-44ff-849f-e25677acc0a1'),(9,'c961b960-c04e-44ff-849f-e25677acc0a1');
/*!40000 ALTER TABLE `link_rooms_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_server_room`
--

DROP TABLE IF EXISTS `link_server_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_server_room` (
  `server_id` bigint(20) DEFAULT NULL,
  `room_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`room_id`),
  KEY `FKhywtkp3hqe3cwktshwctcpu2q` (`server_id`),
  CONSTRAINT `FK80oao0minv07mt98yn9knvpdk` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `FKhywtkp3hqe3cwktshwctcpu2q` FOREIGN KEY (`server_id`) REFERENCES `server` (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_server_room`
--

LOCK TABLES `link_server_room` WRITE;
/*!40000 ALTER TABLE `link_server_room` DISABLE KEYS */;
INSERT INTO `link_server_room` (`server_id`, `room_id`) VALUES (8,'ab354ca2-311d-47bd-80ce-c004861f7b07'),(17,'273f1bfd-8734-4f22-836c-deaed22ce8e1'),(21,'b544ade0-214a-4b0a-be7b-b7bb7dd65d2a'),(27,'0e6e2d27-720f-4b83-916f-87ff5852cf52'),(31,'9ebd6538-79f1-43a7-9e64-a6fe673c9a63'),(35,'83369a5b-5656-4c86-a9b9-2a0854816890'),(39,'418b1954-b64e-4476-b206-79a16b7af852'),(43,'fefd3780-d309-4b61-bffd-802707aa0986'),(50,'c961b960-c04e-44ff-849f-e25677acc0a1');
/*!40000 ALTER TABLE `link_server_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_server_users`
--

DROP TABLE IF EXISTS `link_server_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_server_users` (
  `user_id` bigint(20) NOT NULL,
  `server_id` bigint(20) NOT NULL,
  KEY `FKnl54d04xl2uod7dobwkan11s0` (`server_id`),
  KEY `FKl3t6gr28gyr1eoqppnjw31fem` (`user_id`),
  CONSTRAINT `FKl3t6gr28gyr1eoqppnjw31fem` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKnl54d04xl2uod7dobwkan11s0` FOREIGN KEY (`server_id`) REFERENCES `server` (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_server_users`
--

LOCK TABLES `link_server_users` WRITE;
/*!40000 ALTER TABLE `link_server_users` DISABLE KEYS */;
INSERT INTO `link_server_users` (`user_id`, `server_id`) VALUES (3,8),(11,17),(11,21),(9,17),(11,27),(11,31),(11,35),(11,39),(11,43),(9,39),(9,27),(9,35),(9,31),(9,43),(11,50),(9,50);
/*!40000 ALTER TABLE `link_server_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `message_id` bigint(20) NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `conv_uuid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `edited` bit(1) DEFAULT NULL,
  `message_date` bigint(20) DEFAULT NULL,
  `pinned` bit(1) DEFAULT NULL,
  `sender` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` (`message_id`, `content`, `conv_uuid`, `edited`, `message_date`, `pinned`, `sender`, `type`) VALUES (22,'q;kuugld','b544ade0-214a-4b0a-be7b-b7bb7dd65d2a','\0',1621172755564,'\0','aldric_degore','MESSAGE'),(23,'qldkqgdlqd','b544ade0-214a-4b0a-be7b-b7bb7dd65d2a','\0',1621172756414,'\0','aldric_degore','MESSAGE'),(45,'oiuzorisr','418b1954-b64e-4476-b206-79a16b7af852','\0',1621173437206,'\0','aldric_degore','MESSAGE'),(52,'😅😅😅😅😅😅','094612c2-6a1d-4fbc-8717-0b752d6c86d5','\0',1621173953839,'\0','aldric_degore','MESSAGE');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note` (
  `note_id` bigint(20) NOT NULL,
  `coef` double DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `note` double DEFAULT NULL,
  `titre` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
INSERT INTO `note` (`note_id`, `coef`, `group_id`, `note`, `titre`, `user_id`) VALUES (44,3,36,14,'Contrôle de math',9);
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `role_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rights` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`role_id`, `name`, `rights`) VALUES (6,'Admin','*'),(7,'Etudiant','sr'),(15,'Admin','*'),(16,'Etudiant','sr'),(19,'Admin','*'),(20,'Etudiant','sr'),(25,'Admin','*'),(26,'Etudiant','sr'),(29,'Admin','*'),(30,'Etudiant','sr'),(33,'Admin','*'),(34,'Etudiant','sr'),(37,'Admin','*'),(38,'Etudiant','sr'),(41,'Admin','*'),(42,'Etudiant','sr'),(48,'Admin','*'),(49,'Etudiant','sr');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rooms` (
  `room_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `link_picture` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `room_admin` bigint(20) DEFAULT NULL,
  `room_link` bit(1) DEFAULT NULL,
  `room_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` (`room_id`, `link_picture`, `room_admin`, `room_link`, `room_name`) VALUES ('0e6e2d27-720f-4b83-916f-87ff5852cf52',NULL,11,'','Géneral'),('273f1bfd-8734-4f22-836c-deaed22ce8e1',NULL,11,'','Géneral'),('418b1954-b64e-4476-b206-79a16b7af852',NULL,11,'','Géneral'),('83369a5b-5656-4c86-a9b9-2a0854816890',NULL,11,'','Géneral'),('9ebd6538-79f1-43a7-9e64-a6fe673c9a63',NULL,11,'','Géneral'),('ab354ca2-311d-47bd-80ce-c004861f7b07',NULL,3,'','Géneral'),('b544ade0-214a-4b0a-be7b-b7bb7dd65d2a',NULL,11,'','Géneral'),('c961b960-c04e-44ff-849f-e25677acc0a1',NULL,11,'','Géneral'),('fefd3780-d309-4b61-bffd-802707aa0986',NULL,11,'','Géneral');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server`
--

DROP TABLE IF EXISTS `server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server` (
  `server_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server`
--

LOCK TABLES `server` WRITE;
/*!40000 ALTER TABLE `server` DISABLE KEYS */;
INSERT INTO `server` (`server_id`, `name`) VALUES (8,'Serveur de Discoodle'),(17,'Serveur de Université Paris Diderot'),(21,'Serveur de Mathématiques'),(27,'Serveur de Département des sciences exactes'),(31,'Serveur de Licence Informatiques'),(35,'Serveur de L2'),(39,'Serveur de Elements d\'algo 4'),(43,'Serveur de Mathématiques'),(50,'Serveur de Programmation 4');
/*!40000 ALTER TABLE `server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_request`
--

DROP TABLE IF EXISTS `teacher_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher_request` (
  `tr_id` bigint(20) NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_request`
--

LOCK TABLES `teacher_request` WRITE;
/*!40000 ALTER TABLE `teacher_request` DISABLE KEYS */;
INSERT INTO `teacher_request` (`tr_id`, `status`, `user_id`) VALUES (46,'REFUSEE',9);
/*!40000 ALTER TABLE `teacher_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `link_to_avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `locked` tinyint(1) NOT NULL,
  `mail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`, `enabled`, `last_name`, `link_to_avatar`, `locked`, `mail`, `name`, `password`, `role`, `username`) VALUES (1,0,'FOULON',NULL,0,'foulon.sarah7@gmail.com','Sarah','$2a$10$RZHKkZJAsl1ilugICA/F4.Vw8kgY/2.J1RMtqujabzl9u3Nu2Dxye','STUDENT','sarahfln'),(3,0,'ADMIN',NULL,0,'admin.discoodle@gmail.com','Admin','$2a$10$/3ExJjkeindgP.vt2eBHueih6lkbGrTEDj4ggfsewu2hn8lJVf68W','ADMIN','admin'),(9,0,'SOMON',NULL,0,'bastian.somon@gmail.com','Bastian','$2a$10$fTAkBZ2TdbPuP3GseBES/ejPJ68LjrlcvK6AIvLnHJplCAmQ2WDC6','STUDENT','bastiansmn'),(11,0,'DEGORE',NULL,0,'aldric.degore@gmail.com','Aldric','$2a$10$ZsSwqfHDySWnPVzkpAI60ugfR0AnIm4m1cH3s9uo8/DQ119GECRPm','TEACHER','aldric_degore');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-16 16:22:17
