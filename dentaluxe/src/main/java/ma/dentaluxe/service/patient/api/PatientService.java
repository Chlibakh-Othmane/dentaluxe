package ma.dentaluxe.service.patient.api;

import ma.dentaluxe.entities.enums.Assurance;
import ma.dentaluxe.entities.enums.Sexe;
import ma.dentaluxe.mvc.dto.patient.*; // Importe tes nouveaux DTO
import java.time.LocalDate;
import java.util.List;

/**
 * Interface épurée du service Patient.
 */
public interface PatientService {

    // CRUD
    PatientDTO createPatient(PatientCreateDTO dto);
    PatientDTO getPatientById(Long id);
    PatientDTO updatePatient(Long id, PatientUpdateDTO dto);
    void deletePatient(Long id);
    List<PatientDTO> getAllPatients();

    // RECHERCHE (Utile pour tes tableaux Swing)
    List<PatientDTO> searchByNom(String nom);
    List<PatientDTO> searchByPrenom(String prenom);
    List<PatientDTO> searchByTelephone(String telephone);
    PatientDTO searchByEmail(String email);

    // UTILITAIRES
    boolean emailExists(String email);

}