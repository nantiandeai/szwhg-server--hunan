package com.creatoo.szwhg.core.model;

import com.creatoo.szwhg.core.util.CommonUtil;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.format.DateTimeFormatter;

public class MyDateDeserializer extends LocalDateDeserializer {
    public MyDateDeserializer(){
        super(DateTimeFormatter.ofPattern(CommonUtil.Date_Format));
    }
}
