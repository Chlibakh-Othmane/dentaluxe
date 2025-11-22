package ma.dentaluxe.repository.modules.dossierMedical.api;

import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.enums.StatutConsultation;
import ma.dentaluxe.repository.common.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface ConsultationRepository extends CrudRepository<Consultation, Long> {
    List<Consultation> findByDossierMedicalId(Long idDM);
    List<Consultation> findByMedecinId(Long idMedecin);
    List<Consultation> findByDate(LocalDate date);
    List<Consultation> findByRDVId(Long idRDV);
    List<Consultation> findByStatut(StatutConsultation statut);
}