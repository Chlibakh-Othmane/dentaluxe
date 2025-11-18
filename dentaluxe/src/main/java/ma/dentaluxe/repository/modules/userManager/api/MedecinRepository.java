package ma.dentaluxe.repository.modules.userManager.api;

import ma.dentaluxe.entities.utilisateur.Medecin;
import ma.dentaluxe.repository.common.CrudRepository;
import java.util.List;

public interface MedecinRepository extends CrudRepository<Medecin, Long>{
    List<Medecin> findBySpecialite(String specialite);
}
