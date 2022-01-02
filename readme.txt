swapna vakode
swapna.vakode@npci.org.in

Python login-register system

Flask commands:
    command#1: set FLASK_APP=main.py
    command#2: set FLASK_DEBUG=1
    command#3: flask run

MY SQL QUERIES:
    CREATE DATABASE IF NOT EXISTS `testingDb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
    USE `testingDb`;

    CREATE TABLE IF NOT EXISTS `accounts` (
	    `id` int NOT NULL AUTO_INCREMENT,
  	    `username` varchar(100) NOT NULL,
  	    `password` varchar(100) NOT NULL,
  	    `email` varchar(100) NOT NULL,
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

    INSERT INTO `accounts` (`id`, `username`, `password`, `email`) VALUES (1, 'swapna', 'swapna', 'swapna@test.com');