package cloud.evrone.balance.model.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("balance_accounts")
@Builder(toBuilder = true)
public class AccountEntity {

  @Id
  private Long id;
  private String name;
  private Double balance;
  private Boolean deleted;
  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime updatedAt;

}
