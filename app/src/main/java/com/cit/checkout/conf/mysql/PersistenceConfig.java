package com.cit.checkout.conf.mysql;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.cit.checkout.gateway.mysql.model")
@EnableJpaRepositories(basePackages = "com.cit.checkout.gateway.mysql")
public class PersistenceConfig {

}
