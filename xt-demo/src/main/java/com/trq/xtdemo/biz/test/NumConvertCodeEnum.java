package com.trq.xtdemo.biz.test;

import lombok.Getter;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 13:57
 */
@Getter
public enum NumConvertCodeEnum {

    CODE_1("1", "满足值为当前数字"),
    CODE_2("2", "满足值为当前数字倍数，且不满足多个数字的倍数"),
    CODE_3("3", "满足值为多个数字的倍数时"),
    CODE_4("4", "以上规则都不满足，返回数字本身");

    private String code;
    private String name;

    NumConvertCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
