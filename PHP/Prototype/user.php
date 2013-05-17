<?php
//this file is used for signing up or logging in/out users.
include_once "./system/db_functions.php";
include_once './system/db_connect.php';
include_once './system/GCM.php';

$db = new DB_Functions();

if(isset($_POST['username']))
{
	$user = $db->sanitize($_POST['username']);

	if(isset($_POST['password']))
	{
		$pass = $db->sanitize($_POST['password']);

		if(isset($_POST['email'])){
			$email = $db->sanitize($_POST['email']);
			if(isset($_GET['login']))
			{
				$db->login($user, $pass, $email);
			}
			if(isset($_GET['signup']))
			{
				$db->signup($user, $pass, $email);
			}
		}
	} else
	if(isset($_POST['hash']))
	{
		$hash = $_POST['hash'];
		if(isset($_GET['logout']))
		{
			$db->logout($user, $hash);
		}
	}
}
?>