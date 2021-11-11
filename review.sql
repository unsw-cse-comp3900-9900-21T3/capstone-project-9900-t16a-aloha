DROP TABLE IF EXISTS review;
CREATE TABLE review (
  reviewID int NOT NULL AUTO_INCREMENT,
  orderID int NOT NULL,
  productID varchar(50) NOT NULL,
  rating decimal(2, 1) NOT NULL DEFAULT '0',
  size decimal(3, 1) NOT NULL,

  PRIMARY KEY(reviewID),
  FOREIGN KEY(orderID) REFERENCES orderhistory(orderID),
  FOREIGN KEY(productID) REFERENCES product(productID)
)

INSERT INTO `orderhistory` VALUES (1, 1, '2020-04-13 15:28:25', 'sam', 'yang', 'mascot', '0404744200', 'sydney', 'NSW', '2020', 300.00)
INSERT INTO `orderhistory` VALUES (2, 1, '2020-03-13 15:28:24', 'sam', 'yang', 'mascot', '0404744200', 'sydney', 'NSW', '2020', 250.00)

INSERT INTO `orderdetail` VALUES (1, '553558-125', 10.5, 30)
INSERT INTO `orderdetail` VALUES (1, '488161-602', 9.5, 15)
INSERT INTO `orderdetail` VALUES (2, '488161-602', 11.5, 20)

INSERT INTO `review` VALUES (1, 1, '553558-125', 4.5, 10.5)
INSERT INTO `review` VALUES (2, 1, '488161-602', 3.5, 9.5)