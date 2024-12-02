package cloud.evrone.balance.generator;

import static cloud.evrone.balance.generator.AccountGenerator.accountEntity;
import static cloud.evrone.balance.generator.TransactionGenerator.transactionEntity;

import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.model.entity.TransactionEntity;
import cloud.evrone.balance.repository.AccountRepository;
import cloud.evrone.balance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityCreationHelper {

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private TransactionRepository transactionRepository;

  // ------------------- Account -------------------

  public AccountEntity createAccount() {
    return accountRepository.save(accountEntity()).block();
  }

  public TransactionEntity createTransaction() {
    AccountEntity account = createAccount();
    return createTransaction(account.getId());
  }

  public TransactionEntity createTransaction(long accountId) {
    return transactionRepository.save(transactionEntity(accountId)).block();
  }

}
