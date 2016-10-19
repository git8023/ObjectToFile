package org.yong.commons.iface.text;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.yong.commons.exception.AccessException;

/**
 * 多对象文本处理, 所有接口都会抛出{@link AccessException}
 * 
 * @author Huang.Yong
 * @version 0.1
 * @see AccessException
 */
public interface MultiObjectToText {

    /**
     * 解析源文件, 不规则文件
     * 
     * @param src 源文件
     * @return 对象映射集合
     * @throws AccessException
     */
    public Map<Class<?>, List<?>> parse(File src) throws AccessException;
}
