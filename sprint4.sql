ALTER TABLE `ecommerce`.`user` 
ADD COLUMN `preferred` varchar(50) NULL AFTER `lastPurchased`,
ADD CONSTRAINT `user_fk3` FOREIGN KEY (`preferred`) REFERENCES `ecommerce`.`product` (`productID`) ON DELETE SET NULL ON UPDATE CASCADE;

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for orderhistory
-- ----------------------------
DROP TABLE IF EXISTS `orderhistory`;
CREATE TABLE `orderhistory` (
  `orderID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  `orderTime` datetime NOT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `postcode` varchar(50) DEFAULT NULL,
  `totalCost` float(24,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`orderID`),
  KEY `order_fk1` (`userID`),
  CONSTRAINT `order_fk1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for orderdetail
-- ----------------------------
DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE `orderdetail` (
  `orderID` int NOT NULL,
  `productID` varchar(50) NOT NULL,
  `size` decimal(3,1) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`orderID`,`productID`,`size`) USING BTREE,
  KEY `orderid_fk2` (`productID`),
  CONSTRAINT `orderid_fk1` FOREIGN KEY (`orderID`) REFERENCES `orderhistory` (`orderID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orderid_fk2` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
