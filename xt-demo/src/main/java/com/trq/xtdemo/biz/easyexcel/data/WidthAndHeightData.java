package com.trq.xtdemo.biz.easyexcel.data;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * @author trq
 * @version 1.0
 * @since 2023/3/23 23:00
 * 自定义列宽，行高的数据
 */
@Data
// 表头行高
@HeadRowHeight(20)
// 内容行高
@ContentRowHeight(10)
// 字段宽度
@ColumnWidth(15)
public class WidthAndHeightData {

    /**
     * 我想所有的 字符串起前面加上"自定义："三个字
     */
    @ExcelProperty(value = "字符串标题")
    private String string;
    /**
     * 我想写到excel 用年月日的格式
     */
    @ExcelProperty("日期标题")
    private Date date;
    /**
     * 我想写到excel 用百分比表示
     */
    @ExcelProperty(value = "数字标题")
//    @ColumnWidth(30)
    private Double doubleData;
}
