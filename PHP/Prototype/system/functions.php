<?php
//Sanitize Input
function sanitize($input)
{
	$input = mysql_real_escape_string($input);
	$output = htmlentities($input, ENT_QUOTES);
	return $output;
}

//File Upload
function uploadFile(){
	$fileName = $_FILES['file']['name'];
	$tmpName  = $_FILES['file']['tmp_name'];
	$fileSize = $_FILES['file']['size'];
	$fileType = $_FILES['file']['type'];
	$sender = $_POST['from'];
	$receiver = $_POST['to'];
	$fp      = fopen($tmpName, 'r');
	$content = fread($fp, filesize($tmpName));
	$content = addslashes($content);
	fclose($fp);

	$query = "INSERT INTO files (file_from, file_to, file_seen, name, size, type, content ) VALUES ('$sender', '$receiver', 0, '$fileName', '$fileSize', '$fileType', '$content')";
	mysql_query($query) or die(mysql_error());

	$getid = mysql_insert_id();

	$messages = "INSERT INTO messages (message_from, message_to, message_content, message_seen, is_file, file_id) VALUES ('$sender', '$receiver', 'file', 0, 1, '$getid')";
	mysql_query($messages) or die(mysql_error());
}

//function to get the File Content
function getFile($id, $sender, $receiver){
	$query = "SELECT id, name, type, size, content FROM files WHERE file_from = '$sender' AND file_to = '$receiver' AND file_seen = '0' AND id='$id' LIMIT 1;";
	$result = mysql_query($query) or die(mysql_error());
	if(!(mysql_num_rows($result) == 0))
	{
		list($id, $name, $type, $size, $content) =  mysql_fetch_array($result);

		$files = "SELECT name, type, size, content " .
				"FROM files WHERE id = '$id'";
		$result = mysql_query($files) or die(mysql_error());
		list($name, $type, $size, $content) = mysql_fetch_array($result);

		header("Content-length: $size");
		header("Content-type: $type");
		header("Content-Disposition: attachment; filename=$name");

		$update = "UPDATE files SET file_seen = '1' WHERE id='$id';";
		mysql_query($update) or die(mysql_error());

		echo $content;
	}
}

//function to get the Name of the sent File
function getName($id, $sender, $receiver){
	$query = "SELECT name FROM files WHERE file_from = '$sender' AND file_to = '$receiver' AND file_seen = '0' AND id='$id' LIMIT 1;";
	$result = mysql_query($query) or die(mysql_error());
	if(!(mysql_num_rows($result) == 0))
	{
		list($name) =  mysql_fetch_array($result);
		return $name;
	}
}

//Stores sent Messages in Database to deliver to the Target User
function sendMessage(){
	$sender = $_POST['from'];
	$receiver = $_POST['to'];
	$content = $_POST['content'];
	$messages = "INSERT INTO messages (message_from, message_to, message_content, message_seen, is_file) VALUES ('$sender', '$receiver', '$content', 0, 0)";
	mysql_query($messages) or die(mysql_error());
}

//Returns unseen Messages or Files
function getMessageOrFile(){
	$sender = $_POST['from'];
	$receiver = $_POST['to'];

	$query = "SELECT id, message_content, is_file, file_id FROM messages WHERE message_from = '$sender' AND message_to = '$receiver' AND message_seen = '0';";
	$result = mysql_query($query) or die(mysql_error());
	if(!(mysql_num_rows($result) == 0))
	{
		while(list($id, $content, $is_file, $file_id) = mysql_fetch_array($result))
		{
			if($is_file==1){
				echo '<a href="./getfile.php?id='.$file_id.'&receiver='.$receiver.'&sender='.$sender.'">'.getName($file_id, $sender, $receiver).'</a>';
			} else {
				echo $content . '<br>';
			}
			$update = "UPDATE messages SET message_seen = '1' WHERE id='$id';";
			mysql_query($update) or die(mysql_error());
		}
	}
}

//Returns TRUE if authenticated, FALSE if not
function checkAuth()
{
	if(isset($_POST['username']) && isset($_POST['hash']))
	{
		$givenHash = sanitize($_POST['hash']);
		$givenUser = sanitize($_POST['username']);

		$query = "SELECT hash FROM users WHERE username LIKE '$givenUser' LIMIT 1;";
		$result = mysql_query($query) or die(mysql_error());
		if(mysql_num_rows($result) > 0)
		{
			$datas = mysql_fetch_array($result);
			if(strcmp($datas['hash'], $givenHash) == 0)
			{
				return TRUE;
			}
		}
	}
	return FALSE;
}

//Returns TRUE, if the users have themselves in the contacts list, FALSE if not
function checkContacts()
{
	if(isset($_POST['from']) && isset($_POST['to']))
	{
		$user1 = sanitize($_POST['from']);
		$user2 = sanitize($_POST['to']);

		$que = mysql_query("SELECT id FROM contacts ORDER BY id DESC LIMIT 1");
		if(mysql_num_rows($que) > 0)
		{
			$datas = mysql_fetch_array($que);
			$maxID = $datas['id'];
		}

		for($id = 0; $id <= $maxID; $id++)
		{
			$query1 = "SELECT user2 FROM contacts WHERE user1 LIKE '$user1' AND id LIKE '$id';";
			$result1 = mysql_query($query1) or die(mysql_error());
			if(mysql_num_rows($result1) > 0)
			{
				$datas = mysql_fetch_array($result1);
				// if user1 has user2 in his list
				if(!strcmp($datas['user2'], $user2))
				{
					$query1 = "SELECT user1 FROM contacts WHERE user2 LIKE '$user1' AND id LIKE '$id';";
					$result1 = mysql_query($query1) or die(mysql_error());
					if(mysql_num_rows($result1) > 0)
					{
						$datas = mysql_fetch_array($result1);
						// if user2 has user1 in his list
						if(!strcmp($datas['user1'], $user2))
						{
							return TRUE;
						}
					}
				}
			}
		}
		return FALSE;
	}
}
?>
