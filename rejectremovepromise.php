<?php
	include './dbinfo.php';
	include './sendpush.php';
	
	$otherphonenumber = $_POST['otherphonenumber'];
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
 
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "select token from Puser_list where userphonenumber = '$otherphonenumber';";
		$result = mysqli_query($con, $sql);
		$response = array();
		
		if ($row = mysqli_fetch_row($result)) {
			$token[] = $row[0];
			//echo $sql2;
			//echo $token[0];
			$message = "상대가 약속삭제를 거절했습니다.";
			$inputData = array("body" => $message);
			$res2 = send_notification($token, $inputData);
			$response["success"] = true;	
		}
		else{
			$response["success"] = false;
		}
	}
	echo json_encode($response);
	mysqli_close($con);
?>