package springgradle.bankingproject.mapper;

import org.mapstruct.Mapper;
import springgradle.bankingproject.features.account.dto.AccountRequest;
import springgradle.bankingproject.features.account.dto.AccountResponse;
import springgradle.bankingproject.model.Account;
@Mapper(componentModel = "spring")
public interface AccountMapper {
    // map acc to accResponse
    // source = account
    // target = accountResponse
    AccountResponse toAccountResponse(Account account);
    Account fromAccountRequest(AccountRequest accountRequest);
}
