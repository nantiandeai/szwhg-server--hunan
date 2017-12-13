package com.creatoo.szwhg.core.model;

import com.creatoo.szwhg.core.util.CommonUtil;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.format.DateTimeFormatter;

public class MytTimeSerializer extends LocalTimeSerializer {

    public MytTimeSerializer(){
        super(DateTimeFormatter.ofPattern(CommonUtil.Time_Format));
    }
}
