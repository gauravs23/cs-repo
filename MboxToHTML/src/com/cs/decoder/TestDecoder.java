package com.cs.decoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.channels.IllegalSelectorException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileUploadException;
import org.w3c.tidy.Tidy;

public class TestDecoder {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

/*		
		String encoded="DQpUaGUgcGFzc3dvcmQgZm9yIHlvdXIgR29vZ2xlIEFjY291bnQgLSBuZXJkc2FjaGRldmFAZ21haWwuY29tIC0gd2FzICANCnJlY2VudGx5IGNoYW5nZWQuDQpJZiB5b3UgbWFkZSB0aGlzIGNoYW5nZSwgeW91IGRvbid0IG5lZWQgdG8gZG8gYW55dGhpbmcgbW9yZS4NCg0KSWYgeW91IGRpZG4ndCBjaGFuZ2UgeW91ciBwYXNzd29yZCwgeW91ciBhY2NvdW50IG1pZ2h0IGhhdmUgYmVlbiBoaWphY2tlZC4gIA0KVG8NCmdldCBiYWNrIGludG8geW91ciBhY2NvdW50LCB5b3UnbGwgbmVlZCB0byByZXNldCB5b3VyIHBhc3N3b3JkIGJ5IGNsaWNraW5nICANCnRoaXMNCmxpbms6ICANCmh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbS9SZWNvdmVyQWNjb3VudD9mcE9ubHk9MSZzb3VyY2U9YW5jcGUmRW1haWw9bmVyZHNhY2hkZXZhQGdtYWlsLmNvbS4NCg0KSW1wb3J0YW50IGFjY291bnQgc2VjdXJpdHkgdGlwczoNCi0gTmV2ZXIgZ2l2ZSBvdXQgeW91ciBwYXNzd29yZCBvciBwZXJzb25hbCBpbmZvcm1hdGlvbiBieSBlbWFpbCBvciBvbiBzb2NpYWwNCiAgIG5ldHdvcmtzLiBHb29nbGUgd2lsbCBuZXZlciBjb250YWN0IHlvdSB0byBhc2sgeW91IGZvciB5b3VyIHBhc3N3b3JkLg0KLSBOZXZlciB1c2UgdGhlIHNhbWUgcGFzc3dvcmQgZm9yIEdvb2dsZSBhbmQgb3RoZXIgd2Vic2l0ZXMuDQotIEFkZCB5b3VyIG1vYmlsZSBwaG9uZSB0byB5b3VyIGFjY291bnQgc28geW91IGNhbiBlYXNpbHkgcGFzcyBHb29nbGUncyAgDQpzZWN1cml0eQ0KICAgbWVhc3VyZXMgYW5kIG1ha2UgaXQgaGFyZCBmb3IgaGlqYWNrZXJzIHRvIGdldCBpbi4NCg0KTm90ZTogVGhpcyBlbWFpbCBhZGRyZXNzIGNhbm5vdCBhY2NlcHQgcmVwbGllcy4gVG8gZml4IGFuIGlzc3VlIG9yIGxlYXJuICANCm1vcmUNCmFib3V0IHlvdXIgYWNjb3VudCwgdmlzaXQgb3VyIGhlbHAgY2VudGVyOg0KaHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS9zdXBwb3J0L2FjY291bnRzL2Jpbi9hbnN3ZXIucHk/YW5zd2VyPTE2Mjg4NzUuDQoNClNpbmNlcmVseSwNClRoZSBHb29nbGUgQWNjb3VudHMgVGVhbQ0KDQogICCpIDIwMTIgR29vZ2xlIEluYy4gMTYwMCBBbXBoaXRoZWF0cmUgUGFya3dheSwgTW91bnRhaW4gVmlldywgQ0EgOTQwNDMNCg0KWW91IGhhdmUgcmVjZWl2ZWQgdGhpcyBtYW5kYXRvcnkgZW1haWwgc2VydmljZSBhbm5vdW5jZW1lbnQgdG8gdXBkYXRlIHlvdSAgDQphYm91dCBpbXBvcnRhbnQgY2hhbmdlcyB0byB5b3VyIEdvb2dsZSBwcm9kdWN0IG9yIGFjY291bnQuDQoNCg==";

		
		byte[] decoded=Base64.decodeBase64(encoded);
		InputStream in=new ByteArrayInputStream(decoded);
		StringBuffer sb=new StringBuffer();
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String line;
    	while ((line = br.readLine()) != null) {
    		sb.append(line);
    	} 
		
		System.out.println(sb);
	*/	
/*		String msgBody="<html><body>asdasdasdasdasd</body></html><html><body>1641212</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html><html><body>asdasdasdasdasd</body></html>";
		
		Tidy tidy=new Tidy();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		
		tidy.setXHTML(true);
		
		tidy.parse(new StringReader(msgBody),baos);
		//baos.flush();
		
		msgBody=baos.toString();
		
		System.out.println(msgBody);*/
		System.out.println(new Exception());
		System.out.println(new IOException());
		System.out.println(new NullPointerException());
		System.out.println(new IllegalSelectorException());
		System.out.println(new FileUploadException());
		
	}

}
