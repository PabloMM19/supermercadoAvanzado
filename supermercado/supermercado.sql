CREATE DATABASE  IF NOT EXISTS `almacenSupermercado` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `almacenSupermercado`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: almacenSupermercado
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idCategoria` int NOT NULL AUTO_INCREMENT,
  `nombreCategoria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `nombreProducto` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `precioProducto` double NOT NULL,
  `unidadesProducto` int NOT NULL,
  `fotoProducto` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `idCategoria` int NOT NULL,
  PRIMARY KEY (`idProducto`),
  KEY `idCategoria_idx` (`idCategoria`),
  CONSTRAINT `idCategoria` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-24 17:34:56

INSERT INTO `categoria` VALUES (1,'Frutas y Verduras'),(2,'Carnes y Aves'),(3,'Pescados y Mariscos'),(4,'Productos lácteos'),(5,'Panadería y Pastelería'),(6,'Bebidas'),(7,'Alimentos enlatados'),(8,'Cuidado personal'),(9,'Limpieza del hogar'),(10,'Higiene personal');
INSERT INTO `producto` VALUES (1,'Manzana',1.99,50,'src/main/resources/image_1.png',1),(2,'Pera',2.49,40,'src/main/resources/image_2.png',1),(3,'Banana',0.99,30,'src/main/resources/image_3.png',1),(4,'Carne de res',9.99,20,'src/main/resources/image_4.png',2),(5,'Pollo',6.99,15,'src/main/resources/image_5.png',2),(6,'Salmón',12.99,25,'src/main/resources/image_6.png',3),(7,'Atún',3.99,35,'src/main/resources/image_7.png',3),(8,'Leche',2.29,60,'src/main/resources/image_8.png',4),(9,'Queso',4.99,40,'src/main/resources/image_9.png',4),(10,'Yogur',1.49,70,'src/main/resources/image_10.png',4),(11,'Pan blanco',1.99,80,'src/main/resources/image_11.png',5),(12,'Pan integral',2.49,60,'src/main/resources/image_12.png',5),(13,'Gaseosa',0.99,100,'src/main/resources/image_13.png',6),(14,'Agua mineral',0.79,120,'src/main/resources/image_14.png',6),(15,'Refresco de frutas',1.49,90,'src/main/resources/image_15.png',6),(16,'Tomate enlatado',1.29,50,'src/main/resources/image_16.png',7),(17,'Maíz enlatado',0.99,70,'src/main/resources/image_17.png',7),(18,'Jabón de baño',1.99,40,'src/main/resources/image_18.png',8),(19,'Champú',3.49,30,'src/main/resources/image_19.png',8),(20,'Papel higiénico',0.99,60,'src/main/resources/image_20.png',8),(21,'Detergente líquido',2.99,50,'src/main/resources/image_21.png',9),(22,'Limpiavidrios',1.49,40,'src/main/resources/image_22.png',9),(23,'Esponja',0.99,30,'src/main/resources/image_23.png',9),(24,'Cepillo de dientes',1.99,80,'src/main/resources/image_24.png',10),(25,'Pasta dental',2.49,60,'src/main/resources/image_25.png',10),(26,'Toalla de papel',1.99,70,'src/main/resources/image_26.png',10),(27,'Cebolla',1.49,40,'src/main/resources/image_27.png',1),(28,'Zanahoria',0.99,30,'src/main/resources/image_28.png',1),(29,'Cerdo',7.99,20,'src/main/resources/image_29.png',2),(30,'Pescado blanco',8.99,15,'src/main/resources/image_30.png',3),(31,'Huevo',2.99,40,'src/main/resources/image_31.png',4),(32,'Mantequilla',3.99,30,'src/main/resources/image_32.png',4),(33,'Tortillas',1.49,70,'src/main/resources/image_33.png',5),(34,'Cerveza',1.99,100,'src/main/resources/image_34.png',6),(35,'Sopa enlatada',1.29,50,'src/main/resources/image_35.png',7),(36,'Desodorante',2.99,40,'src/main/resources/image_36.png',8);
