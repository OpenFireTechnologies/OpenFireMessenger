<?php
include_once "db_functions.php";

$db = new DB_Functions();

$prepare0 = "SET FOREIGN_KEY_CHECKS=0;";
mysql_query($prepare0) or die(mysql_error());
$prepare1 = "DROP TABLE IF EXISTS `contacts`;";
mysql_query($prepare1) or die(mysql_error());
$prepare2 = "DROP TABLE IF EXISTS `files`;";
mysql_query($prepare2) or die(mysql_error());
$prepare3 = "DROP TABLE IF EXISTS `messages`;";
mysql_query($prepare3) or die(mysql_error());
$prepare4 = "DROP TABLE IF EXISTS `users`;";
mysql_query($prepare4) or die(mysql_error());
$prepare5 = "DROP TABLE IF EXISTS `gcm_users`;";
mysql_query($prepare5) or die(mysql_error());

$files = "CREATE TABLE `files` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`file_from` varchar(255) DEFAULT NULL,
		`file_to` varchar(255) DEFAULT NULL,
		`file_seen` int(11) DEFAULT NULL,
		`name` varchar(30) NOT NULL,
		`type` varchar(30) NOT NULL,
		`size` int(11) NOT NULL,
		`content` mediumblob NOT NULL,
		PRIMARY KEY (`id`)
		) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;";
mysql_query($files) or die(mysql_error());

$messages = "CREATE TABLE `messages` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`message_from` varchar(255) NOT NULL,
		`message_to` varchar(255) NOT NULL,
		`message_content` varchar(255) NOT NULL,
		`message_seen` int(11) NOT NULL,
		`is_file` int(11) NOT NULL,
		`file_id` int(11) DEFAULT NULL,
		PRIMARY KEY (`id`)
		) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
mysql_query($messages) or die(mysql_error());

$users = "CREATE TABLE `users` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`username` varchar(50) NOT NULL,
		`password` varchar(50) NOT NULL,
		`email` varchar(50) NOT NULL,
		`logged_in` int(11) NOT NULL,
		`hash` varchar(50) NOT NULL,
		PRIMARY KEY (`id`)
		) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
mysql_query($users) or die(mysql_error());

$contacts = "CREATE TABLE `contacts` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`user1` varchar(50) NOT NULL,
		`user2` varchar(50) NOT NULL,
		PRIMARY KEY (`id`)
		) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
mysql_query($contacts) or die(mysql_error());

$gcm = "CREATE TABLE `gcm_users` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`gcm_regid` text,
		`name` varchar(50) NOT NULL,
		`email` varchar(255) NOT NULL,
		`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		PRIMARY KEY (`id`)
		) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;";
mysql_query($gcm) or die(mysql_error());

echo "All Setup!";
?>
