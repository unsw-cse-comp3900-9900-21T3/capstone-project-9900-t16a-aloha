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