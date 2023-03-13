package com.lhw.controller;

import com.alibaba.excel.EasyExcel;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.lhw.pojo.TicketExcelData;
import com.lhw.service.TransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lufei.lhw
 * @date 2023/2/14 12:40
 * @description 中转的接口
 */
@CrossOrigin
@Api(tags = "01 中转")
@ApiSort(value = 1)
@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Resource
    private TransferService transferService;

    @ApiOperation("001 根据[出发地、到达地、中转地列表]生成中转方案")
    @ApiOperationSupport(order = 1)
    @GetMapping("/downloadTicketExcel")
    public void downloadTicketExcel(@RequestParam("fromStation") @ApiParam("出发地") String fromStation,
                                    @RequestParam("toStation") @ApiParam("到达地") String toStation,
                                    @RequestParam("customTransferStationFlag") @ApiParam("是否自定义中转站") Boolean customTransferStationFlag,
                                    @RequestParam(value = "transferStationList", required = false) @ApiParam("中转地列表") List<String> transferStationList,
                                    @RequestParam("departureDate") @ApiParam("出发日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departureDate,
                                    HttpServletResponse response) throws IOException {
        List<TicketExcelData> ticketExcelDataList = transferService.listTicketResult(fromStation, toStation, customTransferStationFlag, transferStationList, departureDate);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(fromStation + "-" + toStation + "-" + LocalDateTime.now(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TicketExcelData.class)
                .sheet(fromStation + "->" + toStation)
                .doWrite(ticketExcelDataList);
    }
}
