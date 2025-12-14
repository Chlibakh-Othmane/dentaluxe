package ma.dentaluxe.repository.modules.caisse.api;
import ma.dentaluxe.entities.caisse.Charge;
import ma.dentaluxe.repository.common.CrudRepository;

public interface ChargeRepository extends CrudRepository<Charge, Long> {
    // Tu pourras ajouter des méthodes comme findByDateBetween...
}