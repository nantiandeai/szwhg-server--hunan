package com.creatoo.szwhg.core.model;

import com.creatoo.szwhg.core.util.CommonUtil;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import java.time.format.DateTimeFormatter;

public class MyTimeDeserializer extends LocalTimeDeserializer {
    public MyTimeDeserializer(){
        super(DateTimeFormatter.ofPattern(CommonUtil.Time_Format));
    }
}
