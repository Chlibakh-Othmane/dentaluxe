package ma.dentaluxe.repository.modules.auth.api;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.common.CrudRepository;

public interface AuthRepository extends CrudRepository<Utilisateur, Long> {

    Utilisateur findByEmail(String email);

    Utilisateur findByUsername(String username);
}