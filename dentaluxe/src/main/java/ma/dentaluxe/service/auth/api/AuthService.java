package ma.dentaluxe.service.auth.api;

import ma.dentaluxe.service.auth.dto.LoginRequest;
import ma.dentaluxe.service.auth.dto.LoginResponse;

public interface AuthService {

    /**
     * Authentifie un utilisateur
     */
    LoginResponse authenticate(LoginRequest request);

    /**
     * Valide une session
     */
    boolean validateSession(String sessionId);

    /**
     * Extrait l'ID utilisateur d'une session
     */
    Long getUserIdFromSession(String sessionId);

    /**
     * Génère une nouvelle session pour un utilisateur
     */
    String generateSession(Long userId, String email);

    /**
     * Révoque une session
     */
    void revokeSession(String sessionId);
}