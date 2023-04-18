package com.trq.xtdemo.biz.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 13:48
 */
@Slf4j
public class NumConvertFactory {
    public static ConcurrentHashMap<String, NumConvert> map = new ConcurrentHashMap<>();

    /**
     * 获取实例
     * @param code
     * @return
     */
    public static NumConvert getInstance(String code) {
//        log.info("code:{}", code);
        if (map.isEmpty() || !map.containsKey(code)) {
            log.info("map is empty or code is error");
            return null;
        }
        return map.get(code);
    }

    /**
     * 注册转换类
     * @param code
     * @param numConvert
     */
    public static void register(String code, NumConvert numConvert) {
        log.info("registerNumConvert,code:{}", code);
        map.put(code, numConvert);
    }

}
