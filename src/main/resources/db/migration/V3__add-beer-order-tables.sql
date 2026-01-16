DROP TABLE IF EXISTS beer_order_line;
DROP TABLE IF EXISTS beer_order;

CREATE TABLE `beer_order` (
  `id` varchar(36) NOT NULL,
  `customer_id` varchar(36) NOT NULL,
  `customer_ref` varchar(255) NOT NULL,
  `version` int DEFAULT NULL,
  `create_date_time` datetime(6) DEFAULT NULL,
  `update_date_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_CustomerBeerOrder FOREIGN KEY (`customer_id`) REFERENCES customer(`id`)
) engine=InnoDB;

CREATE TABLE `beer_order_line` (
  `id` varchar(36) NOT NULL,
  `beer_id` varchar(36) NOT NULL,
  `beer_order_id` varchar(36) NOT NULL,
  `version` int DEFAULT NULL,
  `order_quantity` int default null,
  `quantity_allocated` int default null,
  `create_date_time` datetime(6) DEFAULT NULL,
  `update_date_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FK_BeerToBeerOrderLine FOREIGN KEY (`beer_id`) REFERENCES beer(`id`),
  CONSTRAINT FK_BeerOrderToBeerOrderLine FOREIGN KEY (`beer_order_id`) REFERENCES beer_order(`id`)
) engine=InnoDB;