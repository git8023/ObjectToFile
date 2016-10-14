package org.yong.commons.utils;

import java.text.DateFormat;
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

}
