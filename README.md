# XML Configuration File
    * 通过XML配置设置文本顺序和长度    
    <!-- root label -->
    <conf showTitle="false" append="false" >
      <title  name          = "code" 
              text          = "行业代码"
              prefix        = ""
              suffix        = "|"
              size          = "4"
              rightAlign    = "true"
              formatter     = "yyyyMMdd"
              size          = "8"
       />
     </conf >
# Java Code
    ```java
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
    Collection<TestEntity> list = getDataList();
    File export = objectToText.export(list);
    System.out.println(export.getAbsolutePath());
    ```