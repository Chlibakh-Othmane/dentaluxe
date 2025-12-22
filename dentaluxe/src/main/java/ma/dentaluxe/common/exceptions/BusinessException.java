package ma.dentaluxe.common.exceptions;

// On utilise RuntimeException pour ne pas alourdir le code avec des "throws" partout
public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}