package com.innon.education.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateLib {
	// Unix timestamp 를 Date로 변환하는 함수
	public static Date unixTimeToDate(Object obj) {
		long timestamp = Long.valueOf(String.valueOf(obj));
		Date date = new java.util.Date(timestamp * 1000L);
		return date;
	}
	//Date -> Unix timestamp
	public static long dateToUnixTime() {
		return dateToUnixTime(new Date());
	}
	public static long dateToUnixTime(Date date) {
		return date.getTime() / 1000;
	}

	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
	    return java.sql.Timestamp.valueOf(localDateTime);
	}
	public static LocalDateTime dateToLocalDateTime(Date date) {
	    return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
	}

	public static String getDateStr(){
		return dateToStr(new Date(),0, 0, 0, "yyyyMMdd");
	}
	public static String getDateStr(String sFormat){
		return dateToStr(new Date(),0, 0, 0, sFormat);
	}
	public static String getDateStr(int gapMonth, int gapDay, String sFormat){
		return dateToStr(new Date(),gapMonth, gapDay, 0, sFormat);
	}
	public static String getDateStr(int gapMonth, int gapDay, int fixDay, String sFormat){
		return dateToStr(new Date(),gapMonth, gapDay, fixDay, sFormat);
	}

	public static String dateToStrDefault(Object oDate){
		return dateToStr(oDate,0, 0, 0, "yyyy-MM-dd HH:mm:ss");
	}
	public static String dateToStr(Object oDate){
		return dateToStr(oDate,0, 0, 0, "yyyyMMddHHmmss");
	}
	public static String dateToStr(Object oDate, String sFormat){
		return dateToStr(oDate,0, 0, 0, sFormat);
	}
	public static String dateToStr(Object oDate, int gapMonth, int gapDay, int fixDay, String sFormat){
		return dateToStr(oDate, gapMonth, gapDay, 0, 0, 0, fixDay, sFormat);
	}
	public static String dateToStr(Object oDate, int gapMonth, int gapDay, int gapHour, int gapMinute, int gapSec, int fixDay, String sFormat){
		try{
			Date date = (oDate instanceof Date) ? (Date) oDate : (oDate instanceof LocalDateTime) ? localDateTimeToDate((LocalDateTime)oDate) : strToDate(oDate.toString(),"yyyyMMddHHmmss");
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.MONTH,gapMonth);
			cal.add(Calendar.DATE,gapDay);
			cal.add(Calendar.HOUR,gapHour);
			cal.add(Calendar.MINUTE,gapMinute);
			cal.add(Calendar.SECOND,gapSec);

			SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
			if(fixDay==99){
				cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			}
			else if(fixDay!=0){
				cal.set(Calendar.DATE, fixDay);
			}
			return sdf.format(cal.getTime());
		}
		catch(Exception e) {return null;}
	}
	public static LocalDateTime strToLocalDateTime(String sDate){
		return strToLocalDateTime(sDate, "yyyyMMddHHmmss");
	}
	public static LocalDateTime strToLocalDateTime(String sDate, String sFormat){
		return dateToLocalDateTime(strToDate(sDate, sFormat));
	}
	public static Date strToDate(String sDate){
		return strToDate(sDate, "yyyyMMddHHmmss");
	}
	public static Date strToDate(String sDate, String sFormat){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
			return sdf.parse(toOnlyNumberDateStr(sDate));
		}
		catch(Exception e) {return null;}
	}
	public static long getGapMilliTime(Object oFromData, Object oToData){
		Date fromData = (oFromData instanceof Date) ? (Date) oFromData : (oFromData instanceof LocalDateTime) ? localDateTimeToDate((LocalDateTime)oFromData) : strToDate(oFromData.toString(),"yyyyMMddHHmmss");
		Date toData	  = (oToData instanceof Date)   ? (Date) oToData   : (oToData instanceof LocalDateTime)   ? localDateTimeToDate((LocalDateTime)oToData)   : strToDate(oToData.toString()  ,"yyyyMMddHHmmss");
		return toData.getTime() - fromData.getTime();
	}
	public static String getGapTimeStr(Object oFromData, Object oToData){
		long day = getGapTime(oFromData, oToData, "d");
		String time = getGapTimeFormat(oFromData, oToData, "HH:mm:ss");
		return ((day>0) ? ""+day+"D-" : "") + time;
	}
	public static long getGapTime(Object oFromData, Object oToData, String unit){
		try {
			Date fromData = (oFromData instanceof Date) ? (Date) oFromData : (oFromData instanceof LocalDateTime) ? localDateTimeToDate((LocalDateTime)oFromData) : strToDate(oFromData.toString(),"yyyyMMddHHmmss");
			Date toData	  = (oToData instanceof Date)   ? (Date) oToData   : (oToData instanceof LocalDateTime)   ? localDateTimeToDate((LocalDateTime)oToData)   : strToDate(oToData.toString()  ,"yyyyMMddHHmmss");
			long gapTime  = toData.getTime() - fromData.getTime();
			long calcTime = (unit.equals("d")) ? 1000*60*60*24 : (unit.equals("h")) ? 1000*60*60 : (unit.equals("m")) ? 1000*60 : (unit.equals("s")) ? 1000 : 1;
			return gapTime/calcTime;
		}
		catch(Exception e) {return 0;}
	}
	public static String getGapTimeFormat(Object oFromData, Object oToData, String format){
		try {
			Date fromData = (oFromData instanceof Date) ? (Date) oFromData : (oFromData instanceof LocalDateTime) ? localDateTimeToDate((LocalDateTime)oFromData) : strToDate(oFromData.toString(),"yyyyMMddHHmmss");
			Date toData	  = (oToData instanceof Date)   ? (Date) oToData   : (oToData instanceof LocalDateTime)   ? localDateTimeToDate((LocalDateTime)oToData)   : strToDate(oToData.toString()  ,"yyyyMMddHHmmss");
			long gapSec   = (toData.getTime() - fromData.getTime()) / 1000;

			Date date = strToDate("20000101000000");
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.SECOND, (int)gapSec);
			return dateToStr(cal.getTime(), format);
		}
		catch(Exception e) {return "";}
	}
	//문자열에 숫자만 빼고 나머지는 모두 삭제
	public static String toOnlyNumberDateStr(String sDate){
		if (sDate==null) return null;
		sDate = sDate.replaceAll(" ","").replaceAll("-","").replaceAll("\\.","").replaceAll(":","").replaceAll("T","");
		return (sDate.length()<14) ? DataLib.fillRight(sDate,"0",14) : sDate.substring(0,14);
	}
}
