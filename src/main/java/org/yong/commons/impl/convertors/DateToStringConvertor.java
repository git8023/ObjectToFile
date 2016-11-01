/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.impl.convertors;

import java.util.Date;

import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.iface.convertors.StringConvertor;
import org.yong.commons.utils.DateUtil;
import org.yong.util.string.StringUtil;

/**
 * 日期转换器
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class DateToStringConvertor implements StringConvertor<Date> {

    @Override
    public String convertString(Date date, AttributeConfigure conf) {
        String formatter = conf.getFormatter();
        return DateUtil.format(date, formatter);
    }

    @Override
    public Date convertTarget(String str, AttributeConfigure conf) {
        // 指定规则
        String pattern = conf.getFormatter();
        if (StringUtil.isNotEmpty(pattern, true))
            return DateUtil.parse(str, pattern);

        // 默认毫秒值
        try {
            return new Date(Long.valueOf(str));
        } catch (Exception ignore) {
        }

        return null;
    }

}
