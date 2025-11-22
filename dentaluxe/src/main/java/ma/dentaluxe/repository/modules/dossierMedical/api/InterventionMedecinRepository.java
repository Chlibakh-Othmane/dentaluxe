package ma.dentaluxe.repository.modules.dossierMedical.api;

import ma.dentaluxe.entities.consultation.InterventionMedecin;
import ma.dentaluxe.repository.common.CrudRepository;

import java.util.List;

public interface InterventionMedecinRepository extends CrudRepository<InterventionMedecin, Long> {
    List<InterventionMedecin> findByIdMedecin(Long idMedecin);
    List<InterventionMedecin> findByIdConsultation(Long idConsultation);
    List<InterventionMedecin> findByNumDent(Integer numDent);
    List<InterventionMedecin> findByPrixInterventionBetween(Double minPrix, Double maxPrix);
}