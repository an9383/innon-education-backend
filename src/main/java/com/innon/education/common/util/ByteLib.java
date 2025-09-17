package com.innon.education.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteLib {
	private static Logger logger = LoggerFactory.getLogger(DataLib.class);

	// encrypt type 으로 String을 byte[] 로 바꾸기 (Exception을 기본 처리해야 하므로 공통모듈로)
	public static byte[] getBytes(Object txt, String encType){
		try{return txt.toString().getBytes(encType);}
		catch(UnsupportedEncodingException e){return txt.toString().getBytes();}
	}
	public static byte[] getBytes(Object txt){
		return getBytes(txt, Constants.ENC_TYPE);
	}

	// byte[] 배열의 pos에서 len만큼 얻기
	public static byte[] getBytesFromIdxToLen(byte[] bOrg, int sIdx, int len){
		byte[] bRtn = new byte[len];
		System.arraycopy(bOrg, sIdx, bRtn, 0, len);
		return bRtn;
	}

	// byte[] 배열 합치기
	public static byte[] concatBytes(byte[] ... datas){
		ByteArrayOutputStream baos = null;
		try{
			baos = new ByteArrayOutputStream( );
			for(byte[] data : datas){baos.write(data);}
			return baos.toByteArray();
		}
		catch(Exception e){
			return null;
		}
		finally {
			try {if (baos!=null) baos.close();} catch (IOException e) {logger.debug(e.toString());}
		}
	}
	// String ArrayList를 Byte[] 로 합치기
	public static byte[] concatStringListToByte(ArrayList<String> data, String encType){
		ByteArrayOutputStream baos = null;
		try{
			baos = new ByteArrayOutputStream( );
			for (int i=0, cnt=data.size(); i < cnt; i++) baos.write(getBytes(data.get(i),encType));
			return baos.toByteArray();
		}
		catch(Exception e){
			return null;
		}
		finally {
			try {if (baos!=null) baos.close();} catch (IOException e) {logger.debug(e.toString());}
		}
	}
	// byte[] ArrayList를 Byte[] 로 합치기
	public static byte[] concatBytesList(ArrayList<byte[]> data){
		ByteArrayOutputStream baos = null;
		try{
			baos = new ByteArrayOutputStream( );
			for (int i=0, cnt=data.size(); i < cnt; i++) baos.write(data.get(i));
			return baos.toByteArray();
		}
		catch(Exception e){
			return null;
		}
		finally {
			try {if (baos!=null) baos.close();} catch (IOException e) {logger.debug(e.toString());}
		}
	}

	//1바이트중 필요한 비트의 값만 가져와서 int 숫자로 변환한다.
	public static int byteToIntByIdxAndLen(byte aByte, int startIdx, int length) {
		String binStr = DataLib.fillStr("0", startIdx);
		binStr = DataLib.fillRight(binStr, "1", startIdx+length);
		binStr = DataLib.fillRight(binStr, "0", 8);//1Byte=8bit
		int mask = Integer.parseInt(binStr, 2);
		int result = ((aByte & mask) >> 8-startIdx-length);
		return result;
	}

	public static int byteToInt(byte aByte) {
		return((aByte & 0xff));
	}

	public static int bytesToInt(byte bytes[]) {
		int result = 0;
		int len = bytes.length;
		for (int i=0;i<len;i++){
			result = result + ((bytes[i] & 0xff) << 8*(len-i-1));
		}
		return result;
	}
	// 바이트 배열을 숫자로 디버깅 용으로 화면에 뿌릴 때 사용 : 예) [87,4,2,0,0,0,0,4,0,0]
	public static String bytesToDebugString(byte bytes[]) {
		String showString = "[";
		for (int i=0, len = bytes.length;i<len;i++){
			showString += ByteLib.byteToInt(bytes[i]) +",";
		}
		showString = (bytes.length>0) ? showString.substring(0, showString.length()-1) + "]" : "]";
		return showString;
	}
	// 바이트 배열을 헥사숫자로 디버깅 용으로 화면에 뿌릴 때 사용 : 예) [0x57,0x04,0x02,0x00,0x00,0x00,0x00,0x04,0x00,0x00]
	public static String bytesToDebugHexString(byte bytes[]) {
		String showString = "[";
		for (int i=0, len = bytes.length;i<len;i++){
			showString += "0x"+HexLib.intToHexStr(ByteLib.byteToInt(bytes[i]), 1) +",";
		}
		showString = (bytes.length>0) ? showString.substring(0, showString.length()-1) + "]" : "]";
		return showString;
	}

	//1바이트중 필요한 비트의 값만 가져와서 long 숫자로 변환한다.
	public static long byteToLongByIdxAndLen(byte aByte, int startIdx, int length) {
		String binStr = DataLib.fillStr("0", startIdx);
		binStr = DataLib.fillRight(binStr, "1", startIdx+length);
		binStr = DataLib.fillRight(binStr, "0", 8);//1Byte=8bit
		long mask = Long.parseLong(binStr, 2);
		long result = ((aByte & mask) >> 8-startIdx-length);
		return result;
	}

	public static long bytesToLong(byte bytes[]) {
		long result = 0;
		int len = bytes.length;
		for (int i=0;i<len;i++){
			result = result + ((bytes[i] & 0xff) << 8*(len-i-1));
		}
		return result;
	}

	//소켓통신시 bytes로 길이를 넘겨야 하는 경우 사용
	public static byte[] intToBytes(long value, int len) {
		byte[] bytes = new byte[len];
		for(int i=0;i<len;i++){
			bytes[i] = (byte) (value >> (len-i-1)*8);
		}
		return bytes;
	}

	public static byte[] charsToBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName(Constants.ENC_TYPE).encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
		return bytes;
	}

	//스페이스로 채워서 바이트 문자열을 만듬 (고정길이 통신시 나머지 영역에 스페이스 넣는경우)
	public static byte[] putSpaceToBytes(byte[] bOrg, int len){
		byte[] bRtn = getBytes(DataLib.fillStr(" ", len)); //나머지 공간에 스페이스(32) 들어감
		System.arraycopy(bOrg, 0, bRtn, 0, Math.min(bOrg.length,len));
		return bRtn;
	}
	//스페이스로 채워서 바이트 문자열을 만듬 (고정길이 통신시 나머지 영역에 스페이스 넣는경우)
	public static byte[] putSpaceToBytes(String sOrg, int len){
		return putSpaceToBytes(getBytes(sOrg), len);
	}

	//byte를 String 으로 변환
	public static String toString(byte bStr){
		byte[] bStrs = {bStr};
		return toString(bStrs, Constants.ENC_TYPE);
	}
	public static String toString(byte bStr, String encType){
		byte[] bStrs = {bStr};
		return toString(bStrs, encType);
	}
	//byte[]를 String 으로 변환
	public static String toString(byte[] bStr){
		return toString(bStr, Constants.ENC_TYPE);
	}
	public static String toString(byte[] bStr, String encType){
		try{return new String(bStr, encType);}
		catch(UnsupportedEncodingException e){return new String(bStr);}
	}

	// 수신된 BufferedInputStream을 읽어서 byte[] 로 리턴 하기
	public static byte[] read(BufferedInputStream bufInput, int length) throws IOException {
		byte[] buf = new byte[length];
		if (length < 0) throw new IndexOutOfBoundsException();
		int idx = 0;
		while (idx < length) {
			int count = bufInput.read(buf, idx, length - idx);
			if (count < 0) throw new EOFException();
			idx += count;
		}
		return buf;
	}
	// 수신된 BufferedInputStream을 읽어서 byte[] 로 리턴 하기
	public static int read(BufferedInputStream bufInput, byte[] buf, int start, int length) throws IOException {
		if (length < 0) throw new IndexOutOfBoundsException();
		int idx = 0;
		while (idx < length) {
			int count = bufInput.read(buf, start + idx, length - idx);
			if (count < 0) throw new EOFException();
			idx += count;
		}
		return idx;
	}

	//VO 직렬화
	public static byte[] serialize(Object o) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			return baos.toByteArray();
		}
		catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
		finally {
			try {if (baos!=null) baos.close();} catch (IOException e) {logger.debug(e.toString());}
			try {if (oos!=null)   oos.close();} catch (IOException e) {logger.debug(e.toString());}
		}
	}
	//VO 역직렬화
	public static Object deSerialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			return o;
		}
		catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
		finally {
			try {if (bais!=null) bais.close();} catch (IOException e) {logger.debug(e.toString());}
			try {if (ois!=null)   ois.close();} catch (IOException e) {logger.debug(e.toString());}
		}
	}

	/*
	public static byte[] serialize(Object o) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(o);
				return baos.toByteArray();
			}
		}
		catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
	//VO 역직렬화
	public static Object deSerialize(byte[] bytes) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream ois = new ObjectInputStream(bais)) {
				Object o = ois.readObject();
				return o;
			}
		}
		catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
	*/
}
