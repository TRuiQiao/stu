package com.trq.xtdemo.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/11 22:10
 */
public class Base64Util {

    public static String base64Encode(String s) {
        if (s == null) {
            return null;
        }
        return base64Encode(s.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64Encode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String base64DecodeToString(String s) {
        byte[] bytes = base64Decode(s);
        if (bytes == null) {
            return null;
        } else {
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static byte[] base64Decode(String s) {
        if (s == null) {
            return null;
        }
        return Base64.getDecoder().decode(s);
    }
}
