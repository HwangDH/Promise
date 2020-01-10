<?php
	include './dbinfo.php';

	$userphonenumber = $_GET['userphonenumber'];
	
	$con = mysqli_connect($host,$user,$pass,$dbname);

	if(mysqli_connect_error($con)){	
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "SELECT id, otherphonenumber, endweekend, pid FROM Puser_$userphonenumber;";
		$result = mysqli_query($con, $sql);
		$response = array();
		
		while($row = mysqli_fetch_row($result)){
			array_push($response, array("id"=>$row[0],"otherphonenumber"=>$row[1], "endweekend"=>$row[2], "pid"=>$row[3]));
		}
		
		echo json_encode(array("response"=>$response));
		mysqli_close($con);
	}
?>