package com.suixingpay.config.common.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.suixingpay.config.common.enums.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用平台接口返回的结果
 *
 * @author jiayu.qiu
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求状态
     */
    @ApiModelProperty(value = "请求状态.SUCCESS：表示成功；ERROR：表示错误；TIMEOUT：表示session过期", required = true)
    private StatusCode status = StatusCode.SUCCESS;

    /**
     * 业务数据：如果返回StatusCode.SUCCESS,才取业务数据
     */
    @ApiModelProperty(value = "返回的业务数据。")
    private T data;

    /**
     * 非法的参数名称
     */
    @ApiModelProperty(value = "错误信息，如果是参数错误，会以参数名称作为key，如果是其它错误，会以ERROR_MSG作为key。")
    private Map<String, String> error;

    /**
     * header头中的最大缓存时间 单位为秒
     */
    private Integer cacheMaxAge;

    public ResponseDTO() {

    }

    public ResponseDTO(T data) {
        this.data = data;
    }

    @JsonIgnore
    public Integer getCacheMaxAge() {
        return cacheMaxAge;
    }

    /**
     * 添加错误消息
     *
     * @param message 错误信息
     * @return ResponseTO
     */
    public ResponseDTO<T> addErrorMessage(String message) {
        return addErrorMessage("ERROR_MSG", message);

    }

    /**
     * 添加错误消息
     *
     * @param ex 错误信息
     * @return ResponseTO
     */
    public ResponseDTO<T> addErrorMessage(Throwable ex) {
        String message = ex.getMessage();
        if (null == message) {
            message = ex.getLocalizedMessage();
        }
        if (null == message) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            message = sw.toString();
        }
        return addErrorMessage("ERROR_MSG", message);
    }

    /**
     * 增加非法参数错误信息
     *
     * @param paramName 错误名称
     * @param message   错误信息
     * @return ResponseTO
     */
    public ResponseDTO<T> addErrorMessage(String paramName, String message) {
        setStatus(StatusCode.ERROR);
        if (null == paramName || null == message) {
            return this;
        }
        if (null == this.error) {
            this.error = new HashMap<String, String>(16);
        }
        if (this.error.containsKey(paramName)) {
            String tmp = this.error.get(paramName);
            this.error.put(paramName, tmp + ";" + message);
        } else {
            this.error.put(paramName, message);
        }
        return this;
    }

    /**
     * 错误信息，如果是参数错误，会以参数名称作为key，如果是其它错误，会以ERROR_MSG作为key。
     * 注意此方法不能删除，否则生成的json中没有error项
     *
     * @return 错误信息
     */
    public Map<String, String> getError() {
        return error;
    }

    @JsonIgnore
    public String getErrorMessage() {
        StringBuilder msg = new StringBuilder();
        if (hasError()) {
            for (String val : error.values()) {
                msg.append(val + ";");
            }
            if (msg.length() > 0) {
                msg.deleteCharAt(msg.length() - 1);
            }
        }
        return msg.toString();
    }

    /**
     * 是否成功
     *
     * @return 如果成功返回true，否则返回false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return null != status && status == StatusCode.SUCCESS;
    }

    /**
     * 是否有错误
     *
     * @return 如果有错误返回true，否则返回false
     */
    @JsonIgnore
    public boolean hasError() {
        return status == StatusCode.ERROR || (null != error && error.size() > 0);
    }

    /**
     * session 是否过期
     *
     * @return 如果过期返回true，否则返回false
     */
    @JsonIgnore
    public boolean isSessionTimeOut() {
        return null != status && status == StatusCode.TIMEOUT;
    }
}
