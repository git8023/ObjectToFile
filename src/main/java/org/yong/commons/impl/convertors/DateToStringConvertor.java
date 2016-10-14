package org.yong.commons.impl.convertors;

import java.util.Date;

import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.iface.convertors.StringConvertor;
import org.yong.commons.utils.DateUtil;

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
    public Date convertTarget(String str) {
        // FIXME 日期解析
        throw new RuntimeException("Can not supported");
    }

}
