package ma.dentaluxe.repository.modules.userManager.api;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.common.CrudRepository;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Utilisateur findByLogin(String login);
    Utilisateur authenticate(String login, String password);
}
