package springgradle.bankingproject.features.account.dto;

import lombok.Builder;
import springgradle.bankingproject.features.accountType.dto.AccountTypeResponse;

import java.math.BigDecimal;
@Builder
public record AccountResponse(
        String alias,
        String actName,
        String actNo,
        BigDecimal balance,
        AccountTypeResponse accountType
) {
}
