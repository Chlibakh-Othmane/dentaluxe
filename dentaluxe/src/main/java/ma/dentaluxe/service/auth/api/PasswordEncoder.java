package ma.dentaluxe.service.auth.api;

public interface PasswordEncoder {

    /**
     * Encode un mot de passe
     */
    String encode(String rawPassword);

    /**
     * Vérifie si un mot de passe correspond à son hash
     */
    boolean matches(String rawPassword, String encodedPassword);
}