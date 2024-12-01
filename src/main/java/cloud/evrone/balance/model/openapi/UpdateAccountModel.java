package cloud.evrone.balance.model.openapi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UpdateAccountModel {

  private String name;
  private Boolean deleted;

}
