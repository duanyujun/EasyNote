package com.comtop.easynote.utils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import com.comtop.easynote.constants.Constants;

public class EncryUtil {

	public static String encrypt(String source) {

		RSAPrivateKey priKey = RSAHelper.generateRSAPrivateKey(
				RSAHelper.hexStringToByte(Constants.RSA_MODULUS),
				RSAHelper.hexStringToByte(Constants.RSA_PRIVATE_EXPONENT));
		return RSAHelper.encrypt(priKey, source);
	}

	public static byte[] decrypt(String source) {

		RSAPublicKey priKey = RSAHelper.generateRSAPublicKey(
				RSAHelper.hexStringToByte(Constants.RSA_MODULUS),
				RSAHelper.hexStringToByte(Constants.RSA_PUBLIC_EXPONENT));

		return RSAHelper.decrypt(priKey, RSAHelper.hexStringToByte(source));
	}


}
