package org.yong.commons.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

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
        Class<?> clazz = FileUtil.class;
        URL url = clazz.getClassLoader().getResource("");

        try {
            URI uri = url.toURI();
            File classPathDir = new File(uri);
            File file = new File(classPathDir, fileName);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!file.exists())
                    parentFile.mkdirs();
            }
            return file;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建文件
     * 
     * @param exportFilePath 文件路径
     * @return 目标文件
     */
    public static File createQuietly(String exportFilePath) {
        return createQuietly(exportFilePath, true);
    }

    /**
     * 创建文件
     * 
     * @param exportFilePath 文件路径
     * @param append 文件存在时,true-追加, false-覆盖
     * @return 目标文件
     */
    public static File createQuietly(String exportFilePath, boolean append) {
        File file = new File(exportFilePath);
        File dir = file.getParentFile();
        if (!dir.exists())
            dir.mkdirs();
        try {
            FileUtils.write(file, "", append);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
