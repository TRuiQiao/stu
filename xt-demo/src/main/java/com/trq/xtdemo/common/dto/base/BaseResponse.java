package com.trq.xtdemo.common.dto.base;

import com.alibaba.fastjson.JSONObject;
import com.trq.xtdemo.common.enums.BaseResponseEnum;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/8 16:06
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 7625605399850094401L;

    /**
     * 请求id
     */
    private String reqId = MDC.get("reqId");
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应实体
     */
    private T data = (T) JSONObject.parseObject("{}");

    public static BaseResponse success() {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.SUCCESS.getCode());
        response.setMsg(BaseResponseEnum.SUCCESS.getMsg());

        return response;
    }

    public static BaseResponse success(String msg) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.SUCCESS.getCode());
        response.setMsg(msg);

        return response;
    }

    public static BaseResponse success(Object data) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.SUCCESS.getCode());
        response.setMsg(BaseResponseEnum.SUCCESS.getMsg());
        if (Objects.nonNull(data)) {
            response.setData(data);
        }

        return response;
    }

    public static BaseResponse success(String msg, Object data) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.SUCCESS.getCode());
        response.setMsg(msg);
        if (Objects.nonNull(data)) {
            response.setData(data);
        }

        return response;
    }


    // ===========================
    public static BaseResponse failed() {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.FAILED.getCode());
        response.setMsg(BaseResponseEnum.FAILED.getMsg());

        return response;
    }

    public static BaseResponse failed(String msg) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.FAILED.getCode());
        response.setMsg(msg);

        return response;
    }

    public static BaseResponse failed(String code, String msg) {
        BaseResponse response = new BaseResponse();
        response.setCode(code);
        response.setMsg(msg);

        return response;
    }

    // ===================
    public static BaseResponse exception() {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.EXCEPTION.getCode());
        response.setMsg(BaseResponseEnum.EXCEPTION.getMsg());

        return response;
    }

    /**
     *
     * @param msg
     * @return
     */
    public static BaseResponse exception(String msg) {
        BaseResponse response = new BaseResponse();
        response.setCode(BaseResponseEnum.EXCEPTION.getCode());
        response.setMsg(msg);

        return response;
    }

}
