package org.yong.commons.component;

import java.util.Map;

import org.yong.commons.iface.convertors.StringConvertor;

import com.google.common.collect.Maps;

/**
 * 字符串转换器映射器
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 目标类型
 */
public class StringConverterMap {
    private final Map<Class<?>, StringConvertor<?>> CONVERTERS = Maps.newHashMap();

    /**
     * 获取转换器
     * 
     * @param clazz 转换器字节码
     * @return 转换器
     */
    @SuppressWarnings("unchecked")
    public <T> StringConvertor<T> get(Class<?> clazz) {
        return (StringConvertor<T>) CONVERTERS.get(clazz);
    }

    /**
     * 保存转换器
     * 
     * @param clazz 转换器字节码
     * @param convertor 转换器
     */
    public <T> void put(Class<T> clazz, StringConvertor<T> convertor) {
        CONVERTERS.put(clazz, convertor);
    }
}