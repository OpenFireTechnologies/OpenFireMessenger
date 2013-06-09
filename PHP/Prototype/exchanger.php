<?php
//includes config, functions
//Establishes a database connection and defines the functions
include_once "system/config.php";
include_once "system/functions.php";

if(!checkAuth())
{
	die("101");
}

if(isset($_POST['from']) && isset($_POST['to'])){
	//check for contacts
	if(!checkContacts())
	{
		die("301");
	}

	//if file sending
	if(isset($_FILES['file']['size'])){
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
		getMessageOrFile();
		//no action matched, we die
	} else {
		die("-1");
	}
} else {
	die("-1");
}

?>
