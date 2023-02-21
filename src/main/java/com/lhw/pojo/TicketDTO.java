package com.lhw.pojo;

import lombok.Data;

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
     * 出发时间
     */
    private String departureTime;

    /**
     * 车次
     */
    private String train;

    /**
     * 历时
     */
    private String duration;

    /**
     * 中转站
     */
    private String transferStation;

    /**
     * 到站时间
     */
    private String arrivalTime;

    /**
     * 票价
     */
    private String price;
}
