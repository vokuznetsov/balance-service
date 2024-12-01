package cloud.evrone.balance.controller;

import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import cloud.evrone.balance.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations related to accounts")
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  @Operation(summary = "Create a new account", description = "Creates a new account with the provided details.")
  public Mono<ResponseEntity<AccountModel>> createAccount(
      @RequestBody @Valid Mono<CreateAccountModel> account) {
    return account.flatMap(accountService::createAccount)
        .map(ResponseEntity::ok);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing account", description = "Updates an account by its ID.")
  public Mono<ResponseEntity<AccountModel>> updateAccount(@PathVariable Long id,
      @RequestBody @Valid Mono<UpdateAccountModel> account) {
    return account.flatMap(it -> accountService.updateAccount(id, it))
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /*@PostMapping("/{accountId}/deposit")
  public Mono<Account> deposit(@PathVariable Long accountId, @RequestParam Long amount) {
    return accountService.deposit(accountId, amount);
  }

  @PostMapping("/{accountId}/withdraw")
  public Mono<Account> withdraw(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
    return accountService.withdraw(accountId, amount);
  }

  @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
  public Mono<Void> transfer(@PathVariable Long fromAccountId, @PathVariable Long toAccountId,
      @RequestParam BigDecimal amount) {
    return accountService.transfer(fromAccountId, toAccountId, amount).then();
  }

  @GetMapping("/{accountId}/balance")
  public Mono<Account> getBalance(@PathVariable Long accountId) {
    return accountService.getBalance(accountId);
  }

  @GetMapping("/{accountId}/transactions")
  public Flux<Transaction> getTransactions(@PathVariable Long accountId,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end) {
    return accountService.getTransactions(accountId, start, end);
  }*/
}
