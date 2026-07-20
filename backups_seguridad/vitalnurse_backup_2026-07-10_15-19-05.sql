-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: vitalnurse_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `evaluaciones`
--

DROP TABLE IF EXISTS `evaluaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluaciones` (
  `id_evaluacion` bigint NOT NULL AUTO_INCREMENT,
  `diagnostico` varchar(255) NOT NULL,
  `fecha` datetime(6) NOT NULL,
  `resultado` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `id_paciente` bigint NOT NULL,
  PRIMARY KEY (`id_evaluacion`),
  KEY `FKtq6lw7fxw3fctj3mfhoetbu35` (`id_paciente`),
  CONSTRAINT `FKtq6lw7fxw3fctj3mfhoetbu35` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluaciones`
--

LOCK TABLES `evaluaciones` WRITE;
/*!40000 ALTER TABLE `evaluaciones` DISABLE KEYS */;
INSERT INTO `evaluaciones` VALUES (1,'Sobrepeso (Preaución)','2026-07-10 18:36:11.844567','27.68','Triaje Nutricional (IMC)',1),(2,'Peso normal (Saludable)','2026-07-10 18:47:41.684665','22.49','Triaje Nutricional (IMC)',2),(3,'Sobrepeso (Preaución)','2026-07-10 18:48:50.141428','28.34','Triaje Nutricional (IMC)',11);
/*!40000 ALTER TABLE `evaluaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pacientes` (
  `id_paciente` bigint NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) NOT NULL,
  `cedula` varchar(10) NOT NULL,
  `edad` int NOT NULL,
  `enfermedades_preexistentes` text,
  `nombres` varchar(255) NOT NULL,
  `sexo` varchar(15) NOT NULL,
  PRIMARY KEY (`id_paciente`),
  UNIQUE KEY `UKd4a3p7j8gs30muucdmwkh3sah` (`cedula`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pacientes`
--

LOCK TABLES `pacientes` WRITE;
/*!40000 ALTER TABLE `pacientes` DISABLE KEYS */;
INSERT INTO `pacientes` VALUES (1,'Montufar Pacheco','0201183480',51,'Ninguna','Patricio Emilio','Masculino'),(2,'Gómez Díaz','1712345678',45,'Hipertensión arterial controlada','Carlos Andrés','Masculino'),(3,'López Mendieta','1723456789',32,'Ninguna','María Fernanda','Femenino'),(4,'Salazar Ruiz','1734567890',60,'Diabetes Tipo 2','José Luis','Masculino'),(5,'Cabrera Vela','1745678901',28,'Asma leve','Ana Lucía','Femenino'),(6,'Morales Pino','1756789012',55,'Hipotiroidismo','Roberto Carlos','Masculino'),(7,'Vargas Lema','1767890123',41,'Ninguna','Elena Sofía','Femenino'),(8,'Castro Torres','1778901234',70,'Artritis reumatoide','Manuel Antonio','Masculino'),(9,'Ortiz Silva','1789012345',22,'Alergia a la penicilina','Laura Daniela','Femenino'),(10,'Reyes Paz','1790123456',38,'Ninguna','David Santiago','Masculino'),(11,'Molina Cruz','1701234567',50,'Migraña crónica','Carmen Alicia','Femenino');
/*!40000 ALTER TABLE `pacientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `signos_vitales`
--

DROP TABLE IF EXISTS `signos_vitales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `signos_vitales` (
  `id_signo` bigint NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) NOT NULL,
  `frecuencia_cardiaca` int NOT NULL,
  `frecuencia_respiratoria` int NOT NULL,
  `glicemia` double NOT NULL,
  `presion_arterial` varchar(255) NOT NULL,
  `saturacion_oxigeno` int NOT NULL,
  `temperatura` double NOT NULL,
  `id_paciente` bigint NOT NULL,
  PRIMARY KEY (`id_signo`),
  KEY `FK94jt4e3p706cu0pjyxqdm9rey` (`id_paciente`),
  CONSTRAINT `FK94jt4e3p706cu0pjyxqdm9rey` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `signos_vitales`
--

LOCK TABLES `signos_vitales` WRITE;
/*!40000 ALTER TABLE `signos_vitales` DISABLE KEYS */;
/*!40000 ALTER TABLE `signos_vitales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` bigint NOT NULL AUTO_INCREMENT,
  `cedula` varchar(10) NOT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `rol` varchar(255) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `UKefovjjo5q5jlsa0f9eoptdjly` (`cedula`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'1753402161','$2a$10$qGVzY226dWy2lPRJ8T4q1OpMYWJiybJ1qgkBzDbgs5Pp7OROTysZu','Matias Jair Montufar Ninahualpa','admin');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-10 15:19:05
