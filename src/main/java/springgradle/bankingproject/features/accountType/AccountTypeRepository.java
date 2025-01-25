package springgradle.bankingproject.features.accountType;

import org.springframework.data.jpa.repository.JpaRepository;
import springgradle.bankingproject.model.AccountType;

import java.util.Optional;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {
    // SELECT * FROM account_types WHERE alias =?
    Optional<AccountType> findByAlias(String alias);
}
