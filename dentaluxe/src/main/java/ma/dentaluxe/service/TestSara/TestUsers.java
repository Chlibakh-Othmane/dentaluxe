package ma.dentaluxe.service.TestSara;
import ma.dentaluxe.service.users.Impl.*;
import ma.dentaluxe.service.users.api.*;
import ma.dentaluxe.service.users.dto.*;
import ma.dentaluxe.entities.enums.Sexe;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestUsers {

    private static RoleManagementServiceImpl roleManagementService;
    private static UserManagementService userManagementService;

    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     TESTS DU SERVICE DE GESTION DES UTILISATEURS DENTALUXE    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        initServices();

        testCreateAdmin();
        testCreateMedecin();
        testCreateSecretaire();
        testDuplicateEmailDetection();
        testGetUserById();
        testGetUserByEmail();
        testUpdateUserProfile();
        testDeactivateUser();
        testActivateUser();
        testGetAllActiveUsers();
        testGetAllMedecins();
        testGetAllSecretaires();
        testHasRole();
        testDeleteUser();

        printSummary();
    }

    private static void initServices() {
        System.out.println("═══ INITIALISATION DES SERVICES ═══");
        try {
            roleManagementService = new RoleManagementServiceImpl();
            userManagementService = new UserManagementServiceImpl(roleManagementService);

            System.out.println("✓ Services initialisés avec succès\n");
        } catch (Exception e) {
            System.out.println("✗ Erreur lors de l'initialisation: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void testCreateAdmin() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 1: Création d'un compte administrateur");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            CreateAdminRequest request = new CreateAdminRequest(
                    "Alami",
                    "Hassan",
                    "hassan.alami@dentaluxe.ma",
                    "0612345678",
                    "Casablanca, Maroc",
                    "AB123456",
                    Sexe.HOMME,
                    "halami",
                    "AdminPass123@",
                    LocalDate.of(1985, 5, 15)
            );

            UserAccountDto admin = roleManagementService.createAdmin(request);

            assert admin != null : "Admin créé ne doit pas être null";
            assert "ADMIN".equals(admin.getRole()) : "Le rôle doit être ADMIN";
            assert "Hassan".equals(admin.getPrenom()) : "Prénom incorrect";
            assert "Alami".equals(admin.getNom()) : "Nom incorrect";
            assert admin.getActif() : "L'admin doit être actif";

            System.out.println("✓ Administrateur créé: " + admin.getFullName());
            System.out.println("✓ Email: " + admin.getEmail());
            System.out.println("✓ Login: " + admin.getLogin());
            System.out.println("✓ Rôle: " + admin.getRole());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testCreateMedecin() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 2: Création d'un compte médecin");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            CreateMedecinRequest request = new CreateMedecinRequest(
                    "Benjelloun",
                    "Fatima",
                    "fatima.benjelloun@dentaluxe.ma",
                    "0623456789",
                    "Rabat, Maroc",
                    "CD789012",
                    Sexe.FEMME,
                    "fbenjelloun",
                    "MedecinPass123@",
                    LocalDate.of(1990, 8, 20),
                    "Orthodontie",
                    "Lun-Ven 9h-17h"
            );

            UserAccountDto medecin = roleManagementService.createMedecin(request);

            assert medecin != null : "Médecin créé ne doit pas être null";
            assert "MEDECIN".equals(medecin.getRole()) : "Le rôle doit être MEDECIN";
            assert "Fatima".equals(medecin.getPrenom()) : "Prénom incorrect";
            assert "Orthodontie".equals(medecin.getSpecialite()) : "Spécialité incorrecte";
            assert medecin.getAgendaMedecin() != null : "Agenda doit être défini";

            System.out.println("✓ Médecin créé: Dr. " + medecin.getFullName());
            System.out.println("✓ Spécialité: " + medecin.getSpecialite());
            System.out.println("✓ Agenda: " + medecin.getAgendaMedecin());
            System.out.println("✓ Rôle: " + medecin.getRole());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testCreateSecretaire() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 3: Création d'un compte secrétaire");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            CreateSecretaireRequest request = new CreateSecretaireRequest(
                    "El Amrani",
                    "Samira",
                    "samira.elamrani@dentaluxe.ma",
                    "0634567890",
                    "Fès, Maroc",
                    "EF345678",
                    Sexe.FEMME,
                    "selamrani",
                    "SecretairePass123@",
                    LocalDate.of(1995, 3, 10),
                    "CNSS123456789",
                    new BigDecimal("500.00")
            );

            UserAccountDto secretaire = roleManagementService.createSecretaire(request);

            assert secretaire != null : "Secrétaire créée ne doit pas être null";
            assert "SECRETAIRE".equals(secretaire.getRole()) : "Le rôle doit être SECRETAIRE";
            assert "Samira".equals(secretaire.getPrenom()) : "Prénom incorrect";
            assert "CNSS123456789".equals(secretaire.getNumCnss()) : "Numéro CNSS incorrect";
            assert new BigDecimal("500.00").equals(secretaire.getCommission()) : "Commission incorrecte";

            System.out.println("✓ Secrétaire créée: " + secretaire.getFullName());
            System.out.println("✓ Numéro CNSS: " + secretaire.getNumCnss());
            System.out.println("✓ Commission: " + secretaire.getCommission() + " DH");
            System.out.println("✓ Rôle: " + secretaire.getRole());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testDuplicateEmailDetection() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 4: Détection d'email dupliqué");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            CreateAdminRequest duplicateRequest = new CreateAdminRequest(
                    "Test",
                    "User",
                    "hassan.alami@dentaluxe.ma",
                    "0600000000",
                    "Test Address",
                    "TEST123",
                    Sexe.HOMME,
                    "testuser",
                    "TestPass123@",
                    LocalDate.of(1990, 1, 1)
            );

            try {
                roleManagementService.createAdmin(duplicateRequest);
                System.out.println("✗ TEST ÉCHOUÉ: Exception attendue non levée\n");
                testsFailed++;
            } catch (RuntimeException e) {
                System.out.println("✓ Email dupliqué détecté correctement");
                System.out.println("✓ Message: " + e.getMessage());
                System.out.println("✓ TEST RÉUSSI\n");
                testsPassed++;
            }
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testGetUserById() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 5: Récupération d'un utilisateur par ID");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            UserAccountDto user = userManagementService.getUserById(1L);

            assert user != null : "Utilisateur ne doit pas être null";
            assert user.getId().equals(1L) : "ID incorrect";
            assert "hassan.alami@dentaluxe.ma".equals(user.getEmail()) : "Email incorrect";

            System.out.println("✓ Utilisateur récupéré: " + user.getFullName());
            System.out.println("✓ ID: " + user.getId());
            System.out.println("✓ Email: " + user.getEmail());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testGetUserByEmail() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 6: Récupération d'un utilisateur par email");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            UserAccountDto user = userManagementService.getUserByEmail("fatima.benjelloun@dentaluxe.ma");

            assert user != null : "Utilisateur ne doit pas être null";
            assert "fatima.benjelloun@dentaluxe.ma".equals(user.getEmail()) : "Email incorrect";
            assert "MEDECIN".equals(user.getRole()) : "Rôle incorrect";

            System.out.println("✓ Utilisateur récupéré: " + user.getFullName());
            System.out.println("✓ Email: " + user.getEmail());
            System.out.println("✓ Rôle: " + user.getRole());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testUpdateUserProfile() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 7: Mise à jour du profil utilisateur");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                    "Alami",
                    "Hassan",
                    "0699999999",
                    "Nouvelle adresse, Casablanca",
                    Sexe.HOMME,
                    LocalDate.of(1985, 5, 15)
            );

            UserAccountDto updatedUser = userManagementService.updateUserProfile(1L, updateRequest);

            assert "0699999999".equals(updatedUser.getTel()) : "Téléphone non mis à jour";
            assert "Nouvelle adresse, Casablanca".equals(updatedUser.getAdresse()) : "Adresse non mise à jour";

            System.out.println("✓ Profil mis à jour: " + updatedUser.getFullName());
            System.out.println("✓ Nouveau téléphone: " + updatedUser.getTel());
            System.out.println("✓ Nouvelle adresse: " + updatedUser.getAdresse());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testDeactivateUser() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 8: Désactivation d'un utilisateur");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            roleManagementService.deactivateUser(3L);

            UserAccountDto user = userManagementService.getUserById(3L);
            assert !user.getActif() : "L'utilisateur doit être inactif";

            System.out.println("✓ Utilisateur désactivé: " + user.getFullName());
            System.out.println("✓ Statut actif: " + user.getActif());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testActivateUser() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 9: Activation d'un utilisateur");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            roleManagementService.activateUser(3L);

            UserAccountDto user = userManagementService.getUserById(3L);
            assert user.getActif() : "L'utilisateur doit être actif";

            System.out.println("✓ Utilisateur activé: " + user.getFullName());
            System.out.println("✓ Statut actif: " + user.getActif());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testGetAllActiveUsers() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 10: Récupération de tous les utilisateurs actifs");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            List<UserAccountDto> activeUsers = userManagementService.getAllActiveUsers();

            assert activeUsers != null : "La liste ne doit pas être null";
            assert activeUsers.size() == 3 : "Devrait avoir 3 utilisateurs actifs";

            System.out.println("✓ Nombre d'utilisateurs actifs: " + activeUsers.size());
            for (UserAccountDto user : activeUsers) {
                System.out.println("  - " + user.getFullName() + " (" + user.getRole() + ")");
            }
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testGetAllMedecins() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 11: Récupération de tous les médecins");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            List<UserAccountDto> medecins = userManagementService.getAllMedecins();

            assert medecins != null : "La liste ne doit pas être null";
            assert medecins.size() == 1 : "Devrait avoir 1 médecin";
            assert "MEDECIN".equals(medecins.get(0).getRole()) : "Le rôle doit être MEDECIN";

            System.out.println("✓ Nombre de médecins: " + medecins.size());
            for (UserAccountDto medecin : medecins) {
                System.out.println("  - Dr. " + medecin.getFullName() + " - " + medecin.getSpecialite());
            }
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testGetAllSecretaires() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 12: Récupération de tous les secrétaires");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            List<UserAccountDto> secretaires = userManagementService.getAllSecretaires();

            assert secretaires != null : "La liste ne doit pas être null";
            assert secretaires.size() == 1 : "Devrait avoir 1 secrétaire";
            assert "SECRETAIRE".equals(secretaires.get(0).getRole()) : "Le rôle doit être SECRETAIRE";

            System.out.println("✓ Nombre de secrétaires: " + secretaires.size());
            for (UserAccountDto secretaire : secretaires) {
                System.out.println("  - " + secretaire.getFullName() + " (CNSS: " + secretaire.getNumCnss() + ")");
            }
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testHasRole() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 13: Vérification des rôles");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            assert roleManagementService.hasRole(1L, "ADMIN") : "User 1 devrait être ADMIN";
            assert !roleManagementService.hasRole(1L, "MEDECIN") : "User 1 ne devrait pas être MEDECIN";

            assert roleManagementService.hasRole(2L, "MEDECIN") : "User 2 devrait être MEDECIN";
            assert !roleManagementService.hasRole(2L, "SECRETAIRE") : "User 2 ne devrait pas être SECRETAIRE";

            assert roleManagementService.hasRole(3L, "SECRETAIRE") : "User 3 devrait être SECRETAIRE";
            assert !roleManagementService.hasRole(3L, "ADMIN") : "User 3 ne devrait pas être ADMIN";

            System.out.println("✓ User 1 est ADMIN: true");
            System.out.println("✓ User 2 est MEDECIN: true");
            System.out.println("✓ User 3 est SECRETAIRE: true");
            System.out.println("✓ Vérifications croisées correctes");
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testDeleteUser() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 14: Suppression (désactivation) d'un utilisateur");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            // Créer un utilisateur temporaire pour le test
            CreateAdminRequest tempRequest = new CreateAdminRequest(
                    "Temp",
                    "User",
                    "temp.user@dentaluxe.ma",
                    "0611111111",
                    "Test Address",
                    "TEMP123",
                    Sexe.HOMME,
                    "tempuser",
                    "TempPass123@",
                    LocalDate.of(1990, 1, 1)
            );

            UserAccountDto tempUser = roleManagementService.createAdmin(tempRequest);
            Long tempUserId = tempUser.getId();

            // Supprimer l'utilisateur
            userManagementService.deleteUser(tempUserId);

            // Vérifier qu'il est désactivé
            UserAccountDto deletedUser = userManagementService.getUserById(tempUserId);
            assert !deletedUser.getActif() : "L'utilisateur devrait être désactivé";

            System.out.println("✓ Utilisateur créé temporairement: " + tempUser.getFullName());
            System.out.println("✓ Utilisateur supprimé (désactivé)");
            System.out.println("✓ Statut actif après suppression: " + deletedUser.getActif());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void printSummary() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      RÉSUMÉ DES TESTS                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║  Total de tests    : " + String.format("%-42d", totalTests) + "║");
        System.out.println("║  Tests réussis     : " + String.format("%-42d", testsPassed) + "║");
        System.out.println("║  Tests échoués     : " + String.format("%-42d", testsFailed) + "║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        if (testsFailed == 0) {
            System.out.println("║  ✓✓✓ TOUS LES TESTS ONT RÉUSSI ! ✓✓✓                          ║");
            System.out.println("║  Service de gestion des utilisateurs validé à 100%            ║");
        } else {
            double successRate = (testsPassed * 100.0) / totalTests;
            System.out.println("║  Taux de réussite  : " + String.format("%.2f%%", successRate) + "                                      ║");
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
}