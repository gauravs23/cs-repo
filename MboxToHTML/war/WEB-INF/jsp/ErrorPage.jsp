<%@ page isErrorPage="true" language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.io.IOException"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Oops...Error Occurred - Convert your mbox files into HTML</title>
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
#description{
	margin-left:20px;
}
</style>

</head>

<body>

<div id="main">
<div id="header">MBox To HTML Converter</div>
<div id="subheader">Access your mails easily in html format</div>
<div id="description">

<%
String errormsg="";
String exceptionType=(String)request.getAttribute("exceptionType"); 

if(exceptionType.equalsIgnoreCase("IOException") || exceptionType.equalsIgnoreCase("FileUploadException"))
	errormsg="There was a problem uploading the file.";
else if(exceptionType.equalsIgnoreCase("NullPointerException"))
	errormsg="The file you have uploaded is not a valid mbox file.";
else if(exceptionType.equalsIgnoreCase("IllegalSelectorException"))
	errormsg="There was a problem opening the stream for file upload.";
else if(exceptionType.equalsIgnoreCase("IllegalArgumentException"))
	errormsg="There was a problem opening the stream for file upload.";
else if(exceptionType.equals("Exception"))
	errormsg="The file you have uploaded is not a valid mbox file.";
%>



<br></br>
Oops....an error has occurred. <%=errormsg%>
<br>
</br>
Please try again here. <a href="/fileupload.jsp">Try Again</a> 



</div>
</div>

</body>
</html>