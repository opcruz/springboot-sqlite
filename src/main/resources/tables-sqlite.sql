PRAGMA foreign_keys = ON;

-- Table structure for table `clients`
CREATE TABLE IF NOT EXISTS `clients` (
     `id` INTEGER PRIMARY KEY AUTOINCREMENT,
     `name` TEXT NOT NULL,
     `surnames` TEXT NOT NULL,
     `direction` TEXT DEFAULT NULL,
     `state` TEXT DEFAULT NULL,
     `postal_code` TEXT DEFAULT NULL,
     `phone` TEXT DEFAULT NULL,
     `email` TEXT NOT NULL UNIQUE,
     `password_hash` TEXT NOT NULL
);

-- Table structure for table `employees`
CREATE TABLE IF NOT EXISTS `employees` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `name` TEXT NOT NULL,
    `surnames` TEXT NOT NULL,
    `email` TEXT NOT NULL UNIQUE,
    `password_hash` TEXT NOT NULL
);

-- Table structure for table `order`
CREATE TABLE IF NOT EXISTS `orders` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `client_id` INTEGER NOT NULL,
    `payment_method` TEXT NOT NULL,
    `created_at` TIMESTAMP NOT NULL,
    FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)
);

-- Table structure for table `order_details`
CREATE TABLE IF NOT EXISTS `order_details` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `order_id` INTEGER NOT NULL,
    `product_code` INTEGER NOT NULL,
    `quantity` INTEGER NOT NULL DEFAULT 1,
    `price` REAL NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
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

-- Table structure for table `categories`
CREATE TABLE IF NOT EXISTS `categories` (
    id INTEGER PRIMARY KEY,
    category TEXT,
    description TEXT
);

-- Table structure for table `stock`
CREATE TABLE IF NOT EXISTS `stock` (
    `code` INTEGER PRIMARY KEY AUTOINCREMENT,
    `description` TEXT NOT NULL,
    `image` BLOB DEFAULT NULL,
    `category_id` TEXT NOT NULL,
    `quantity` INTEGER NOT NULL,
    `price` REAL NOT NULL,
    `status` TEXT NOT NULL DEFAULT 'active' CHECK(status IN ('active', 'inactive', 'out_of_stock', 'deleted')),
    `created_by` INTEGER NOT NULL,
    `updated_by` INTEGER NOT NULL,
    FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`),
    FOREIGN KEY (`updated_by`) REFERENCES `employees` (`id`)
);

-- Insert values into clients
INSERT OR IGNORE INTO `clients` (id, name, surnames, direction, state, postal_code, phone, email, password_hash)
VALUES (1, 'John', 'Doe', '123 Main St', 'New York', '12345', '555-1234', 'john@example.com', '2689367B205C16CE32ED4200942B8B8B1E262DFC70D9BC9FBC77C49699A4F1DF'); -- password ok

-- Insert values into clients
INSERT OR IGNORE INTO `employees` (id, name, surnames, email, password_hash)
VALUES (1, 'John', 'Doe', 'john@example.com', '2689367B205C16CE32ED4200942B8B8B1E262DFC70D9BC9FBC77C49699A4F1DF'); -- password ok


-- Insert values into categories
INSERT OR IGNORE INTO `categories` (id, category, description)
VALUES
    (1, 'Clothing and Accessories', 'Includes clothing items, footwear, jewelry, handbags, hats, and other fashion accessories.'),
    (2, 'Consumer Electronics:', 'Includes electronic devices such as mobile phones, computers, tablets, televisions, cameras, audio players, etc.'),
    (3, 'Beauty and Personal Care', 'Includes makeup products, skincare items, hair care products, fragrances, body care products, etc.'),
    (4, 'Home and Kitchen', 'Includes appliances, kitchen utensils, furniture, home decor, garden supplies, pet supplies, etc.'),
    (5, 'Books', 'Includes printed books, e-books, music on CDs or digital format, movies on DVDs or digital format, audiobooks, etc.'),
    (6, 'Toys and Games', 'Includes toys for children, board games, video games, puzzles, dolls, action figures, etc.'),
    (7, 'Sports and Outdoor Activities', 'Includes sports equipment, sportswear, camping gear, hiking gear, cycling equipment, fitness equipment, etc.'),
    (8, 'Health and Wellness', 'Includes dietary supplements, exercise equipment, healthcare products, activity monitors, mental wellness products, etc'),
    (9, 'Automotive', 'Includes automotive parts, car accessories, car care and cleaning products, tires, automotive tools, etc.'),
    (10, 'Food and Beverages', 'Includes fresh food items, canned products, alcoholic and non-alcoholic beverages, gourmet products, organic products, etc.');
