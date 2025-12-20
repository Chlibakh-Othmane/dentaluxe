package ma.dentaluxe.service.agenda.exception;

public class RDVConflictException extends AgendaException {
    public RDVConflictException(String message) {
        super("Conflit de planning : " + message);
    }
}