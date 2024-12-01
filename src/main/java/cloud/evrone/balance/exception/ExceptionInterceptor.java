package cloud.evrone.balance.exception;


import cloud.evrone.balance.model.error.ErrorCode;
import cloud.evrone.balance.model.error.ErrorInfo;
import cloud.evrone.balance.model.error.ErrorInfoList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionInterceptor {

  @ExceptionHandler(ServerWebInputException.class)
  public ResponseEntity<ErrorInfoList> handleException(ServerWebInputException exception) {
    log.error("Error {}", exception.getMessage(), exception);
    ErrorInfoList errorInfoList = builtErrorInfoMessage(exception.getMessage());
    return new ResponseEntity<>(errorInfoList, exception.getStatus());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus
  @ResponseBody
  public ErrorInfoList handleException(Exception exception) {
    log.error("Error {}", exception.getMessage(), exception);
    return builtErrorInfoMessage(exception.getMessage());
  }

  // TODO: Correct according to the business requirements
  private ErrorInfoList builtErrorInfoMessage(String title) {
    ErrorInfo errorInfo = ErrorInfo.builder()
        .code(ErrorCode.UNKNOWN)
        .title(title)
        .detail(null)
        .build();
    return ErrorInfoList.builder()
        .errors(List.of(errorInfo))
        .build();
  }
}

