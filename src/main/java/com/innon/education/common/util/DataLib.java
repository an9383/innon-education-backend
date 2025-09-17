package com.innon.education.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;


public class DataLib {
	private static Logger logger = LoggerFactory.getLogger(DataLib.class);

	public static String fillStr(Object fObj,int len){
		String sReturn = "";
		String fStr = fObj.toString();
		if(len<0) return "";
		for(int i=0;i<len;i+=fStr.length()) {sReturn+=fStr;}
		return sReturn.substring(0,len);
	}
	public static String fillRight(Object oBase, Object fObj, int len){
		return (oBase.toString() + fillStr(fObj,len-ByteLib.getBytes(oBase.toString()).length));
	}
	public static String fillLeft(Object oBase, Object fObj, int len){
		return (fillStr(fObj,len-ByteLib.getBytes(oBase.toString()).length) + oBase);
	}
	public static boolean isTrimEmpty(Object obj) {
		return (obj == null || obj.toString().trim().length() == 0);
	}

	// int로 바꾸어 리턴 (null이면 0으로)
	public static int toINN(Object obj) {
		try {return Integer.parseInt(obj.toString());}
		catch (Exception e) {return 0;}
	}
	public static int toINN(Object obj, Object nullTo) {
		try {return Integer.parseInt(obj.toString());}
		catch (Exception e) {return Integer.parseInt(nullTo.toString());}
	}

	//toNotNull
	public static String toNN(Object obj) {
		return (obj==null) ? "" : obj.toString();
	}
	public static String toNN(Object obj, Object nullTo) {
		try{return (obj==null || obj.equals("")) ? (nullTo==null) ? null : nullTo.toString() : obj.toString();}
		catch(Exception e){return "";}
	}
	public static String toNnTrim(Object obj) {
		return (obj==null) ? "" : obj.toString().trim();
	}
	public static Object toNnObj(Object obj, Object nullTo) {
		try{return (isEmpty(obj)) ? nullTo : obj;}
		catch(Exception e){return "";}
	}

	//맵의 키값이 없거나 값이 isTrimEmpty 이면 빈문자열 넣음
	public static Object toNnMap(Map<String, Object> map, String key) {
		return toNnMap(map, key, "");
	}
	//맵의 키값이 없거나 값이 isTrimEmpty 이면 인자값의 value 넣음
	public static Object toNnMap(Map<String, Object> map, String key, Object nullTo) {
		if (isTrimEmpty(map.get(key))) map.put(key, nullTo);
		return map.get(key);
	}
	public static Object toNnMapTrim(Map<String,Object> map, String name) {
		return toNnMapTrim(map, name, "");
	}
	public static Object toNnMapTrim(Map<String,Object> map, String name, Object nullTo) {
		try{
			if (map.get(name)==null || map.get(name).toString().trim().equals("")) map.put(name, nullTo);
			return map.get(name);
		}
		catch(Exception e){return "";}
	}

	public static String commaNum(Object obj) {
		return NumberFormat.getInstance().format(obj);
	}

	public static int toInt(Object obj) {
		return toInt(obj,0);
	}
	public static int toInt(Object obj, int nullTo) {
		return Integer.parseInt(toNN(obj,nullTo));
	}
	public static long toLong(Object obj) {
		return toLong(obj,0);
	}
	public static long toLong(Object obj, long nullTo) {
		return Long.parseLong(toNN(obj,nullTo));
	}
	public static float toFloat(Object obj) {
		return toFloat(obj,0);
	}
	public static float toFloat(Object obj, float nullTo) {
		return Float.parseFloat(toNN(obj,nullTo));
	}
	public static double toDouble(Object obj) {
		return toDouble(obj,0);
	}
	public static double toDouble(Object obj, double nullTo) {
		return Double.parseDouble(toNN(obj,nullTo));
	}

	public static String nvl2(Object baseStr, Object notNullTo, Object nullTo) {
		Object oRtn = (baseStr==null || baseStr.equals("")) ? nullTo : notNullTo;
		return (oRtn==null) ? null : oRtn.toString();
	}
	public static String strTo(Object baseStr, Object fromStr, Object nullTo) {
		if(baseStr==null) return null;
		if(baseStr.equals(fromStr)) return (nullTo==null) ? null : nullTo.toString();
		else return baseStr.toString();
	}
	//javascript 의 encodeURIComponent 와 같게 문자열을 만듬
	public static String encodeURIComponent(String s) {
		String result = null;
		try {
			result = URLEncoder.encode(s, Constants.ENC_TYPE)
				.replaceAll("\\+", "%20")
				.replaceAll("\\%21", "!")
				.replaceAll("\\%27", "'")
				.replaceAll("\\%28", "(")
				.replaceAll("\\%29", ")")
				.replaceAll("\\%7E", "~");
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			result = s;
		}
		return result;
	}
	public static String decodeURIComponent(String s) {
		if (s == null) { return null; }
		String result = null;
		try {
			result = URLDecoder.decode(s, Constants.ENC_TYPE);
		}
		// This exception should never occur.
		catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			result = s;
		}
		return result;
	}
	public static String encUrl(String url) {
		try {
			if(url==null) return null;
			else return URLEncoder.encode(url, Constants.ENC_TYPE);
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			return url;
		}
	}
	public static String decUrl(String url) {
		try {
			if(url==null) return null;
			else return URLDecoder.decode(url, Constants.ENC_TYPE);
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			return url;
		}
	}
	public static String rTrim(String str){
		char[] val = str.toCharArray();
		int idx = 0;
		int len = str.length();
		while (idx < len && val[len-1] <= ' '){
			len--;
		}
		return str.substring(0, len);
	}
	public static String lTrim(String str){
		char[] val = str.toCharArray();
		int idx  = 0;
		int len = str.length();
		while (idx < len && val[idx] <= ' '){
			idx++;
		}
		return str.substring(idx, len);
	}
	public static boolean in(Object baseStr, Object ... inStrs){
		for(Object inStr : inStrs){if(baseStr.equals(inStr)) return true;}
		return false;
	}

	//아래 toNN 사용
	public static String nullTo(Object baseStr, Object toStr) {
		if(baseStr==null || baseStr.equals("")) return (toStr==null) ? null : toStr.toString();
		else return baseStr.toString();
	}
	public static String notNullTo(Object baseStr, Object toStr) {
		if(baseStr!=null && !baseStr.equals("")) return (toStr==null) ? null : toStr.toString();
		else return toNN(baseStr);
	}

	//한글이 섞인 문자열에서 길이로 짜르기(길어지지 않게 자른다)
	public static String getSubStrByByteLen(String sBase, int sIdx, int len){
		int eIdx = sIdx + len;
		String sRtn = "";
		char[] chr = sBase.replaceAll("\r\n","\n").toCharArray();
		int j=0;
		try{
			for (int i=0;i<eIdx;i++) {
				if (i>=sIdx) sRtn += chr[j];
				if (chr[j]>127) {
					i++;
					if(i>=eIdx) sRtn = sRtn.substring(0,sRtn.length()-1);
				}
				j++;
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return sRtn;
		}
		return sRtn;
	}

	//반각영문숫자를 전각 영문숫자로 변환한다.
	public static String convertToJunkak(String value) {
		StringBuilder sb = new StringBuilder(value);
		for (int i=0; i<sb.length(); i++) {
			int c = sb.charAt(i);
			if ((c >= 0x30 && c <= 0x39) || (c >= 0x41 && c <= 0x5A) || (c >= 0x61 && c <= 0x7A)) {
				sb.setCharAt(i, (char) (c + 0xFEE0));
			}
		}
		value = sb.toString();
		return value;
	}

	//전각영문숫자를 반각영문숫자로 변환한다. (특수기호는 안됨 -> 기호도 바뀔 수 있게 찾아보기)
	public static String convertToBankak(String value) {
		StringBuilder sb = new StringBuilder(value);
		for (int i=0; i<sb.length(); i++) {
			int c = sb.charAt(i);
			if ((c >= 0xFF10 && c <= 0xFF19) || (c >= 0xFF21 && c <= 0xFF3A) || (c >= 0xFF41 && c <= 0xFF5A)) {
				sb.setCharAt(i, (char) (c - 0xFEE0));
			}
		}
		value = sb.toString();
		return value;
	}

	public static String getXmlValue(String xml, String name) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			Element element = document.getDocumentElement();

			NodeList list = element.getElementsByTagName(name);
			return list.item(0).getChildNodes().item(0).getNodeValue();
		}
		catch (Exception e) {
			return "";
		}
	}

	//String 문자열을 계산한다 (예:2/3*12)
	public static double calcToDouble(String input) {
		try {
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			return (Double) engine.eval(input);
		}
		catch (ScriptException e) {
			return 0;
		}
	}
	public static int calcToInt(String input) {
		try {
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			return (int) engine.eval(input);
		}
		catch (ScriptException e) {
			return 0;
		}
	}
	//Object 를 String으로 변환하여 리턴한다.
	//@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String objToStr(Object obj) {
		String resultStr = "";
		//if		(obj instanceof List)	convertToJsonScriptValue((List)obj);
		//else if	(obj instanceof Map)	convertToJsonScriptValue((Map)obj);

		try {
			resultStr = new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception e) {
			logger.error(e.toString());
		}
		return resultStr;
	}
	// 브라우저 긴숫자 및 Date 타입을 인식하지 못하는 문제 해결
	public static List<Map<String, Object>> convertToJsonScriptValue(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			convertToJsonScriptValue(map);
		}
		return list;
	}
	// 브라우저 긴숫자 및 Date 타입을 인식하지 못하는 문제 해결
	public static Map<String, Object> convertToJsonScriptValue(Map<String, Object> map) {
		if (isEmpty(map)) return map;
		for (String key : map.keySet()) {
			if (map.get(key) instanceof Long) {
				// 아주 긴 숫자 String 타입으로 변경
				if (((Long) map.get(key)) > 999999999999999.0) map.put(key, map.get(key).toString());
			}
			else if (map.get(key) instanceof Date) {
				// Date 타입 String 타입으로 변경
				map.put(key, map.get(key).toString());
			}
		}
		return map;
	}

	//List 내 keyField, valueField 을 가진 key를 찾아서 그 value를 Map으로 리턴 한다.
	//로컬에서 받으면 키 순서 보장 안됨
	public static Map<String, Object> listToMap(List<Map<String, Object>> list, String keyField, String valueField) {
		Map<String, Object> returnMap = new LinkedHashMap<>();
		for (Map<String, Object> map : list) {
			returnMap.put(toNN(map.get(keyField)), map.get(valueField));
		}
		return returnMap;
	}
	//브라우져로 아래 listToMap 값을 그대로 던지면 json은 key:value  순서를 보장하지 않기 때문에 이 함수를 사용 해야 한다.
	public static List<Map<String, Object>> listToMapArray(List<Map<String, Object>> list, String keyField, String valueField) {
		List<Map<String, Object>> rsltList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			Map<String, Object> rsltMap = new HashMap<>();
			rsltMap.put(toNN(map.get(keyField)), map.get(valueField));
			rsltList.add(rsltMap);
		}
		return rsltList;
	}

	//로컬에서 받으면 키 순서 보장 안됨
	public static Map<String, Object> listToMap(List<Map<String, Object>> list, String[] keyFields, String keyDelimiter, String valueField) {
		Map<String, Object> returnMap = new LinkedHashMap<>();
		for (Map<String, Object> map : list) {
			String key = "";
			for(String keyField : keyFields) {
				key += keyDelimiter + toNN(map.get(keyField));
			}
			returnMap.put(key.substring(keyDelimiter.length()), map.get(valueField));
		}
		return returnMap;
	}
	//브라우져로 아래 listToMap 값을 그대로 던지면 json은 key:value  순서를 보장하지 않기 때문에 이 함수를 사용 해야 한다.
	public static List<Map<String, Object>> listToMapArray(List<Map<String, Object>> list, String[] keyFields, String keyDelimiter, String valueField) {
		List<Map<String, Object>> rsltList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			Map<String, Object> rsltMap = new HashMap<>();
			String key = "";
			for(String keyField : keyFields) {
				key += keyDelimiter + toNN(map.get(keyField));
			}
			rsltMap.put(key.substring(keyDelimiter.length()), map.get(valueField));
			rsltList.add(rsltMap);
		}
		return rsltList;
	}

	/**
	 * null 이거나 빈문자열 인지 체크
	 * @param obj Object
	 * @return null 이거나 객체가 비어 있음
	 * ObjectUtils.isEmpty 와 동일
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null || obj.equals("")) return true;
		else if (obj instanceof String) {
			return obj.toString().length()==0;
		}
		else if (obj instanceof Collection<?> collection) {
			return collection.isEmpty();
		}
		else if (obj instanceof Map<?, ?> map) {
			return map.isEmpty();
		}
		else if (obj instanceof Optional<?> optional) {
			return optional.isEmpty();
		}
		else if (obj instanceof CharSequence charSequence) {
			return charSequence.isEmpty();
		}
		else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		else return false;
	}

	//DB 입력시 빈문자열을 Null로 대체
	public static Map<String, Object> setBlankToNull(Map<String, Object> map) {
		for (String key : map.keySet()) {
			if ("".equals(map.get(key))) map.put(key, null);
		}
		return map;
	}

	//inputStream 을 String 으로 변환하기
	public static String inputStreamToStr(InputStream is) {
		return inputStreamToStr(is, Constants.ENC_TYPE);
	}
	public static String inputStreamToStr(InputStream is, String charsetName) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName(charsetName)));
		String str = br.lines().collect(Collectors.joining("\n")); // \n = System.lineSeparator()
		try {br.close();} catch (IOException e) {logger.error(e.toString());}
		return str;
	}
	//charset 없이 읽기
	public static String inputStreamToStrNotcharset(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String str = br.lines().collect(Collectors.joining("\n")); // \n = System.lineSeparator()
		try {br.close();} catch (IOException e) {logger.error(e.toString());}
		return str;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> voToMap(Object vo){
		return new ObjectMapper().convertValue(vo, Map.class);
	}
	public static <T> T mapToVo(Object vo, Class<T> cls){
		return new ObjectMapper().convertValue(vo, cls);
	}

	// fromObj -> toObj 로 값을 복사한다.
	@SuppressWarnings("unchecked")
	public static <T> T voToVo(Object fromObj, Object toObj){
		try {
			Field[] fromObjFields = fromObj.getClass().getDeclaredFields();

			List<Method> toClsSetMethods = Stream.of(toObj.getClass().getDeclaredMethods()).filter(method -> method.getName().startsWith("set")).collect(Collectors.toList());
			//아래 JDK 상위버전에서는 가능함
			//List<Method> toClsSetMethods = Stream.of(toObj.getClass().getDeclaredMethods()).filter(method -> method.getName().startsWith("set")).toList();

			for (Field fromObjField : fromObjFields) {
				fromObjField.setAccessible(true);
				toClsSetMethods.stream().filter(method -> method.getName().replaceFirst("set", "").toLowerCase().equals(fromObjField.getName())).forEach(method -> {
					try {
						method.invoke(toObj, fromObjField.get(fromObj));
					}
					catch (Exception e) {}
				});
			}
			return (T) toObj;
		}
		catch(Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	// fromObj 에서 toCls 타입의 새로운 VO에 값을 복사해서 리턴한다.
	public static <T> T voToVo(Object fromObj, Class<T> toCls){
		try {
			return voToVo(fromObj, toCls.getConstructor().newInstance());
		}
		catch(Exception e) {
			logger.error(e.toString());
			return null;
		}
	}
}
