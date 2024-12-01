package cloud.evrone.balance.controller;

import static cloud.evrone.balance.generator.AccountGenerator.openApiCreateAccountModel;
import static cloud.evrone.balance.generator.AccountGenerator.openApiUpdateAccountModel;
import static cloud.evrone.balance.utils.WebClientUtils.makePostRequest;
import static cloud.evrone.balance.utils.WebClientUtils.makePutRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cloud.evrone.balance.AbstractInitializerTest;
import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import org.junit.jupiter.api.Test;


public class AccountControllerTest extends AbstractInitializerTest {

  private static final String ACCOUNT_URI = "/api/v1/accounts";

  @Test
  void createAccount() {
    CreateAccountModel postBody = openApiCreateAccountModel();

    AccountModel openApiResponse = makePostRequest(
        uriBuilder -> uriBuilder.path(ACCOUNT_URI).build(),
        client, postBody, CreateAccountModel.class, AccountModel.class
    );

    assertNotNull(openApiResponse);
    assertAccount(postBody, openApiResponse);
  }

  @Test
  void updateAccount() {
    assertRowsInTable(0, accountRepository);

    AccountEntity account = creationHelper.createAccount();
    assertRowsInTable(1, accountRepository);

    UpdateAccountModel putBody = openApiUpdateAccountModel();

    AccountModel openApiResponse = makePutRequest(
        uriBuilder -> uriBuilder.path(ACCOUNT_URI + "/{id}").build(account.getId()),
        client, putBody, UpdateAccountModel.class, AccountModel.class
    );

    assertNotNull(openApiResponse);
    assertAccount(putBody, account, openApiResponse);
  }

  private void assertAccount(CreateAccountModel expected, AccountModel actual) {
    assertTrue(actual.getId() > 0);
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getBalance(), actual.getBalance());
    assertFalse(actual.getDeleted());
  }

  private void assertAccount(UpdateAccountModel expected, AccountEntity before,
      AccountModel actual) {
    assertEquals(before.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(before.getBalance(), actual.getBalance());
    assertEquals(expected.getDeleted(), actual.getDeleted());
  }
}
