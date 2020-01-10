<?php
	function send_notification ($tokens, $data)
	{
		$url = 'https://fcm.googleapis.com/fcm/send';
		//어떤 형태의 data/notification payload를 사용할것인지에 따라 폰에서 알림의 방식이 달라 질 수 있다.
		$msg = array(
			'body' 	=> $data["body"],
			'id' => $data["id"],
			'otherphonenumber' => $data["otherphonenumber"],
			'text' => $data["text"],
			'endweekend' => $data["endweekend"],
			'restweek' => $data["restweek"],
			'status' => $data["status"],
			'pid' => $data["pid"]
			);
		
		//data payload로 보내서 앱이 백그라운드이든 포그라운드이든 무조건 알림이 떠도록 하자.
		$fields = array(
			'registration_ids' => $tokens,
			'data' => $msg
		);
		define('GOOGLE_API_KEY', 'AAAAOoKYsv4:APA91bG5DQhXO_gSqoshTAsB4fJq-M9w6lI5hLI733TiaODrVsNhP2LmupOLgBn3Hb6GYMKJWcRczko1zV7nJ_PT32o4X0SaiILxsCnsjy9W6gQZYoIMdDeCIihodJTc3gyLUa1u1Qp3');
		$headers = array(
		  'Authorization:key =' .GOOGLE_API_KEY,
		  'Content-Type: application/json'
		);
		
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
		
		$result = curl_exec($ch);           
		
		if ($result == FALSE) {
			die('Curl failed: ' . curl_error($ch));
		}
		//print_r ($msg);
		curl_close($ch);
		return $result;
	}
	
	include './dbinfo.php';
	
	$userphonenumber = $_POST['userphonenumber'];
	$otherphonenumber = $_POST['otherphonenumber'];
	$id = $_POST['id'];
	$pid = $_POST['pid'];
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
 
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "select token from Puser_list where userphonenumber = '$otherphonenumber';";
		$result = mysqli_query($con, $sql);
		$response = array();
		
		if($row = mysqli_fetch_row($result)){
			$token[] = $row[0];
			$message = "약속삭제요청";
			$sql2 = "SELECT A.id, A.otherphonenumber, A.text, A.endweekend as endweekend, A.endweekend - now() as restweek, A.status as status, A.pid FROM Puser_$userphonenumber as A, Puser_$otherphonenumber as B where A.otherphonenumber = $otherphonenumber and A.pid = $pid and B.id=$pid;";
			$result2 = mysqli_query($con, $sql2);
			$inputData = array();
			while($row = mysqli_fetch_row($result2)){
				$inputData = array("body"=>$message, "id"=>$row[0],"otherphonenumber"=>$row[1], "text"=>$row[2], "endweekend"=>$row[3], "restweek"=>$row[4], "status"=>$row[5], "pid"=>$row[6]);
			}
			//$inputData = array("body"=>$message, "body2"=>"ASd");
			$res = send_notification($token, $inputData);
			//echo $res;
			$response["success"]= true;
		}
		else{
			$response["success"]= false;
		}
	}
	echo json_encode($response);
	mysqli_close($con);
?>