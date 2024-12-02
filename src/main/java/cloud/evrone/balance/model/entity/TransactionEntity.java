package cloud.evrone.balance.model.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("balance_transactions")
@Builder(toBuilder = true)
public class TransactionEntity {

  @Id
  private Long id;
  private Long accountId;
  private Double amount;
  private Integer type;
  private LocalDateTime createdAt;
}
