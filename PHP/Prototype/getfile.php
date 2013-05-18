<?php
//includes config, functions
//Establishes a database connection and defines the functions
include "system/config.php";
include "system/functions.php";

//executes getFile in functions.php
getFile($_GET['id'], $_GET['sender'], $_GET['receiver']);
?>