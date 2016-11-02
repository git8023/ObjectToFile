/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.impl.text.multi;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.component.ConvertorRepostory;
import org.yong.commons.component.TypeConfigure;
import org.yong.commons.exception.AccessException;
import org.yong.commons.iface.convertors.StringConvertor;
import org.yong.commons.iface.listeners.ComplexTextListener;
import org.yong.commons.iface.scanner.DataScanner;
import org.yong.commons.iface.text.MultiObjectToText;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 多数据结构与文本转换处理
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class MultiObjectToTextImpl implements MultiObjectToText {
    private final List<TypeConfigure<?>>          typesConf;

    private final Map<Class<?>, TypeConfigure<?>> confMaps       = Maps.newHashMap();

    private static final PropertyUtilsBean        PROPERTY_UTILS = BeanUtilsBean2.getInstance().getPropertyUtils();

    private final DataScanner                     textScanner;

    /** 未知的文本数据 */
    private final List<String>                    UNKONW_DATAS   = Lists.newArrayList();

    /** 监听器 */
    private ThreadLocal<ComplexTextListener>      localListener  = new ThreadLocal<ComplexTextListener>();

    /**
     * @param typesConf 类型配置列表
     * @param textScanner 文本扫描器
     */
    public MultiObjectToTextImpl(List<TypeConfigure<?>> typesConf, DataScanner textScanner) {
        super();
        this.typesConf = Lists.newArrayList(typesConf);
        this.textScanner = textScanner;
        mappingConf();
    }

    @Override
    public ComplexTextListener registerListener(ComplexTextListener listener) {
        ComplexTextListener oldListener = localListener.get();
        localListener.set(listener);
        return oldListener;
    }

    /**
     * 映射配置列表
     */
    private void mappingConf() {
        for (TypeConfigure<?> conf : typesConf)
            confMaps.put(conf.getBeanClass(), conf);
    }

    @Override
    public Map<Class<?>, List<?>> parse(File src) throws AccessException {
        try {
            Map<Class<?>, List<String>> typeDataMap = parseFile(src);
            // 新增解析后监听器
            Map<Class<?>, List<?>> typeBeans = parseToBeans(typeDataMap);
            ComplexTextListener listener = localListener.get();
            if (null != listener) {
                listener.afterFileParsed(src, typeBeans);
            }
            return typeBeans;
        } catch (Exception e) {
            throw new AccessException(e);
        }
    }

    /**
     * 解析文本文件
     * 
     * @param src 源文件
     * @return 类型与行数据映射
     */
    private Map<Class<?>, List<String>> parseFile(File src) throws Exception {
        Map<Class<?>, List<String>> dataMap = Maps.newHashMap();

        List<String> lines = FileUtils.readLines(src);
        for (String textData : lines) {
            Class<?> beanClass = this.textScanner.discernBeanType(textData);
            if (null == beanClass) {
                this.UNKONW_DATAS.add(textData);
                continue;
            }

            List<String> list = dataMap.get(beanClass);
            if (null == list) {
                list = Lists.newArrayList();
                dataMap.put(beanClass, list);
            }

            list.add(textData);
        }

        return dataMap;
    }

    /**
     * 填充对象列表
     * 
     * @param typeDataMap 类型数据映射
     * @return 类型对象列表映射
     * @throws Exception
     */
    private Map<Class<?>, List<?>> parseToBeans(Map<Class<?>, List<String>> typeDataMap) throws Exception {
        Map<Class<?>, List<?>> beansMap = Maps.newLinkedHashMap();

        for (Entry<Class<?>, List<String>> me : typeDataMap.entrySet()) {
            Class<?> beanClass = me.getKey();

            List<?> beans = convertToBeans(beanClass, me.getValue());
            beansMap.put(beanClass, beans);
        }

        return beansMap;
    }

    /**
     * 文本数据列表转换为对象列表
     * 
     * @param beanClass 对象类型字节码
     * @param textList 文本列表
     * @return 对象列表
     * @throws Exception
     */
    private <E> List<E> convertToBeans(Class<E> beanClass, List<String> textList) throws Exception {
        List<E> beans = Lists.newArrayList();

        for (String textData : textList) {
            E bean = converToBean(beanClass, textData);

            ComplexTextListener listener = this.localListener.get();
            if (!(null == listener || listener.beanFilter(textData, beanClass, bean)))
                continue;
            beans.add(bean);
        }

        return beans;
    }

    /**
     * 文本转换为对象
     * 
     * @param beanClass 对象类型
     * @param textData 文本数据
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    private <E> E converToBean(Class<E> beanClass, String textData) throws Exception {
        TypeConfigure<E> conf = (TypeConfigure<E>) confMaps.get(beanClass);

        E bean = beanClass.newInstance();
        List<AttributeConfigure> attrConfs = conf.getAttrConfs();
        int start = 0;
        int end = 0;

        for (AttributeConfigure attrConf : attrConfs) {
            int len = attrConf.getRealLength();
            end = len + start;
            String textVal = StringUtils.substring(textData, start, end);
            textVal = attrConf.getRealValue(textVal);

            String name = attrConf.getName();
            Object val = parseTextVal(bean, name, textVal, attrConf);
            PROPERTY_UTILS.setProperty(bean, name, val);

            start = end;
        }
        return bean;
    }

    /**
     * 解析文本数据
     * 
     * @param bean 数据对象
     * @param fieldName 字段名
     * @param textVal 文本数据
     * @param conf 属性配置
     * @return 字段值
     */
    private Object parseTextVal(Object bean, String fieldName, String textVal, AttributeConfigure conf) throws Exception {
        Class<?> fieldType = PROPERTY_UTILS.getPropertyType(bean, fieldName);
        StringConvertor<?> convertor = ConvertorRepostory.getConvertor(fieldType);
        if (null != convertor)
            return convertor.convertTarget(textVal, conf);
        return null;
    }

    /**
     * 获取未识别的文本数据列表
     * 
     * @return 未识别的文本数据列表
     */
    public List<String> getKnowDatas() {
        return Lists.newArrayList(this.UNKONW_DATAS);
    }

    /**
     * 清空未识别的文本数据
     */
    public void clearKnowDatas() {
        this.UNKONW_DATAS.clear();
    }
}
