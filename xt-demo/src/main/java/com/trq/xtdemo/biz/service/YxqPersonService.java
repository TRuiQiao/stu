package com.trq.xtdemo.biz.service;

import com.trq.xtdemo.biz.dto.YxqPersonDTO;

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
}
