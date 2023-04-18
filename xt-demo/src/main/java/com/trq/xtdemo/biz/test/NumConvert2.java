package com.trq.xtdemo.biz.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 13:44
 */
@Component
public class NumConvert2 extends NumConvert implements InitializingBean {


    /**
     * 碰到当前数字的倍数时，使用字母替换， 例如：6->A
     * @param num  数字
     * @return
     */
    @Override
    String convert(int num) {
        String result = String.valueOf(num);
        if (num % NumConvertConstants.THREE == 0) {
            return NumConvertConstants.CODE_A;
        }
        if (num % NumConvertConstants.FIVE == 0) {
            return NumConvertConstants.CODE_B;
        }
        if (num % NumConvertConstants.SEVEN == 0) {
            return NumConvertConstants.CODE_C;
        }

        return result;
    }

    @Override
    String getCode() {
        return NumConvertCodeEnum.CODE_2.getCode();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NumConvertFactory.register(getCode(), this);
    }
}
