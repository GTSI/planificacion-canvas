package helpers;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Misc {
	static String validCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvW-_";
	
	public static String generateHash(String toHash ) {
	    
	    MessageDigest md = null;
	    byte[] hash = null;
	    try {
	        md = MessageDigest.getInstance("SHA-512");
	        hash = md.digest(toHash.getBytes("UTF-8"));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return convertToHex(hash);
	}
	 
	/**
	* Converts the given byte[] to a hex string.
	* @param raw the byte[] to convert
	* @return the string the given byte[] represents
	*/
	private static String convertToHex(byte[] raw) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < raw.length; i++) {
	        sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return sb.toString();
	}
	

	
	

	public static String  randomString(int length) {

	    
	    List<String> letters = Arrays.asList(validCharacters.split(""));
	    Collections.shuffle(letters);
	    String shuffled = "";
	    int i=0;

	    for (String letter : letters.subList(0, length)) {
	      shuffled += letter;

	    }
	    return shuffled;
	    
	}
}
