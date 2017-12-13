package com.creatoo.szwhg.server;

import com.creatoo.szwhg.base.service.SysParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

/**
 * 资源服务器启动类
 * Created by yunyan on 2017/8/3.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.creatoo")
@EnableMongoRepositories("com.creatoo.szwhg")
@EnableMongoAuditing
@Slf4j
public class SzWhgApplication implements CommandLineRunner {
    @Value("${db.rebuild}")
    private Boolean db_rebuildMongoData;
    @Autowired
    private SysParamService paramService;

    public static void main( String[] args ) throws Exception{
        SpringApplication.run(SzWhgApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        paramService.initSysParams();
    }

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(){
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        //判断是否需要重新生成数据库内容
        if(db_rebuildMongoData){
            //加载.json文件
            Resource initData = new ClassPathResource("init-data.json");
            factory.setResources(new Resource[]{initData});
        }else{
            factory.setResources(new Resource[]{});
        }
        return factory;
    }
}
