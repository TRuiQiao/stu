package com.trq.xtdemo.biz.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author trq
 * @version 1.0
 * @since 2023/4/7 14:39
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试类")
@Slf4j
public class NumController {

    @ApiOperation(value = "数字转换")
    @PostMapping("/numConvert")
    public String numConvert(@RequestBody Integer num) {

        for (int i = 0; i <= 100; i++) {
            String convert = ConvertUtils.convert(i);
//            log.info("num:{} -> convert:{}", i, convert);
            System.out.println("num:" + i + " -> " + convert);
        }
        return "ok";
    }
}
