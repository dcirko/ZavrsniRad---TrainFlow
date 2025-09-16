/*
SQLyog Community v13.2.1 (64 bit)
MySQL - 8.0.36 : Database - trainflow
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`trainflow` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `trainflow`;

/*Table structure for table `exercises` */

DROP TABLE IF EXISTS `exercises`;

CREATE TABLE `exercises` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `category` enum('Cardio','Strength','Mobility','HIIT') NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `exercises` */

insert  into `exercises`(`id`,`name`,`category`,`description`) values 
(1,'Push-ups','Strength','Bodyweight exercise for chest, shoulders, and triceps.'),
(2,'Squats','Strength','Compound lower body exercise for quads, hamstrings, and glutes.'),
(3,'Jumping Jacks','Cardio','A full-body cardio warm-up exercise.'),
(4,'Plank','Strength','Core stability exercise that strengthens abs and back.'),
(5,'Deadlift','Strength','Compound lift targeting the posterior chain.'),
(6,'Pull-ups','Strength','Upper body pull exercise targeting back and biceps.'),
(7,'Mountain Climbers','HIIT','High-intensity core and cardio movement.'),
(8,'Lunges','Strength','Leg exercise targeting glutes, quads, and balance.'),
(9,'Bench Press','Strength','Chest press using barbell'),
(10,'Overhead Press','Strength','Shoulder press standing or seated'),
(11,'Triceps Pushdown','Strength','Triceps isolation on cable machine'),
(12,'Incline Dumbbell Press','Strength','Upper chest focus'),
(13,'Barbell Rows','Strength','Bent over barbell row for lats'),
(14,'Face Pulls','Strength','Posterior delts and upper back'),
(15,'Bicep Curls','Strength','Biceps isolation'),
(16,'Leg Press','Strength','Machine-based compound leg move'),
(17,'Calf Raises','Strength','Isolate and train calves'),
(18,'Jump Squats','HIIT','Explosive leg movement for power'),
(19,'Burpees','HIIT','Full body plyometric exercise'),
(20,'High Knees','Cardio','Running in place with high legs'),
(21,'Russian Twists','Strength','Core rotation exercise'),
(22,'Bulgarian Squats','Strength','legs'),
(23,'bench','Strength',''),
(24,'sda','Mobility',''),
(25,'sdasd','Mobility','sddsd'),
(26,'asdsad','Mobility','asdsd'),
(27,'Squat','Strength',''),
(28,'Bicycle','Cardio',''),
(29,'Tricep extension','Strength',''),
(30,'Lat Pulldown','Strength',''),
(31,'Tricep Pulldown','Strength',''),
(32,'asdasd','Strength',''),
(33,'sdasds','Strength',''),
(34,'GUZE','Strength',''),
(38,'Skullcrushers','Strength',''),
(39,'hard','Strength','hard'),
(40,'KURAC','Strength','KURČINA'),
(41,'asdasda','Strength','dasda'),
(42,'Leg Extension','Strength','Legs'),
(43,'Leg exstension','Strength','Legs');

/*Table structure for table `refresh_token` */

DROP TABLE IF EXISTS `refresh_token`;

CREATE TABLE `refresh_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKf95ixxe7pa48ryn1awmh2evt7` (`user_id`),
  CONSTRAINT `FKjtx87i0jvq2svedphegvdwcuy` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `refresh_token` */

insert  into `refresh_token`(`id`,`expiry_date`,`token`,`user_id`) values 
(6,'2025-05-30 14:47:35.104139','d9937a8e-a3fc-4b9f-b555-8c9d2a9fa700',6),
(7,'2025-06-19 14:33:47.831728','d54d9354-428a-4fe6-8bbe-a74887d11945',1),
(75,'2025-08-13 16:20:55.473091','5924fd32-d512-4e91-9c50-aab605cfc53f',10000),
(88,'2025-08-19 19:07:19.471186','1f755974-abd7-48e6-933b-ffbbe5a4c7e6',10003),
(89,'2025-08-20 19:48:41.728551','72d4f98f-0a27-4540-a7a2-0699b8a15b73',10002),
(91,'2025-08-20 19:49:30.894277','eb507225-6ba9-473c-9f5a-e0cdc1be0d45',10001),
(142,'2025-09-08 15:45:41.739429','2a63c006-a941-4ba6-89e3-9cc3561c7cef',4),
(143,'2025-09-08 16:09:32.736782','f6998713-3335-46b9-84d0-2fd2232f6a94',10004),
(144,'2025-09-09 13:00:55.410728','092bf2e2-e163-4ae9-8e79-99f1bdb4bc03',10006),
(146,'2025-09-12 11:13:57.858500','5b0d1594-e2e7-4465-bd8a-b1ccec7da8ff',10007);

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `roles` */

insert  into `roles`(`id`,`name`) values 
(1,'ROLE_ADMIN'),
(2,'ROLE_USER');

/*Table structure for table `training_day_exercises` */

DROP TABLE IF EXISTS `training_day_exercises`;

CREATE TABLE `training_day_exercises` (
  `id` int NOT NULL AUTO_INCREMENT,
  `training_day_id` int NOT NULL,
  `exercise_id` int NOT NULL,
  `sets` int DEFAULT '3',
  `reps` int DEFAULT '10',
  `rest_time` int DEFAULT '60',
  PRIMARY KEY (`id`),
  KEY `training_day_id` (`training_day_id`),
  KEY `exercise_id` (`exercise_id`),
  CONSTRAINT `training_day_exercises_ibfk_1` FOREIGN KEY (`training_day_id`) REFERENCES `training_days` (`id`) ON DELETE CASCADE,
  CONSTRAINT `training_day_exercises_ibfk_2` FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE,
  CONSTRAINT `training_day_exercises_chk_1` CHECK ((`sets` > 0)),
  CONSTRAINT `training_day_exercises_chk_2` CHECK ((`reps` > 0)),
  CONSTRAINT `training_day_exercises_chk_3` CHECK ((`rest_time` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=230 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `training_day_exercises` */

insert  into `training_day_exercises`(`id`,`training_day_id`,`exercise_id`,`sets`,`reps`,`rest_time`) values 
(1,1,1,3,12,60),
(2,1,6,3,8,90),
(3,1,4,3,45,60),
(4,1,3,3,30,30),
(5,2,2,4,10,90),
(6,2,8,3,10,60),
(7,2,3,2,30,30),
(8,2,4,3,45,30),
(9,4,1,4,15,60),
(10,4,3,3,30,30),
(11,4,4,3,60,45),
(12,4,2,3,10,60),
(13,5,6,3,10,90),
(14,5,4,3,60,30),
(15,5,3,3,30,30),
(16,5,7,3,30,30),
(17,6,7,4,30,20),
(18,6,3,4,30,20),
(19,6,1,3,15,45),
(20,6,8,3,12,45),
(117,50,9,3,8,110),
(118,50,27,3,10,60),
(119,51,15,3,10,60),
(120,56,28,3,10,60),
(121,57,9,3,10,120),
(122,57,29,3,10,60),
(123,58,8,3,10,60),
(124,64,9,3,10,60),
(125,65,2,3,10,120),
(130,85,32,3,10,60),
(131,87,32,3,10,60),
(132,90,33,3,10,60),
(134,99,18,3,10,30),
(135,99,19,3,8,30),
(136,99,7,3,10,60),
(137,100,4,3,10,60),
(138,100,21,3,10,30),
(139,101,3,3,10,35),
(140,101,4,3,5,60),
(141,102,2,3,10,60),
(142,102,8,3,10,60),
(143,103,1,3,10,60),
(144,103,6,3,10,60),
(145,103,10,3,10,60),
(146,104,19,3,10,60),
(147,104,18,3,10,60),
(148,104,1,3,10,60),
(181,141,9,4,8,90),
(182,141,10,3,10,60),
(183,141,11,3,12,45),
(184,141,12,4,10,60),
(185,141,38,3,8,60),
(186,142,13,4,10,90),
(187,142,14,3,12,45),
(188,142,15,3,12,45),
(189,142,6,3,8,60),
(190,143,2,4,10,90),
(191,143,5,4,8,90),
(192,143,16,3,12,60),
(193,143,17,3,15,45),
(194,145,9,4,8,90),
(195,145,10,3,10,60),
(196,145,12,4,10,60),
(197,145,1,3,15,30),
(198,146,13,4,10,90),
(199,146,14,3,12,45),
(200,146,15,3,12,45),
(201,146,4,3,60,30),
(202,147,2,4,10,90),
(203,147,16,3,12,60),
(204,147,17,3,15,45),
(205,147,8,3,12,60),
(207,155,25,3,10,60),
(208,155,39,3,10,60),
(209,162,40,3,10,60);

/*Table structure for table `training_days` */

DROP TABLE IF EXISTS `training_days`;

CREATE TABLE `training_days` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_id` int NOT NULL,
  `day` enum('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday') NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  CONSTRAINT `training_days_ibfk_1` FOREIGN KEY (`plan_id`) REFERENCES `training_plans` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `training_days` */

insert  into `training_days`(`id`,`plan_id`,`day`,`name`) values 
(1,2,'Monday','Upper Body Strength'),
(2,2,'Tuesday','Lower Body Strength'),
(3,2,'Wednesday','Rest Day'),
(4,2,'Thursday','Push Focus'),
(5,2,'Friday','Pull Focus'),
(6,2,'Saturday','Full Body HIIT'),
(7,2,'Sunday','Rest Day'),
(50,1007,'Monday','Full Body'),
(51,1007,'Tuesday','Full Body'),
(52,1007,'Wednesday','Rest Day'),
(53,1007,'Thursday','Full Body'),
(54,1007,'Friday','Full Body'),
(55,1007,'Saturday','Rest Day'),
(56,1007,'Sunday','Cardio'),
(57,1008,'Monday','Upper'),
(58,1008,'Tuesday','Lower'),
(59,1008,'Wednesday','Rest'),
(60,1008,'Thursday','Upper'),
(61,1008,'Friday','Lower'),
(62,1008,'Saturday','Rest'),
(63,1008,'Sunday','Rest'),
(64,1009,'Monday','Upper '),
(65,1009,'Tuesday','Lower'),
(66,1009,'Wednesday','Rest Day'),
(67,1009,'Thursday','Upper'),
(68,1009,'Friday','Lower'),
(69,1009,'Saturday','Rest Day'),
(70,1009,'Sunday','Rest Day'),
(85,1012,'Monday','sfdasdas'),
(86,1012,'Tuesday','asdasd'),
(87,1012,'Wednesday','sadsd'),
(88,1012,'Thursday','sdsdas'),
(89,1012,'Friday','asdasd'),
(90,1012,'Saturday','asdasd'),
(91,1012,'Sunday','asdasd'),
(99,1014,'Monday','Full Body HIIT'),
(100,1014,'Tuesday','Active Recovery'),
(101,1014,'Wednesday','Core & Cardio'),
(102,1014,'Thursday','Lower Body HIIT'),
(103,1014,'Friday','Upper Body'),
(104,1014,'Saturday','Full Body Circuit'),
(105,1014,'Sunday','Rest Day'),
(141,1001,'Monday','Push'),
(142,1001,'Tuesday','Pull'),
(143,1001,'Wednesday','Legs'),
(144,1001,'Thursday','Rest Day'),
(145,1001,'Friday','Push'),
(146,1001,'Saturday','Pull'),
(147,1001,'Sunday','Legs'),
(155,1006,'Monday','asds'),
(156,1006,'Tuesday','sdsd'),
(157,1006,'Wednesday','DSADASD'),
(158,1006,'Thursday','ADAS'),
(159,1006,'Friday','dsd'),
(160,1006,'Saturday','SADSAD'),
(161,1006,'Sunday','ASD'),
(162,1015,'Monday','adsd'),
(163,1015,'Tuesday','asdasd'),
(164,1015,'Wednesday','asdsads'),
(165,1015,'Thursday','ads'),
(166,1015,'Friday','sadsad'),
(167,1015,'Saturday','sadsad'),
(168,1015,'Sunday','sad');

/*Table structure for table `training_plans` */

DROP TABLE IF EXISTS `training_plans`;

CREATE TABLE `training_plans` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `description` text,
  `goal` varchar(255) NOT NULL,
  `difficulty` varchar(255) NOT NULL,
  `is_suggested` tinyint(1) DEFAULT '0',
  `picture` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `training_plans_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1021 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `training_plans` */

insert  into `training_plans`(`id`,`user_id`,`name`,`description`,`goal`,`difficulty`,`is_suggested`,`picture`,`is_active`) values 
(2,9999,'Full Body Split - Beginner','A perfect plan for beginners focused on building foundational strength and conditioning with 5 training days and 2 rest days.','General Fitness','Beginner',1,NULL,0),
(1001,9999,'Push Pull Legs','A balanced split that targets muscle groups methodically across the week. Ideal for muscle growth.','Muscle Gain','Beginner',1,NULL,0),
(1006,4,'UPDATE','awdeawedwa','edwasda','Intermediate',0,NULL,0),
(1007,10001,'Full Body','Full body','Muscle Gain','Intermediate',0,NULL,0),
(1008,10002,'Upper Lower','Upper Lower','Muscle Gain','Beginner',0,NULL,1),
(1009,10003,'Upper Lower','Upper Lower','Muscle Gain','Intermediate',0,NULL,0),
(1012,10004,'afadfa','asdadasd','asfasdas','Beginner',0,NULL,0),
(1014,9999,'Fat Burn Circuit','High-intensity, full-body workouts focused on burning fat and improving cardiovascular health.','Weight Loss','Advanced',1,NULL,0),
(1015,10004,'dsad','asdsad','asdasd','Beginner',0,NULL,1);

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`),
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt7e7djp752sqn6w22i6ocqy6q` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user_role` */

insert  into `user_role`(`user_id`,`role_id`) values 
(4,1),
(1,2),
(2,2),
(5,2),
(6,2),
(10000,2),
(10001,2),
(10002,2),
(10003,2),
(10004,2),
(10005,2),
(10006,2),
(10007,2);

/*Table structure for table `user_saved_plans` */

DROP TABLE IF EXISTS `user_saved_plans`;

CREATE TABLE `user_saved_plans` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `plan_id` int NOT NULL,
  `saved_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_user` (`user_id`),
  KEY `fk_user_saved_plans_plan` (`plan_id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_saved_plans_plan` FOREIGN KEY (`plan_id`) REFERENCES `training_plans` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user_saved_plans` */

insert  into `user_saved_plans`(`id`,`user_id`,`plan_id`,`saved_at`,`is_active`) values 
(3,10000,1001,'2025-08-13 18:13:37',0),
(4,10001,2,'2025-08-20 21:42:47',0),
(5,10002,1001,'2025-08-19 20:30:26',0),
(6,10003,1001,'2025-08-19 20:58:21',0),
(7,10004,2,'2025-08-20 22:34:51',0),
(11,4,2,'2025-09-06 16:46:36',0),
(12,10006,1001,'2025-09-09 13:13:15',1),
(13,10007,1001,'2025-09-12 10:25:15',0);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `age` int DEFAULT NULL,
  `height` int DEFAULT NULL,
  `weight` int DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `users_chk_1` CHECK ((`age` between 10 and 120))
) ENGINE=InnoDB AUTO_INCREMENT=10008 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `users` */

insert  into `users`(`id`,`name`,`surname`,`email`,`password`,`age`,`height`,`weight`,`gender`) values 
(1,'Ana','Anić','ana@example.com','$2a$12$xEYjngqW4iktV0gQXLI3sOFqmv32FdhcDxGYTqEL72fR.siTA6Re6',28,165,60,'F'),
(2,'Ivan','Ivić','ivan@example.com','$2a$12$GSlVH0Nq9uBM461yQdxi6uh45mehl5Z2x7kWB9T/BiutP5tWnPqc.',32,180,82,'M'),
(4,'Domagoj','Čirko','domagoj.cirko@gmail.com','$2a$10$5Vz5dQE.dJd.qYKEx6uOB.d3k4YAU2DIdFsdX07iURD3ttk0IuEAq',21,185,70,'male'),
(5,'Iva','Ivić','iva@gmail.com','$2a$10$f4/KecSovR.p3n.TGWhE6u7mPzVPDMQ.nnIDMezijUVIDkJzvD/fm',NULL,NULL,NULL,'female'),
(6,'Sarah ','Čirko','cirkosarah@gmail.com','$2a$10$eYTlwk0Ke0X.7EJ2V4LD1OFXjieGfwvISNFecIx5Qc3W5x1BB5Pnu',NULL,NULL,NULL,'female'),
(9999,'System','Sys','system@fitflow.com','$2a$12$Q3f6dN2ok46hEl.REpV2ke1Wm0DEzJdWSroDy1WilPeKTZSvlsIfG',NULL,NULL,NULL,'male'),
(10000,'Luka','Lukić','luka.lukic@gmail.com','$2a$10$phWzvGUyGTt1Hp8uX848meKe7PtzzzvwFewzS7RRJh6yA7LuSWUE6',17,177,72,'male'),
(10001,'Lovro','Lovrić','lovro.lovric@gmail.com','$2a$10$LA.2lC9Dw.5QsoOMeDIkuONk04d/dPQlxFAgy4zpgczwfL0AZPwN.',23,185,120,'male'),
(10002,'Duje','Dujic','duje.dujic@gmail.com','$2a$10$9rLhpuzFHfx4Lt9eLBd3t.ig4HWU07EQz0PzFxFqkUFl1JCbr689K',23,178,100,'male'),
(10003,'Boro','Borić','boro.boric@gmail.com','$2a$10$Nx1ftIelOAbObR/QOK2Qm.mTjoJtQWHZTdIJQuFLXKckCZE8kU6Gq',21,178,75,'male'),
(10004,'Marko','Markić','marko.markic@gmail.com','$2a$10$sQOOVhTjG1cf1MOfUkAcO.ToPwkFoqHkUK4uW62xVxUUPoASllo8u',20,176,70,'male'),
(10005,'Laura','Sršić','laura@gmail.com','$2a$10$0Nw0G4z9esAz/y4iwnRRgOJC5NLrGPKpBdPXmpHH.BORHstKY2Ay6',NULL,NULL,NULL,'female'),
(10006,'Bero','Berić','bero@gmail.com','$2a$10$cQaypqqCqe0uZSxPNrf3xOBTfXaxdtvJF1ZEzNXd8YO/PktQzbUaK',NULL,NULL,NULL,'male'),
(10007,'tvz123','tvz123','tvz123@gmail.com','$2a$10$hovHcg4Y4.avPTz6VIDv3OPh4gmSk6NR8t7DMRzvzT1vmZgd1SZDK',21,185,70,'male');

/*Table structure for table `workout_logs` */

DROP TABLE IF EXISTS `workout_logs`;

CREATE TABLE `workout_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `training_plan_id` int NOT NULL,
  `training_day_id` int NOT NULL,
  `training_day_exercise_id` int NOT NULL,
  `exercise_id` int NOT NULL,
  `log_date` date NOT NULL,
  `planned_sets` int DEFAULT NULL,
  `planned_reps` int DEFAULT NULL,
  `planned_weight` int DEFAULT NULL,
  `actual_sets` int DEFAULT NULL,
  `actual_reps` int DEFAULT NULL,
  `actual_weight` int DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `training_plan_id` (`training_plan_id`),
  KEY `training_day_id` (`training_day_id`),
  KEY `training_day_exercise_id` (`training_day_exercise_id`),
  KEY `exercise_id` (`exercise_id`),
  CONSTRAINT `workout_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `workout_logs_ibfk_2` FOREIGN KEY (`training_plan_id`) REFERENCES `training_plans` (`id`) ON DELETE CASCADE,
  CONSTRAINT `workout_logs_ibfk_3` FOREIGN KEY (`training_day_id`) REFERENCES `training_days` (`id`) ON DELETE CASCADE,
  CONSTRAINT `workout_logs_ibfk_4` FOREIGN KEY (`training_day_exercise_id`) REFERENCES `training_day_exercises` (`id`) ON DELETE CASCADE,
  CONSTRAINT `workout_logs_ibfk_5` FOREIGN KEY (`exercise_id`) REFERENCES `exercises` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `workout_logs` */

insert  into `workout_logs`(`id`,`user_id`,`training_plan_id`,`training_day_id`,`training_day_exercise_id`,`exercise_id`,`log_date`,`planned_sets`,`planned_reps`,`planned_weight`,`actual_sets`,`actual_reps`,`actual_weight`,`notes`) values 
(5,10002,1008,58,123,8,'2025-08-19',3,10,80,3,9,82,'Good Training.'),
(10,10004,1012,90,132,33,'2025-08-23',3,10,100,3,9,90,'awdada'),
(12,10004,1012,90,132,33,'2025-08-23',NULL,NULL,NULL,NULL,NULL,NULL,'great'),
(13,10004,1012,90,132,33,'2025-08-23',3,10,110,3,8,110,'great'),
(18,10006,1001,142,186,13,'2025-09-09',4,10,80,4,8,82,'Good.'),
(19,10006,1001,142,187,14,'2025-09-09',3,12,25,3,12,25,'Okay'),
(20,10006,1001,142,188,15,'2025-09-09',3,12,30,3,10,35,'Great.'),
(21,10006,1001,142,189,6,'2025-09-09',3,8,10,3,6,10,'Okay.');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
