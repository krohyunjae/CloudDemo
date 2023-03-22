CREATE TABLE `app_user` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
);

CREATE TABLE meta (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `user_id` bigint NOT NULL,
  `path` varchar(10000) NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES app_user(`id`)
);