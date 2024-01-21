package com.zch.common.mvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zch.common.mvc.utils.DateUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;

/**
 * @author Poison02
 * @date 2024/1/21
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JsonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT);
            jacksonObjectMapperBuilder.timeZone(DateUtils.TIME_ZONE_8);
            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
            jacksonObjectMapperBuilder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
            // long -> String
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
        };
    }

}
