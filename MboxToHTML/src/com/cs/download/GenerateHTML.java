/*	@GenerateHTML.java
*	Created By :	Gaurav Sachdeva
*	This servlet is the final step in the request flow before the response is returned to the user. This
*	servlet is responsible for generating the html files (maybe 1 or more, dependding upon the split size
*	as required by a user), and then put those html files in the zip archive before sending the zip file 
*	to the user as the response.
*	
*	The servlet gets the msgList from the session variable as set in the ParseMob.java class and loop through 
*	each message object and create the structure of the HTML output file. There are different methods define in
*	servlet that helps in generating the output HTML structure. The code logic has been written in such a way
*	if there are more than one HTML file, each file will  have all the styles/scripts required and each HTML 
*	will begin with number 1 so there is no dependency between output html files.
*	All the javascript and styles have been embedded as the part of the HTML structure code as this was one of
*	requirements to not to link to any external javascript/css file.
*
*	The servlet finally gives back all the HTML files zipped in an archive folder to the user for download.
*/
package com.cs.download;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.w3c.tidy.Tidy;
import com.cs.mail.Message;
import com.cs.mail.Message.Attachment;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

public class GenerateHTML extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException,ServletException
	{
		doPost(request, response);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException,ServletException
	{
	try{	
			
		//Here the file split size is set. By default, as per the requirements it should be 10 mb as set below.
		int fileSplitSize=10000*1024;
		
	
		
		//keeping track of the currentFileSize to use for file splitting
		int currentFileSize=0;
		
		//total number of html files to be generated
		int numOfFiles=1;
		
		//The total size of all the HTML files
		int totalFileSize=0;
		
		/*This list will contain the StringBuffer objects and each StringBuffer will have the HTML content that has
		* to be written in the HTML output files.
		*/
		List<StringBuffer> splittedFilesList=new ArrayList<StringBuffer>();
		
		//Getting the session
		HttpSession session=request.getSession();
	
		//Retreiving message objects ArrayList from the session
		List<Message> msgList=(List<Message>)session.getAttribute("msgListS");
		
		
		//From here the HTML content generation code logic starts
		
		//StringBuffer used to store the HTML content that will be written to HTML file in later part of the code
		StringBuffer sb=new StringBuffer();
		
		sb.append("<html><head>" +
				"<title>Mbox mails in HTML Format</title>");
				
		
		currentFileSize=sb.length();
		
		//Keeping track of totalMsgCount and the current msgCount in the loop
		int msgCount=0;
		int totalMsgCount=0;
		
		/*Another StringBuffer that will contain only the mailBody String. This has been defined seperately to aid 
		* dynamic generation of javascript code for each HTML file.
		*/
		StringBuffer mbsb=new StringBuffer();
		
		//Looping through the message list and adding each message to the StirngBuffer
		for(Message m:msgList){
			
			totalMsgCount++;
			msgCount++;
			
			mbsb.append("<div id='mail"+msgCount+"' class='mails'>");
			
			mbsb.append("<div id='mailcount'>");
			mbsb.append("Mail - "+msgCount);
			mbsb.append("</div>");
			
			mbsb.append("<div id='mailbody'>");
			mbsb.append("<div id='mailheader'>");
			
			mbsb.append(m.getSubject().replace("<", "&lt;").replace(">","&gt;"));
			mbsb.append("<br>");
			mbsb.append("<br>");
			
			mbsb.append("<table>");
			
			mbsb.append("<tr>");
			mbsb.append("<td class='first'>");
			mbsb.append("From	:");
			mbsb.append("</td>");
			mbsb.append("<td class='second'>");
			mbsb.append(m.getSender().replace("<", "&lt;").replace(">","&gt;"));
			mbsb.append("</td>");
			mbsb.append("</tr>");
			
			mbsb.append("<tr>");
			mbsb.append("<td class='first'>");
			mbsb.append("To	:");
			mbsb.append("</td>");
			mbsb.append("<td class='second'>");
			
			String[] toReceiverArray=m.getToReceivers();
			for(int i=0; i<toReceiverArray.length;i++){
				mbsb.append(toReceiverArray[i].replace("<", "&lt;").replace(">","&gt;"));
				if(i!=toReceiverArray.length-1)
					mbsb.append(" , ");
			}
			mbsb.append("</td>");
			mbsb.append("</tr>");
			
			
			String[] ccReceiverArray=m.getCcReceivers();
			if(ccReceiverArray !=null && ccReceiverArray.length > 0){
				mbsb.append("<tr>");
				mbsb.append("<td class='first'>");
				
				mbsb.append("CC	:");
				mbsb.append("</td>");
				mbsb.append("<td class='second'>");
				
				for(int i=0; i<ccReceiverArray.length;i++){
					mbsb.append(ccReceiverArray[i].replace("<", "&lt;").replace(">","&gt;"));
					if(i!=ccReceiverArray.length-1)
						mbsb.append(" , ");
				}
				mbsb.append("</td>");
				mbsb.append("</tr>");
			}
			
			
			mbsb.append("<tr>");
			mbsb.append("<td class='first'>");
			mbsb.append("Date	:	");
			mbsb.append("</td>");
			mbsb.append("<td class='second'>");
			mbsb.append(m.getDate());
			mbsb.append("</td>");
			mbsb.append("</tr>");
			
			if(m.getAttachmentCount() > 0){
				mbsb.append("<tr>");
				mbsb.append("<td class='first'>");
				mbsb.append("Attachments	:	");
				mbsb.append("</td>");
				mbsb.append("<td class='second'>");
				
				List<Attachment> attachmentsList=m.getAttachmentList();
				int tempcount=0;
				for(Message.Attachment ma: attachmentsList){
					
					tempcount++;
					System.out.print("Attachment	: "+ma.getAttachmentName());
					System.out.print(" Attachment size:"+ma.getAttachmentSizeInKb());
					System.out.print(" Attachment length:"+ma.getAttachmentContent().length());
					System.out.print(" Attachment Encoding:"+ma.getAttachmentEncoding()+"\n");
					
					if(ma.getAttachmentName() != null){
						System.out.println("@test : "+ma.getAttachmentName());
						mbsb.append(ma.getAttachmentName()+"&nbsp;");
					
					
					if(ma.getAttachmentSizeInKb() < 1024){
						mbsb.append("("+String.format("%.2f", ma.getAttachmentSizeInKb())+ "KB)");
					}else{
						mbsb.append("("+String.format("%.2f", ma.getAttachmentSizeInKb()/1024)+ "MB)");
					}
					
					mbsb.append("&nbsp;["+ma.getAttachmentType().replace("<", "&lt;").replace(">","&gt;")+"]");
					}
					
					if(!(tempcount==m.getAttachmentCount())){
						mbsb.append(",&nbsp;");
					}
					
				}
				mbsb.append("</td>");
				mbsb.append("</tr>");
					
			}
	
			mbsb.append("</table>");
			mbsb.append("</div>");
	
			
			
			mbsb.append("<div id='mailcontent'>");
			mbsb.append("<hr/>");
			
			String msgBody=m.getBody();
			int tempIndex=0;
			if((tempIndex=msgBody.indexOf("<body")) > -1){
				msgBody=msgBody.substring(tempIndex+5);
				if((tempIndex=msgBody.indexOf(">")) > -1)
					msgBody=msgBody.substring(tempIndex+1);
			}
			
			if((tempIndex=msgBody.indexOf("</body")) > -1){
				msgBody=msgBody.substring(0,tempIndex);
			}
			
			mbsb.append(msgBody);
			mbsb.append("<br/>");
	
			mbsb.append("</div>");
			mbsb.append("</div>");
			mbsb.append("</div>");
			
			
			
			currentFileSize=sb.length()+mbsb.length();
			System.out.println("After msg - "+msgCount+" , currentfilesize is : "+currentFileSize);
	
			/*Code logic for file splitting in case the current file size exceeds the file split size as specifies
			* at the top of this servlet.
			*/
			if( fileSplitSize > 0 &&	(currentFileSize >= fileSplitSize)){
				System.out.println("inside file split loop");
				totalFileSize=totalFileSize+currentFileSize;
				currentFileSize=0;
				
				int totalPages=msgCount;
				
				insertHtmlScript(sb,totalPages);
				insertHtmlStyle(sb);
				sb.append("</head>");
				sb.append("<body onload='hideShow(1,"+totalPages+");'>");
				sb.append("<div id='main'>");
				insertHtmlPageHeader(sb);
								
				insertHtmlPagination(sb,totalPages);
				sb.append("<div id='allmails'>");
				sb.append(mbsb);
				sb.append("</div>");
				sb.append("</div>");
				
				
				sb.append("</body>");
				sb.append("</html>");
			
				
				
				
				splittedFilesList.add(sb);
				if(totalMsgCount != msgList.size()){
					numOfFiles++;
				}
				
				
				sb=new StringBuffer();
				mbsb=new StringBuffer();
				
				msgCount=0;
				
				System.out.println("sb after delete is : "+sb.toString());
				System.out.println("mbsb after delete is : "+mbsb.toString());
				sb.append("<html><head>" +
				"<title>Mbox mails in HTML Format</title>");
				
			}
			
		}
		
		System.out.println("total files to create : "+numOfFiles);
		
		if(numOfFiles != splittedFilesList.size()){
		
			int totalPages=msgCount;
			
			insertHtmlScript(sb,totalPages);
			insertHtmlStyle(sb);
			sb.append("</head>");
			sb.append("<body onload='hideShow(1,"+totalPages+");'>");
			sb.append("<div id='main'>");
			insertHtmlPageHeader(sb);
		
			
			insertHtmlPagination(sb,totalPages);
			sb.append("<div id='allmails'>");
			sb.append(mbsb);
			sb.append("</div>");
			sb.append("</div>");
			
			
			
			//make sure all div tags have proper ending tags
			Pattern pattern=Pattern.compile("(<div)");
			Matcher matcher=pattern.matcher(sb.toString());
			int startTagCount=0;
			while(matcher.find())
				startTagCount++;
			
			Pattern pattern1=Pattern.compile("(</div)");
			Matcher matcher1=pattern.matcher(sb.toString());
			int endTagCount=0;
			while(matcher1.find())
				endTagCount++;
			
			System.out.println("For Message-"+msgCount+" [start tags = "+startTagCount+" , end tags = "+endTagCount+"]");
			
		
			
			
			sb.append("</body>");
			sb.append("</html>");
			
			splittedFilesList.add(sb);
		}
		
		/*For loop ends here. By now we have the content taht need to be added to the output HTML files in the List of
		* StringBuffer. The size of the StringBuffer list will also indicate how many HTML files need to be created.
		*/

		//Setting the response type.
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=mails1.zip");
		
		//Declaring the output stream variables
		OutputStream os = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;		
	
		
		os= response.getOutputStream();
		bos=new BufferedOutputStream(os);
		zos=new ZipOutputStream(bos);
	
		for(int i=1;i<=splittedFilesList.size();i++){
			
			String allContent=splittedFilesList.get(i-1).toString();
			do{	
				Tidy tidy=new Tidy();
				tidy.setXHTML(true);
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				tidy.parse(new StringReader(allContent),baos);
				
				if(tidy.getParseErrors() > 0)
					break;
				allContent=baos.toString();
			}while(false);	
			
			/*Creating HTML giles in the GAE blobstore. These files are first created on the blobstore and then 
			* to put them in the zip archive
			*/
			
			FileService fileService=FileServiceFactory.getFileService();
			AppEngineFile file=fileService.createNewBlobFile("text/html");
			boolean lock=true;
			FileWriteChannel writeChannel=fileService.openWriteChannel(file, lock);
			PrintWriter out1=new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
			
			out1.println(allContent);
			out1.close();
			writeChannel.closeFinally();
			FileReadChannel readChannel=fileService.openReadChannel(file, false);

			/*Fetching the HTML files created in blobstore in above step and dding them to the zipoutputstream
			*/
			try{
									
				InputStream instream=Channels.newInputStream(readChannel);
				ZipEntry ze=null;
				ze=new ZipEntry("mails_"+i+".html");
				ze.setComment("Html file 1");
				
				zos.putNextEntry(ze);
				
				zos.write(IOUtils.toByteArray(instream));
				
				zos.flush();
				zos.closeEntry();
				
				
				
			}catch(IOException e){
				e.printStackTrace();
				request.setAttribute("exceptionType", "IOException");
				request.getRequestDispatcher("/WEB-INF/jsp/ErrorPage.jsp").forward(request, response);
				
			}
			
			readChannel.close();

		}//loop should end here
		
		if(zos!=null){
			zos.finish();
			zos.flush();
			zos.close();
			IOUtils.closeQuietly(zos);
		}
		IOUtils.closeQuietly(bos);
		IOUtils.closeQuietly(os);

		
	}catch(Exception ex){
		ex.printStackTrace();
		request.setAttribute("exceptionType", "Exception");
		request.getRequestDispatcher("/WEB-INF/jsp/ErrorPage.jsp").forward(request, response);
		//ex.printStackTrace(response.getWriter());
	}
	
	}

	/*Helper method for adding HTMLPageHeader content to the StringBuffer. It takes the StringBuffer reference
	* then adds the content to it and returns back the same reference
	*/
	private StringBuffer insertHtmlPageHeader(StringBuffer sb){
		
		sb.append("<div id='pageheader'>MailBox" +
				"<br/>	" +
				"<span>All your mbox mails in easy-to-read format</span>" +
				"</div>");

		return sb;
	}
	
	
	/*Helper method for adding css style content to the StringBuffer. It takes the StringBuffer reference
	* then adds the content to it and returns back the same reference
	*/
	private StringBuffer insertHtmlStyle(StringBuffer sb){
		
		sb.append("<style type='text/css'>" +
				"body{"+
					"background-color	:	#C8E2EB;" +
					"width				:	1000px;" +
					"margin				:	auto;"+
				"}"+	
				"#main{"+
					"font-family	:	sans-serif;"+
					"font-size		:	14px;"+
					"width			:	auto;"+
					"height			:	100%;"+
				"}"+
				"#pageheader{"+
					"font-size		:	26px;"+
					"text-align		:	center;"+
					"color			: 	#455560;"+
					"font-weight		:	bold;"+
					"border-bottom	:	2px solid #008000;"+
				"}"+
				"#pageheader span{"+
					"font-size			:	16px;"+
					"text-align			:	center;"+
				"}"+
				"#pagination{"+
					"margin-top:10px;"+
					"font-size	:	12px;"+
					"text-align	:	right;"+
				"}"+
				"#pagination a{"+
					"font-style:	italic;"+
				"}"+	
				"#mailcount{"+
					"margin-top:	10px;"+
					"margin-bottom:	10px;"+
					"font-size:	18px;"+
					"background-color:	#456179;"+
					"color:white;"+
					"padding: 5px;"+
					"font-weight:	bold;"+
				"}"+

				"#mailbody{"+
					"margin:10px;"+
					"border: 2px solid black;"+
					"background-color:	white;"+
				"}"+

				"#mailheader{" +
					"background-color: #D2FDD2;"+
					"font-size:	16px;"+
					"padding :10px;"+
					"font-weight:bold;"+
					"color:blue;"+
				"}"+

				"#mailheader table{"+
					"border-collapse:	collapse;"+
				"}"+
				"#mailheader .first{" +
					"white-space: nowrap;"+
					"font-size:14px;"+
					"text-align:right;"+
					"font-weight:bold;"+
					"color: grey;"+
				"}"+

				"#mailheader .second{"+
					"padding-left:10px;"+
					"font-size:14px;"+
					"font-weight: bold;"+
					"height:25px;" +
					"color:	black;"+
				"}" +
				"#mailheader td{" +
					"vertical-align: top; " +
				"}"+

				"#mailcontent{"+
					"padding:10px;" +
					"overflow-x:auto;"+
				"}"+
				"#mailcontent span#mailcontentspan{"+
					"font-size:16px;"+
					"font-weight:bold;"+
				"}"+
				"#mailcontent hr{"+
					"height: 5px;"+
					"background-color:black;"+
				"}"+
				"</style>");
		
		return sb;
	}

	
	/*Helper method for adding javascript logic content to the StringBuffer. It takes the StringBuffer reference
	* and the total number messages in the html file (total pages basically as the html file has pagination support 
	* for each message)then adds the content to it and returns back the same StringBuffer reference.
	*/
	private StringBuffer insertHtmlScript(StringBuffer sb, int totalPages){
		
		sb.append("<script type='text/javascript'>");
	
		sb.append("var current=0;"+
			"var prev=0;"+
			"var next=0;"+
			"var totalCount="+totalPages+";"+
	
			"function hideShow(num,count){"+
				"if(num==1){"+
					"prev=1;"+
				"}else{"+
					"prev=num-1;"+
				"}"+
				"next=num+1;"+
				"current=num;"+
				"hideAll(count);"+
				"document.getElementById('mail'+current).style.display='block';"+
			"}"+
			"function hideAll(count)"+
			"{"+
				"totalCount=count;"+
				"for(var i=1;i <=totalCount;i++){"+
					"document.getElementById('mail'+i).style.display='none';"+
				"}"+
			"}"+
			"function showAll(count){"+
				"for(var i=1;i <=count;i++){"+
					"document.getElementById('mail'+i).style.display='block';"+
				"}"+
			"}"+
			"function showNext(){"+
				"hideAll(totalCount);"+
				"if(current == 0 && next == 0 && prev == 0){"+
					"prev=1;"+
					"current=2;"+
					"next=3;"+
				"}"+
				"else{"+
					"if( current != totalCount){"+
						"prev=current;"+
					"}"+
					"current=next;"+
					"if( current != totalCount){"+
						"next=next+1;"+
					"}"+
				"}"+
				"document.getElementById('mail'+current).style.display='block';"+
			"}"+
			"function showPrev(){"+
				"hideAll(totalCount);"+
				"if(current == 0 && next == 0 && prev == 0){"+
					"prev=1;"+
					"current=1;"+
					"next=2;"+
				"}else{"+
					"if(current!=1){"+
						"next=current;"+
						"current=prev;"+
					"}"+
					
					"if(prev!=1){"+
						"prev=prev-1;"+
					"}"+
				"}"+
				"document.getElementById('mail'+current).style.display='block';"+
			"}"+
			"function showFirst(){"+
				"hideAll(totalCount);"+
				"prev=1; current=1; next=2;"+
				"document.getElementById('mail'+current).style.display='block';"+
			"}"+
			"function showLast(){"+
				"hideAll(totalCount);"+
				"prev=totalCount-1; current=totalCount; next=totalCount;"+
				"document.getElementById('mail'+current).style.display='block';"+
			"}");
		sb.append("</script>");

		return sb;
	}
	
	
	/*Helper method for adding HTML Pagination links content to the StringBuffer. It takes the StringBuffer reference
	* then adds the content to it and returns back the same reference
	*/
	private StringBuffer insertHtmlPagination(StringBuffer sb, int totalPages){
		
		sb.append("<div id='pagination'>");
				
		sb.append("<a id='showprev' onclick='showPrev()' href='javascript:void(0)'>prev</a>");
		sb.append("&nbsp;&nbsp;");
		sb.append("<a id='shownext' onclick='showNext()' href='javascript:void(0)'>next</a>");
		sb.append("<br/>");

		sb.append("<a id='showFirst' onclick='showFirst()' href='javascript:void(0)'>First</a>");
		sb.append("&nbsp;&nbsp;");
		for(int i=1;i<=totalPages;i++){
			sb.append("<a id='page"+i+"' onclick='hideShow("+i+","+totalPages+")'  href='javascript:void(0)'>"+i+" "+"</a>");

		}
		
		sb.append("<a id='showLast' onclick='showLast()' href='javascript:void(0)'>Last</a>");
		sb.append("&nbsp;&nbsp;");
		sb.append("<a id='showall' onclick='showAll("+totalPages+")' href='javascript:void(0)'>Show All</a>");
			
		sb.append("</div>");
		
		return sb;
	}
	
	
	
}
