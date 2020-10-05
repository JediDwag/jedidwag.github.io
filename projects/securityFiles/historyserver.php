<html>
<title>Admissions | University of St. Thomas - Minnesota</title>
<head>

<link rel="shortcut icon" type="image/x-icon" href="nav_icon.png">

<style>



</style>
</head>
<body>

<center>
<br>
<p>
<a href="https://www.stthomas.edu/" target="blank">
<img src="st-thomas.jpg" alt="University of St. Thomas" height="281" width="500">
</a>
</p>

<?php

$valid = true;
$code = $_POST['code'];
$key = "gfuL436eT82K";

$codeArray = str_split($code);

foreach($codeArray as $char){
	if(strpos($key, $char) === false){
	$valid = false;
	break;
	}
}

if($valid){

	$cisc = strpos($code, "f");
	$physics = strpos($code, "L");
	$english = strpos($code, "4");
	$economics = strpos($code, "e");
	$history = strpos($code, "2");

	if($cisc === false && $physics === false && $english === false && $economics === false && $history === false){


?>

<div size="5" style="font-family: 'verdana'">
Time to pick your major. Choose wisely.<br>
<a href="https://www.stthomas.edu/admissions/">Click here to continue to enrollment.</a>
</div>

<?php

	}

	else{

?>

	<div size="5" style="font-family: 'verdana'">
	Have you considered the following majors?<br><br><br>
	</div>

<?php

		if($cisc != false){

?>

<div size="5" style="font-family: 'verdana'">
<a href="https://www.stthomas.edu/cisc/" target="blank">Computer and Informational Sciences</a>
<br><br>
</div>

<?php

	}

		if($physics != false){

?>

<div size="5" style="font-family: 'verdana'">
<a href="https://www.stthomas.edu/physics/" target="blank">Physics</a>
<br><br>
</div>

<?php

		}

		if($english != false){

?>

<div size="5" style="font-family: 'verdana'">
<a href="https://www.stthomas.edu/english/" target="blank">English</a>
<br><br>
</div>

<?php

		}

		if($economics != false){

?>

<div size="5" style="font-family: 'verdana'">
<a href="https://www.stthomas.edu/economics/" target="blank">Economics</a>
<br><br>
</div>

<?php

		}

		if($history != false){

?>

<div size="5" style="font-family: 'verdana'">
<a href="https://www.stthomas.edu/history/" target="blank">History</a>
<br><br>
</div>

<?php

		}

?>

<br><br><br>
<a href="https://www.stthomas.edu/admissions/" style="font-family: 'verdana'">Click here to continue to enrollment.</a>
</div>

<?php


	}

}

else{

?>

<div size="5" style="font-family: 'verdana'">
Invalid captcha.<br>
<a href="historyclient.html">Click here to try again.</a>
</div>


<?php

}

?>



</center>

</body>
</html>
