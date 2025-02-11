package springgradle.bankingproject.features.user;

import org.springframework.data.jpa.repository.JpaRepository;
import springgradle.bankingproject.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // SELECT * FROM users WHERE uuid =?
    Optional<User> findByUuid(String uuid);
    // SELECT * FROM users WHERE phoneNumber =?
    Optional<User> findByPhoneNumberAndIsDeletedFalse(String phoneNumber);
    // SELECT EXISTS(SELECT * FROM users WHERE  phoneNumber =?);
    Boolean existsByPhoneNumber(String phoneNumber);

    // SELECT EXISTS(SELECT * FROM users WHERE  email =?);
    Boolean existsByEmail(String email);

    // SELECT EXISTS(SELECT * FROM users WHERE  nationalCardId =?);
    Boolean existsByNationalCardId(String nationalCardId);

    Optional<User> findByEmail(String email);
}
