/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.iface.scanner;

/**
 * 文本扫描器
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public interface DataScanner {

    /**
     * 识别指定文本的对象类型
     * 
     * @param textData 文本数据
     * @return 对象类型字节码, 返回null将丢弃当前文本数据
     */
    public Class<?> discernBeanType(String textData);

}
