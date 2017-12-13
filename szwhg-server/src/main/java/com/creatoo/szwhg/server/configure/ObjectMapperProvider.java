package com.creatoo.szwhg.server.configure;

import com.creatoo.szwhg.core.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by yunyan on 2017/3/17.
 */
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    final ObjectMapper objectMapper=new ObjectMapper();

    public ObjectMapperProvider() {
        JavaTimeModule timeModule=new JavaTimeModule();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern(CommonUtil.DateTime_Format);
        DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern(CommonUtil.Date_Format);
        DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern(CommonUtil.Time_Format);
        timeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(dateTimeFormatter));
        timeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(dateTimeFormatter));
        timeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(dateFormatter));
        timeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(timeFormatter));
        timeModule.addSerializer(LocalDate.class,new LocalDateSerializer(dateFormatter));
        timeModule.addSerializer(LocalTime.class,new LocalTimeSerializer(timeFormatter));
        objectMapper.registerModule(timeModule);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public ObjectMapper getContext(Class<?> type) {
        return this.objectMapper;
    }
}