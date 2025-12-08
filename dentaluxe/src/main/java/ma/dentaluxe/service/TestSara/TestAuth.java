package ma.dentaluxe.service.TestSara;




import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.auth.inMemDB_implementation.AuthRepositoryImpl;

import ma.dentaluxe.service.auth.api.AuthService;
import ma.dentaluxe.service.auth.Impl.AuthServiceImpl;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TestAuth {

    private static Scanner scanner = new Scanner(System.in);
    private static AuthService authService;
    private static AuthRepositoryImpl authRepo;

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║           🔐 TEST SERVICE AUTHENTIFICATION           ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");

        // Initialisation
        testConnexionBDD();
        initializeServices();

        int choix;
        do {
            afficherMenu();
            System.out.print("Votre choix (0-10): ");
            choix = scanner.nextInt();
            scanner.nextLine();

            traiterChoix(choix);

            if (choix != 0) {
                System.out.print("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }

        } while (choix != 0);

        scanner.close();
    }

    private static void testConnexionBDD() {
        try (Connection conn = Db.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Connexion à la base de données réussie!");
            } else {
                System.out.println("✗ Échec de connexion à la base de données");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("✗ Erreur de connexion: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void initializeServices() {
        authRepo = new AuthRepositoryImpl();
        authService = new AuthServiceImpl(authRepo);
        System.out.println("✓ Services initialisés avec succès!\n");
    }

    private static void afficherMenu() {
        System.out.println("\n=== MENU TEST AUTH SERVICE ===");
        System.out.println("1.  Tester l'authentification");
        System.out.println("2.  Tester la déconnexion");
        System.out.println("3.  Tester vérification session");
        System.out.println("4.  Tester récupération rôle");
        System.out.println("5.  Tester vérification permissions");
        System.out.println("6.  Tester changement mot de passe");
        System.out.println("7.  Tester réinitialisation mot de passe");
        System.out.println("8.  Tester validité session");
        System.out.println("9.  Tester utilisateur courant");
        System.out.println("10. Tester toutes les fonctionnalités");
        System.out.println("0.  Quitter");
        System.out.println("==============================");
    }

    private static void traiterChoix(int choix) {
        switch (choix) {
            case 1: testerAuthentification(); break;
            case 2: testerDeconnexion(); break;
            case 3: testerVerificationSession(); break;
            case 4: testerRecuperationRole(); break;
            case 5: testerVerificationPermissions(); break;
            case 6: testerChangementMotDePasse(); break;
            case 7: testerReinitialisationMotDePasse(); break;
            case 8: testerValiditeSession(); break;
            case 9: testerUtilisateurCourant(); break;
            case 10: testerToutesFonctionnalites(); break;
            case 0: System.out.println("Au revoir!"); break;
            default: System.out.println("Choix invalide!");
        }
    }

    private static void testerAuthentification() {
        System.out.println("\n=== TEST AUTHENTIFICATION ===");

        System.out.print("Login: ");
        String login = scanner.nextLine();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Utilisateur user = authService.authenticate(login, password);

        if (user != null) {
            System.out.println("\n✅ Authentification réussie!");
            System.out.println("Utilisateur: " + user.getNom() + " " + user.getPrenom());
            System.out.println("ID: " + user.getId());
            System.out.println("Login: " + user.getLogin());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Actif: " + (user.getActif() ? "Oui" : "Non"));
        } else {
            System.out.println("\n❌ Authentification échouée!");
        }
    }

    private static void testerDeconnexion() {
        System.out.println("\n=== TEST DÉCONNEXION ===");

        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté");
            return;
        }

        System.out.println("Utilisateur actuel: " + currentUser.getNom() + " " + currentUser.getPrenom());
        System.out.print("Confirmer la déconnexion (oui/non)? ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("oui")) {
            authService.logout(currentUser.getId());
            System.out.println("✅ Déconnexion effectuée");

            // Vérification
            boolean stillConnected = authService.isAuthenticated(currentUser.getId());
            System.out.println("Session toujours active? " + (stillConnected ? "Oui" : "Non"));
        } else {
            System.out.println("❌ Déconnexion annulée");
        }
    }

    private static void testerVerificationSession() {
        System.out.println("\n=== TEST VÉRIFICATION SESSION ===");

        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté");
            System.out.println("Veuillez d'abord vous authentifier");
            testerAuthentification();
            currentUser = authService.getCurrentUser();
        }

        if (currentUser != null) {
            boolean isAuth = authService.isAuthenticated(currentUser.getId());
            System.out.println("Utilisateur: " + currentUser.getNom() + " " + currentUser.getPrenom());
            System.out.println("Session authentifiée? " + (isAuth ? "✅ Oui" : "❌ Non"));

            if (!isAuth) {
                System.out.println("⚠ La session a probablement expiré");
            }
        }
    }

    private static void testerRecuperationRole() {
        System.out.println("\n=== TEST RÉCUPÉRATION RÔLE ===");

        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté");
            System.out.print("Voulez-vous spécifier un ID utilisateur? (oui/non): ");
            String choix = scanner.nextLine();

            if (choix.equalsIgnoreCase("oui")) {
                System.out.print("ID utilisateur: ");
                Long userId = scanner.nextLong();
                scanner.nextLine();

                String role = authService.getUserRole(userId);
                System.out.println("Rôle de l'utilisateur " + userId + ": " + role);

                List<String> roles = authService.getUserRoles(userId);
                System.out.println("Tous les rôles: " + roles);
            }
        } else {
            String role = authService.getUserRole(currentUser.getId());
            System.out.println("Utilisateur: " + currentUser.getNom() + " " + currentUser.getPrenom());
            System.out.println("Rôle principal: " + role);

            List<String> roles = authService.getUserRoles(currentUser.getId());
            System.out.println("Tous les rôles: " + roles);
        }
    }

    private static void testerVerificationPermissions() {
        System.out.println("\n=== TEST VÉRIFICATION PERMISSIONS ===");

        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté");
            return;
        }

        System.out.println("Utilisateur: " + currentUser.getNom() + " " + currentUser.getPrenom());
        String role = authService.getUserRole(currentUser.getId());
        System.out.println("Rôle: " + role);

        // Tester différentes permissions selon le rôle
        System.out.println("\n🔍 Test des permissions:");

        if ("ADMIN".equalsIgnoreCase(role)) {
            testPermission(currentUser.getId(), "user.create", "Créer utilisateur");
            testPermission(currentUser.getId(), "patient.all", "Gérer tous les patients");
            testPermission(currentUser.getId(), "dashboard.all", "Accès dashboard complet");
        } else if ("MEDECIN".equalsIgnoreCase(role)) {
            testPermission(currentUser.getId(), "patient.view", "Voir patients");
            testPermission(currentUser.getId(), "ordonnance.create", "Créer ordonnance");
            testPermission(currentUser.getId(), "user.create", "Créer utilisateur (admin seulement)");
        } else if ("SECRETAIRE".equalsIgnoreCase(role)) {
            testPermission(currentUser.getId(), "patient.create", "Créer patient");
            testPermission(currentUser.getId(), "rdv.create", "Créer RDV");
            testPermission(currentUser.getId(), "payment.create", "Enregistrer paiement");
        }
    }

    private static void testPermission(Long userId, String permission, String description) {
        boolean hasPermission = authService.hasPermission(userId, permission);
        System.out.println("  " + (hasPermission ? "✅" : "❌") + " " + description +
                " (" + permission + "): " + (hasPermission ? "Autorisé" : "Refusé"));
    }

    private static void testerChangementMotDePasse() {
        System.out.println("\n=== TEST CHANGEMENT MOT DE PASSE ===");

        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté");
            return;
        }

        System.out.println("Utilisateur: " + currentUser.getNom() + " " + currentUser.getPrenom());

        System.out.print("Ancien mot de passe: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Nouveau mot de passe: ");
        String newPassword = scanner.nextLine();

        System.out.print("Confirmer nouveau mot de passe: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("❌ Les mots de passe ne correspondent pas");
            return;
        }

        boolean success = authService.changePassword(currentUser.getId(), oldPassword, newPassword);
        System.out.println(success ? "✅ Mot de passe changé avec succès" : "❌ Échec du changement");
    }

    private static void testerReinitialisationMotDePasse() {
        System.out.println("\n=== TEST RÉINITIALISATION MOT DE PASSE ===");

        // Simuler un admin
        System.out.println("⚠ Cette fonction nécessite des droits ADMIN");

        // Vérifier si l'utilisateur courant est admin
        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            String role = authService.getUserRole(currentUser.getId());
            if (!"ADMIN".equalsIgnoreCase(role)) {
                System.out.println("❌ Permission refusée: Admin requis");
                return;
            }
        } else {
            // Pour le test, simuler un admin
            System.out.println("Simulation d'un admin pour le test...");
            authService.setCurrentUser(createAdminUserForTest());
        }

        System.out.print("ID utilisateur à réinitialiser: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Nouveau mot de passe: ");
        String newPassword = scanner.nextLine();

        boolean success = authService.resetPassword(userId, newPassword);
        System.out.println(success ? "✅ Mot de passe réinitialisé" : "❌ Échec de la réinitialisation");
    }

    private static Utilisateur createAdminUserForTest() {
        return Utilisateur.builder()
                .id(1L)
                .nom("Admin")
                .prenom("Test")
                .login("admin_test")
                .email("admin@test.com")
                .actif(true)
                .build();
    }

    private static void testerValiditeSession() {
        System.out.println("\n=== TEST VALIDITÉ SESSION ===");

        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ Aucun utilisateur connecté");
            return;
        }

        boolean sessionValid = authService.isSessionValid(currentUser.getId());
        System.out.println("Utilisateur: " + currentUser.getNom() + " " + currentUser.getPrenom());
        System.out.println("Session valide? " + (sessionValid ? "✅ Oui" : "❌ Non"));

        if (sessionValid) {
            System.out.println("La session est active et valide");
        } else {
            System.out.println("La session a expiré ou est invalide");
        }
    }

    private static void testerUtilisateurCourant() {
        System.out.println("\n=== TEST UTILISATEUR COURANT ===");

        Utilisateur currentUser = authService.getCurrentUser();

        if (currentUser != null) {
            System.out.println("✅ Utilisateur courant trouvé:");
            System.out.println("   ID: " + currentUser.getId());
            System.out.println("   Nom complet: " + currentUser.getNom() + " " + currentUser.getPrenom());
            System.out.println("   Login: " + currentUser.getLogin());
            System.out.println("   Email: " + currentUser.getEmail());
            System.out.println("   Actif: " + (currentUser.getActif() ? "Oui" : "Non"));
            System.out.println("   Rôle: " + authService.getUserRole(currentUser.getId()));
        } else {
            System.out.println("❌ Aucun utilisateur courant");
            System.out.println("Voulez-vous définir un utilisateur de test? (oui/non)");
            String choix = scanner.nextLine();

            if (choix.equalsIgnoreCase("oui")) {
                Utilisateur testUser = Utilisateur.builder()
                        .id(999L)
                        .nom("Test")
                        .prenom("Utilisateur")
                        .login("test_user")
                        .email("test@user.com")
                        .actif(true)
                        .build();

                authService.setCurrentUser(testUser);
                System.out.println("✅ Utilisateur de test défini");
                testerUtilisateurCourant(); // Rappeler la méthode
            }
        }
    }

    private static void testerToutesFonctionnalites() {
        System.out.println("\n=== TEST COMPLET TOUTES LES FONCTIONNALITÉS ===\n");

        System.out.println("1. Authentification d'un utilisateur test...");
        // Créer un utilisateur test si nécessaire
        System.out.println("Login: admin");
        System.out.println("Mot de passe: admin123");

        Utilisateur testUser = authService.authenticate("admin", "admin123");
        if (testUser == null) {
            System.out.println("⚠ Authentification échouée, utilisation d'un utilisateur simulé");
            testUser = createAdminUserForTest();
            authService.setCurrentUser(testUser);
        }

        System.out.println("\n2. Test de toutes les méthodes:");

        // Test 1: Vérification session
        boolean isAuth = authService.isAuthenticated(testUser.getId());
        System.out.println("   • Session authentifiée: " + (isAuth ? "✅" : "❌"));

        // Test 2: Récupération rôle
        String role = authService.getUserRole(testUser.getId());
        System.out.println("   • Rôle utilisateur: " + role);

        // Test 3: Vérification permissions
        boolean hasUserCreate = authService.hasPermission(testUser.getId(), "user.create");
        System.out.println("   • Permission 'user.create': " + (hasUserCreate ? "✅" : "❌"));

        // Test 4: Validité session
        boolean sessionValid = authService.isSessionValid(testUser.getId());
        System.out.println("   • Session valide: " + (sessionValid ? "✅" : "❌"));

        // Test 5: Utilisateur courant
        Utilisateur current = authService.getCurrentUser();
        System.out.println("   • Utilisateur courant: " +
                (current != null ? current.getNom() + " " + current.getPrenom() : "❌ Aucun"));

        // Test 6: Déconnexion
        System.out.println("\n3. Test déconnexion...");
        authService.logout(testUser.getId());
        boolean stillConnected = authService.isAuthenticated(testUser.getId());
        System.out.println("   • Déconnexion effectuée: " + (!stillConnected ? "✅" : "❌"));

        System.out.println("\n✅ Test complet terminé!");
    }
}