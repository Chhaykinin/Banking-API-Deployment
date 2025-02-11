package springgradle.bankingproject.features.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springgradle.bankingproject.features.account.dto.AccountRenameRequest;
import springgradle.bankingproject.features.account.dto.AccountRequest;
import springgradle.bankingproject.features.account.dto.AccountResponse;
import springgradle.bankingproject.features.account.dto.AccountTransferLimitRequest;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    AccountResponse createNew(@Valid @RequestBody AccountRequest accountRequest) {
        return accountService.createNew(accountRequest);
    }
    @GetMapping
    Page<AccountResponse> findList(@RequestParam(required = false,defaultValue = "0") int pageNumber,
                                   @RequestParam(required = false ,defaultValue = "10") int pageSize) {
        return accountService.findList(pageNumber, pageSize);
    }
    @GetMapping("/{actNo}")
    AccountResponse findByActNo(@PathVariable("actNo") String actNo) {
        return accountService.findByActNo(actNo);
    }
    @PutMapping("/{actNo}/rename")
    AccountResponse renameAccount(@PathVariable("actNo") String actNo,
                                  @Valid @RequestBody AccountRenameRequest  accountRenameRequest) {
        return accountService.renameAccount(actNo, accountRenameRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{actNo}/hided")
    void hideAccount(@PathVariable("actNo") String actNo) {
         accountService.hideAccount(actNo);
    }
    @PutMapping("/{actNo}/transfer-limit")
    void transferLimit(@PathVariable("actNo") String actNo, AccountTransferLimitRequest accountTransferLimitRequest) {
        accountService.transferLimit(actNo,accountTransferLimitRequest);

    }

}
