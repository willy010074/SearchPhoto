<?php
	$name = $_POST['name'];
	$passwd = $_POST['passwd'];
	$link = mysqli_connect("127.0.0.1","1033025","willy520") or die("GG");

	$db = mysqli_select_db($link,"test") or die("QQ");
	$sql = "INSERT INTO user (name,passwd) VALUES ('$name','$passwd')";
	mysqli_query($link,$sql);
	mysqli_close();
	
	mkdir("UserUploadFile/".$name, 0755,true);
	mkdir("UserUploadFile/".$name."/after_tesseract", 0755, true);
	mkdir("UserUploadFile/".$name."/tmp", 0755, true);
	mkdir("UserUploadFile/".$name."/image", 0755, true);
	system("cp do_tesseract.sh UserUploadFile/$name/do_tesseract.sh");
	system("cp downloadImg.php UserUploadFile/$name/downloadImg.php");
?>
