package springgradle.bankingproject.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springgradle.bankingproject.features.accountType.AccountTypeRepository;
import springgradle.bankingproject.features.user.UserRepository;
import springgradle.bankingproject.model.AccountType;
import springgradle.bankingproject.model.User;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final UserRepository userRepository;
    private final AccountTypeRepository accountTypeRepository;
    @PostConstruct
    void init(){
        if(accountTypeRepository.count() == 0) {
            AccountType accountType = new AccountType();
            accountType.setName("Payroll");
            accountType.setAlias("payroll");
            accountType.setIsDeleted(false);
            accountType.setDescription("Payroll account for user");

            AccountType saving = new AccountType();
            saving.setName("Saving");
            saving.setAlias("saving");
            saving.setIsDeleted(false);
            saving.setDescription("Saving account for user");

            accountTypeRepository.save(accountType);
            accountTypeRepository.save(saving);
        }
        if(userRepository.count() == 0){
            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setName("John Doe");
            user.setGender("Male");
            user.setPassword("abcd");
            user.setPin("1234");
            user.setPhoneNumber("0122343443");
            user.setNationalCardId("123456789");
            user.setStudentCardId("HRD-100");
            user.setProfileImage("user/string.png");
            user.setIsDeleted(false);
            user.setIsBlocked(false);
            userRepository.save(user);
        }

    }
}
