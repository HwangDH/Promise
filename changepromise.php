<?php
	include './dbinfo.php';
	include './sendpush.php';

	$id = $_POST['id'];
	$userphonenumber = $_POST['userphonenumber'];
	$otherphonenumber = $_POST['otherphonenumber'];
	$endweekend = $_POST['endweekend'];
	$ptext = $_POST['text'];
	$pid = $_POST['pid'];
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
 
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "UPDATE Puser_$userphonenumber set endweekend = '$endweekend', text = '$ptext', agreement = '1' where id = $pid;";
		$sql2 = "UPDATE Puser_$otherphonenumber set endweekend = '$endweekend', text = '$ptext', agreement = '1' where id = $id;";
		
		if(mysqli_query($con, $sql)){
			if(mysqli_query($con, $sql2)){
				$sql3 = "select token from Puser_list where userphonenumber = '$otherphonenumber';";
				$result = mysqli_query($con, $sql3);
				echo $sql3;
				if($row = mysqli_fetch_array($result)){
					$token [] = $row[0];
					echo $token[0];
				}
				$message = "약속이 수정됬습니다.";
				$inputData = array("body" => $message);
				$res = send_notification($token, $inputData);
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