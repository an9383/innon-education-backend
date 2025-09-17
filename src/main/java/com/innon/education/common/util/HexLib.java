package com.innon.education.common.util;

public class HexLib {
	//16진수문자열을 byte[]로 바꾸기
	public static byte[] hexStrToBytes(String data) {
		byte[] bytes;
		bytes = new byte[data.length() / 2];
		for (int i = 0; i < data.length(); i += 2) {
			bytes[i / 2] = (byte) (Integer.parseInt(data.substring(i, i + 2), 16));
		}
		return bytes;
	}

	//16진수문자열을 숫자로 바꾸기
	public static int hexStrToInt(String hexStr) {
		return Integer.parseInt(hexStr, 16);
	}

	//16진수문자열을 숫자로 바꾸기
	public static long hexStrToLong(String hexStr) {
		return Long.parseLong(hexStr, 16);
	}

	//16진수문자열을 문자로 바꾸기
	public static String hexStrToString(String hexStr) {
		String result = "";
		for(int i=0, len=hexStr.length(); i<len ; i+=2) {
			result += "" + (char)(Integer.parseInt(hexStr.substring(i,i+2), 16));
		}
		return result;
	}

	// 역 Casting ---------------------------------------------------------------------------

	//byte[]를 16진수 문자열로 바꾸기
	public static String bytesToHexStr(byte[] bytes) {
		String hexStr = "";
		for (byte bt : bytes) {
			hexStr += Integer.toString((bt & 0xff)+ 0x100, 16).substring(1);
		}
		return hexStr;
	}

	//문자를 16진수 문자열로 바꾸기
	public static String stringToHexStr(String str) {
		return bytesToHexStr(ByteLib.getBytes(str));
	}

	//숫자를 16진수 문자열로 바꾸기
	public static String intToHexStr(long num, int len) {
		return bytesToHexStr(ByteLib.intToBytes(num, len));
	}
}
