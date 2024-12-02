package cloud.evrone.balance.controller;

import static cloud.evrone.balance.WebClientUtils.makeGetRequest;
import static cloud.evrone.balance.WebClientUtils.makePostRequest;
import static cloud.evrone.balance.WebClientUtils.makePutRequest;
import static cloud.evrone.balance.generator.AccountGenerator.openApiCreateAccountModel;
import static cloud.evrone.balance.generator.AccountGenerator.openApiUpdateAccountModel;
import static cloud.evrone.balance.utils.Utils.roundDown;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cloud.evrone.balance.AbstractInitializerTest;
import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.model.entity.TransactionEntity;
import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.TransactionModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import reactor.test.StepVerifier;


public class AccountControllerTest extends AbstractInitializerTest {

  private static final String ACCOUNT_URI = "/api/v1/accounts";

  @Test
  void createAccount() {
    assertRowsInTable(0, accountRepository);
    CreateAccountModel postBody = openApiCreateAccountModel();

    AccountModel openApiResponse = makePostRequest(
        uriBuilder -> uriBuilder.path(ACCOUNT_URI).build(),
        client, postBody, CreateAccountModel.class, AccountModel.class
    );

    assertNotNull(openApiResponse);
    assertAccount(postBody, openApiResponse);
    assertRowsInTable(1, accountRepository);
  }

  @Test
  void getAccount() {
    assertRowsInTable(0, accountRepository);
    AccountEntity account = creationHelper.createAccount();
    assertRowsInTable(1, accountRepository);

    AccountModel openApiResponse = makeGetRequest(uriBuilder ->
            uriBuilder
                .path(ACCOUNT_URI + "/{id}")
                .build(account.getId()),
        client,
        new ParameterizedTypeReference<>() {
        }
    );

    assertNotNull(openApiResponse);
    assertAccount(account, openApiResponse);
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

  @Test
  void deposit() {
    AccountEntity account = creationHelper.createAccount();
    assertRowsInTable(1, accountRepository);

    double depositAmount = 50.9999;
    double roundAmount = roundDown(depositAmount, 2);

    AccountModel response = makePutRequest(
        uriBuilder -> uriBuilder.path(ACCOUNT_URI + "/{id}/deposit").build(account.getId()),
        client, depositAmount, Double.class, AccountModel.class
    );

    assertNotNull(response);
    assertEquals(account.getId(), response.getId());
    assertEquals(account.getBalance() + roundAmount, response.getBalance());
  }

  @Test
  void withdraw() {
    AccountEntity account = creationHelper.createAccount();
    assertRowsInTable(1, accountRepository);

    double withdrawAmount = 30.5599;
    double roundAmount = roundDown(withdrawAmount, 2);

    AccountModel response = makePutRequest(
        uriBuilder -> uriBuilder.path(ACCOUNT_URI + "/{id}/withdraw").build(account.getId()),
        client, withdrawAmount, Double.class, AccountModel.class
    );

    assertNotNull(response);
    assertEquals(account.getId(), response.getId());
    assertEquals(account.getBalance() - roundAmount, response.getBalance());
  }

  @Test
  void transfer() {
    AccountEntity fromAccount = creationHelper.createAccount();
    AccountEntity toAccount = creationHelper.createAccount();
    assertRowsInTable(2, accountRepository);

    Double transferAmount = 20.1323;
    double roundAmount = roundDown(transferAmount, 2);

    AccountModel response = makePutRequest(
        uriBuilder -> uriBuilder.path(ACCOUNT_URI + "/{fromId}/transfer/{toId}")
            .build(fromAccount.getId(), toAccount.getId()),
        client, transferAmount, Double.class, AccountModel.class
    );

    assertNotNull(response);
    assertEquals(fromAccount.getId(), response.getId());
    assertEquals(fromAccount.getBalance() - roundAmount, response.getBalance());

    StepVerifier.create(accountRepository.findById(toAccount.getId()))
        .assertNext(updatedToAccount -> {
          assertNotNull(updatedToAccount);
          assertEquals(toAccount.getBalance() + roundAmount, updatedToAccount.getBalance());
        })
        .expectComplete()
        .verify(Duration.ofSeconds(10));
  }

  @Test
  void getStatement() {
    assertRowsInTable(0, transactionRepository);

    AccountEntity account = creationHelper.createAccount();

    List<TransactionEntity> entities = Stream.of(
            creationHelper.createTransaction(account.getId()),
            creationHelper.createTransaction(account.getId())
        )
        .sorted(Comparator.comparing(TransactionEntity::getId))
        .toList();

    assertRowsInTable(2, transactionRepository);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    String formattedStart = LocalDateTime.now().minusDays(1).format(formatter);
    String formattedEnd = LocalDateTime.now().plusDays(1).format(formatter);

    List<TransactionModel> response = makeGetRequest(
        uriBuilder -> uriBuilder.path(ACCOUNT_URI + "/{id}/statement")
            .queryParam("start", formattedStart)
            .queryParam("end", formattedEnd)
            .build(account.getId()),
        client,
        new ParameterizedTypeReference<>() {
        }
    );

    assertNotNull(response);

    List<TransactionModel> models = response.stream()
        .sorted(Comparator.comparing(TransactionModel::getId))
        .toList();
    assertEquals(entities.size(), models.size());

    IntStream.range(0, entities.size())
        .forEach(i -> assertTransaction(entities.get(i), models.get(i)));
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

  private void assertAccount(AccountEntity expected, AccountModel actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getBalance(), actual.getBalance());
    assertEquals(expected.getDeleted(), actual.getDeleted());
  }

  private void assertTransaction(TransactionEntity expected, TransactionModel actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getAccountId(), actual.getAccountId());
    assertEquals(expected.getAmount(), actual.getAmount());
    assertEquals(expected.getType(), actual.getType().getCode());
    assertTrue(LocalDateTime.now().minusMinutes(10).isBefore(actual.getCreatedAt()));
  }
}
