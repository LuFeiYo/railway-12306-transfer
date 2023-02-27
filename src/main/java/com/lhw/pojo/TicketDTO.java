package com.lhw.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lufei.lhw
 * @date 2023/2/20 12:22
 * @description 车票信息
 */
@Data
public class TicketDTO {

    /**
     * 出发站
     */
    private String fromStation;

    /**
     * 出发日期
     */
    private LocalDate departureDate;

    /**
     * 出发时间
     */
    private String departureTime;

    /**
     * 出发日期+时间
     */
    private LocalDateTime departureDateTime;

    /**
     * 车次
     */
    private String train;

    /**
     * 历时
     */
    private String duration;

    /**
     * 当日到达文本
     */
    private String arrivalTheDayText;

    /**
     * 中转站
     */
    private String transferStation;

    /**
     * 到站日期
     */
    private LocalDate arrivalDate;

    /**
     * 到站时间
     */
    private String arrivalTime;

    /**
     * 到站日期+时间
     */
    private LocalDateTime arrivalDateTime;

    /**
     * 票价
     */
    private String price;
}
