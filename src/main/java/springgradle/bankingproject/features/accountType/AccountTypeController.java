package springgradle.bankingproject.features.accountType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springgradle.bankingproject.features.accountType.dto.AccountTypeRequest;
import springgradle.bankingproject.features.accountType.dto.AccountTypeResponse;
import springgradle.bankingproject.features.accountType.dto.AccountTypeUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accountTypes")
@RequiredArgsConstructor
public class AccountTypeController {
    private final AccountTypeService accountTypeService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    AccountTypeResponse createAccountType(@Valid @RequestBody AccountTypeRequest accountTypeRequest) {
        return accountTypeService.createAccountType(accountTypeRequest);
    }
    @PatchMapping("/alias")
    AccountTypeResponse updateAccountType(String alias,@Valid @RequestBody AccountTypeUpdateRequest accountTypeUpdateRequest) {
        return accountTypeService.updateAccountType(alias,accountTypeUpdateRequest);
    }
    @GetMapping
    List<AccountTypeResponse> findList(){
        return accountTypeService.findList();
    }
    @DeleteMapping("/{alias}")
    void deleteAccountTypeByAlias(@PathVariable("alias") String alias) {
        accountTypeService.deleteAccountTypeByAlias(alias);
    }
    @GetMapping("/{alias}")
    AccountTypeResponse getAccountTypeByAlias(@PathVariable("alias") String alias) {
        return accountTypeService.getAccountTypeByAlias(alias);
    }
}
