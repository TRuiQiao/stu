package com.trq.xtdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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


    @Test
    public void testThread() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<Integer>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            int finalI = i;

            futures.add((Future<Integer>) executorService.submit(() -> {
                try {
//                    Thread.sleep(1000L);
                    System.out.println(Thread.currentThread().getName() + "当前值：" + finalI);
                    System.out.println(this.incr(finalI));;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        Thread.sleep(1000L);
        try {
            System.out.println("输出结果。。。");
            for (Future f : futures) {
                System.out.println("计算结果："+f.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

    }

    public Integer incr(int i) {
        return i + 1;
    }

    @Test
    public void testex() {
        int array[] = new int[10];
        System.out.println(array[0]);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
    }


}
