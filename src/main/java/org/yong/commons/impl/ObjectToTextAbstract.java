package org.yong.commons.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.component.AttributeConfigure.Attributable;
import org.yong.commons.component.StringConverterMap;
import org.yong.commons.exception.AccessException;
import org.yong.commons.iface.convertors.StringConvertor;
import org.yong.commons.iface.listeners.TextListener;
import org.yong.commons.iface.text.ObjectToText;
import org.yong.commons.impl.convertors.DateToStringConvertor;
import org.yong.commons.utils.file.FileUtil;
import org.yong.util.file.xml.XMLObject;
import org.yong.util.file.xml.XMLParser;
import org.yong.util.string.StringUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 对象转文本文件基础类
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 目标对象类型
 */
public abstract class ObjectToTextAbstract<T> implements ObjectToText<T> {

    private XMLObject root;

    private final List<AttributeConfigure> cellsConfig = Lists.newArrayList();

    private String exportFilePath;

    private File xmlConf;

    private TextListener<T> listener;

    private static final StringConverterMap converterMap = new StringConverterMap();

    public ObjectToTextAbstract() {
        this((String) null, (String) null);
    }

    /**
     * @param exportFilePath 导出文件路径
     * @param xmlConfPath XML配置文件路径
     */
    public ObjectToTextAbstract(String exportFilePath, String xmlConfPath) {
        this(exportFilePath, FileUtil.getFileFromClassPath(xmlConfPath));
    }

    /**
     * @param exportFilePath 导出文件路径
     * @param xmlConf XML配置文件
     */
    public ObjectToTextAbstract(String exportFilePath, File xmlConf) {
        super();
        this.exportFilePath = exportFilePath;
        this.xmlConf = xmlConf;
        setExportFilePath(exportFilePath);
        setXmlConfPath(this.xmlConf);
        registerConvertors();
    }

    /**
     * 注册转换器
     */
    private void registerConvertors() {
        this.registerConvertor(Date.class, new DateToStringConvertor());
    }

    @Override
    public void setXmlConfPath(File xmlConfFile) {
        try {
            XMLParser parser = new XMLParser(xmlConfFile.getAbsolutePath());
            root = parser.parse();
            parseXMLConf();
            this.xmlConf = xmlConfFile;
        } catch (Exception e) {
            throw new AccessException("Parse file error", e, xmlConfFile);
        }
    }

    @Override
    public void setExportFilePath(String exportFilePath) {
        exportFilePath = StringUtils.trimToEmpty(exportFilePath);
        if (StringUtil.isEmpty(exportFilePath, true)) {
            throw new IllegalArgumentException("Invalid export file path.");
        }
        this.exportFilePath = exportFilePath;
    }

    /**
     * 解析XML配置文件为配置对象
     */
    private void parseXMLConf() {
        List<XMLObject> titles = root.getChildTags("title");
        for (int i = 0, len = titles.size(); i < len; i++) {
            XMLObject xmlTitleConfig = titles.get(i);
            AttributeConfigure conf = parseTitleConfig(xmlTitleConfig);

            if (null != conf) {
                conf.setOrdinal(i);
                this.cellsConfig.add(conf);
            }
        }
    }

    /**
     * 解析标题配置
     * 
     * @param xmlTitleConfig XML标题配置
     * @return 框架配置对象实例
     */
    private AttributeConfigure parseTitleConfig(XMLObject xmlTitleConfig) {
        Map<String, String> attrConf = Maps.newHashMap();
        for (Attributable attrItem : Attributable.values()) {
            String attrName = attrItem.getAttrName();
            String attrConfVal = xmlTitleConfig.getAttr(attrName);
            attrConf.put(attrName, StringUtils.trimToEmpty(attrConfVal));
        }

        if (MapUtils.isEmpty(attrConf))
            return null;

        try {
            AttributeConfigure conf = new AttributeConfigure();
            BeanUtils.copyProperties(conf, attrConf);
            return conf;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid configuration", e);
        }
    }

    /**
     * 获取属性配合列表
     * 
     * @return 属性配置列表
     */
    protected List<AttributeConfigure> getCellsConfig() {
        return cellsConfig;
    }

    @Override
    public File export(Iterable<T> beans) throws AccessException {
        File expFile = new File(this.exportFilePath);
        try {
            writeTitle(expFile);
            writeRows(expFile, beans);
        } catch (Exception e) {
            throw new AccessException("Export file error", e);
        }
        return expFile;
    }

    /**
     * 写入数据行
     * 
     * @param expFile 导出文件
     * @param beans 数据列表
     * @throws IOException
     */
    private void writeRows(File expFile, Iterable<T> beans) throws IOException {
        int cachedRows = 50;
        int dataRowCount = 0;

        StringBuilder buffer = new StringBuilder();
        for (T bean : beans) {
            // 行数据处理前
            beforeBeanConvert(bean);
            String content = convert(bean);
            // TODO 行数据处理后
            content = beforeRowContentAppend(content, bean);
            content = StringUtils.trimToEmpty(content);
            buffer.append(content).append(FileUtil.LINE_SEPARATOR);
            if (0 == dataRowCount % cachedRows) {
                FileUtils.write(expFile, buffer, true);
                buffer.setLength(0);
            }
        }

        if (0 < buffer.length()) {
            FileUtils.write(expFile, buffer, true);
        }
    }

    /**
     * 写入表头
     * 
     * @param expFile 导出的文件
     * @throws IOException
     */
    private void writeTitle(File expFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (AttributeConfigure attrConf : cellsConfig) {
            sb.append(attrConf.getTitleText());
        }
        sb.append(FileUtil.LINE_SEPARATOR);
        FileUtils.write(expFile, sb.toString());
    }

    @Override
    public <E> void registerConvertor(Class<E> clazz, StringConvertor<E> convertor) {
        converterMap.put(clazz, convertor);
    }

    @Override
    public <E> StringConvertor<E> getConvertor(Class<?> clazz) {
        return converterMap.get(clazz);
    }

    @Override
    public void registerListener(TextListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 列数据前置处理
     * 
     * @param name 属性名
     * @param propVal 列值
     * @param ordinal 配置中的序列
     * @return 处理后列值
     */
    protected Object beforeColumnConvert(String name, Object propVal, int ordinal) {
        if (null != this.listener)
            return this.listener.beforeColumnConvert(name, propVal, ordinal);
        return propVal;
    }

    /**
     * 列值添加追加潜质处理器
     * 
     * @param name 属性名
     * @param val 列值
     * @param content 本行已追加列值
     * @return 本列列值
     */
    protected String beforeColumnAppend(String name, String val, String content) {
        if (null != this.listener)
            return this.listener.beforeColumnAppend(name, val, content);
        return val;
    }

    /**
     * 对象转换前置处理器
     * 
     * @param bean 目标对象
     */
    private void beforeBeanConvert(T bean) {
        if (null != this.listener)
            this.listener.beforeBeanConvert(bean);
    }

    /**
     * 行数据添加前置处理器
     * 
     * @param content 行数据文本
     * @param bean 行对象
     * @return 处理后行数据文本
     */
    private String beforeRowContentAppend(String content, T bean) {
        if (null != this.listener)
            return this.listener.beforeRowContentAppend(content, bean);
        return content;
    }
}
