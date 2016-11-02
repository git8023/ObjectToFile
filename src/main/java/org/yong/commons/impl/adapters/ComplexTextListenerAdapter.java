/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.impl.adapters;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.yong.commons.iface.listeners.ComplexTextListener;

/**
 * 复杂监听器适配器
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class ComplexTextListenerAdapter implements ComplexTextListener {

    @Override
    public void afterFileParsed(File src, Map<Class<?>, List<?>> typeBeans) {
        return;
    }

    @Override
    public <E> boolean beanFilter(String rowContent, Class<E> clazz, E bean) {
        return true;
    }

}
