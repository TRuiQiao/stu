package com.trq.xtdemo.biz.dto;

import com.trq.xtdemo.biz.entity.YxqPerson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 粤信签用户DTO
 * @author trq
 * @version 1.0
 * @since 2022/5/21 18:20
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class YxqPersonDTO extends YxqPerson {
}
