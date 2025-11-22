package ma.dentaluxe.repository.modules.dossierMedical.api;

import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface DossierMedicalRepository extends CrudRepository<DossierMedical, Long> {
    DossierMedical findByPatientId(Long idPatient);
}