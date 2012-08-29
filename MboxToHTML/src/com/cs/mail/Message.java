
/* @Message.java
*	Created By :	Gaurav Sachdeva

*	This class has the data structure defined for individual email messages that will be parsed form the
*	uploaded mbox file. It has every possible field to store each and every detail about the email messages
*	that included sender, receivers(both to and cc), subject, date, email body content, and all the attachments
*	in the message. For storing the attachment details it defines an inner class @Attachment which stores all 
*	possible details of the attachements. All the attachements of an email are stored in a list object in the
*	Message class itself. All the fields are kept private and the getters and setters are provided to give access
*	to other classes.
*/

package com.cs.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String subject;
	private String sender;
	private String[] toReceivers;
	private String[] ccReceivers;
	private String date;
	private int attachmentCount;
	private String body;
	private StringBuffer rawContent;
	private List<Attachment> attachmentList;
	
	{
		rawContent=new StringBuffer();
		attachmentList=new ArrayList<Attachment>();
	}
	
	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public StringBuffer getRawContent() {
		return rawContent;
	}

	public void addToRawContent(StringBuffer rawContent) {
		this.rawContent.append(rawContent);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String[] getToReceivers() {
		return toReceivers;
	}

	public void setToReceivers(String[] toReceivers) {
		this.toReceivers = toReceivers;
	}
	
	public String[] getCcReceivers() {
		return ccReceivers;
	}

	public void setCcReceivers(String[] ccReceivers) {
		this.ccReceivers = ccReceivers;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public class Attachment implements Serializable {
		
		private String attachmentName;
		private String attachmentType;
		private double attachmentSizeInKb;
		private StringBuffer attachmentContent=new StringBuffer();
		private String attachmentEncoding;
		
		public String getAttachmentEncoding() {
			return attachmentEncoding;
		}

		public void setAttachmentEncoding(String attachmentEncoding) {
			this.attachmentEncoding = attachmentEncoding;
		}

		public String toString(){
			return attachmentName+"("+attachmentSizeInKb+")";
		}
		
		public String getAttachmentName() {
			return attachmentName;
		}
		public void setAttachmentName(String attachmentName) {
			this.attachmentName = attachmentName;
		}
		public String getAttachmentType() {
			return attachmentType;
		}
		public void setAttachmentType(String attachmentType) {
			this.attachmentType = attachmentType;
		}
		public double getAttachmentSizeInKb() {
			return attachmentSizeInKb;
		}
		public void setAttachmentSizeInKB(double attachmentSizeInKb) {
			this.attachmentSizeInKb = attachmentSizeInKb;
		}
		public StringBuffer getAttachmentContent() {
			return attachmentContent;
		}
		public void setAttachmentContent(StringBuffer attachmentContent) {
			this.attachmentContent = attachmentContent;
		}
	}
}

