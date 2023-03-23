package com.lhw.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMAT_WHIFFLETREE_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_WHIFFLETREE_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DIAGONAL_SECOND = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT_TIMESTAMP = "yyyy/MM/dd HH:mm:ss.S";
    public static final String DATE_FORMAT_DIAGONAL_DAY = "yyyy/MM/dd";
    public static final String DATE_FORMAT_MINUTE = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_DAY = "yyyyMMdd";
    public static final String DATE_FORMAT_MONTH = "yyyyMM";
    public static final String DATE_FORMAT_WHIFFLETREE_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_WHIFFLETREE_MICRO = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String DATE_FORMAT_T_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DATE_FORMAT_ZONE_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'";
    public static final String DATE_FORMAT_ZONE_COLON_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'";
    public static final String DATE_FORMAT_TIME = "HH:mm:ss";
    public static final String DATE_FORMAT_TIME_MILLIS = "HH:mm:ss.SSS";
    public static final String DATE_FORMAT_TIME_MICRO = "HH:mm:ss.SSSSSS";
    public static final DateTimeFormatter DATE_FORMAT_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            DATE_FORMAT_TIME);
    public static final DateTimeFormatter DATE_FORMAT_TIME_MICRO_FORMATTER = DateTimeFormatter.ofPattern(
            DATE_FORMAT_TIME_MICRO);
    public static final DateTimeFormatter DATE_FORMAT_WHIFFLETREE_SECOND_FORMATTER = DateTimeFormatter
            .ofPattern(DATE_FORMAT_WHIFFLETREE_SECOND);
    public static final DateTimeFormatter DATE_FORMAT_WHIFFLETREE_MICRO_FORMATTER = DateTimeFormatter
            .ofPattern(DATE_FORMAT_WHIFFLETREE_MICRO);
    public static final DateTimeFormatter DATE_FORMAT_WHIFFLETREE_DAY_FORMATTER = DateTimeFormatter.ofPattern(
            DATE_FORMAT_WHIFFLETREE_DAY);
    public static final DateTimeFormatter DATE_FORMAT_DAY_FORMATTER = DateTimeFormatter.ofPattern(
            DATE_FORMAT_DAY);
    public static final DateTimeFormatter DATE_FORMAT_WHIFFLETREE_MILLIS_FORMATTER = DateTimeFormatter.ofPattern(
            DATE_FORMAT_WHIFFLETREE_MILLIS);
    private static final int DATABASE_PRECISION = 6;
    /**
     * 存储格式的Map
     */
    private static final ThreadLocal<Map<String, SimpleDateFormat>> DATE_FORMAT_MAP = ThreadLocal
            .withInitial(HashMap::new);
    public static final SimpleDateFormat DATE_FORMAT_WHIFFLETREE_SECOND_DATE_FORMAT = getFormat(
            DATE_FORMAT_WHIFFLETREE_SECOND);
    public static final SimpleDateFormat DATE_FORMAT_WHIFFLETREE_DAY_DATE_FORMAT = getFormat(
            DATE_FORMAT_WHIFFLETREE_DAY);
    public static final SimpleDateFormat DATE_FORMAT_TIME_DATE_FORMAT = getFormat(DATE_FORMAT_TIME);
    public static final SimpleDateFormat DATE_FORMAT_TIME_MILLIS_FORMAT = getFormat(
            DATE_FORMAT_TIME_MILLIS);
    public static final SimpleDateFormat DATE_FORMAT_WHIFFLETREE_MILLIS_DATE_FORMAT = getFormat(
            DATE_FORMAT_WHIFFLETREE_MILLIS);

    public static SimpleDateFormat getFormat(final String pattern) {
        Map<String, SimpleDateFormat> dateFormatMap = DATE_FORMAT_MAP.get();
        SimpleDateFormat dateFormat = dateFormatMap.get(pattern);
        if (null == dateFormat) {
            if (null == pattern || pattern.length() == 0) {
                dateFormat = new SimpleDateFormat(DATE_FORMAT_ZONE_UTC);
                dateFormatMap.put(pattern, dateFormat);
            } else {
                dateFormat = new SimpleDateFormat(pattern);
                dateFormatMap.put(pattern, dateFormat);
            }
        }
        return dateFormat;
    }

    public static String format(Date date, String pattern) {
        return getFormat(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getFormat(pattern).parse(dateStr);
    }

    public static Date parse(String dateStr) throws ParseException {
        return getFormat("").parse(dateStr);
    }

    public static String getNextDayStart() {
        long nowTime = System.currentTimeMillis();
        long nextDayStartTime =
                nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (3600_000 * 24)
                        + 3600_000 * 24;
        String nextDayStart = getFormat(DATE_FORMAT_WHIFFLETREE_SECOND)
                .format(new Date(nextDayStartTime));
        return nextDayStart;
    }

    public static String getTodayEnd() {
        long nowTime = System.currentTimeMillis();
        long todayEndTime =
                nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (3600_000 * 24)
                        + 3600_000 * 24 - 1;
        String todayEnd = getFormat(DATE_FORMAT_WHIFFLETREE_SECOND).format(new Date(todayEndTime));
        return todayEnd;
    }

    public static String getTodayStart() {
        long nowTime = System.currentTimeMillis();
        long todayStartTime =
                nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (3600_000 * 24);
        String todayStart = getFormat(DATE_FORMAT_WHIFFLETREE_SECOND).format(new Date(todayStartTime));
        return todayStart;
    }

    /**
     * 获取指定偏移量天数的开始时间(明天偏移量为1今天偏移量为0昨天偏移量为 - 1)
     * @param offset
     * @return
     */
    public static LocalDateTime getDayStart(int offset) {
        return LocalDateTime.of(LocalDate.now().plusDays(offset), LocalTime.of(0, 0, 0));
    }

    /**
     * 获取指定偏移量天数的结束时间(明天偏移量为1今天偏移量为0昨天偏移量为 - 1)
     * @param offset
     * @return
     */
    public static LocalDateTime getDayEnd(int offset) {
        return LocalDateTime.of(LocalDate.now().plusDays(offset), LocalTime.of(23, 59, 59));
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return dateToLocalDateTime(date, ZoneId.systemDefault());
    }

    public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
        if (null == date) {
            return null;
        }
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
        if (null == localDateTime) {
            return null;
        }
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return localDateTimeToDate(localDateTime, ZoneId.systemDefault());
    }

    /**
     * 时间戳转LocalDateTime(指定时区偏移量)
     * @param timestamp
     * @param zoneOffset
     * @return
     */
    public static LocalDateTime timestampToLocalDateTime(Long timestamp, int zoneOffset) {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(zoneOffset));
    }

    /**
     * LocalDateTime转时间戳
     * @param localDateTime
     * @return
     */
    public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTimeToMilliTimestamp(localDateTime) / 1000;
    }

    public static Long localDateTimeToMilliTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
