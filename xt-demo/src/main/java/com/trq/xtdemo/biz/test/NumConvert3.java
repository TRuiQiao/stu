package com.trq.xtdemo.biz.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import static com.trq.xtdemo.biz.test.NumConvertConstants.*;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 13:44
 */
@Component
public class NumConvert3 extends NumConvert implements InitializingBean {


    /**
     * 碰到多个数字的倍数时，使用多个对应的字母替代，例如：15 -> AB，21->AC
     * @param num  数字
     * @return
     */
    @Override
    String convert(int num) {
        String result = String.valueOf(num);

        if (num % (THREE * FIVE) == 0) {
            return NumConvertConstants.CODE_AB;
        }
        if (num % (THREE * SEVEN) == 0) {
            return NumConvertConstants.CODE_AC;
        }
        if (num % (FIVE * SEVEN) == 0) {
            return NumConvertConstants.CODE_BC;
        }

        return result;
    }

    @Override
    String getCode() {
        return NumConvertCodeEnum.CODE_3.getCode();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NumConvertFactory.register(getCode(), this);
    }
}
