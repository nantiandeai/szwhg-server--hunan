package com.creatoo.szwhg.server.configure;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by yunyan on 2017/8/4.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey("access_token", "accessToken", "header");
    }


    @Bean
    public Docket restConfig() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("jax-rs")
                .genericModelSubstitutes(DeferredResult.class)
                .apiInfo(restInfo()).forCodeGeneration(true)
                .pathMapping("/").select().paths(paths())// 过滤的接口
                .build().useDefaultResponseMessages(false);
    }

    // 请求url匹配，支持and or，可以过滤筛选
    private Predicate<String> paths() {
        return or(regex("/.*"));
    }



    private ApiInfo restInfo() {
        return new ApiInfoBuilder().title("数字文化馆API ")// 大标题
                .description("湖南文化馆")// 小标题
                .version("1.0").build();
    }
}
