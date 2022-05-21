package com.trq.xtdemo.biz.service.impl;

import com.trq.xtdemo.biz.dto.YxqPersonDTO;
import com.trq.xtdemo.biz.mapper.YxqPersonMapper;
import com.trq.xtdemo.biz.service.YxqPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/21 18:32
 */
@Service
@Slf4j
public class YxqPersonServiceImpl implements YxqPersonService {

    @Resource
    private YxqPersonMapper yxqPersonMapper;

    @Override
    public int updateByPersonId(YxqPersonDTO dto) {
        return yxqPersonMapper.updateByPersonId(dto);
    }
}
