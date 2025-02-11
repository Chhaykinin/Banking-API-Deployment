package springgradle.bankingproject.features.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerificationRequest(
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Verified code is required ")
        String verifiedCode
) {
}
