package ma.dentaluxe.repository.modules.patient.api;

import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.repository.common.CrudRepository;
import java.util.List;

public interface AntecedentRepository extends CrudRepository<Antecedent, Long> {

    List<Antecedent> findByNom(String nom);

    Antecedent findById(Long id);

    void create(Antecedent antecedent);

    void update(Antecedent antecedent);

    void delete(Antecedent antecedent);

    void deleteById(Long id);


    // Recherche par catégorie
    List<Antecedent> findByCategorie(ma.dentaluxe.entities.enums.CategorieAntecedent categorie);

    // Recherche par niveau de risque
    List<Antecedent> findByNiveauRisque(ma.dentaluxe.entities.enums.NiveauRisque niveauRisque);

    // Récupère les antécédents d'un patient spécifique
    List<Antecedent> findByPatientId(Long patientId);

    // Ajoute un antécédent à un patient
    void addAntecedentToPatient(Long patientId, Long antecedentId, String notes);

    // Supprime un antécédent d'un patient
    void removeAntecedentFromPatient(Long patientId, Long antecedentId);

    // Récupère les notes d'un antécédent pour un patient
    String getNotesForPatient(Long patientId, Long antecedentId);

    // Met à jour les notes d'un antécédent pour un patient
    void updateNotesForPatient(Long patientId, Long antecedentId, String notes);
}