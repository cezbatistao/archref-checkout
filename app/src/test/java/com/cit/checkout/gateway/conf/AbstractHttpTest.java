package com.cit.checkout.gateway.conf;

import com.cit.checkout.conf.SampleArchrefProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Import(SampleArchrefProperties.class)
public abstract class AbstractHttpTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    protected MockMvc buildMockMvcWithBusinessExecptionHandler() {
        return MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
    }

    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
