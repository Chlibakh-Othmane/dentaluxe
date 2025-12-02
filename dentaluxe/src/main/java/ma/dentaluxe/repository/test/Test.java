package ma.dentaluxe.repository.test;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.auth.api.AuthRepository;
import ma.dentaluxe.repository.modules.auth.inMemDB_implementation.AuthRepositoryImpl;

import java.util.Scanner;

public class Test {

    private AuthRepository authRepository;

    public Test() {
        this.authRepository = new AuthRepositoryImpl();
    }

    public void startLoginProcess() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     SYSTÈME D'AUTHENTIFICATION - DENTALUXE            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        System.out.print("\n➡️  Entrez votre login : ");
        String login = scanner.nextLine();

        System.out.print("➡️  Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        // 1. Vérifier login vide
        if (login.isEmpty()) {
            System.out.println("\n ERREUR : le login est vide !");
            return;
        }

        // 2. Vérifier si login existe
        if (!authRepository.loginExists(login)) {
            System.out.println("\n ERREUR : ce login n'existe pas !");
            return;
        }

        // 3. Authentifier
        Utilisateur user = authRepository.authenticate(login, password);

        if (user == null) {
            System.out.println("\n ERREUR : mot de passe incorrect !");
            return;
        }

        // 4. Afficher les infos de l'utilisateur
        afficherInfosUtilisateur(user);

        // 5. Récupérer rôle
        String role = authRepository.getUserRole(user.getId());

        if (role == null) {
            System.out.println("\nAucun rôle assigné !");
            return;
        }

        System.out.println("\n Rôle détecté : " + role);

        // 6. Redirection Dashboard
        redirectByRole(role, user);
    }

    private void afficherInfosUtilisateur(Utilisateur user) {

        System.out.println("\n✅ Connexion réussie !");
        System.out.println("┌──────────────────────────────────────────────────────────┐");
        System.out.println("│                    UTILISATEUR CONNECTÉ                  │");
        System.out.println("├──────────────────────────────────────────────────────────┤");
        System.out.println("│ ID        : " + user.getId());
        System.out.println("│ Nom       : " + user.getNom());
        System.out.println("│ Prénom    : " + user.getPrenom());
        System.out.println("│ Email     : " + user.getEmail());
        System.out.println("│ Téléphone : " + user.getTel());
        System.out.println("│ Login     : " + user.getLogin());
        System.out.println("│ Sexe      : " + user.getSexe());
        System.out.println("│ Actif     : " + (user.getActif() ? "Oui" : "Non"));
        System.out.println("└──────────────────────────────────────────────────────────┘");
    }

    private void redirectByRole(String role, Utilisateur user) {

        switch (role.toUpperCase()) {

            case "MEDECIN":
            case "ROLE_MEDECIN":
                afficherDashboardMedecin(user);
                break;

            case "SECRETAIRE":
            case "ROLE_SECRETAIRE":
                afficherDashboardSecretaire(user);
                break;

            case "ADMIN":
            case "ROLE_ADMIN":
                afficherDashboardAdmin(user);
                break;

            default:
                System.out.println("\n Rôle inconnu : " + role);
        }
    }


    private void afficherDashboardMedecin(Utilisateur user) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║            🩺 DASHBOARD MÉDECIN                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue Dr. " + user.getNom() + " " + user.getPrenom());
        System.out.println();
        System.out.println("   📊 STATISTIQUES DU JOUR :");
        System.out.println("      • Consultations du jour    : 10");
        System.out.println("      • Consultations du mois    : 200");
        System.out.println("      • Consultations de l'année : 3000");
        System.out.println();
        System.out.println("   💰 RECETTES :");
        System.out.println("      • Recette du jour    : 2300 DH");
        System.out.println("      • Recette du mois    : 20000 DH");
        System.out.println("      • Recette de l'année : 300000 DH");
        System.out.println();
        System.out.println("   📅 RENDEZ-VOUS :");
        System.out.println("      • RDV du jour        : 10");
        System.out.println("      • RDV annulés        : 5");
        System.out.println();
        System.out.println("   🏥 PATIENTS :");
        System.out.println("      • Nombre total       : 100");
        System.out.println();
        System.out.println("   📋 MENU DISPONIBLE :");
        System.out.println("      1. Mon profil");
        System.out.println("      2. Agenda");
        System.out.println("      3. Caisse");
        System.out.println("      4. Patients");
        System.out.println("      5. Dossier médical");
        System.out.println("      6. Consultations");
        System.out.println("      7. Ordonnances");
        System.out.println("      8. Certificats");
        System.out.println("      9. Paramètres");
        System.out.println("      10. Déconnexion");
    }

    private void afficherDashboardSecretaire(Utilisateur user) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          💼 DASHBOARD SECRÉTAIRE                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue " + user.getPrenom() + " " + user.getNom());
        System.out.println();
        System.out.println("   📊 STATISTIQUES DU JOUR :");
        System.out.println("      • Total des patients     : 100");
        System.out.println("      • Total des rendez-vous  : 200");
        System.out.println("      • Recette du jour        : 2300 DH");
        System.out.println();
        System.out.println("   📅 RENDEZ-VOUS :");
        System.out.println("      • RDV du jour        : 10");
        System.out.println("      • RDV annulés        : 5");
        System.out.println();
        System.out.println("   📋 MENU DISPONIBLE :");
        System.out.println("      1. Mon profil");
        System.out.println("      2. Agenda Médecin");
        System.out.println("      3. Patients");
        System.out.println("      4. Rendez-vous");
        System.out.println("      5. Factures");
        System.out.println("      6. Déconnexion");
    }

    private void afficherDashboardAdmin(Utilisateur user) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║            ⚙️ DASHBOARD ADMIN                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue Administrateur " + user.getNom());
        System.out.println();
        System.out.println("   📊 STATISTIQUES GLOBALES :");
        System.out.println("      • Utilisateurs actifs    : 5");
        System.out.println("      • Médecins              : 2");
        System.out.println("      • Secrétaires           : 2");
        System.out.println("      • Patients              : 100");
        System.out.println();
        System.out.println("   📋 MENU DISPONIBLE :");
        System.out.println("      1. Gestion utilisateurs");
        System.out.println("      2. Gestion des rôles");
        System.out.println("      3. Configuration système");
        System.out.println("      4. Déconnexion");
    }



    public static void main(String[] args) {
        Test app = new Test();
        app.startLoginProcess();
    }
}
