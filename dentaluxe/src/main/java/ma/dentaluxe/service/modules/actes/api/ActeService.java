// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.modules.actes.api;

import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import java.util.List;

public interface ActeService {

    List<Acte> getAllActes();

    Acte getActeById(Long id);

    void addActe(Acte acte);

    void updateActe(Acte acte);

    void deleteActeById(Long id);

    List<Acte> getActesByCategorie(CategorieActe categorie);

    List<Acte> searchActesByLibelle(String keyword);
}