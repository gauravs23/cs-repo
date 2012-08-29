
/*	@ParseMbox.java
*	Created By :	Gaurav Sachdeva
*	This servlet is responsible for converting the mbox file input stream into a list object that
*	contains the email messages object. Each email message is stored in the Message class as defined
*	com.cs.mail package in this project. It reads uploaded file line by line and performs paring as 
*	per the mbox file format. For email messages format, it assumes the gmail original email structure.
*	In case the file uploaded is some format other than the mbox, it throws an exception and an
*	appropriate message is shown to the user. In case the parsing fails, it displays the error message
*	for that too.
*	If the parsing is successful, it sets the individual email messages in their corresponding Message 
*	objects and put all the Message objects in an ArrayList. It then sets this List object in a session
*	attribute and forwards the request to the GenerateHTML.java for html generation and returning 
*	response to the user.
*	
*	
*/

package com.cs.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cs.decoder.Base64Decoder;
import com.cs.mail.Message;


public class ParseMbox extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
		
	try{	
		
		//getting the response from the request attribute set by the FileUploadServlet.java
		InputStream in=(InputStream)request.getAttribute("stream");
		InputStreamReader inr=new InputStreamReader(in);
		
		//obtaining a buffered reader from the input stream to read the file line by line
		BufferedReader br=new BufferedReader(inr);
		
		//Initialising the List object. This list will contain all the Message objects for each email in mbox file
		List<Message> msgList=new ArrayList<Message>();
		
		/* From here the mbox file parsing starts. We first seperates each email message from the mbox file and
		*	set its content in the Message object, rawContent instance variable. This rawContent is then later 
		*	used to extract the details of the email message and based on that all the instance variables of that
		*	Message object are set. These include sender, date, subject, attachments, email body, receivers in To list,
		*	and receivers in Cc list.
		*/
		String str=null;
		int count=0;
		boolean breakOccurred=false;
		String ls = System.getProperty("line.separator");
		msgList.add(new Message());
		boolean isValidFile=false;
		
		while((str=br.readLine())!=null){
			
			/*Here we check whether the file starts from the String "From -". This is to make sure that the file
			* uploaded by the user is a valid mbox file or not. Each valid mbox file will start from the string 
			* specified. This string basically specifies the start of an email message embedded in the mbox file.
			* This same string format is used for pasring and seperating all email messages from the mbox file.
			*/
			if(!isValidFile){
				if(str.startsWith("From -"))
					isValidFile=true;
				else
					throw new Exception();
			}
			
			if(str.startsWith("From -") && breakOccurred){
				count++;
				msgList.add(new Message());
			}
			
			breakOccurred=false;
			if(str.isEmpty())
				breakOccurred=true;
			
			StringBuffer tempsb=msgList.get(count).getRawContent();
			tempsb.append(str);
			tempsb.append(ls);
		}
		
		/*Here we have now got all the emails in the mbox file as seperate Message class instances in an ArrayList
		* named as "msgList". Here only the "rawContent" variable for Message object is set and all other variables
		* have null values as of now. In the below section of the code, we will loop through this arraylist and 
		* retrieve all the details of each Message and set the values correspondingly.
		*/
	//	System.out.println("Total Messages in arraylist are: "+msgList.size());
		
		
		//From here the loop through the list starts and all the values are set for each Messsage object.
		
		//keeping count of current msgCount in the loop.
		int msgCount=0;
		
		for(Message m:msgList){
			
			msgCount++;
			StringBuffer msg=m.getRawContent();
			BufferedReader testbr=new BufferedReader(new StringReader(msg.toString()));
			String msgLine=null;
			
			/*local variables defined to aid in parsing the rawContent and the logic is totally specific to the
			* gmail style of message delivery.
			*/
			boolean contentFound=false;
			boolean attachmentFound=false;
			boolean htmlFound=false;
			boolean htmlDone=false;
			boolean plainTextFound=false;
			String plainTextEncoding="";
			String plainText="";
			StringBuffer temp=null;
			int attachmentCount=0;
			Message.Attachment ma=null;
			
			try{
			while((msgLine=testbr.readLine())!=null){
				
				/*a little info about the gmail structure is as follows: from the start all the information
				* information about the message like sender, receiver, subject, date etc are listed at the top and then
				* the plain/text content follows which is the text version fo the email content. After that we have
				* text/html content embedded in the message, which has the whole content body of the email. Here if there
				* are any embedded images in the HTML content in the email body, then those are embedded in the gmail structure
				* as base64 encoding. After html content, there mayb be no content if there are no attachments. In case there 
				* attachments , the attachments are specifies along with their content type which may be anything depending 
				* upon the mim-type of the atachment.
				*/
				
				
				
				/*checks whether we have reached the content section and sets the variable that content has beedn found. It
				* bascially means that all the lines followed are the content of the email.
				*/
				
				if(msgLine.startsWith("Content-Type:") && !contentFound){
					contentFound=true;
				}
				
				if(contentFound){
					
					/*Retrieving the plain.text content in the temporary string buffer. as specified above, even if there is html
					* content in the email, still the text version of the html will also be embedded in the mail format. Here the 
					* need to check for the text content also as in some emails, like gmail password recovery, we have only text 
					* content and no html content. So if we dont find HTML content, we will be setting the message body with this
					* text content.
					*/
					if(msgLine.startsWith("Content-Type: text/plain")){
						plainTextFound=true;
						temp=new StringBuffer();
						msgLine=testbr.readLine();
						
						// If the text is encoded , we set the variable here.
						if(msgLine.contains("base64")){
							plainTextEncoding="base64";
						}
						msgLine=testbr.readLine();
						msgLine=testbr.readLine();
					}
					
					/*checks whether the plain text is found and set. All these conditions help us to determine what
					* parts of the message have been detected and are set. This helps to track the string buffer current
					* location in the email rawContent which is part of the logic of parsing.
					*/
					if(plainTextFound){
						if(msgLine.endsWith("=") && plainTextEncoding !="base64"){
							int len=msgLine.length();
							msgLine=msgLine.substring(0,len-1);
						}
						
						if((msgLine.startsWith("Content-Type: ") && (msgLine.contains("charset") || msgLine.contains("boundary")))){
							plainTextFound=false;
							plainText=temp.toString();
							temp=null;
						}else{
							temp.append(msgLine);
							temp.append(ls);
						}
						
					}
					
					
					/* Checks whether it is the start of the HTML content and the html content has not already been found. One case
					*	where HTML has been found and we still get the content type as text/html, will be when the HTML file is
					* enclosed as attachment in the message. This second condition is handle that particular case.
					*/
					if(msgLine.startsWith("Content-Type: text/html") && !htmlDone){
						htmlFound=true;
						temp=new StringBuffer();
						msgLine=testbr.readLine();
						msgLine=testbr.readLine();
						msgLine=testbr.readLine();
					}
					
					/* If the start of the html content has been found, then set the message body with the html content
					*/
					if(htmlFound && !htmlDone){
						if(msgLine.endsWith("=")){
							int len=msgLine.length();
							msgLine=msgLine.substring(0,len-1);
						}
						if(temp == null)
							temp=new StringBuffer();
						temp.append(msgLine);
						temp.append(ls);
						if(msgLine.contains("</html>") || msgLine.contains("</body>") || msgLine.startsWith("Content-Type:") || (msgLine.startsWith("--")&&msgLine.endsWith("--"))){
							htmlFound=false;
							m.setBody(temp.toString());
							temp=null;
							htmlDone=true;
						}
					}
					
					/*	In case the HTML content has been set and we get the line starting with "Content-type", it
						* means that the eamil has attachments. So here we set the atttachments in the mail message as a 
						* list in the message object.
					*/
					else if(htmlDone){
						
						/* Initialises and set the values of the attachment object
						*/
										
						if(msgLine.startsWith("Content-Type:")){
							attachmentFound=true;
							attachmentCount++;
							temp=new StringBuffer();
							m.getAttachmentList().add(m.new Attachment());
							ma=m.getAttachmentList().get(attachmentCount-1);
							
							String thisLine =msgLine.toString().replaceFirst("Content-Type: ", "");
							String tempArr[]=thisLine.split(";",2);
							
							
							ma.setAttachmentType(tempArr[0]);
							
							if(tempArr.length ==2){
								int nameIndex=tempArr[1].indexOf("name");
								tempArr[1]=tempArr[1].substring(nameIndex);
								if(tempArr[1].indexOf(";")> -1)
									tempArr[1]=tempArr[1].substring(0, tempArr[1].indexOf(";")-1);
								ma.setAttachmentName(tempArr[1].trim().replaceFirst("name=","" ).replaceAll("\"",""));
							}	
														
							msgLine=testbr.readLine();
							String encoding=msgLine.substring(msgLine.indexOf(":")+1).trim();
							ma.setAttachmentEncoding(encoding);
							msgLine=testbr.readLine();
							if(!msgLine.contains("Content-Disposition: attachment")){
								m.getAttachmentList().remove(attachmentCount-1);
								attachmentCount--;
								attachmentFound=false;
							}
							msgLine=testbr.readLine();
						}
						
						/*	Setting the attachment content. All the attachmetn content will be base64 encoding.
						*/
						else if(attachmentFound){
							
							if(msgLine.isEmpty()){
								attachmentFound=false;
								ma=m.getAttachmentList().get(attachmentCount-1);
								ma.setAttachmentContent(temp);
								temp=null;
								
								
								double attachmentSizeInKb=0;
								if(ma.getAttachmentEncoding().contains("base64")){
									attachmentSizeInKb=((ma.getAttachmentContent().length())/1.36)/1024.0;
									ma.setAttachmentSizeInKB(attachmentSizeInKb);
								}else{
									attachmentSizeInKb=(ma.getAttachmentContent().length())/1024.0;
									ma.setAttachmentSizeInKB(attachmentSizeInKb);
								}
									
							}else{
								temp.append(msgLine);
								temp.append(ls);
							}
						}
					}
				}			
				else{
					
					/*Here all the values of the email apart from body content is set in the apropriate instance 
					* variables. This part of the code will be executed first while parsing the message as all these
					* values are at the top of the email message as per the gmail format.
					*/
				
					if(msgLine.startsWith("Subject:")){
						m.setSubject(msgLine.replaceFirst("Subject:", ""));
					}
					if(msgLine.startsWith("Date:")){
						m.setDate(msgLine.replaceFirst("Date:", ""));
					}
					if(msgLine.startsWith("From:")){
						m.setSender(msgLine.replaceFirst("From:", ""));
						
					}
					if(msgLine.startsWith("To:")){
						String[] toReceivers= msgLine.replaceFirst("To:","").trim().split(",");
						for(int i=0; i< toReceivers.length;i++){
							toReceivers[i]=toReceivers[i].replaceAll("\"", "");
							}

						m.setToReceivers(toReceivers);
					}
					if(msgLine.startsWith("CC:")){
						String[] ccReceivers= msgLine.replaceFirst("CC:","").trim().split(",");
						for(int i=0; i< ccReceivers.length;i++)
							ccReceivers[i]=ccReceivers[i].replaceAll("\"", "");
					//	System.out.println(ccReceivers[0]);
						m.setCcReceivers(ccReceivers);
					}
					
					
				}
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			/* Here, we have all the values set for each of the message object in the arraylist.
			*/
			
			/* Incase there was no HTML content in the email body, then at this point the megBody will be null for that particulat
			* message. So we check if there was no html, we should set the texxt content as the mesgBody as there will always be plain
			* text in the email message.
			*/
			if(!htmlDone){
				if(plainTextEncoding.equals("base64")){
					m.setBody(Base64Decoder.getDecodedString(plainText));
				}else{
					m.setBody(plainText);
				}
			}
			
		
			
			
			
			m.setBody(m.getBody().replaceAll(ls, ""));
			m.setBody(m.getBody().replaceAll("=0A",""));
			m.setBody(m.getBody().replaceAll("=3D","="));
			m.setBody(m.getBody().replaceAll("=09",""));
			m.setBody(m.getBody().replaceAll("=E2",""));
			m.setBody(m.getBody().replaceAll("=80",""));
			m.setBody(m.getBody().replaceAll("=AC",""));
			m.setBody(m.getBody().replaceAll("=AA",""));
			m.setBody(m.getBody().replaceAll("=A9",""));
			m.setBody(m.getBody().replaceAll("=20",""));

			m.setAttachmentCount(attachmentCount);
		}
	
		//End of for loop the msgList.
	
		//Getting the session
		HttpSession session=request.getSession(true);
		//Setting the messages arraylist object in the session.
		session.setAttribute("msgListS", msgList);
	
		//Passing the request to the GenerateHTML.java which does the job of generating HTML from the message arraylist.
		request.getRequestDispatcher("/generatehtml").forward(request, response);
		
	}catch(Exception e){
		e.printStackTrace();
		request.setAttribute("exceptionType", "Exception");
		request.getRequestDispatcher("/WEB-INF/jsp/ErrorPage.jsp").forward(request, response);
		
	}
}
}
