package org.yong.commons.iface;

import java.io.File;
import java.util.Collection;

/**
 * 对象转换为文件
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public interface ObjectToFile<T> {

    /**
     * 设置文件路径
     * 
     * @param absoluteFilePath 文件路径, 如果存在则覆盖
     */
    public void setFilePath(String absoluteFilePath);

    /**
     * 执行转换
     * 
     * @param list 对象列表
     * @return 转换后文件对象
     */
    public File convert(Collection<T> list);
}
