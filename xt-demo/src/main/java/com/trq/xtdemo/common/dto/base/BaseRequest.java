package com.trq.xtdemo.common.dto.base;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 统一请求基类
 * @author trq
 * @version 1.0
 * @since 2022/5/8 15:46
 */
@Data
public class BaseRequest<T> implements Serializable {
    private static final long serialVersionUID = -1169862275920620809L;

    // 唯一请求id
    @NotBlank(message = "reqId不能为空")
    private String reqId;

    // 请求实体
    @Valid
    private T data;
}
