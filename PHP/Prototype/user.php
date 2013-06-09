<?php
//this file is used for signing up or logging in/out users.
include_once "system/config.php";
include_once "system/functions.php";
include_once "system/gateway.php";

if(isset($_POST['username']))
{
	$user = sanitize($_POST['username']);
	if(isset($_POST['password']))
	{
		$pass = sanitize($_POST['password']);

		if(isset($_GET['login']))
		{
			login($user, $pass);
		}

		if(isset($_POST['email'])){
			$email = $_POST['email'];
			if(isset($_GET['signup']))
			{
				signup($user, $pass, $email);
			}
		}
	} else
	if(isset($_POST['hash']))
	{
		$hash = $_POST['hash'];
		if(isset($_GET['logout']))
		{
			logout($user, $hash);
		}
	}
}
?>