package ma.dentaluxe.common.exceptions.modules.patient;

import ma.dentaluxe.common.exceptions.BusinessException;

// Pour un patient introuvable
public class PatientNotFoundException extends BusinessException {
    public PatientNotFoundException(Long id) {
        super("Patient introuvable avec l'ID : " + id);
    }
}

