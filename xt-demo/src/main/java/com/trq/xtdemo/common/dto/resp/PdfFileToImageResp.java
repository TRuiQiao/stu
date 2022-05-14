package com.trq.xtdemo.common.dto.resp;

import com.trq.xtdemo.common.dto.base.BaseResp;
import lombok.Data;
import lombok.ToString;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/11 21:47
 */
@Data
@ToString
public class PdfFileToImageResp extends BaseResp {

    private Integer width;
    private Integer height;
    private String imageData;
}
