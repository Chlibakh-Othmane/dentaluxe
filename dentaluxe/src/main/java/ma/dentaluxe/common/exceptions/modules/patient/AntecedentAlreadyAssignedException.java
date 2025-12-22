package ma.dentaluxe.common.exceptions.modules.patient;

import ma.dentaluxe.common.exceptions.BusinessException;

public class AntecedentAlreadyAssignedException extends BusinessException {
    public AntecedentAlreadyAssignedException(String nomAntecedent) {
        super("Cet antécédent (" + nomAntecedent + ") est déjà présent dans le dossier du patient.");
    }
}