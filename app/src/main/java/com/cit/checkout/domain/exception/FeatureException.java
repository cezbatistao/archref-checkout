package com.cit.checkout.domain.exception;

import com.cit.checkout.conf.ff4j.Features;
import lombok.Getter;

@Getter
public class FeatureException extends RuntimeException {

    private static final long serialVersionUID = -3975602943736930273L;

    private final Features featureFlag;

    public FeatureException(Features featureFlag, String message) {
        super(message);
        this.featureFlag = featureFlag;
    }
}
