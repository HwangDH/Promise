<?php 
	include './dbinfo.php';
 
	$userphonenumber  = $_POST['userphonenumber'];
	$userpassword  = $_POST['userpassword'];
	
	$today = date("Y-m-d");
	//echo $today;
	$con = mysqli_connect($host,$user,$pass,$dbname);
 
	if(mysqli_connect_error($con)){		
		echo "DB connect error : ". mysqli_connect_error();
	}
	else{
		$sql = "SELECT userphonenumber, userpassword FROM Puser_list WHERE userphonenumber='".$userphonenumber."' and userpassword = '".$userpassword."'"; 
		$r = mysqli_query($con,$sql); 
		$res = mysqli_fetch_array($r);
		$response = array();
		//echo $sql;
		if(sizeof($res) > 0){
			$response["success"] = true;
			
			//$sql2 = "select id, endweekend, hour(endweekend), minute(endweekend), endweekend - now(), status from Puser_$userphonenumber where endweekend < now() ";
			//echo $sql2;
			$sql2 = "select id from Puser_$userphonenumber where endweekend < now();";
			$result = mysqli_query($con, $sql2);
			$result2 = mysqli_query($con, $sql2);
			$res2 = mysqli_fetch_array($result);
			
			$i = 0;
			if(sizeof($res2)>0){
				while($row = mysqli_fetch_array($result2)){
					$id[] = $row[0];
					$sql3 = "update Puser_$userphonenumber set status = '1' where id = '$id[$i]';";
					mysqli_query($con, $sql3);
					//echo $id[$i];
					$i++;
				}
			}
		}
		else {
			$response["success"] = false; 
		}
	}
	echo json_encode($response);		
	mysqli_close($con);
?>