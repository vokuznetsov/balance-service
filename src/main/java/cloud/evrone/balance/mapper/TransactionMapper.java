package cloud.evrone.balance.mapper;

import cloud.evrone.balance.model.entity.TransactionEntity;
import cloud.evrone.balance.model.entity.TransactionType;
import cloud.evrone.balance.model.openapi.TransactionModel;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

  public TransactionModel toModel(TransactionEntity entity) {
    if (entity == null) {
      return null;
    }
    return TransactionModel.builder()
        .id(entity.getId())
        .accountId(entity.getAccountId())
        .amount(entity.getAmount())
        .type(TransactionType.fromCode(entity.getType()))
        .createdAt(entity.getCreatedAt())
        .build();
  }

}
