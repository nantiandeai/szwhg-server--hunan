package com.creatoo.szwhg.server.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Created by yunyan on 2017/9/6.
 */
@Configuration
@EnableMongoAuditing
public class SpringMongoConfig  {
    @Autowired
    private MongoDbFactory factory;
    @Autowired
    private MappingMongoConverter converter;

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(factory, converter);
    }
}
