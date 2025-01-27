package springgradle.bankingproject.features.accountType.dto;


public record AccountTypeUpdateRequest(
        String description,
        Boolean isDeleted
) {
}
