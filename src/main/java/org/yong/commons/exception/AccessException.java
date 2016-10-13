package org.yong.commons.exception;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 执行异常
 * 
 * @author Huang.Yong
 * @version 0.1
 */
public class AccessException extends RuntimeException {
    private static final long serialVersionUID = -7746931882438226261L;

    private List<Object> params = Lists.newArrayList();

    public AccessException(String message, Throwable cause, Object... params) {
        super();
        boolean hasParams = (null != params);
        hasParams = hasParams && (0 < params.length);
        if (hasParams) {
            this.params.addAll(Arrays.asList(params));
        }
    }

    public AccessException() {
        super();
        // Auto-generated constructor stub
    }

    public AccessException(String message, Throwable cause) {
        super(message, cause);
        // Auto-generated constructor stub
    }

    public AccessException(String message) {
        super(message);
        // Auto-generated constructor stub
    }

    public AccessException(Throwable cause) {
        super(cause);
        // Auto-generated constructor stub
    }

    /**
     * 获取参数列表
     * 
     * @return params 参数列表
     */
    public List<Object> getParams() {
        return Lists.newArrayList(this.params);
    }

}
