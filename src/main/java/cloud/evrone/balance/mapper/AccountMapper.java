package cloud.evrone.balance.mapper;

import static cloud.evrone.balance.utils.ConsumerUtils.acceptIfNotNull;

import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

  public AccountEntity toEntity(CreateAccountModel model) {
    if (model == null) {
      return null;
    }
    return AccountEntity.builder()
        .name(model.getName())
        .balance(model.getBalance())
        .build();
  }

  public AccountEntity toEntity(AccountEntity existed, UpdateAccountModel model) {
    var builder = existed.toBuilder();

    acceptIfNotNull(model.getName(), builder::name);
    acceptIfNotNull(model.getDeleted(), builder::deleted);

    return builder.build();
  }

  public AccountModel toModel(AccountEntity entity) {
    if (entity == null) {
      return null;
    }
    return AccountModel.builder()
        .id(entity.getId())
        .name(entity.getName())
        .balance(entity.getBalance())
        .deleted(entity.getDeleted())
        .build();
  }
}
