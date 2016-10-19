package org.yong.commons.component;

import org.yong.commons.iface.convertors.PrimerConvertor;
import org.yong.commons.iface.convertors.StringConvertor;

/**
 * 基础数据类型(文本转换器)
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public abstract class ConvertorStory {

    private static final StringConverterMap convertorMap = new StringConverterMap();
    static {
        registerPrimerConvertor(Integer.class, int.class);
        registerPrimerConvertor(Short.class, short.class);
        registerPrimerConvertor(Long.class, long.class);
        registerPrimerConvertor(Float.class, float.class);
        registerPrimerConvertor(Double.class, double.class);
        registerPrimerConvertor(Byte.class, byte.class);
        registerPrimerConvertor(Character.class, char.class);
        registerPrimerConvertor(Boolean.class, boolean.class);
        registerPrimerConvertor(String.class, String.class);
    }

    /**
     * 注册转换器
     * 
     * @param beanClass 类型字节码
     * @param convertor 转换器
     */
    public static <T> StringConvertor<T> registerConvertor(Class<T> beanClass, StringConvertor<T> convertor) {
        return convertorMap.put(beanClass, convertor);
    }

    /**
     * 获取基础类型转换器
     * 
     * @param primerClass 基础类型字节码
     * @return 转换器
     */
    public static <T> StringConvertor<T> getConvertor(Class<T> primerClass) {
        return convertorMap.get(primerClass);
    }

    /**
     * 注册基础数据类型转换器
     * 
     * @param boxClass 装箱类型
     * @param primerClass 拆箱类型
     */
    public static <T, E> void registerPrimerConvertor(Class<T> boxClass, Class<E> primerClass) {
        final StringConvertor<T> mainConvertor = PrimerConvertor.newInstance(boxClass);
        convertorMap.put(boxClass, mainConvertor);
        convertorMap.put(primerClass, new StringConvertor<E>() {

            @SuppressWarnings("unchecked")
            @Override
            public String convertString(E t, AttributeConfigure conf) {
                return mainConvertor.convertString((T) t, conf);
            }

            @SuppressWarnings("unchecked")
            @Override
            public E convertTarget(String str, AttributeConfigure conf) {
                return (E) mainConvertor.convertTarget(str, conf);
            }
        });
    }
}
