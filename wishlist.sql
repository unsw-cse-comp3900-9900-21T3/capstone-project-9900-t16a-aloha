
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wishlist
-- ----------------------------
DROP TABLE IF EXISTS `wishlist`;
CREATE TABLE `wishlist` (
  `userID` int NOT NULL,
  `productID` varchar(50) NOT NULL,
  `size` decimal(3,1) NOT NULL,
  `addTime` datetime DEFAULT NULL,
  PRIMARY KEY (`userID`,`productID`,`size`),
  KEY `productID_fk` (`productID`),
  CONSTRAINT `productID_fk` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userID_fk` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
