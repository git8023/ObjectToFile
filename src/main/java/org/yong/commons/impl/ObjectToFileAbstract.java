package org.yong.commons.impl;

import java.io.File;
import java.util.Collection;

import org.yong.commons.exception.AccessException;
import org.yong.commons.iface.text.ObjectToText;
import org.yong.util.file.xml.XMLObject;
import org.yong.util.file.xml.XMLParser;

/**
 * 对象转文本文件基础类
 * 
 * @author Huang.Yong
 * @version 0.1
 * @param &lt;T&gt; 目标对象类型
 */
public abstract class ObjectToFileAbstract<T> implements ObjectToText<T> {

    private XMLObject root;

    @Override
    public void setFilePath(String absoluteFilePath) {
        XMLParser parser = new XMLParser(absoluteFilePath);
        try {
            root = parser.parse();
        } catch (Exception e) {
            throw new AccessException("Parse file error", e, absoluteFilePath);
        }
    }

    @Override
    public File convert(Collection<T> list) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setXmlConfPath(String xmlConfFile) {
        // TODO Auto-generated method stub

    }

}
