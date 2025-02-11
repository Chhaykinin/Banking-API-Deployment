package springgradle.bankingproject.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springgradle.bankingproject.features.user.UserRepository;
import springgradle.bankingproject.model.User;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user= userRepository
                .findByPhoneNumberAndIsDeletedFalse(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User has been not found"));
        log.info("User found with phone number: {} " , user.getPhoneNumber());
        CustomUserDetails customUserDetails= new CustomUserDetails();
        customUserDetails.setUser(user);
        return customUserDetails;
    }
}
