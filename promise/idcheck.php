<?php
	include './dbinfo.php';

	$userid  = $_GET['userid'];
	$userpassword  = $_GET['userpassword'];
	$con = mysqli_connect($host,$user,$pass,$dbname);
 
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "SELECT userid FROM Puser_list WHERE userid='".$userid."'"; 
		echo $sql;
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