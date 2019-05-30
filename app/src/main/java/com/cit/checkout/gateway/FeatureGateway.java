package com.cit.checkout.gateway;

import com.cit.checkout.conf.ff4j.Features;

@FunctionalInterface
public interface FeatureGateway {

    boolean isFeatureEnable(Features features);

}
