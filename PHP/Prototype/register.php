<?php

// response json
$json = array();

/**
 * Registering a user device
 * Store reg id in users table
*/
if (isset($_POST["name"]) && isset($_POST["email"]) && isset($_POST["regId"])) {
	$name = $_POST["name"];
	$email = $_POST["email"];
	$gcm_regid = $_POST["regId"]; // GCM Registration ID
	// Store user details in db
	include_once "./system/db_functions.php";
	include_once './system/db_connect.php';
	include_once './system/GCM.php';

	$db = new DB_Functions();
	$gcm = new GCM();

	$res = $db->storeUser($name, $email, $gcm_regid);
} else {
}
?>