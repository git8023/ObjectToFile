package org.yong.commons.impl;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.yong.commons.iface.text.ObjectToText;

public class ObjectToFileAbstractTest {

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
        ObjectToText<Object> objectToFile = new ObjectToTextAbstract<Object>() {

            @Override
            public String convert(Object bean) {
                // TODO Auto-generated method stub
                return null;
            }
        };

        File confFile = getFileFromClassPath("/conf.xml");
        objectToFile.setXmlConfPath(confFile);
    }

}
