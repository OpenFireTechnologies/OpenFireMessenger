<?php
//includes config, functions
//Establishes a database connection and defines the functions
include "system/config.php";
include "system/functions.php";

//if file sending
if(isset($_POST['from']) && isset($_POST['to']) && isset($_FILES['file']['size'])){
	if($_FILES['file']['size'] > 0)
	{
		//functions.php -> uploadFile()
		uploadFile();
	}
	//if message sending
} else if(isset($_POST['from']) && isset($_POST['to']) && isset($_POST['content'])){
	//functions.php -> sendMessage()
	sendMessage();
	//if message receiving
} else if (isset($_POST['from']) && isset($_POST['to'])){
	//functions.php -> getMessage()
	getMessage();
	//no action matched, we die
} else {die();
}

?>
