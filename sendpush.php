<?php
	function send_notification ($tokens, $data)
	{
		$url = 'https://fcm.googleapis.com/fcm/send';
		//어떤 형태의 data/notification payload를 사용할것인지에 따라 폰에서 알림의 방식이 달라 질 수 있다.
		$msg = array(
			'body' 	=> $data["body"]
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
		
		curl_close($ch);
		return $result;
	}
?>
		