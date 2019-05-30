package com.cit.checkout.gateway.conf;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "com.cit.checkout.gateway.mysql")
@EntityScan("com.cit.checkout.gateway.mysql")
@EnableJpaRepositories(basePackages = "com.cit.checkout.gateway.mysql")
public class MySQLDBTestConfiguration {

}
