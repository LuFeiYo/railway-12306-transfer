package com.lhw.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author lufei.lhw
 * @date 2023/2/20 12:22
 * @description 车票信息
 */
@Data
public class TicketExcelData {

    /**
     * 出发站
     */
    @ExcelProperty({"车次一", "出发站"})
    private String fromStation1;

    /**
     * 出发时间
     */
    @ExcelProperty({"车次一", "出发时间"})
    private String departureTime1;

    /**
     * 车次
     */
    @ExcelProperty({"车次一", "车次"})
    private String train1;

    /**
     * 票价
     */
    @ExcelProperty({"车次一", "票价"})
    private String price1;

    /**
     * 历时
     */
    @ExcelProperty({"车次一", "历时"})
    private String duration1;

    /**
     * 到达情况
     */
    @ExcelProperty({"车次一", "到达情况"})
    private String arriveStatus1;

    /**
     * 到达日期
     */
    @ExcelProperty({"车次一", "到达日期"})
    private String arrivalDate1;

    /**
     * 到达时间
     */
    @ExcelProperty({"车次一", "到达时间"})
    private String arrivalTime1;

    /**
     * 到达时间
     */
    @ExcelProperty({"车次一", "到达日期时间"})
    private String arrivalDateTime1;

    /**
     * 到达地
     */
    @ExcelProperty({"车次一", "到达地"})
    private String toStation1;

    /**
     * 停留时间
     */
    @ExcelProperty("停留时间")
    private String residenceTime;

    /**
     * 出发站
     */
    @ExcelProperty({"车次二", "出发站"})
    private String fromStation2;

    /**
     * 出发时间
     */
    @ExcelProperty({"车次二", "出发时间"})
    private String departureTime2;

    /**
     * 车次
     */
    @ExcelProperty({"车次二", "车次"})
    private String train2;

    /**
     * 票价
     */
    @ExcelProperty({"车次二", "票价"})
    private String price2;

    /**
     * 历时
     */
    @ExcelProperty({"车次二", "历时"})
    private String duration2;

    /**
     * 到达情况
     */
    @ExcelProperty({"车次二", "到达情况"})
    private String arriveStatus2;

    /**
     * 到达日期
     */
    @ExcelProperty({"车次二", "到达日期"})
    private String arrivalDate2;

    /**
     * 到达时间
     */
    @ExcelProperty({"车次二", "到达时间"})
    private String arrivalTime2;

    /**
     * 到达时间
     */
    @ExcelProperty({"车次二", "到达日期时间"})
    private String arrivalDateTime2;

    /**
     * 到达地
     */
    @ExcelProperty({"车次二", "到达地"})
    private String toStation2;

    /**
     * 总历时
     */
    @ExcelProperty("总历时")
    private String totalDuration;

    /**
     * 总票价
     */
    @ExcelProperty("总票价")
    private String totalPrice;

    /**
     * 筛选条件-是否同站换乘
     */
    @ExcelProperty("是否同站换乘")
    private String isSameStation;
}
