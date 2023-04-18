package com.trq.xtdemo.biz.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author trq
 * @version 1.0
 * @since 2022/7/21 12:08
 */
@FeignClient(url = "https://www.baidu.com", name = "baidu")
public interface UserFeignService {

    @GetMapping("/")
    String getBaiduHome();
}
