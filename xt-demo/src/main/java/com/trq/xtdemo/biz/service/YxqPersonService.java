package com.trq.xtdemo.biz.service;

import com.trq.xtdemo.biz.dto.YxqPersonDTO;
import com.trq.xtdemo.biz.dto.req.YxqPersonListReq;
import com.trq.xtdemo.biz.dto.resp.YxqPersonListResp;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/21 18:32
 */
public interface YxqPersonService {

    /**
     * 根据用户id修改用户信息
     * @param dto
     * @return
     */
    int updateByPersonId(YxqPersonDTO dto);

    /**
     * 用户列表
     * @return
     */
    YxqPersonListResp queryYxqPersonList(YxqPersonListReq req);
}
