package springgradle.bankingproject.features.account;

import org.springframework.stereotype.Service;
import springgradle.bankingproject.features.account.dto.AccountRequest;
import springgradle.bankingproject.features.account.dto.AccountResponse;

import java.util.List;

public interface AccountService {
    /**
     * create a new acc
     * @param accountRequest {@link AccountRequest}
     * @return {@link AccountResponse}
     * */
    AccountResponse createNew(AccountRequest accountRequest);
    /**
     * Find account by account no
     * @return {@link List<AccountResponse>}
     * **/
    List<AccountResponse> findList();

    /**
     * Find account by account no
     * @return {@link AccountResponse
     **/
    AccountResponse findByActNo(String actNo);
}
