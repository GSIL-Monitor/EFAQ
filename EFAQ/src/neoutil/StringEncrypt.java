/**
 * File-Name:StringEncrypt.java
 * 
 * Created on 2011-6-2 下午09:55:00
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * 
 *          Copyright (c) 2009, Peking University
 * 
 * 
 */
package neoutil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description:
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2011-6-2 09:55:00
 */
public class StringEncrypt {

	public static String encrypt(String strSrc) {
		return encrypt(strSrc, "");
	}

	/**
	 * MD5,SHA-1,SHA-256,default SHA-256
	 * 
	 * @param strSrc
	 *            
	 * @param encName
	 *            
	 * @return
	 */
	public static String encrypt(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "SHA-1";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public static void main(String args[]) {
		String s = StringEncrypt.encrypt("password", "");
		System.out.println(s);
	}

}
