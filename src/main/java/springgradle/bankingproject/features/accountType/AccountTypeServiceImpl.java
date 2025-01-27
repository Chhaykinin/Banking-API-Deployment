package springgradle.bankingproject.features.accountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springgradle.bankingproject.features.account.dto.AccountResponse;
import springgradle.bankingproject.features.accountType.dto.AccountTypeRequest;
import springgradle.bankingproject.features.accountType.dto.AccountTypeResponse;
import springgradle.bankingproject.features.accountType.dto.AccountTypeUpdateRequest;
import springgradle.bankingproject.mapper.AccountTypeMapper;
import springgradle.bankingproject.model.AccountType;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j //for log
public class AccountTypeServiceImpl implements AccountTypeService {


    private final AccountTypeRepository accountTypeRepository;
    private final AccountTypeMapper accountTypeMapper;
    @Override
    public AccountTypeResponse createAccountType(AccountTypeRequest accountTypeRequest) {
        if(accountTypeRepository.existsByAlias(accountTypeRequest.alias())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account type alias has already exists");
        }
        AccountType accountType = accountTypeMapper.fromAccountTypeRequest(accountTypeRequest);
        accountType.setIsDeleted(false);
        accountTypeRepository.save(accountType);
        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

    @Override
    public AccountTypeResponse updateAccountType(String alias, AccountTypeUpdateRequest accountTypeUpdateRequest) {
        AccountType accountType = accountTypeRepository.findByAlias(alias).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account type with alias " + alias + " not found")
        );
        log.info("Before map: description: {},status: {}, alias: {}" ,accountType.getDescription(), accountType.getIsDeleted(),accountType.getAlias());
        accountTypeMapper.fromAccountTypeUpdateRequest(accountTypeUpdateRequest,accountType);
        log.info("After map: description: {},status: {}, alias: {} " ,accountType.getDescription(), accountType.getIsDeleted(),accountType.getAlias());
        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

    @Override
    public List<AccountTypeResponse> findList() {
        Sort sortById= Sort.by(Sort.Direction.DESC, "id");
        List<AccountType> accountTypes = accountTypeRepository.findAll(sortById);
        return accountTypeMapper.toAccountTypeResponseList(accountTypes);
    }

    @Override
    public void deleteAccountTypeByAlias(String alias) {
        AccountType accountType=accountTypeRepository.findByAlias(alias).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account type with alias " + alias + "has been not found"));
        accountTypeRepository.delete(accountType);
    }

    @Override
    public AccountTypeResponse getAccountTypeByAlias(String alias) {
        AccountType accountType= accountTypeRepository.findByAlias(alias).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account type with alias " + alias + " has been not found"));
        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

}
