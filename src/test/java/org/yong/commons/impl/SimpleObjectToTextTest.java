package org.yong.commons.impl;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.yong.commons.iface.text.ObjectToText;
import org.yong.commons.impl.adapters.TextListenerAdapter;
import org.yong.commons.impl.text.SimpleObjectToText;
import org.yong.commons.utils.MD5Helper;

import test.entities.TestEntity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SimpleObjectToTextTest {

    private static File getFileFromClassPath(String fileName) {
        try {
            Class<?> clazz = SimpleObjectToTextTest.class;
            URL url = clazz.getResource(fileName);
            URI uri = url.toURI();
            File file = new File(uri);
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSetXmlConfPath() {
        ObjectToText<Object> objectToText = new SimpleObjectToText<Object>("d:/test.txt", getFileFromClassPath("/conf.xml"));
        System.out.println(objectToText);
    }

    @Test
    public void testExport() throws Exception {
        // 获取打印对象
        String exportFilePath = "d:/test.txt";
        File fileFromClassPath = getFileFromClassPath("/conf.xml");
        ObjectToText<TestEntity> objectToText = new SimpleObjectToText<TestEntity>(exportFilePath, fileFromClassPath);
        // 注册监听器
        objectToText.registerListener(new TextListenerAdapter<TestEntity>() {
            @Override
            public String beforeColumnAppend(String name, String val, String content) {
                if ("mac".equals(name))
                    return MD5Helper.encode(content);
                return val;
            }
        });
        // 打印文件
        Collection<TestEntity> list = getDataList(1);
        File export = objectToText.export(list);
        System.out.println("total : " + export);

        // 获取打印对象
        String exportFilePath1 = "d:/test.txt";
        File fileFromClassPath1 = getFileFromClassPath("/append.xml");
        ObjectToText<TestEntity> objectToText1 = new SimpleObjectToText<TestEntity>(exportFilePath1, fileFromClassPath1);
        // 注册监听器
        objectToText1.registerListener(new TextListenerAdapter<TestEntity>() {
            @Override
            public String beforeColumnAppend(String name, String val, String content) {
                if ("mac".equals(name))
                    return MD5Helper.encode(content);
                return val;
            }
        });
        // 打印文件
        Collection<TestEntity> list1 = getDataList(5);
        File export1 = objectToText1.export(list1);

        System.out.println("details : " + export1.getAbsolutePath());
    }

    private Collection<TestEntity> getDataList(int size) {
        List<TestEntity> list = Lists.newArrayList();

        Random r = new Random();
        for (; size > 0; size--) {
            TestEntity order = new TestEntity("001", r.nextInt(100), r.nextInt(), new Date(), new Date(),
                    "FB-00" + r.nextInt(10), "xxxxxx");
            list.add(order);
        }
        return list;
    }

    @Test
    public void testParse() throws Exception {
        // 获取打印对象
        File fileFromClassPath = getFileFromClassPath("/conf.xml");
        ObjectToText<TestEntity> objectToText = new SimpleObjectToText<TestEntity>(fileFromClassPath);
        File src = new File("D:\\test.txt");
        @SuppressWarnings("deprecation")
        List<TestEntity> list = objectToText.parse(TestEntity.class, src);
        System.err.println(list);
    }

    @Test
    public void testName() throws Exception {
        Map<Class<?>, List<?>> map = Maps.newHashMap();
        map.put(Integer.class, Lists.newArrayList());
    }

}
