package ma.dentaluxe.repository.modules.medicament.api;

import ma.dentaluxe.entities.ordonnance.Medicament;
import ma.dentaluxe.repository.common.CrudRepository;
import java.util.List;

public interface MedicamentRepository extends CrudRepository<Medicament, Long> {

    List<Medicament> findByNom(String nom);
    List<Medicament> findByType(String type);
    List<Medicament> findByRemboursable(Boolean remboursable);
    List<Medicament> findByForme(String forme);
}