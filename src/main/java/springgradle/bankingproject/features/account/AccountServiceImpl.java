package springgradle.bankingproject.features.account;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springgradle.bankingproject.features.account.dto.AccountRenameRequest;
import springgradle.bankingproject.features.account.dto.AccountRequest;
import springgradle.bankingproject.features.account.dto.AccountResponse;
import springgradle.bankingproject.features.account.dto.AccountTransferLimitRequest;
import springgradle.bankingproject.features.accountType.AccountTypeRepository;
import springgradle.bankingproject.features.user.UserRepository;
import springgradle.bankingproject.mapper.AccountMapper;
import springgradle.bankingproject.model.Account;
import springgradle.bankingproject.model.AccountType;
import springgradle.bankingproject.model.User;

import java.math.BigDecimal;
import java.util.List;
@Builder
@Service
@RequiredArgsConstructor// create private acc
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;  //interface not impl
    @Override
    public AccountResponse createNew(AccountRequest accountRequest) {
        // validation acc type
        AccountType accountType= accountTypeRepository.
        findByAlias(accountRequest.accountTypeAlias())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account type not found"
                ));
        //validation user id
        User user = userRepository.
                findByUuid(accountRequest.userUuid())
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
        // validation acc no
        if(accountRepository.existsByActNo(accountRequest.actNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account already exists");
        }
        if (accountRequest.balance().compareTo(BigDecimal.valueOf(10))< 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Balance 10$ is required to creat account");
        }
        // Transfer dto to domain
        // when write by yourself
//        Account account = new Account();
//        account.setActNo(accountRequest.actNo());
//        account.setBalance(accountRequest.balance());
//
        // write by lombok
        Account account = accountMapper.fromAccountRequest(accountRequest);
        account.setAccountType(accountType);
        account.setUser(user);

        //system generate data
        account.setActName(user.getName());
        account.setIsHidden(false);
        account.setTransferLimit(BigDecimal.valueOf(1000));
        //save to database
        accountRepository.save(account);

        // write by yourself
        // Transfer Domain Model to Dto
//        return AccountResponse.builder()
//                .alias(account.getAlias())
//                .actName(account.getActName())
//                .actNo(account.getActNo())
//                .balance(account.getBalance())
//                .accountTypeAlias(account.getAccountType().getName())
//                .build();
        //write by lombok
        return accountMapper.toAccountResponse(account);// lombok and mapstruct write auto
    }
    @Override
    public Page<AccountResponse> findList(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);
        Page<Account> accounts = accountRepository.findAll(pageRequest);
        return accounts.map(accountMapper::toAccountResponse);// account to account response
    }
    @Override
    public AccountResponse findByActNo(String actNo) {
        //validate
        Account account = accountRepository.findByActNo(actNo).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account no have been not found"));
        return accountMapper.toAccountResponse(account);
    }
    @Override
    public AccountResponse renameAccount(String actNo, AccountRenameRequest accountRenameRequest) {
        //validate
        Account account = accountRepository.findByActNo(actNo).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account no have been not found"));
        account.setAlias(accountRenameRequest.alias());
        account=accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public void hideAccount(String actNo) {
        Account account=accountRepository.findByActNo(actNo).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,  "Account no have been not found")
        );
        account.setIsHidden(true);
        accountRepository.save(account);
    }

    @Override
    public void transferLimit(String actNo, AccountTransferLimitRequest accountTransferLimitRequest) {
        //validate
        Account account = accountRepository.findByActNo(actNo).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account no have been not found"));
        account.setTransferLimit(accountTransferLimitRequest.amount());
        accountRepository.save(account);
    }

}
