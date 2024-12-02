package cloud.evrone.balance.repository;

import cloud.evrone.balance.model.entity.TransactionEntity;
import java.time.LocalDateTime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<TransactionEntity, Long> {

  Flux<TransactionEntity> findAllByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime start,
      LocalDateTime end);
}
