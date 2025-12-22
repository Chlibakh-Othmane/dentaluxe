package ma.dentaluxe.service.patient.api;

import ma.dentaluxe.mvc.dto.patient.AntecedentPatientCreateDTO;
import ma.dentaluxe.mvc.dto.patient.AntecedentPatientDTO;
import java.util.List;

public interface AntecedentPatientService {

    // Récupérer tout l'historique médical d'un patient (avec les noms des maladies)
    List<AntecedentPatientDTO> getPatientMedicalHistory(Long idPatient);

    // Associer un antécédent (du catalogue) à un patient
    void addAntecedentToPatient(AntecedentPatientCreateDTO dto);

    // Modifier une note ou le statut actif/inactif
    void updatePatientAntecedent(AntecedentPatientDTO dto);

    // Supprimer un antécédent du dossier du patient
    void removeAntecedentFromPatient(Long idPatient, Long idAntecedent);

    // Vérifier si un patient a un antécédent spécifique
    boolean hasAntecedent(Long idPatient, Long idAntecedent);
}