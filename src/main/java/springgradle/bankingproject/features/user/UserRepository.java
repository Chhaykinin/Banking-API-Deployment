package springgradle.bankingproject.features.user;

import org.springframework.data.jpa.repository.JpaRepository;
import springgradle.bankingproject.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // SELECT * FROM account WHERE uuid =?
    Optional<User> findByUuid(String uuid);
}
