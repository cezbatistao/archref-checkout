package com.cit.checkout.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "sample-app", ignoreUnknownFields = false)
public class SampleArchrefProperties {

    private final Async async = new Async();
    private final Billet billet = new Billet();

    public Async getAsync() {
        return async;
    }

    public Billet getBillet() {
        return billet;
    }

    @Data
    public static class Async {
        private Integer corePoolSize;
    }

    @Data
    public static class Billet {
        private int daysDueDate;
        private BigDecimal additionPrice;
    }
}