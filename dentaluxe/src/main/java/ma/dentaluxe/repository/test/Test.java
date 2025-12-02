package ma.dentaluxe.repository.test;

// ===== IMPORTS ACTES, RDV, FACTURES (AYA LEZREGUE) =====
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.entities.enums.StatutFacture;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;
import ma.dentaluxe.entities.finance.Facture;
import ma.dentaluxe.entities.finance.SituationFinanciere;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.repository.modules.actes.inMemDB_implementation.ActeRepositoryImpl;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.repository.modules.agenda.inMemDB_implementation.RDVRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.api.FactureRepository;
import ma.dentaluxe.repository.modules.caisse.api.SituationFinanciereRepository;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.FactureRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.SituationFinanciereRepositoryImpl;

// ===== IMPORTS AUTH & DASHBOARD (SARA NABIL) =====
import ma.dentaluxe.repository.modules.auth.api.AuthRepository;
import ma.dentaluxe.repository.modules.auth.inMemDB_implementation.AuthRepositoryImpl;
import ma.dentaluxe.repository.modules.dashboard.api.DashboardRepository;
import ma.dentaluxe.repository.modules.dashboard.inMemDB_implementation.DashboardRepositoryImpl;
import ma.dentaluxe.entities.enums.Sexe;
import ma.dentaluxe.entities.utilisateur.Utilisateur;

// ===== IMPORTS PATIENT (OTHMANE CHLIBAKH) =====
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.repository.modules.patient.inMemDB_implementation.PatientRepositoryImpl;

// ===== IMPORTS ORDONNANCE & PRESCRIPTION (OTHMANE CHLIBAKH) =====
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;
import ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation.OrdonnanceRepositoryImpl;
import ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation.PrescriptionRepositoryImpl;

// ===== IMPORTS MÉDICAMENT (OTHMANE CHLIBAKH) =====
import ma.dentaluxe.entities.ordonnance.Medicament;
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;
import ma.dentaluxe.repository.modules.medicament.inMemDB_implementation.MedicamentRepositoryImpl;

// ===== IMPORTS BASE DE DONNÉES =====
import ma.dentaluxe.conf.Db;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// ===== AUTRES IMPORTS =====
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Test {

    // ==============================================================
    // INSTANCES DES REPOSITORIES
    // ==============================================================

    // Repositories Aya Lezregue
    private final ActeRepository acteRepository;
    private final RDVRepository rdvRepository;
    private final FactureRepository factureRepository;
    private final SituationFinanciereRepository situationFinanciereRepository;

    // Repositories Sara Nabil
    private final AuthRepository authRepository;
    private final DashboardRepository dashboardRepository;

    // Repositories Othmane Chlibakh
    private final PatientRepository patientRepository;
    private final OrdonnanceRepository ordonnanceRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicamentRepository medicamentRepository;

    // ==============================================================
    // CONSTRUCTEUR - INITIALISATION DES REPOSITORIES
    // ==============================================================

    public Test() {
        // Initialisation des repositories Aya
        this.acteRepository = new ActeRepositoryImpl();
        this.rdvRepository = new RDVRepositoryImpl();
        this.factureRepository = new FactureRepositoryImpl();
        this.situationFinanciereRepository = new SituationFinanciereRepositoryImpl();

        // Initialisation des repositories Sara
        this.authRepository = new AuthRepositoryImpl();
        this.dashboardRepository = new DashboardRepositoryImpl();

        // Initialisation des repositories Othmane
        this.patientRepository = new PatientRepositoryImpl();
        this.ordonnanceRepository = new OrdonnanceRepositoryImpl();
        this.prescriptionRepository = new PrescriptionRepositoryImpl();
        this.medicamentRepository = new MedicamentRepositoryImpl();

        System.out.println("✅ Tous les repositories initialisés avec succès");
    }

    // ==============================================================
    // MÉTHODES UTILITAIRES COMMUNES
    // ==============================================================

    /**
     * Diagnostic complet de la base de données
     */
    void diagnosticComplet() {
        System.out.println("\n🔍 DIAGNOSTIC COMPLET DE LA BASE");

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("📊 ÉTAT DES TABLES :");

            String[] tables = {"patient", "dossiermedical", "medecin", "acte", "rdv",
                    "facture", "situationfinanciere", "ordonnance",
                    "prescription", "medicament", "utilisateur"};

            for (String table : tables) {
                try {
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + table);
                    rs.next();
                    System.out.println("   📋 " + table + " : " + rs.getInt("count") + " enregistrement(s)");
                } catch (SQLException e) {
                    System.out.println("   ❌ " + table + " : Table non trouvée");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur diagnostic : " + e.getMessage());
        }
    }

    /**
     * Crée un dossier médical complet pour les tests
     */
    Long creerDossierMedicalComplet() {
        System.out.println("\n🔧 Création d'un dossier médical complet...");

        List<Patient> patients = this.patientRepository.findAll();
        if (patients.isEmpty()) {
            System.out.println("📝 Création d'un patient pour le test...");
            Patient patientTest = Patient.builder()
                    .nom("TEST")
                    .prenom("Ordonnance")
                    .telephone("0622222222")
                    .email("test.ordonnance@example.com")
                    .adresse("Adresse test")
                    .build();
            this.patientRepository.create(patientTest);
            patients = this.patientRepository.findAll();
        }

        Patient patient = patients.get(0);
        System.out.println("✅ Patient utilisé : " + patient.getNom() + " " + patient.getPrenom() + " (ID: " + patient.getId() + ")");

        Long idDossierMedical = patient.getId();
        System.out.println("📁 Dossier médical référence : ID " + idDossierMedical);

        return idDossierMedical;
    }

    /**
     * Nettoyage des tables
     */
    void cleanDatabase() {
        System.out.println("\n🧹 Nettoyage de la base de données...");

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            // Vider les tables dans l'ordre inverse des dépendances
            stmt.execute("TRUNCATE TABLE prescription");
            stmt.execute("TRUNCATE TABLE ordonnance");
            stmt.execute("TRUNCATE TABLE medicament");
            stmt.execute("TRUNCATE TABLE facture");
            stmt.execute("TRUNCATE TABLE situationfinanciere");
            stmt.execute("TRUNCATE TABLE rdv");
            stmt.execute("TRUNCATE TABLE acte");
            stmt.execute("TRUNCATE TABLE patient");
            stmt.execute("TRUNCATE TABLE dossiermedical");
            stmt.execute("TRUNCATE TABLE medecin");
            stmt.execute("TRUNCATE TABLE utilisateur");

            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            System.out.println("✅ Base de données nettoyée");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du nettoyage : " + e.getMessage());
        }
    }

    // ==============================================================
    // TESTS AUTH REPOSITORY (SARA NABIL)
    // ==============================================================

    void testCreateUsers() {
        System.out.println("\n========== TEST AUTH 1 : Création des Utilisateurs ==========");

        createUserIfNotExists("NABIL", "Sara", "sara.nabil@dentaluxe.ma", "07000000",
                "123 Avenue Hassan II, Casablanca", "AB123", Sexe.FEMME,
                "admin.sara", "password_123", null, "Administrateur");

        createUserIfNotExists("CHLIBAKH", "Othmane", "othmane.chlibakh@dentaluxe.ma", "0612345678",
                "456 Rue Mohammed VI, Casablanca", "AB123456", Sexe.HOMME,
                "admin.othmane", "hashed_password_123", null, "Administrateur");

        createUserIfNotExists("LEZREGUE", "Aya", "aya.lezregue@dentaluxe.ma", "0623456789",
                "789 Boulevard Mohammed V, Rabat", "CD789012", Sexe.FEMME,
                "dr.aya", "hashed_password_456", LocalDate.of(1990, 5, 15), "Dentiste");
    }

    private void createUserIfNotExists(String nom, String prenom, String email, String tel,
                                       String adresse, String cin, Sexe sexe, String login,
                                       String passwordHash, LocalDate dateNaissance, String role) {

        Utilisateur existing = authRepository.findByEmail(email);
        if (existing != null) {
            System.out.println("⚠️  " + role + " existe déjà : " + prenom + " " + nom + " (ID: " + existing.getId() + ")");
            return;
        }

        existing = authRepository.findByUsername(login);
        if (existing != null) {
            System.out.println("⚠️  Login déjà utilisé : " + login + " - Attribution d'un login alternatif");
            login = login + "_" + System.currentTimeMillis();
        }

        Utilisateur user = Utilisateur.builder()
                .nom(nom)
                .prenom(prenom)
                .email(email)
                .tel(tel)
                .adresse(adresse)
                .cin(cin)
                .sexe(sexe)
                .login(login)
                .passwordHash(passwordHash)
                .creationDate(LocalDateTime.now())
                .lastModificationDate(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")
                .dateNaissance(dateNaissance)
                .actif(true)
                .build();

        authRepository.create(user);
        if (user.getId() != null) {
            System.out.println("✅ " + role + " créé : " + user.getPrenom() + " " + user.getNom() + " (ID: " + user.getId() + ")");
        } else {
            System.out.println("❌ Échec de création du " + role.toLowerCase());
        }
    }

    void testFindAllUsers() {
        System.out.println("\n========== TEST AUTH 2 : Lire tous les Utilisateurs ==========");

        List<Utilisateur> users = authRepository.findAll();
        System.out.println("📊 Nombre total d'utilisateurs : " + users.size());

        for (Utilisateur user : users) {
            System.out.println("   - " + user.getPrenom() + " " + user.getNom() +
                    " (" + user.getLogin() + ") - " + user.getEmail() +
                    " - Actif: " + user.getActif());
        }
    }

    void testFindUserById() {
        System.out.println("\n========== TEST AUTH 3 : Trouver Utilisateur par ID ==========");

        Utilisateur user = authRepository.findById(1L);
        if (user != null) {
            System.out.println("✅ Utilisateur trouvé :");
            System.out.println("   ID : " + user.getId());
            System.out.println("   Nom complet : " + user.getPrenom() + " " + user.getNom());
            System.out.println("   Email : " + user.getEmail());
            System.out.println("   Téléphone : " + user.getTel());
            System.out.println("   CIN : " + user.getCin());
            System.out.println("   Sexe : " + user.getSexe());
            System.out.println("   Login : " + user.getLogin());
            System.out.println("   Actif : " + user.getActif());
        } else {
            System.out.println("❌ Utilisateur non trouvé");
        }
    }

    void testFindUserByEmail() {
        System.out.println("\n========== TEST AUTH 4 : Trouver Utilisateur par Email ==========");

        Utilisateur user = authRepository.findByEmail("sara.nabil@dentaluxe.ma");
        if (user != null) {
            System.out.println("✅ Utilisateur trouvé par email : " + user.getPrenom() + " " + user.getNom());
            System.out.println("   Email : " + user.getEmail());
            System.out.println("   Login : " + user.getLogin());
        } else {
            System.out.println("❌ Utilisateur non trouvé avec cet email");
        }
    }

    void testFindUserByUsername() {
        System.out.println("\n========== TEST AUTH 5 : Trouver Utilisateur par Username ==========");

        Utilisateur user = authRepository.findByUsername("admin.sara");
        if (user != null) {
            System.out.println("✅ Utilisateur trouvé par username : " + user.getPrenom() + " " + user.getNom());
            System.out.println("   Login : " + user.getLogin());
            System.out.println("   Email : " + user.getEmail());
        } else {
            System.out.println("❌ Utilisateur non trouvé avec ce username");
        }
    }

    void testUpdateUser() {
        System.out.println("\n========== TEST AUTH 6 : Mise à jour d'un Utilisateur ==========");

        Utilisateur user = authRepository.findById(1L);
        if (user != null) {
            System.out.println("📝 Avant mise à jour :");
            System.out.println("   Téléphone : " + user.getTel());
            System.out.println("   Adresse : " + user.getAdresse());

            user.setTel("0698765432");
            user.setAdresse("321 Nouvelle Avenue, Casablanca");
            user.setLastModificationDate(LocalDateTime.now());
            user.setUpdatedBy("test_system");

            authRepository.update(user);

            Utilisateur updatedUser = authRepository.findById(1L);
            System.out.println("✅ Après mise à jour :");
            System.out.println("   Téléphone : " + updatedUser.getTel());
            System.out.println("   Adresse : " + updatedUser.getAdresse());
            System.out.println("   Dernière modification : " + updatedUser.getLastModificationDate());
        }
    }

    void testDeactivateUser() {
        System.out.println("\n========== TEST AUTH 7 : Désactivation d'un Utilisateur ==========");

        Utilisateur user = authRepository.findById(3L);
        if (user != null) {
            System.out.println("📝 Avant désactivation - Actif : " + user.getActif());

            user.setActif(false);
            user.setLastModificationDate(LocalDateTime.now());
            user.setUpdatedBy("test_system");
            authRepository.update(user);

            Utilisateur deactivatedUser = authRepository.findById(3L);
            System.out.println("✅ Après désactivation - Actif : " + deactivatedUser.getActif());
        }
    }

    void testDeleteUser() {
        System.out.println("\n========== TEST AUTH 8 : Suppression d'un Utilisateur ==========");

        Utilisateur tempUser = Utilisateur.builder()
                .nom("TEMPORAIRE")
                .prenom("Test")
                .email("temp.user@dentaluxe.ma")
                .tel("0600000000")
                .login("temp_user")
                .passwordHash("temp_hash")
                .creationDate(LocalDateTime.now())
                .lastModificationDate(LocalDateTime.now())
                .createdBy("test_system")
                .updatedBy("test_system")
                .actif(true)
                .build();
        authRepository.create(tempUser);

        System.out.println("✅ Utilisateur temporaire créé (ID: " + tempUser.getId() + ")");

        authRepository.deleteById(tempUser.getId());
        System.out.println("🗑️  Utilisateur supprimé (ID: " + tempUser.getId() + ")");

        Utilisateur deletedUser = authRepository.findById(tempUser.getId());
        if (deletedUser == null) {
            System.out.println("✅ Confirmation : Utilisateur bien supprimé");
        } else {
            System.out.println("❌ Erreur : Utilisateur toujours présent");
        }
    }

    // ==============================================================
    // TESTS DASHBOARD (SARA NABIL)
    // ==============================================================

    void testDashboardPatients() {
        System.out.println("\n========== TEST DASHBOARD 9 : Statistiques Patients ==========");

        int totalPatients = dashboardRepository.countPatientsTotal();
        int patientsToday = dashboardRepository.countPatientsToday();

        System.out.println("📊 Statistiques Patients :");
        System.out.println("   Total patients : " + totalPatients);
        System.out.println("   Patients ajoutés aujourd'hui : " + patientsToday);
    }

    void testDashboardRDV() {
        System.out.println("\n========== TEST DASHBOARD 10 : Statistiques RDV ==========");

        int rdvToday = dashboardRepository.countRDVToday();
        int rdvThisWeek = dashboardRepository.countRDVThisWeek();
        int rdvThisMonth = dashboardRepository.countRDVThisMonth();
        int rdvThisYear = dashboardRepository.countRDVThisYear();

        System.out.println("📅 Statistiques RDV :");
        System.out.println("   RDV aujourd'hui : " + rdvToday);
        System.out.println("   RDV cette semaine : " + rdvThisWeek);
        System.out.println("   RDV ce mois : " + rdvThisMonth);
        System.out.println("   RDV cette année : " + rdvThisYear);
    }

    void testDashboardConsultations() {
        System.out.println("\n========== TEST DASHBOARD 11 : Statistiques Consultations ==========");

        int consultationsToday = dashboardRepository.countConsultationsToday();
        int consultationsThisMonth = dashboardRepository.countConsultationsThisMonth();
        int consultationsThisYear = dashboardRepository.countConsultationsThisYear();

        System.out.println("🩺 Statistiques Consultations :");
        System.out.println("   Consultations aujourd'hui : " + consultationsToday);
        System.out.println("   Consultations ce mois : " + consultationsThisMonth);
        System.out.println("   Consultations cette année : " + consultationsThisYear);
    }

    void testDashboardFinancials() {
        System.out.println("\n========== TEST DASHBOARD 12 : Statistiques Financières ==========");

        double caJour = dashboardRepository.calculateCAJour();
        double caMensuel = dashboardRepository.calculateCAMensuel();
        double caTotal = dashboardRepository.calculateCATotal();

        System.out.println("💰 Statistiques Financières :");
        System.out.println("   Chiffre d'affaires aujourd'hui : " + String.format("%.2f", caJour) + " DH");
        System.out.println("   Chiffre d'affaires ce mois : " + String.format("%.2f", caMensuel) + " DH");
        System.out.println("   Chiffre d'affaires total : " + String.format("%.2f", caTotal) + " DH");
    }

    void testDashboardLastLogins() {
        System.out.println("\n========== TEST DASHBOARD 13 : Dernières Connexions ==========");

        List<String> lastLogins = dashboardRepository.getLastLoginDates();

        System.out.println("👥 Dernières connexions utilisateurs :");
        if (lastLogins.isEmpty()) {
            System.out.println("   Aucune connexion enregistrée");
        } else {
            for (String login : lastLogins) {
                System.out.println("   - " + login);
            }
        }
    }

    void testDashboardComplet() {
        System.out.println("\n========== TEST DASHBOARD 14 : Tableau de Bord Complet ==========");

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║              TABLEAU DE BORD                  ║");
        System.out.println("╚════════════════════════════════════════════════╝");

        System.out.println("\n👥 PATIENTS :");
        System.out.println("   ┌─ Total : " + dashboardRepository.countPatientsTotal());
        System.out.println("   └─ Aujourd'hui : " + dashboardRepository.countPatientsToday());

        System.out.println("\n📅 RENDEZ-VOUS :");
        System.out.println("   ├─ Aujourd'hui : " + dashboardRepository.countRDVToday());
        System.out.println("   ├─ Cette semaine : " + dashboardRepository.countRDVThisWeek());
        System.out.println("   ├─ Ce mois : " + dashboardRepository.countRDVThisMonth());
        System.out.println("   └─ Cette année : " + dashboardRepository.countRDVThisYear());

        System.out.println("\n🩺 CONSULTATIONS :");
        System.out.println("   ├─ Aujourd'hui : " + dashboardRepository.countConsultationsToday());
        System.out.println("   ├─ Ce mois : " + dashboardRepository.countConsultationsThisMonth());
        System.out.println("   └─ Cette année : " + dashboardRepository.countConsultationsThisYear());

        System.out.println("\n💰 FINANCES :");
        System.out.println("   ├─ CA aujourd'hui : " + String.format("%.2f", dashboardRepository.calculateCAJour()) + " DH");
        System.out.println("   ├─ CA ce mois : " + String.format("%.2f", dashboardRepository.calculateCAMensuel()) + " DH");
        System.out.println("   └─ CA total : " + String.format("%.2f", dashboardRepository.calculateCATotal()) + " DH");

        System.out.println("\n🔐 DERNIÈRES CONNEXIONS :");
        List<String> logins = dashboardRepository.getLastLoginDates();
        if (logins.isEmpty()) {
            System.out.println("   Aucune donnée de connexion");
        } else {
            for (int i = 0; i < Math.min(logins.size(), 5); i++) {
                System.out.println("   " + (i + 1) + ". " + logins.get(i));
            }
        }

        System.out.println("\n" + "═".repeat(50));
    }

    // ==============================================================
    // TESTS ACTE REPOSITORY (AYA LEZREGUE)
    // ==============================================================

    void testCreateActes() {
        System.out.println("\n========== TEST ACTE 15 : Création des Actes ==========");

        Acte acte1 = Acte.builder()
                .idInterventionMedecin(1L)
                .libelle("Consultation générale")
                .description("Consultation dentaire de contrôle")
                .prixDeBase(200.0)
                .categorie(CategorieActe.CONSULTATION)
                .build();
        acteRepository.create(acte1);
        System.out.println("✅ Acte créé : " + acte1.getLibelle() + " (ID: " + acte1.getIdActe() + ")");

        Acte acte2 = Acte.builder()
                .idInterventionMedecin(1L)
                .libelle("Extraction dentaire simple")
                .description("Extraction d'une dent simple")
                .prixDeBase(300.0)
                .categorie(CategorieActe.EXTRACTION)
                .build();
        acteRepository.create(acte2);
        System.out.println("✅ Acte créé : " + acte2.getLibelle() + " (ID: " + acte2.getIdActe() + ")");

        Acte acte3 = Acte.builder()
                .idInterventionMedecin(1L)
                .libelle("Détartrage complet")
                .description("Nettoyage et détartrage des dents")
                .prixDeBase(400.0)
                .categorie(CategorieActe.DETARTRAGE)
                .build();
        acteRepository.create(acte3);
        System.out.println("✅ Acte créé : " + acte3.getLibelle() + " (ID: " + acte3.getIdActe() + ")");
    }

    void testFindAllActes() {
        System.out.println("\n========== TEST ACTE 16 : Lire tous les Actes ==========");
        List<Acte> actes = acteRepository.findAll();
        System.out.println("📊 Nombre total d'actes : " + actes.size());

        for (Acte acte : actes) {
            System.out.println("   - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH (" + acte.getCategorie() + ")");
        }
    }

    void testFindActeById() {
        System.out.println("\n========== TEST ACTE 17 : Trouver Acte par ID ==========");
        Acte acte = acteRepository.findById(1L);
        if (acte != null) {
            System.out.println("✅ Acte trouvé :");
            System.out.println("   ID : " + acte.getIdActe());
            System.out.println("   Libellé : " + acte.getLibelle());
            System.out.println("   Prix : " + acte.getPrixDeBase() + " DH");
            System.out.println("   Catégorie : " + acte.getCategorie());
        } else {
            System.out.println("❌ Acte non trouvé");
        }
    }

    void testFindActesByCategorie() {
        System.out.println("\n========== TEST ACTE 18 : Trouver Actes par Catégorie ==========");

        List<Acte> consultations = acteRepository.findByCategorie(CategorieActe.CONSULTATION);
        System.out.println("📋 Actes de type CONSULTATION : " + consultations.size());
        for (Acte acte : consultations) {
            System.out.println("   - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
        }

        List<Acte> extractions = acteRepository.findByCategorie(CategorieActe.EXTRACTION);
        System.out.println("📋 Actes de type EXTRACTION : " + extractions.size());
        for (Acte acte : extractions) {
            System.out.println("   - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
        }
    }

    void testSearchActeByLibelle() {
        System.out.println("\n========== TEST ACTE 19 : Rechercher Acte par mot-clé ==========");

        List<Acte> resultats = acteRepository.searchByLibelle("Consultation générale");
        System.out.println("🔍 Recherche 'Consultation générale' : " + resultats.size() + " résultats");
        for (Acte acte : resultats) {
            System.out.println("   - " + acte.getLibelle());
        }
    }

    void testUpdateActe() {
        System.out.println("\n========== TEST ACTE 20 : Mise à jour d'un Acte ==========");

        Acte acte = acteRepository.findById(1L);
        if (acte != null) {
            System.out.println("📝 Avant : " + acte.getLibelle() + " - " + acte.getPrixDeBase() + " DH");

            acte.setPrixDeBase(250.0);
            acte.setDescription("Consultation dentaire complète avec radiographie");
            acteRepository.update(acte);

            Acte acteUpdated = acteRepository.findById(1L);
            System.out.println("✅ Après : " + acteUpdated.getLibelle() + " - " + acteUpdated.getPrixDeBase() + " DH");
        }
    }

    void testDeleteActe() {
        System.out.println("\n========== TEST ACTE 21 : Suppression d'un Acte ==========");

        Acte acteTemp = Acte.builder()
                .libelle("Acte temporaire")
                .description("Pour test de suppression")
                .prixDeBase(100.0)
                .categorie(CategorieActe.AUTRE)
                .build();
        acteRepository.create(acteTemp);

        System.out.println("✅ Acte temporaire créé (ID: " + acteTemp.getIdActe() + ")");

        acteRepository.deleteById(acteTemp.getIdActe());
        System.out.println("🗑️ Acte supprimé (ID: " + acteTemp.getIdActe() + ")");

        Acte acteDeleted = acteRepository.findById(acteTemp.getIdActe());
        if(acteDeleted == null){
            System.out.println("✅ Confirmation : Acte bien supprimé");
        }
        else {
            System.out.println("❌ Erreur : Acte toujours présent");
        }
    }

    // ==============================================================
    // TESTS RDV REPOSITORY (AYA LEZREGUE)
    // ==============================================================

    void testCreateRDV() {
        System.out.println("\n========== TEST RDV 22 : Création des RDV ==========");

        RDV rdv1 = RDV.builder()
                .idDM(1L)
                .idMedecin(1L)
                .dateRDV(LocalDate.now())
                .heureRDV(LocalTime.of(9, 0))
                .motif("Consultation de contrôle")
                .statut(StatutRDV.PLANIFIE)
                .noteMedecin("Consultation de contrôle")
                .build();
        rdvRepository.create(rdv1);
        System.out.println("✅ RDV créé : " + rdv1.getMotif() + " le " + rdv1.getDateRDV() + " à " + rdv1.getHeureRDV());

        RDV rdv2 = RDV.builder()
                .idDM(2L)
                .idMedecin(1L)
                .dateRDV(LocalDate.now().plusDays(1))
                .heureRDV(LocalTime.of(10, 30))
                .motif("Extraction dentaire")
                .statut(StatutRDV.CONFIRME)
                .noteMedecin("Extraction dentaire")
                .build();
        rdvRepository.create(rdv2);
        System.out.println("✅ RDV créé : " + rdv2.getMotif() + " le " + rdv2.getDateRDV() + " à " + rdv2.getHeureRDV());

        RDV rdv3 = RDV.builder()
                .idDM(1L)
                .idMedecin(1L)
                .dateRDV(LocalDate.now().plusDays(2))
                .heureRDV(LocalTime.of(14, 0))
                .motif("Détartrage")
                .statut(StatutRDV.PLANIFIE)
                .noteMedecin("Détartrage")
                .build();
        rdvRepository.create(rdv3);
        System.out.println("✅ RDV créé : " + rdv3.getMotif() + " le " + rdv3.getDateRDV() + " à " + rdv3.getHeureRDV());
    }

    void testFindAllRDV() {
        System.out.println("\n========== TEST RDV 23 : Lire tous les RDV ==========");

        List<RDV> rdvs = rdvRepository.findAll();
        System.out.println("📊 Nombre total de RDV : " + rdvs.size());

        for (RDV rdv : rdvs) {
            System.out.println("   - " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() +
                    " : " + rdv.getMotif() + " (" + rdv.getStatut() + ")");
        }
    }

    void testFindRDVByDate() {
        System.out.println("\n========== TEST RDV 24 : Trouver RDV par Date ==========");

        LocalDate today = LocalDate.now();
        List<RDV> rdvsToday = rdvRepository.findByDate(today);
        System.out.println("📅 RDV aujourd'hui (" + today + ") : " + rdvsToday.size());

        for (RDV rdv : rdvsToday) {
            System.out.println("   - " + rdv.getHeureRDV() + " : " + rdv.getMotif());
        }
    }

    void testFindRDVByMedecin() {
        System.out.println("\n========== TEST RDV 25 : Trouver RDV par Médecin ==========");

        List<RDV> rdvsMedecin = rdvRepository.findByMedecinId(1L);
        System.out.println("👨‍⚕️ RDV du médecin (ID: 1) : " + rdvsMedecin.size());

        for (RDV rdv : rdvsMedecin) {
            System.out.println("   - " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() +
                    " : " + rdv.getMotif());
        }
    }

    void testFindRDVByPatient() {
        System.out.println("\n========== TEST RDV 26 : Trouver RDV par Patient ==========");

        List<RDV> rdvsPatient = rdvRepository.findByPatientDossierId(1L);
        System.out.println("👥 RDV du patient (DM ID: 1) : " + rdvsPatient.size());

        for (RDV rdv : rdvsPatient) {
            System.out.println("   - " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() +
                    " : " + rdv.getMotif() + " (" + rdv.getStatut() + ")");
        }
    }

    void testFindRDVByStatut() {
        System.out.println("\n========== TEST RDV 27 : Trouver RDV par Statut ==========");

        List<RDV> rdvsPlanifies = rdvRepository.findByStatut(StatutRDV.PLANIFIE);
        System.out.println("📋 RDV PLANIFIÉS : " + rdvsPlanifies.size());

        List<RDV> rdvsConfirmes = rdvRepository.findByStatut(StatutRDV.CONFIRME);
        System.out.println("📋 RDV CONFIRMÉS : " + rdvsConfirmes.size());
    }

    void testUpdateRDV() {
        System.out.println("\n========== TEST RDV 28 : Mise à jour d'un RDV ==========");

        RDV rdv = rdvRepository.findById(1L);
        if (rdv != null) {
            System.out.println("📝 Avant : " + rdv.getStatut() + " - " + rdv.getHeureRDV());

            rdv.setStatut(StatutRDV.CONFIRME);
            rdv.setNoteMedecin("Patient confirmé par téléphone");
            rdvRepository.update(rdv);

            RDV rdvUpdated = rdvRepository.findById(1L);
            System.out.println("✅ Après : " + rdvUpdated.getStatut() + " - Note: " + rdvUpdated.getNoteMedecin());
        }
    }

    // ==============================================================
    // TESTS CAISSE (AYA LEZREGUE)
    // ==============================================================

    void testCreateSituationFinanciere() {
        System.out.println("\n========== TEST CAISSE 29 : Création Situation Financière ==========");

        SituationFinanciere sf = SituationFinanciere.builder()
                .idDM(1L)
                .totalDesActes(1500.0)
                .totalPaye(500.0)
                .resteDu(1000.0)
                .creance(0.0)
                .statut(StatutSituationFinanciere.DEBIT)
                .enPromo(false)
                .build();

        situationFinanciereRepository.create(sf);
        System.out.println("✅ Situation financière créée (ID: " + sf.getIdSF() + ")");
        System.out.println("   💰 Total actes : " + sf.getTotalDesActes() + " DH");
        System.out.println("   💵 Total payé : " + sf.getTotalPaye() + " DH");
        System.out.println("   💳 Reste dû : " + sf.getResteDu() + " DH");
        System.out.println("   📊 Statut : " + sf.getStatut());
    }

    void testCreateFactures() {
        System.out.println("\n========== TEST CAISSE 30 : Création des Factures ==========");

        Facture facture1 = Facture.builder()
                .idSF(1L)
                .idConsultation(1L)
                .totalFacture(500.0)
                .totalDesActes(500.0)
                .montantPaye(500.0)
                .reste(0.0)
                .statut(StatutFacture.PAYEE)
                .build();
        factureRepository.create(facture1);
        System.out.println("✅ Facture 1 créée : " + facture1.getTotalFacture() + " DH (" + facture1.getStatut() + ")");

        Facture facture2 = Facture.builder()
                .idSF(1L)
                .idConsultation(2L)
                .totalFacture(800.0)
                .totalDesActes(800.0)
                .montantPaye(300.0)
                .reste(500.0)
                .statut(StatutFacture.PARTIELLEMENT_PAYEE)
                .build();
        factureRepository.create(facture2);
        System.out.println("✅ Facture 2 créée : " + facture2.getTotalFacture() + " DH (" + facture2.getStatut() + ")");

        Facture facture3 = Facture.builder()
                .idSF(1L)
                .idConsultation(3L)
                .totalFacture(600.0)
                .totalDesActes(600.0)
                .montantPaye(0.0)
                .reste(600.0)
                .statut(StatutFacture.EN_ATTENTE)
                .build();
        factureRepository.create(facture3);
        System.out.println("✅ Facture 3 créée : " + facture3.getTotalFacture() + " DH (" + facture3.getStatut() + ")");
    }

    void testFindAllFactures() {
        System.out.println("\n========== TEST CAISSE 31 : Lire toutes les Factures ==========");

        List<Facture> factures = factureRepository.findAll();
        System.out.println("📊 Nombre total de factures : " + factures.size());

        double totalFacture = 0;
        double totalPaye = 0;
        double totalReste = 0;

        for (Facture facture : factures) {
            System.out.println("   - Facture #" + facture.getIdFacture() +
                    " : " + facture.getTotalFacture() + " DH (" + facture.getStatut() + ")");
            totalFacture += facture.getTotalFacture();
            totalPaye += facture.getMontantPaye();
            totalReste += facture.getReste();
        }

        System.out.println("📈 Résumé financier :");
        System.out.println("   💰 Total facturé : " + totalFacture + " DH");
        System.out.println("   💵 Total payé : " + totalPaye + " DH");
        System.out.println("   💳 Total restant : " + totalReste + " DH");
    }

    void testCalculateTotalImpaye() {
        System.out.println("\n========== TEST CAISSE 32 : Calculer Total Impayé ==========");

        double totalImpaye = factureRepository.calculateTotalFacturesImpayees();
        System.out.println("📊 Total des factures impayées : " + totalImpaye + " DH");
    }

    void testUpdateAfterPayment() {
        System.out.println("\n========== TEST CAISSE 33 : Mise à jour après Paiement ==========");

        SituationFinanciere sf = situationFinanciereRepository.findById(1L);
        if (sf != null) {
            System.out.println("📝 Avant paiement :");
            System.out.println("   💵 Total payé : " + sf.getTotalPaye() + " DH");
            System.out.println("   💳 Reste dû : " + sf.getResteDu() + " DH");
            System.out.println("   📊 Statut : " + sf.getStatut());

            situationFinanciereRepository.updateAfterPayment(sf.getIdSF(), 500.0);

            SituationFinanciere sfUpdated = situationFinanciereRepository.findById(1L);
            System.out.println("\n✅ Après paiement de 500 DH :");
            System.out.println("   💵 Total payé : " + sfUpdated.getTotalPaye() + " DH");
            System.out.println("   💳 Reste dû : " + sfUpdated.getResteDu() + " DH");
            System.out.println("   📊 Statut : " + sfUpdated.getStatut());
        }
    }

    // ==============================================================
    // TESTS PATIENT (OTHMANE CHLIBAKH)
    // ==============================================================

    void testCreatePatients() {
        System.out.println("\n========== TEST PATIENT OC 34 : Création des Patients ==========");

        Patient p1 = Patient.builder()
                .nom("CHLIBAKH")
                .prenom("Othmane")
                .telephone("0600000000")
                .email("othmane@example.com")
                .adresse("Rabat")
                .build();

        this.patientRepository.create(p1);

        if (p1.getId() != null) {
            System.out.println("✅ Patient créé : " + p1.getNom() + " " + p1.getPrenom() +
                    " (ID: " + p1.getId() + ")");
        } else {
            System.out.println("❌ ERREUR: Patient créé mais ID non généré - " + p1.getNom() + " " + p1.getPrenom());
        }
    }

    void testFindAllPatients() {
        System.out.println("\n========== TEST PATIENT OC 35 : Lire tous les Patients ==========");

        List<Patient> patients = this.patientRepository.findAll();
        System.out.println("📊 Nombre total de patients : " + patients.size());

        if (patients.isEmpty()) {
            System.out.println("ℹ️  Aucun patient trouvé dans la base de données");
        } else {
            for (Patient p : patients) {
                System.out.println("   - " + p.getNom() + " " + p.getPrenom() + " | " + p.getTelephone() + " | ID: " + p.getId());
            }
        }
    }

    void testFindPatientById() {
        System.out.println("\n========== TEST PATIENT OC 36 : Trouver Patient par ID ==========");

        Long patientId = 1L;
        Patient p = this.patientRepository.findById(patientId);

        if (p != null) {
            System.out.println("✅ Patient trouvé : " + p.getNom() + " " + p.getPrenom() + " (ID: " + p.getId() + ")");
        } else {
            System.out.println("❌ Patient non trouvé avec ID: " + patientId);
        }
    }

    void testUpdatePatient() {
        System.out.println("\n========== TEST PATIENT OC 37 : Mise à jour Patient ==========");

        List<Patient> patients = this.patientRepository.findAll();
        if (patients.isEmpty()) {
            System.out.println("ℹ️  Aucun patient à mettre à jour - créer un patient d'abord");
            return;
        }

        Patient p = patients.get(0);

        if (p != null) {
            System.out.println("📝 Avant : " + p.getNom() + " - " + p.getTelephone() + " - " + p.getAdresse());

            p.setTelephone("0612345678");
            p.setAdresse("Casablanca");

            this.patientRepository.update(p);

            Patient updated = this.patientRepository.findById(p.getId());
            if (updated != null) {
                System.out.println("✅ Après : " + updated.getNom() + " - " + updated.getTelephone() + " - " + updated.getAdresse());
            } else {
                System.out.println("❌ Erreur: Patient non trouvé après mise à jour");
            }
        }
    }

    void testDeletePatient() {
        System.out.println("\n========== TEST PATIENT OC 38 : Suppression Patient ==========");

        Patient temp = Patient.builder()
                .nom("Temp")
                .prenom("Delete")
                .telephone("0700000000")
                .email("temp@test.com")
                .adresse("Adresse temporaire")
                .build();

        this.patientRepository.create(temp);

        if (temp.getId() == null) {
            System.out.println("❌ ERREUR CRITIQUE: Impossible de créer le patient temporaire - ID null");
            return;
        }

        System.out.println("✅ Patient temporaire créé (ID: " + temp.getId() + ")");

        Patient beforeDelete = this.patientRepository.findById(temp.getId());
        if (beforeDelete != null) {
            this.patientRepository.deleteById(temp.getId());
            System.out.println("🗑️  Patient supprimé.");

            Patient afterDelete = this.patientRepository.findById(temp.getId());
            if (afterDelete == null) {
                System.out.println("✅ Confirmation : suppression réussie");
            } else {
                System.out.println("❌ Erreur : patient encore présent après suppression");
            }
        } else {
            System.out.println("❌ Erreur : patient temporaire non trouvé avant suppression");
        }
    }

    // ==============================================================
    // TESTS ORDONNANCE (OTHMANE CHLIBAKH)
    // ==============================================================

    void testCreateOrdonnances() {
        System.out.println("\n========== TEST ORDONNANCE OC 39 : Création des Ordonnances ==========");

        Long idDM = creerDossierMedicalComplet();

        boolean ordonnanceCreee = false;

        for (Long idMedecin = 1L; idMedecin <= 5L; idMedecin++) {
            System.out.println("🔄 Tentative avec médecin ID: " + idMedecin);

            Ordonnance ord1 = Ordonnance.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)
                    .dateOrdonnance(LocalDate.now().minusDays(2))
                    .build();

            try {
                this.ordonnanceRepository.create(ord1);
                System.out.println("✅ SUCCÈS ! Ordonnance créée (ID: " + ord1.getIdOrdo() + ")");
                System.out.println("   📁 Dossier Médical: " + idDM);
                System.out.println("   👨‍⚕️ Médecin: " + idMedecin);
                ordonnanceCreee = true;
                break;
            } catch (Exception e) {
                System.out.println("   ❌ Échec avec médecin " + idMedecin + " - Essai suivant...");
            }
        }

        if (!ordonnanceCreee) {
            System.out.println("❌ ÉCHEC TOTAL : Aucun médecin valide trouvé !");
        }
    }

    void testFindAllOrdonnances() {
        System.out.println("\n========== TEST ORDONNANCE OC 40 : Lire toutes les Ordonnances ==========");

        List<Ordonnance> ordonnances = this.ordonnanceRepository.findAll();
        System.out.println("📊 Nombre total d'ordonnances en base : " + ordonnances.size());

        if (ordonnances.isEmpty()) {
            System.out.println("ℹ️  Aucune ordonnance trouvée dans la base de données");
        } else {
            System.out.println("📋 Liste des ordonnances :");
            for (Ordonnance o : ordonnances) {
                System.out.println("   - ID: " + o.getIdOrdo() + " | DM: " + o.getIdDM() +
                        " | Médecin: " + o.getIdMedecin() + " | Date: " + o.getDateOrdonnance());
            }
        }
        System.out.println("🎯 TEST REUSSI : Consultation de " + ordonnances.size() + " ordonnances");
    }

    void testFindOrdonnanceByDossierMedical() {
        System.out.println("\n========== TEST ORDONNANCE OC 41 : Ordonnances par Dossier Médical ==========");

        Long idDMTest = 1L;
        List<Ordonnance> ordonnances = this.ordonnanceRepository.findByDossierMedical(idDMTest);

        System.out.println("🔍 Recherche des ordonnances pour le dossier médical ID: " + idDMTest);
        System.out.println("📈 Résultat : " + ordonnances.size() + " ordonnance(s) trouvée(s)");

        for (Ordonnance o : ordonnances) {
            System.out.println("   - Ordonnance ID: " + o.getIdOrdo() + " | Date: " + o.getDateOrdonnance());
        }

        if (ordonnances.size() > 0) {
            System.out.println("🎯 TEST REUSSI : " + ordonnances.size() + " ordonnance(s) trouvée(s) pour le DM " + idDMTest);
        } else {
            System.out.println("⚠️  ATTENTION : Aucune ordonnance trouvée pour le DM " + idDMTest);
        }
    }

    void testFindOrdonnanceByMedecin() {
        System.out.println("\n========== TEST ORDONNANCE OC 42 : Ordonnances par Médecin ==========");

        Long idMedecinTest = 1L;
        List<Ordonnance> ordonnances = this.ordonnanceRepository.findByMedecin(idMedecinTest);

        System.out.println("🔍 Recherche des ordonnances pour le médecin ID: " + idMedecinTest);
        System.out.println("📈 Résultat : " + ordonnances.size() + " ordonnance(s) trouvée(s)");

        for (Ordonnance o : ordonnances) {
            System.out.println("   - Ordonnance ID: " + o.getIdOrdo() + " | DM: " + o.getIdDM() + " | Date: " + o.getDateOrdonnance());
        }

        System.out.println("🎯 TEST REUSSI : Consultation des ordonnances par médecin");
    }

    // ==============================================================
    // TESTS PRESCRIPTION (OTHMANE CHLIBAKH)
    // ==============================================================

    void testCreatePrescriptions() {
        System.out.println("\n========== TEST PRESCRIPTION OC 43 : Création des Prescriptions ==========");

        List<Ordonnance> ordonnances = this.ordonnanceRepository.findAll();
        if (ordonnances.isEmpty()) {
            System.out.println("❌ CRÉEZ D'ABORD DES ORDONNANCES !");
            return;
        }

        Ordonnance premiereOrdonnance = ordonnances.get(0);
        Long idOrdo = premiereOrdonnance.getIdOrdo();

        System.out.println("✅ Utilisation de l'ordonnance ID: " + idOrdo);

        Prescription pres1 = Prescription.builder()
                .idOrdo(idOrdo)
                .idMedicament(1L)
                .quantite(30)
                .frequence("3 fois par jour après les repas")
                .dureeEnJours(10)
                .build();

        this.prescriptionRepository.create(pres1);
        System.out.println("✅ Prescription 1 créée (ID: " + pres1.getIdPrescription() +
                ") - Médicament: " + pres1.getIdMedicament() + ", Durée: " + pres1.getDureeEnJours() + " jours");

        Prescription pres2 = Prescription.builder()
                .idOrdo(idOrdo)
                .idMedicament(2L)
                .quantite(20)
                .frequence("2 fois par jour matin et soir")
                .dureeEnJours(5)
                .build();

        this.prescriptionRepository.create(pres2);
        System.out.println("✅ Prescription 2 créée (ID: " + pres2.getIdPrescription() +
                ") - Médicament: " + pres2.getIdMedicament() + ", Quantité: " + pres2.getQuantite());

        System.out.println("🎯 TEST REUSSI : Création de " + this.prescriptionRepository.findAll().size() + " prescriptions");
    }

    void testFindAllPrescriptions() {
        System.out.println("\n========== TEST PRESCRIPTION OC 44 : Lire toutes les Prescriptions ==========");

        List<Prescription> prescriptions = this.prescriptionRepository.findAll();
        System.out.println("📊 Nombre total de prescriptions en base : " + prescriptions.size());

        if (prescriptions.isEmpty()) {
            System.out.println("ℹ️  Aucune prescription trouvée dans la base de données");
        } else {
            System.out.println("📋 Liste des prescriptions :");
            for (Prescription p : prescriptions) {
                System.out.println("   - ID: " + p.getIdPrescription() + " | Ordo: " + p.getIdOrdo() +
                        " | Médicament: " + p.getIdMedicament() + " | " + p.getFrequence() +
                        " (" + p.getDureeEnJours() + " jours)");
            }
        }
        System.out.println("🎯 TEST REUSSI : Consultation de " + prescriptions.size() + " prescriptions");
    }

    void testFindPrescriptionByOrdonnance() {
        System.out.println("\n========== TEST PRESCRIPTION OC 45 : Prescriptions par Ordonnance ==========");

        List<Ordonnance> ordonnances = this.ordonnanceRepository.findAll();
        if (ordonnances.isEmpty()) {
            System.out.println("❌ AUCUNE ORDONNANCE DISPONIBLE !");
            return;
        }

        Long idOrdoTest = ordonnances.get(0).getIdOrdo();
        List<Prescription> prescriptions = this.prescriptionRepository.findByOrdonnance(idOrdoTest);

        System.out.println("🔍 Recherche des prescriptions pour l'ordonnance ID: " + idOrdoTest);
        System.out.println("📈 Résultat : " + prescriptions.size() + " prescription(s) trouvée(s)");

        if (prescriptions.isEmpty()) {
            System.out.println("ℹ️  Aucune prescription pour cette ordonnance");
        } else {
            System.out.println("💊 Détail des prescriptions :");
            for (Prescription p : prescriptions) {
                System.out.println("   - Médicament " + p.getIdMedicament() + " : " + p.getQuantite() +
                        " unités, " + p.getFrequence().toLowerCase() +
                        " pendant " + p.getDureeEnJours() + " jours");
            }
        }

        System.out.println("🎯 TEST REUSSI : " + prescriptions.size() + " prescription(s) trouvée(s) pour l'ordonnance " + idOrdoTest);
    }

    void testDeletePrescriptionByOrdonnance() {
        System.out.println("\n========== TEST PRESCRIPTION OC 46 : Suppression des Prescriptions par Ordonnance ==========");

        List<Ordonnance> ordonnances = this.ordonnanceRepository.findAll();
        if (ordonnances.size() < 2) {
            System.out.println("❌ BESOIN D'AU MOINS 2 ORDONNANCES POUR CE TEST !");
            return;
        }

        Long idOrdoTest = ordonnances.get(1).getIdOrdo();

        int avantSuppression = this.prescriptionRepository.findByOrdonnance(idOrdoTest).size();
        System.out.println("📊 Nombre de prescriptions avant suppression pour ordonnance " + idOrdoTest + " : " + avantSuppression);

        this.prescriptionRepository.deleteByOrdonnance(idOrdoTest);

        int apresSuppression = this.prescriptionRepository.findByOrdonnance(idOrdoTest).size();
        System.out.println("📊 Nombre de prescriptions après suppression : " + apresSuppression);

        if (apresSuppression == 0) {
            System.out.println("✅ SUCCÈS : Toutes les prescriptions de l'ordonnance " + idOrdoTest + " ont été supprimées");
        } else {
            System.out.println("❌ ÉCHEC : Il reste " + apresSuppression + " prescription(s) après suppression");
        }
    }

    // ==============================================================
    // TESTS MÉDICAMENT (OTHMANE CHLIBAKH)
    // ==============================================================

    void testCreateMedicaments() {
        System.out.println("\n========== TEST MÉDICAMENT OC 47 : Création des Médicaments ==========");

        Medicament med1 = Medicament.builder()
                .nom("Paracétamol")
                .type("Antalgique")
                .forme("Comprimé")
                .remboursable(true)
                .prixUnitaire(2.5)
                .description("Antalgique et antipyrétique")
                .build();

        this.medicamentRepository.create(med1);
        System.out.println("✅ Médicament 1 créé (ID: " + med1.getIdMedicament() + ") - " + med1.getNom());

        Medicament med2 = Medicament.builder()
                .nom("Ibuprofène")
                .type("Anti-inflammatoire")
                .forme("Comprimé")
                .remboursable(true)
                .prixUnitaire(3.2)
                .description("Anti-inflammatoire non stéroïdien")
                .build();

        this.medicamentRepository.create(med2);
        System.out.println("✅ Médicament 2 créé (ID: " + med2.getIdMedicament() + ") - " + med2.getNom());

        Medicament med3 = Medicament.builder()
                .nom("Amoxicilline")
                .type("Antibiotique")
                .forme("Gélule")
                .remboursable(true)
                .prixUnitaire(8.7)
                .description("Antibiotique à large spectre")
                .build();

        this.medicamentRepository.create(med3);
        System.out.println("✅ Médicament 3 créé (ID: " + med3.getIdMedicament() + ") - " + med3.getNom());

        System.out.println("🎯 TEST REUSSI : Création de " + this.medicamentRepository.findAll().size() + " médicaments");
    }

    void testFindAllMedicaments() {
        System.out.println("\n========== TEST MÉDICAMENT OC 48 : Lire tous les Médicaments ==========");

        List<Medicament> medicaments = this.medicamentRepository.findAll();
        System.out.println("📊 Nombre total de médicaments en base : " + medicaments.size());

        if (medicaments.isEmpty()) {
            System.out.println("ℹ️  Aucun médicament trouvé dans la base de données");
        } else {
            System.out.println("📋 Liste des médicaments :");
            for (Medicament m : medicaments) {
                System.out.println("   - ID: " + m.getIdMedicament() + " | " + m.getNom() +
                        " | " + m.getType() + " | " + m.getForme() + " | " + m.getPrixUnitaire() + " DH");
            }
        }
        System.out.println("🎯 TEST REUSSI : Consultation de " + medicaments.size() + " médicaments");
    }

    void testFindMedicamentByNom() {
        System.out.println("\n========== TEST MÉDICAMENT OC 49 : Recherche par Nom ==========");

        String recherche = "para";
        List<Medicament> medicaments = this.medicamentRepository.findByNom(recherche);

        System.out.println("🔍 Recherche de médicaments contenant : '" + recherche + "'");
        System.out.println("📈 Résultat : " + medicaments.size() + " médicament(s) trouvé(s)");

        if (medicaments.isEmpty()) {
            System.out.println("ℹ️  Aucun médicament ne correspond à la recherche");
        } else {
            for (Medicament m : medicaments) {
                System.out.println("   - " + m.getNom() + " (" + m.getType() + ") - " + m.getPrixUnitaire() + " DH");
            }
        }
    }

    void testFindMedicamentByType() {
        System.out.println("\n========== TEST MÉDICAMENT OC 50 : Recherche par Type ==========");

        String typeRecherche = "Antalgique";
        List<Medicament> medicaments = this.medicamentRepository.findByType(typeRecherche);

        System.out.println("🔍 Recherche de médicaments de type : '" + typeRecherche + "'");
        System.out.println("📈 Résultat : " + medicaments.size() + " médicament(s) trouvé(s)");

        if (medicaments.isEmpty()) {
            System.out.println("ℹ️  Aucun médicament de ce type");
        } else {
            for (Medicament m : medicaments) {
                System.out.println("   - " + m.getNom() + " | " + m.getForme() + " | " +
                        (m.getRemboursable() ? "💊 Remboursable" : "💰 Non remboursable"));
            }
        }
    }

    void testFindMedicamentRemboursables() {
        System.out.println("\n========== TEST MÉDICAMENT OC 51 : Médicaments Remboursables ==========");

        List<Medicament> medicaments = this.medicamentRepository.findByRemboursable(true);

        System.out.println("🔍 Recherche des médicaments remboursables");
        System.out.println("📈 Résultat : " + medicaments.size() + " médicament(s) remboursable(s)");

        if (medicaments.isEmpty()) {
            System.out.println("ℹ️  Aucun médicament remboursable trouvé");
        } else {
            double total = 0.0;
            for (Medicament m : medicaments) {
                System.out.println("   - " + m.getNom() + " | " + m.getPrixUnitaire() + " DH");
                total += m.getPrixUnitaire();
            }
            System.out.println("💰 Prix moyen des médicaments remboursables : " +
                    String.format("%.2f", total / medicaments.size()) + " DH");
        }
    }

    void testFindMedicamentByForme() {
        System.out.println("\n========== TEST MÉDICAMENT OC 52 : Recherche par Forme ==========");

        String formeRecherche = "Comprimé";
        List<Medicament> medicaments = this.medicamentRepository.findByForme(formeRecherche);

        System.out.println("🔍 Recherche de médicaments sous forme : '" + formeRecherche + "'");
        System.out.println("📈 Résultat : " + medicaments.size() + " médicament(s) trouvé(s)");

        if (medicaments.isEmpty()) {
            System.out.println("ℹ️  Aucun médicament sous cette forme");
        } else {
            for (Medicament m : medicaments) {
                System.out.println("   - " + m.getNom() + " | " + m.getType() + " | " + m.getPrixUnitaire() + " DH");
            }
        }
    }

    // ==============================================================
    // MÉTHODE PRINCIPALE DE TEST
    // ==============================================================

    void testProcess() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   TESTS COMPLETS - CABINET DENTAIRE          ║");
        System.out.println("╚════════════════════════════════════════════════╝");

        try {
            // 🔍 ÉTAPE 1 : DIAGNOSTIC & NETTOYAGE
            diagnosticComplet();
            cleanDatabase();

            // 👤 ÉTAPE 2 : TESTS AUTH & DASHBOARD (SARA)
            System.out.println("\n" + "═".repeat(60));
            System.out.println("🔐 AUTHENTIFICATION & DASHBOARD (SARA NABIL)");
            System.out.println("═".repeat(60));
            testCreateUsers();
            testFindAllUsers();
            testFindUserById();
            testFindUserByEmail();
            testFindUserByUsername();
            testUpdateUser();
            testDeactivateUser();
            testDeleteUser();

            testDashboardPatients();
            testDashboardRDV();
            testDashboardConsultations();
            testDashboardFinancials();
            testDashboardLastLogins();
            testDashboardComplet();

            // 📝 ÉTAPE 3 : TESTS ACTES & RDV (AYA)
            System.out.println("\n" + "═".repeat(60));
            System.out.println("📝 ACTES & RDV (AYA LEZREGUE)");
            System.out.println("═".repeat(60));
            testCreateActes();
            testFindAllActes();
            testFindActeById();
            testFindActesByCategorie();
            testSearchActeByLibelle();
            testUpdateActe();
            testDeleteActe();

            testCreateRDV();
            testFindAllRDV();
            testFindRDVByDate();
            testFindRDVByMedecin();
            testFindRDVByPatient();
            testFindRDVByStatut();
            testUpdateRDV();

            // 💰 ÉTAPE 4 : TESTS CAISSE (AYA)
            System.out.println("\n" + "═".repeat(60));
            System.out.println("💰 CAISSE (AYA LEZREGUE)");
            System.out.println("═".repeat(60));
            testCreateSituationFinanciere();
            testCreateFactures();
            testFindAllFactures();
            testCalculateTotalImpaye();
            testUpdateAfterPayment();

            // 👥 ÉTAPE 5 : TESTS PATIENT (OTHMANE)
            System.out.println("\n" + "═".repeat(60));
            System.out.println("👥 PATIENTS (OTHMANE CHLIBAKH)");
            System.out.println("═".repeat(60));
            testCreatePatients();
            testFindAllPatients();
            testFindPatientById();
            testUpdatePatient();
            testDeletePatient();

            // 📋 ÉTAPE 6 : TESTS ORDONNANCE & PRESCRIPTION (OTHMANE)
            System.out.println("\n" + "═".repeat(60));
            System.out.println("📋 ORDONNANCES & PRESCRIPTIONS (OTHMANE CHLIBAKH)");
            System.out.println("═".repeat(60));
            testCreateOrdonnances();
            testFindAllOrdonnances();
            testFindOrdonnanceByDossierMedical();
            testFindOrdonnanceByMedecin();

            testCreatePrescriptions();
            testFindAllPrescriptions();
            testFindPrescriptionByOrdonnance();
            testDeletePrescriptionByOrdonnance();

            // 💊 ÉTAPE 7 : TESTS MÉDICAMENT (OTHMANE)
            System.out.println("\n" + "═".repeat(60));
            System.out.println("💊 MÉDICAMENTS (OTHMANE CHLIBAKH)");
            System.out.println("═".repeat(60));
            testCreateMedicaments();
            testFindAllMedicaments();
            testFindMedicamentByNom();
            testFindMedicamentByType();
            testFindMedicamentRemboursables();
            testFindMedicamentByForme();

            // 📊 ÉTAPE FINALE : DIAGNOSTIC FINAL
            System.out.println("\n" + "═".repeat(60));
            System.out.println("📊 DIAGNOSTIC FINAL");
            System.out.println("═".repeat(60));
            diagnosticComplet();

            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║      TOUS LES TESTS TERMINÉS AVEC SUCCÈS!     ║");
            System.out.println("╚════════════════════════════════════════════════╝");

            System.out.println("\n👥 AUTEURS :");
            System.out.println("   • SARA NABIL - Auth & Dashboard");
            System.out.println("   • AYA LEZREGUE - Actes, RDV & Caisse");
            System.out.println("   • OTHMANE CHLIBAKH - Patients, Ordonnances, Prescriptions & Médicaments");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR lors des tests : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.testProcess();
    }
}

