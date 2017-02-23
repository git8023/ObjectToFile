/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.impl.text;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.yong.commons.component.AttributeConfigure;
import org.yong.commons.component.AttributeConfigure.Attributable;
import org.yong.commons.component.StringConverterMap;
import org.yong.commons.exception.AccessException;
import org.yong.commons.iface.convertors.PrimerConvertor;
import org.yong.commons.iface.convertors.StringConvertor;
import org.yong.commons.iface.listeners.SimpleTextListener;
import org.yong.commons.iface.text.ObjectToText;
import org.yong.commons.impl.convertors.DateToStringConvertor;
import org.yong.commons.utils.file.FileUtil;
import org.yong.util.file.xml.XMLObject;
import org.yong.util.file.xml.XMLParser;
import org.yong.util.string.StringUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 对象转文本文件基础类, 支持监听器实现 : <br>
 * 对象数据转换前置处理(
 * {@link org.yong.commons.iface.listeners.SimpleTextListener#beforeBeanConvert}
 * ) <br>
 * 行文本追加前置处理(
 * {@link org.yong.commons.iface.listeners.SimpleTextListener#beforeRowContentAppend}
 * ),
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 目标对象类型
 * @see SimpleTextListener
 */
public abstract class ObjectToTextAbstract<T> implements ObjectToText<T> {

    private XMLObject                       root;

    private final List<AttributeConfigure>  CELLS_CONFIG = Lists.newArrayList();

    private String                          exportFilePath;

    private File                            xmlConf;

    private SimpleTextListener<T>           listener;

    private static final StringConverterMap converterMap = new StringConverterMap();
    static {
        registerPrimerConvertor(Integer.class, int.class);
        registerPrimerConvertor(Short.class, short.class);
        registerPrimerConvertor(Long.class, long.class);
        registerPrimerConvertor(Float.class, float.class);
        registerPrimerConvertor(Double.class, double.class);
        registerPrimerConvertor(Byte.class, byte.class);
        registerPrimerConvertor(Character.class, char.class);
        registerPrimerConvertor(Boolean.class, boolean.class);
    }

    /**
     * 注册基础数据类型转换器
     * 
     * @param boxClass 装箱类型
     * @param primerClass 拆箱类型
     */
    private static <T, E> void registerPrimerConvertor(Class<T> boxClass, Class<E> primerClass) {
        final StringConvertor<T> mainConvertor = PrimerConvertor.newInstance(boxClass);
        converterMap.put(boxClass, mainConvertor);
        converterMap.put(primerClass, new StringConvertor<E>() {

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

    /** true-打印首行标题 */
    private boolean showTitle;

    /** true-追加内容, false-覆盖内容 */
    private Boolean appendContent;

    public ObjectToTextAbstract() {
        this((String) null, (String) null);
    }

    /**
     * @param xmlConf XML配置文件
     */
    public ObjectToTextAbstract(File xmlConf) {
        super();
        this.xmlConf = xmlConf;
        setXmlConfPath(this.xmlConf);
        registerConvertors();
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
        this(xmlConf);
        this.exportFilePath = exportFilePath;
        setExportFilePath(exportFilePath);
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
        parseRootConf(root);
        for (int i = 0, len = titles.size(); i < len; i++) {
            XMLObject xmlTitleConfig = titles.get(i);
            AttributeConfigure conf = parseTitleConfig(xmlTitleConfig);

            if (null != conf) {
                conf.setOrdinal(i);
                this.CELLS_CONFIG.add(conf);
            }
        }
    }

    /**
     * 初始化标记
     * 
     * @param xmlRoot XML根配置
     */
    private void parseRootConf(XMLObject xmlRoot) {
        this.showTitle = Boolean.valueOf(xmlRoot.getAttr("showTitle"));
        this.appendContent = Boolean.valueOf(xmlRoot.getAttr("append"));
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
    protected List<AttributeConfigure> getCellsConfigList() {
        return CELLS_CONFIG;
    }

    /**
     * 获取属性配置映射
     * 
     * @return 属性配置映射
     */
    protected Map<String, AttributeConfigure> createCellsConfigMap() {
        Map<String, AttributeConfigure> map = Maps.newHashMap();
        for (AttributeConfigure conf : CELLS_CONFIG)
            map.put(conf.getName(), conf);
        return map;
    }

    @Override
    public File export(Iterable<T> beans) throws AccessException {
        File expFile = FileUtil.createQuietly(this.exportFilePath, this.appendContent);
        try {
            if (this.showTitle)
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
            // 行数据处理后
            content = beforeRowContentAppend(content, bean);
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
        for (AttributeConfigure attrConf : CELLS_CONFIG) {
            sb.append(attrConf.getTitleText());
        }
        sb.append(FileUtil.LINE_SEPARATOR);
        FileUtils.write(expFile, sb.toString(), true);
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
    public void registerListener(SimpleTextListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 列数据前置处理
     * 
     * @param name 属性名
     * @param propVal 列值
     * @param ordinal 配置中的序列
     * @param bean
     * @return 处理后列值
     */
    protected Object beforeColumnConvert(String name, Object propVal, int ordinal, T bean) {
        if (null != this.listener) {
            this.listener.setBean(bean);
            return this.listener.beforeColumnConvert(name, propVal, ordinal);
        }
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

    @Override
    public List<T> parse(Class<T> clazz, File src) throws AccessException {
        if (!(null != src && src.exists()))
            throw new AccessException("Invalid source file.");

        if (null == this.root)
            throw new AccessException("Missing XML configuration file.");

        if (null == this.CELLS_CONFIG)
            throw new AccessException("Invalid XML configuration file.");

        try {
            return parseFileToBeans(clazz, src);
        } catch (Exception e) {
            throw new AccessException(e);
        }
    }

    /**
     * 解析文件
     * 
     * @param src 源文件
     * @return 对象列表
     * @param clazz 对象字节码
     * @throws IOException
     */
    private List<T> parseFileToBeans(Class<T> clazz, File src) throws IOException {
        List<T> beans = Lists.newArrayList();

        List<String> rowDatas = FileUtils.readLines(src);
        if (CollectionUtils.isEmpty(rowDatas))
            return beans;

        for (String rowData : rowDatas) {
            T bean = convert(clazz, rowData);
            if (true == afterTextConverted(bean, rowData))
                beans.add(bean);
        }
        return beans;
    }

    /**
     * 文本数据转换成对象后
     * 
     * @param bean 目标对象
     * @param textData 文本数据
     * @return true-保存到列表中, false-跳过当前对象
     */
    private boolean afterTextConverted(T bean, String textData) {
        return (null == this.listener) ? true : this.listener.afterTextConverted(bean, textData);
    }

    /**
     * 是否展示表头
     * 
     * @return the showTitle
     */
    protected boolean isShowTitle() {
        return showTitle;
    }

}
