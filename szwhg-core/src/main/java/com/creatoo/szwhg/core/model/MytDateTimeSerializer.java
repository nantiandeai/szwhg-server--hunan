package com.creatoo.szwhg.core.model;

import com.creatoo.szwhg.core.util.CommonUtil;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.format.DateTimeFormatter;

public class MytDateTimeSerializer extends LocalDateTimeSerializer {

    public MytDateTimeSerializer(){
        super(DateTimeFormatter.ofPattern(CommonUtil.DateTime_Format));
    }
}
