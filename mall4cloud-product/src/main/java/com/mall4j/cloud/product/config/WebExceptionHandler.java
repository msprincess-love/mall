package com.mall4j.cloud.product.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/24 17:58
 * @description mall
 */
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public String runtimeExceptionHandler(Exception exception) {
        System.out.println("全部异常捕获" + Arrays.toString(exception.getStackTrace()));
        return exception.getMessage();
    }
}
