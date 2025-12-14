package ma.dentaluxe.service.testaya;

import ma.dentaluxe.entities.enums.StatutFacture;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;
import ma.dentaluxe.mvc.dto.caisse.*;
import ma.dentaluxe.service.caisse.baseImplementation.CaisseServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TestCaisse {

    private static final CaisseServiceImpl service = new CaisseServiceImpl();
    private static Long currentIdSF = null;

    public static void menu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║      💰 TEST MODULE CAISSE             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Initialiser Situation Financière Patient");
            System.out.println("2. Créer une Facture");
            System.out.println("3. Payer une Facture");
            System.out.println("4. Ajouter une Charge (Dépense)");
            System.out.println("5. Ajouter un Revenu (Autre)");
            System.out.println("6. Voir Historique Financier (Cabinet)");
            System.out.println("0. Retour au Menu Principal");
            System.out.print("👉 Choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1: testInitSituation(scanner); break;
                case 2: testCreerFacture(scanner); break;
                case 3: testPayerFacture(scanner); break;
                case 4: testAjouterCharge(scanner); break;
                case 5: testAjouterRevenu(scanner); break;
                case 6: testHistoriqueCabinet(); break;
                case 0: back = true; break;
                default: System.out.println("❌ Choix invalide !");
            }
        }
    }

    private static void testInitSituation(Scanner scanner) {
        System.out.print("ID Dossier Médical : ");
        Long idDM = scanner.nextLong();
        SituationFinanciereDTO sf = service.initSituation(idDM);
        currentIdSF = sf.getIdSF();
        System.out.println("✅ Situation Financière prête (ID: " + sf.getIdSF() + ")");
        System.out.println("   Statut: " + sf.getStatut() + " | Reste dû: " + sf.getResteDu());
    }

    private static void testCreerFacture(Scanner scanner) {
        if (currentIdSF == null) {
            System.out.println("❌ Initialisez d'abord la situation (Option 1).");
            return;
        }
        System.out.print("Montant total de la facture : ");
        double montant = scanner.nextDouble();

        FactureDTO f = new FactureDTO();
        f.setIdSF(currentIdSF);
        f.setTotalFacture(montant);
        f.setTotalDesActes(montant);
        f.setIdConsultation(1L); // Simulé

        FactureDTO created = service.creerFacture(f);
        System.out.println("✅ Facture créée (ID: " + created.getIdFacture() + ")");
        System.out.println("   À payer: " + created.getReste() + " DH");
    }

    private static void testPayerFacture(Scanner scanner) {
        System.out.print("ID Facture à payer : ");
        Long idF = scanner.nextLong();
        System.out.print("Montant versé : ");
        double versment = scanner.nextDouble();

        service.payerFacture(idF, versment);

        FactureDTO f = service.getFactureById(idF);
        System.out.println("✅ Paiement enregistré.");
        System.out.println("   Nouveau Reste: " + f.getReste() + " DH");
        System.out.println("   Statut Facture: " + f.getStatut());
    }

    private static void testAjouterCharge(Scanner scanner) {
        System.out.println("--- Dépense Cabinet ---");
        System.out.print("Titre (ex: Loyer) : ");
        String titre = scanner.nextLine();
        System.out.print("Montant : ");
        double montant = scanner.nextDouble();

        ChargeDTO c = new ChargeDTO();
        c.setTitre(titre);
        c.setDescription("Dépense courante");
        c.setMontant(montant);

        service.ajouterCharge(c);
        System.out.println("✅ Charge enregistrée : -" + montant + " DH");
    }

    private static void testAjouterRevenu(Scanner scanner) {
        System.out.println("--- Revenu Cabinet (Hors Patients) ---");
        System.out.print("Titre (ex: Subvention) : ");
        String titre = scanner.nextLine();
        System.out.print("Montant : ");
        double montant = scanner.nextDouble();

        RevenuDTO r = new RevenuDTO();
        r.setTitre(titre);
        r.setDescription("Entrée d'argent");
        r.setMontant(montant);

        service.ajouterRevenu(r);
        System.out.println("✅ Revenu enregistré : +" + montant + " DH");
    }

    private static void testHistoriqueCabinet() {
        System.out.println("\n--- Historique ---");
        List<ChargeDTO> charges = service.getAllCharges();
        System.out.println("🔻 Charges (" + charges.size() + ") :");
        charges.forEach(c -> System.out.println("   - " + c.getTitre() + ": " + c.getMontant()));

        List<RevenuDTO> revenus = service.getAllRevenus();
        System.out.println("🔺 Revenus (" + revenus.size() + ") :");
        revenus.forEach(r -> System.out.println("   - " + r.getTitre() + ": " + r.getMontant()));
    }
}