package com.jyblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j API文档配置类
 * @author LGX_TvT <br>
 * @version 1.0 <br>
 * Create by 2022-04-04 22:25 <br>
 * @description: JyKnife4jConfiguration <br>
 */
@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class) // 开启使用JSR303注解 https://doc.xiaominfo.com/knife4j/documentation/jsr303.html
public class JyKnife4jConfig {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("# swagger-bootstrap-ui-demo RESTful APIs")
                        .version("1.0")
                        .build())
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.jyblog"))
                .paths(PathSelectors.any())
                .build();
    }

}
