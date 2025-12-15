package ma.dentaluxe.service.auth.api;

import ma.dentaluxe.service.auth.dto.LoginRequest;
import ma.dentaluxe.service.auth.dto.RegisterRequest;

public interface CredentialsValidator {

    /**
     * Valide les credentials de connexion
     */
    void validateLoginCredentials(LoginRequest request);

    /**
     * Valide les données d'enregistrement
     */
    void validateRegistrationData(RegisterRequest request);

    /**
     * Valide le format d'un email
     */
    boolean isValidEmail(String email);

    /**
     * Valide la force d'un mot de passe
     */
    boolean isValidPassword(String password);

    /**
     * Valide qu'un nom d'utilisateur est acceptable
     */
    boolean isValidUsername(String username);
}