package com.suixingpay.config.server.exception;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Set;

/**
 * 异常处理器
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 上午11:33:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 上午11:33:48
 */
@Component
public class SuixingPayExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(SuixingPayExceptionHandler.class);

    private static final String JSON_TYPE = "application/json; charset=UTF-8";

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
                                         Exception ex) {
        try {
            writeResponseTO(exceptionToResponseTO(ex), response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (ex instanceof ForbiddenException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        return null;
    }

    /**
     * 把异常转为 ResponseTO
     *
     * @param ex
     * @return
     */
    public static ResponseDTO<Object> exceptionToResponseTO(Throwable throwable) {
        ResponseDTO<Object> responseTO;
        if (throwable instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException e1 = (UndeclaredThrowableException) throwable;
            throwable = e1.getUndeclaredThrowable();
        }
        if (throwable instanceof ConfigException) {
            ConfigException tmpEx = (ConfigException) throwable;
            responseTO = tmpEx.getResponse();
        } else if (throwable instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) throwable;
            Set<ConstraintViolation<?>> constraintViolationSet = cve.getConstraintViolations();
            responseTO = new ResponseDTO<Object>();
            String field;
            for (ConstraintViolation<?> constraintViolation : constraintViolationSet) {
                field = StringUtils.substringAfterLast(constraintViolation.getPropertyPath().toString(), ".");
                responseTO.addErrorMessage(field, constraintViolation.getMessage());
            }
        } else if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException mException = (MethodArgumentNotValidException) throwable;
            BindingResult bindingResult = mException.getBindingResult();
            List<ObjectError> errors = bindingResult.getAllErrors();
            responseTO = new ResponseDTO<Object>();
            for (ObjectError error : errors) {
                if (error instanceof FieldError) {
                    FieldError fError = (FieldError) error;
                    responseTO.addErrorMessage(fError.getObjectName(), fError.getDefaultMessage());
                } else {
                    responseTO.addErrorMessage(error.getObjectName(), error.getDefaultMessage());
                }
            }
        } else if (throwable instanceof BindException) {
            responseTO = new ResponseDTO<Object>();
            BindException bEx = (BindException) throwable;
            List<FieldError> errors = bEx.getFieldErrors();
            for (FieldError error : errors) {
                responseTO.addErrorMessage(error.getField(), error.getDefaultMessage());
            }
        } else {
            logger.error(throwable.getMessage(), throwable);
            responseTO = new ResponseDTO<Object>();
            responseTO.addErrorMessage(throwable);
        }
        return responseTO;
    }

    /**
     * @param obj
     * @param response
     * @throws Exception
     */
    public static void writeResponseTO(Object obj, HttpServletResponse response) throws Exception {
        if (response.isCommitted()) {
            return;
        }
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        String jsonStr = JsonUtil.objectToJson(obj);
        response.setContentType(JSON_TYPE);
        writeData(response, jsonStr);
    }

    /**
     * 输出数据
     *
     * @param response
     * @param message  字符串
     */
    public static void writeData(HttpServletResponse response, String message) {
        // response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(message);
            out.flush();
        } catch (IOException e) {
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
        }
    }
}
