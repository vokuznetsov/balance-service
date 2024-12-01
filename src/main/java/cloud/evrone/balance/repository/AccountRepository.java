package cloud.evrone.balance.repository;

import cloud.evrone.balance.model.entity.AccountEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Long> {

  @Query("SELECT * FROM balance_accounts WHERE id = :accountId FOR UPDATE")
  Mono<AccountEntity> findByIdForUpdate(Long accountId);
}
