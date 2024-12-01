package cloud.evrone.balance.test.container;

import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class PostgresqlContainer extends PostgreSQLContainer<PostgresqlContainer> {

  private static final Network NETWORK = Network.newNetwork();
  private static final String IMAGE = "postgres:16";

  public PostgresqlContainer() {
    this(IMAGE);
  }

  public PostgresqlContainer(String image) {
    super(image);
    this.withNetwork(NETWORK)
        .withExposedPorts(5432)
        .waitingFor(Wait.defaultWaitStrategy());
  }

}
