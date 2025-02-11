package springgradle.bankingproject.features.account.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountRenameRequest(
        @NotBlank(message = "Alias is required")
        String alias
) {
}
