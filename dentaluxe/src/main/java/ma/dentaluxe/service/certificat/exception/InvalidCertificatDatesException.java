package ma.dentaluxe.service.certificat.exception;

public class InvalidCertificatDatesException extends CertificatException {
    public InvalidCertificatDatesException(String message) {
        super("Erreur de dates sur le certificat : " + message);
    }
}
