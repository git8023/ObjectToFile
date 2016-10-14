package org.yong.commons.iface.listeners;

/**
 * 文本转换监听器
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 目标数据类型
 */
public interface TextListener<T> extends Listener<T> {

    /**
     * 列数据前置处理
     * 
     * @param name 属性名
     * @param propVal 列值
     * @param ordinal 配置中的序列
     * @return 处理后列值
     */
    Object beforeColumnConvert(String name, Object propVal, int ordinal);

    /**
     * 对象转换前置处理器
     * 
     * @param bean 目标对象
     */
    public void beforeBeanConvert(T bean);

    /**
     * 行数据添加前置处理器
     * 
     * @param content 行数据文本
     * @param bean 行对象
     * @return 处理后行数据文本
     */
    public String beforeRowContentAppend(String content, T bean);

    /**
     * 列值添加追加潜质处理器
     * 
     * @param name 属性名
     * @param val 列值
     * @param content 本行已追加列值
     * @return 本列列值
     */
    String beforeColumnAppend(String name, String val, String content);
}
