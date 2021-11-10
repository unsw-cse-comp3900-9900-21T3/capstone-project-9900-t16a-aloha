-- --------
-- !!! this table information is already covered inside database-init.sql
-- --------

-- ----------------------------
-- add a column in table product
-- 0: not deleted
-- 1: has been deleted
-- ----------------------------
ALTER TABLE `ecommerce`.`product` 
ADD COLUMN `isDeleted` tinyint(1) NOT NULL DEFAULT 0 AFTER `visibility`;