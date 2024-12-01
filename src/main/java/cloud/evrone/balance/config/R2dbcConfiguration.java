package cloud.evrone.balance.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

@Slf4j
@Data
@Configuration
public class R2dbcConfiguration {

  @Value("${app.r2dbc.url}")
  private String r2dbcUrl;

  @Bean
  public ConnectionFactory connectionFactory(final DataSourceProperties dataSourceProperties,
      final R2dbcProperties r2dbcProperties) {

    ConnectionFactoryOptions.Builder connOptionsBuilder =
        ConnectionFactoryOptions.parse(r2dbcUrl).mutate()
            .option(ConnectionFactoryOptions.DRIVER, "postgresql")
            .option(ConnectionFactoryOptions.USER, dataSourceProperties.getUsername())
            .option(ConnectionFactoryOptions.PASSWORD, dataSourceProperties.getPassword());

    r2dbcProperties.getProperties()
        .forEach((key, value) -> connOptionsBuilder.option(Option.valueOf(key), value));

    ConnectionPoolConfiguration configuration =
        ConnectionPoolConfiguration.builder(ConnectionFactories.get(connOptionsBuilder.build()))
            .initialSize(r2dbcProperties.getPool().getInitialSize())
            .maxSize(r2dbcProperties.getPool().getMaxSize())
            .maxIdleTime(r2dbcProperties.getPool().getMaxIdleTime())
            .maxCreateConnectionTime(r2dbcProperties.getPool().getMaxCreateConnectionTime())
            .maxAcquireTime(r2dbcProperties.getPool().getMaxAcquireTime())
            .maxLifeTime(r2dbcProperties.getPool().getMaxLifeTime())
            .validationDepth(r2dbcProperties.getPool().getValidationDepth())
            .validationQuery(r2dbcProperties.getPool().getValidationQuery())
            .build();

    return new ConnectionPool(configuration);
  }

  @Bean
  public ReactiveTransactionManager reactiveTransactionManager(
      final ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }
}
