package ma.dentaluxe.service.users.api;

import ma.dentaluxe.service.users.dto.CreateAdminRequest;
import ma.dentaluxe.service.users.dto.CreateMedecinRequest;
import ma.dentaluxe.service.users.dto.CreateSecretaireRequest;
import ma.dentaluxe.service.users.dto.UserAccountDto;

public interface RoleManagementService {

    /**
     * Crée un compte administrateur
     */
    UserAccountDto createAdmin(CreateAdminRequest request);

    /**
     * Crée un compte médecin
     */
    UserAccountDto createMedecin(CreateMedecinRequest request);

    /**
     * Crée un compte secrétaire
     */
    UserAccountDto createSecretaire(CreateSecretaireRequest request);

    /**
     * Désactive un utilisateur
     */
    void deactivateUser(Long userId);

    /**
     * Active un utilisateur
     */
    void activateUser(Long userId);

    /**
     * Vérifie si un utilisateur a un rôle spécifique
     */
    boolean hasRole(Long userId, String role);
}