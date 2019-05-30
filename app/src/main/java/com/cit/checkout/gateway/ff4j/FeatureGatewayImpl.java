package com.cit.checkout.gateway.ff4j;

import com.cit.checkout.conf.ff4j.Features;
import com.cit.checkout.gateway.FeatureGateway;
import org.ff4j.FF4j;
import org.springframework.stereotype.Component;

@Component
public class FeatureGatewayImpl implements FeatureGateway {

    private FF4j ff4j;

    public FeatureGatewayImpl(FF4j ff4j) {
        this.ff4j = ff4j;
    }

    @Override
    public boolean isFeatureEnable(Features features) {
        return ff4j.getFeature(features.getKey()).isEnable();
    }
}
