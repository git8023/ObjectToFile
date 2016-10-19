package org.yong.commons.component;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 对象属性配置类
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class AttributeConfigure implements Comparable<AttributeConfigure> {

    /**
     * 可配置属性列表
     * 
     * @author Huang.Yong
     * @version 0.1
     */
    public enum Attributable {
        /** 属性名 */
        NAME,
        /** 表头文本 */
        TEXT,
        /** 前缀 */
        SUFFIX,
        /** 后缀 */
        PREFIX,
        /** 属性值长度 */
        SIZE,
        /** 日期格式化 */
        FORMATTER,
        /** 右对齐方式 */
        RIGHT_ALIGN {
            public String getAttrName() {
                return "rightAlign";
            }
        };

        /**
         * 获取配置属性名
         * 
         * @return 属性名
         */
        public String getAttrName() {
            return this.name().toLowerCase();
        }
    };

    /**
     * 长度集合
     * 
     * @author Huang.Yong
     * @version 0.1
     */
    private class Sizes {
        private int preLen;

        private int size;

        private int sufLen;

        public Sizes(int preLen, int size, int sufLen) {
            super();
            this.preLen = preLen;
            this.size = size;
            this.sufLen = sufLen;
        }

    }

    private Sizes sizes;

    private String name;

    private String text;

    private String suffix;

    private String prefix;

    private int size;

    private int ordinal;

    private String value;

    private String formatter;

    private boolean rightAlign;

    /**
     * 获取标题文本, 带前缀和后缀
     * 
     * @return 标题文本
     */
    public String getTitleText() {
        String txt = this.getText();
        int size = this.getSize();
        int offset = size - txt.length();
        while (--offset >= 0)
            txt += " ";

        return this.getPrefix() + txt + this.getSuffix();
    }

    @Override
    public int compareTo(AttributeConfigure o) {
        return this.ordinal - o.ordinal;
    }

    /**
     * 获取表头中包含非ASCii码的数量
     * 
     * @return 表头中包含非ASCii码的数量
     */
    public int getOtherCharSize() {
        String regex = "([^\\x00-\\xff])";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(this.getText());
        int count = 0;
        while (m.find())
            count++;
        return count;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = StringUtils.trim(name);
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * @param ordinal the ordinal to set
     */
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the formatter
     */
    public String getFormatter() {
        return formatter;
    }

    /**
     * @param formatter the formatter to set
     */
    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    /**
     * @return the rightAlign
     */
    public boolean isRightAlign() {
        return rightAlign;
    }

    /**
     * @param rightAlign the rightAlign to set
     */
    public void setRightAlign(boolean rightAlign) {
        this.rightAlign = rightAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AttributeConfigure [name=" + name + ", text=" + text + ", suffix=" + suffix + ", prefix=" + prefix + ", size="
                + size + ", ordinal=" + ordinal + ", value=" + value + ", formatter=" + formatter + ", rightAlign=" + rightAlign
                + "]";
    }

    /**
     * 获取单元格真实长度
     * 
     * @return 长度
     */
    public int getRealLength() {
        Sizes currSizes = getSizes();
        return currSizes.preLen + currSizes.size + currSizes.sufLen;
    }

    /**
     * 获取长度集
     * 
     * @return 长度集
     */
    private Sizes getSizes() {
        if (null == this.sizes) {
            int preLen = StringUtils.trimToEmpty(this.getPrefix()).length();
            int size = this.getSize();
            int sufLen = StringUtils.trimToEmpty(this.getSuffix()).length();
            this.sizes = new Sizes(preLen, size, sufLen);
        }
        return this.sizes;
    }

    /**
     * 获取真实值
     * 
     * @param origVal 文本原始值
     * @return 真实值
     */
    public String getRealValue(String origVal) {
        Sizes currSizes = getSizes();
        return origVal.substring(currSizes.preLen, origVal.length() - currSizes.sufLen);
    }

    /**
     * 解析配置文件
     * 
     * @param xmlFile 配置文件
     * @return 属性配置列表
     */
    public static List<AttributeConfigure> parseConfig(File xmlFile) {
        // TODO
        return null;
    }
}
