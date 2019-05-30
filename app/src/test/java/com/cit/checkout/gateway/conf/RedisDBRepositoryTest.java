package com.cit.checkout.gateway.conf;

import com.cit.checkout.conf.ff4j.FF4jConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import static java.lang.String.format;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RedisDBTestConfiguration.class, FF4jConfiguration.class })
@ActiveProfiles("test")
@ContextConfiguration(initializers = RedisDBRepositoryTest.Initializer.class)
public abstract class RedisDBRepositoryTest {

    static GenericContainer redisContainer = new GenericContainer("redis:3.2.4")
            .withExposedPorts(6379);

    static {
        redisContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    format("ff4j.redis.server=%s", redisContainer.getContainerIpAddress()),
                    format("ff4j.redis.port=%s", redisContainer.getMappedPort(6379))
            );
            values.applyTo(configurableApplicationContext);
        }
    }
}
