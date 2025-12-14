package ma.dentaluxe.service.dossierMedical.api;

import ma.dentaluxe.mvc.dto.dossier.*;
import java.util.List;

public interface DossierMedicalService {
    // Dossier
    DossierMedicalDTO createDossier(Long idPatient);
    DossierMedicalDTO getDossierById(Long id);
    DossierMedicalDTO getDossierByPatientId(Long idPatient);
    boolean hasDossier(Long idPatient);

    // Antécédents
    AntecedentDTO addAntecedent(AntecedentDTO dto);
    List<AntecedentDTO> getAntecedentsByDossier(Long idDM);
    void removeAntecedent(Long idAntecedent);

    // Consultations
    ConsultationDTO planifierConsultation(ConsultationDTO dto);
    ConsultationDTO terminerConsultation(Long idConsultation, String observation);
    void annulerConsultation(Long idConsultation);
    ConsultationDTO getConsultationById(Long id);
    List<ConsultationDTO> getConsultationsByDossier(Long idDM);
}