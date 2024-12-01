package cloud.evrone.balance.repository;

import cloud.evrone.balance.model.Account;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

  @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :accountId RETURNING *")
  Mono<Account> updateBalance(Long accountId, Long amount);
}
