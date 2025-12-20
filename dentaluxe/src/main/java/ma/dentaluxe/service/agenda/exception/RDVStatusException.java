package ma.dentaluxe.service.agenda.exception;

public class RDVStatusException extends AgendaException {
    public RDVStatusException(String message) {
        super("Action impossible sur le statut actuel : " + message);
    }
}
