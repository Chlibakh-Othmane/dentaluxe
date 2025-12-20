package ma.dentaluxe.service.certificat.exception;

public class CertificatNotFoundException extends CertificatException {
    public CertificatNotFoundException(Long id) {
        super("Certificat médical introuvable (ID: " + id + ")");
    }
}
