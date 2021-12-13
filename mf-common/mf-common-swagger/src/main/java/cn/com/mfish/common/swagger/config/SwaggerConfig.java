package cn.com.mfish.common.swagger.config;

import cn.com.mfish.common.core.constants.Constants;
import io.swagger.annotations.Api;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ：qiufeng
 * @description：swagger配置类
 * @date ：2021/11/15 12:59
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig implements WebMvcConfigurer {
    /**
     * swagger接口设置
     *
     * @param swaggerProperties
     * @return
     */
    @Bean
    public Docket docket(SwaggerProperties swaggerProperties) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
        if (swaggerProperties.getNeedAuth()) {
            docket.securitySchemes(Collections.singletonList(securityScheme()))
                    .securityContexts(securityContexts());
        }
        return docket;
    }

    /***
     *
     * 请求认证模式配置，通过通过Authorization头请求头传递
     * @return
     */
    SecurityScheme securityScheme() {
        return new ApiKey(Constants.AUTHENTICATION, Constants.AUTHENTICATION, Constants.HEADER);
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(o -> o.requestMappingPattern().matches("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    /**
     * 默认的安全上下文引用
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything")};
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(Constants.AUTHENTICATION, authorizationScopes));
        return securityReferences;
    }

    /**
     * 接口信息
     *
     * @param swaggerProperties
     * @return
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** swagger-ui 地址 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }

}
