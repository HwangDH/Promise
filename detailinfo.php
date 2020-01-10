<?php
	include './dbinfo.php';

	$userphonenumber = $_GET['userphonenumber'];
	$otherphonenumber = $_GET['otherphonenumber'];
	$id = $_GET['id'];
	$pid = $_GET['pid'];
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
	
	if(mysqli_connect_error($con)){	
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "SELECT A.id, A.otherphonenumber, A.text, A.endweekend as endweekend, A.endweekend - now() as restweek,  A.agreement, B.agreement as agreement1, A.status as status, A.pid FROM Puser_$userphonenumber as A, Puser_$otherphonenumber as B where A.otherphonenumber = $otherphonenumber and A.pid = $pid and B.id=$pid;";
		$result = mysqli_query($con, $sql);
		$response = array();
		//echo $sql;
		while($row = mysqli_fetch_row($result)){
			array_push($response, array("id"=>$row[0],"otherphonenumber"=>$row[1], "text"=>$row[2], "endweekend"=>$row[3], "restweek"=>$row[4], "agreement"=>$row[5], "agreement1"=>$row[6],"status"=>$row[7], "pid"=>$row[8]));
		}
		
		echo json_encode(array("response"=>$response));
		mysqli_close($con);
	}
?>