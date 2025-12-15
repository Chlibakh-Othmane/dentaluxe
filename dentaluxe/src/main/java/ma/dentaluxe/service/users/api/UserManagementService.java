package ma.dentaluxe.service.users.api;

import ma.dentaluxe.service.users.dto.UpdateUserProfileRequest;
import ma.dentaluxe.service.users.dto.UserAccountDto;
import java.util.List;

public interface UserManagementService {

    /**
     * Récupère un utilisateur par son ID
     */
    UserAccountDto getUserById(Long userId);

    /**
     * Récupère un utilisateur par son email
     */
    UserAccountDto getUserByEmail(String email);

    /**
     * Récupère tous les utilisateurs actifs
     */
    List<UserAccountDto> getAllActiveUsers();

    /**
     * Récupère tous les médecins
     */
    List<UserAccountDto> getAllMedecins();

    /**
     * Récupère tous les secrétaires
     */
    List<UserAccountDto> getAllSecretaires();

    /**
     * Met à jour le profil d'un utilisateur
     */
    UserAccountDto updateUserProfile(Long userId, UpdateUserProfileRequest request);

    /**
     * Supprime un utilisateur
     */
    void deleteUser(Long userId);
}