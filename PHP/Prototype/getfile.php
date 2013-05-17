<?php
//includes config, functions
//Establishes a database connection and defines the functions
include_once "./system/db_functions.php";
include_once './system/db_connect.php';
include_once './system/GCM.php';

$db = new DB_Functions();

//executes getFile in functions.php
$db->getFile($_GET['id'], $_GET['sender'], $_GET['receiver']);
?>