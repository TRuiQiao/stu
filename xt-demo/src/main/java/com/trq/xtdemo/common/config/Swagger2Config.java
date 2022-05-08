package com.trq.xtdemo.common.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * swagger2配置类
 * @author trq
 * @version 1.0
 * @since 2022/5/8 16:21
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    // 默认访问地址为：http://localhost:端口/swagger-ui.html
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(HttpServletRequest.class, HttpServletResponse.class)
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.trq.xtdemo")
                )).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")// 页面标题
                .version("1.0")// 版本号
                .description("API文档")// 描述
                .build();
    }
}
