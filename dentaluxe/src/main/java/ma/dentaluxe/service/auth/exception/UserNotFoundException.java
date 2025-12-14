// UserNotFoundException.java
package ma.dentaluxe.service.auth.exception;

public class UserNotFoundException extends AuthException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("Utilisateur avec l'ID " + userId + " non trouvé");
    }
}