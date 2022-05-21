package com.trq.xtdemo.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trq.xtdemo.biz.dto.YxqPersonDTO;
import com.trq.xtdemo.biz.entity.YxqPerson;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/21 18:31
 */
@Mapper
public interface YxqPersonMapper extends BaseMapper<YxqPerson> {

    /**
     * 根据用户id修改用户信息
     * @param dto
     * @return
     */
    int updateByPersonId(YxqPersonDTO dto);
}
