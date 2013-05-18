<?php
include "config.php";

$files = "CREATE TABLE `files` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`file_from` int(11) DEFAULT NULL,
		`file_to` int(11) DEFAULT NULL,
		`file_seen` int(11) DEFAULT NULL,
		`name` varchar(30) NOT NULL,
		`type` varchar(30) NOT NULL,
		`size` int(11) NOT NULL,
		`content` mediumblob NOT NULL,
		PRIMARY KEY (`id`)
		) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;";
mysql_query($files) or die(mysql_error());

$messages = "CREATE TABLE `messages` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`message_from` int(11) NOT NULL,
		`message_to` int(11) NOT NULL,
		`message_content` varchar(255) NOT NULL,
		`message_seen` int(11) NOT NULL,
		`is_file` int(11) NOT NULL,
		`file_id` int(11) DEFAULT NULL,
		PRIMARY KEY (`id`)
		) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
mysql_query($messages) or die(mysql_error());
?>