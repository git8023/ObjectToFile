/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.iface.convertors;

import java.lang.reflect.Method;

import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.exception.AccessException;

/**
 * 原始数据转换器
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class PrimerConvertor<T> implements StringConvertor<T> {

    private Class<T> clazz;

    public PrimerConvertor() {
        super();
    }

    public PrimerConvertor(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <E> StringConvertor<E> newInstance(Class<E> clazz) {
        return new PrimerConvertor<E>(clazz);
    }

    @Override
    public String convertString(T t, AttributeConfigure conf) {
        return String.valueOf(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convertTarget(String str, AttributeConfigure conf) {
        if (String.class == clazz)
            return (T) str;

        try {
            Method m = clazz.getMethod("valueOf", String.class);
            return (T) m.invoke(null, str.trim());
        } catch (Exception e) {
            throw new AccessException("Attribute convert error", e);
        }
    }
}
