package com.suixingpay.config.server.config;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.exception.SuixingPayExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:07:30
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:07:30
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        logger.error(
                "handleException Occured:: URL=" + request.getRequestURL() + ";ex.name=" + ex.getClass().getName());
        try {
            ResponseDTO<Object> res = SuixingPayExceptionHandler.exceptionToResponseTO(ex);
            SuixingPayExceptionHandler.writeResponseTO(res, response);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }
}
