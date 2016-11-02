package org.yong.commons.impl.text.multi;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.yong.commons.component.TypeConfigure;
import org.yong.commons.iface.listeners.ComplexTextListener;
import org.yong.commons.iface.scanner.DataScanner;
import org.yong.commons.iface.text.MultiObjectToText;
import org.yong.commons.utils.file.FileUtil;

import test.entities.TestEntity;
import test.entities.TestEntity2;

public class MultiObjectToTextTest {

    @Test
    public void testBuild() throws Exception {
        String multiConfPath = "multi.xml";
        List<TypeConfigure<?>> typesConf = TypeConfigure.builder(multiConfPath).build();
        System.out.println(typesConf);
    }

    @Test
    public void testParse() {
        // 构建多类型配置
        String multiConfPath = "multi.xml";
        List<TypeConfigure<?>> typesConf = TypeConfigure.builder(multiConfPath).build();

        // 创建多类型文本扫描器
        DataScanner textScanner = new DataScanner() {
            @Override
            public Class<?> discernBeanType(String textData) {
                return (3 == textData.split("\\|").length) ? TestEntity2.class : TestEntity.class;
            }
        };

        // 创建多类型文本处理器,
        // 需要使用类型配置 和 文本扫描器
        MultiObjectToText multiObjectToText = new MultiObjectToTextImpl(typesConf, textScanner);
        multiObjectToText.registerListener(new ComplexTextListener() {
            @Override
            public void afterFileParsed(File src, Map<Class<?>, List<?>> typeBeans) {
                System.out.println(typeBeans);
            }
        });

        // 解析指定文件
        File src = FileUtil.getFileFromClassPath("test.txt");
        Map<Class<?>, List<?>> datasMap = multiObjectToText.parse(src);

        // 处理解析结果
        System.out.println(datasMap.size());
        for (Entry<Class<?>, List<?>> me : datasMap.entrySet()) {
            System.out.println(me.getKey() + " --- " + me.getValue().size());
        }
    }

}
