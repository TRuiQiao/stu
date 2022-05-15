package com.trq.xtdemo.common.exception;

import com.trq.xtdemo.common.dto.base.BaseResponse;
import com.trq.xtdemo.common.enums.BaseResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 * @author trq
 * @version 1.0
 * @since 2022/5/15 12:22
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizException.class)
    public BaseResponse handleBizException(BizException e) {
        log.error("BizException", e);
        return BaseResponse.failed(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse handleException(Exception e) {
        log.error("UnKnowException", e);
        return BaseResponse.failed(BaseResponseEnum.UN_KNOW_EXCEPTION.getCode(), BaseResponseEnum.UN_KNOW_EXCEPTION.getMsg());
    }
}
