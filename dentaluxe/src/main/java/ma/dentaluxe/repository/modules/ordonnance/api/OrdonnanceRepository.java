package ma.dentaluxe.repository.modules.ordonnance.api;

import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.repository.common.CrudRepository;
import java.util.List;

public interface OrdonnanceRepository extends CrudRepository<Ordonnance, Long> {

    // Méthodes spécifiques pour rechercher par dossier médical
    List<Ordonnance> findByDossierMedical(Long idDM);

    // Méthodes spécifiques pour rechercher par médecin
    List<Ordonnance> findByMedecin(Long idMedecin);
}