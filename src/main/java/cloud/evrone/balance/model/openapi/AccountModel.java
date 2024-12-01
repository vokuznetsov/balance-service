package cloud.evrone.balance.model.openapi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AccountModel {

  private Long id;
  private String name;
  private Long balance;
  private Boolean deleted;

}
