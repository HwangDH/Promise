<?php
	include './dbinfo.php';
	include './sendpush.php';
	
	$userphonenumber = $_POST['userphonenumber'];
	$phonecert = $_POST['phonecert'];
	$userpassword = $_POST['userpassword'];
	$username = $_POST['username'];
	$agreement1 = $_POST['agreement1'];
	$agreement2 = $_POST['agreement2'];
	$agreement3 = $_POST['agreement3'];
	$token = $_POST['token'];
	
	$con = mysqli_connect($host,$user,$pass,$dbname);
	
	if(mysqli_connect_error($con)){	
		echo "DB connect error : ". mysqli_connect_error();
	}
	else if(){
		$sql3 = "select userphonenumber from Puser_list where userphonenumber = $userpassword;";
		$r = mysqli_query($con, $sql);
		$res = mysqli_fetch_array($r);
		$resonse = array();
		
		if(sizeof($res) > 0){
			$respone["success"] = false;
			echo json_encode($response);	
		}
	}
	
	else{
		$sql="insert into Puser_list() values('".$userphonenumber."','".$phonecert."','".$userpassword."','".$username."','".$agreement1."','".$agreement2."','".$agreement3."','".$token."');";
		$sql2="CREATE TABLE Puser_$userphonenumber (id INT NOT NULL, userphonenumber VARCHAR(30) NOT NULL, otherphonenumber VARCHAR(30) NOT NULL , text VARCHAR(100) NOT NULL , endweekend Datetime NOT NULL, agreement INT(10) NOT NULL DEFAULT '0', status INT(10) NOT NULL, pid INT, PRIMARY KEY (id,userphonenumber, otherphonenumber));";
		
		if(mysqli_query($con,$sql) && mysqli_query($con, $sql2)){
			echo  "success";
		}
		else{
			echo "failed";
		}	
	}
	mysqli_close($con);
?>