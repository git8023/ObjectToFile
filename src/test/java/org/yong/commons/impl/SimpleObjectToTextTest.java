package org.yong.commons.impl;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.yong.commons.iface.text.ObjectToText;
import org.yong.commons.impl.adapters.TextListenerAdapter;
import org.yong.commons.utils.MD5Helper;

import test.entities.TestEntity;

import com.google.common.collect.Lists;

public class SimpleObjectToTextTest {

    private static File getFileFromClassPath(String fileName) {
        try {
            Class<?> clazz = ObjectToFileAbstractTest.class;
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
        ObjectToText<TestEntity> objectToText = new SimpleObjectToText<TestEntity>("d:/test.txt",
                getFileFromClassPath("/conf.xml"));
        Collection<TestEntity> list = createOrders();
        objectToText.registerListener(new TextListenerAdapter<TestEntity>() {
            @Override
            public String beforeColumnAppend(String name, String val, String content) {
                if ("mac".equals(name))
                    return MD5Helper.encode(content);
                return val;
            }
        });
        File export = objectToText.export(list);
        System.out.println(export.getAbsolutePath());
    }

    private Collection<TestEntity> createOrders() {
        List<TestEntity> list = Lists.newArrayList();

        Random r = new Random();
        for (int i = 0, len = 10; i < len; i++) {
            TestEntity order = new TestEntity("001", r.nextInt(100), r.nextInt(), new Date(), new Date(),
                    "FB-00" + r.nextInt(10), "xxxxxx");
            list.add(order);
        }
        return list;
    }
}
