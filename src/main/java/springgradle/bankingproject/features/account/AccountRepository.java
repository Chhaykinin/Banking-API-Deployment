package springgradle.bankingproject.features.account;

import org.springframework.data.jpa.repository.JpaRepository;
import springgradle.bankingproject.model.Account;
import springgradle.bankingproject.model.User;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByActNo(String actNo);
    // SELECT EXISTS(SELECT * FROM account WHERE act_no =?)
    Boolean existsByActNo(String actNo);

}
