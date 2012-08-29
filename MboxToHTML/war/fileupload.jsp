<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page errorPage="/WEB-INF/jsp/ErrorPage.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Convert your mbox files into HTML</title>
<style type="text/css">

body{
	font-family	:	Verdana, Geneva, Arial, sans-serif;
	font-size	:	14px;
	background-color:#F8ECE0;
	margin-left: auto;
	margin-right: auto;
	width:800px;
	
}
#main{
	background-image: url(/images/background.jpg);
	background-repeat: no-repeat;

	overflow:hidden;
	margin-left:	auto;
	margin-right: 	auto;
	margin-top:0px;
	height:600px;
}
#header{
	font-size:	200%;
	color:	#347C17;
	text-align:	center;
}
#subheader{
	text-align: center;
	font-style: italic;
}
.description{
	font-size: 120%;
	margin-left:auto;
	margin-right:auto;
	margin-top:50px;
	margin-left: 25px;
}
.uploadform{
	margin-top:20px;
	margin-left:175px;
}
.uploadform span{
	margin-left: 125px;
}
#note{
	font-size:12px;
}
</style>
<script type="text/javascript">

function validate(form){

	var filename=form.document.value;
	
	if(filename == null || filename == ""){
		alert("Please select a file.");
		return false;
	}
	return true;
}

</script>

</head>

<body>

<div id="main">
<div id="header">MBox To HTML Converter</div>
<div id="subheader">Access your mails easily in html format</div>

<div class="description">Upload your mbox format mail files and convert them
easily to easy-to-read HTML format. Use the below button to upload your mbox file
and let us do the heavy lifting. 
</div>

<form onsubmit="return validate(this);" class="uploadform" action="/fileuploadservlet" method="post" enctype="multipart/form-data">
Select File:&nbsp;&nbsp;&nbsp;<input id="doc" type="file" name="document" size="50" /><br></br>

<br></br><span><input type="submit" value="Convert Now"></input></span>
</form>
</div>
<div id='note'>
Note: If the output html file size exceeds 10 Mb, then multiple files will be generated. To change this value,please contact the application provider.
</div>
</body>
</html>