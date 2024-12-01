package cloud.evrone.balance.model.openapi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CreateAccountModel {

  private String name;
  private Long balance;

}
