package com.cit.checkout.conf.springfox;

import com.cit.checkout.gateway.http.json.PaymentBilletRequest;
import com.cit.checkout.gateway.http.json.PaymentCardRequest;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "io.swagger")
public class SpringfoxConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String applicationVersion;

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .additionalModels(
                        typeResolver.resolve(PaymentCardRequest.class),
                        typeResolver.resolve(PaymentBilletRequest.class))
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cit.checkout.gateway.http"))
                .paths(regex("/api/.*"))
                .build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true)
                .validatorUrl((String) null)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .version(applicationVersion)
                .build();
    }

}
