package com.trq.xtdemo.common.dto.base;

import lombok.Data;

import java.util.List;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/8 15:59
 */
@Data
public class BasePageResp<T> extends BaseResp {

    /**
     * 分页-总条数
     */
    private long total;

    /**
     * 分页-结果集合
     */
    private List<T> list;
}
