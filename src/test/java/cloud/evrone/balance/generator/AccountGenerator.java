package cloud.evrone.balance.generator;

import static cloud.evrone.balance.utils.Utils.nextLongId;

import cloud.evrone.balance.model.entity.AccountEntity;
import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountGenerator {

  public static AccountModel openApiAccountModel() {
    return AccountModel.builder()
        .id(nextLongId())
        .name("name-1")
        .balance(100L)
        .deleted(false)
        .build();
  }

  public static AccountEntity accountEntity() {
    AccountModel model = openApiAccountModel();
    return AccountEntity.builder()
        .id(null)
        .name(model.getName())
        .balance(model.getBalance())
        .deleted(model.getDeleted())
        .build();
  }

  public static CreateAccountModel openApiCreateAccountModel() {
    AccountModel model = openApiAccountModel();
    return CreateAccountModel.builder()
        .name(model.getName())
        .balance(model.getBalance())
        .build();
  }

  public static UpdateAccountModel openApiUpdateAccountModel() {
    AccountModel model = openApiAccountModel();
    return UpdateAccountModel.builder()
        .name("new-name-1")
        .deleted(!model.getDeleted())
        .build();
  }

}
