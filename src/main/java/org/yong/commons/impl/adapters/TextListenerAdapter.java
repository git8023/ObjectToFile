package org.yong.commons.impl.adapters;

import org.yong.commons.iface.listeners.TextListener;

/**
 * 文本监听器适配器
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class TextListenerAdapter<T> implements TextListener<T> {

    @Override
    public Object beforeColumnConvert(String name, Object propVal, int ordinal) {
        return propVal;
    }

    @Override
    public String beforeColumnAppend(String name, String val, String content) {
        return val;
    }

    @Override
    public void beforeBeanConvert(T bean) {
    }

    @Override
    public String beforeRowContentAppend(String content, T bean) {
        return content;
    }

    @Override
    public boolean afterTextConverted(T bean, String textData) {
        return true;
    }

}
