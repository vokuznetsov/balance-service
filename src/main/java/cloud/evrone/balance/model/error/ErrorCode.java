package cloud.evrone.balance.model.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {

  UNKNOWN("UNKNOWN"),
  INVALID_STRING_FORMAT("INVALID_STRING_FORMAT"),
  FIELD_VALIDATION("FIELD_VALIDATION"),
  INVALID_TYPE("INVALID_TYPE");

  private String value;

}
