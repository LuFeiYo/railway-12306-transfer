package com.lhw.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.lhw.enums.OperationSystemEnum;
import com.lhw.pojo.TicketDTO;
import com.lhw.pojo.TicketExcelData;
import com.lhw.service.TransferService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lufei.lhw
 * @date 2023/2/14 13:03
 * @description 中转接口实现类
 */
@Service
public class TransferServiceImpl implements TransferService {

    @Override
    public List<TicketExcelData> listTicketResult(String fromStation, String toStation, List<String> transferStationList, LocalDate departureDate) {
        List<TicketExcelData> ticketExcelDataList = new ArrayList<>();
        ChromeOptions option = new ChromeOptions();
        String osName = System.getProperty("os.name");
        if (osName.equals(OperationSystemEnum.LINUX.getSystemName())) {
            // 没有窗口的模式
            option.addArguments("--headless");
            // 沙盒模式
            option.addArguments("--no-sandbox");
        }
        WebDriver driver = new ChromeDriver(option);
        driver.manage().window().maximize();
        // 从出发地到中转地
        Map<String, List<TicketDTO>> toTransferStationMap = new HashMap<>();
        // 从中转地到到达地
        Map<String, List<TicketDTO>> fromTransferStationMap = new HashMap<>();
        for (String transferStation : transferStationList) {
            // 计算从出发地到中转地
            List<TicketDTO> toTicketDTOList = generateTicketDTOList(driver, fromStation, transferStation, departureDate);
            toTransferStationMap.putAll(toTicketDTOList.stream().collect(Collectors.groupingBy(item -> {
                return transferStation + ":" + item.getArrivalDate();
            })));
        }
        for (String key : toTransferStationMap.keySet()) {
            // 计算从中转地到到达地
            fromTransferStationMap.put(key, generateTicketDTOList(driver, key.split(":")[0], toStation, LocalDate.parse(key.split(":")[1])));
        }
        driver.quit();
        for (Map.Entry<String, List<TicketDTO>> toMap : toTransferStationMap.entrySet()) {
            String toTransferStation = toMap.getKey();
            for (Map.Entry<String, List<TicketDTO>> fromMap : fromTransferStationMap.entrySet()) {
                String fromTransferStation = fromMap.getKey();
                if (toTransferStation.equals(fromTransferStation)) {
                    List<TicketDTO> toTicketDTOList = toMap.getValue();
                    List<TicketDTO> fromTicketDTOList = fromMap.getValue();
                    for (TicketDTO toTicketDTO : toTicketDTOList) {
                        for (TicketDTO fromTicketDTO : fromTicketDTOList) {
                            if (toTicketDTO.getArrivalDateTime().isBefore(fromTicketDTO.getDepartureDateTime())) {
                                TicketExcelData ticketExcelData = new TicketExcelData();
                                ticketExcelData.setFromStation1(toTicketDTO.getFromStation());
                                ticketExcelData.setDepartureTime1(toTicketDTO.getDepartureTime());
                                ticketExcelData.setTrain1(toTicketDTO.getTrain());
                                ticketExcelData.setPrice1(toTicketDTO.getPrice());
                                ticketExcelData.setDuration1(toTicketDTO.getDuration());
                                ticketExcelData.setArriveStatus1(toTicketDTO.getArrivalTheDayText());
                                ticketExcelData.setArrivalDate1(toTicketDTO.getArrivalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                                ticketExcelData.setArrivalTime1(toTicketDTO.getArrivalTime());
                                ticketExcelData.setArrivalDateTime1(toTicketDTO.getArrivalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                ticketExcelData.setToStation1(toTicketDTO.getTransferStation());

                                String train1StartTime = toTicketDTO.getDepartureDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                String train2StartTime = fromTicketDTO.getDepartureDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                ticketExcelData.setResidenceTime(DateUtil.formatBetween(DateUtil.parse(ticketExcelData.getArrivalDateTime1()), DateUtil.parse(train2StartTime), BetweenFormatter.Level.MINUTE));

                                ticketExcelData.setFromStation2(fromTicketDTO.getFromStation());
                                ticketExcelData.setDepartureTime2(fromTicketDTO.getDepartureTime());
                                ticketExcelData.setTrain2(fromTicketDTO.getTrain());
                                ticketExcelData.setPrice2(fromTicketDTO.getPrice());
                                ticketExcelData.setDuration2(fromTicketDTO.getDuration());
                                ticketExcelData.setArriveStatus2(fromTicketDTO.getArrivalTheDayText());
                                ticketExcelData.setArrivalDate2(fromTicketDTO.getArrivalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                                ticketExcelData.setArrivalTime2(fromTicketDTO.getArrivalTime());
                                ticketExcelData.setArrivalDateTime2(fromTicketDTO.getArrivalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                ticketExcelData.setToStation2(fromTicketDTO.getTransferStation());
                                ticketExcelData.setTotalDuration(DateUtil.formatBetween(DateUtil.parse(train1StartTime), DateUtil.parse(ticketExcelData.getArrivalDateTime2()), BetweenFormatter.Level.MINUTE));
                                ticketExcelData.setTotalPrice(new BigDecimal(toTicketDTO.getPrice().replaceAll("¥", "")).add(new BigDecimal(fromTicketDTO.getPrice().replaceAll("¥", ""))).toString());
                                ticketExcelData.setIsSameStation(toTicketDTO.getTransferStation().equals(fromTicketDTO.getFromStation()) ? "是" : "否");
                                ticketExcelDataList.add(ticketExcelData);
                            }
                        }
                    }
                }
            }
        }
        return ticketExcelDataList;
    }

    /**
     * 根据出发地和目的地获取火车车次信息
     * @param driver      驱动
     * @param fromStation 出发地
     * @param toStation   目的地
     * @return 火车车次信息
     */
    private List<TicketDTO> generateTicketDTOList(WebDriver driver, String fromStation, String toStation, LocalDate departureDate) {
        List<TicketDTO> ticketDTOList = new ArrayList<>();
        try {
            driver.get("https://www.12306.cn/index/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(60L));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_one")));
            // 加载完成，开始输入参数
            clearAndSendKey(driver.findElement(By.id("fromStationText")), fromStation);
            Thread.sleep(500);
            driver.findElement(By.id("citem_0")).click();
            clearAndSendKey(driver.findElement(By.id("toStationText")), toStation);
            Thread.sleep(500);
            driver.findElement(By.id("citem_0")).click();
            driver.findElement(By.id("train_date")).clear();
            driver.findElement(By.id("train_date")).sendKeys(departureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            driver.findElement(By.id("search_one")).click();
            // 切换到新标签页
            driver.close();
            Set<String> windowHandleSet = driver.getWindowHandles();
            driver.switchTo().window(String.valueOf(windowHandleSet.toArray()[0]));
            WebDriverWait ticketTableWait = new WebDriverWait(driver, Duration.ofMillis(60L));
            ticketTableWait.until(ExpectedConditions.presenceOfElementLocated(By.id("queryLeftTable")));
            WebElement queryLeftTable = driver.findElement(By.id("queryLeftTable"));
            List<WebElement> webElementList = queryLeftTable.findElements(By.tagName("tr"));
            for (int i = 0; i < webElementList.size(); i++) {
                WebElement webElement = webElementList.get(i);
                if (StringUtils.isEmpty(webElement.getAttribute("datatran")) && !webElement.getAttribute("id").equals("lcdata")) {
                    TicketDTO ticketDTO = new TicketDTO();
                    // 出发站
                    String fromStationTemp = webElement.findElements(By.tagName("strong")).get(0).getText();
                    ticketDTO.setFromStation(fromStationTemp);
                    // 出发日期
                    ticketDTO.setDepartureDate(departureDate);
                    // 出发时间
                    String departureTime = webElement.findElements(By.tagName("strong")).get(2).getText();
                    ticketDTO.setDepartureTime(departureTime);
                    // 车次
                    String train = webElement.findElement(By.tagName("a")).getText();
                    ticketDTO.setTrain(train);
                    // 历时
                    String duration = webElement.findElements(By.tagName("strong")).get(4).getText();
                    ticketDTO.setDuration(duration);
                    // 中转站
                    String transferStationTemp = webElement.findElements(By.tagName("strong")).get(1).getText();
                    ticketDTO.setTransferStation(transferStationTemp);
                    // 到站时间
                    String arrivalTime = webElement.findElements(By.tagName("strong")).get(3).getText();
                    ticketDTO.setArrivalTime(arrivalTime);
                    List<WebElement> tdList = webElement.findElements(By.tagName("td"));
                    tdList.get(tdList.size() - 2).click();
                    // 这个时间不可修改
                    Thread.sleep(3500);
                    WebElement priceWebElement = webElementList.get(i + 1);
                    List<WebElement> priceTdElementList = priceWebElement.findElements(By.tagName("td"));
                    if (CollectionUtils.isNotEmpty(priceTdElementList)) {
                        // 出发日期+时间
                        ticketDTO.setDepartureDateTime(LocalDateTime.parse(departureDate + " " + departureTime + ":00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        // 到站日期+时间
                        LocalDateTime arrivalDateTime = ticketDTO.getDepartureDateTime()
                                .plusHours(Long.parseLong(ticketDTO.getDuration().split(":")[0]))
                                .plusMinutes(Long.parseLong(ticketDTO.getDuration().split(":")[1]));
                        ticketDTO.setArrivalDateTime(arrivalDateTime);
                        // 到站日期
                        LocalDate arrivalDate = arrivalDateTime.toLocalDate();
                        ticketDTO.setArrivalDate(arrivalDate);
                        // 当日到达文本
                        String spanText = webElement.findElements(By.tagName("span")).get(2).getText();
                        ticketDTO.setArrivalTheDayText(spanText);
                        // 票价
                        String erDengZuo = priceTdElementList.get(3).getText();
                        String yingZuo = priceTdElementList.get(9).getText();
                        ticketDTO.setPrice(StringUtils.isNotEmpty(yingZuo) ? yingZuo : erDengZuo);
                        ticketDTOList.add(ticketDTO);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ticketDTOList;
    }

    /**
     * 清除输入框内容，并输入值
     * @param webElement 输入框元素
     * @param sendKey    值
     */
    private void clearAndSendKey(WebElement webElement, String sendKey) {
        webElement.sendKeys(Keys.COMMAND + "a");
        webElement.sendKeys(Keys.BACK_SPACE);
        webElement.sendKeys(Keys.CONTROL + "a");
        webElement.sendKeys(Keys.BACK_SPACE);
        webElement.sendKeys(sendKey);
    }
}
