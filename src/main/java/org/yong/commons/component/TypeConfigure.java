/*
 * github: https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.component;

import java.io.File;
import java.util.List;

import org.yong.commons.exception.AccessException;
import org.yong.commons.utils.file.FileUtil;
import org.yong.util.file.xml.XMLObject;
import org.yong.util.file.xml.XMLParser;

import com.google.common.collect.Lists;

/**
 * 类型文件配置
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 配置文件所对应的数据结构类型
 */
public class TypeConfigure<T> {
    /** 标签名:title */
    private static final String      TAG_NAME_OF_TITLE                = "title";

    /** 标签名: conf */
    private static final String      TAG_NAME_OF_CONF                 = "conf";

    /** 属性配置: title.包含表头(首行) */
    private static final String      CONF_ATTR_NAME_OF_SHOW_TITLE     = "showTitle";

    /** 属性配置: title.追加的内容 */
    private static final String      CONF_ATTR_NAME_OF_APPEND_CONTENT = "appendContent";

    /** 属性配置: title.配置对应Java类型 */
    private static final String      CONF_ATTR_NAME_OF_BEAN_CLASS     = "beanClass";

    /** 属性配置: title.name */
    private static final String      TITLE_ATTR_NAME_OF_NAME          = "name";

    /** 属性配置: title.text */
    private static final String      TITLE_ATTR_NAME_OF_TEXT          = "text";

    /** 属性配置: title.prefix */
    private static final String      TITLE_ATTR_NAME_OF_PREFIX        = "prefix";

    /** 属性配置: title.suffix */
    private static final String      TITLE_ATTR_NAME_OF_SUFFIX        = "suffix";

    /** 属性配置: title.formatter */
    private static final String      TITLE_ATTR_NAME_OF_FORMATTER     = "formatter";

    /** 属性配置: title.size */
    private static final String      TITLE_ATTR_NAME_OF_SIZE          = "size";

    /** 属性配置: title.rightAlign */
    private static final String      TITLE_ATTR_NAME_OF_RIGHT_ALIGN   = "rightAlign";

    private Class<T>                 beanClass;

    private List<AttributeConfigure> attrConfs;

    private boolean                  showTitle;

    private boolean                  appendContent;

    /**
     * 获取类型配置构建器
     * 
     * @param multiConfPath 配置文件路径
     * @return 构建器
     */
    public static TypeConfigureBuilder builder(String multiConfPath) {
        return new TypeConfigureBuilder(multiConfPath);
    }

    /**
     * 配置文件构建器
     * 
     * @author Huang.Yong
     * @version 0.1
     */
    public static class TypeConfigureBuilder {
        private String confPath;

        /**
         * @param confPath 配置文件路径
         */
        public TypeConfigureBuilder(String confPath) {
            super();
            this.confPath = confPath;
        }

        /**
         * 构建类型配置列表
         * 
         * @return 类型配置列表
         */
        public List<TypeConfigure<?>> build() {
            File confFile = FileUtil.getFileFromClassPath(confPath);
            try {
                return parseFile(confFile);
            } catch (Exception e) {
                throw new AccessException("To parse the XML configuration file error", e, confPath);
            }
        }

        /**
         * 解析配置文件
         * 
         * @param confFile 配置文件
         * @return 类型配置列表
         * @throws Exception
         */
        private List<TypeConfigure<?>> parseFile(File confFile) throws Exception {
            XMLParser parser = new XMLParser(confFile.getAbsolutePath());
            XMLObject root = parser.parse();

            List<TypeConfigure<?>> typesConf = Lists.newArrayList();
            List<XMLObject> confs = root.getChildTags(TAG_NAME_OF_CONF);
            for (XMLObject conf : confs) {
                TypeConfigure<?> typeConf = createTypeConf(conf);
                typesConf.add(typeConf);
            }

            return typesConf;
        }

        /**
         * 创建类型配置对象
         * 
         * @param conf XML配置
         * @return 类型配置对象
         * @throws Exception
         */
        private <E> TypeConfigure<E> createTypeConf(XMLObject conf) throws Exception {
            TypeConfigure<E> tConf = new TypeConfigure<E>();
            setBaseConf(conf, tConf);

            List<AttributeConfigure> attrsConf = Lists.newArrayList();
            List<XMLObject> titles = conf.getChildTags(TAG_NAME_OF_TITLE);
            for (XMLObject attrXmlConf : titles)
                attrsConf.add(createAttrConf(attrXmlConf));
            tConf.setAttrConfs(attrsConf);

            return tConf;
        }

        /**
         * 设置配型配置
         * 
         * @param conf 类型XML配置
         * @param tConf 类型配置对象
         * @throws ClassNotFoundException
         */
        @SuppressWarnings("unchecked")
        private <E> void setBaseConf(XMLObject conf, TypeConfigure<E> tConf) throws ClassNotFoundException {
            tConf.setShowTitle(Boolean.valueOf(conf.getAttr(CONF_ATTR_NAME_OF_SHOW_TITLE)));
            tConf.setAppendContent(Boolean.valueOf(conf.getAttr(CONF_ATTR_NAME_OF_APPEND_CONTENT)));
            tConf.setBeanClass((Class<E>) Class.forName(conf.getAttr(CONF_ATTR_NAME_OF_BEAN_CLASS)));
        }

        /**
         * 创建属性配置对象
         * 
         * @param attrXmlConf 属性XML配置对象
         * @return 属性配置对象
         */
        private AttributeConfigure createAttrConf(XMLObject attrXmlConf) {
            AttributeConfigure attrConf = new AttributeConfigure();
            attrConf.setName(attrXmlConf.getAttr(TITLE_ATTR_NAME_OF_NAME));
            attrConf.setText(attrXmlConf.getAttr(TITLE_ATTR_NAME_OF_TEXT));
            attrConf.setPrefix(attrXmlConf.getAttr(TITLE_ATTR_NAME_OF_PREFIX));
            attrConf.setSuffix(attrXmlConf.getAttr(TITLE_ATTR_NAME_OF_SUFFIX));
            attrConf.setFormatter(attrXmlConf.getAttr(TITLE_ATTR_NAME_OF_FORMATTER));
            attrConf.setSize(Integer.valueOf(attrXmlConf.getAttr(TITLE_ATTR_NAME_OF_SIZE)));
            attrConf.setRightAlign(Boolean.valueOf(attrXmlConf.getAttr(TITLE_ATTR_NAME_OF_RIGHT_ALIGN)));
            return attrConf;
        }

    }

    /**
     * @return the beanClass
     */
    public Class<T> getBeanClass() {
        return beanClass;
    }

    /**
     * @param beanClass the beanClass to set
     */
    public void setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * @return the attrConfs
     */
    public List<AttributeConfigure> getAttrConfs() {
        return attrConfs;
    }

    /**
     * @param attrConfs the attrConfs to set
     */
    public void setAttrConfs(List<AttributeConfigure> attrConfs) {
        this.attrConfs = attrConfs;
    }

    /**
     * @return the showTitle
     */
    public boolean isShowTitle() {
        return showTitle;
    }

    /**
     * @param showTitle the showTitle to set
     */
    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * @return the appendContent
     */
    public boolean isAppendContent() {
        return appendContent;
    }

    /**
     * @param appendContent the appendContent to set
     */
    public void setAppendContent(boolean appendContent) {
        this.appendContent = appendContent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TypeConfigure [beanClass=" + beanClass + ", attrConfs=" + attrConfs + ", showTitle=" + showTitle
                + ", appendContent=" + appendContent + "]";
    }

}
