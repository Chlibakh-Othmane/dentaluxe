package ma.dentaluxe.service.TestSara;

import ma.dentaluxe.service.auth.Impl.*;
import ma.dentaluxe.service.auth.api.*;
import ma.dentaluxe.service.auth.dto.*;
import ma.dentaluxe.service.auth.exception.*;

public class TestAuth {

    private static AuthorizationService authorizationService;
    private static AuthService authService;
    private static PasswordEncoder passwordEncoder;
    private static CredentialsValidator validator;
    private static UserServiceImpl userService;

    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║   TESTS DU SERVICE D'AUTHENTIFICATION DENTALUXE                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Initialisation
        initServices();

        // Exécution des tests
        testUserRegistration();
        testDuplicateEmailDetection();
        testCredentialsValidation();
        testSuccessfulLogin();
        testLoginWithWrongPassword();
        testLoginWithNonExistentEmail();
        testSessionValidation();
        testGetUserProfile();
        testEmailExistence();
        testPasswordChange();
        testPasswordChangeWithWrongOldPassword();
        testLogout();
        testPasswordEncoding();
        testRegistrationWithInvalidData();
        testMultipleSessions();

        // Résumé final
        printSummary();
    }

    private static void initServices() {
        System.out.println("═══ INITIALISATION DES SERVICES ═══");
        try {
            authService = new AuthServiceImpl();
            passwordEncoder = new PasswordEncoderImpl();
            validator = new CredentialsValidatorImpl();
            authorizationService = new AuthorizationServiceImpl(authService, passwordEncoder, validator);
            userService = new UserServiceImpl(authorizationService);

            System.out.println("✓ Services initialisés avec succès\n");
        } catch (Exception e) {
            System.out.println("✗ Erreur lors de l'initialisation: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void testUserRegistration() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 1: Inscription d'un nouvel utilisateur");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            RegisterRequest request = new RegisterRequest(
                    "Dr. Ahmed Bennani",
                    "ahmed.bennani@dentaluxe.ma",
                    "DentalPass123@",
                    "DentalPass123@",
                    "0612345678"
            );

            UserResponse user = authorizationService.register(request);

            assert user != null : "L'utilisateur ne doit pas être null";
            assert user.getId() != null : "L'ID doit être généré";
            assert "Dr. Ahmed Bennani".equals(user.getUsername()) : "Username incorrect";
            assert "ahmed.bennani@dentaluxe.ma".equals(user.getEmail()) : "Email incorrect";
            assert user.isActive() : "L'utilisateur doit être actif";

            System.out.println("✓ Utilisateur inscrit: " + user.getUsername());
            System.out.println("✓ Email: " + user.getEmail());
            System.out.println("✓ ID: " + user.getId());
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
        System.out.println("TEST 2: Détection d'email déjà existant");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            RegisterRequest duplicateRequest = new RegisterRequest(
                    "Autre Utilisateur",
                    "ahmed.bennani@dentaluxe.ma",
                    "Password123@",
                    "Password123@",
                    "0698765432"
            );

            try {
                authorizationService.register(duplicateRequest);
                System.out.println("✗ TEST ÉCHOUÉ: Exception attendue non levée\n");
                testsFailed++;
            } catch (UserAlreadyExistsException e) {
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

    private static void testCredentialsValidation() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 3: Validation des credentials");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            // Test email
            assert !validator.isValidEmail("email-invalide") : "Email invalide accepté";
            assert !validator.isValidEmail("@example.com") : "Email sans nom accepté";
            assert validator.isValidEmail("valid@example.com") : "Email valide rejeté";

            // Test mot de passe
            assert !validator.isValidPassword("faible") : "Mot de passe faible accepté";
            assert !validator.isValidPassword("12345678") : "Mot de passe sans lettre accepté";
            assert validator.isValidPassword("StrongPass123@") : "Mot de passe fort rejeté";

            // Test nom d'utilisateur
            assert !validator.isValidUsername("AB") : "Nom trop court accepté";
            assert validator.isValidUsername("John Doe") : "Nom valide rejeté";

            System.out.println("✓ Validation d'email: OK");
            System.out.println("✓ Validation de mot de passe: OK");
            System.out.println("✓ Validation de nom d'utilisateur: OK");
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (AssertionError | Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testSuccessfulLogin() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 4: Connexion avec credentials valides");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest loginRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "DentalPass123@"
            );

            LoginResponse response = authorizationService.login(loginRequest);

            assert response != null : "Réponse nulle";
            assert response.getSessionId() != null : "Session non générée";
            assert "ahmed.bennani@dentaluxe.ma".equals(response.getEmail()) : "Email incorrect";

            System.out.println("✓ Connexion réussie pour: " + response.getUsername());
            System.out.println("✓ Session ID: " + response.getSessionId());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testLoginWithWrongPassword() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 5: Connexion avec mot de passe incorrect");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest wrongPasswordRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "WrongPassword123@"
            );

            try {
                authorizationService.login(wrongPasswordRequest);
                System.out.println("✗ TEST ÉCHOUÉ: Exception attendue non levée\n");
                testsFailed++;
            } catch (InvalidCredentialsException e) {
                System.out.println("✓ Mot de passe incorrect détecté");
                System.out.println("✓ Message: " + e.getMessage());
                System.out.println("✓ TEST RÉUSSI\n");
                testsPassed++;
            }
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testLoginWithNonExistentEmail() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 6: Connexion avec email inexistant");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest nonExistentRequest = new LoginRequest(
                    "inexistant@dentaluxe.ma",
                    "Password123@"
            );

            try {
                authorizationService.login(nonExistentRequest);
                System.out.println("✗ TEST ÉCHOUÉ: Exception attendue non levée\n");
                testsFailed++;
            } catch (InvalidCredentialsException e) {
                System.out.println("✓ Email inexistant détecté");
                System.out.println("✓ Message: " + e.getMessage());
                System.out.println("✓ TEST RÉUSSI\n");
                testsPassed++;
            }
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testSessionValidation() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 7: Validation de session");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest loginRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "DentalPass123@"
            );

            LoginResponse response = authorizationService.login(loginRequest);
            String sessionId = response.getSessionId();

            assert authorizationService.validateSession(sessionId) : "Session valide rejetée";
            assert !authorizationService.validateSession("session-invalide") : "Session invalide acceptée";
            assert !authorizationService.validateSession(null) : "Session null acceptée";

            System.out.println("✓ Session active validée: " + sessionId);
            System.out.println("✓ Sessions invalides rejetées");
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testGetUserProfile() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 8: Récupération du profil utilisateur");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            // On récupère l'utilisateur via son email
            LoginRequest loginRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "DentalPass123@"
            );
            LoginResponse loginResponse = authorizationService.login(loginRequest);

            UserResponse profile = userService.getUserProfile(loginResponse.getUserId());

            assert profile != null : "Profil null";
            assert "ahmed.bennani@dentaluxe.ma".equals(profile.getEmail()) : "Email incorrect";

            System.out.println("✓ Profil récupéré: " + profile.getUsername());
            System.out.println("✓ Email: " + profile.getEmail());
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testEmailExistence() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 9: Vérification d'existence d'email");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            assert userService.isEmailTaken("ahmed.bennani@dentaluxe.ma") : "Email existant non détecté";
            assert !userService.isEmailTaken("nouveau@dentaluxe.ma") : "Email disponible marqué comme pris";

            System.out.println("✓ Email existant: ahmed.bennani@dentaluxe.ma");
            System.out.println("✓ Email disponible: nouveau@dentaluxe.ma");
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testPasswordChange() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 10: Changement de mot de passe");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest loginRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "DentalPass123@"
            );
            LoginResponse loginResponse = authorizationService.login(loginRequest);
            Long userId = loginResponse.getUserId();

            ChangePasswordRequest changeRequest = new ChangePasswordRequest(
                    "DentalPass123@",
                    "NewDentalPass456@",
                    "NewDentalPass456@"
            );

            authorizationService.changePassword(userId, changeRequest);

            // Vérifier que l'ancien mot de passe ne fonctionne plus
            try {
                authorizationService.login(new LoginRequest("ahmed.bennani@dentaluxe.ma", "DentalPass123@"));
                System.out.println("✗ TEST ÉCHOUÉ: Ancien mot de passe fonctionne encore\n");
                testsFailed++;
                return;
            } catch (InvalidCredentialsException e) {
                // C'est normal
            }

            // Vérifier que le nouveau mot de passe fonctionne
            LoginResponse newLogin = authorizationService.login(
                    new LoginRequest("ahmed.bennani@dentaluxe.ma", "NewDentalPass456@")
            );

            assert newLogin != null : "Connexion avec nouveau mot de passe échouée";

            System.out.println("✓ Mot de passe changé avec succès");
            System.out.println("✓ Ancien mot de passe invalidé");
            System.out.println("✓ Nouveau mot de passe fonctionnel");
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testPasswordChangeWithWrongOldPassword() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 11: Changement avec ancien mot de passe incorrect");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest loginRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "NewDentalPass456@"
            );
            LoginResponse loginResponse = authorizationService.login(loginRequest);

            ChangePasswordRequest wrongOldPassword = new ChangePasswordRequest(
                    "WrongOldPass123@",
                    "AnotherNewPass123@",
                    "AnotherNewPass123@"
            );

            try {
                authorizationService.changePassword(loginResponse.getUserId(), wrongOldPassword);
                System.out.println("✗ TEST ÉCHOUÉ: Exception attendue non levée\n");
                testsFailed++;
            } catch (InvalidCredentialsException e) {
                System.out.println("✓ Ancien mot de passe incorrect détecté");
                System.out.println("✓ Message: " + e.getMessage());
                System.out.println("✓ TEST RÉUSSI\n");
                testsPassed++;
            }
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testLogout() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 12: Déconnexion");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest loginRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "NewDentalPass456@"
            );

            LoginResponse response = authorizationService.login(loginRequest);
            String sessionId = response.getSessionId();

            assert authorizationService.validateSession(sessionId) : "Session invalide avant déconnexion";

            authorizationService.logout(sessionId);

            assert !authorizationService.validateSession(sessionId) : "Session valide après déconnexion";

            System.out.println("✓ Déconnexion réussie");
            System.out.println("✓ Session invalidée: " + sessionId);
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testPasswordEncoding() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 13: Encodage de mot de passe");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            String rawPassword = "TestPassword123@";

            String encoded1 = passwordEncoder.encode(rawPassword);
            String encoded2 = passwordEncoder.encode(rawPassword);

            assert encoded1 != null : "Encodage null";
            assert !rawPassword.equals(encoded1) : "Mot de passe en clair";
            assert !encoded1.equals(encoded2) : "Encodages identiques";
            assert passwordEncoder.matches(rawPassword, encoded1) : "Correspondance échouée";
            assert !passwordEncoder.matches("WrongPassword", encoded1) : "Mauvais mot de passe accepté";

            System.out.println("✓ Encodage unique avec salt aléatoire");
            System.out.println("✓ Vérification de mot de passe fonctionnelle");
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testRegistrationWithInvalidData() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 14: Inscription avec données invalides");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            int errorsDetected = 0;

            // Email invalide
            try {
                RegisterRequest invalidEmail = new RegisterRequest(
                        "User", "email-invalide", "Password123@", "Password123@", "0612345678"
                );
                authorizationService.register(invalidEmail);
            } catch (AuthException e) {
                errorsDetected++;
            }

            // Mot de passe faible
            try {
                RegisterRequest weakPassword = new RegisterRequest(
                        "User", "user@example.com", "weak", "weak", "0612345678"
                );
                authorizationService.register(weakPassword);
            } catch (AuthException e) {
                errorsDetected++;
            }

            // Mots de passe non correspondants
            try {
                RegisterRequest mismatchPassword = new RegisterRequest(
                        "User", "user2@example.com", "Password123@", "DifferentPass123@", "0612345678"
                );
                authorizationService.register(mismatchPassword);
            } catch (AuthException e) {
                errorsDetected++;
            }

            // Nom d'utilisateur trop court
            try {
                RegisterRequest shortUsername = new RegisterRequest(
                        "AB", "user3@example.com", "Password123@", "Password123@", "0612345678"
                );
                authorizationService.register(shortUsername);
            } catch (AuthException e) {
                errorsDetected++;
            }

            assert errorsDetected == 4 : "Toutes les erreurs n'ont pas été détectées";

            System.out.println("✓ Email invalide rejeté");
            System.out.println("✓ Mot de passe faible rejeté");
            System.out.println("✓ Mots de passe non correspondants rejetés");
            System.out.println("✓ Nom d'utilisateur invalide rejeté");
            System.out.println("✓ TEST RÉUSSI\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void testMultipleSessions() {
        totalTests++;
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("TEST 15: Gestion de sessions multiples");
        System.out.println("─────────────────────────────────────────────────────────────");

        try {
            LoginRequest loginRequest = new LoginRequest(
                    "ahmed.bennani@dentaluxe.ma",
                    "NewDentalPass456@"
            );

            LoginResponse session1 = authorizationService.login(loginRequest);
            LoginResponse session2 = authorizationService.login(loginRequest);
            LoginResponse session3 = authorizationService.login(loginRequest);

            assert !session1.getSessionId().equals(session2.getSessionId()) : "Sessions identiques";
            assert !session2.getSessionId().equals(session3.getSessionId()) : "Sessions identiques";

            assert authorizationService.validateSession(session1.getSessionId()) : "Session 1 invalide";
            assert authorizationService.validateSession(session2.getSessionId()) : "Session 2 invalide";
            assert authorizationService.validateSession(session3.getSessionId()) : "Session 3 invalide";

            authorizationService.logout(session1.getSessionId());

            assert !authorizationService.validateSession(session1.getSessionId()) : "Session 1 encore valide";
            assert authorizationService.validateSession(session2.getSessionId()) : "Session 2 affectée";
            assert authorizationService.validateSession(session3.getSessionId()) : "Session 3 affectée";

            System.out.println("✓ Sessions multiples créées: 3");
            System.out.println("✓ Chaque session est unique");
            System.out.println("✓ Déconnexion sélective fonctionnelle");
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

        double successRate = (testsPassed * 100.0) / totalTests;

        if (testsFailed == 0) {
            System.out.println("║  ✓✓✓ TOUS LES TESTS ONT RÉUSSI ! ✓✓✓                          ║");
            System.out.println("║  Service d'authentification DentalLuxe validé à 100%          ║");
        } else {
            System.out.println("║  Taux de réussite  : " + String.format("%.2f%%", successRate) + "                                      ║");
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
}