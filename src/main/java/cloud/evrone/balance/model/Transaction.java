package cloud.evrone.balance.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("transactions")
@Builder(toBuilder = true)
public class Transaction {

  @Id
  private Long id;
  private Long accountId;
  private BigDecimal amount;
  private String type; // DEPOSIT, WITHDRAW, TRANSFER
  private LocalDateTime timestamp;
}
