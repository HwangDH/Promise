<?php
	include './dbinfo.php';
	include './sendpush.php';

	$userphonenumber = $_POST['userphonenumber'];
	$id = $_POST['id'];
	$otherphonenumber = $_POST['otherphonenumber'];
	
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
 
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "update Puser_$userphonenumber set agreement = '0' where id = '$id';";
		if(mysqli_query($con, $sql)){ 
			$state = "success";
		}
		else{
			$state = "failed";
		}
	}
	
	if($state = "success"){
		$sql2 = "select token from Puser_list where userphonenumber = '$otherphonenumber';";
		$result = mysqli_query($con, $sql2);
		$result2 = mysqli_query($con, $sql2);
		$res = mysqli_fetch_array($result);			
		$response = array();
		
		if(sizeof($res) > 0){ 
			if ($row = mysqli_fetch_row($result2)) {
				$token[] = $row[0];
				//echo $sql2;
				//echo $token[0];
			}
			$message = "약속승인이 취소됬습니다.";
			$inputData = array("body" => $message);
			$res2 = send_notification($token, $inputData);
			$response["success"] = true;	
		}
		else{
			$response["success"] = false;
		}
	}
	else{
		$response["success"] = false;
	}
	
	echo json_encode($response);
	mysqli_close($con);
?>