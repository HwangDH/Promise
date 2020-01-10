<?php
	include './dbinfo.php';
	
	$userphonenumber = $_POST['userphonenumber'];
	$otherphonenumber = $_POST['otherphonenumber'];
	$id = $_POST['id'];
	$pid = $_POST['pid'];
	
	$con = mysqli_connect($host, $user, $pass, $dbname);
	$response = array();
	if(mysqli_connect_error($con)){
		echo "DB connect error : " . mysqli_connect_error();
	}
	else{
		$sql = "DELETE FROM Puser_$userphonenumber where id = $id;";
		//echo $sql;
		if(mysqli_query($con, $sql)){
			$sql2 = "DELETE FROM Puser_$otherphonenumber where id = $pid;"; 
			//echo $sql2;
			if(mysqli_query($con, $sql2)){
				$response["success"] = true;
			}
			else{
				$response["success"] = false;
			}
		}
		else{
			$response["success"] = false;
		}
	}
	echo json_encode($response);
	mysqli_close($con);
?>