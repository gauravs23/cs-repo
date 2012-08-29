
/* @Base64Decoder.java
*	Created By :	Gaurav Sachdeva
*	This is utility class that does the job of decoding the base64 encoded string for
*	display or processing purposes. It has a method @getDecodedString that takes the 
*	base64 encoded string as input and returns the decoded string back to the calling 
*	entity.
*/
package com.cs.decoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;

public class Base64Decoder {

	public static String getDecodedString(String encoded) throws IOException{
		
		byte[] decoded=Base64.decodeBase64(encoded);
		InputStream in=new ByteArrayInputStream(decoded);
		StringBuffer sb=new StringBuffer();
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String line;
    	while ((line = br.readLine()) != null) {
    		sb.append(line);
    	} 
		
		return sb.toString();
	}
	
}
