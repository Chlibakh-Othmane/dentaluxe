//AUTEUR : AYA LEZREGUE
package ma.dentaluxe.repository.modules.agenda.api;

import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.repository.common.CrudRepository;
import java.time.LocalDate;
import java.util.List;
public interface RDVRepository extends CrudRepository<RDV, Long> {

    List<RDV> findByDate(LocalDate date);
    List<RDV> findByMedecinId(Long idMedecin);
    List<RDV> findByPatientDossierId(Long idDM);
    List<RDV> findByStatut(StatutRDV statut);
    List<RDV> findByDateRange(LocalDate debut, LocalDate fin);
}
