package com.trq.xtdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 普通测试类，无法注入bean
 * @author trq
 * @version 1.0
 * @since 2022/5/14 22:50
 */
@Slf4j
public class NormalTest {

    @Test
    public void test() {
//        System.out.println(isPalindrome(-121));
        System.out.println(romanToInt1("MCMXCIV"));
    }


    // 判断是否是回文数字
    public boolean isPalindrome(int x) {
        if (x == 0) {
            return true;
        } else if (x < 0) {
            return false;
        } else {
            String str = String.valueOf(x);
            int len = str.length();
            if (len % 2 == 0) {
                // 长度为偶数
                String partStr = str.substring(0, (len/2));
                String partStr1 = str.substring((len/2), len);
                if (new StringBuffer(partStr).reverse().toString().equals(partStr1)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // 长度为奇数
                String partStr = str.substring(0, (len/2));
                String partStr1 = str.substring((len/2) + 1, len);
                if (new StringBuffer(partStr).reverse().toString().equals(partStr1)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * 字符          数值
     * I             1
     * V             5
     * X             10
     * L             50
     * C             100
     * D             500
     * M             1000
     * 例如， 罗马数字 2 写做II，即为两个并列的 1 。12 写做XII，即为X+II。 27 写做XXVII, 即为XX+V+II。
     *
     * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做IIII，而是IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为IX。这个特殊的规则只适用于以下六种情况：
     *
     * I可以放在V(5) 和X(10) 的左边，来表示 4 和 9。
     * X可以放在L(50) 和C(100) 的左边，来表示 40 和90。
     * C可以放在D(500) 和M(1000) 的左边，来表示400 和900
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/roman-to-integer
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * 给定一个罗马数字，将其转换成整数
     * @param s
     * @return
     */
    public int romanToInt(String s) {
        Map<String, Integer> oneMap = new HashMap<String, Integer>();
        oneMap.put("I", 1);
        oneMap.put("V", 5);
        oneMap.put("X", 10);
        oneMap.put("L", 50);
        oneMap.put("C", 100);
        oneMap.put("D", 500);
        oneMap.put("M", 1000);
        oneMap.put("a", 4);
        oneMap.put("b", 9);
        oneMap.put("c", 40);
        oneMap.put("d", 90);
        oneMap.put("e", 400);
        oneMap.put("f", 900);

        s = s.replace("IV", "a");
        s = s.replace("IX", "b");
        s = s.replace("XL", "c");
        s = s.replace("XC", "d");
        s = s.replace("CD", "e");
        s = s.replace("CM", "f");

        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            num = num + oneMap.get(String.valueOf(s.charAt(i)));
        }
        return num;
    }

    public int romanToInt1(String s) {
        s = s.replace("IV","a");
        s = s.replace("IX","b");
        s = s.replace("XL","c");
        s = s.replace("XC","d");
        s = s.replace("CD","e");
        s = s.replace("CM","f");

        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            res += getValue(s.charAt(i));
        }
        return res;
    }

    public int getValue(char c) {
        switch(c) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            case 'a': return 4;
            case 'b': return 9;
            case 'c': return 40;
            case 'd': return 90;
            case 'e': return 400;
            case 'f': return 900;
        }
        return 0;
    }
}
