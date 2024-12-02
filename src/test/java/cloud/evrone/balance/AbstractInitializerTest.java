package cloud.evrone.balance;


import static org.junit.jupiter.api.Assertions.assertEquals;

import cloud.evrone.balance.generator.EntityCreationHelper;
import cloud.evrone.balance.repository.AccountRepository;
import cloud.evrone.balance.repository.TransactionRepository;
import cloud.evrone.balance.test.container.PostgresqlContainer;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@Slf4j
@SpringBootTest(classes = BalanceApplication.class)
@ContextConfiguration(initializers = {AbstractInitializerTest.Initializer.class})
@AutoConfigureWebTestClient
public class AbstractInitializerTest {

  public static final PostgresqlContainer postgresql = new PostgresqlContainer();

  @Autowired
  protected WebTestClient client;

  @Autowired
  protected AccountRepository accountRepository;
  @Autowired
  protected TransactionRepository transactionRepository;

  @Autowired
  protected EntityCreationHelper creationHelper;

  @BeforeEach
  public void cleanUp() {
    log.info("Clean up...");
    transactionRepository.deleteAll().block();
    accountRepository.deleteAll().block();
  }


  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      postgresql.setCommand("postgres", "-c", "log_statement=all");
      postgresql.setPortBindings(List.of("5433:5432"));

      postgresql.start();

      configurationAppProps(configurableApplicationContext);
    }

    private void configurationAppProps(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of(
              "spring.datasource.url=" + postgresql.getJdbcUrl(),
              "spring.datasource.username=" + postgresql.getUsername(),
              "spring.datasource.password=" + postgresql.getPassword(),
              "app.r2dbc.url=" + postgresql.getJdbcUrl().replace("jdbc", "r2dbc")
          )
          .applyTo(applicationContext.getEnvironment());

      applicationContext.addApplicationListener((e) -> {
        if (e instanceof ContextStoppedEvent) {
          postgresql.stop();
        }
      });
    }
  }

  protected void assertRowsInTable(int expected, ReactiveCrudRepository<?, ?> repository) {
    StepVerifier.create(repository.count())
        .assertNext(count -> assertEquals(expected, count))
        .expectComplete()
        .verify(Duration.ofSeconds(10));
  }

}
