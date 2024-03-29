-- Table structure for table `clients`
CREATE TABLE IF NOT EXISTS `clients` (
     `id` INTEGER PRIMARY KEY AUTOINCREMENT,
     `name` TEXT NOT NULL,
     `surnames` TEXT NOT NULL,
     `direction` TEXT DEFAULT NULL,
     `province` TEXT DEFAULT NULL,
     `cp` TEXT DEFAULT NULL,
     `phone` TEXT DEFAULT NULL,
     `email` TEXT NOT NULL UNIQUE,
     `passwordhash` TEXT NOT NULL
);

-- Table structure for table `employees`
CREATE TABLE IF NOT EXISTS `employees` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `name` TEXT NOT NULL,
    `surnames` TEXT NOT NULL,
    `direction` TEXT DEFAULT NULL,
    `province` TEXT DEFAULT NULL,
    `cp` TEXT DEFAULT NULL,
    `phone` TEXT DEFAULT NULL,
    `email` TEXT NOT NULL,
    `passwordhash` TEXT NOT NULL
);

-- Table structure for table `order`
CREATE TABLE IF NOT EXISTS `order` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `client_id` INTEGER NOT NULL,
    `status` TEXT NOT NULL,
    `payment_method` TEXT NOT NULL,
    `total` REAL NOT NULL,
    `created_at` TEXT NOT NULL,
    FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)
);

-- Table structure for table `order_details`
CREATE TABLE IF NOT EXISTS `order_details` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `order_id` INTEGER NOT NULL,
    `product_code` INTEGER NOT NULL,
    `quantity` INTEGER NOT NULL DEFAULT 1,
    `price` REAL NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_code`) REFERENCES `stock` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Table structure for table `shopping_cart`
CREATE TABLE IF NOT EXISTS `shopping_cart` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `product_code` INTEGER NOT NULL,
    `quantity` INTEGER NOT NULL DEFAULT 1,
    `client_id` INTEGER NOT NULL,
    UNIQUE (`client_id`, `product_code`),
    FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_code`) REFERENCES `stock` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table structure for table `stock`
CREATE TABLE IF NOT EXISTS `stock` (
    `code` INTEGER PRIMARY KEY AUTOINCREMENT,
    `description` TEXT NOT NULL,
    `image` BLOB,
    `color` TEXT DEFAULT NULL,
    `category` TEXT NOT NULL,
    `quantity` INTEGER NOT NULL,
    `price` REAL NOT NULL,
    `status` TEXT NOT NULL,
    `created_by` INTEGER NOT NULL,
    `updated_by` INTEGER NOT NULL,
    FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`),
    FOREIGN KEY (`updated_by`) REFERENCES `employees` (`id`)
);