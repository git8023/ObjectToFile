/*
 * Github - https://github.com/git8023/ObjectToFile/
 */
package org.yong.commons.iface.text;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.yong.commons.exception.AccessException;
import org.yong.commons.iface.listeners.ComplexTextListener;

/**
 * 多对象文本处理, 所有接口都会抛出{@link AccessException} <br>
 * 
 * <pre>
 * <b>XML配置</b>
 *  &lt;types>
 *    &lt;conf showTitle="false" append="true" beanClass="test.entities.TestEntity">
 *      &lt;title   
 *              name        = "attrName"    # 对象属性名
 *              text        = "txt"         # 平文件抬头
 *              prefix      = "txtPrefix"   # 数据前缀, 排除
 *              suffix      = "|"           # 数据后缀, 排除
 *              placeholder = "0"           # 指定补位字符, 包含
 *              size        = "10"          # 数据长度, 完整长度=prefix+size+suffix
 *              rightAlign  = "true"        # 数据对齐方式
 *              formatter   = "yyyyMMdd"    # 日期文本格式
 *       />
 *    &lt;/conf >
 *    &lt;conf showTitle="false" append="true" beanClass="test.entities.TestEntity2">
 *    ...
 *    &lt;/conf >
 *  &lt;/types>
 *  
 *  <b>Java代码实现</b>
 *  public void testParse() {
 *      // 构建多类型配置
 *      String multiConfPath = "multi.xml";
 *      List&lt;TypeConfigure&lt;?>> typesConf = TypeConfigure.builder(multiConfPath).build();
 * 
 *      // 创建多类型文本扫描器
 *      DataScanner textScanner = new DataScanner() {
 *          public Class&lt;?> discernBeanType(String textData) {
 *              return (3 == textData.split("\\|").length) ? TestEntity2.class : TestEntity.class;
 *          }
 *      };
 * 
 *      // 创建多类型文本处理器, 
 *      // 需要使用类型配置 和 文本扫描器
 *      MultiObjectToText multiObjectToText = new MultiObjectToTextImpl(typesConf, textScanner);
 * 
 *      // 解析指定文件
 *      File src = FileUtil.getFileFromClassPath("test.txt");
 *      Map&lt;Class&lt;?>, List&lt;?>> datasMap = multiObjectToText.parse(src);
 * 
 *      // 处理解析结果
 *      System.out.println(datasMap.size());
 *      for (Entry&lt;Class&lt;?>, List&lt;?>> me : datasMap.entrySet()) {
 *          System.out.println(me.getKey() + " --- " + me.getValue().size());
 *      }
 *  }
 * </pre>
 * 
 * @author Huang.Yong
 * @version 0.1 提供多类型文件解析服务
 * @version 0.12 新增监听器接口 {@code ComplexTextListener}
 * @see AccessException
 * @see ComplexTextListener
 */
public interface MultiObjectToText {

    /**
     * 解析源文件, 不规则文件
     * 
     * @param src 源文件
     * @return 对象映射集合
     * @throws AccessException
     */
    public Map<Class<?>, List<?>> parse(File src) throws AccessException;

    /**
     * 注册监听器
     * 
     * @param complexTextListener 监听器
     * @return 已注册监听器, 如果没有总是返回null
     */
    ComplexTextListener registerListener(ComplexTextListener complexTextListener);
}
