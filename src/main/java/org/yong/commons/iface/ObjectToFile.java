package org.yong.commons.iface;

import java.io.File;

import org.yong.commons.exception.AccessException;

/**
 * 对象转换为文件
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public interface ObjectToFile<T> {

    /**
     * 设置导出的文件路径
     * 
     * @param file 文件路径, 如果存在则覆盖
     */
    public void setExportFilePath(String filePath);

    /**
     * 执行转换
     * 
     * @param beans 对象列表
     * @return 转换后文件对象
     * @throws AccessException 导出文件失败或其他错误时
     */
    public File export(Iterable<T> beans) throws AccessException;

}
