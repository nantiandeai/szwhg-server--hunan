package com.creatoo.szwhg.server.configure;

import com.creatoo.szwhg.base.filter.LogFilter;
import com.creatoo.szwhg.base.filter.RequestFilter;
import com.creatoo.szwhg.core.rest.PageableValueFactoryProvider;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

/**
 * Created by yunyan on 2017/4/10.
 */
@Component
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(LoggingFeature.class);
        //定义包含接口定义的包
        packages("com.creatoo.szwhg");
        //统一异常处理类
        register(DefaultExceptionMapperSupport.class);
        //处理校验异常
        register(ValidationExceptionMapperSupport.class);
        //支持文件上传
        register(MultiPartFeature.class);
        //数据类型转换类
        register(ObjectMapperProvider.class);
        //请求过滤器
        register(RequestFilter.class);
        //自动转换分页对象
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(PageableValueFactoryProvider.class)
                        .to(ValueFactoryProvider.class)
                        .in(Singleton.class);
            }
        });
        //日志过滤器
        register(LogFilter.class);
    }

    @PostConstruct
    public void init() {
        this.configureSwagger();
    }



    private void configureSwagger() {
        // Available at localhost:port/swagger.json
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);
        BeanConfig config = new BeanConfig();
        config.setConfigId("szwhg");
        config.setTitle("数字文化馆系统");
        config.setVersion("v1");
        config.setContact("zhangyunyan");
        config.setSchemes(new String[] { "http"});
        config.setBasePath("/api/v1");
        config.setResourcePackage("com.creatoo.szwhg");
        config.setPrettyPrint(true);
        config.setScan(true);
    }
}
