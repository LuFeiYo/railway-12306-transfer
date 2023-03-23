package com.lhw.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.lhw.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.TimeZone;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;

@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@Role(value = ROLE_INFRASTRUCTURE)
public class CacheSerializerConfiguration {

    @Value("${cache.serializer.date.format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Value("${cache.serializer.local-date.format:yyyy-MM-dd}")
    private String localDateFormat;

    @Value("${cache.serializer.local-time.format:HH:mm:ss}")
    private String localTimeFormat;

    @Value("${cache.serializer.local-date-time.format:yyyy-MM-dd HH:mm:ss}")
    private String localDateTimeFormat;

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        JsonUtil.configTime(objectMapper, dateFormat, localDateFormat, localTimeFormat, localDateTimeFormat);
        JsonUtil.disableIgnoreDuplicateModuleRegistrations(objectMapper);
        objectMapper.registerModule(new ParameterNamesModule());
        JsonUtil.enableIgnoreDuplicateModuleRegistrations(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        StringRedisSerializer stringRedisSerializer =  new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
