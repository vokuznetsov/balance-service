package cloud.evrone.balance.service;

import static cloud.evrone.balance.utils.Utils.roundDown;

import cloud.evrone.balance.annotation.ReactiveTransactional;
import cloud.evrone.balance.mapper.AccountMapper;
import cloud.evrone.balance.mapper.TransactionMapper;
import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.model.entity.TransactionEntity;
import cloud.evrone.balance.model.entity.TransactionType;
import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.TransactionModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import cloud.evrone.balance.repository.AccountRepository;
import cloud.evrone.balance.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

// TODO: Almost all methods call accountRepository.findById(...) at the end.
//  It is necessary to return row with latest data from DB.
//  Possibly we can do it using single SQL. It will be for future optimization.

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountMapper accountMapper;
  private final TransactionMapper transactionMapper;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  @ReactiveTransactional
  public Mono<AccountModel> createAccount(CreateAccountModel account) {
    AccountEntity entity = accountMapper.toEntity(account);
    if (ObjectUtils.isEmpty(entity)) {
      return Mono.error(new IllegalArgumentException("Account cannot be empty."));
    }

    return accountRepository.save(entity)
        .flatMap(e -> accountRepository.findById(e.getId()))
        .map(accountMapper::toModel);
  }

  public Mono<AccountModel> getAccount(Long accountId) {
    return accountRepository.findById(accountId)
        .map(accountMapper::toModel)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Account not found")));
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

  @ReactiveTransactional
  public Mono<AccountModel> deposit(Long accountId, Double amount) {
    double roundAmount = roundDown(amount, 2);
    return accountRepository.findByIdForUpdate(accountId)
        .flatMap(account -> updateAccountBalance(account, roundAmount, TransactionType.DEPOSIT))
        .flatMap(e -> accountRepository.findById(e.getId()))
        .map(accountMapper::toModel);
  }

  @ReactiveTransactional
  public Mono<AccountModel> withdraw(Long accountId, Double amount) {
    double roundAmount = roundDown(amount, 2);
    return accountRepository.findByIdForUpdate(accountId)
        .flatMap(account -> {
          if (account.getBalance() < roundAmount) {
            return Mono.error(new IllegalStateException("Insufficient balance"));
          }

          return updateAccountBalance(account, -roundAmount, TransactionType.WITHDRAW);
        })
        .flatMap(transaction -> accountRepository.findById(accountId))
        .map(accountMapper::toModel);
  }

  @ReactiveTransactional
  public Mono<AccountModel> transfer(Long fromAccountId, Long toAccountId, Double amount) {
    // It is necessary to make SELECT .. FOR UPDATE for an account with smaller account id
    // in order to avoid deadlocks.
    Long firstAccountId = Math.min(fromAccountId, toAccountId);
    Long secondAccountId = Math.max(fromAccountId, toAccountId);
    double roundAmount = roundDown(amount, 2);

    return accountRepository.findByIdForUpdate(firstAccountId)
        .zipWith(accountRepository.findByIdForUpdate(secondAccountId))
        .flatMap(tuple -> {
          if (fromAccountId.equals(firstAccountId)) {
            return processTransfer(tuple.getT1(), tuple.getT2(), roundAmount);
          } else {
            return processTransfer(tuple.getT2(), tuple.getT1(), roundAmount);
          }
        });
  }

  private Mono<AccountModel> processTransfer(AccountEntity fromAccount, AccountEntity toAccount,
      Double amount) {
    if (fromAccount.getBalance() < amount) {
      return Mono.error(new IllegalStateException("Insufficient balance"));
    }

    return updateAccountBalance(fromAccount, -amount, TransactionType.WITHDRAW)
        .then(updateAccountBalance(toAccount, amount, TransactionType.DEPOSIT))
        .then(accountRepository.findById(fromAccount.getId()))
        .map(accountMapper::toModel);
  }

  private Mono<AccountEntity> updateAccountBalance(AccountEntity account, Double amount,
      TransactionType transactionType) {
    Double newBalance = account.getBalance() + amount;
    account.setBalance(newBalance);

    return accountRepository.save(account)
        .flatMap(savedAccount -> transactionRepository.save(
            TransactionEntity.builder()
                .accountId(account.getId())
                .type(transactionType.getCode())
                .amount(amount)
                .build()
        ).then(Mono.just(savedAccount)));
  }

  public Mono<List<TransactionModel>> getStatement(Long accountId, LocalDateTime start,
      LocalDateTime end) {
    return transactionRepository.findAllByAccountIdAndCreatedAtBetween(accountId, start, end)
        .map(transactionMapper::toModel)
        .collectList();
  }
}
