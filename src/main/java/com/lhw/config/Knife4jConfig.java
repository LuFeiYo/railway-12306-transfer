package com.lhw.config;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * @author: Ares
 * @date: 2021/6/16 13:32
 * @description: knife4j config
 * @version: JDK 1.8
 */
@EnableOpenApi
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true")
@AllArgsConstructor
public class Knife4jConfig {

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Bean(value = "abilityCenter")
    public Docket abilityCenter() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(abilityCenterApiInfo())
                // 分组名称
                .groupName("铁路12306中转")
                .select()
                // 这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo abilityCenterApiInfo() {
        return new ApiInfoBuilder()
                .title("铁路12306中转")
                .description("铁路12306中转")
                .version("1.0.0")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    private SecurityContext extendSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(extendAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return CollectionUtils.newArrayList(new SecurityReference("Cookie", authorizationScopes));
    }

    List<SecurityReference> extendAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return CollectionUtils.newArrayList(new SecurityReference("Cookie", authorizationScopes));
    }
}
