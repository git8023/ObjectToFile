package org.yong.commons.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.iface.convertors.StringConvertor;

public class SimpleObjectToText<T> extends ObjectToTextAbstract<T> {

    public SimpleObjectToText() {
        super();
    }

    public SimpleObjectToText(String exportFilePath, File xmlConf) {
        super(exportFilePath, xmlConf);
    }

    public SimpleObjectToText(String exportFilePath, String xmlConfPath) {
        super(exportFilePath, xmlConfPath);
    }

    @Override
    public String convert(T bean) {
        List<AttributeConfigure> cellConfs = this.getCellsConfig();
        PropertyUtilsBean propertyUtils = BeanUtilsBean2.getInstance().getPropertyUtils();

        StringBuilder buffer = new StringBuilder();
        for (AttributeConfigure conf : cellConfs) {
            String name = conf.getName();
            String val = "";
            try {
                Object propVal = propertyUtils.getProperty(bean, name);
                // 列数据转换前
                propVal = beforeColumnConvert(name, propVal, conf.getOrdinal());
                val = valueToString(propVal, conf);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }

            val = reviseLength(val, conf);
            // 数据添加前
            val = beforeColumnAppend(name, val, buffer.toString());
            buffer.append(conf.getPrefix()).append(val).append(conf.getSuffix());
        }

        return buffer.toString();
    }

    /**
     * 值转换为字符串
     * 
     * @param propVal 属性值
     * @param conf 属性配置
     * @return 字符串形式属性值
     */
    private String valueToString(Object propVal, AttributeConfigure conf) {
        if (null == propVal)
            return "";
        Class<? extends Object> clazz = propVal.getClass();
        StringConvertor<Object> convertor = getConvertor(clazz);
        return (null == convertor) ? String.valueOf(propVal) : convertor.convertString(propVal, conf);
    }

    /**
     * 值长度修复
     * 
     * @param val 值
     * @param conf 配置
     * @return 修复后值长度
     */
    private String reviseLength(String val, AttributeConfigure conf) {
        if (null == val)
            val = "";

        String sVal = StringUtils.trimToEmpty(val);
        int valLen = sVal.length();

        int size = conf.getSize();
        int otherCharLen = conf.getOtherCharSize();
        size += otherCharLen;

        int offset = size - valLen;
        if (0 < offset) {
            StringBuilder buffer = new StringBuilder(sVal);
            while (--offset >= 0)
                buffer.append(" ");
            return buffer.toString();
        }

        return sVal;
    }
}
