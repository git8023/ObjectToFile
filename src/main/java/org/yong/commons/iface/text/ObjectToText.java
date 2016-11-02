/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.iface.text;

import java.io.File;

import org.yong.commons.exception.AccessException;
import org.yong.commons.iface.ObjectToFile;
import org.yong.commons.iface.convertors.StringConvertor;
import org.yong.commons.iface.listeners.SimpleTextListener;

/**
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 目标对象类型
 */
public interface ObjectToText<T> extends ObjectToFile<T> {

    /**
     * 设置XML配置文件路径, 配置示例
     * 
     * <pre>
     *  &lt;conf &gt;&lt;!-- root label --&gt;
     *      &lt;title  prop-name   = "属性名" 
     *              text        = "列标题"
     *              suffix      = "列后缀"
     *              prefix      = "列前缀"
     *              max-size    = "列值最大长度, 本列总长度 = suffix.length() + prefix.length() + max-size.length()"
     *       /&gt;
     *   &lt;/conf &gt;
     * </pre>
     * 
     * @param xmlConfFile XML配置文件
     */
    public void setXmlConfPath(File xmlConfFile);

    /**
     * 对象转换为文本
     * 
     * @param bean 目标对象
     * @return 一行文本字符串
     */
    public String convert(T bean) throws AccessException;

    /**
     * 文本数据转换为对象
     * 
     * @param clazz 对象字节码
     * @param src 文本数据
     * @return 对象
     */
    public T convert(Class<T> clazz, String src) throws AccessException;

    /**
     * 转换器注册
     * 
     * @param clazz 转换器类型
     * @param convertor
     */
    <E> void registerConvertor(Class<E> clazz, StringConvertor<E> convertor);

    /**
     * 获取转换器
     * 
     * @param clazz 转换器类型
     * @return 转换器
     */
    <E> StringConvertor<E> getConvertor(Class<?> clazz);

    /**
     * 注册监听器
     * 
     * @param listener 监听器
     */
    public void registerListener(SimpleTextListener<T> listener);

}
