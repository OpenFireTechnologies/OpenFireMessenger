<?php
//includes config, functions
//Establishes a database connection and defines the functions
include_once "./system/db_functions.php";
include_once './system/db_connect.php';
include_once './system/GCM.php';

$db = new DB_Functions();

if(!$db->checkAuth())
{
	$arr = array ('statuscode'=>101,'error'=>"Not authenticated");
	die(json_encode($arr));
}

if(isset($_POST['from']) && isset($_POST['to'])){

	//if file sending
	if(isset($_FILES['file']['size'])){
		if($_FILES['file']['size'] > 0)
		{
			//functions.php -> uploadFile()
			$db->uploadFile();
		}
		//if message sending
	} else if(isset($_POST['from']) && isset($_POST['to']) && isset($_POST['content'])){
		//functions.php -> sendMessage()
		$db->sendMessage();
		//if message receiving
	} else if (isset($_POST['from']) && isset($_POST['to'])){
		//functions.php -> getMessage()
		$db->getMessageOrFile();
		//no action matched, we die
	} else {
		$arr = array ('statuscode'=>-1,'error'=>"Couldn't understand Request");
		die(json_encode($arr));
	}
} else {
	$arr = array ('statuscode'=>-1,'error'=>"Couldn't understand Request");
	die(json_encode($arr));
}

?>
