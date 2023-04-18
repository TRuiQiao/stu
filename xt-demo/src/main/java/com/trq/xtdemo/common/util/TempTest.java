package com.trq.xtdemo.common.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author trq
 * @version 1.0
 * @since 2022/7/25 20:10
 */
@Slf4j
public class TempTest {

    static class Data {
        @Getter
        private static int count = 0;
        @Getter
        private static AtomicInteger count1 = new AtomicInteger(0);

        private static ThreadLocal<Integer> count2 = new ThreadLocal<>();

        public void wrong() {
            count ++;
        }
    }

    public static void main(String[] args) {

//        test02();
//        test03();
//        test04();
//        test05();
//        test06();
//        log.info("1/2:{}", 1/2);
//        log.info("2/2:{}", 2/2);
//        log.info("3/2:{}", 3/2);
//        log.info("4/2:{}", 4/2);
//        log.info("5/2:{}", 5/2);

//        log.info("reverse:{}", reverse(1011));
        log.info("reverse1:{}", reverse(1));
        log.info("==================================");
        log.info("reverse1:{}", reverse(-9));
        log.info("==================================");
        log.info("reverse1:{}", reverse(0));
        log.info("==================================");
        log.info("reverse1:{}", reverse(1012210));
        log.info("==================================");
        log.info("reverse1:{}", reverse(110909));
    }

    /**
     * num为正整数，逆序输出
     * @param num 正整数
     * @return  逆序输出
     */
    public static Integer reverse(Integer num) {
        Integer res = null;
        if (num.intValue() < 0) {
            return res;
        }
        int len = String.valueOf(num).length();
        log.info("len:{}", len);
        if (len == 1) {
            return num;
        }

        char[] orgArr = num.toString().toCharArray();
        StringBuffer buffer = new StringBuffer("");
        for (int i = orgArr.length - 1; i >= 0; i--) {
            buffer.append(orgArr[i]);
        }
        res = Integer.valueOf(buffer.toString());
        log.info("buffer:{}", buffer.toString());
        log.info("res:{}", res);

        return res;
    }

    public static void test02() {
        IntStream.rangeClosed(1, 1000).parallel().forEach(i ->
                new Data().wrong());
        System.out.println("count2:" + Data.getCount());
    }

    // synchronized方式保证线程安全
    public static void test03() {
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            synchronized (Data.class) {
                new Data().wrong();
            }
        });
        System.out.println("count3:" + Data.getCount());
    }

    // lock锁方式保证线程安全
    public static void test04() {
        ReentrantLock lock = new ReentrantLock();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            lock.lock();
            try {
                new Data().wrong();
            } finally {
                lock.unlock();
            }
        });
        System.out.println("count4:" + Data.getCount());
    }

    // AtomicInteger方式保证线程安全
    public static void test05() {
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
                Data.count1.incrementAndGet();
        });
        System.out.println("count5:" + Data.count1);
    }

    public static void test06() {

        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            i = Data.count2.get();
            Data.count2.set(++ i);
        });
        System.out.println("count6:" + Data.count2.get());
    }

    public static void test01() {
        //        int a = 0;
//        for (int i = 0; i < 99; i++) {
//            a = a ++;
//        }
//        System.out.println(a);
//
//        int b = 0;
//        for (int i = 0; i < 99; i++) {
//            b = ++ b;
//        }
//        System.out.println(b);
        Integer a = 0;
        int b = 0;
        for (int i = 0; i < 9; i++) {
            a = a ++;
            System.out.println("a-i"+i+"="+a);

            b = a ++;
            System.out.println("b-i"+i+"="+b);
        }
        System.out.println(a);
        System.out.println(b);
    }
}
