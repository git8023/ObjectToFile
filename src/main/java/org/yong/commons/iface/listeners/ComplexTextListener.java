/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.iface.listeners;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.yong.commons.impl.adapters.ComplexTextListenerAdapter;

/**
 * 复杂文本转换监听器
 * 
 * @author Huang.Yong
 * @version 0.12
 * @version 0.13 1.新增对象过滤接口; 2.新增适配器{@code ComplexTextListenerAdapter}
 * @see ComplexTextListenerAdapter
 */
public interface ComplexTextListener extends ConvertorListener {

    /**
     * 文件解析后执行
     * 
     * @param src 目标文件
     * @param typeBeans 解析后的不同类型数据列表
     */
    void afterFileParsed(File src, Map<Class<?>, List<?>> typeBeans);

    /**
     * 对象创建后过滤器
     * 
     * @param rowContent 行内容
     * @param clazz 对象类型
     * @param bean 对象实例
     * @return true-包含对象, false-排除对象
     */
    <E> boolean beanFilter(String rowContent, Class<E> clazz, E bean);
}
