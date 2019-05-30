package com.cit.checkout;

import com.cit.checkout.config.cucumber.SpringProfileCucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(SpringProfileCucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber"},
        features = {"src/test/resources/com/cit/checkout/Order.feature"}
)
public class OrderTest {

}
