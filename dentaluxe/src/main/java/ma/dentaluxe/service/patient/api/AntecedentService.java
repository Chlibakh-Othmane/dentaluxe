package ma.dentaluxe.service.patient.api;

import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import java.util.List;

/**
 * Interface du service de gestion des antécédents médicaux.
 */
public interface AntecedentService {

    // ==================== CRUD DE BASE ====================

    List<Antecedent> findAll();

    Antecedent findById(Long id);

    Antecedent create(Antecedent antecedent);

    Antecedent update(Long id, Antecedent antecedent);

    void delete(Long id);

    // ==================== MÉTHODES SPÉCIFIQUES ====================

    List<Antecedent> findByNom(String nom);

    List<Antecedent> findByCategorie(CategorieAntecedent categorie);

    List<Antecedent> findByNiveauRisque(NiveauRisque niveauRisque);

    List<Antecedent> findByPatientId(Long patientId);

    void addAntecedentToPatient(Long patientId, Long antecedentId, String notes);

    void removeAntecedentFromPatient(Long patientId, Long antecedentId);

    String getNotesForPatient(Long patientId, Long antecedentId);

    void updateNotesForPatient(Long patientId, Long antecedentId, String notes);

    // ==================== MÉTHODES UTILITAIRES ====================

    String getStatistics();

    List<Antecedent> findWithPagination(int page, int size);

    long count();

    boolean existsById(Long id);
}