package com.trq.xtdemo.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.trq.xtdemo.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 粤信签用户
 * @author trq
 * @version 1.0
 * @since 2022/5/21 18:20
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_yxq_person")
public class YxqPerson extends BaseEntity {

    @ApiModelProperty(value = "用户id")
    private Long personId;
    @ApiModelProperty(value = "openId")
    private String openId;
    @ApiModelProperty(value = "账号可信等级")
    private String level;
    @ApiModelProperty(value = "同意隐私协议标识,0:不同意,1:同意")
    private Integer agreePolicyFlag;
}
