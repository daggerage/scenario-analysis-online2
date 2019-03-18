package org.egc.sao.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_PURE = "yyyy_MM_dd_HH_mm_ss";


    /**
     * 按指定格式返回当前日期
     * @return 日期字符串
     */
    public static String getStandardDateNow(){
        return parseDateToString(new Date());
    }
    public static String getPureDateNow(){
        return parseDateToString(new Date(),PATTERN_PURE);
    }

    public static String parseStandardToPure(String standard){
        String pure=String.valueOf(standard);
        return pure.replaceAll("[-: ]","_");
    }
    public static String parseLocalDateTimeToString(LocalDateTime date,String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }
    /**
     * 获得当前时间offsetMinute分钟之后的日期字符串
     * @param offsetMinute
     * @return 日期字符串
     */
    public static String getTimeAfter(int offsetMinute){
        Date now=new Date(System.currentTimeMillis()+offsetMinute*60*1000);
        return parseDateToString(now);
    }

    /**
     * 将Date类型转为日期字符串
     * @param date 当前日期
     * @return 日期字符串
     */
    private static String parseDateToString(Date date){
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_STANDARD);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(date);
    }
    private static String parseDateToString(Date date,String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(date);
    }
}
