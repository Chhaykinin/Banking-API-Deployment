package springgradle.bankingproject.features.accountType.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record AccountTypeRequest(
        @NotBlank(message = "Alias is required")
        String alias, //sco
        @NotBlank(message = "Alias is required")
        String name,
        String description
) {
}
