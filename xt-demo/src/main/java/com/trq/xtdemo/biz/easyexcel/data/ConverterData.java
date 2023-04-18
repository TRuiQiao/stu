package com.trq.xtdemo.biz.easyexcel.data;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.trq.xtdemo.biz.easyexcel.write.convert.CustomStringConverter;
import lombok.Data;

import java.util.Date;

/**
 * @author trq
 * @version 1.0
 * @since 2023/3/23 23:00
 * 自定义格式转换数据
 */
@Data
public class ConverterData {

    /**
     * 我想所有的 字符串起前面加上"自定义："三个字
     */
    @ExcelProperty(value = "字符串标题", converter = CustomStringConverter.class)
    private String string;
    /**
     * 我想写到excel 用年月日的格式
     */
//    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
//    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat("yyyy/MM/dd HH:mm")
    @ExcelProperty("日期标题")
    private Date date;
    /**
     * 我想写到excel 用百分比表示
     */
    @NumberFormat("#.##%")
    @ExcelProperty(value = "数字标题")
    private Double doubleData;
}
