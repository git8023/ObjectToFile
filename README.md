# API
    * 通过XML配置设置文本顺序和长度    
    <conf ><!-- root label -->
        <title  prop-name   = "属性名" 
                text        = "列标题"
                suffix      = "列后缀"
                prefix      = "列前缀"
                max-size    = "列值最大长度, 本列总长度 = suffix.length() + prefix.length() + max-size.length()"
         />
     </conf >
# aaa