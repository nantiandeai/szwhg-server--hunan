package com.creatoo.szwhg.core.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by yunyan on 2017/8/3.
 */
@Data
public abstract class IdEntity {
    @Id
    private String id;
}
