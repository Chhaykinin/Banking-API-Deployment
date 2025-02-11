package springgradle.bankingproject.features.auth.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String password,
        String ConfirmedPassword
) {
}
