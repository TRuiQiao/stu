package com.trq.xtdemo.biz.controller;

import com.alibaba.fastjson.JSONObject;
import com.trq.xtdemo.common.dto.base.BaseResponse;
import com.trq.xtdemo.biz.dto.req.GetImageReq;
import com.trq.xtdemo.biz.dto.resp.PdfFileToImageResp;
import com.trq.xtdemo.common.util.PdfToImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/8 15:23
 */
@Slf4j
@RestController
@RequestMapping("/")
@Api(tags = "pdf转图片控制器")
public class PdfToImageController {

    @ApiOperation(value = "1-pdf文件拆分", notes = "1-pdf文件拆分")
    @PostMapping("/splitPdfFile")
    public BaseResponse splitPdfFile(MultipartFile file) throws Exception {
        // 拆分目录
        String partBasePath = "d:\\temp\\pdfToImage\\part\\";
        // 1.上传文件
        String filePath = PdfToImageUtil.uploadPdfFile(file);
        // 2.按页拆分pdf文件
        PdfToImageUtil.splitPdfFile(filePath, partBasePath);
        // 3.获取拆分的pdf文件
        File partFilePath = new File(partBasePath);
        File[] files = partFilePath.listFiles();
        log.info("files.length:{}", files.length);

        JSONObject res = new JSONObject();
        res.put("total", files.length);
        res.put("filePath", partFilePath);

        return BaseResponse.success("文件拆分成功", res);
    }

    @ApiOperation(value = "2-获取pdf转图片数据", notes = "2-获取pdf转图片数据")
    @PostMapping("/getImage")
    public BaseResponse getImage(@RequestBody GetImageReq getImageReq) throws Exception {
        String filePath = getImageReq.getFilePath();
        // 获取拆分的pdf文件
        File partFilePath = new File(filePath);
        File[] files = partFilePath.listFiles();

        // 1.总文件数
        int fileTotals = files.length;
        log.info("文件总数fileTotals：{}", fileTotals);

        if (fileTotals > 0) {
            List<PdfFileToImageResp> fileList = new ArrayList<>();
            List<PdfFileToImageResp> list = null;

            // 默认转所有
            int pageNum = 1;
            int pageSize = 10;

            // 2.总文件页数
            int fileTotalPages = (fileTotals / pageSize) + 1;
            log.info("文件总页数fileTotalPages:{}", fileTotalPages);

            // 3.从第几页开始转
            if (getImageReq.getPageNum().intValue() > pageNum) {
                pageNum = getImageReq.getPageNum().intValue();
                if (pageNum > fileTotalPages) {
                    pageNum = fileTotalPages;
                }
            }
            log.info("从第几页开始转pageNum:{}", pageNum);
            int start = (pageNum - 1) * pageSize;
            log.info("从第{}张开始转", start);
            int end = pageNum < fileTotalPages
                    ? start + pageSize
                    : start + (fileTotals % pageSize);
            log.info("start:{}, pageSize:{}, fileTotals % pageSize:{}", start, pageSize, fileTotals % pageSize);
            log.info("转到第{}张结束", end);

            for (int i = start; i < end; i++) {
                File f = files[i];
                if (f.exists()) {
                    log.info("转换pdf文件名:{}", f.getName());
                    list = PdfToImageUtil.pdfFileToImageBase64Str(f);
                    fileList.addAll(list);
                    // 转换成功后删除拆分的pdf文件
//                    f.delete();
                }
            }
            JSONObject res = new JSONObject();
            res.put("imageBase64List", fileList);
            return BaseResponse.success("获取图片成功", res);
        } else {
            return BaseResponse.failed("当前目录文件为空");
        }
    }
}
