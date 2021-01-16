<?php  
	$user = $_POST['user'];
	$file_path = "/var/www/html/UserUploadFile/".$user."/tmp/";    
	$file_path = $file_path.basename( $_FILES['file']['name']);
	
	if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {
		echo "success";
	} else{
		echo "fail";
	}
	chdir("UserUploadFile/$user");
	system("./do_tesseract.sh");
?>
