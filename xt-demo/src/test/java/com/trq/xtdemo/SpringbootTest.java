package com.trq.xtdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * springboot测试类，可以注入bean
 * @author trq
 * @version 1.0
 * @since 2022/5/14 22:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootTest {

    @Test
    public void test() {
        System.out.println("hello SpringbootTest");
    }
}
