package cloud.evrone.balance.controller;

import static cloud.evrone.balance.utils.WebClientUtils.makeGetRequest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cloud.evrone.balance.AbstractInitializerTest;
import cloud.evrone.balance.model.Account;
import org.junit.jupiter.api.Test;


public class AccountControllerTest extends AbstractInitializerTest {

  private static final String ACCOUNT_URI = "/api/v1/accounts";

  @Test
  void testMethod() {
    var openApiResponse = makeGetRequest(uriBuilder ->
            uriBuilder
                .path(ACCOUNT_URI + "/test")
                .build(),
        client,
        Account.class
    );

    assertNotNull(openApiResponse);
  }
}
