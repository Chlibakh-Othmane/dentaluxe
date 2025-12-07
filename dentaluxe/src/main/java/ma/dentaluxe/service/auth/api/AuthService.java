package ma.dentaluxe.service.auth.api;


import ma.dentaluxe.entities.utilisateur.Utilisateur;

import java.util.List;

public interface AuthService {

    /**
     * Authentifier un utilisateur
     */
    Utilisateur authenticate(String login, String password);

    /**
     * Déconnecter un utilisateur
     */
    void logout(Long userId);

    /**
     * Vérifier si un utilisateur est connecté
     */
    boolean isAuthenticated(Long userId);

    /**
     * Obtenir le rôle principal d'un utilisateur
     */
    String getUserRole(Long userId);

    /**
     * Obtenir tous les rôles d'un utilisateur
     */
    List<String> getUserRoles(Long userId);

    /**
     * Vérifier les permissions
     */
    boolean hasPermission(Long userId, String permission);

    /**
     * Changer le mot de passe
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * Réinitialiser le mot de passe (admin seulement)
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * Vérifier la validité de session
     */
    boolean isSessionValid(Long userId);

    /**
     * Obtenir l'utilisateur connecté
     */
    Utilisateur getCurrentUser();

    /**
     * Définir l'utilisateur connecté
     */
    void setCurrentUser(Utilisateur user);
}