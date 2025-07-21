CREATE DATABASE  IF NOT EXISTS `unidad_cancer_v2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `unidad_cancer_v2`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: unidad_cancer_v2
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `establecimientosalud`
--

DROP TABLE IF EXISTS `establecimientosalud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `establecimientosalud` (
  `id_establecimiento` int NOT NULL AUTO_INCREMENT,
  `red_salud` varchar(100) DEFAULT NULL,
  `microred` varchar(100) DEFAULT NULL,
  `renipress` varchar(100) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_establecimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `establecimientosalud`
--

LOCK TABLES `establecimientosalud` WRITE;
/*!40000 ALTER TABLE `establecimientosalud` DISABLE KEYS */;
INSERT INTO `establecimientosalud` VALUES (1,'Lambayeque','Olmos','00004407','Olmos','I3');
/*!40000 ALTER TABLE `establecimientosalud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metaobstetra`
--

DROP TABLE IF EXISTS `metaobstetra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metaobstetra` (
  `id_meta_obstetra` int NOT NULL AUTO_INCREMENT,
  `id_obstetra` int NOT NULL,
  `id_programa` int NOT NULL,
  `anio` int NOT NULL,
  `meta_anual` int NOT NULL,
  `meta_mensual` int NOT NULL,
  PRIMARY KEY (`id_meta_obstetra`),
  UNIQUE KEY `id_obstetra` (`id_obstetra`,`id_programa`,`anio`),
  KEY `id_programa` (`id_programa`),
  CONSTRAINT `metaobstetra_ibfk_1` FOREIGN KEY (`id_obstetra`) REFERENCES `obstetra` (`id_obstetra`),
  CONSTRAINT `metaobstetra_ibfk_2` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metaobstetra`
--

LOCK TABLES `metaobstetra` WRITE;
/*!40000 ALTER TABLE `metaobstetra` DISABLE KEYS */;
INSERT INTO `metaobstetra` VALUES (34,1,1,2025,69,6),(35,2,1,2025,69,6),(36,3,1,2025,69,6),(37,4,1,2025,69,6),(38,5,1,2025,69,6),(39,6,1,2025,69,6),(40,9,1,2025,69,6),(41,1,2,2025,9,1),(42,2,2,2025,9,1),(43,3,2,2025,9,1),(44,4,2,2025,9,1),(45,5,2,2025,9,1),(46,6,2,2025,9,1),(47,9,2,2025,9,1),(48,1,3,2025,79,7),(49,2,3,2025,79,7),(50,3,3,2025,79,7),(51,4,3,2025,79,7),(52,5,3,2025,79,7),(53,6,3,2025,79,7),(54,9,3,2025,79,7),(55,1,4,2025,90,8),(56,2,4,2025,90,8),(57,3,4,2025,90,8),(58,4,4,2025,90,8),(59,5,4,2025,90,8),(60,6,4,2025,90,8),(61,9,4,2025,90,8),(62,1,5,2025,206,18),(63,2,5,2025,206,18),(64,3,5,2025,206,18),(65,4,5,2025,206,18),(66,5,5,2025,206,18),(67,6,5,2025,206,18),(68,9,5,2025,206,18);
/*!40000 ALTER TABLE `metaobstetra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metaprograma`
--

DROP TABLE IF EXISTS `metaprograma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metaprograma` (
  `id_meta_programa` int NOT NULL AUTO_INCREMENT,
  `id_programa` int NOT NULL,
  `anio` int NOT NULL,
  `meta_anual` int NOT NULL,
  `meta_mensual` int NOT NULL,
  PRIMARY KEY (`id_meta_programa`),
  UNIQUE KEY `id_programa` (`id_programa`,`anio`),
  CONSTRAINT `metaprograma_ibfk_1` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metaprograma`
--

LOCK TABLES `metaprograma` WRITE;
/*!40000 ALTER TABLE `metaprograma` DISABLE KEYS */;
INSERT INTO `metaprograma` VALUES (1,1,2025,483,41),(2,2,2025,61,6),(3,3,2025,548,46),(4,4,2025,624,52),(5,5,2025,1440,120);
/*!40000 ALTER TABLE `metaprograma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `obstetra`
--

DROP TABLE IF EXISTS `obstetra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `obstetra` (
  `id_obstetra` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `colegio` varchar(100) DEFAULT NULL,
  `numero_colegiatura` varchar(100) DEFAULT NULL,
  `id_establecimiento` int DEFAULT NULL,
  PRIMARY KEY (`id_obstetra`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  UNIQUE KEY `dni` (`dni`),
  KEY `id_establecimiento` (`id_establecimiento`),
  CONSTRAINT `obstetra_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `obstetra_ibfk_2` FOREIGN KEY (`id_establecimiento`) REFERENCES `establecimientosalud` (`id_establecimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `obstetra`
--

LOCK TABLES `obstetra` WRITE;
/*!40000 ALTER TABLE `obstetra` DISABLE KEYS */;
INSERT INTO `obstetra` VALUES (1,2,'Raquel','Rodriguez','49682315','Colegio de Obstetras','15487965123',1),(2,3,'Carmen','Flores','45896234','Colegio de Obstetras','14785956123',1),(3,4,'Virginia','Díaz','17895623','Colegio de Obstetras','18794516321',1),(4,5,'María','Paredes','16897512','Colegio de Obstetras','12345678912',1),(5,6,'Ana','Muro','19987564','Colegio de Obstetras','16723457901',1),(6,7,'Esperanza','Ramirez','79458914','Colegio de Obstetras','15478942561',1),(9,8,'Ariana','Sánchez','71736019','Colegio de Obstetras','18479632694',1),(10,9,'Luisa','Peréz','12678934','Colegio de Obstetras','12345678902',1);
/*!40000 ALTER TABLE `obstetra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente` (
  `id_paciente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id_paciente`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente`
--

LOCK TABLES `paciente` WRITE;
/*!40000 ALTER TABLE `paciente` DISABLE KEYS */;
INSERT INTO `paciente` VALUES (1,'Ana','Martinez','45789632','1985-06-27'),(2,'Fernanda','Torres','78591546','2002-06-04'),(3,'María','Vallejo','45789612','1995-04-07'),(4,'Karina','Díaz','65894778','1995-07-16'),(5,'Claudia','Llanos','15468975','1977-07-18');
/*!40000 ALTER TABLE `paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa`
--

DROP TABLE IF EXISTS `programa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programa` (
  `id_programa` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  PRIMARY KEY (`id_programa`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa`
--

LOCK TABLES `programa` WRITE;
/*!40000 ALTER TABLE `programa` DISABLE KEYS */;
INSERT INTO `programa` VALUES (1,'PAP',''),(2,'IVVA',''),(3,'VPH',''),(4,'EXAMEN DE MAMAS',''),(5,'CONSEJERIA','');
/*!40000 ALTER TABLE `programa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registroprueba`
--

DROP TABLE IF EXISTS `registroprueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registroprueba` (
  `id_registro` int NOT NULL AUTO_INCREMENT,
  `id_paciente` int NOT NULL,
  `id_obstetra` int NOT NULL,
  `id_programa` int NOT NULL,
  `fecha` date NOT NULL,
  `observaciones` text,
  `edad` int DEFAULT NULL,
  PRIMARY KEY (`id_registro`),
  KEY `id_paciente` (`id_paciente`),
  KEY `id_obstetra` (`id_obstetra`),
  KEY `id_programa` (`id_programa`),
  CONSTRAINT `registroprueba_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`),
  CONSTRAINT `registroprueba_ibfk_2` FOREIGN KEY (`id_obstetra`) REFERENCES `obstetra` (`id_obstetra`),
  CONSTRAINT `registroprueba_ibfk_3` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registroprueba`
--

LOCK TABLES `registroprueba` WRITE;
/*!40000 ALTER TABLE `registroprueba` DISABLE KEYS */;
INSERT INTO `registroprueba` VALUES (1,1,1,1,'2025-06-27','',40),(2,1,1,2,'2025-06-27','',40),(3,2,1,2,'2025-06-27','',23),(4,3,1,1,'2025-06-27','',30),(5,1,1,3,'2025-01-27','',39),(6,2,1,1,'2025-06-27','',23),(7,1,1,1,'2025-01-27','',39),(8,2,1,5,'2025-07-03','',23),(9,2,1,1,'2025-07-04','',23),(10,3,1,3,'2025-07-10','',30),(11,2,1,5,'2025-07-11','',23),(12,3,1,5,'2025-07-17','',30),(13,2,6,5,'2025-06-27','',23),(14,5,1,2,'2025-07-18','',48);
/*!40000 ALTER TABLE `registroprueba` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `tipo_usuario` enum('administrador','obstetra') NOT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Admin Inicial','admin@gmail.com','$2a$10$UWzXMcud70GJVFk6Z7CvA./0GjS.cCQYS2s70/3ofNmMjTViohgS6','administrador','2025-06-27 05:22:00'),(2,'Raquel','raquel@gmail.com','$2a$10$VFLx7gJ4w16ZLdDhEdJttucxNgkbuqk1eURhuplmQ346U.M/KQiI2','obstetra','2025-06-27 08:15:52'),(3,'Carmen','carmen@gmail.com','$2a$10$VFLx7gJ4w16ZLdDhEdJttucxNgkbuqk1eURhuplmQ346U.M/KQiI2','obstetra','2025-06-27 10:09:03'),(4,'Virginia','virginia@gmail.com','$2a$10$VFLx7gJ4w16ZLdDhEdJttucxNgkbuqk1eURhuplmQ346U.M/KQiI2','obstetra','2025-07-04 01:46:40'),(5,'María','maria@gmail.com','$2a$10$VFLx7gJ4w16ZLdDhEdJttucxNgkbuqk1eURhuplmQ346U.M/KQiI2','obstetra','2025-07-04 01:55:48'),(6,'Ana','ana@gmail.com','$2a$10$pWj3CbPvsmBHvi/Q45H2SOPPs6WzPjeqAHRdXp/qftGXLMmzQc/WK','obstetra','2025-07-04 02:02:55'),(7,'Esperanza','esperanza@gmail.com','$2a$10$lzMVq13A2o/nXFpRBj.XuOPsmKTWNY4h3qvIiDJ1ZtVgqCwRX9/cK','obstetra','2025-07-18 04:33:23'),(8,'Camila','camila@gmail.com','$2a$10$KNN8ed4hJz6vF758uphgj.BsLCLcuclLYOG3XVJtkyuVRU5NvDJAu','obstetra','2025-07-18 05:09:19'),(9,'Luisa','luisa@gmail.com','$2a$10$jmcq5zyBaxeEtkmRLyrP2O7j1cF/xfUx5qyPbswd53KEbApiFepja','obstetra','2025-07-18 14:16:00');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-18  9:45:34
