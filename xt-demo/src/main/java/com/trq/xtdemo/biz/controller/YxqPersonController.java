package com.trq.xtdemo.biz.controller;

import com.trq.xtdemo.biz.dto.YxqPersonDTO;
import com.trq.xtdemo.biz.dto.req.YxqPersonListReq;
import com.trq.xtdemo.biz.dto.resp.YxqPersonListResp;
import com.trq.xtdemo.biz.service.YxqPersonService;
import com.trq.xtdemo.common.dto.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/21 22:41
 */
@RestController
@RequestMapping("/yxq")
@Api(tags = "粤信签用户控制器")
public class YxqPersonController {

    @Resource
    private YxqPersonService personService;

    @ApiOperation(value = "修改用户信息")
    @PostMapping("/update")
    public BaseResponse update(@RequestBody YxqPersonDTO dto) {
        personService.updateByPersonId(dto);
        return BaseResponse.success();
    }

    @ApiOperation(value = "查询用户列表")
    @PostMapping("/queryList")
    public BaseResponse<YxqPersonListResp> queryList(@RequestBody YxqPersonListReq req) {
        return BaseResponse.success(personService.queryYxqPersonList(req));
    }
}
