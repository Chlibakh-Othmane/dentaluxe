package ma.dentaluxe.common.exceptions.modules.patient;

import ma.dentaluxe.common.exceptions.BusinessException;

// Pour un antécédent introuvable
public class AntecedentNotFoundException extends BusinessException {
    public AntecedentNotFoundException(String nom) {
        super("L'antécédent '" + nom + "' n'existe pas dans la base.");
    }
}
