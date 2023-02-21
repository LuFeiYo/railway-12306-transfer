package com.lhw.service.impl;

import com.lhw.enums.OperationSystemEnum;
import com.lhw.pojo.TicketDTO;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author lufei.lhw
 * @date 2023/2/14 13:03
 * @description 中转接口实现类
 */
@Service
public class TransferServiceImpl implements TransferService {

    @Override
    public void getExcelResult(String fromStation, String toStation, List<String> transferStationList) {
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
            toTransferStationMap.put(transferStation, generateTicketDTOList(driver, fromStation, transferStation));
            // 计算从中转地到到达地
            fromTransferStationMap.put(transferStation, generateTicketDTOList(driver, transferStation, toStation));
        }
        driver.quit();
        // 生成方案
    }

    /**
     * 根据出发地和目的地获取火车车次信息
     * @param driver      驱动
     * @param fromStation 出发地
     * @param toStation   目的地
     * @return 火车车次信息
     */
    private List<TicketDTO> generateTicketDTOList(WebDriver driver, String fromStation, String toStation) {
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
            driver.findElement(By.id("train_date")).sendKeys(LocalDate.now().plusDays(13L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
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
                    // 票价
                    WebElement priceWebElement = webElementList.get(i + 1);
                    List<WebElement> priceTdElementList = priceWebElement.findElements(By.tagName("td"));
                    if (CollectionUtils.isNotEmpty(priceTdElementList)) {
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
