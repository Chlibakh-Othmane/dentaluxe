package ma.dentaluxe.service.auth.Impl;

import ma.dentaluxe.service.auth.api.AuthService;
import ma.dentaluxe.service.auth.dto.LoginRequest;
import ma.dentaluxe.service.auth.dto.LoginResponse;
import ma.dentaluxe.service.auth.exception.InvalidCredentialsException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthServiceImpl implements AuthService {

    private final Map<String, Long> sessions = new ConcurrentHashMap<>();
    private final Map<Long, String> userEmails = new ConcurrentHashMap<>();

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        // Cette méthode sera appelée par AuthorizationService
        // qui vérifiera les credentials
        throw new UnsupportedOperationException("Utilisez AuthorizationService.login()");
    }

    @Override
    public boolean validateSession(String sessionId) {
        return sessionId != null && sessions.containsKey(sessionId);
    }

    @Override
    public Long getUserIdFromSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public String generateSession(Long userId, String email) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, userId);
        userEmails.put(userId, email);
        return sessionId;
    }

    @Override
    public void revokeSession(String sessionId) {
        Long userId = sessions.remove(sessionId);
        if (userId != null) {
            userEmails.remove(userId);
        }
    }
}