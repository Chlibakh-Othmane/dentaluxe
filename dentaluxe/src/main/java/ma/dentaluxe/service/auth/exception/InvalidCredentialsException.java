package ma.dentaluxe.service.auth.exception;

public class InvalidCredentialsException extends AuthException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException() {
        super("Email ou mot de passe incorrect");
    }
}