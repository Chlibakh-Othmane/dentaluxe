package ma.dentaluxe.common.exceptions.modules.ordonnance;

import ma.dentaluxe.common.exceptions.BusinessException;

public class PrescriptionNotFoundException extends BusinessException {
    public PrescriptionNotFoundException(Long id) {
        super("La ligne de prescription #" + id + " n'existe pas.");
    }
}
