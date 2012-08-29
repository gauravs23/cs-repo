
/*	@FileUploadServlet.java
*	Created By :	Gaurav Sachdeva
*
*	This servlet is responsible for getting the uploaded file from the form submitted by the user 
*	convert it into an input stream object that is used by the other classes to read data from.
*	
*/

package com.cs.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
      
    public FileUploadServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		try {
			
			ServletFileUpload upload=new ServletFileUpload();
			FileItemIterator iterator=upload.getItemIterator(request);
		
			//initializing the inputstream variable
			InputStream stream=null;
			try{
				FileItemStream item = iterator.next();
				//Checking if the form field is a file then set it in the input stream object
			    if (!item.isFormField()) {
		        	stream = item.openStream();
			    }
			    
			/*	The different values for the exceptionType attribute are set in case of some exception.
			*	These values are used by the error page jsp to display appropriate error message to the
			*	user in case of any failure or exception.
			*/
			  
			}catch(IOException e){
				e.printStackTrace();
				request.setAttribute("exceptionType", "IOException");
				request.getRequestDispatcher("/WEB-INF/jsp/ErrorPage.jsp").forward(request, response);
			}catch(Exception e){
				e.printStackTrace();
				request.setAttribute("exceptionType", "Exception");
				request.getRequestDispatcher("/WEB-INF/jsp/ErrorPage.jsp").forward(request, response);

			}
		
			//setting the input stream object in the request attribute so that it can be used in parsing servlet.
			request.setAttribute("stream", stream);
			
		}catch (FileUploadException e) {
			e.printStackTrace();
			request.setAttribute("exceptionType", "FileUploadException");
			request.getRequestDispatcher("/WEB-INF/jsp/ErrorPage.jsp").forward(request, response);
	
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			request.setAttribute("exceptionType", "IllegalArgumentException");
			request.getRequestDispatcher("/WEB-INF/jsp/ErrorPage.jsp").forward(request, response);
		}
		
		/*	Forwarding the request to the ParseMbox.java serlvet that is responsible for parsing the file 
		*	setting the Messages objects in a list.
		*/
		request.getRequestDispatcher("/parsembox").forward(request, response);
	}
	
}

