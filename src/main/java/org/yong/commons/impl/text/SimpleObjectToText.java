/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.impl.text;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.exception.AccessException;
import org.yong.commons.iface.convertors.StringConvertor;
import org.yong.commons.iface.listeners.SimpleTextListener;
import org.yong.util.string.StringUtil;

import com.google.common.collect.Maps;

/**
 * 简单实现对象文本转换器, 支持监听器实现:
 * <ol>
 * <li>列数据转换前({@link SimpleTextListener#beforeColumnConvert})</li>
 * <li>列数据追加前({@link SimpleTextListener#beforeColumnAppend})</li>
 * </ol>
 * 
 * <pre>
 * <b>程序代码示例</b>
 * // 获取打印对象
 * String exportFilePath = &quot;d:/test.txt&quot;;
 * File fileFromClassPath = getFileFromClassPath(&quot;/conf.xml&quot;);
 * ObjectToText&lt;TestEntity&gt; objectToText = new SimpleObjectToText&lt;TestEntity&gt;(exportFilePath, fileFromClassPath);
 * 
 * // 注册监听器
 * objectToText.registerListener(new TextListenerAdapter&lt;TestEntity&gt;() {
 *     &#064;Override
 *     public String beforeColumnAppend(String name, String val, String content) {
 *         if (&quot;mac&quot;.equals(name))
 *             return MD5Helper.encode(content);
 *         return val;
 *     }
 * });
 * 
 * // 打印文件
 * Collection&lt;TestEntity&gt; list = getDataList();
 * File export = objectToText.export(list);
 * </pre>
 * 
 * <pre>
 * <b>XML配置</b>
 * &lt;conf >
 *   &lt;title  
 *           name      = "code"         属性名
 *           text      = "Code"         文本第一行展示的标题
 *           prefix    = ""             数据行前缀
 *           suffix    = "|"            数据行后缀
 *           size      = "4"            数据最大长度, 总长度=prefix.length+suffix.length+length, 
 *                                      如果code.length&lt;size设置空格后缀. <b>属性值包含非ASCII码时, 
 *                                      总长度=总长度+NON-ASC.length</b>
 *           formatter = "yyyyMMdd"     日期格式化规则
 *    />
 *    ...
 * &lt;/conf >
 * </pre>
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 实体对象类型
 * @see SimpleTextListener
 */
public class SimpleObjectToText<T> extends ObjectToTextAbstract<T> {

    public SimpleObjectToText() {
        super();
    }

    /**
     * @param xmlConf XML配置文件
     */
    public SimpleObjectToText(File xmlConf) {
        super(xmlConf);
    }

    /**
     * @param exportFilePath 导出文件路径
     * @param xmlConf XML配置文件
     */
    public SimpleObjectToText(String exportFilePath, File xmlConf) {
        super(exportFilePath, xmlConf);
    }

    /**
     * @param exportFilePath 导出文件路径
     * @param xmlConfPath XML配置文件路径
     */
    public SimpleObjectToText(String exportFilePath, String xmlConfPath) {
        super(exportFilePath, xmlConfPath);
    }

    @Override
    public String convert(T bean) {
        List<AttributeConfigure> cellConfs = this.getCellsConfigList();
        PropertyUtilsBean propertyUtils = BeanUtilsBean2.getInstance().getPropertyUtils();

        StringBuilder buffer = new StringBuilder();
        for (AttributeConfigure conf : cellConfs) {
            String name = conf.getName();
            String val = "";
            try {
                Object propVal = propertyUtils.getProperty(bean, name);
                // 列数据转换前
                propVal = beforeColumnConvert(name, propVal, conf.getOrdinal(), bean);
                val = valueToString(propVal, conf);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }

            val = reviseLength(val, conf);
            // 数据添加前
            val = beforeColumnAppend(name, val, buffer.toString());
            buffer.append(conf.getPrefix()).append(val).append(conf.getSuffix());
        }

        return buffer.toString();
    }

    /**
     * 值转换为字符串
     * 
     * @param propVal 属性值
     * @param conf 属性配置
     * @return 字符串形式属性值
     */
    private String valueToString(Object propVal, AttributeConfigure conf) {
        if (null == propVal)
            return "";
        Class<? extends Object> clazz = propVal.getClass();
        StringConvertor<Object> convertor = getConvertor(clazz);
        return (null == convertor) ? String.valueOf(propVal) : convertor.convertString(propVal, conf);
    }

    /**
     * 值长度修复
     * 
     * @param val 值
     * @param conf 配置
     * @return 修复后值长度
     */
    private String reviseLength(String val, AttributeConfigure conf) {
        if (null == val)
            val = "";

        String sVal = StringUtils.trimToEmpty(val);
        int valLen = sVal.length();

        int size = conf.getSize();
        if (isShowTitle()) {
            int otherCharLen = conf.getOtherCharSize();
            size += otherCharLen;
        }

        StringBuilder buffer = new StringBuilder(sVal);
        int offset = size - valLen;
        CharSequence offsetSpace = createOffsetSpace(offset, conf);
        buffer = conf.isRightAlign() ? buffer.insert(0, offsetSpace) : buffer.append(offsetSpace);
        return buffer.toString();
    }

    /**
     * 创建补偿空白字符
     * 
     * @param offset 字符串长度
     * @param conf 列配置
     * @return 补偿字符串
     */
    private CharSequence createOffsetSpace(int offset, AttributeConfigure conf) {
        if (0 > offset)
            offset = 0;
        StringBuilder buffer = new StringBuilder();
        while (--offset >= 0)
            buffer.append(conf.getPlaceholder());
        return buffer;
    }

    @Override
    public T convert(Class<T> clazz, String src) throws AccessException {
        Map<String, String> attrs = Maps.newHashMap();
        List<AttributeConfigure> attrConfs = getCellsConfigList();
        int begin = 0;
        for (AttributeConfigure conf : attrConfs) {
            String name = conf.getName();
            int cellLen = conf.getRealLength();
            int end = begin + cellLen;

            String val = substring(src, begin, end, true);
            val = conf.getRealValue(val);
            attrs.put(name, StringUtils.trimToEmpty(val));

            begin += cellLen;
        }

        return createInstance(clazz, attrs);
    }

    /**
     * 截取字符串
     * 
     * @param src 源
     * @param beginIndex 开始下标(包含)
     * @param endIndex 结束下标(不包含)
     * @param useEmpty true-源无效或截取长度无效时返回空字符串, false-返回null
     * @return 子字符串
     */
    private String substring(String src, int beginIndex, int endIndex, boolean useEmpty) {
        String srcStr = StringUtils.trimToEmpty(src);
        String subStr = StringUtils.substring(srcStr, beginIndex, endIndex);
        return (StringUtil.isEmpty(subStr, true) && useEmpty) ? StringUtils.EMPTY : subStr;
    }

    /**
     * 创建对象实例
     * 
     * @param clazz 对象字节码
     * @param attrs 属性集合
     * @return 包含属性集合的对象字节码
     */
    private T createInstance(Class<T> clazz, Map<String, String> attrs) {
        PropertyUtilsBean propertyUtils = BeanUtilsBean2.getInstance().getPropertyUtils();
        try {
            T bean = clazz.newInstance();
            Map<String, AttributeConfigure> configMap = createCellsConfigMap();
            for (Entry<String, String> me : attrs.entrySet()) {
                String name = me.getKey();
                Class<?> type = propertyUtils.getPropertyType(bean, name);

                Object val = me.getValue();
                StringConvertor<?> convertor = getConvertor(type);
                if (null != convertor)
                    val = convertor.convertTarget((String) val, configMap.get(name));

                propertyUtils.setProperty(bean, name, val);
            }
            return bean;
        } catch (Exception e) {
            throw new AccessException("Create bean instance error, " + e.getMessage(), e);
        }
    }
}
