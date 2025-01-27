package springgradle.bankingproject.features.accountType;
import org.springframework.data.domain.Sort;
import springgradle.bankingproject.features.accountType.dto.AccountTypeRequest;
import springgradle.bankingproject.features.accountType.dto.AccountTypeResponse;
import springgradle.bankingproject.features.accountType.dto.AccountTypeUpdateRequest;
import springgradle.bankingproject.model.Account;

import java.util.List;

public interface AccountTypeService {
    AccountTypeResponse createAccountType(AccountTypeRequest accountTypeRequest);
    AccountTypeResponse updateAccountType(String alias, AccountTypeUpdateRequest accountTypeUpdateRequestccountTypeUpdateRequest);
    List<AccountTypeResponse> findList();
    void deleteAccountTypeByAlias(String alias);
    AccountTypeResponse getAccountTypeByAlias(String alias);
}
