package ma.dentaluxe.repository.modules.userManager.api;

import ma.dentaluxe.entities.utilisateur.Secretaire;
import ma.dentaluxe.repository.common.CrudRepository;

public interface SecretaireRepository extends CrudRepository<Secretaire, Long>{
    Secretaire findByCNSS(String numCnss);
}
