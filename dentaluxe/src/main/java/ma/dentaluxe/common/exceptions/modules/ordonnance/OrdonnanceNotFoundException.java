package ma.dentaluxe.common.exceptions.modules.ordonnance;

import ma.dentaluxe.common.exceptions.BusinessException;

public class OrdonnanceNotFoundException extends BusinessException {
    public OrdonnanceNotFoundException(Long id) {
        super("Ordonnance #" + id + " introuvable.");
    }
}

