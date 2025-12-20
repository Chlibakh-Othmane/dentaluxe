package ma.dentaluxe.service.actes.exception;

public class ActeNotFoundException extends ActeException {
    public ActeNotFoundException(Long id) {
        super("Acte introuvable avec l'ID : " + id);
    }
}
