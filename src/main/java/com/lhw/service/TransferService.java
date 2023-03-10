package com.lhw.service;

import com.lhw.pojo.TicketExcelData;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lufei.lhw
 * @date 2023/2/14 13:03
 * @description 中转接口
 */
public interface TransferService {

    List<TicketExcelData> listTicketResult(String fromStation, String toStation, Boolean customTransferStationFlag, List<String> transferStationList, LocalDate departureDate);
}
