-- MySQL dump 10.13  Distrib 8.0.44, for Linux (x86_64)
--
-- Host: localhost    Database: Dockin
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Authority`
--

DROP TABLE IF EXISTS `Authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Authority` (
  `id` int NOT NULL AUTO_INCREMENT,
  `authority` varchar(256) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `Authority_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Authority`
--

LOCK TABLES `Authority` WRITE;
/*!40000 ALTER TABLE `Authority` DISABLE KEYS */;
/*!40000 ALTER TABLE `Authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `absence_requests`
--

DROP TABLE IF EXISTS `absence_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `absence_requests` (
  `request_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `request_type` varchar(20) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `reason` text NOT NULL,
  `document_url` varchar(255) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'PENDING',
  `requested_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `processed_by` varchar(50) DEFAULT NULL,
  `processed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`request_id`),
  KEY `user_id` (`user_id`),
  KEY `processed_by` (`processed_by`),
  CONSTRAINT `absence_requests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `absence_requests_ibfk_2` FOREIGN KEY (`processed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `absence_requests`
--

LOCK TABLES `absence_requests` WRITE;
/*!40000 ALTER TABLE `absence_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `absence_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `clock_in_time` datetime NOT NULL,
  `clock_out_time` datetime DEFAULT NULL,
  `work_date` date NOT NULL,
  `role` enum('NORMAL','LATE','ABSENT','VACATION','SICK') DEFAULT NULL,
  `in_location` varchar(255) DEFAULT NULL,
  `out_location` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attendance_member` (`user_id`),
  KEY `idx_work_date` (`work_date`),
  CONSTRAINT `fk_attendance_member` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_members`
--

DROP TABLE IF EXISTS `chat_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_members` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `joined_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_read_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `chat_members_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`room_id`) ON DELETE CASCADE,
  CONSTRAINT `chat_members_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_members`
--

LOCK TABLES `chat_members` WRITE;
/*!40000 ALTER TABLE `chat_members` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_messages`
--

DROP TABLE IF EXISTS `chat_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_messages` (
                                 `message_id` bigint NOT NULL AUTO_INCREMENT,
                                 `room_id` int NOT NULL,
                                 `sender_id` varchar(50) NOT NULL,
                                 `content` text NOT NULL,
                                 `sent_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`message_id`),
                                 KEY `idx_room_sent` (`room_id`,`sent_at`),
                                 KEY `fk_chat_sender_idx` (`sender_id`), -- sender_id로 검색을 빠르게 하기 위한 인덱스
                                 CONSTRAINT `chat_messages_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`room_id`) ON DELETE CASCADE,
                                 CONSTRAINT `fk_chat_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_messages`
--

LOCK TABLES `chat_messages` WRITE;
/*!40000 ALTER TABLE `chat_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_rooms`
--

DROP TABLE IF EXISTS `chat_rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_rooms` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `room_name` varchar(100) DEFAULT NULL,
  `is_group` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_rooms`
--

LOCK TABLES `chat_rooms` WRITE;
/*!40000 ALTER TABLE `chat_rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checklist_items`
--

DROP TABLE IF EXISTS `checklist_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklist_items` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `checklist_id` int NOT NULL,
  `content` varchar(255) NOT NULL,
  `sequence` int NOT NULL,
  PRIMARY KEY (`item_id`),
  KEY `checklist_id` (`checklist_id`),
  CONSTRAINT `checklist_items_ibfk_1` FOREIGN KEY (`checklist_id`) REFERENCES `checklists` (`checklist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklist_items`
--

LOCK TABLES `checklist_items` WRITE;
/*!40000 ALTER TABLE `checklist_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `checklist_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checklist_results`
--

DROP TABLE IF EXISTS `checklist_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklist_results` (
  `result_id` int NOT NULL AUTO_INCREMENT,
  `checklist_id` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `equipment_id` int NOT NULL,
  `checked_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_checked` tinyint(1) NOT NULL,
  PRIMARY KEY (`result_id`),
  KEY `checklist_id` (`checklist_id`),
  KEY `user_id` (`user_id`),
  KEY `equipment_id` (`equipment_id`),
  CONSTRAINT `checklist_results_ibfk_1` FOREIGN KEY (`checklist_id`) REFERENCES `checklists` (`checklist_id`),
  CONSTRAINT `checklist_results_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `checklist_results_ibfk_3` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklist_results`
--

LOCK TABLES `checklist_results` WRITE;
/*!40000 ALTER TABLE `checklist_results` DISABLE KEYS */;
/*!40000 ALTER TABLE `checklist_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checklists`
--

DROP TABLE IF EXISTS `checklists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklists` (
  `checklist_id` int NOT NULL AUTO_INCREMENT,
  `equipment_id` int NOT NULL,
  `title` varchar(100) NOT NULL,
  `role` enum('pre','post') DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`checklist_id`),
  KEY `equipment_id` (`equipment_id`),
  CONSTRAINT `checklists_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklists`
--

LOCK TABLES `checklists` WRITE;
/*!40000 ALTER TABLE `checklists` DISABLE KEYS */;
/*!40000 ALTER TABLE `checklists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emergency_contacts`
--

DROP TABLE IF EXISTS `emergency_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_contacts` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `team_name` varchar(50) NOT NULL,
  `contact_number` varchar(20) NOT NULL,
  `is_primary` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_contacts`
--

LOCK TABLES `emergency_contacts` WRITE;
/*!40000 ALTER TABLE `emergency_contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `equipment_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `qr_code` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`equipment_id`),
  UNIQUE KEY `qr_code` (`qr_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `target_user_id` varchar(50) NOT NULL,
  `due_at` datetime DEFAULT NULL,
  `sent` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`notification_id`),
  KEY `target_user_id` (`target_user_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`target_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `safety_courses`
--

DROP TABLE IF EXISTS `safety_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_courses` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `video_url` varchar(255) NOT NULL,
  `duration_minutes` int DEFAULT '0',
  `is_mandatory` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `safety_courses`
--

LOCK TABLES `safety_courses` WRITE;
/*!40000 ALTER TABLE `safety_courses` DISABLE KEYS */;
/*!40000 ALTER TABLE `safety_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `safety_enrollments`
--

DROP TABLE IF EXISTS `safety_enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_enrollments` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `course_id` int NOT NULL,
  `is_completed` tinyint(1) DEFAULT '0',
  `completion_date` datetime DEFAULT NULL,
  `enrolled_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`enrollment_id`),
  UNIQUE KEY `uk_user_course` (`user_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `safety_enrollments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `safety_enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `safety_courses` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `safety_enrollments`
--

LOCK TABLES `safety_enrollments` WRITE;
/*!40000 ALTER TABLE `safety_enrollments` DISABLE KEYS */;
/*!40000 ALTER TABLE `safety_enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` varchar(50) NOT NULL,
  `name` varchar(10) NOT NULL,
  `password` varchar(256) NOT NULL,
  `role` enum('ADMIN','USER') DEFAULT NULL,
  `language_code` varchar(10) DEFAULT 'ko',
  `tts_enabled` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ship_yard_area` varchar(100) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('1001','김철수','1234','ADMIN','ko',1,'2026-01-08 08:08:25','2026-01-08 08:08:25','제8조선소'),('1002','이영희','1234','ADMIN','en',0,'2026-01-08 08:08:25','2026-01-08 08:08:25','제8조선소'),('1003','박민준','1234','USER','ko',1,'2026-01-08 08:08:25','2026-01-08 08:08:25','제5조선소');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_logs`
--

DROP TABLE IF EXISTS `work_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_logs` (
                             `log_id` int NOT NULL AUTO_INCREMENT,
                             `user_id` varchar(50) DEFAULT NULL,
                             `title` varchar(256) NOT NULL,
                             `equipment_id` int DEFAULT NULL,
                             `log_text` text NOT NULL,
                             `audio_file_url` varchar(255) DEFAULT NULL,
                             `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                             `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (`log_id`),
                             KEY `user_id` (`user_id`),
                             KEY `equipment_id` (`equipment_id`),
                             CONSTRAINT `work_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
                             CONSTRAINT `work_logs_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_logs`
--

LOCK TABLES `work_logs` WRITE;
/*!40000 ALTER TABLE `work_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `work_logs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

DROP TABLE IF EXISTS `work_log_images`;
CREATE TABLE `work_log_images` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `image_url` varchar(500) NOT NULL,   -- S3 URL이 길 수 있으므로 500자 권장
                                   `work_log_id` int NOT NULL,          -- Work_logs의 log_id 참조
                                   PRIMARY KEY (`id`),
                                   KEY `fk_work_log_image_parent` (`work_log_id`),
                                   CONSTRAINT `fk_work_log_image_parent`
                                       FOREIGN KEY (`work_log_id`) REFERENCES `work_logs` (`log_id`)
                                           ON DELETE CASCADE                 -- 게시글 삭제 시 사진 데이터도 삭제
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dump completed on 2026-01-08  8:37:53

       --
-- Table structure for table `chat_history`
--

DROP TABLE IF EXISTS `chat_history`;

CREATE TABLE `chat_history` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `user_id` varchar(50) DEFAULT NULL,
                                `trace_id` varchar(255) DEFAULT NULL,
                                `user_query` text NOT NULL,
                                `reply` text NOT NULL,
                                `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
                                PRIMARY KEY (`id`),
                                KEY `fk_chat_user` (`user_id`),
                                KEY `idx_trace_id` (`trace_id`), -- 추적용 ID로 빠르게 찾기 위해 추가
                                CONSTRAINT `fk_chat_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `translate_logs`;

-- 번역 로그 테이블 (기존 유지하되 제약 조건 확인)
DROP TABLE IF EXISTS `translate_logs`;
CREATE TABLE `translate_logs` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `log_id` int NOT NULL,
                                  `user_id` varchar(50) DEFAULT NULL,
                                  `trace_id` varchar(255) DEFAULT NULL,
                                  `target_lang` varchar(10) NOT NULL,
                                  `original_title` varchar(256),
                                  `translated_title` varchar(256),
                                  `original_text` text NOT NULL,
                                  `translated_text` text NOT NULL,
                                  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
                                  PRIMARY KEY (`id`),
                                  KEY `fk_trans_log` (`log_id`),
                                  KEY `fk_trans_user` (`user_id`),
                                  CONSTRAINT `fk_trans_log` FOREIGN KEY (`log_id`) REFERENCES `work_logs` (`log_id`) ON DELETE CASCADE,
                                  CONSTRAINT `fk_trans_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;