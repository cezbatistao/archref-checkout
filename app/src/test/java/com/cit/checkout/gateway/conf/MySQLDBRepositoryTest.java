package com.cit.checkout.gateway.conf;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.cit.checkout.conf.mysql.PersistenceConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import static java.lang.String.format;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MySQLDBTestConfiguration.class, PersistenceConfig.class})
@ActiveProfiles("test")
@Sql(executionPhase= Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:/test-sql/clean-db.sql")
@ContextConfiguration(initializers = MySQLDBRepositoryTest.Initializer.class)
public abstract class MySQLDBRepositoryTest {

    static MySQLContainer mysqlContainer = (MySQLContainer) new MySQLContainer("mysql:5.7")
            .withDatabaseName("db_integration_test")
            .withUsername("user_integration_test")
            .withPassword("passwd_integration_test")
            .withExposedPorts(3306);
//            .withCreateContainerCmdModifier(
//                    new Consumer<CreateContainerCmd>() {
//                        @Override
//                        public void accept(CreateContainerCmd createContainerCmd) {
//                            createContainerCmd.withPortBindings(new PortBinding(Ports.Binding.bindPort(3306), new ExposedPort(3306)));
//                        }
//                    }
//            )
//            .withStartupTimeout(Duration.ofSeconds(600);

    static {
        mysqlContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    format("spring.datasource.url=%s", mysqlContainer.getJdbcUrl()),
                    format("spring.datasource.username=%s", mysqlContainer.getUsername()),
                    format("spring.datasource.password=%s", mysqlContainer.getPassword())
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @Before
    public void setup() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates");
    }
}
