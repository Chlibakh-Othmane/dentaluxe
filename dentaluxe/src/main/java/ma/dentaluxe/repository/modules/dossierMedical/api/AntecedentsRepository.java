package ma.dentaluxe.repository.modules.dossierMedical.api;

import ma.dentaluxe.entities.patient.Antecedent; // Importe TON entité
import ma.dentaluxe.repository.common.CrudRepository;
import java.util.List;

// Change <Antecedents, Long> par <Antecedent, Long>
public interface AntecedentsRepository extends CrudRepository<Antecedent, Long> {
    List<Antecedent> findByDossierMedicalId(Long idDM);
    List<Antecedent> findByCategorie(ma.dentaluxe.entities.enums.CategorieAntecedent categorie);
    List<Antecedent> findByNiveauRisque(ma.dentaluxe.entities.enums.NiveauRisque niveauRisque);
}