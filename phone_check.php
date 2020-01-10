<?php
	include './dbinfo.php';

	$userphonenumber = $_POST['userphonenumber'];
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
 
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "SELECT userphonenumber FROM Puser_list WHERE userphonenumber='".$userphonenumber."'"; 
		
		$r = mysqli_query($con,$sql); 
		
		$res = mysqli_fetch_array($r);
		
		$response = array();
 
		if(sizeof($res) > 0){
			$response["success"] = false;
		}
		else {
			$response["success"] = true; 
		}
		echo json_encode($response);		
		mysqli_close($con);
	}
?>