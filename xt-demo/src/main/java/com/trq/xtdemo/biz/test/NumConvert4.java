package com.trq.xtdemo.biz.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 13:44
 */
@Component
public class NumConvert4 extends NumConvert implements InitializingBean {


    /**
     * 不转换
     * @param num  数字
     * @return
     */
    @Override
    String convert(int num) {
        return String.valueOf(num);
    }

    @Override
    String getCode() {
        return NumConvertCodeEnum.CODE_4.getCode();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NumConvertFactory.register(getCode(), this);
    }
}
