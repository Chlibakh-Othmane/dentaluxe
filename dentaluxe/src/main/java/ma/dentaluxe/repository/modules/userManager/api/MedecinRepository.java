package ma.dentaluxe.repository.modules.userManager.api;

import ma.dentaluxe.entities.utilisateur.Medecin;
import ma.dentaluxe.repository.modules.userManager.api.SecretaireRepository;

import java.util.List;

public interface MedecinRepository extends SecretaireRepository {

    // Trouver des médecins par spécialité
    List<Medecin> findBySpecialite(String specialite);


}