package springgradle.bankingproject.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import springgradle.bankingproject.features.accountType.dto.AccountTypeRequest;
import springgradle.bankingproject.features.accountType.dto.AccountTypeResponse;
import springgradle.bankingproject.features.accountType.dto.AccountTypeUpdateRequest;
import springgradle.bankingproject.model.AccountType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {
    // partially map for update all, and do not null data field on table
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromAccountTypeUpdateRequest(AccountTypeUpdateRequest accountTypeUpdateRequest,@MappingTarget AccountType accountType);
    AccountType fromAccountTypeUpdateRequest(AccountTypeUpdateRequest accountTypeUpdateRequest);
    AccountTypeResponse toAccountTypeResponse(AccountType accountType);
    AccountType fromAccountTypeRequest(AccountTypeRequest accountTypeRequest);
    List<AccountTypeResponse> toAccountTypeResponseList(List<AccountType> accountTypes);
}
