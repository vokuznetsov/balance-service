package cloud.evrone.balance.service;

import cloud.evrone.balance.model.Account;
import cloud.evrone.balance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  //private final TransactionRepository transactionRepository;

  public Mono<Account> createAccount(Account account) {
    return accountRepository.save(account);
  }


  /*public Mono<Account> deposit(Long accountId, BigDecimal amount) {
    return accountRepository.updateBalance(accountId, amount)
        .flatMap(account -> transactionRepository.save(
            new Transaction(null, accountId, amount, "DEPOSIT", LocalDateTime.now())
        ).thenReturn(account));
  }

  public Mono<Account> withdraw(Long accountId, BigDecimal amount) {
    return accountRepository.findById(accountId)
        .filter(account -> account.getBalance().compareTo(amount) >= 0)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Insufficient balance")))
        .flatMap(account -> accountRepository.updateBalance(accountId, amount.negate())
            .flatMap(updatedAccount -> transactionRepository.save(
                new Transaction(null, accountId, amount.negate(), "WITHDRAW", LocalDateTime.now())
            ).thenReturn(updatedAccount)));
  }

  public Mono<Account> transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
    return withdraw(fromAccountId, amount)
        .then(deposit(toAccountId, amount));
  }

  public Mono<Account> getBalance(Long accountId) {
    return accountRepository.findById(accountId);
  }

  public Flux<Transaction> getTransactions(Long accountId, LocalDateTime start, LocalDateTime end) {
    return transactionRepository.findByAccountIdAndTimestampBetween(accountId, start, end);
  }*/
}
