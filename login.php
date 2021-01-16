<?php
	$link = mysqli_connect("127.0.0.1", "1033025", "willy520");

	$db=mysqli_select_db($link,"test") or die("QQ");

	$sql = "SELECT * FROM user";
	$result = mysqli_query($link,$sql);
	
	while( $row = mysqli_fetch_assoc($result) ){ 
		$tmp[]=$row;
	} 
	
	echo json_encode(array("data"=>$tmp));
	mysqli_close($link);
?>
