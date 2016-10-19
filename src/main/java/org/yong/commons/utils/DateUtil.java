package org.yong.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public abstract class DateUtil {

    /**
     * 日期格式化
     * 
     * @param date 日期对象
     * @param pattern 格式化规则
     * @return 格式化字符串
     */
    public static String format(Date date, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化当前时间
     * 
     * @param pattern 格式化规则
     * @return 格式化字符串
     */
    public static String formatNow(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 日期字符串解析
     * 
     * @param dateStr 字符串
     * @param pattern 规则
     * @return 日期对象
     */
    public static Date parse(String dateStr, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern);
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
