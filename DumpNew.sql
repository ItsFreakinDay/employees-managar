-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: mng_db
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (4,'ИТ'),(1,'Общий отдел'),(5,'Отдел бухгалтерии'),(6,'Отдел экономики');
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `FullName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `BirthDate` date NOT NULL,
  `Email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Education` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `PersonnelNumber` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PersonnelNumber` (`PersonnelNumber`),
  UNIQUE KEY `Email` (`Email`),
  UNIQUE KEY `Phone` (`Phone`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Иванов Иван Иванович','1985-03-15','ivanov@mail.com','123-456-7890','Высшее','12345'),(2,'Петров Петр Петрович','1990-05-20','petrov@mail.com','234-567-8901','Высшее','23456'),(3,'Сидоров Сидор Сидорович','1995-07-25','sidorov@mail.com','345-678-9012','Высшее','34567'),(15,'Даянов Илья Степанович','1990-01-02','dayanov@mail.ru','999-100-4939','Высшее','45678'),(16,'Ким Чи Ным','1992-02-04','kimich@mail.com','999-123-3454','Высшее','56789');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employeestatuses`
--

DROP TABLE IF EXISTS `employeestatuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employeestatuses` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Employee_ID` int NOT NULL,
  `Status_ID` int NOT NULL,
  `Position_ID` int DEFAULT NULL,
  `Department_ID` int DEFAULT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `Employee_ID` (`Employee_ID`),
  KEY `Status_ID` (`Status_ID`),
  KEY `Position_ID` (`Position_ID`),
  KEY `Department_ID` (`Department_ID`),
  CONSTRAINT `employeestatuses_ibfk_1` FOREIGN KEY (`Employee_ID`) REFERENCES `employees` (`ID`),
  CONSTRAINT `employeestatuses_ibfk_2` FOREIGN KEY (`Status_ID`) REFERENCES `statusvalues` (`ID`),
  CONSTRAINT `employeestatuses_ibfk_3` FOREIGN KEY (`Position_ID`) REFERENCES `positions` (`ID`),
  CONSTRAINT `employeestatuses_ibfk_4` FOREIGN KEY (`Department_ID`) REFERENCES `departments` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeestatuses`
--

LOCK TABLES `employeestatuses` WRITE;
/*!40000 ALTER TABLE `employeestatuses` DISABLE KEYS */;
INSERT INTO `employeestatuses` VALUES (1,1,1,1,1,'2023-01-01',NULL),(2,2,4,19,1,'2023-02-01',NULL),(3,3,1,3,1,'2023-03-01',NULL),(7,15,1,7,NULL,'2025-01-30',NULL),(8,16,3,12,NULL,'2025-01-30',NULL);
/*!40000 ALTER TABLE `employeestatuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `positiondepartments`
--

DROP TABLE IF EXISTS `positiondepartments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `positiondepartments` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Position_ID` int NOT NULL,
  `Department_ID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `Position_ID` (`Position_ID`),
  KEY `Department_ID` (`Department_ID`),
  CONSTRAINT `positiondepartments_ibfk_1` FOREIGN KEY (`Position_ID`) REFERENCES `positions` (`ID`),
  CONSTRAINT `positiondepartments_ibfk_2` FOREIGN KEY (`Department_ID`) REFERENCES `departments` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `positiondepartments`
--

LOCK TABLES `positiondepartments` WRITE;
/*!40000 ALTER TABLE `positiondepartments` DISABLE KEYS */;
INSERT INTO `positiondepartments` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,1),(5,7,5),(6,6,5),(7,5,5),(8,8,5),(12,9,6),(13,11,6),(14,12,6),(15,10,6),(19,17,4),(20,18,4),(21,19,4);
/*!40000 ALTER TABLE `positiondepartments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `positions`
--

DROP TABLE IF EXISTS `positions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `positions` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `positions`
--

LOCK TABLES `positions` WRITE;
/*!40000 ALTER TABLE `positions` DISABLE KEYS */;
INSERT INTO `positions` VALUES (7,'Бухгалтер'),(6,'Главный бухгалтер'),(2,'Главный специалист'),(1,'Начальник общего отдела'),(5,'Начальник отдела бухгалтерии'),(17,'Начальник отдела ИТ'),(9,'Начальник отдела экономики'),(3,'Оператор программы'),(4,'Специалист'),(19,'Специалист отдела'),(8,'Специалист по учету'),(11,'Специалист по экономическому планированию'),(18,'Технический специалист'),(12,'Финансовый аналитик'),(10,'Экономист');
/*!40000 ALTER TABLE `positions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statusvalues`
--

DROP TABLE IF EXISTS `statusvalues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statusvalues` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statusvalues`
--

LOCK TABLES `statusvalues` WRITE;
/*!40000 ALTER TABLE `statusvalues` DISABLE KEYS */;
INSERT INTO `statusvalues` VALUES (1,'Активный'),(4,'Больничный'),(3,'В отпуске'),(2,'Уволен');
/*!40000 ALTER TABLE `statusvalues` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-30 14:45:08
