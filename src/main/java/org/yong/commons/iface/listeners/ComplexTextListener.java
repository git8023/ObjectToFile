/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.iface.listeners;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 复杂文本转换监听器
 * 
 * @author Huang.Yong
 * @version 0.12
 */
public interface ComplexTextListener extends ConvertorListener {

    /**
     * 文件解析后执行
     * 
     * @param src 目标文件
     * @param typeBeans 解析后的不同类型数据列表
     */
    void afterFileParsed(File src, Map<Class<?>, List<?>> typeBeans);

}
