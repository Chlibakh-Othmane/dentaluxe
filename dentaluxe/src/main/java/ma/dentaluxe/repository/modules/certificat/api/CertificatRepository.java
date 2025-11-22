package ma.dentaluxe.repository.modules.certificat.api;

import ma.dentaluxe.entities.certificat.Certificat;
import ma.dentaluxe.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface CertificatRepository extends CrudRepository<Certificat, Long> {
    List<Certificat> findByIdDM(Long idDM);
    List<Certificat> findByIdMedecin(Long idMedecin);
    List<Certificat> findByDateDebutBetween(LocalDate startDate, LocalDate endDate);
    List<Certificat> findByDateFinBetween(LocalDate startDate, LocalDate endDate);
}