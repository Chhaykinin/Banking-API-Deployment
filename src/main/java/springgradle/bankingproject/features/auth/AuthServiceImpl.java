package springgradle.bankingproject.features.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springgradle.bankingproject.features.auth.dto.*;
import springgradle.bankingproject.features.user.RoleRepository;
import springgradle.bankingproject.features.user.UserRepository;
import springgradle.bankingproject.mapper.UserMapper;
import springgradle.bankingproject.model.Role;
import springgradle.bankingproject.model.User;
import springgradle.bankingproject.model.UserVerification;
import springgradle.bankingproject.util.RandomUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserVerificationRepository userVerificationRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    // send mail
    private final JavaMailSender javaMailSender;
    private final UserVerificationRepository verificationRepository;

    private final JwtDecoder accessTokenJwtDecoder;
    private final JwtEncoder accessTokenJwtEncoder;
    private final JwtEncoder refreshTokenJwtEncoder;

    // inject sen mail
    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws MessagingException {
        String refreshToken = refreshTokenRequest.refreshToken();
        Authentication auth = new BearerTokenAuthenticationToken(refreshToken);
       auth= jwtAuthenticationProvider.authenticate(auth);
       log.info("Auth: {}", auth.getPrincipal());
        // ROLE_USER ROLE_ADMIN
        String scope = auth
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)// map and get authority
                .collect(Collectors.joining(" ")); // map spring join service
        log.info("Scope: {}", scope);
        // Generate JWT Token by JwtEncoder
        // 1. Define JwtClaimsSet (payload)
        Jwt jwt = (Jwt) auth.getCredentials();
        Instant now = Instant.now();
        JwtClaimsSet jwtAccessClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .subject("Access APIs")
                .issuer(jwt.getId())
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.SECONDS))
                .audience(jwt.getAudience())
                //custom
                .claim("isAdmin",true)
                .claim("studentId","ISTAD0022")
                .claim("scope",jwt.getClaimAsString("scope"))
                .build();

//        // 2. Generate Token
//
        String accessToken = accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtAccessClaimsSet))
                .getTokenValue();
        Instant expiresAt = jwt.getExpiresAt();
        long remainingDays = Duration.between(now, expiresAt).toDays();
        log.info("remainingDays: {}", remainingDays);
        if(remainingDays<=1){
            // refresh token
            JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                    .id(auth.getName())
                    .subject("Refresh Token")
                    .issuer(auth.getName())
                    .issuedAt(now)
                    .expiresAt(now.plus(1, ChronoUnit.DAYS))
                    .audience(List.of("Next js","Android","ios"))
                    //custom
                    .claim("scope",jwt.getClaimAsString("scope"))
                    .build();
            refreshToken = refreshTokenJwtEncoder
                    .encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                    .getTokenValue();
        }

        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        //step 1 validate phone number
        if(userRepository.existsByPhoneNumber(registerRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone number already been used");
        }
        // validate email
        if(userRepository.existsByEmail(registerRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already been used");
        }
        if(!registerRequest.password().equals(registerRequest.confirmedPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Passwords do not match");
        }
        // validate national id card
        if(userRepository.existsByNationalCardId(registerRequest.nationalCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "National card already been used");
        }
        // validate term and policy
        if(!registerRequest.acceptTerm()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must accept the term");
        }
        //step 2 map data
        User user =  userMapper.fromRegisterRequest(registerRequest);
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));// encode password
        user.setProfileImage("profile/default-user.png");
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsVerified(false);
        //set to system
        Role roleUser = roleRepository.findRoleUser();// default role
        Role roleCustomer = roleRepository.findRoleCustomer();
        // add role in list and set
        List<Role> roles = List.of(roleUser, roleCustomer);
        user.setRoles(roles);
        //save to model
        userRepository.save(user);

        return  RegisterResponse.builder()
                .message("You have registered successfully, please verify your email")
                .email(user.getEmail())
                .build();
    }

    @Override
    public void sendVerification(String email) throws MessagingException {
        // validate email
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User have been not found"));
        //set to db
        UserVerification userVerification = new UserVerification();
        userVerification.setUser(user);
        userVerification.setVerificationCode(RandomUtil.random6Digits());
        userVerification.setExpiryTime(LocalTime.now().plusMinutes(1));

        userVerificationRepository.save(userVerification);// sent to db
        // prepare for send email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setFrom(adminEmail);

        //goal
        helper.setSubject("Verification email brother");
        helper.setText(userVerification.getVerificationCode());
        // bos method
        javaMailSender.send(mimeMessage);

    }

    @Override
    public void verify(VerificationRequest verificationRequest) throws MessagingException {
        // validate email
        User user = userRepository.findByEmail(verificationRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User have not been found"));
        // validate verified code
        UserVerification userVerification=userVerificationRepository.findByUserAndVerificationCode(user,verificationRequest.verifiedCode()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User verification code has not been found"));
        // Is verified code expired?
        if(LocalTime.now().isAfter(userVerification.getExpiryTime())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Verification code has expired");
        }

        user.setIsVerified(true);
        userRepository.save(user);
        // update again
        userVerificationRepository.delete(userVerification);
    }

    @Override
    public void resendVerification(String email) throws MessagingException {
        // validate email
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User have not been found"));
        //set to db
        UserVerification userVerification = userVerificationRepository.findByUser(user)
                        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found"));
        // set only two data
        userVerification.setVerificationCode(RandomUtil.random6Digits());
        userVerification.setExpiryTime(LocalTime.now().plusMinutes(1));

        userVerificationRepository.save(userVerification);// sent to db
        // prepare for send email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setFrom(adminEmail);

        //goal
        helper.setSubject("Verification email brother");
        helper.setText(userVerification.getVerificationCode());
        // bos method
        javaMailSender.send(mimeMessage);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // auth of spring security
        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.phoneNumber(),loginRequest.password());
        auth = daoAuthenticationProvider.authenticate(auth);
        // log auth
        log.info("Auth: {}", auth.getPrincipal());

        // log role user
        auth.getAuthorities()
                .forEach(grantedAuthority ->
                        log.info("Authorities: {}", grantedAuthority.getAuthority()));

       // ROLE_USER ROLE_ADMIN
        String scope = auth
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)// map and get authority
                .collect(Collectors.joining(" ")); // map spring join service
        log.info("Scope: {}", scope);
        // Generate JWT Token by JwtEncoder
        // 1. Define JwtClaimsSet (payload)
        Instant now = Instant.now();
        JwtClaimsSet jwtAccessClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Access APIs")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .audience(List.of("Next js","Android","ios"))
                //custom
                .claim("isAdmin",true)
                .claim("studentId","ISTAD0022")
                .claim("scope",scope)
                .build();
        // refresh token
        JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Refresh Token")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .audience(List.of("Next js","Android","ios"))
                //custom
                .claim("scope",scope)
                .build();
        // 2. Generate Token
        // generate access
        String accessToken = accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtAccessClaimsSet))
                .getTokenValue();

        // generate refresh
        String refreshToken = refreshTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                .getTokenValue();
        log.info("Access Token : {}",accessToken);
        log.info("Refresh Token : {}",refreshToken);
        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}
