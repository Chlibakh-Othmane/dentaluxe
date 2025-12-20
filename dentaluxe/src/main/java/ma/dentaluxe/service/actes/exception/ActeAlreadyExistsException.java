package ma.dentaluxe.service.actes.exception;

public class ActeAlreadyExistsException extends ActeException {
    public ActeAlreadyExistsException(String libelle) {
        super("Un acte avec le libellé '" + libelle + "' existe déjà.");
    }
}
