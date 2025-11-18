package ma.dentaluxe.repository.modules.userManager.api;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.common.CrudRepository;

import java.util.List;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

    // Trouver un utilisateur par login
    Utilisateur findByLogin(String login);

    // Trouver un utilisateur par email
    Utilisateur findByEmail(String email);

    // Retourner tous les utilisateurs actifs
    List<Utilisateur> findAllActive();
}