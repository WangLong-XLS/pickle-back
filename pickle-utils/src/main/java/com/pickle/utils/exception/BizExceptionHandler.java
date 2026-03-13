package com.pickle.utils.exception;

import com.pickle.utils.rest.RestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BizExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public RestBean<String> error(Exception e){
        logger.error("操作失败：" +e.getMessage());
        return RestBean.fail();
    }

    @ExceptionHandler(BizException.class)
    public RestBean<String> bizError(BizException e){
        logger.error("业务异常：" +e);
        return RestBean.fail(e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public RestBean<String> tokenError(TokenException e){
        logger.error("登录鉴权：" +e);
        return RestBean.fail(401, e.getMessage());
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestBean<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuffer stringBuffer = new StringBuffer();
        ex.getBindingResult().getFieldErrors().forEach(error -> stringBuffer.append(error.getDefaultMessage()).append(";"));
        return RestBean.fail(stringBuffer.toString());
    }
}