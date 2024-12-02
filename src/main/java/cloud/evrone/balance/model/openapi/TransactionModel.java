package cloud.evrone.balance.model.openapi;

import cloud.evrone.balance.model.entity.TransactionType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TransactionModel {

  private Long id;
  private Long accountId;
  private Double amount;
  private TransactionType type;
  private LocalDateTime createdAt;

}
