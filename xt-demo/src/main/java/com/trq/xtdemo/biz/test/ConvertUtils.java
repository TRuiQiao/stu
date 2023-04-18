package com.trq.xtdemo.biz.test;

import lombok.extern.slf4j.Slf4j;

import static com.trq.xtdemo.biz.test.NumConvertConstants.*;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 13:31
 */
@Slf4j
public class ConvertUtils {


    /**
     * 转换
     * @param num
     * @return
     */
    public static String convert(int num) {
//        log.info("num:{}", num);
        if (num <= 0 || num > 100) {
            return String.valueOf(num);
        }
        String code = getCode(num);
        NumConvert instance = NumConvertFactory.getInstance(code);
        String convert = instance.convert(num);
//        log.info("num:{} -> {}", num, convert);
        return convert;
    }

    /**
     * 获取子类编码
     * @param num
     * @return
     */
    public static String getCode(int num) {
        // 等于当前数值3，5，7
        if (num == THREE || num == FIVE || num == SEVEN) {
            return NumConvertCodeEnum.CODE_1.getCode();
        }

        // 为当前值3，5，7的倍数，且不为3，5，7任意两两组合的倍数
        if ((num % THREE == 0 && num % (THREE * FIVE) != 0 && num % (THREE * SEVEN) != 0)
                || (num % FIVE == 0 && num % (THREE * FIVE) != 0 && num % (FIVE * SEVEN) != 0)
                || (num % SEVEN == 0 && num % (SEVEN * THREE) != 0 && num % (SEVEN * FIVE) != 0)) {
            return NumConvertCodeEnum.CODE_2.getCode();
        }

        // 为3，5，7任意两两组合的倍数
        if (num % (THREE * FIVE) == 0 || num % (THREE * SEVEN) == 0 || num % (FIVE * SEVEN) == 0) {
            return NumConvertCodeEnum.CODE_3.getCode();
        }
        // 不符合以上规则，原值返回
        return NumConvertCodeEnum.CODE_4.getCode();
    }


}
