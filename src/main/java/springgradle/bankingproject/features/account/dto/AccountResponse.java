package springgradle.bankingproject.features.account.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record AccountResponse(
        String alias,
        String actName,
        String actNo,
        BigDecimal balance,
        String accountTypeAlias

) {
}
