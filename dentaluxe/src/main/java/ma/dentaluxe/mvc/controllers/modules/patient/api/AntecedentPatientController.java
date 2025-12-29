package ma.dentaluxe.mvc.controllers.modules.patient.api;

import ma.dentaluxe.mvc.dto.patient.AntecedentPatientDTO;
import ma.dentaluxe.mvc.dto.patient.AntecedentPatientCreateDTO;
import java.util.List;

public interface AntecedentPatientController {
    void linkToPatient(AntecedentPatientCreateDTO dto);
    List<AntecedentPatientDTO> getPatientMedicalHistory(Long patientId);
    void updateNote(AntecedentPatientDTO dto);
}