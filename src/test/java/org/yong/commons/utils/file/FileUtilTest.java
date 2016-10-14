package org.yong.commons.utils.file;

import org.junit.Test;

public class FileUtilTest {

    @Test
    public void testGetClassPath() {
        System.err.println(FileUtil.getFileFromClassPath("/xx.xx"));
    }

}
