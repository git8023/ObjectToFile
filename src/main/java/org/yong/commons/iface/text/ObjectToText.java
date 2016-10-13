package org.yong.commons.iface.text;

import org.yong.commons.iface.ObjectToFile;

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
     * @param xmlConfFile XML配置文件路径
     */
    public void setXmlConfPath(String xmlConfFile);

}
