package springgradle.bankingproject.features.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springgradle.bankingproject.features.account.dto.AccountRequest;
import springgradle.bankingproject.features.account.dto.AccountResponse;
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

}
