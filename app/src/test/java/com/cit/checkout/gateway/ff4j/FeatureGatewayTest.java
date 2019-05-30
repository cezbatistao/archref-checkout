package com.cit.checkout.gateway.ff4j;

import com.cit.checkout.conf.ff4j.Features;
import com.cit.checkout.gateway.FeatureGateway;
import com.cit.checkout.gateway.conf.RedisDBRepositoryTest;
import org.ff4j.FF4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FeatureGatewayTest extends RedisDBRepositoryTest {

    @Autowired
    private FeatureGateway featureGateway;

    @Autowired
    private FF4j ff4j;

    @Test
    public void featureEnabled() {
        // GIVEN
        ff4j.enable(Features.BILLET_PAYMENT.getKey());

        // WHEN
        boolean featureEnable = featureGateway.isFeatureEnable(Features.BILLET_PAYMENT);

        // THEN
        assertThat(featureEnable, equalTo(true));
    }

    @Test
    public void featureDisabled() {
        // GIVEN
        ff4j.disable(Features.BILLET_PAYMENT.getKey());

        // WHEN
        boolean featureEnable = featureGateway.isFeatureEnable(Features.BILLET_PAYMENT);

        // THEN
        assertThat(featureEnable, equalTo(false));
    }
}
