/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.impl.adapters;

import org.yong.commons.iface.listeners.SimpleTextListener;

/**
 * 文本监听器适配器
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class TextListenerAdapter<T> implements SimpleTextListener<T> {

    private T bean;

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

    @Override
    public void setBean(T bean) {
        this.bean = bean;
    }

    @Override
    public T getBean() {
        return this.bean;
    }
}
