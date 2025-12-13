package ma.dentaluxe.service.auth.Impl;

import ma.dentaluxe.service.auth.api.AuthorizationService;
import ma.dentaluxe.service.auth.api.AuthService;
import ma.dentaluxe.service.auth.api.PasswordEncoder;
import ma.dentaluxe.service.auth.api.CredentialsValidator;
import ma.dentaluxe.service.auth.dto.*;
import ma.dentaluxe.service.auth.exception.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorizationServiceImpl implements AuthorizationService {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsValidator validator;

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<String, Long> emailToUserId = new ConcurrentHashMap<>();
    private final AtomicLong userIdGenerator = new AtomicLong(1);

    public AuthorizationServiceImpl(AuthService authService,
                                    PasswordEncoder passwordEncoder,
                                    CredentialsValidator validator) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        validator.validateRegistrationData(request);

        if (emailExists(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        Long userId = userIdGenerator.getAndIncrement();
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                userId,
                request.getUsername(),
                request.getEmail(),
                encodedPassword,
                request.getPhoneNumber(),
                LocalDateTime.now(),
                true
        );

        users.put(userId, user);
        emailToUserId.put(request.getEmail(), userId);

        return toUserResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        validator.validateLoginCredentials(request);

        Long userId = emailToUserId.get(request.getEmail());
        if (userId == null) {
            throw new InvalidCredentialsException();
        }

        User user = users.get(userId);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isActive()) {
            throw new AuthException("Compte désactivé");
        }

        String sessionId = authService.generateSession(userId, user.getEmail());

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                sessionId
        );
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Ancien mot de passe incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AuthException("Les mots de passe ne correspondent pas");
        }

        if (!validator.isValidPassword(request.getNewPassword())) {
            throw new AuthException("Le nouveau mot de passe ne respecte pas les critères de sécurité");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public boolean emailExists(String email) {
        return emailToUserId.containsKey(email);
    }

    @Override
    public void logout(String sessionId) {
        authService.revokeSession(sessionId);
    }

    @Override
    public boolean validateSession(String sessionId) {
        return authService.validateSession(sessionId);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.isActive()
        );
    }

    private static class User {
        private final Long id;
        private final String username;
        private final String email;
        private String password;
        private final String phoneNumber;
        private final LocalDateTime createdAt;
        private boolean active;

        public User(Long id, String username, String email, String password,
                    String phoneNumber, LocalDateTime createdAt, boolean active) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.createdAt = createdAt;
            this.active = active;
        }

        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getPhoneNumber() { return phoneNumber; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}