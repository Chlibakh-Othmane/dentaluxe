package ma.dentaluxe.repository.modules.userManager.api;

import ma.dentaluxe.entities.utilisateur.Secretaire;
import java.util.List;

public interface SecretaireRepository extends UtilisateurRepository {

    // Trouver un secrétaire par numéro CNSS
    Secretaire findByNumCNSS(String numCNSS);

    // Lister tous les secrétaires dont la commission est dans un intervalle
    List<Secretaire> findAllByCommission(double minCommission, double maxCommission);
}