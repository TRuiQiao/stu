package com.trq.xtdemo.common.exception;

import com.trq.xtdemo.common.constants.CommonConstants;
import com.trq.xtdemo.common.enums.BaseResponseEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * 自定义业务异常类
 * @author trq
 * @version 1.0
 * @since 2022/5/15 11:14
 */
@Data
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 7025174125771950626L;

    private String code;
    private String msg;
    private String reqId = MDC.get(CommonConstants.REQ_ID);

    public BizException() {
        super();
    }

    public BizException(String msg) {
        super(msg);
        this.code = BaseResponseEnum.EXCEPTION.getCode();
        this.msg = msg;
    }

    public BizException(BaseResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
    }

    public BizException(String reqId, String msg) {
        super(msg);
        this.code = BaseResponseEnum.EXCEPTION.getCode();
        this.msg = msg;
        if (StringUtils.isBlank(reqId)) {
            reqId = MDC.get(CommonConstants.REQ_ID);
        }
        this.reqId = reqId;
    }

    public BizException(String reqId, BaseResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
        if (StringUtils.isBlank(reqId)) {
            reqId = MDC.get(CommonConstants.REQ_ID);
        }
        this.reqId = reqId;
    }
}
