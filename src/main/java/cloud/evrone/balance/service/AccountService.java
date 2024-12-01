package cloud.evrone.balance.service;

import cloud.evrone.balance.annotation.ReactiveTransactional;
import cloud.evrone.balance.mapper.AccountMapper;
import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import cloud.evrone.balance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountMapper accountMapper;
  private final AccountRepository accountRepository;
  //private final TransactionRepository transactionRepository;

  @ReactiveTransactional
  public Mono<AccountModel> createAccount(CreateAccountModel account) {
    AccountEntity entity = accountMapper.toEntity(account);
    if (ObjectUtils.isEmpty(entity)) {
      return Mono.error(new IllegalArgumentException("Account cannot be empty."));
    }

    // TODO: accountRepository.findById(...) is necessary to return correct row from DB.
    //  Possibly we can do it using single SQL INSERT.
    //  For future optimization.
    //  The same situation for update(..) method.
    return accountRepository.save(entity)
        .flatMap(e -> accountRepository.findById(e.getId()))
        .map(accountMapper::toModel);
  }

  @ReactiveTransactional
  public Mono<AccountModel> updateAccount(Long accountId, UpdateAccountModel account) {
    return accountRepository.findByIdForUpdate(accountId)
        .flatMap(existingAccount -> {
          AccountEntity entity = accountMapper.toEntity(existingAccount, account);
          return accountRepository.save(entity);
        })
        .flatMap(e -> accountRepository.findById(e.getId()))
        .map(accountMapper::toModel);
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
