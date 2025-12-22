package ma.dentaluxe.common.exceptions.modules.patient;

import ma.dentaluxe.common.exceptions.BusinessException;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException(String email) {
        super("Un patient possède déjà l'adresse email : " + email);
    }
}