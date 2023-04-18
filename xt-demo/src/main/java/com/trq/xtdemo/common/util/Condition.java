package com.trq.xtdemo.common.util;

import java.util.ArrayList;

/**
 * @author trq
 * @version 1.0
 * @since 2022/7/25 22:47
 */
@FunctionalInterface
interface Condition<T1, T2> {

    boolean check(T1 t1, T2 t2);

    static void test1() {
        Condition<String, ?> con = (t1, t2) -> t1.equals(t2.toString());
//        con.check("t1", "t1");
//        con.check("t1", 1);
//        con.check("t1", new ArrayList<>());
        con.check("t1", null);
    }

    static void test2() {
        Condition<String, Object> con = (t1, t2) -> t1.equals(t2.toString());
        con.check("t1", "t1");
        con.check("t1", 1);
        con.check("t1", new ArrayList<>());
        con.check("t1", null);
    }
}
