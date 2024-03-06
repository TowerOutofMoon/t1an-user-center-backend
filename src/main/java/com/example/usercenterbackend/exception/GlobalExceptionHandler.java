package com.example.usercenterbackend.exception;

import com.example.usercenterbackend.common.BaseErrorCode;
import com.example.usercenterbackend.common.BaseResponse;
import com.example.usercenterbackend.common.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 处理全局异常，返回处理过的信息，并打印日志
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResponseHelper.failedResponse(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runTimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException: ", e);
        return ResponseHelper.failedResponse(BaseErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
