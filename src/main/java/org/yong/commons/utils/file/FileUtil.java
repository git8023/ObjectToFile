package org.yong.commons.utils.file;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.yong.commons.impl.ObjectToFileAbstractTest;

/**
 * 文件工具
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public abstract class FileUtil {

    /** 当前系统换行符 */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * 获取文件(从ClassPath开始查找)
     * 
     * @param fileName 文件名
     * @return 指定文件
     */
    public static File getFileFromClassPath(String fileName) {
        Class<?> clazz = ObjectToFileAbstractTest.class;
        URL url = clazz.getResource(fileName);
        if (null == url) {
            return null;
        }

        try {
            URI uri = url.toURI();
            File file = new File(uri);
            return file;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
