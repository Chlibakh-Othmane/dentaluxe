package ma.dentaluxe.repository.modules.ordonnance.api;

import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.repository.common.CrudRepository;
import java.util.List;

public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {

    // Méthodes spécifiques pour rechercher par ordonnance
    List<Prescription> findByOrdonnance(Long idOrdo);

    // Méthode pour supprimer toutes les prescriptions d'une ordonnance
    void deleteByOrdonnance(Long idOrdo);
}