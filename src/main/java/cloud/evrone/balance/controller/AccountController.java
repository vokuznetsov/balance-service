package cloud.evrone.balance.controller;

import cloud.evrone.balance.model.Account;
import cloud.evrone.balance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping("/test")
  public Mono<Account> testMethod() {
    Account account = Account.builder().balance(10L).owner("some-owner").build();

    return accountService.createAccount(account);
  }

  /*@PostMapping("/{accountId}/deposit")
  public Mono<Account> deposit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
    return accountService.deposit(accountId, amount);
  }

  @PostMapping("/{accountId}/withdraw")
  public Mono<Account> withdraw(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
    return accountService.withdraw(accountId, amount);
  }

  @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
  public Mono<Void> transfer(@PathVariable Long fromAccountId, @PathVariable Long toAccountId, @RequestParam BigDecimal amount) {
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
