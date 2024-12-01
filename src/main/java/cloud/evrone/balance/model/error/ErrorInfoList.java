package cloud.evrone.balance.model.error;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ErrorInfoList {

  private List<ErrorInfo> errors;

}
