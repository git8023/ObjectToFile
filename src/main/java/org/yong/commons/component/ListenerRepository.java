/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.component;

import java.util.Map;

import org.yong.commons.iface.listeners.ConvertorListener;

import com.google.common.collect.Maps;

/**
 * 监听器仓库
 * 
 * @author Huang.Yong
 * @version 0.11
 */
@Deprecated
public abstract class ListenerRepository {

    private static final Map<Class<?>, ConvertorListener> REPOSITORY = Maps.newHashMap();

    /**
     * 获取监听器
     * 
     * @param clazz 监听器字节码
     * @return 监听器
     */
    public static <T extends ConvertorListener> T getListener(Class<T> clazz) {
        return clazz.cast(REPOSITORY.get(clazz));
    }

    /**
     * 监听器注册
     * 
     * @param clazz 监听器字节码
     * @param listener 监听器
     * @return 已存在的监听器, 如果没有总是返回null
     */
    public static <T extends ConvertorListener> T setListener(Class<T> clazz, ConvertorListener listener) {
        return clazz.cast(REPOSITORY.put(clazz, listener));
    }
}
