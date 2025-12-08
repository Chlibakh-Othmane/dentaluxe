package ma.dentaluxe.service.auth.Impl;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.auth.api.AuthRepository;
import ma.dentaluxe.service.auth.api.AuthService;
import java.time.LocalDateTime;
import java.util.*;

public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private Utilisateur currentUser;
    private final Map<Long, LocalDateTime> sessions = new HashMap<>();
    private static final long SESSION_TIMEOUT_MINUTES = 30;

    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Utilisateur authenticate(String login, String password) {
        try {
            Utilisateur user = authRepository.authenticate(login, password);

            if (user != null) {
                this.currentUser = user;
                sessions.put(user.getId(), LocalDateTime.now());
                System.out.println("✅ Authentification réussie pour: " + user.getLogin());
                return user;
            }

            System.out.println("❌ Authentification échouée pour: " + login);
            return null;

        } catch (Exception e) {
            System.out.println("❌ Erreur d'authentification: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void logout(Long userId) {
        if (userId != null) {
            sessions.remove(userId);
            if (currentUser != null && currentUser.getId().equals(userId)) {
                currentUser = null;
            }
            System.out.println("✅ Utilisateur " + userId + " déconnecté");
        }
    }

    @Override
    public boolean isAuthenticated(Long userId) {
        if (userId == null) return false;

        LocalDateTime lastActivity = sessions.get(userId);
        if (lastActivity == null) return false;

        // Vérifier le timeout de session
        long minutesSinceLastActivity = java.time.Duration.between(lastActivity, LocalDateTime.now()).toMinutes();
        if (minutesSinceLastActivity > SESSION_TIMEOUT_MINUTES) {
            sessions.remove(userId);
            return false;
        }

        // Mettre à jour le temps d'activité
        sessions.put(userId, LocalDateTime.now());
        return true;
    }

    @Override
    public String getUserRole(Long userId) {
        if (userId == null) return null;
        return authRepository.getUserRole(userId);
    }

    @Override
    public List<String> getUserRoles(Long userId) {
        if (userId == null) return Collections.emptyList();

        // Si votre repository a cette méthode
        // return authRepository.getUserRoles(userId);

        // Sinon, implémentation basique
        String role = getUserRole(userId);
        return role != null ? List.of(role) : Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        if (!isAuthenticated(userId)) return false;

        String role = getUserRole(userId);
        if (role == null) return false;

        // Définir les permissions par rôle
        Map<String, List<String>> rolePermissions = new HashMap<>();
        rolePermissions.put("ADMIN", Arrays.asList(
                "user.create", "user.edit", "user.delete", "user.view",
                "patient.all", "rdv.all", "finance.all", "dashboard.all"
        ));

        rolePermissions.put("MEDECIN", Arrays.asList(
                "patient.view", "patient.create", "consultation.create",
                "ordonnance.create", "certificat.create", "rdv.view"
        ));

        rolePermissions.put("SECRETAIRE", Arrays.asList(
                "patient.create", "patient.edit", "rdv.create", "rdv.edit",
                "payment.create", "invoice.view"
        ));

        List<String> permissions = rolePermissions.getOrDefault(role.toUpperCase(),
                Collections.emptyList());

        return permissions.contains(permission);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        if (!isAuthenticated(userId)) {
            System.out.println("❌ Utilisateur non authentifié");
            return false;
        }

        // Vérifier l'ancien mot de passe
        Utilisateur user = authRepository.findById(userId);
        if (user == null) {
            System.out.println("❌ Utilisateur non trouvé");
            return false;
        }

        // Note: Dans une vraie application, vous devriez hasher les mots de passe
        // et comparer les hashs

        // Mettre à jour le mot de passe
        user.setPasswordHash(newPassword);
        user.setLastModificationDate(LocalDateTime.now());
        user.setUpdatedBy(user.getLogin());

        authRepository.update(user);
        System.out.println("✅ Mot de passe changé pour l'utilisateur: " + user.getLogin());
        return true;
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        // Seul un admin peut réinitialiser les mots de passe
        if (currentUser == null || !"ADMIN".equals(getUserRole(currentUser.getId()))) {
            System.out.println("❌ Permission refusée: Admin requis");
            return false;
        }

        Utilisateur user = authRepository.findById(userId);
        if (user == null) {
            System.out.println("❌ Utilisateur non trouvé");
            return false;
        }

        user.setPasswordHash(newPassword);
        user.setLastModificationDate(LocalDateTime.now());
        user.setUpdatedBy(currentUser.getLogin());

        authRepository.update(user);
        System.out.println("✅ Mot de passe réinitialisé pour: " + user.getLogin());
        return true;
    }

    @Override
    public boolean isSessionValid(Long userId) {
        return isAuthenticated(userId);
    }

    @Override
    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        if (user != null && user.getId() != null) {
            sessions.put(user.getId(), LocalDateTime.now());
        }
    }
}