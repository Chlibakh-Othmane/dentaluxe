//AUTEUR : AYA LEZREGUE
package ma.dentaluxe.repository.modules.caisse.api;

import ma.dentaluxe.entities.finance.Facture;
import ma.dentaluxe.repository.common.CrudRepository;
import ma.dentaluxe.entities.enums.StatutFacture;
import java.util.List;

public interface FactureRepository extends CrudRepository<Facture, Long> {
    List<Facture> findByStatut(StatutFacture statut);
    Facture findByConsultationId(Long idConsultation);
    List<Facture> findBySituationFinanciereId(Long idSF);
    double calculateTotalFacturesImpayees();
}
