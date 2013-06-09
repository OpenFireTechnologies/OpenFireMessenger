<?php
function generateHash()
{
	return sha1(md5(sha1(uniqid(rand(), TRUE))));
}

function login($user, $pass)
{
	$givenUser = $user;
	$givenPass = $pass;

	$query = "SELECT password FROM users WHERE username LIKE '$givenUser' LIMIT 1;";
	$result = mysql_query($query) or die(mysql_error());
	if(mysql_num_rows($result) > 0)
	{
		$datas = mysql_fetch_array($result);
		if(strcmp($datas['password'], $givenPass) == 0)
		{
			$hash = generateHash();

			$update = "UPDATE users SET logged_in = '1' WHERE username='$givenUser';";
			mysql_query($update) or die(mysql_error());
			$updateHash = "UPDATE users SET hash = '$hash' WHERE username='$givenUser';";
			mysql_query($updateHash) or die(mysql_error());
			die("0");
		}
	}
	die("102");
}

function signup($user, $pass, $email)
{
	$queryEmail = "SELECT * FROM users WHERE email LIKE '$email';";
	$resultEmail = mysql_query($queryEmail) or die(mysql_error());
	if(mysql_num_rows($resultEmail) > 0)
	{
		die("203");
	}

	$queryUser = "SELECT * FROM users WHERE username LIKE '$user';";
	$resultUser = mysql_query($queryUser) or die(mysql_error());
	if(mysql_num_rows($resultUser) > 0)
	{
		die("201");
	}

	$query = "INSERT INTO users(username, password, email, logged_in, hash) VALUES ('$user', '$pass', '$email', 0, '0')";
	mysql_query($query) or die(mysql_error());
	die("0");
}

function logout($user, $hash)
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
?>
