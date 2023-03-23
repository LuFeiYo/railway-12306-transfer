package com.lhw.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS;

/**
 * @author lufei.lhw
 * @date 2023/3/22 15:08
 * @description
 */
public class JsonUtil {

    public static void configTime(ObjectMapper objectMapper, String dateFormat,
                                  String localDateFormat, String localTimeFormat, String localDateTimeFormat) {
        if (StringUtils.isNotEmpty(dateFormat)) {
            objectMapper.setDateFormat(DateUtil.getFormat(dateFormat));
        }

        boolean localDateIsNotEmpty = StringUtils.isNotEmpty(localDateFormat);
        boolean localTimeIsNotEmpty = StringUtils.isNotEmpty(localTimeFormat);
        boolean localDateTimeIsNotEmpty = StringUtils.isNotEmpty(localDateTimeFormat);
        if (localDateIsNotEmpty || localTimeIsNotEmpty || localDateTimeIsNotEmpty) {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            if (localDateIsNotEmpty) {
                javaTimeModule.addSerializer(LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(localDateFormat)));
                javaTimeModule.addDeserializer(LocalDate.class,
                        new LocalDateDeserializer(DateTimeFormatter.ofPattern(localDateFormat)));
            }
            if (localTimeIsNotEmpty) {
                javaTimeModule.addSerializer(LocalTime.class,
                        new LocalTimeSerializer(DateTimeFormatter.ofPattern(localTimeFormat)));
                javaTimeModule.addDeserializer(LocalTime.class,
                        new LocalTimeDeserializer(DateTimeFormatter.ofPattern(localTimeFormat)));
            }
            if (localDateTimeIsNotEmpty) {
                javaTimeModule.addSerializer(LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(localDateTimeFormat)));
                javaTimeModule.addDeserializer(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(localDateTimeFormat)));
            }
            disableIgnoreDuplicateModuleRegistrations(objectMapper);
            objectMapper.registerModule(javaTimeModule);
            enableIgnoreDuplicateModuleRegistrations(objectMapper);
        }
    }

    public static void disableIgnoreDuplicateModuleRegistrations(ObjectMapper objectMapper) {
        objectMapper.disable(IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
    }

    public static void enableIgnoreDuplicateModuleRegistrations(ObjectMapper objectMapper) {
        objectMapper.enable(IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
    }
}
