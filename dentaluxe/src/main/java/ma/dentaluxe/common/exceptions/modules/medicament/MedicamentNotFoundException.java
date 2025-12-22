package ma.dentaluxe.common.exceptions.modules.medicament;

import ma.dentaluxe.common.exceptions.BusinessException;

public class MedicamentNotFoundException extends BusinessException {
    public MedicamentNotFoundException(String nom) {
        super("Le médicament '" + nom + "' est inconnu ou n'est plus en stock.");
    }
}