package cloud.evrone.balance.controller;

import cloud.evrone.balance.model.openapi.AccountModel;
import cloud.evrone.balance.model.openapi.CreateAccountModel;
import cloud.evrone.balance.model.openapi.TransactionModel;
import cloud.evrone.balance.model.openapi.UpdateAccountModel;
import cloud.evrone.balance.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations related to accounts")
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  @Operation(summary = "Create a new account", description = "Creates a new account with the provided details.")
  public Mono<ResponseEntity<AccountModel>> createAccount(
      @RequestBody @Valid Mono<CreateAccountModel> account) {
    return account.flatMap(accountService::createAccount)
        .map(ResponseEntity::ok);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get account.", description = "Gets the current balance of the account.")
  public Mono<ResponseEntity<AccountModel>> getAccount(@PathVariable Long id) {
    return accountService.getAccount(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing account", description = "Updates an account by its ID.")
  public Mono<ResponseEntity<AccountModel>> updateAccount(@PathVariable Long id,
      @RequestBody @Valid Mono<UpdateAccountModel> account) {
    return account.flatMap(it -> accountService.updateAccount(id, it))
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/deposit")
  @Operation(summary = "Deposit money", description = "Adds money to the account.")
  public Mono<ResponseEntity<AccountModel>> deposit(@PathVariable Long id,
      @RequestBody Double amount) {
    return accountService.deposit(id, amount)
        .map(ResponseEntity::ok);
  }

  @PutMapping("/{id}/withdraw")
  @Operation(summary = "Withdraw money", description = "Removes money from the account.")
  public Mono<ResponseEntity<AccountModel>> withdraw(@PathVariable Long id,
      @RequestBody Double amount) {
    return accountService.withdraw(id, amount)
        .map(ResponseEntity::ok);
  }

  @PutMapping("/{id}/transfer/{toId}")
  @Operation(summary = "Transfer money", description = "Transfers money between accounts.")
  public Mono<ResponseEntity<AccountModel>> transfer(@PathVariable Long id, @PathVariable Long toId,
      @RequestBody Double amount) {
    return accountService.transfer(id, toId, amount)
        .map(ResponseEntity::ok);
  }

  @GetMapping("/{id}/statement")
  @Operation(summary = "Get statement", description = "Gets the account statement for a period.")
  public Mono<ResponseEntity<List<TransactionModel>>> getStatement(@PathVariable Long id,
      @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
      @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
    return accountService.getStatement(id, start, end)
        .map(ResponseEntity::ok);
  }
}
