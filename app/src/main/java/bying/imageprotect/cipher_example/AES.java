package bying.imageprotect.cipher_example;

import bying.imageprotect.util.AES256Util;
import bying.imageprotect.util.BytesToHex;

public class AES {

	//待加密的原文
	public static final String DATA = "hi, welcome to my git area!";
	
	public static void main(String[] args) throws Exception {
		//获得密钥
		byte[] aesKey = AES256Util.initKey();
		System.out.println("AES 密钥 : " + BytesToHex.fromBytesToHex(aesKey));
		//加密
		byte[] encrypt = AES256Util.encryptAES(DATA.getBytes(), aesKey);
		System.out.println(DATA + " AES 加密 : " + BytesToHex.fromBytesToHex(encrypt));
		
		//解密
		byte[] plain = AES256Util.decryptAES(encrypt, aesKey);
		System.out.println(DATA + " AES 解密 : " + new String(plain));
	}
}
