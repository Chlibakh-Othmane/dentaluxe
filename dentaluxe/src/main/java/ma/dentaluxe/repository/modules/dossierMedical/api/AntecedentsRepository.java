package ma.dentaluxe.repository.modules.dossierMedical.api;

import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.repository.common.CrudRepository;
import java.util.List;

public interface AntecedentsRepository extends CrudRepository<Antecedents, Long> {
    List<Antecedents> findByDossierMedicalId(Long idDM);
    List<Antecedents> findByCategorie(CategorieAntecedent categorie);
    List<Antecedents> findByNiveauRisque(NiveauRisque niveauRisque);
}