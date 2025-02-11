package springgradle.bankingproject.features.account;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import springgradle.bankingproject.features.account.dto.AccountRenameRequest;
import springgradle.bankingproject.features.account.dto.AccountRequest;
import springgradle.bankingproject.features.account.dto.AccountResponse;
import springgradle.bankingproject.features.account.dto.AccountTransferLimitRequest;

import java.util.List;

public interface AccountService {
    /**
     * create a new acc
     * @param accountRequest {@link AccountRequest}
     * @return {@link AccountResponse}
     * */
    AccountResponse createNew(AccountRequest accountRequest);
    /**
     * Find all account by pagination
     * @param pageNumber is current page request from client
     * @param pageSize is size page request from client
     * @return {@link List<AccountResponse>}
     * **/
    Page<AccountResponse> findList(int pageNumber, int pageSize);

    /**
     * Find account by account no
     * @return {@link AccountResponse
     **/
    AccountResponse findByActNo(String actNo);
    AccountResponse renameAccount(String actNo,AccountRenameRequest accountRenameRequest);

    void hideAccount(String actNo);

    void transferLimit(String actNo, AccountTransferLimitRequest accountTransferLimitRequest);
}
