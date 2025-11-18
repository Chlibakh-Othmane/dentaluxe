package ma.dentaluxe.repository.modules.actes.api;

import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.repository.common.CrudRepository;
import ma.dentaluxe.entities.enums.CategorieActe;
import java.util.List;

public interface ActeRepository extends CrudRepository<Acte, Long>{
    List<Acte> findByCategorie(CategorieActe categorie);
    List<Acte> searchByLibelle(String keyword);
}
