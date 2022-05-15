package com.trq.xtdemo.common.util;

import com.trq.xtdemo.biz.dto.resp.PdfFileToImageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.MyDefaultResourceCache;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/8 17:30
 */
@Slf4j
public class PdfToImageUtil {

    public static final String PDF_FILE_SUFFIX = ".pdf";
    /**
     * DPI值越大，转换后越清晰，但相对转换速度也越慢
     */
    public static final Integer DPI = 80;
    /**
     * 转换后的图片格式
     */
    private static final String IMG_TYPE_PNG = "png";

    /**
     * pdf文件上传
     * @param file  需要上传的pdf文件
     * @return
     * @throws Exception
     */
    public static String uploadPdfFile(MultipartFile file) throws Exception {
        String uploadPath = PathUtil.createPath("01");
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(PDF_FILE_SUFFIX)) {
            throw new Exception("请上传pdf格式文件");
        }
        // 创建目录
        File parentFile = new File(uploadPath);
        log.info("uploadPath:{}, fileName:{}", uploadPath, fileName);
        File childFile = new File(parentFile, fileName);
        // 实现文件上传
        file.transferTo(childFile);

        return uploadPath + fileName;
    }

    /**
     * pdf文件拆分
     * @param pdfFilePath   pdf所在路径
     * @param partBasePath  pdf拆分存放基础目录
     * @throws Exception
     */
    public static void splitPdfFile(String pdfFilePath, String partBasePath) throws Exception {
        try (PDDocument source = PDDocument.load(new File(pdfFilePath))) {
            Splitter splitter = new Splitter();
            List<PDDocument> pdDocumentList = splitter.split(source);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            int i = 0;
            log.info("pdf总页数：{}", pdDocumentList.size());
            for (PDDocument document : pdDocumentList) {
                i++;
                document.save(StringUtils.join(partBasePath, df.format(new Date()), "_", i, PDF_FILE_SUFFIX));
            }
        }
    }

    /**
     * pdf文件转图片base64
     * @param filePath  pdf文件路径
     * @return
     * @throws Exception
     */
    public static List<PdfFileToImageResp> pdfFileToImageBase64Str(String filePath) throws Exception {
        String fileBase64Str = FileUtil.file2Base64(new File(filePath));
        return pdfBase64ToImageBase64(fileBase64Str);
    }

    /**
     * pdf文件转图片base64
     * @param file  pdf文件
     * @return
     * @throws Exception
     */
    public static List<PdfFileToImageResp> pdfFileToImageBase64Str(File file) throws Exception {
        String fileBase64Str = FileUtil.file2Base64(file);
        return pdfBase64ToImageBase64(fileBase64Str);
    }

    /**
     * pdf文件base64转图片base64
     * @param pdfBase64 pdf文件的base64字符串
     * @return
     * @throws Exception
     */
    public static List<PdfFileToImageResp> pdfBase64ToImageBase64(String pdfBase64) throws Exception {
        List<PdfFileToImageResp> imageBase64StrList = new ArrayList<>();
        if (StringUtils.isBlank(pdfBase64)) {
            log.info("pdfBase64 is blank...");
            return imageBase64StrList;
        }
        byte[] fileContent = Base64Util.base64Decode(pdfBase64);
        return pdfByteArrayToImageBase64(fileContent);
    }

    /**
     * pdf文件byte数组转图片
     * @param fileContent   文件二进制数组
     * @return
     * @throws IOException
     */
    public static List<PdfFileToImageResp> pdfByteArrayToImageBase64(byte[] fileContent) throws IOException {
        log.info("fileContent.size:{}", fileContent.length);
        List<PdfFileToImageResp> imageBase64StrList = new ArrayList<>();
        try (PDDocument document = PDDocument.load(fileContent);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // 设置ResourceCache为自定义的子类，避免内存溢出
            document.setResourceCache(new MyDefaultResourceCache());
            int fileSize = document.getNumberOfPages();
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < fileSize; i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DPI);
                // 设置时间格式
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                ImageIO.write(bufferedImage, IMG_TYPE_PNG, new File("d:\\temp\\pdfToImage\\photo\\pdf2Img_"+df.format(new Date()) + ".png"));
                ImageIO.write(bufferedImage, IMG_TYPE_PNG, out);

                PdfFileToImageResp imageResp = new PdfFileToImageResp();
                imageResp.setWidth(bufferedImage.getWidth());
                imageResp.setHeight(bufferedImage.getHeight());
                imageResp.setImageData(StringUtils.join("data:image/png;base64,", Base64Util.base64Encode(out.toByteArray())));

                imageBase64StrList.add(imageResp);
            }
        }
        return imageBase64StrList;
    }


}
