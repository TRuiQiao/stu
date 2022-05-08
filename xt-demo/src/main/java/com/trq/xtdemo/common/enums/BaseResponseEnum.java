package com.trq.xtdemo.common.enums;

import lombok.Getter;

/**
 * 基础响应枚举类
 * @author trq
 * @version 1.0
 * @since 2022/5/8 16:03
 */
@Getter
public enum BaseResponseEnum {

    SUCCESS("200", "成功"),
    FAILED("300", "失败"),
    EXCEPTION("900", "异常");

    private String code;
    private String msg;

    BaseResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
