package com.trq.xtdemo.biz.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.trq.xtdemo.biz.easyexcel.data.*;
import com.trq.xtdemo.biz.easyexcel.util.TestFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author trq
 * @version 1.0
 * @since 2023/3/23 22:23
 */
@Slf4j
public class WriteExcel {

    public static void main(String[] args) {
        WriteExcel excel = new WriteExcel();
        long start = System.currentTimeMillis();
        try {
//            excel.simpleWrite();
//            excel.excludeOrIncludeWrite();
//            excel.indexWrite();
//            excel.converterWrite();
            excel.widthAndHeightWrite();

            long end = System.currentTimeMillis();
            log.info("总耗时：{}ms,{}s", (end - start), (end - start) / 1000);
        } catch (Exception e) {
            log.error("导出异常", e);
        }
    }

    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 直接写即可
     */
    public void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
//        EasyExcel.write(fileName, DemoData.class)
//                .sheet("模板")
//                .doWrite(() -> {
//                    // 分页查询数据
//                    return data();
//                });

        // 写法2
        fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());
//
//        // 写法3
//        fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写
//        try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build()) {
//            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
//            excelWriter.write(data(), writeSheet);
//        }
        log.info("导出成功");
    }

    /**
     * 根据参数只导出指定列
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 根据自己或者排除自己需要的列
     * <p>
     * 3. 直接写即可
     *
     * @since 2.1.1
     */
    public void excludeOrIncludeWrite() {
        String fileName = TestFileUtil.getPath() + "\\excludeOrIncludeWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

        // 根据用户传入字段 假设我们要忽略 date
        Set<String> excludeColumnFiledNames = new HashSet<String>();
        excludeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class)
                .excludeColumnFiledNames(excludeColumnFiledNames)
                .sheet("模板")
                .doWrite(data());

//        fileName = TestFileUtil.getPath() + "excludeOrIncludeWrite" + System.currentTimeMillis() + ".xlsx";
//        // 根据用户传入字段 假设我们只要导出 date
//        Set<String> includeColumnFiledNames = new HashSet<String>();
//        includeColumnFiledNames.add("date");
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        EasyExcel.write(fileName, DemoData.class)
//                .includeColumnFiledNames(includeColumnFiledNames)
//                .sheet("模板")
//                .doWrite(data());
        log.info("导出成功");
    }

    /**
     * 指定写入的列
     * <p>1. 创建excel对应的实体对象 参照{@link IndexData}
     * <p>2. 使用{@link ExcelProperty}注解指定写入的列
     * <p>3. 直接写即可
     */
    public void indexWrite() {
        String fileName = TestFileUtil.getPath() + "indexWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        EasyExcel.write(fileName, IndexData.class).sheet("模板").doWrite(data());
        EasyExcel.write(fileName, IndexData2.class).sheet("模板").doWrite(data());
        log.info("导出成功");
    }

    /**
     * 日期、数字或者自定义格式转换
     * <p>1. 创建excel对应的实体对象 参照{@link ConverterData}
     * <p>2. 使用{@link ExcelProperty}配合使用注解{@link DateTimeFormat}、{@link NumberFormat}或者自定义注解
     * <p>3. 直接写即可
     */
    public void converterWrite() {
        String fileName = TestFileUtil.getPath() + "converterWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, ConverterData.class).sheet("模板").doWrite(data());
        log.info("导出成功");
    }

    /**
     * 列宽、行高
     * <p>1. 创建excel对应的实体对象 参照{@link WidthAndHeightData}
     * <p>2. 使用注解{@link ColumnWidth}、{@link HeadRowHeight}、{@link ContentRowHeight}指定宽度或高度
     * <p>3. 直接写即可
     */
    public void widthAndHeightWrite() {
        String fileName = TestFileUtil.getPath() + "widthAndHeightWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        List<DemoData> data = bigData();

        long start = System.currentTimeMillis();
        EasyExcel.write(fileName, WidthAndHeightData.class).sheet("模板").doWrite(data);
        log.info("导出成功，耗时：{}s", (System.currentTimeMillis() - start)/1000);
    }

    /**
     * 数据封装
     * @return
     */
    private List<DemoData> data() {
        List<DemoData> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.0056);
            list.add(data);
        }
        return list;
    }


    private List<DemoData> bigData() {
        List<DemoData> list = new ArrayList();
        for (int i = 0; i < 500000; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
}
