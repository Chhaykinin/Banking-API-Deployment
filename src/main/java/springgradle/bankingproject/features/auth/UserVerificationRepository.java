package springgradle.bankingproject.features.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import springgradle.bankingproject.model.User;
import springgradle.bankingproject.model.UserVerification;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    // relationship
    Optional<UserVerification> findByUserAndVerificationCode(User user, String verificationCode);
    Optional<UserVerification> findByUser(User user);

}
