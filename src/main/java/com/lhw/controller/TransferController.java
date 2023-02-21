package com.lhw.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.lhw.service.TransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @GetMapping("/getExcelResult")
    public void getExcelResult(@RequestParam("fromStation") @ApiParam("出发地") String fromStation,
                               @RequestParam("toStation") @ApiParam("到达地") String toStation,
                               @RequestParam("transferStationList") @ApiParam("中转地列表") List<String> transferStationList) {
        transferService.getExcelResult(fromStation, toStation, transferStationList);
    }
}
