package ma.dentaluxe.service.auth.api;

import ma.dentaluxe.service.auth.dto.LoginRequest;
import ma.dentaluxe.service.auth.dto.LoginResponse;
import ma.dentaluxe.service.auth.dto.RegisterRequest;
import ma.dentaluxe.service.auth.dto.ChangePasswordRequest;
import ma.dentaluxe.service.auth.dto.UserResponse;

public interface AuthorizationService {

    /**
     * Enregistre un nouvel utilisateur
     */
    UserResponse register(RegisterRequest request);

    /**
     * Authentifie un utilisateur
     */
    LoginResponse login(LoginRequest request);

    /**
     * Change le mot de passe d'un utilisateur
     */
    void changePassword(Long userId, ChangePasswordRequest request);

    /**
     * Vérifie si un email existe déjà
     */
    boolean emailExists(String email);

    /**
     * Déconnecte un utilisateur (invalide la session)
     */
    void logout(String sessionId);

    /**
     * Vérifie la validité d'une session
     */
    boolean validateSession(String sessionId);

    /**
     * Récupère les informations d'un utilisateur par son ID
     */
    UserResponse getUserById(Long userId);
}