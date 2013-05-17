<?php

class DB_Functions {

	private $db;

	//put your code here
	// constructor
	function __construct() {
		// connecting to database
		$this->db = new DB_Connect();
		$this->db->connect();
		$this->gcm = new GCM();
	}

	// destructor
	function __destruct() {
		 
	}

	/**
	 * Storing new user
	 * returns user details
	 */
	public function storeUser($name, $email, $gcm_regid) {
		// insert user into database
		$result = mysql_query("INSERT INTO gcm_users(name, email, gcm_regid, created_at) VALUES('$name', '$email', '$gcm_regid', NOW())");
		// check for successful store
		if ($result) {
			// get user details
			$id = mysql_insert_id(); // last inserted id
			$result = mysql_query("SELECT * FROM gcm_users WHERE id = $id") or die(mysql_error());
			// return user details
			if (mysql_num_rows($result) > 0) {
				return mysql_fetch_array($result);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Getting all users
	 */
	public function getAllUsers() {
		$result = mysql_query("select * FROM gcm_users");
		return $result;
	}

	public function signup($user, $pass, $email)
	{
		$queryEmail = "SELECT * FROM users WHERE email LIKE '$email';";
		$resultEmail = mysql_query($queryEmail) or die(mysql_error());
		if(mysql_num_rows($resultEmail) > 0)
		{
			$arr = array ('statuscode'=>203,'error'=>"Email taken");
			die(json_encode($arr));
		}

		$queryUser = "SELECT * FROM users WHERE username LIKE '$user';";
		$resultUser = mysql_query($queryUser) or die(mysql_error());
		if(mysql_num_rows($resultUser) > 0)
		{
			$arr = array ('statuscode'=>201,'error'=>"Username taken");
			die(json_encode($arr));
		}

		$query = "INSERT INTO users(username, password, email, logged_in, hash) VALUES ('$user', '$pass', '$email', 0, '0')";
		mysql_query($query) or die(mysql_error());
		$hash = sha1(md5(sha1(uniqid(rand(), TRUE))));
		$update = "UPDATE users SET logged_in = '1' WHERE username='$user';";
		mysql_query($update) or die(mysql_error());
		$updateHash = "UPDATE users SET hash = '$hash' WHERE username='$user';";
		mysql_query($updateHash) or die(mysql_error());
		$arr = array ('statuscode'=>0,'hash'=>$hash);
		die(json_encode($arr));
	}

	public function login($user, $pass, $email)
	{
		$givenUser = $user;
		$givenPass = $pass;
		$givenEmail = $email;

		$query = "SELECT password FROM users WHERE username LIKE '$givenUser' AND email LIKE '$givenEmail' LIMIT 1;";
		$result = mysql_query($query) or die(mysql_error());
		if(mysql_num_rows($result) > 0)
		{
			$datas = mysql_fetch_array($result);
			if(strcmp($datas['password'], $givenPass) == 0)
			{
				$hash = sha1(md5(sha1(uniqid(rand(), TRUE))));

				$update = "UPDATE users SET logged_in = '1' WHERE username='$givenUser';";
				mysql_query($update) or die(mysql_error());
				$updateHash = "UPDATE users SET hash = '$hash' WHERE username='$givenUser';";
				mysql_query($updateHash) or die(mysql_error());
				$arr = array ('statuscode'=>0,'hash'=>$hash);
				die(json_encode($arr));
			}
		}
		$arr = array ('statuscode'=>102,'error'=>"Wrong Credentials");
		die(json_encode($arr));
	}

	public function logout($user, $hash)
	{
		$givenUser = $user;

		$query = "SELECT * FROM users WHERE username LIKE '$givenUser' AND logged_in = 1 AND hash = '$hash';";
		$result = mysql_query($query) or die(mysql_error());
		if(mysql_num_rows($result) > 0)
		{
			$datas = mysql_fetch_array($result);
			$update = "UPDATE users SET logged_in = '' WHERE username='$givenUser';";
			mysql_query($update) or die(mysql_error());
			$nullhash = "UPDATE users SET hash = '' WHERE username='$givenUser';";
			mysql_query($nullhash) or die(mysql_error());
			die("0");
		}
		else
		{
			die("-1");
		}
	}

	//Sanitize Input
	public function sanitize($input)
	{
		$input = mysql_real_escape_string($input);
		$output = htmlentities($input, ENT_QUOTES);
		return $output;
	}

	//File Upload
	public function uploadFile(){
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

		$result = mysql_query("select gcm_regid FROM gcm_users WHERE email LIKE '$receiver' LIMIT 1;");
		list($regId) = mysql_fetch_array($result);

		$registation_ids = array($regId);
		$message = array("title" => $sender, "content" => $fileName);

		$result = $this->gcm->send_notification($registation_ids, $message);
	}

	//function to get the File Content
	public function getFile($id, $sender, $receiver){
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
	public function getName($id, $sender, $receiver){
		$query = "SELECT name FROM files WHERE file_from = '$sender' AND file_to = '$receiver' AND file_seen = '0' AND id='$id' LIMIT 1;";
		$result = mysql_query($query) or die(mysql_error());
		if(!(mysql_num_rows($result) == 0))
		{
			list($name) =  mysql_fetch_array($result);
			return $name;
		}
	}

	//Stores sent Messages in Database to deliver to the Target User
	public function sendMessage(){
		$sender = $_POST['from'];
		$receiver = $_POST['to'];
		$content = $_POST['content'];
		if(isset($_POST['time'])){
		$time = $_POST['time'];
		} else {
		$time = "! ".date("H:i");
		}
		$messages = "INSERT INTO messages (message_from, message_to, message_content, message_seen, is_file) VALUES ('$sender', '$receiver', '$content', 0, 0)";
		mysql_query($messages) or die(mysql_error());

		$result = mysql_query("select gcm_regid FROM gcm_users WHERE email LIKE '$receiver' LIMIT 1;");
		list($regId) = mysql_fetch_array($result);

		$registation_ids = array($regId);
		$message = array("title" => $sender, "content" => $content, "time" => $time);

		$result = $this->gcm->send_notification($registation_ids, $message);
	}

	//Returns unseen Messages or Files
	public function getMessageOrFile(){
		$sender = $_POST['from'];
		$receiver = $_POST['to'];

		$query = "SELECT id, message_content, is_file, file_id FROM messages WHERE message_from = '$receiver' AND message_to = '$sender' AND message_seen = '0';";
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
	public function checkAuth()
	{
		if(isset($_POST['from']) && isset($_POST['hash']))
		{
			$givenHash = $this->sanitize($_POST['hash']);
			$givenUser = $this->sanitize($_POST['from']);

			$query = "SELECT hash FROM users WHERE email LIKE '$givenUser' LIMIT 1;";
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

}

?>