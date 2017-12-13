package com.creatoo.szwhg.core.model;

import com.creatoo.szwhg.core.util.CommonUtil;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.format.DateTimeFormatter;

public class MytDateSerializer extends LocalDateSerializer {

    public MytDateSerializer(){
        super(DateTimeFormatter.ofPattern(CommonUtil.Date_Format));
    }
}
