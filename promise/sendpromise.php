<?php
	function id_count ($userphonenumber){
		$user = "scv0319";
		$pass = "jessica0319!";
		$host= "localhost";
		$dbname="scv0319";
		
		$con = mysqli_connect($host,$user,$pass,$dbname);
	
		if(mysqli_connect_error($con)){		
			echo "DB connect error : ". mysqli_connect_error();
		}
		else{
			$sql = "select id from Puser_$userphonenumber order by id DESC;";
			$result = mysqli_query($con, $sql);
			$result2 = mysqli_query($con, $sql);
			$res = mysqli_fetch_array($result);	
			if(sizeof($res) > 0){ 			
				if ($row = mysqli_fetch_row($result2)) {
					$count = $row[0]+1;
				}
				else{
					$count = "error";
				}
			}
			else{
				$count = 1;
			}
		}
		mysqli_close($con);
		return $count;
	}

	include './dbinfo.php';
	include './sendpush.php';
	
	$userphonenumber = $_POST['userphonenumber'];
	$otherphonenumber = $_POST['otherphonenumber'];
	$text = $_POST['text'];
	$endweekend = $_POST['endweekend'];
	$phour = $_POST['hour'];
	$pmin = $_POST['min'];
	$total = $endweekend.$phour.$pmin."00";
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
	
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
			
	else{
		$sql2 = "select token from Puser_list where userphonenumber = '$otherphonenumber';";
		$result = mysqli_query($con, $sql2); 
		$result2 = mysqli_query($con, $sql2); 
		$res = mysqli_fetch_array($result);			
		$response = array();
		
		if(sizeof($res) > 0){ 
			if ($row = mysqli_fetch_row($result2)) {
				$token[] = $row[0];
				//echo $token[0];
			}
			
			$count = id_count($userphonenumber);
			$sql = "insert into Puser_$userphonenumber( id, userphonenumber, otherphonenumber, text, endweekend, agreement, status) values('".$count."', '".$userphonenumber."','".$otherphonenumber."','".$text."','".$total."', '1', '0');";
			//echo $sql;
			if(mysqli_query($con,$sql)){
				$response["success"] = true;
			}
			else{
				$response["success"] = false;
				//echo $state;
			}

			$count2 = id_count($otherphonenumber);
			//echo $count2;
			
			$sql4 = "insert into Puser_$otherphonenumber( id, userphonenumber, otherphonenumber, text, endweekend, agreement, status, pid) values('".$count2."', '".$otherphonenumber."','".$userphonenumber."','".$text."','".$total."', '0','0', '".$count."');";
			
			if(mysqli_query($con, $sql4)){
				$sql3 = "update Puser_$userphonenumber set pid = $count2 where id=$count;";
				mysqli_query($con, $sql3); 
				$message = "약속이 도착했습니다.";
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
	}
	
	echo json_encode($response);		
	mysqli_close($con);
?>
















