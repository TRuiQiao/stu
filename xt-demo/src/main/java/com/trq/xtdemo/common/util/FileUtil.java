package com.trq.xtdemo.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/11 21:52
 */
public class FileUtil {

    /**
     * 将文件转化为base64字符串
     * @param inFile    输入文件
     * @return
     */
    public static String file2Base64(File inFile) {
        byte[] bytes = copyFile2Byte(inFile);
        if (bytes == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 将文件转化为字节码
     * @param inFile
     * @return
     */
    public static byte[] copyFile2Byte(File inFile) {
        InputStream in = null;
        try {
            in = new FileInputStream(inFile);
            // 文件长度
            int len = in.available();

            byte[] bytes = new byte[len];
            in.read(bytes);

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
