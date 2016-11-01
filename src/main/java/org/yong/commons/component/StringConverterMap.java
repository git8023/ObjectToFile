/*
 * github: https://github.com/git8023/ObjectToFile/
 */
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
     * @return 上一个转换器, 如果没有返回null
     */
    public <T> StringConvertor<T> put(Class<T> clazz, StringConvertor<T> convertor) {
        @SuppressWarnings("unchecked")
        StringConvertor<T> oldConvertor = (StringConvertor<T>) CONVERTERS.put(clazz, convertor);
        return oldConvertor;
    }
}