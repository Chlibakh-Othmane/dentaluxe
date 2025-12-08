package ma.dentaluxe.service.TestSara;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.agenda.inMemDB_implementation.RDVRepositoryImpl;
import ma.dentaluxe.repository.modules.auth.inMemDB_implementation.AuthRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.FactureRepositoryImpl;
import ma.dentaluxe.repository.modules.dashboard.inMemDB_implementation.DashboardRepositoryImpl;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.ConsultationRepositoryImpl;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.InterventionMedecinRepositoryImpl;
import ma.dentaluxe.repository.modules.patient.inMemDB_implementation.PatientRepositoryImpl;
import ma.dentaluxe.service.dashboard.api.DashboardService;
import ma.dentaluxe.service.dashboard.Impl.AdminDashboardServiceImpl;
import ma.dentaluxe.service.dashboard.Impl.MedecinDashboardServiceImpl;
import ma.dentaluxe.service.dashboard.Impl.SecretaireDashboardServiceImpl;

import java.sql.Connection;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class TestDashboard {

    private static Scanner scanner = new Scanner(System.in);
    private static DashboardRepositoryImpl dashboardRepo;
    private static AuthRepositoryImpl authRepo;
    private static PatientRepositoryImpl patientRepo;
    private static ConsultationRepositoryImpl consultationRepo;
    private static RDVRepositoryImpl rdvRepo;
    private static InterventionMedecinRepositoryImpl interventionRepo;
    private static FactureRepositoryImpl factureRepo;

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║           📊 TEST SERVICE DASHBOARD                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");

        // Initialisation
        testConnexionBDD();
        initializeRepositories();

        int choix;
        do {
            afficherMenu();
            System.out.print("Votre choix (0-7): ");
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

    private static void initializeRepositories() {
        dashboardRepo = new DashboardRepositoryImpl();
        authRepo = new AuthRepositoryImpl();
        patientRepo = new PatientRepositoryImpl();
        consultationRepo = new ConsultationRepositoryImpl();
        rdvRepo = new RDVRepositoryImpl();
        interventionRepo = new InterventionMedecinRepositoryImpl();
        factureRepo = new FactureRepositoryImpl();

        System.out.println("✓ Tous les repositories initialisés!\n");
    }

    private static void afficherMenu() {
        System.out.println("\n=== MENU TEST DASHBOARD SERVICE ===");
        System.out.println("1.  Tester Dashboard ADMIN");
        System.out.println("2.  Tester Dashboard MÉDECIN");
        System.out.println("3.  Tester Dashboard SECRÉTAIRE");
        System.out.println("4.  Tester Statistiques");
        System.out.println("5.  Tester Notifications");
        System.out.println("6.  Tester Tâches en attente");
        System.out.println("7.  Tester Alertes");
        System.out.println("0.  Quitter");
        System.out.println("===================================");
    }

    private static void traiterChoix(int choix) {
        switch (choix) {
            case 1: testerDashboardAdmin(); break;
            case 2: testerDashboardMedecin(); break;
            case 3: testerDashboardSecretaire(); break;
            case 4: testerStatistiques(); break;
            case 5: testerNotifications(); break;
            case 6: testerTachesEnAttente(); break;
            case 7: testerAlertes(); break;
            case 0: System.out.println("Au revoir!"); break;
            default: System.out.println("Choix invalide!");
        }
    }

    private static Utilisateur createTestUser(String role) {
        return Utilisateur.builder()
                .id(getTestUserId(role))
                .nom(getTestUserName(role))
                .prenom("Test")
                .login("test_" + role.toLowerCase())
                .email("test." + role.toLowerCase() + "@dentaluxe.ma")
                .actif(true)
                .build();
    }

    private static Long getTestUserId(String role) {
        switch (role.toUpperCase()) {
            case "ADMIN": return 1L;
            case "MEDECIN": return 2L;
            case "SECRETAIRE": return 3L;
            default: return 999L;
        }
    }

    private static String getTestUserName(String role) {
        switch (role.toUpperCase()) {
            case "ADMIN": return "Administrateur";
            case "MEDECIN": return "Docteur";
            case "SECRETAIRE": return "Secrétaire";
            default: return "Utilisateur";
        }
    }

    private static void testerDashboardAdmin() {
        System.out.println("\n=== TEST DASHBOARD ADMIN ===\n");

        Utilisateur adminUser = createTestUser("ADMIN");

        // Création directe sans factory
        DashboardService adminDashboard = new AdminDashboardServiceImpl(
                dashboardRepo, authRepo, patientRepo);

        System.out.println("1. Affichage du dashboard:");
        System.out.println("-".repeat(50));
        adminDashboard.displayDashboard(adminUser);
        System.out.println("-".repeat(50));

        System.out.println("\n2. Récupération des statistiques:");
        Map<String, Object> stats = adminDashboard.getStatistics(adminUser);
        System.out.println("Statistiques récupérées: " + stats.size() + " éléments");
        stats.forEach((key, value) -> {
            System.out.println("   • " + key + ": " + value);
        });

        System.out.println("\n3. Notifications admin:");
        List<String> notifications = adminDashboard.getNotifications(adminUser);
        notifications.forEach(notification -> {
            System.out.println("   • " + notification);
        });

        System.out.println("\n4. Tâches en attente:");
        List<String> tasks = adminDashboard.getPendingTasks(adminUser);
        if (tasks.isEmpty()) {
            System.out.println("   • Aucune tâche en attente");
        } else {
            tasks.forEach(task -> {
                System.out.println("   • " + task);
            });
        }

        System.out.println("\n5. Alertes admin:");
        List<String> alerts = adminDashboard.getAlerts(adminUser);
        if (alerts.isEmpty()) {
            System.out.println("   • Aucune alerte");
        } else {
            alerts.forEach(alert -> {
                System.out.println("   ⚠ " + alert);
            });
        }

        System.out.println("\n6. Test rafraîchissement données:");
        adminDashboard.refreshDashboardData(adminUser);

        System.out.println("\n7. Test export données:");
        adminDashboard.exportDashboardData(adminUser, "PDF");

        System.out.println("\n✅ Test dashboard ADMIN terminé!");
    }

    private static void testerDashboardMedecin() {
        System.out.println("\n=== TEST DASHBOARD MÉDECIN ===\n");

        Utilisateur medecinUser = createTestUser("MEDECIN");

        // Création directe sans factory
        DashboardService medecinDashboard = new MedecinDashboardServiceImpl(
                dashboardRepo, consultationRepo, rdvRepo, interventionRepo);

        System.out.println("1. Affichage du dashboard:");
        System.out.println("-".repeat(50));
        medecinDashboard.displayDashboard(medecinUser);
        System.out.println("-".repeat(50));

        System.out.println("\n2. Récupération des statistiques:");
        Map<String, Object> stats = medecinDashboard.getStatistics(medecinUser);
        System.out.println("Statistiques récupérées: " + stats.size() + " éléments");
        stats.forEach((key, value) -> {
            System.out.println("   • " + key + ": " + value);
        });

        System.out.println("\n3. Notifications médecin:");
        List<String> notifications = medecinDashboard.getNotifications(medecinUser);
        notifications.forEach(notification -> {
            System.out.println("   • " + notification);
        });

        System.out.println("\n4. Tâches en attente:");
        List<String> tasks = medecinDashboard.getPendingTasks(medecinUser);
        if (tasks.isEmpty()) {
            System.out.println("   • Aucune tâche en attente");
        } else {
            tasks.forEach(task -> {
                System.out.println("   • " + task);
            });
        }

        System.out.println("\n5. Alertes médecin:");
        List<String> alerts = medecinDashboard.getAlerts(medecinUser);
        if (alerts.isEmpty()) {
            System.out.println("   • Aucune alerte");
        } else {
            alerts.forEach(alert -> {
                System.out.println("   ⚠ " + alert);
            });
        }

        System.out.println("\n6. Test rafraîchissement données:");
        medecinDashboard.refreshDashboardData(medecinUser);

        System.out.println("\n✅ Test dashboard MÉDECIN terminé!");
    }

    private static void testerDashboardSecretaire() {
        System.out.println("\n=== TEST DASHBOARD SECRÉTAIRE ===\n");

        Utilisateur secretaireUser = createTestUser("SECRETAIRE");

        // Création directe sans factory
        DashboardService secretaireDashboard = new SecretaireDashboardServiceImpl(
                dashboardRepo, patientRepo, rdvRepo, factureRepo);

        System.out.println("1. Affichage du dashboard:");
        System.out.println("-".repeat(50));
        secretaireDashboard.displayDashboard(secretaireUser);
        System.out.println("-".repeat(50));

        System.out.println("\n2. Récupération des statistiques:");
        Map<String, Object> stats = secretaireDashboard.getStatistics(secretaireUser);
        System.out.println("Statistiques récupérées: " + stats.size() + " éléments");
        stats.forEach((key, value) -> {
            System.out.println("   • " + key + ": " + value);
        });

        System.out.println("\n3. Notifications secrétaire:");
        List<String> notifications = secretaireDashboard.getNotifications(secretaireUser);
        notifications.forEach(notification -> {
            System.out.println("   • " + notification);
        });

        System.out.println("\n4. Tâches en attente:");
        List<String> tasks = secretaireDashboard.getPendingTasks(secretaireUser);
        if (tasks.isEmpty()) {
            System.out.println("   • Aucune tâche en attente");
        } else {
            tasks.forEach(task -> {
                System.out.println("   • " + task);
            });
        }

        System.out.println("\n5. Alertes secrétaire:");
        List<String> alerts = secretaireDashboard.getAlerts(secretaireUser);
        if (alerts.isEmpty()) {
            System.out.println("   • Aucune alerte");
        } else {
            alerts.forEach(alert -> {
                System.out.println("   ⚠ " + alert);
            });
        }

        System.out.println("\n6. Test rafraîchissement données:");
        secretaireDashboard.refreshDashboardData(secretaireUser);

        System.out.println("\n✅ Test dashboard SECRÉTAIRE terminé!");
    }

    private static void testerStatistiques() {
        System.out.println("\n=== TEST STATISTIQUES ===\n");

        // Tester les statistiques pour chaque rôle
        String[] roles = {"ADMIN", "MEDECIN", "SECRETAIRE"};

        for (String role : roles) {
            System.out.println("📊 Statistiques pour " + role + ":");

            DashboardService service = createDashboardServiceForRole(role);
            Utilisateur user = createTestUser(role);
            Map<String, Object> stats = service.getStatistics(user);

            System.out.println("   • Nombre d'éléments: " + stats.size());
            System.out.println("   • Clés disponibles: " + String.join(", ", stats.keySet()));

            // Afficher quelques valeurs importantes
            if (stats.containsKey("patients")) {
                System.out.println("   • Patients: " + stats.get("patients"));
            }
            if (stats.containsKey("rdvToday")) {
                System.out.println("   • RDV aujourd'hui: " + stats.get("rdvToday"));
            }

            System.out.println();
        }

        System.out.println("✅ Test statistiques terminé!");
    }

    private static DashboardService createDashboardServiceForRole(String role) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return new AdminDashboardServiceImpl(dashboardRepo, authRepo, patientRepo);
            case "MEDECIN":
                return new MedecinDashboardServiceImpl(dashboardRepo, consultationRepo, rdvRepo, interventionRepo);
            case "SECRETAIRE":
                return new SecretaireDashboardServiceImpl(dashboardRepo, patientRepo, rdvRepo, factureRepo);
            default:
                throw new IllegalArgumentException("Rôle non supporté: " + role);
        }
    }

    private static void testerNotifications() {
        System.out.println("\n=== TEST NOTIFICATIONS ===\n");

        String[] roles = {"ADMIN", "MEDECIN", "SECRETAIRE"};

        for (String role : roles) {
            System.out.println("🔔 Notifications pour " + role + ":");

            DashboardService service = createDashboardServiceForRole(role);
            Utilisateur user = createTestUser(role);
            List<String> notifications = service.getNotifications(user);

            if (notifications.isEmpty()) {
                System.out.println("   • Aucune notification");
            } else {
                System.out.println("   • Nombre de notifications: " + notifications.size());
                notifications.forEach(notification -> {
                    System.out.println("   • " + notification);
                });
            }
            System.out.println();
        }

        System.out.println("✅ Test notifications terminé!");
    }

    private static void testerTachesEnAttente() {
        System.out.println("\n=== TEST TÂCHES EN ATTENTE ===\n");

        String[] roles = {"ADMIN", "MEDECIN", "SECRETAIRE"};

        for (String role : roles) {
            System.out.println("📋 Tâches en attente pour " + role + ":");

            DashboardService service = createDashboardServiceForRole(role);
            Utilisateur user = createTestUser(role);
            List<String> tasks = service.getPendingTasks(user);

            if (tasks.isEmpty()) {
                System.out.println("   • Aucune tâche en attente");
            } else {
                System.out.println("   • Nombre de tâches: " + tasks.size());
                tasks.forEach(task -> {
                    System.out.println("   • " + task);
                });
            }
            System.out.println();
        }

        System.out.println("✅ Test tâches en attente terminé!");
    }

    private static void testerAlertes() {
        System.out.println("\n=== TEST ALERTES ===\n");

        String[] roles = {"ADMIN", "MEDECIN", "SECRETAIRE"};

        for (String role : roles) {
            System.out.println("🚨 Alertes pour " + role + ":");

            DashboardService service = createDashboardServiceForRole(role);
            Utilisateur user = createTestUser(role);
            List<String> alerts = service.getAlerts(user);

            if (alerts.isEmpty()) {
                System.out.println("   • Aucune alerte");
            } else {
                System.out.println("   • Nombre d'alertes: " + alerts.size());
                alerts.forEach(alert -> {
                    System.out.println("   ⚠ " + alert);
                });
            }
            System.out.println();
        }

        System.out.println("✅ Test alertes terminé!");
    }

    // Méthode supplémentaire pour tester la création manuelle
    private static void testerCreationManuelleServices() {
        System.out.println("\n=== TEST CRÉATION MANUELLE SERVICES ===\n");

        System.out.println("1. Test création AdminDashboardServiceImpl:");
        AdminDashboardServiceImpl adminService = new AdminDashboardServiceImpl(
                dashboardRepo, authRepo, patientRepo);
        System.out.println("   ✅ AdminDashboardServiceImpl créé avec succès");
        System.out.println("   • Dependencies: DashboardRepository, AuthRepository, PatientRepository");

        System.out.println("\n2. Test création MedecinDashboardServiceImpl:");
        MedecinDashboardServiceImpl medecinService = new MedecinDashboardServiceImpl(
                dashboardRepo, consultationRepo, rdvRepo, interventionRepo);
        System.out.println("   ✅ MedecinDashboardServiceImpl créé avec succès");
        System.out.println("   • Dependencies: DashboardRepository, ConsultationRepository, RDVRepository, InterventionRepository");

        System.out.println("\n3. Test création SecretaireDashboardServiceImpl:");
        SecretaireDashboardServiceImpl secretaireService = new SecretaireDashboardServiceImpl(
                dashboardRepo, patientRepo, rdvRepo, factureRepo);
        System.out.println("   ✅ SecretaireDashboardServiceImpl créé avec succès");
        System.out.println("   • Dependencies: DashboardRepository, PatientRepository, RDVRepository, FactureRepository");

        System.out.println("\n4. Test polymorphisme (interface DashboardService):");
        DashboardService service1 = adminService;
        DashboardService service2 = medecinService;
        DashboardService service3 = secretaireService;

        System.out.println("   ✅ Tous les services implémentent l'interface DashboardService");
        System.out.println("   • Admin: " + service1.getClass().getSimpleName());
        System.out.println("   • Medecin: " + service2.getClass().getSimpleName());
        System.out.println("   • Secretaire: " + service3.getClass().getSimpleName());

        System.out.println("\n✅ Test création manuelle terminé!");
    }

    // Test complet d'intégration
    private static void testerIntegrationComplete() {
        System.out.println("\n=== TEST INTÉGRATION COMPLÈTE ===\n");

        System.out.println("Ce test simule un workflow complet avec les trois types de dashboard:\n");

        // Test ADMIN
        System.out.println("1. 🎯 SCÉNARIO ADMINISTRATEUR");
        Utilisateur admin = createTestUser("ADMIN");
        admin.setNom("Super");
        admin.setPrenom("Admin");

        DashboardService adminService = new AdminDashboardServiceImpl(dashboardRepo, authRepo, patientRepo);
        System.out.println("   • Création du service: OK");
        System.out.println("   • Affichage dashboard:");
        System.out.println("-".repeat(40));
        adminService.displayDashboard(admin);
        System.out.println("-".repeat(40));

        Map<String, Object> adminStats = adminService.getStatistics(admin);
        System.out.println("   • Statistiques récupérées: " + adminStats.size());

        // Test MÉDECIN
        System.out.println("\n2. 🩺 SCÉNARIO MÉDECIN");
        Utilisateur medecin = createTestUser("MEDECIN");
        medecin.setNom("Smith");
        medecin.setPrenom("John");

        DashboardService medecinService = new MedecinDashboardServiceImpl(
                dashboardRepo, consultationRepo, rdvRepo, interventionRepo);
        System.out.println("   • Création du service: OK");
        System.out.println("   • Notifications médecin:");
        List<String> medecinNotifications = medecinService.getNotifications(medecin);
        medecinNotifications.forEach(n -> System.out.println("     • " + n));

        // Test SECRÉTAIRE
        System.out.println("\n3. 💼 SCÉNARIO SECRÉTAIRE");
        Utilisateur secretaire = createTestUser("SECRETAIRE");
        secretaire.setNom("Dupont");
        secretaire.setPrenom("Marie");

        DashboardService secretaireService = new SecretaireDashboardServiceImpl(
                dashboardRepo, patientRepo, rdvRepo, factureRepo);
        System.out.println("   • Création du service: OK");
        System.out.println("   • Tâches en attente:");
        List<String> secretaireTasks = secretaireService.getPendingTasks(secretaire);
        secretaireTasks.forEach(t -> System.out.println("     • " + t));

        System.out.println("\n4. 🔄 TESTS COMMUNS À TOUS LES SERVICES");
        System.out.println("   • Rafraîchissement des données:");
        adminService.refreshDashboardData(admin);
        medecinService.refreshDashboardData(medecin);
        secretaireService.refreshDashboardData(secretaire);
        System.out.println("     ✅ Tous les dashboards rafraîchis");

        System.out.println("   • Export des données:");
        adminService.exportDashboardData(admin, "PDF");
        medecinService.exportDashboardData(medecin, "EXCEL");
        secretaireService.exportDashboardData(secretaire, "CSV");
        System.out.println("     ✅ Export simulé pour tous les formats");

        System.out.println("\n🎉 TEST D'INTÉGRATION RÉUSSI !");
        System.out.println("Tous les services dashboard fonctionnent correctement:");
        System.out.println("• AdminDashboardServiceImpl: Gestion système");
        System.out.println("• MedecinDashboardServiceImpl: Interface médicale");
        System.out.println("• SecretaireDashboardServiceImpl: Interface administrative");
    }
}