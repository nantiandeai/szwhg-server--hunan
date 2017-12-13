package com.creatoo.szwhg.core.model;

import com.creatoo.szwhg.core.util.CommonUtil;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.format.DateTimeFormatter;

public class MyDateTimeDeserializer extends LocalDateTimeDeserializer {
    public MyDateTimeDeserializer(){
        super(DateTimeFormatter.ofPattern(CommonUtil.DateTime_Format));
    }
}
