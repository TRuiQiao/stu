package com.trq.xtdemo.biz.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 13:44
 */
@Component
public class NumConvert1 extends NumConvert implements InitializingBean {


    /**
     * 碰到当前数字时，使用字母替换，例如，3-> A
     * @param num  数字
     * @return
     */
    @Override
    String convert(int num) {
        String result = "";
        switch (num) {
            case NumConvertConstants.THREE:
                result = NumConvertConstants.CODE_A;
                break;
            case NumConvertConstants.FIVE:
                result = NumConvertConstants.CODE_B;
                break;
            case NumConvertConstants.SEVEN:
                result = NumConvertConstants.CODE_C;
                break;
            default:
                result = String.valueOf(num);
                break;
        }

        return result;
    }

    @Override
    String getCode() {
        return NumConvertCodeEnum.CODE_1.getCode();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NumConvertFactory.register(getCode(), this);
    }
}
