package com.trq.xtdemo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/8 17:32
 */
@Slf4j
public class PathUtil {

    public static String createPath(String... paths) throws IOException {
        String basePath = "D:\\temp\\pdfToImage\\upload\\";
        StringBuffer buffer = new StringBuffer();
        buffer.append(basePath);

        for (String path : paths) {
            if (StringUtils.isNoneBlank(path)) {
                buffer.append(path + File.separator);
            }
        }

        String datePath = buffer.toString();
        log.debug("createPath datePath:{}", datePath);
        try {
            FileUtils.forceMkdir(FileUtils.getFile(datePath));
        } catch (IOException e) {
            throw new IOException("未知异常");
        }
        return datePath;
    }
}
