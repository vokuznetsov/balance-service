package cloud.evrone.balance.generator;

import static cloud.evrone.balance.utils.Utils.roundDown;

import cloud.evrone.balance.model.entity.TransactionEntity;
import cloud.evrone.balance.model.entity.TransactionType;
import java.util.concurrent.ThreadLocalRandom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionGenerator {

  public static TransactionEntity transactionEntity(long accountId) {
    double amount = roundDown(ThreadLocalRandom.current().nextDouble(50.0, 10000.0), 2);
    return TransactionEntity.builder()
        .id(null)
        .accountId(accountId)
        .amount(amount)
        .type(TransactionType.DEPOSIT.getCode())
        .build();
  }

}
