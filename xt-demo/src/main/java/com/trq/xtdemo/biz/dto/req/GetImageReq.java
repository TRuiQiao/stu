package com.trq.xtdemo.biz.dto.req;

import com.trq.xtdemo.common.dto.base.BasePageReq;
import lombok.Data;
import lombok.ToString;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/11 22:32
 */
@Data
@ToString
public class GetImageReq extends BasePageReq {

    /**
     * 文件目录
     */
    private String filePath;
}
