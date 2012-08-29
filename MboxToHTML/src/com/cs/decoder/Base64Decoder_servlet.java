package com.cs.decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.binary.Base64;

public class Base64Decoder_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Base64Decoder_servlet() {
        super();

    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		response.setContentType("text/html");
	//	response.setHeader("Content-Disposition", "attachment;filename=info.zip");
		
		String encoded="iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyBpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBXaW5kb3dzIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjUwOUZBRkM1QUM0MTExRTFCQURDRDFFMjRFMTI0MzQwIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjUwOUZBRkM2QUM0MTExRTFCQURDRDFFMjRFMTI0MzQwIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6NTA5RkFGQzNBQzQxMTFFMUJBRENEMUUyNEUxMjQzNDAiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6NTA5RkFGQzRBQzQxMTFFMUJBRENEMUUyNEUxMjQzNDAiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6zYfyWAAADFElEQVR42qST328UVRTHv/fH7Mzsnd1tK6WLP+iyAoVgJKnS2kqiQYmAaUI0vmh4JCG8Ev8C30xMCG88kfhgTPqgPlhWkfCALyqJKTGy3YYWxJSWFLfdZWfmzsy9lzuLkoZXJ5OZzJxzPud7flxijMH/uXhl6lM4pWEQVoBRMaCUvSVgjFusvvqFQVYMV/88q1d+ixmjb3B/YMjGkWRz5U68uXqLP0s0RgNae8HoxAWx9/jp/B9xGlm3s3JOONG7pZGxzzIZdta79z8Go03+TLQNVn5Qm74o6m+d0ut/NAiIFC8fOW0VKn/9+uXStuFmEnbWNhFdEUKoLQBjS0hFsTb9pV87/IFutxpm8btFY7KQ7D6Z+PW3z+jy9sle+8Y8SeR+Lyg7jJDkKcCoJCjWpr7yXpqeIRsLc2a5sZomyScGOissz31NiZk1Qwc+kpRJ/XC2kSapDN0XwNznJ0E5Hwh2HZ71Xjx0gvxzaw53fuwmUW8GbnmQOL5QMtxDOks3aUG0zOD+Y4oXWdRe/TbVTshKBz58TdTfvOTuGH+HbjSvmLs/pTKKj1nnEskbmMtjnq9SuYd2l5vcFU06fPA9KoYntXzUIrWzN1q8stMab18zd6+mMk6PgHs834//NqQPIvaZRZlbIN+Q0aOOHhg7mbWXb5Oho5+/XwyEqKT3xzuSn6M2mFID33XBGOtDtNaIpLQDsr2ykAKJz4f+zl9l2JW889fN78sjHpxA1MGqXCmNfaNDmJkYwIMH6zYYqG7fhp8XJK7Pr4HxIle9NVe1f5nthSl4dZCjGgAxIay/1oSiXHTQmr+Gyz9ctfIppiYPojJ6ApTSJ+MGDRySwXMo+NgOB3nWXqKfFpzl37FBomwJtvZIGpBUbdkYeBUnxmCBgEeptjFbDxTJO4ZERuh1N6yFIY5CiH9HkntaIX6Zhrmj4dRK7gNILo8ix+XNO/T6uH1zayF4ZayOpciDtm6MMst3hDG8T+RpEvcBWvERIAS13IWlFMw8B1OeQN6X39coFu/9bWsLYWx6k8l9iU5sRqIeCzAAnOFkoj0aS9IAAAAASUVORK5CYII=";

		
		byte[] decoded=Base64.decodeBase64(encoded);
		InputStream in=new ByteArrayInputStream(decoded);

		in.toString();
		
		//ServletOutputStream sout=response.getOutputStream();

	//	response.getWriter().println("<img src="+image+">");
		byte [] outputByte =new byte[4096];
		while(in.read(outputByte, 0, 4096) != -1){
		//	sout.write(outputByte, 0, 4096);
		}
		in.close();
	//	sout.flush();
		//sout.close();
		
		
	
		
	/*
		PrintWriter out=response.getWriter();
		out.println("Decoding Starts");
	
		File file=new File("c:/mbox/encodedFiles/sample_pic.jpg");
		
		byte[] bytes=FileUtils.readFileToByteArray(file);
		
		String encoded=Base64.encodeBase64String(bytes);
		
		out.println(encoded);
		byte[] decoded=Base64.decodeBase64(encoded);
		
		File file2=new File("f:/sample");
		file2.createNewFile();
		FileOutputStream os=new FileOutputStream(file2);
		
		
		os.write(decoded);
		os.flush();
		os.close();
	
	*/	
	
	}
	
}
