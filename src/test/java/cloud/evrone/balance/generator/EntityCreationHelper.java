package cloud.evrone.balance.generator;

import static cloud.evrone.balance.generator.AccountGenerator.accountEntity;

import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityCreationHelper {

  @Autowired
  private AccountRepository accountRepository;

  // ------------------- Account -------------------

  public AccountEntity createAccount() {
    return accountRepository.save(accountEntity()).block();
  }


}
