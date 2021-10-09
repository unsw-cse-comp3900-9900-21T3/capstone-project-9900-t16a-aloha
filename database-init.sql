DROP DATABASE IF EXISTS `ecommerce`;
CREATE DATABASE `ecommerce`; 
USE `ecommerce`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `productID` varchar(50) NOT NULL,
  `lastVisitedTime` datetime DEFAULT NULL,
  `avgRating` decimal(4,2) NOT NULL DEFAULT '0.00',
  `name` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `discount` decimal(4,2) NOT NULL DEFAULT '0.00',
  `brand` varchar(50) DEFAULT NULL,
  `description` text,
  `imgURL` text,
  `visibility` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`productID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of product
-- ----------------------------
BEGIN;
INSERT INTO `product` VALUES ('CJ1646-600', NULL, 0.00, 'Nike Air Force 1 \'07 Essential', 74.95, 0.00, 'Nike', 'Let your shoe game shimmer in the Nike Air Force 1 \'07 Essential. It takes the classic AF-1 design to the next level with its premium leather upper and iridescent Swoosh.', 'url1', 1);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` char(20) NOT NULL,
  `firstName` char(20) NOT NULL,
  `lastName` char(20) NOT NULL,
  `tag` tinyint NOT NULL COMMENT '0 = customer, 1 = admin',
  `gender` enum('Male','Female','Other') DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `postcode` char(10) DEFAULT NULL,
  `imgURL` text,
  `telephone` varchar(20) DEFAULT NULL,
  `lastVisited` varchar(50) DEFAULT NULL,
  `lastPurchased` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`userID`),
  KEY `user_fk1` (`lastVisited`),
  KEY `user_fk2` (`lastPurchased`),
  CONSTRAINT `user_fk1` FOREIGN KEY (`lastVisited`) REFERENCES `product` (`productID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `user_fk2` FOREIGN KEY (`lastPurchased`) REFERENCES `product` (`productID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'test@test.com', 'test', 'Junwei', 'Guo', 0, 'Male', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (2, 'admin@admin.com', 'admin', 'Junwei', 'Guo', 1, 'Male', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;



-- ----------------------------
-- Storge table
-- ----------------------------
DROP TABLE IF EXISTS storge;
CREATE TABLE storge (
    productID varchar(50) NOT NULL,
    size decimal(3,1) NOT NULL,
    stock int NOT NULL DEFAULT '0',
    PRIMARY KEY(productID, size),
    
    -- notice here that we can't set the foreign key to be "on detele set null since it points to a primary key" 
    FOREIGN KEY(productID) REFERENCES product(productID)
)


-- ----------------------------
-- Records of storge
-- ----------------------------
-- BEGIN;
INTO `storge` VALUES ('CJ1646-600', 10.5, 300);
-- COMMIT;
