/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.iface.convertors;

import org.yong.commons.component.AttributeConfigure;

/**
 * 字符串转换器
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 与字符串互转类型
 */
public interface StringConvertor<T> {

    /**
     * 目标类型转换为字符串
     * 
     * @param t 目标类型
     * @param conf 属性配置
     * @return 字符串
     */
    public String convertString(T t, AttributeConfigure conf);

    /**
     * 字符串转换为目标类型
     * 
     * @param str 字符串
     * @param conf 属性配置
     * @return 目标类型值
     */
    public T convertTarget(String str, AttributeConfigure conf);
}
