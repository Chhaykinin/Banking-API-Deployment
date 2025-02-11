package springgradle.bankingproject.features.auth;

import jakarta.mail.MessagingException;
import springgradle.bankingproject.features.auth.dto.*;

public interface AuthService {
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws MessagingException;
    RegisterResponse register(RegisterRequest registerRequest);
    void sendVerification(String email) throws MessagingException;
    void verify(VerificationRequest verificationRequest) throws MessagingException;
    void resendVerification(String email) throws MessagingException;
    AuthResponse login(LoginRequest loginRequest);
}
