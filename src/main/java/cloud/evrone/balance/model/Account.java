package cloud.evrone.balance.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("balance_accounts")
@Builder(toBuilder = true)
public class Account {

  @Id
  private Long id;
  private Long balance;
  private String owner; // Владелец счета
}
