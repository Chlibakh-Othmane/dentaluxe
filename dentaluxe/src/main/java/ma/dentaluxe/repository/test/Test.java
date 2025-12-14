package ma.dentaluxe.repository.test;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.enums.*;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.consultation.InterventionMedecin;
import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.ordonnance.Medicament;
import ma.dentaluxe.entities.certificat.Certificat;
import ma.dentaluxe.entities.caisse.Facture;
import ma.dentaluxe.entities.caisse.SituationFinanciere;
import ma.dentaluxe.entities.utilisateur.Utilisateur;

import ma.dentaluxe.repository.modules.actes.inMemDB_implementation.ActeRepositoryImpl;
import ma.dentaluxe.repository.modules.agenda.inMemDB_implementation.RDVRepositoryImpl;
import ma.dentaluxe.repository.modules.auth.inMemDB_implementation.AuthRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.*;
import ma.dentaluxe.repository.modules.certificat.inMemDB_implementation.CertificatRepositoryImpl;
import ma.dentaluxe.repository.modules.dashboard.inMemDB_implementation.DashboardRepositoryImpl;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.*;
import ma.dentaluxe.repository.modules.medicament.inMemDB_implementation.MedicamentRepositoryImpl;
import ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation.*;
import ma.dentaluxe.repository.modules.patient.inMemDB_implementation.PatientRepositoryImpl;
import ma.dentaluxe.repository.modules.statistiques.inMemDB_implementation.StatistiqueRepositoryImpl;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;

public class Test {

    private static Scanner scanner = new Scanner(System.in);
    private static Utilisateur utilisateurConnecte = null;
    private static String roleUtilisateur = null;

    // Instances des repositories
    private static AuthRepositoryImpl authRepo = new AuthRepositoryImpl();
    private static ActeRepositoryImpl acteRepo = new ActeRepositoryImpl();
    private static RDVRepositoryImpl rdvRepo = new RDVRepositoryImpl();
    private static FactureRepositoryImpl factureRepo = new FactureRepositoryImpl();
    private static SituationFinanciereRepositoryImpl sfRepo = new SituationFinanciereRepositoryImpl();
    private static CertificatRepositoryImpl certifRepo = new CertificatRepositoryImpl();
    private static DashboardRepositoryImpl dashboardRepo = new DashboardRepositoryImpl();
    private static AntecedentsRepositoryImpl antecedentsRepo = new AntecedentsRepositoryImpl();
    private static ConsultationRepositoryImpl consultationRepo = new ConsultationRepositoryImpl();
    private static DossierMedicalRepositoryImpl dossierRepo = new DossierMedicalRepositoryImpl();
    private static InterventionMedecinRepositoryImpl interventionRepo = new InterventionMedecinRepositoryImpl();
    private static MedicamentRepositoryImpl medicamentRepo = new MedicamentRepositoryImpl();
    private static OrdonnanceRepositoryImpl ordonnanceRepo = new OrdonnanceRepositoryImpl();
    private static PrescriptionRepositoryImpl prescriptionRepo = new PrescriptionRepositoryImpl();
    private static PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();
    private static StatistiqueRepositoryImpl statRepo = new StatistiqueRepositoryImpl();

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║        🦷 DENTALUXE - SYSTEME DE GESTION              ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");

        testConnexionBDD();
        authentifierUtilisateur();

        if (utilisateurConnecte != null) {
            afficherDashboardSelonRole();
            menuPrincipal();
        }

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

    private static void authentifierUtilisateur() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║                 🔐 CONNEXION                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");

        int tentatives = 3;

        while (tentatives > 0 && utilisateurConnecte == null) {
            System.out.print("👤 Login: ");
            String login = scanner.nextLine();

            System.out.print("🔒 Mot de passe: ");
            String password = scanner.nextLine();

            utilisateurConnecte = authRepo.authenticate(login, password);

            if (utilisateurConnecte != null) {
                roleUtilisateur = authRepo.getUserRole(utilisateurConnecte.getId());
                System.out.println("\n✓ Connexion réussie !");
                System.out.println("Bienvenue " + utilisateurConnecte.getNom() + " " +
                        utilisateurConnecte.getPrenom());
                System.out.println("Rôle: " + roleUtilisateur);
            } else {
                tentatives--;
                if (tentatives > 0) {
                    System.out.println("\n✗ Identifiants incorrects. Tentatives restantes: " + tentatives);
                } else {
                    System.out.println("\n✗ Nombre de tentatives dépassé. Au revoir!");
                    System.exit(0);
                }
            }
        }
    }

    private static void afficherDashboardSelonRole() {
        if (roleUtilisateur == null) return;

        switch (roleUtilisateur.toUpperCase()) {
            case "MEDECIN":
                afficherDashboardMedecin();
                break;
            case "SECRETAIRE":
                afficherDashboardSecretaire();
                break;
            case "ADMIN":
                afficherDashboardAdmin();
                break;
            default:
                System.out.println("Rôle non reconnu: " + roleUtilisateur);
        }
    }

    private static void afficherDashboardMedecin() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║            🩺 DASHBOARD MÉDECIN                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue Dr. " + utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
        System.out.println();

        // Statistiques réelles
        System.out.println("   📊 STATISTIQUES DU JOUR :");
        System.out.println("      • Consultations du jour    : " + consultationRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream().filter(c -> c.getDateConsultation().equals(LocalDate.now())).count());

        System.out.println("      • RDV du jour              : " + rdvRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream().filter(r -> r.getDateRDV().equals(LocalDate.now())).count());

        // Interventions du médecin
        List<InterventionMedecin> interventions = interventionRepo.findByIdMedecin(utilisateurConnecte.getId());
        double revenuTotal = interventions.stream()
                .mapToDouble(InterventionMedecin::getPrixIntervention)
                .sum();
        System.out.println("      • Revenu total             : " + revenuTotal + " DH");

        System.out.println();
        System.out.println("   📅 PROCHAINES CONSULTATIONS :");
        List<Consultation> consultations = consultationRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream()
                .filter(c -> c.getDateConsultation().isEqual(LocalDate.now()) || c.getDateConsultation().isAfter(LocalDate.now()))
                .limit(3)
                .toList();

        if (!consultations.isEmpty()) {
            consultations.forEach(c -> {
                try {
                    Patient patient = getPatientByDossierId(c.getIdDM());
                    System.out.println("      • " + c.getDateConsultation() + " - " +
                            (patient != null ? patient.getNom() + " " + patient.getPrenom() : "Patient"));
                } catch (Exception e) {
                    System.out.println("      • " + c.getDateConsultation() + " - Consultation");
                }
            });
        } else {
            System.out.println("      • Aucune consultation à venir");
        }
    }

    private static void afficherDashboardSecretaire() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          💼 DASHBOARD SECRÉTAIRE                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue " + utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom());
        System.out.println();

        System.out.println("   📊 STATISTIQUES :");
        System.out.println("      • Patients totaux         : " + patientRepo.findAll().size());
        System.out.println("      • RDV aujourd'hui         : " + rdvRepo.findByDate(LocalDate.now()).size());

        double totalImpaye = factureRepo.calculateTotalFacturesImpayees();
        System.out.println("      • Montant impayé          : " + totalImpaye + " DH");

        System.out.println();
        System.out.println("   📅 RENDEZ-VOUS DU JOUR :");
        List<RDV> rdvsAujourdhui = rdvRepo.findByDate(LocalDate.now());
        if (!rdvsAujourdhui.isEmpty()) {
            rdvsAujourdhui.stream().limit(5).forEach(r -> {
                String statut = (r.getStatut() == StatutRDV.CONFIRME) ? "✓" :
                        (r.getStatut() == StatutRDV.ANNULE) ? "✗" : "?";
                System.out.println("      " + statut + " " + r.getHeureRDV() + " - " + r.getMotif());
            });
        } else {
            System.out.println("      • Aucun RDV aujourd'hui");
        }
    }

    private static void afficherDashboardAdmin() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║            ⚙ DASHBOARD ADMIN                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue Administrateur " + utilisateurConnecte.getNom());
        System.out.println();

        System.out.println("   📊 STATISTIQUES GLOBALES :");
        System.out.println("      • Utilisateurs actifs    : " +
                authRepo.findAll().stream().filter(Utilisateur::getActif).count());
        System.out.println("      • Patients              : " + patientRepo.findAll().size());

        List<Utilisateur> medecins = getUsersByRole("MEDECIN");
        List<Utilisateur> secretaires = getUsersByRole("SECRETAIRE");
        System.out.println("      • Médecins              : " + medecins.size());
        System.out.println("      • Secrétaires           : " + secretaires.size());
    }

    private static void menuPrincipal() {
        int choix;

        do {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║                 📋 MENU PRINCIPAL                     ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");

            // Menu selon le rôle
            if ("ADMIN".equalsIgnoreCase(roleUtilisateur)) {
                System.out.println("1.  👥 Gestion utilisateurs");
                System.out.println("2.  🏥 Tests patients");
                System.out.println("3.  📅 Tests rendez-vous");
                System.out.println("4.  💰 Tests financiers");
                System.out.println("5.  📊 Dashboard système");
                System.out.println("0.  🚪 Déconnexion");
            } else if ("MEDECIN".equalsIgnoreCase(roleUtilisateur)) {
                System.out.println("1.  👥 Mes patients");
                System.out.println("2.  📅 Mon agenda");
                System.out.println("3.  🩺 Mes consultations");
                System.out.println("4.  💊 Ordonnances");
                System.out.println("5.  📝 Certificats");
                System.out.println("6.  📊 Mes statistiques");
                System.out.println("0.  🚪 Déconnexion");
            } else if ("SECRETAIRE".equalsIgnoreCase(roleUtilisateur)) {
                System.out.println("1.  👥 Gestion patients");
                System.out.println("2.  📅 Gestion rendez-vous");
                System.out.println("3.  💰 Gestion caisse");
                System.out.println("4.  📞 Accueil");
                System.out.println("5.  📊 Statistiques");
                System.out.println("0.  🚪 Déconnexion");
            }

            System.out.print("\nVotre choix: ");
            choix = scanner.nextInt();
            scanner.nextLine();

            // Traitement selon rôle
            if ("ADMIN".equalsIgnoreCase(roleUtilisateur)) {
                traiterChoixAdmin(choix);
            } else if ("MEDECIN".equalsIgnoreCase(roleUtilisateur)) {
                traiterChoixMedecin(choix);
            } else if ("SECRETAIRE".equalsIgnoreCase(roleUtilisateur)) {
                traiterChoixSecretaire(choix);
            }

        } while (choix != 0);

        System.out.println("\n👋 Au revoir " + utilisateurConnecte.getPrenom() + "!");
    }

    // ============================================
    // ADMIN : GESTION UTILISATEURS COMPLÈTE
    // ============================================
    private static void traiterChoixAdmin(int choix) {
        switch (choix) {
            case 1: menuGestionUtilisateurs(); break;
            case 2: testerPatientsAdmin(); break;
            case 3: testerRDVAdmin(); break;
            case 4: testerFinancesAdmin(); break;
            case 5: testerDashboardAdmin(); break;
            case 0: break;
            default: System.out.println("Choix invalide!");
        }
    }

    private static void menuGestionUtilisateurs() {
        int choix;
        do {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║            👥 GESTION UTILISATEURS                   ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("1.  Lister tous les utilisateurs");
            System.out.println("2.  Ajouter un utilisateur");
            System.out.println("3.  Modifier un utilisateur");
            System.out.println("4.  Supprimer un utilisateur");
            System.out.println("5.  Affecter/Modifier rôle");
            System.out.println("6.  Rechercher utilisateur");
            System.out.println("0.  Retour");
            System.out.print("\nChoix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1: listerUtilisateurs(); break;
                case 2: ajouterUtilisateur(); break;
                case 3: modifierUtilisateur(); break;
                case 4: supprimerUtilisateur(); break;
                case 5: affecterRoleUtilisateur(); break;
                case 6: rechercherUtilisateur(); break;
                case 0: break;
                default: System.out.println("Choix invalide!");
            }
        } while (choix != 0);
    }

    private static void listerUtilisateurs() {
        System.out.println("\n=== LISTE DES UTILISATEURS ===");
        List<Utilisateur> users = authRepo.findAll();

        System.out.println("\nID | Nom | Prénom | Login | Email | Rôle | Actif");
        System.out.println("------------------------------------------------");

        for (Utilisateur user : users) {
            String role = authRepo.getUserRole(user.getId());
            String actif = user.getActif() ? "✓" : "✗";
            System.out.printf("%-3d %-15s %-15s %-10s %-20s %-10s %s%n",
                    user.getId(), user.getNom(), user.getPrenom(),
                    user.getLogin(), user.getEmail(), role, actif);
        }
    }

    private static void ajouterUtilisateur() {
        System.out.println("\n=== AJOUT D'UN NOUVEL UTILISATEUR ===");

        try {
            System.out.print("Nom: ");
            String nom = scanner.nextLine();

            System.out.print("Prénom: ");
            String prenom = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Login: ");
            String login = scanner.nextLine();

            System.out.print("Mot de passe: ");
            String password = scanner.nextLine();

            System.out.print("Téléphone: ");
            String tel = scanner.nextLine();

            System.out.print("Rôle (ADMIN/MEDECIN/SECRETAIRE): ");
            String role = scanner.nextLine().toUpperCase();

            // Création de l'utilisateur
            Utilisateur nouveau = Utilisateur.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .email(email)
                    .tel(tel)
                    .login(login)
                    .passwordHash(password)
                    .actif(true)
                    .creationDate(LocalDateTime.now())
                    .lastModificationDate(LocalDateTime.now())
                    .createdBy(utilisateurConnecte.getLogin())
                    .updatedBy(utilisateurConnecte.getLogin())
                    .build();

            // Sauvegarde
            authRepo.create(nouveau);

            // Affectation du rôle
            if (nouveau.getId() != null) {
                affecterRole(nouveau.getId(), role);
                System.out.println("\n✅ Utilisateur créé avec succès !");
                System.out.println("ID: " + nouveau.getId());
                System.out.println("Rôle: " + role);
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void modifierUtilisateur() {
        System.out.println("\n=== MODIFICATION D'UTILISATEUR ===");

        System.out.print("ID de l'utilisateur à modifier: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Utilisateur user = authRepo.findById(id);
        if (user == null) {
            System.out.println("❌ Utilisateur non trouvé");
            return;
        }

        System.out.println("\nUtilisateur actuel:");
        System.out.println("Nom: " + user.getNom());
        System.out.println("Prénom: " + user.getPrenom());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Actif: " + (user.getActif() ? "Oui" : "Non"));

        System.out.println("\nNouvelles valeurs (laisser vide pour ne pas modifier):");

        System.out.print("Nom [" + user.getNom() + "]: ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) user.setNom(nom);

        System.out.print("Prénom [" + user.getPrenom() + "]: ");
        String prenom = scanner.nextLine();
        if (!prenom.isEmpty()) user.setPrenom(prenom);

        System.out.print("Email [" + user.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("Actif (oui/non) [" + (user.getActif() ? "oui" : "non") + "]: ");
        String actifStr = scanner.nextLine();
        if (!actifStr.isEmpty()) {
            user.setActif(actifStr.equalsIgnoreCase("oui"));
        }

        user.setLastModificationDate(LocalDateTime.now());
        user.setUpdatedBy(utilisateurConnecte.getLogin());

        authRepo.update(user);
        System.out.println("✅ Utilisateur modifié avec succès");
    }

    private static void supprimerUtilisateur() {
        System.out.println("\n=== SUPPRESSION D'UTILISATEUR ===");

        System.out.print("ID de l'utilisateur à supprimer: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Utilisateur user = authRepo.findById(id);
        if (user == null) {
            System.out.println("❌ Utilisateur non trouvé");
            return;
        }

        System.out.println("\n⚠ ATTENTION: Vous allez supprimer:");
        System.out.println("Nom: " + user.getNom() + " " + user.getPrenom());
        System.out.println("Login: " + user.getLogin());
        System.out.println("Email: " + user.getEmail());

        System.out.print("\nConfirmer la suppression (oui/non)? ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("oui")) {
            authRepo.delete(user);
            System.out.println("✅ Utilisateur supprimé avec succès");
        } else {
            System.out.println("❌ Suppression annulée");
        }
    }

    private static void affecterRoleUtilisateur() {
        System.out.println("\n=== AFFECTATION DE RÔLE ===");

        System.out.print("ID de l'utilisateur: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();

        Utilisateur user = authRepo.findById(userId);
        if (user == null) {
            System.out.println("❌ Utilisateur non trouvé");
            return;
        }

        String roleActuel = authRepo.getUserRole(userId);
        System.out.println("Rôle actuel: " + roleActuel);

        System.out.println("\nRôles disponibles:");
        System.out.println("1. ADMIN");
        System.out.println("2. MEDECIN");
        System.out.println("3. SECRETAIRE");
        System.out.print("\nChoisir nouveau rôle (1-3): ");

        int choixRole = scanner.nextInt();
        scanner.nextLine();

        String nouveauRole = "";
        switch (choixRole) {
            case 1: nouveauRole = "ADMIN"; break;
            case 2: nouveauRole = "MEDECIN"; break;
            case 3: nouveauRole = "SECRETAIRE"; break;
            default:
                System.out.println("❌ Choix invalide");
                return;
        }

        // Supprimer ancien rôle
        String sqlDelete = "DELETE FROM utilisateur_role WHERE utilisateur_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression ancien rôle: " + e.getMessage());
            return;
        }

        // Ajouter nouveau rôle
        String sqlInsert = "INSERT INTO utilisateur_role (utilisateur_id, role_id) " +
                "SELECT ?, id FROM role WHERE libelle = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setLong(1, userId);
            pstmt.setString(2, nouveauRole);
            pstmt.executeUpdate();
            System.out.println("✅ Rôle " + nouveauRole + " affecté avec succès");
        } catch (SQLException e) {
            System.out.println("❌ Erreur affectation rôle: " + e.getMessage());
        }
    }

    private static void rechercherUtilisateur() {
        System.out.println("\n=== RECHERCHE D'UTILISATEUR ===");

        System.out.println("1. Par nom");
        System.out.println("2. Par login");
        System.out.println("3. Par email");
        System.out.print("Choix: ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Terme de recherche: ");
        String terme = scanner.nextLine();

        String sql = "";
        switch (choix) {
            case 1: sql = "SELECT * FROM utilisateur WHERE nom LIKE ?"; break;
            case 2: sql = "SELECT * FROM utilisateur WHERE login LIKE ?"; break;
            case 3: sql = "SELECT * FROM utilisateur WHERE email LIKE ?"; break;
            default:
                System.out.println("❌ Choix invalide");
                return;
        }

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + terme + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\nRésultats:");
            System.out.println("ID | Nom | Prénom | Login | Email | Actif");
            System.out.println("------------------------------------------");

            while (rs.next()) {
                String role = getRoleForUser(rs.getLong("id"));
                System.out.printf("%-3d %-15s %-15s %-10s %-20s %s%n",
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login"),
                        rs.getString("email"),
                        rs.getBoolean("actif") ? "✓" : "✗");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche: " + e.getMessage());
        }
    }

    private static void affecterRole(Long userId, String roleName) {
        String sql = "INSERT INTO utilisateur_role (utilisateur_id, role_id) " +
                "SELECT ?, id FROM role WHERE libelle = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setString(2, roleName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Erreur affectation rôle: " + e.getMessage());
        }
    }

    private static String getRoleForUser(Long userId) {
        String sql = "SELECT r.libelle FROM role r " +
                "JOIN utilisateur_role ur ON r.id = ur.role_id " +
                "WHERE ur.utilisateur_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("libelle");
            }
        } catch (SQLException e) {
            // Ignorer
        }
        return "N/A";
    }

    private static List<Utilisateur> getUsersByRole(String roleName) {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT u.* FROM utilisateur u " +
                "JOIN utilisateur_role ur ON u.id = ur.utilisateur_id " +
                "JOIN role r ON ur.role_id = r.id " +
                "WHERE r.libelle = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roleName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return users;
    }

    private static Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        return Utilisateur.builder()
                .id(rs.getLong("id"))
                .nom(rs.getString("nom"))
                .prenom(rs.getString("prenom"))
                .email(rs.getString("email"))
                .tel(rs.getString("tel"))
                .login(rs.getString("login"))
                .actif(rs.getBoolean("actif"))
                .build();
    }

    private static void testerPatientsAdmin() {
        System.out.println("\n=== PATIENTS AVEC LEURS RENDEZ-VOUS ===");

        List<Patient> patients = patientRepo.findAll();
        System.out.println("Total patients: " + patients.size());

        for (Patient patient : patients) {
            System.out.println("\n─────────────────────────────────────");
            System.out.println("Patient: " + patient.getNom() + " " + patient.getPrenom());
            System.out.println("ID: " + patient.getId());
            System.out.println("Téléphone: " + patient.getTelephone());

            // Trouver le dossier médical
            DossierMedical dossier = dossierRepo.findByPatientId(patient.getId());
            if (dossier != null) {
                // Rendez-vous du patient
                List<RDV> rdvs = rdvRepo.findByPatientDossierId(dossier.getIdDM());
                System.out.println("Rendez-vous (" + rdvs.size() + "):");

                for (RDV rdv : rdvs) {
                    System.out.println("  • " + rdv.getDateRDV() + " " + rdv.getHeureRDV() +
                            " - " + rdv.getMotif() + " (" + rdv.getStatut() + ")");
                }
            }
        }
    }

    private static void testerRDVAdmin() {
        System.out.println("\n=== STATISTIQUES RENDEZ-VOUS ===");

        System.out.println("RDV aujourd'hui: " + rdvRepo.findByDate(LocalDate.now()).size());

        List<RDV> rdvsConfirmes = rdvRepo.findByStatut(StatutRDV.CONFIRME);
        List<RDV> rdvsAnnules = rdvRepo.findByStatut(StatutRDV.ANNULE);
        List<RDV> rdvsTermines = rdvRepo.findByStatut(StatutRDV.TERMINE);

        System.out.println("RDV confirmés: " + rdvsConfirmes.size());
        System.out.println("RDV annulés: " + rdvsAnnules.size());
        System.out.println("RDV terminés: " + rdvsTermines.size());

        System.out.println("\nProchains RDV (7 jours):");
        LocalDate fin = LocalDate.now().plusDays(7);
        List<RDV> rdvsProchains = rdvRepo.findByDateRange(LocalDate.now(), fin);

        rdvsProchains.stream()
                .limit(10)
                .forEach(r -> {
                    System.out.println("  • " + r.getDateRDV() + " " + r.getHeureRDV() +
                            " - " + r.getMotif());
                });
    }

    private static void testerFinancesAdmin() {
        System.out.println("\n=== ÉTAT FINANCIER ===");

        double totalImpaye = factureRepo.calculateTotalFacturesImpayees();
        System.out.println("Total impayé: " + totalImpaye + " DH");

        double caTotal = dashboardRepo.calculateCATotal();
        System.out.println("CA total: " + caTotal + " DH");

        List<SituationFinanciere> situations = sfRepo.findAll();
        System.out.println("Situations financières: " + situations.size());

        System.out.println("\nTop 5 créances:");
        situations.stream()
                .sorted((s1, s2) -> Double.compare(s2.getCreance(), s1.getCreance()))
                .limit(5)
                .forEach(s -> {
                    System.out.println("  • Dossier " + s.getIdDM() + ": " + s.getCreance() + " DH");
                });
    }

    private static void testerDashboardAdmin() {
        System.out.println("\n=== DASHBOARD SYSTÈME ===");

        System.out.println("\n📊 Tableaux de bord:");
        System.out.println("Patients totaux: " + dashboardRepo.countPatientsTotal());
        System.out.println("Patients aujourd'hui: " + dashboardRepo.countPatientsToday());
        System.out.println("Consultations ce mois: " + dashboardRepo.countConsultationsThisMonth());
        System.out.println("CA aujourd'hui: " + dashboardRepo.calculateCAJour() + " DH");
        System.out.println("CA mensuel: " + dashboardRepo.calculateCAMensuel() + " DH");

        System.out.println("\n🕐 Dernières connexions:");
        List<String> logins = dashboardRepo.getLastLoginDates();
        if (!logins.isEmpty()) {
            logins.forEach(System.out::println);
        } else {
            System.out.println("Aucune connexion récente");
        }
    }

    // ============================================
    // MÉDECIN : FONCTIONNALITÉS COMPLÈTES
    // ============================================
    private static void traiterChoixMedecin(int choix) {
        switch (choix) {
            case 1: menuPatientsMedecin(); break;
            case 2: menuAgendaMedecin(); break;
            case 3: menuConsultationsMedecin(); break;
            case 4: menuOrdonnancesMedecin(); break;
            case 5: menuCertificatsMedecin(); break;
            case 6: testerStatistiquesMedecin(); break;
            case 0: break;
            default: System.out.println("Choix invalide!");
        }
    }

    private static void menuPatientsMedecin() {
        int choix;
        do {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║                  👥 MES PATIENTS                      ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("1.  Lister mes patients");
            System.out.println("2.  Ajouter un patient");
            System.out.println("3.  Voir dossier médical");
            System.out.println("4.  Rechercher patient");
            System.out.println("0.  Retour");
            System.out.print("\nChoix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1: listerPatientsMedecin(); break;
                case 2: ajouterPatientMedecin(); break;
                case 3: voirDossierPatient(); break;
                case 4: rechercherPatientMedecin(); break;
                case 0: break;
                default: System.out.println("Choix invalide!");
            }
        } while (choix != 0);
    }

    private static void listerPatientsMedecin() {
        System.out.println("\n=== MES PATIENTS ===");

        // Trouver les patients via consultations
        List<Consultation> consultations = consultationRepo.findByMedecinId(utilisateurConnecte.getId());
        Set<Long> dossiersIds = new HashSet<>();
        for (Consultation c : consultations) {
            dossiersIds.add(c.getIdDM());
        }

        System.out.println("Patients traités: " + dossiersIds.size());

        for (Long idDM : dossiersIds) {
            try {
                DossierMedical dossier = dossierRepo.findById(idDM);
                if (dossier != null) {
                    Patient patient = patientRepo.findById(dossier.getIdPatient());
                    if (patient != null) {
                        System.out.println("• " + patient.getNom() + " " + patient.getPrenom() +
                                " (Dossier: " + dossier.getIdDM() + ")");
                    }
                }
            } catch (Exception e) {
                // Continuer avec le patient suivant
            }
        }
    }

    private static void ajouterPatientMedecin() {
        System.out.println("\n=== AJOUT D'UN NOUVEAU PATIENT ===");

        try {
            System.out.print("Nom: ");
            String nom = scanner.nextLine();

            System.out.print("Prénom: ");
            String prenom = scanner.nextLine();

            System.out.print("Date de naissance (AAAA-MM-JJ): ");
            String dateStr = scanner.nextLine();
            LocalDate dateNaissance = null;
            if (!dateStr.isEmpty()) {
                dateNaissance = LocalDate.parse(dateStr);
            }

            System.out.print("Sexe (HOMME/FEMME): ");
            String sexeStr = scanner.nextLine();
            Sexe sexe = Sexe.valueOf(sexeStr.toUpperCase());

            System.out.print("Téléphone: ");
            String telephone = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Adresse: ");
            String adresse = scanner.nextLine();

            System.out.print("Assurance (CNOPS/CNSS/Privée/Aucune): ");
            String assuranceStr = scanner.nextLine();
            Assurance assurance = Assurance.valueOf(assuranceStr.toUpperCase());

            Patient patient = Patient.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .dateNaissance(dateNaissance)
                    .sexe(sexe)
                    .telephone(telephone)
                    .email(email)
                    .adresse(adresse)
                    .assurance(assurance)
                    .build();

            patientRepo.create(patient);

            // Créer automatiquement un dossier médical
            DossierMedical dossier = DossierMedical.builder()
                    .idPatient(patient.getId())
                    .dateDeCreation(LocalDate.now())
                    .build();

            dossierRepo.create(dossier);

            System.out.println("\n✅ Patient ajouté avec succès !");
            System.out.println("ID Patient: " + patient.getId());
            System.out.println("ID Dossier: " + dossier.getIdDM());

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void voirDossierPatient() {
        System.out.println("\n=== DOSSIER MÉDICAL ===");

        System.out.print("ID du patient: ");
        Long patientId = scanner.nextLong();
        scanner.nextLine();

        Patient patient = patientRepo.findById(patientId);
        if (patient == null) {
            System.out.println("❌ Patient non trouvé");
            return;
        }

        System.out.println("\nPatient: " + patient.getNom() + " " + patient.getPrenom());
        System.out.println("Téléphone: " + patient.getTelephone());
        System.out.println("Email: " + patient.getEmail());
        System.out.println("Date naissance: " + patient.getDateNaissance());

        DossierMedical dossier = dossierRepo.findByPatientId(patientId);
        if (dossier != null) {
            System.out.println("\n📋 DOSSIER MÉDICAL #" + dossier.getIdDM());
            System.out.println("Date création: " + dossier.getDateDeCreation());

            // Antécédents
            List<Antecedents> antecedents = antecedentsRepo.findByDossierMedicalId(dossier.getIdDM());
            System.out.println("\nAntécédents (" + antecedents.size() + "):");
            for (Antecedents a : antecedents) {
                System.out.println("  • " + a.getNom() + " (" + a.getCategorie() + ", " +
                        a.getNiveauDeRisque() + ")");
            }

            // Consultations
            List<Consultation> consultations = consultationRepo.findByDossierMedicalId(dossier.getIdDM());
            System.out.println("\nConsultations (" + consultations.size() + "):");
            for (Consultation c : consultations) {
                System.out.println("  • " + c.getDateConsultation() + " - " +
                        c.getStatut() + " - " +
                        (c.getObservation() != null && c.getObservation().length() > 30 ?
                                c.getObservation().substring(0, 30) + "..." :
                                c.getObservation()));
            }
        }
    }

    private static void rechercherPatientMedecin() {
        System.out.println("\n=== RECHERCHE PATIENT ===");

        System.out.print("Nom ou prénom: ");
        String terme = scanner.nextLine();

        List<Patient> resultats = patientRepo.findByNom(terme);
        System.out.println("\nRésultats trouvés: " + resultats.size());

        for (Patient p : resultats) {
            System.out.println("• " + p.getId() + " - " + p.getNom() + " " + p.getPrenom() +
                    " - Tel: " + p.getTelephone());
        }
    }

    private static void menuAgendaMedecin() {
        System.out.println("\n=== MON AGENDA ===");

        System.out.println("1. Agenda du jour");
        System.out.println("2. Agenda de la semaine");
        System.out.println("3. Prochains RDV");
        System.out.print("Choix: ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1: afficherAgendaDuJour(); break;
            case 2: afficherAgendaSemaine(); break;
            case 3: afficherProchainsRDV(); break;
            default: System.out.println("Choix invalide");
        }
    }

    private static void afficherAgendaDuJour() {
        System.out.println("\n=== AGENDA DU JOUR (" + LocalDate.now() + ") ===");

        List<RDV> rdvs = rdvRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream()
                .filter(r -> r.getDateRDV().equals(LocalDate.now()))
                .sorted((r1, r2) -> r1.getHeureRDV().compareTo(r2.getHeureRDV()))
                .toList();

        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous aujourd'hui");
            return;
        }

        System.out.println("Heure | Patient | Motif | Statut");
        System.out.println("---------------------------------");

        for (RDV rdv : rdvs) {
            try {
                Patient patient = getPatientByDossierId(rdv.getIdDM());
                String nomPatient = patient != null ? patient.getNom() + " " + patient.getPrenom() : "Inconnu";
                System.out.printf("%-5s %-20s %-15s %s%n",
                        rdv.getHeureRDV(),
                        nomPatient,
                        rdv.getMotif(),
                        rdv.getStatut());
            } catch (Exception e) {
                System.out.printf("%-5s %-20s %-15s %s%n",
                        rdv.getHeureRDV(),
                        "Patient",
                        rdv.getMotif(),
                        rdv.getStatut());
            }
        }
    }

    private static void afficherAgendaSemaine() {
        System.out.println("\n=== AGENDA DE LA SEMAINE ===");

        LocalDate debut = LocalDate.now();
        LocalDate fin = debut.plusDays(6);

        List<RDV> rdvs = rdvRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream()
                .filter(r -> !r.getDateRDV().isBefore(debut) && !r.getDateRDV().isAfter(fin))
                .sorted((r1, r2) -> {
                    int dateCompare = r1.getDateRDV().compareTo(r2.getDateRDV());
                    return dateCompare != 0 ? dateCompare : r1.getHeureRDV().compareTo(r2.getHeureRDV());
                })
                .toList();

        Map<LocalDate, List<RDV>> rdvsParDate = new TreeMap<>();
        for (RDV rdv : rdvs) {
            rdvsParDate.computeIfAbsent(rdv.getDateRDV(), k -> new ArrayList<>()).add(rdv);
        }

        for (Map.Entry<LocalDate, List<RDV>> entry : rdvsParDate.entrySet()) {
            System.out.println("\n📅 " + entry.getKey() + ":");
            for (RDV rdv : entry.getValue()) {
                System.out.println("  • " + rdv.getHeureRDV() + " - " + rdv.getMotif());
            }
        }
    }

    private static void afficherProchainsRDV() {
        System.out.println("\n=== PROCHAINS RENDEZ-VOUS ===");

        List<RDV> rdvs = rdvRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream()
                .filter(r -> r.getDateRDV().isAfter(LocalDate.now()) ||
                        (r.getDateRDV().equals(LocalDate.now()) &&
                                r.getHeureRDV().isAfter(LocalTime.now())))
                .sorted((r1, r2) -> {
                    int dateCompare = r1.getDateRDV().compareTo(r2.getDateRDV());
                    return dateCompare != 0 ? dateCompare : r1.getHeureRDV().compareTo(r2.getHeureRDV());
                })
                .limit(10)
                .toList();

        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous à venir");
            return;
        }

        for (RDV rdv : rdvs) {
            try {
                Patient patient = getPatientByDossierId(rdv.getIdDM());
                System.out.println("• " + rdv.getDateRDV() + " " + rdv.getHeureRDV() +
                        " - " + (patient != null ? patient.getNom() + " " + patient.getPrenom() : "") +
                        " - " + rdv.getMotif());
            } catch (Exception e) {
                System.out.println("• " + rdv.getDateRDV() + " " + rdv.getHeureRDV() +
                        " - " + rdv.getMotif());
            }
        }
    }

    private static Patient getPatientByDossierId(Long idDM) {
        try {
            DossierMedical dossier = dossierRepo.findById(idDM);
            if (dossier != null) {
                return patientRepo.findById(dossier.getIdPatient());
            }
        } catch (Exception e) {
            // Ignorer
        }
        return null;
    }

    private static void menuConsultationsMedecin() {
        System.out.println("\n=== MES CONSULTATIONS ===");

        System.out.println("1. Consultations du jour");
        System.out.println("2. Historique consultations");
        System.out.println("3. Créer une consultation");
        System.out.print("Choix: ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1: consultationsDuJour(); break;
            case 2: historiqueConsultations(); break;
            case 3: creerConsultation(); break;
            default: System.out.println("Choix invalide");
        }
    }

    private static void consultationsDuJour() {
        System.out.println("\n=== CONSULTATIONS DU JOUR ===");

        List<Consultation> consultations = consultationRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream()
                .filter(c -> c.getDateConsultation().equals(LocalDate.now()))
                .toList();

        System.out.println("Consultations aujourd'hui: " + consultations.size());

        for (Consultation c : consultations) {
            try {
                Patient patient = getPatientByDossierId(c.getIdDM());
                System.out.println("• " + c.getDateConsultation() + " - " +
                        (patient != null ? patient.getNom() + " " + patient.getPrenom() : "") +
                        " - " + c.getStatut());
            } catch (Exception e) {
                System.out.println("• " + c.getDateConsultation() + " - Consultation");
            }
        }
    }

    private static void historiqueConsultations() {
        System.out.println("\n=== HISTORIQUE DES CONSULTATIONS ===");

        List<Consultation> consultations = consultationRepo.findByMedecinId(utilisateurConnecte.getId())
                .stream()
                .sorted((c1, c2) -> c2.getDateConsultation().compareTo(c1.getDateConsultation()))
                .limit(20)
                .toList();

        for (Consultation c : consultations) {
            try {
                Patient patient = getPatientByDossierId(c.getIdDM());
                System.out.println("• " + c.getDateConsultation() + " - " +
                        (patient != null ? patient.getNom() + " " + patient.getPrenom() : "") +
                        " - " + c.getStatut() +
                        (c.getObservation() != null ? " - " +
                                c.getObservation().substring(0, Math.min(30, c.getObservation().length())) : ""));
            } catch (Exception e) {
                System.out.println("• " + c.getDateConsultation() + " - " + c.getStatut());
            }
        }
    }

    private static void creerConsultation() {
        System.out.println("\n=== CRÉATION D'UNE CONSULTATION ===");

        try {
            System.out.print("ID du patient: ");
            Long patientId = scanner.nextLong();
            scanner.nextLine();

            Patient patient = patientRepo.findById(patientId);
            if (patient == null) {
                System.out.println("❌ Patient non trouvé");
                return;
            }

            System.out.println("Patient: " + patient.getNom() + " " + patient.getPrenom());

            System.out.print("Observation: ");
            String observation = scanner.nextLine();

            System.out.print("Statut (PLANIFIEE/TERMINEE/ANNULEE) [PLANIFIEE]: ");
            String statutStr = scanner.nextLine();
            if (statutStr.isEmpty()) statutStr = "PLANIFIEE";
            StatutConsultation statut = StatutConsultation.valueOf(statutStr.toUpperCase());

            // Trouver le dossier médical
            DossierMedical dossier = dossierRepo.findByPatientId(patientId);
            if (dossier == null) {
                // Créer un dossier si nécessaire
                dossier = DossierMedical.builder()
                        .idPatient(patientId)
                        .dateDeCreation(LocalDate.now())
                        .build();
                dossierRepo.create(dossier);
            }

            Consultation consultation = Consultation.builder()
                    .idDM(dossier.getIdDM())
                    .idMedecin(utilisateurConnecte.getId())
                    .dateConsultation(LocalDate.now())
                    .statut(statut)
                    .observation(observation)
                    .build();

            consultationRepo.create(consultation);

            System.out.println("✅ Consultation créée avec succès !");
            System.out.println("ID Consultation: " + consultation.getIdConsultation());

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void menuOrdonnancesMedecin() {
        System.out.println("\n=== ORDONNANCES ===");

        System.out.println("1. Créer une ordonnance");
        System.out.println("2. Historique des ordonnances");
        System.out.println("3. Liste des médicaments");
        System.out.print("Choix: ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1: creerOrdonnance(); break;
            case 2: historiqueOrdonnances(); break;
            case 3: listeMedicaments(); break;
            default: System.out.println("Choix invalide");
        }
    }

    private static void creerOrdonnance() {
        System.out.println("\n=== CRÉATION D'UNE ORDONNANCE ===");

        try {
            System.out.print("ID du patient: ");
            Long patientId = scanner.nextLong();
            scanner.nextLine();

            Patient patient = patientRepo.findById(patientId);
            if (patient == null) {
                System.out.println("❌ Patient non trouvé");
                return;
            }

            System.out.println("Patient: " + patient.getNom() + " " + patient.getPrenom());

            System.out.print("Motif de consultation: ");
            String motif = scanner.nextLine();

            // Trouver le dossier médical
            DossierMedical dossier = dossierRepo.findByPatientId(patientId);
            if (dossier == null) {
                System.out.println("❌ Aucun dossier médical trouvé");
                return;
            }

            // Créer l'ordonnance
            Ordonnance ordonnance = Ordonnance.builder()
                    .idDM(dossier.getIdDM())
                    .idMedecin(utilisateurConnecte.getId())
                    .dateOrdonnance(LocalDate.now())
                    .build();

            ordonnanceRepo.create(ordonnance);

            // Ajouter des prescriptions
            boolean continuer = true;
            while (continuer) {
                System.out.println("\n➕ AJOUTER UN MÉDICAMENT");

                System.out.print("Nom du médicament (ou 'fin' pour terminer): ");
                String nomMedicament = scanner.nextLine();

                if (nomMedicament.equalsIgnoreCase("fin")) {
                    continuer = false;
                    continue;
                }

                // Rechercher le médicament
                List<Medicament> medicaments = medicamentRepo.findByNom(nomMedicament);
                if (medicaments.isEmpty()) {
                    System.out.println("❌ Médicament non trouvé");
                    continue;
                }

                System.out.println("Médicaments trouvés:");
                for (int i = 0; i < medicaments.size(); i++) {
                    Medicament m = medicaments.get(i);
                    System.out.println((i+1) + ". " + m.getNom() + " (" + m.getType() + ") - " +
                            m.getPrixUnitaire() + " DH");
                }

                System.out.print("Choisir le médicament (numéro): ");
                int choixMed = scanner.nextInt();
                scanner.nextLine();

                if (choixMed < 1 || choixMed > medicaments.size()) {
                    System.out.println("❌ Choix invalide");
                    continue;
                }

                Medicament medicament = medicaments.get(choixMed - 1);

                System.out.print("Quantité: ");
                int quantite = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Fréquence (ex: 3x/jour): ");
                String frequence = scanner.nextLine();

                System.out.print("Durée en jours: ");
                int duree = scanner.nextInt();
                scanner.nextLine();

                // Créer la prescription
                Prescription prescription = Prescription.builder()
                        .idOrdo(ordonnance.getIdOrdo())
                        .idMedicament(medicament.getIdMedicament())
                        .quantite(quantite)
                        .frequence(frequence)
                        .dureeEnJours(duree)
                        .build();

                prescriptionRepo.create(prescription);
                System.out.println("✅ Médicament ajouté à l'ordonnance");
            }

            System.out.println("\n✅ Ordonnance créée avec succès !");
            System.out.println("ID Ordonnance: " + ordonnance.getIdOrdo());
            System.out.println("Date: " + ordonnance.getDateOrdonnance());

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void historiqueOrdonnances() {
        System.out.println("\n=== HISTORIQUE DES ORDONNANCES ===");

        List<Ordonnance> ordonnances = ordonnanceRepo.findByMedecin(utilisateurConnecte.getId())
                .stream()
                .sorted((o1, o2) -> o2.getDateOrdonnance().compareTo(o1.getDateOrdonnance()))
                .limit(10)
                .toList();

        System.out.println("Ordonnances créées: " + ordonnances.size());

        for (Ordonnance o : ordonnances) {
            try {
                DossierMedical dossier = dossierRepo.findById(o.getIdDM());
                if (dossier != null) {
                    Patient patient = patientRepo.findById(dossier.getIdPatient());
                    System.out.println("• " + o.getDateOrdonnance() + " - " +
                            (patient != null ? patient.getNom() + " " + patient.getPrenom() : "") +
                            " (Ordo #" + o.getIdOrdo() + ")");
                }
            } catch (Exception e) {
                System.out.println("• " + o.getDateOrdonnance() + " - Ordonnance #" + o.getIdOrdo());
            }
        }
    }

    private static void listeMedicaments() {
        System.out.println("\n=== LISTE DES MÉDICAMENTS ===");

        List<Medicament> medicaments = medicamentRepo.findAll()
                .stream()
                .limit(20)
                .toList();

        System.out.println("Médicaments disponibles: " + medicaments.size());

        for (Medicament m : medicaments) {
            System.out.println("• " + m.getNom() + " (" + m.getType() + ") - " +
                    m.getForme() + " - " + m.getPrixUnitaire() + " DH" +
                    (m.getRemboursable() ? " [Remboursable]" : ""));
        }
    }

    private static void menuCertificatsMedecin() {
        System.out.println("\n=== CERTIFICATS ===");

        System.out.println("1. Créer un certificat");
        System.out.println("2. Modifier un certificat");
        System.out.println("3. Supprimer un certificat");
        System.out.println("4. Liste des certificats");
        System.out.print("Choix: ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1: creerCertificat(); break;
            case 2: modifierCertificat(); break;
            case 3: supprimerCertificat(); break;
            case 4: listeCertificats(); break;
            default: System.out.println("Choix invalide");
        }
    }

    private static void creerCertificat() {
        System.out.println("\n=== CRÉATION D'UN CERTIFICAT ===");

        try {
            System.out.print("ID du patient: ");
            Long patientId = scanner.nextLong();
            scanner.nextLine();

            Patient patient = patientRepo.findById(patientId);
            if (patient == null) {
                System.out.println("❌ Patient non trouvé");
                return;
            }

            System.out.println("Patient: " + patient.getNom() + " " + patient.getPrenom());

            System.out.print("Date de début (AAAA-MM-JJ): ");
            LocalDate dateDebut = LocalDate.parse(scanner.nextLine());

            System.out.print("Date de fin (AAAA-MM-JJ): ");
            LocalDate dateFin = LocalDate.parse(scanner.nextLine());

            int duree = (int) java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin);

            System.out.print("Note du médecin: ");
            String note = scanner.nextLine();

            // Trouver le dossier médical
            DossierMedical dossier = dossierRepo.findByPatientId(patientId);
            if (dossier == null) {
                System.out.println("❌ Aucun dossier médical trouvé");
                return;
            }

            Certificat certificat = Certificat.builder()
                    .idDM(dossier.getIdDM())
                    .idMedecin(utilisateurConnecte.getId())
                    .dateDebut(dateDebut)
                    .dateFin(dateFin)
                    .duree(duree)
                    .noteMedecin(note)
                    .build();

            certifRepo.create(certificat);

            System.out.println("\n✅ Certificat créé avec succès !");
            System.out.println("ID Certificat: " + certificat.getIdCertif());
            System.out.println("Durée: " + duree + " jours");
            System.out.println("Période: " + dateDebut + " au " + dateFin);

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void modifierCertificat() {
        System.out.println("\n=== MODIFICATION D'UN CERTIFICAT ===");

        try {
            System.out.print("ID du certificat à modifier: ");
            Long idCertif = scanner.nextLong();
            scanner.nextLine();

            Certificat certificat = certifRepo.findById(idCertif);
            if (certificat == null) {
                System.out.println("❌ Certificat non trouvé");
                return;
            }

            if (!certificat.getIdMedecin().equals(utilisateurConnecte.getId())) {
                System.out.println("❌ Vous n'êtes pas autorisé à modifier ce certificat");
                return;
            }

            System.out.println("\nCertificat actuel:");
            System.out.println("Date début: " + certificat.getDateDebut());
            System.out.println("Date fin: " + certificat.getDateFin());
            System.out.println("Note: " + certificat.getNoteMedecin());

            System.out.println("\nNouvelles valeurs (laisser vide pour ne pas modifier):");

            System.out.print("Date début [" + certificat.getDateDebut() + "]: ");
            String dateDebutStr = scanner.nextLine();
            if (!dateDebutStr.isEmpty()) {
                certificat.setDateDebut(LocalDate.parse(dateDebutStr));
            }

            System.out.print("Date fin [" + certificat.getDateFin() + "]: ");
            String dateFinStr = scanner.nextLine();
            if (!dateFinStr.isEmpty()) {
                certificat.setDateFin(LocalDate.parse(dateFinStr));
            }

            certificat.setDuree((int) java.time.temporal.ChronoUnit.DAYS.between(
                    certificat.getDateDebut(), certificat.getDateFin()));

            System.out.print("Note [" + certificat.getNoteMedecin() + "]: ");
            String note = scanner.nextLine();
            if (!note.isEmpty()) {
                certificat.setNoteMedecin(note);
            }

            certifRepo.update(certificat);
            System.out.println("✅ Certificat modifié avec succès");

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void supprimerCertificat() {
        System.out.println("\n=== SUPPRESSION D'UN CERTIFICAT ===");

        try {
            System.out.print("ID du certificat à supprimer: ");
            Long idCertif = scanner.nextLong();
            scanner.nextLine();

            Certificat certificat = certifRepo.findById(idCertif);
            if (certificat == null) {
                System.out.println("❌ Certificat non trouvé");
                return;
            }

            if (!certificat.getIdMedecin().equals(utilisateurConnecte.getId())) {
                System.out.println("❌ Vous n'êtes pas autorisé à supprimer ce certificat");
                return;
            }

            System.out.println("\n⚠ ATTENTION: Vous allez supprimer le certificat #" + idCertif);
            System.out.print("Confirmer la suppression (oui/non)? ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("oui")) {
                certifRepo.delete(certificat);
                System.out.println("✅ Certificat supprimé avec succès");
            } else {
                System.out.println("❌ Suppression annulée");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void listeCertificats() {
        System.out.println("\n=== LISTE DES CERTIFICATS ===");

        List<Certificat> certificats = certifRepo.findByIdMedecin(utilisateurConnecte.getId())
                .stream()
                .sorted((c1, c2) -> c2.getDateDebut().compareTo(c1.getDateDebut()))
                .limit(10)
                .toList();

        System.out.println("Certificats délivrés: " + certificats.size());

        for (Certificat c : certificats) {
            try {
                DossierMedical dossier = dossierRepo.findById(c.getIdDM());
                if (dossier != null) {
                    Patient patient = patientRepo.findById(dossier.getIdPatient());
                    System.out.println("• Certificat #" + c.getIdCertif() + " - " +
                            (patient != null ? patient.getNom() + " " + patient.getPrenom() : "") +
                            " - " + c.getDateDebut() + " au " + c.getDateFin());
                }
            } catch (Exception e) {
                System.out.println("• Certificat #" + c.getIdCertif() + " - " +
                        c.getDateDebut() + " au " + c.getDateFin());
            }
        }
    }

    private static void testerStatistiquesMedecin() {
        System.out.println("\n=== MES STATISTIQUES ===");

        // Interventions du médecin
        List<InterventionMedecin> interventions = interventionRepo.findByIdMedecin(utilisateurConnecte.getId());
        double revenuTotal = interventions.stream()
                .mapToDouble(InterventionMedecin::getPrixIntervention)
                .sum();

        // Consultations
        List<Consultation> consultations = consultationRepo.findByMedecinId(utilisateurConnecte.getId());
        long consultationsMois = consultations.stream()
                .filter(c -> c.getDateConsultation().getMonth() == LocalDate.now().getMonth())
                .count();

        System.out.println("Interventions réalisées: " + interventions.size());
        System.out.println("Consultations ce mois: " + consultationsMois);
        System.out.println("Revenu total: " + revenuTotal + " DH");
        System.out.println("Moyenne par intervention: " +
                (interventions.isEmpty() ? 0 : revenuTotal / interventions.size()) + " DH");

        // Patients uniques
        Set<Long> patientsUniques = new HashSet<>();
        for (Consultation c : consultations) {
            try {
                DossierMedical dossier = dossierRepo.findById(c.getIdDM());
                if (dossier != null) {
                    patientsUniques.add(dossier.getIdPatient());
                }
            } catch (Exception e) {
                // Ignorer
            }
        }
        System.out.println("Patients uniques traités: " + patientsUniques.size());
    }

    // ============================================
    // SECRÉTAIRE : FONCTIONNALITÉS COMPLÈTES
    // ============================================
    private static void traiterChoixSecretaire(int choix) {
        switch (choix) {
            case 1: menuGestionPatientsSecretaire(); break;
            case 2: menuGestionRDVSecretaire(); break;
            case 3: menuGestionCaisse(); break;
            case 4: testerAccueil(); break;
            case 5: testerStatistiquesSecretaire(); break;
            case 0: break;
            default: System.out.println("Choix invalide!");
        }
    }

    private static void menuGestionPatientsSecretaire() {
        int choix;
        do {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║              👥 GESTION PATIENTS                     ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("1.  Lister tous les patients");
            System.out.println("2.  Rechercher un patient");
            System.out.println("3.  Créer un patient");
            System.out.println("4.  Modifier un patient");
            System.out.println("5.  Supprimer un patient");
            System.out.println("0.  Retour");
            System.out.print("\nChoix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1: listerTousPatients(); break;
                case 2: rechercherPatientSecretaire(); break;
                case 3: creerPatientSecretaire(); break;
                case 4: modifierPatientSecretaire(); break;
                case 5: supprimerPatientSecretaire(); break;
                case 0: break;
                default: System.out.println("Choix invalide!");
            }
        } while (choix != 0);
    }

    private static void listerTousPatients() {
        System.out.println("\n=== LISTE COMPLÈTE DES PATIENTS ===");

        List<Patient> patients = patientRepo.findAll();
        System.out.println("Total patients: " + patients.size());

        System.out.println("\nID | Nom | Prénom | Téléphone | Email");
        System.out.println("----------------------------------------");

        for (Patient p : patients) {
            System.out.printf("%-3d %-15s %-15s %-12s %s%n",
                    p.getId(),
                    p.getNom(),
                    p.getPrenom(),
                    p.getTelephone() != null ? p.getTelephone() : "N/A",
                    p.getEmail() != null ? p.getEmail() : "N/A");
        }
    }

    private static void rechercherPatientSecretaire() {
        System.out.println("\n=== RECHERCHE DE PATIENT ===");

        System.out.println("1. Par nom");
        System.out.println("2. Par téléphone");
        System.out.println("3. Par email");
        System.out.print("Choix: ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Terme de recherche: ");
        String terme = scanner.nextLine();

        List<Patient> resultats = new ArrayList<>();

        switch (choix) {
            case 1:
                resultats = patientRepo.findByNom(terme);
                break;
            case 2:
                // Recherche personnalisée par téléphone
                resultats = patientRepo.findAll().stream()
                        .filter(p -> p.getTelephone() != null && p.getTelephone().contains(terme))
                        .toList();
                break;
            case 3:
                // Recherche personnalisée par email
                resultats = patientRepo.findAll().stream()
                        .filter(p -> p.getEmail() != null && p.getEmail().contains(terme))
                        .toList();
                break;
            default:
                System.out.println("❌ Choix invalide");
                return;
        }

        System.out.println("\nRésultats trouvés: " + resultats.size());

        for (Patient p : resultats) {
            System.out.println("• " + p.getId() + " - " + p.getNom() + " " + p.getPrenom() +
                    " - Tel: " + p.getTelephone() +
                    " - Email: " + p.getEmail());
        }
    }

    private static void creerPatientSecretaire() {
        System.out.println("\n=== CRÉATION D'UN PATIENT ===");

        try {
            System.out.print("Nom: ");
            String nom = scanner.nextLine();

            System.out.print("Prénom: ");
            String prenom = scanner.nextLine();

            System.out.print("Date de naissance (AAAA-MM-JJ): ");
            String dateStr = scanner.nextLine();
            LocalDate dateNaissance = null;
            if (!dateStr.isEmpty()) {
                dateNaissance = LocalDate.parse(dateStr);
            }

            System.out.print("Sexe (HOMME/FEMME): ");
            String sexeStr = scanner.nextLine();
            Sexe sexe = Sexe.valueOf(sexeStr.toUpperCase());

            System.out.print("Téléphone: ");
            String telephone = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Adresse: ");
            String adresse = scanner.nextLine();

            System.out.print("Assurance (CNOPS/CNSS/Privée/Aucune): ");
            String assuranceStr = scanner.nextLine();
            Assurance assurance = Assurance.valueOf(assuranceStr.toUpperCase());

            Patient patient = Patient.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .dateNaissance(dateNaissance)
                    .sexe(sexe)
                    .telephone(telephone)
                    .email(email)
                    .adresse(adresse)
                    .assurance(assurance)
                    .build();

            patientRepo.create(patient);

            // Créer automatiquement un dossier médical
            DossierMedical dossier = DossierMedical.builder()
                    .idPatient(patient.getId())
                    .dateDeCreation(LocalDate.now())
                    .build();

            dossierRepo.create(dossier);

            // Créer une situation financière par défaut
            SituationFinanciere sf = SituationFinanciere.builder()
                    .idDM(dossier.getIdDM())
                    .totalDesActes(0.0)
                    .totalPaye(0.0)
                    .resteDu(0.0)
                    .creance(0.0)
                    .statut(StatutSituationFinanciere.SOLDE)
                    .enPromo(false)
                    .build();

            sfRepo.create(sf);

            System.out.println("\n✅ Patient créé avec succès !");
            System.out.println("ID Patient: " + patient.getId());
            System.out.println("ID Dossier: " + dossier.getIdDM());
            System.out.println("ID Situation Financière: " + sf.getIdSF());

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void modifierPatientSecretaire() {
        System.out.println("\n=== MODIFICATION D'UN PATIENT ===");

        try {
            System.out.print("ID du patient à modifier: ");
            Long patientId = scanner.nextLong();
            scanner.nextLine();

            Patient patient = patientRepo.findById(patientId);
            if (patient == null) {
                System.out.println("❌ Patient non trouvé");
                return;
            }

            System.out.println("\nPatient actuel:");
            System.out.println("Nom: " + patient.getNom());
            System.out.println("Prénom: " + patient.getPrenom());
            System.out.println("Téléphone: " + patient.getTelephone());
            System.out.println("Email: " + patient.getEmail());
            System.out.println("Assurance: " + patient.getAssurance());

            System.out.println("\nNouvelles valeurs (laisser vide pour ne pas modifier):");

            System.out.print("Nom [" + patient.getNom() + "]: ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) patient.setNom(nom);

            System.out.print("Prénom [" + patient.getPrenom() + "]: ");
            String prenom = scanner.nextLine();
            if (!prenom.isEmpty()) patient.setPrenom(prenom);

            System.out.print("Téléphone [" + patient.getTelephone() + "]: ");
            String telephone = scanner.nextLine();
            if (!telephone.isEmpty()) patient.setTelephone(telephone);

            System.out.print("Email [" + patient.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) patient.setEmail(email);

            System.out.print("Adresse [" + patient.getAdresse() + "]: ");
            String adresse = scanner.nextLine();
            if (!adresse.isEmpty()) patient.setAdresse(adresse);

            patientRepo.update(patient);
            System.out.println("✅ Patient modifié avec succès");

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void supprimerPatientSecretaire() {
        System.out.println("\n=== SUPPRESSION D'UN PATIENT ===");

        try {
            System.out.print("ID du patient à supprimer: ");
            Long patientId = scanner.nextLong();
            scanner.nextLine();

            Patient patient = patientRepo.findById(patientId);
            if (patient == null) {
                System.out.println("❌ Patient non trouvé");
                return;
            }

            System.out.println("\n⚠ ATTENTION: Vous allez supprimer:");
            System.out.println("Nom: " + patient.getNom() + " " + patient.getPrenom());
            System.out.println("Téléphone: " + patient.getTelephone());
            System.out.println("Email: " + patient.getEmail());

            System.out.print("\nConfirmer la suppression (oui/non)? ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("oui")) {
                // Supprimer d'abord le dossier médical (cascade)
                DossierMedical dossier = dossierRepo.findByPatientId(patientId);
                if (dossier != null) {
                    dossierRepo.delete(dossier);
                }

                // Supprimer le patient
                patientRepo.delete(patient);
                System.out.println("✅ Patient supprimé avec succès");
            } else {
                System.out.println("❌ Suppression annulée");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void menuGestionRDVSecretaire() {
        int choix;
        do {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║           📅 GESTION RENDEZ-VOUS                      ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("1.  Voir RDV du jour");
            System.out.println("2.  Créer un RDV");
            System.out.println("3.  Modifier un RDV");
            System.out.println("4.  Annuler un RDV");
            System.out.println("5.  RDV à venir");
            System.out.println("0.  Retour");
            System.out.print("\nChoix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1: voirRDVDuJour(); break;
                case 2: creerRDV(); break;
                case 3: modifierRDV(); break;
                case 4: annulerRDV(); break;
                case 5: rdvAVenir(); break;
                case 0: break;
                default: System.out.println("Choix invalide!");
            }
        } while (choix != 0);
    }

    private static void voirRDVDuJour() {
        System.out.println("\n=== RENDEZ-VOUS DU JOUR ===");

        List<RDV> rdvs = rdvRepo.findByDate(LocalDate.now())
                .stream()
                .sorted((r1, r2) -> r1.getHeureRDV().compareTo(r2.getHeureRDV()))
                .toList();

        System.out.println("RDV aujourd'hui: " + rdvs.size());

        System.out.println("\nHeure | Patient | Médecin | Motif | Statut");
        System.out.println("-------------------------------------------");

        for (RDV rdv : rdvs) {
            try {
                Patient patient = getPatientByDossierId(rdv.getIdDM());
                String nomPatient = patient != null ? patient.getNom() + " " + patient.getPrenom() : "Inconnu";

                Utilisateur medecin = authRepo.findById(rdv.getIdMedecin());
                String nomMedecin = medecin != null ? "Dr. " + medecin.getNom() : "Médecin";

                System.out.printf("%-5s %-20s %-15s %-15s %s%n",
                        rdv.getHeureRDV(),
                        nomPatient,
                        nomMedecin,
                        rdv.getMotif(),
                        rdv.getStatut());
            } catch (Exception e) {
                System.out.printf("%-5s %-20s %-15s %-15s %s%n",
                        rdv.getHeureRDV(),
                        "Patient",
                        "Médecin",
                        rdv.getMotif(),
                        rdv.getStatut());
            }
        }
    }

    private static void creerRDV() {
        System.out.println("\n=== CRÉATION D'UN RENDEZ-VOUS ===");

        try {
            System.out.print("ID du patient: ");
            Long patientId = scanner.nextLong();
            scanner.nextLine();

            Patient patient = patientRepo.findById(patientId);
            if (patient == null) {
                System.out.println("❌ Patient non trouvé");
                return;
            }

            System.out.println("Patient: " + patient.getNom() + " " + patient.getPrenom());

            // Lister les médecins
            List<Utilisateur> medecins = getUsersByRole("MEDECIN");
            System.out.println("\nMédecins disponibles:");
            for (int i = 0; i < medecins.size(); i++) {
                System.out.println((i+1) + ". Dr. " + medecins.get(i).getNom() + " " + medecins.get(i).getPrenom());
            }

            System.out.print("Choisir le médecin (numéro): ");
            int choixMed = scanner.nextInt();
            scanner.nextLine();

            if (choixMed < 1 || choixMed > medecins.size()) {
                System.out.println("❌ Choix invalide");
                return;
            }

            Utilisateur medecin = medecins.get(choixMed - 1);

            System.out.print("Date du RDV (AAAA-MM-JJ): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Heure du RDV (HH:MM): ");
            LocalTime heure = LocalTime.parse(scanner.nextLine());

            System.out.print("Motif: ");
            String motif = scanner.nextLine();

            // Trouver le dossier médical
            DossierMedical dossier = dossierRepo.findByPatientId(patientId);
            if (dossier == null) {
                System.out.println("❌ Aucun dossier médical trouvé");
                return;
            }

            RDV rdv = RDV.builder()
                    .idDM(dossier.getIdDM())
                    .idMedecin(medecin.getId())
                    .dateRDV(date)
                    .heureRDV(heure)
                    .motif(motif)
                    .statut(StatutRDV.PLANIFIE)
                    .noteMedecin("")
                    .build();

            rdvRepo.create(rdv);

            System.out.println("\n✅ Rendez-vous créé avec succès !");
            System.out.println("ID RDV: " + rdv.getIdRDV());
            System.out.println("Date: " + date + " " + heure);
            System.out.println("Médecin: Dr. " + medecin.getNom() + " " + medecin.getPrenom());

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void modifierRDV() {
        System.out.println("\n=== MODIFICATION D'UN RENDEZ-VOUS ===");

        try {
            System.out.print("ID du RDV à modifier: ");
            Long rdvId = scanner.nextLong();
            scanner.nextLine();

            RDV rdv = rdvRepo.findById(rdvId);
            if (rdv == null) {
                System.out.println("❌ RDV non trouvé");
                return;
            }

            System.out.println("\nRDV actuel:");
            System.out.println("Date: " + rdv.getDateRDV());
            System.out.println("Heure: " + rdv.getHeureRDV());
            System.out.println("Motif: " + rdv.getMotif());
            System.out.println("Statut: " + rdv.getStatut());

            System.out.println("\nNouvelles valeurs (laisser vide pour ne pas modifier):");

            System.out.print("Date [" + rdv.getDateRDV() + "]: ");
            String dateStr = scanner.nextLine();
            if (!dateStr.isEmpty()) {
                rdv.setDateRDV(LocalDate.parse(dateStr));
            }

            System.out.print("Heure [" + rdv.getHeureRDV() + "]: ");
            String heureStr = scanner.nextLine();
            if (!heureStr.isEmpty()) {
                rdv.setHeureRDV(LocalTime.parse(heureStr));
            }

            System.out.print("Motif [" + rdv.getMotif() + "]: ");
            String motif = scanner.nextLine();
            if (!motif.isEmpty()) {
                rdv.setMotif(motif);
            }

            System.out.print("Statut (PLANIFIE/CONFIRME/ANNULE/TERMINE) [" + rdv.getStatut() + "]: ");
            String statutStr = scanner.nextLine();
            if (!statutStr.isEmpty()) {
                rdv.setStatut(StatutRDV.valueOf(statutStr.toUpperCase()));
            }

            rdvRepo.update(rdv);
            System.out.println("✅ RDV modifié avec succès");

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void annulerRDV() {
        System.out.println("\n=== ANNULATION D'UN RENDEZ-VOUS ===");

        try {
            System.out.print("ID du RDV à annuler: ");
            Long rdvId = scanner.nextLong();
            scanner.nextLine();

            RDV rdv = rdvRepo.findById(rdvId);
            if (rdv == null) {
                System.out.println("❌ RDV non trouvé");
                return;
            }

            System.out.println("\nRDV à annuler:");
            System.out.println("Date: " + rdv.getDateRDV() + " " + rdv.getHeureRDV());
            System.out.println("Motif: " + rdv.getMotif());

            System.out.print("\nConfirmer l'annulation (oui/non)? ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("oui")) {
                rdv.setStatut(StatutRDV.ANNULE);
                rdvRepo.update(rdv);
                System.out.println("✅ RDV annulé avec succès");
            } else {
                System.out.println("❌ Annulation annulée");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void rdvAVenir() {
        System.out.println("\n=== RENDEZ-VOUS À VENIR ===");

        LocalDate fin = LocalDate.now().plusDays(7);
        List<RDV> rdvs = rdvRepo.findByDateRange(LocalDate.now(), fin)
                .stream()
                .filter(r -> r.getStatut() != StatutRDV.ANNULE && r.getStatut() != StatutRDV.TERMINE)
                .sorted((r1, r2) -> {
                    int dateCompare = r1.getDateRDV().compareTo(r2.getDateRDV());
                    return dateCompare != 0 ? dateCompare : r1.getHeureRDV().compareTo(r2.getHeureRDV());
                })
                .toList();

        System.out.println("RDV à venir (7 jours): " + rdvs.size());

        for (RDV rdv : rdvs) {
            try {
                Patient patient = getPatientByDossierId(rdv.getIdDM());
                Utilisateur medecin = authRepo.findById(rdv.getIdMedecin());

                System.out.println("• " + rdv.getDateRDV() + " " + rdv.getHeureRDV() +
                        " - " + (patient != null ? patient.getNom() + " " + patient.getPrenom() : "") +
                        " - Dr. " + (medecin != null ? medecin.getNom() : "") +
                        " - " + rdv.getMotif() + " (" + rdv.getStatut() + ")");
            } catch (Exception e) {
                System.out.println("• " + rdv.getDateRDV() + " " + rdv.getHeureRDV() +
                        " - " + rdv.getMotif() + " (" + rdv.getStatut() + ")");
            }
        }
    }

    private static void menuGestionCaisse() {
        int choix;
        do {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║               💰 GESTION CAISSE                       ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("1.  Factures en attente");
            System.out.println("2.  Enregistrer un paiement");
            System.out.println("3.  Historique des paiements");
            System.out.println("4.  Situation financière patients");
            System.out.println("0.  Retour");
            System.out.print("\nChoix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1: facturesEnAttente(); break;
                case 2: enregistrerPaiement(); break;
                case 3: historiquePaiements(); break;
                case 4: situationFinancierePatients(); break;
                case 0: break;
                default: System.out.println("Choix invalide!");
            }
        } while (choix != 0);
    }

    private static void facturesEnAttente() {
        System.out.println("\n=== FACTURES EN ATTENTE ===");

        List<Facture> factures = factureRepo.findByStatut(StatutFacture.EN_ATTENTE);
        System.out.println("Factures en attente: " + factures.size());

        double totalImpaye = 0;
        System.out.println("\nID Facture | Patient | Montant | Reste");
        System.out.println("---------------------------------------");

        for (Facture f : factures) {
            try {
                // Trouver le patient via la consultation
                Consultation consultation = consultationRepo.findById(f.getIdConsultation());
                if (consultation != null) {
                    DossierMedical dossier = dossierRepo.findById(consultation.getIdDM());
                    if (dossier != null) {
                        Patient patient = patientRepo.findById(dossier.getIdPatient());
                        if (patient != null) {
                            System.out.printf("%-10d %-20s %-8.2f %-8.2f%n",
                                    f.getIdFacture(),
                                    patient.getNom() + " " + patient.getPrenom(),
                                    f.getTotalFacture(),
                                    f.getReste());
                            totalImpaye += f.getReste();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.printf("%-10d %-20s %-8.2f %-8.2f%n",
                        f.getIdFacture(),
                        "Patient",
                        f.getTotalFacture(),
                        f.getReste());
                totalImpaye += f.getReste();
            }
        }

        System.out.println("\nTotal à encaisser: " + totalImpaye + " DH");
    }

    private static void enregistrerPaiement() {
        System.out.println("\n=== ENREGISTREMENT D'UN PAIEMENT ===");

        try {
            System.out.print("ID de la facture: ");
            Long factureId = scanner.nextLong();
            scanner.nextLine();

            Facture facture = factureRepo.findById(factureId);
            if (facture == null) {
                System.out.println("❌ Facture non trouvée");
                return;
            }

            System.out.println("\nFacture #" + facture.getIdFacture());
            System.out.println("Montant total: " + facture.getTotalFacture() + " DH");
            System.out.println("Déjà payé: " + facture.getMontantPaye() + " DH");
            System.out.println("Reste à payer: " + facture.getReste() + " DH");
            System.out.println("Statut: " + facture.getStatut());

            System.out.print("\nMontant du paiement: ");
            double montant = scanner.nextDouble();
            scanner.nextLine();

            if (montant <= 0) {
                System.out.println("❌ Montant invalide");
                return;
            }

            // Mettre à jour la facture
            double nouveauPaye = facture.getMontantPaye() + montant;
            double nouveauReste = facture.getTotalFacture() - nouveauPaye;

            facture.setMontantPaye(nouveauPaye);
            facture.setReste(nouveauReste);

            if (nouveauReste <= 0) {
                facture.setStatut(StatutFacture.PAYEE);
            } else {
                facture.setStatut(StatutFacture.PARTIELLEMENT_PAYEE);
            }

            factureRepo.update(facture);

            // Mettre à jour la situation financière
            sfRepo.updateAfterPayment(facture.getIdSF(), montant);

            System.out.println("\n✅ Paiement enregistré avec succès !");
            System.out.println("Nouveau montant payé: " + nouveauPaye + " DH");
            System.out.println("Nouveau reste: " + nouveauReste + " DH");
            System.out.println("Nouveau statut: " + facture.getStatut());

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void historiquePaiements() {
        System.out.println("\n=== HISTORIQUE DES PAIEMENTS ===");

        List<Facture> factures = factureRepo.findAll()
                .stream()
                .filter(f -> f.getMontantPaye() > 0)
                .sorted((f1, f2) -> f2.getDateCreation().compareTo(f1.getDateCreation()))
                .limit(10)
                .toList();

        System.out.println("Derniers paiements: " + factures.size());

        System.out.println("\nDate | Facture | Patient | Payé | Statut");
        System.out.println("-----------------------------------------");

        for (Facture f : factures) {
            try {
                Consultation consultation = consultationRepo.findById(f.getIdConsultation());
                if (consultation != null) {
                    DossierMedical dossier = dossierRepo.findById(consultation.getIdDM());
                    if (dossier != null) {
                        Patient patient = patientRepo.findById(dossier.getIdPatient());
                        if (patient != null) {
                            System.out.printf("%-10s %-8d %-20s %-8.2f %s%n",
                                    f.getDateCreation().toLocalDate(),
                                    f.getIdFacture(),
                                    patient.getNom() + " " + patient.getPrenom(),
                                    f.getMontantPaye(),
                                    f.getStatut());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.printf("%-10s %-8d %-20s %-8.2f %s%n",
                        f.getDateCreation().toLocalDate(),
                        f.getIdFacture(),
                        "Patient",
                        f.getMontantPaye(),
                        f.getStatut());
            }
        }
    }

    private static void situationFinancierePatients() {
        System.out.println("\n=== SITUATION FINANCIÈRE DES PATIENTS ===");

        List<SituationFinanciere> situations = sfRepo.findAll()
                .stream()
                .filter(s -> s.getCreance() > 0)
                .sorted((s1, s2) -> Double.compare(s2.getCreance(), s1.getCreance()))
                .toList();

        System.out.println("Patients avec créances: " + situations.size());

        System.out.println("\nPatient | Total Actes | Payé | Créance | Statut");
        System.out.println("------------------------------------------------");

        for (SituationFinanciere s : situations) {
            try {
                DossierMedical dossier = dossierRepo.findById(s.getIdDM());
                if (dossier != null) {
                    Patient patient = patientRepo.findById(dossier.getIdPatient());
                    if (patient != null) {
                        System.out.printf("%-20s %-12.2f %-8.2f %-8.2f %s%n",
                                patient.getNom() + " " + patient.getPrenom(),
                                s.getTotalDesActes(),
                                s.getTotalPaye(),
                                s.getCreance(),
                                s.getStatut());
                    }
                }
            } catch (Exception e) {
                System.out.printf("%-20s %-12.2f %-8.2f %-8.2f %s%n",
                        "Patient",
                        s.getTotalDesActes(),
                        s.getTotalPaye(),
                        s.getCreance(),
                        s.getStatut());
            }
        }
    }

    private static void testerAccueil() {
        System.out.println("\n=== POSTE D'ACCUEIL ===");
        System.out.println("Bienvenue au poste d'accueil, " + utilisateurConnecte.getPrenom() + "!");
        System.out.println("\n📋 TÂCHES DISPONIBLES:");
        System.out.println("  1. Accueil des patients");
        System.out.println("  2. Prise de rendez-vous");
        System.out.println("  3. Enregistrement des paiements");
        System.out.println("  4. Gestion des appels téléphoniques");
        System.out.println("  5. Envoi de rappels RDV");
        System.out.println("\n📊 STATUT:");
        System.out.println("  • Connecté(e): " + utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
        System.out.println("  • Rôle: " + roleUtilisateur);
        System.out.println("  • RDV aujourd'hui: " + rdvRepo.findByDate(LocalDate.now()).size());
    }

    private static void testerStatistiquesSecretaire() {
        System.out.println("\n=== STATISTIQUES SECRÉTARIAT ===");

        System.out.println("📅 RDV ce mois: " + dashboardRepo.countRDVThisMonth());
        System.out.println("👥 Patients totaux: " + dashboardRepo.countPatientsTotal());
        System.out.println("💰 CA du jour: " + dashboardRepo.calculateCAJour() + " DH");

        double totalImpaye = factureRepo.calculateTotalFacturesImpayees();
        System.out.println("💳 Montant impayé: " + totalImpaye + " DH");

        System.out.println("\n📊 ÉVOLUTION:");

        // Statistiques basiques
        int rdvAujourdhui = rdvRepo.findByDate(LocalDate.now()).size();
        int rdvHier = rdvRepo.findByDate(LocalDate.now().minusDays(1)).size();
        double evolutionRdv = rdvHier > 0 ? ((double)(rdvAujourdhui - rdvHier) / rdvHier) * 100 : 0;

        System.out.println("  • RDV aujourd'hui vs hier: " +
                (evolutionRdv > 0 ? "+" : "") + String.format("%.1f", evolutionRdv) + "%");

        // Patients ce mois (approximatif)
        long patientsNouveaux = patientRepo.findAll().stream()
                .filter(p -> {
                    // Vérifier si le patient a un dossier récent
                    DossierMedical dossier = dossierRepo.findByPatientId(p.getId());
                    return dossier != null &&
                            dossier.getDateDeCreation().getMonth() == LocalDate.now().getMonth();
                })
                .count();
        System.out.println("  • Patients nouveaux ce mois: " + patientsNouveaux);

        // Taux d'occupation (approximatif)
        int rdvConfirmes = rdvRepo.findByStatut(StatutRDV.CONFIRME).size();
        int rdvTotaux = rdvRepo.findAll().size();
        double tauxOccupation = rdvTotaux > 0 ? ((double)rdvConfirmes / rdvTotaux) * 100 : 0;
        System.out.println("  • Taux de confirmation RDV: " + String.format("%.1f", tauxOccupation) + "%");
    }
}