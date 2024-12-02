package cloud.evrone.balance.model.entity;

import lombok.Getter;

@Getter
public enum TransactionType {

  DEPOSIT(1),
  WITHDRAW(2);

  private final int code;

  TransactionType(int code) {
    this.code = code;
  }

  public static TransactionType fromCode(int code) {
    return switch (code) {
      case 1 -> DEPOSIT;
      case 2 -> WITHDRAW;
      default -> null;
    };
  }

}
