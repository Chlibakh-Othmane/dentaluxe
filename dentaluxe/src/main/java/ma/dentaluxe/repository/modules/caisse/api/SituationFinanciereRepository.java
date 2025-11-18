package ma.dentaluxe.repository.modules.caisse.api;

import ma.dentaluxe.entities.finance.SituationFinanciere;
import ma.dentaluxe.repository.common.CrudRepository;

public interface SituationFinanciereRepository extends CrudRepository<SituationFinanciere, Long> {
    SituationFinanciere findByDossierMedicalId(Long idDM);
    void updateAfterPayment(Long idSF, double montantPaye);
}
