package ma.dentaluxe.service.agenda.exception;

public class RDVNotFoundException extends AgendaException {
    public RDVNotFoundException(Long id) {
        super("Le rendez-vous avec l'ID " + id + " est introuvable.");
    }
}