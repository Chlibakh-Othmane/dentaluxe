package ma.dentaluxe.service.auth.Impl;

import ma.dentaluxe.service.auth.api.CredentialsValidator;
import ma.dentaluxe.service.auth.dto.LoginRequest;
import ma.dentaluxe.service.auth.dto.RegisterRequest;
import ma.dentaluxe.service.auth.exception.AuthException;
import java.util.regex.Pattern;

public class CredentialsValidatorImpl implements CredentialsValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$"
    );

    @Override
    public void validateLoginCredentials(LoginRequest request) {
        if (request == null) {
            throw new AuthException("La requête de connexion est nulle");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new AuthException("L'email est obligatoire");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new AuthException("Le mot de passe est obligatoire");
        }

        if (!isValidEmail(request.getEmail())) {
            throw new AuthException("Format d'email invalide");
        }
    }

    @Override
    public void validateRegistrationData(RegisterRequest request) {
        if (request == null) {
            throw new AuthException("La requête d'inscription est nulle");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new AuthException("Le nom d'utilisateur est obligatoire");
        }

        if (!isValidUsername(request.getUsername())) {
            throw new AuthException("Le nom d'utilisateur doit contenir entre 3 et 50 caractères");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new AuthException("L'email est obligatoire");
        }

        if (!isValidEmail(request.getEmail())) {
            throw new AuthException("Format d'email invalide");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new AuthException("Le mot de passe est obligatoire");
        }

        if (!isValidPassword(request.getPassword())) {
            throw new AuthException(
                    "Le mot de passe doit contenir au moins 8 caractères, " +
                            "une majuscule, une minuscule, un chiffre et un caractère spécial"
            );
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AuthException("Les mots de passe ne correspondent pas");
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    @Override
    public boolean isValidUsername(String username) {
        return username != null &&
                username.trim().length() >= 3 &&
                username.trim().length() <= 50;
    }
}