package com.suixingpay.config.server.exception;

import com.suixingpay.config.common.to.ResponseDTO;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 下午2:55:59
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 下午2:55:59
 */
public class ConfigException extends Exception {

    private static final long serialVersionUID = 1L;

    private final ResponseDTO<Object> response = new ResponseDTO<Object>();

    public ConfigException() {
        super();
    }

    public ConfigException(Exception e) {
        super(e);
        addErrorMessage(e.getMessage());
    }

    public ConfigException(String message) {
        super(message);
        addErrorMessage(message);
    }

    public ConfigException(String paramName, String message) {
        super(message);
        addErrorMessage(paramName, message);
    }

    /**
     * 添加错误消息
     *
     * @param message 错误信息
     */
    public void addErrorMessage(String message) {
        response.addErrorMessage(message);
    }

    /**
     * 增加非法参数错误信息
     *
     * @param paramName 错误名称
     * @param message   错误信息
     */
    public void addErrorMessage(String paramName, String message) {
        response.addErrorMessage(paramName, message);
    }

    public ResponseDTO<Object> getResponse() {
        return response;
    }

    @Override
    public String getMessage() {
        if (response.hasError()) {
            return response.getErrorMessage();
        }
        return super.getMessage();
    }

    public boolean hasError() {
        return response.hasError();
    }
}
