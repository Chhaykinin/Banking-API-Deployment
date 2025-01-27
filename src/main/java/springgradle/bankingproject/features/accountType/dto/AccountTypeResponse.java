package springgradle.bankingproject.features.accountType.dto;

import lombok.Builder;
@Builder
public record AccountTypeResponse(
        String alias,
        String name,
        String description,
        Boolean isDeleted
) {
}
