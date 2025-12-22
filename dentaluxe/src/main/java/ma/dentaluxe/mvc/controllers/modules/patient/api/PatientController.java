package ma.dentaluxe.mvc.controllers.modules.patient.api;

import ma.dentaluxe.mvc.dto.patient.PatientCreateDTO;
import ma.dentaluxe.mvc.dto.patient.PatientDTO;
import java.util.List;

public interface PatientController {

    // Ta méthode spécifique
    void showRecentPatients();

    // Méthodes standards pour que le contrôleur soit utile à ta vue
    void savePatient(PatientCreateDTO dto);
    List<PatientDTO> getAllPatients();
    void removePatient(Long id);
}