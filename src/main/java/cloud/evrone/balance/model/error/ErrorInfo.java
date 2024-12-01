package cloud.evrone.balance.model.error;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ErrorInfo {

  private ErrorCode code;
  private String title;
  private Map<String, Object> detail = null;

}
