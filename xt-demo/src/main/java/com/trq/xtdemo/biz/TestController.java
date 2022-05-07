package com.trq.xtdemo.biz;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/check")
    public String check() {
        return "ok";
    }
}
