# XML Configuration File

  * 通过XML配置设置文本顺序和长度 

  ```html
  <!-- 单结构配置 -->
  <conf showTitle="false" append="false" >
      <title  name          = "code" 
              text          = "第一列"
              prefix        = ""
              suffix        = "|"
              size          = "4"
              rightAlign    = "true"
              formatter     = "yyyyMMdd"
              size          = "8"
       />
  </conf>
     
  <!-- 多结构配置 -->
  <types>
       <conf showTitle="false" append="true" beanClass="test.entities.TestEntity">
         <title  name      = "code" 
                 text      = "第一列"
                 prefix    = ""
                 suffix    = "|"
                 size      = "4"
                 hasTitle  = "false"
          />
         <title  name      = "amount" 
                 text      = "第二列"
                 prefix    = ""
                 suffix    = "|"
                 size      = "10"
                 rightAlign= "false"
          />
       </conf >
       <conf showTitle="false" append="true" beanClass="test.entities.TestEntity2">
         <title  name      = "code" 
                 text      = "第三列"
                 prefix    = ""
                 suffix    = "|"
                 size      = "12"
                 hasTitle  = "false"
                 rightAlign= "false"
         />
      </conf >
  </types>
  ```

# 打印文本文件

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

# 从文本文件中解析(多结构)

  ```java
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
  
  // 解析指定文件
  File src = FileUtil.getFileFromClassPath("test.txt");
  Map<Class<?>, List<?>> datasMap = multiObjectToText.parse(src);
  
  // 处理解析结果
  System.out.println(datasMap.size());
  for (Entry<Class<?>, List<?>> me : datasMap.entrySet()) {
      System.out.println(me.getKey() + " --- " + me.getValue().size());
  }
  ```
