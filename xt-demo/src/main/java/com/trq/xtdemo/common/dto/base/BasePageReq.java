package com.trq.xtdemo.common.dto.base;

import lombok.Data;

/**
 * 分页请求实体基类
 * @author trq
 * @version 1.0
 * @since 2022/5/8 15:56
 */
@Data
public class BasePageReq extends BaseReq {

    /**
     * 分页参数-页码值
     */
    private Integer pageNum = 1;
    /**
     * 分页参数-每页容量
     */
    private Integer pageSize = 10;
}
