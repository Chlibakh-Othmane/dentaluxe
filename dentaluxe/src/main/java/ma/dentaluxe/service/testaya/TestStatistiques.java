package ma.dentaluxe.service.testaya;

import ma.dentaluxe.mvc.dto.statistiques.BilanFinancierDTO;
import ma.dentaluxe.service.statistiques.baseImplementation.StatistiquesServiceImpl;

import java.time.LocalDate;
import java.util.Scanner;

public class TestStatistiques {

    private static final StatistiquesServiceImpl service = new StatistiquesServiceImpl();

    public static void menu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║      📊 TEST MODULE STATISTIQUES       ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Bilan Aujourd'hui (Journalier)");
            System.out.println("2. Bilan du Mois en cours");
            System.out.println("3. Bilan de l'Année");
            System.out.println("0. Retour au Menu Principal");
            System.out.print("👉 Choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1: afficherBilan(service.getBilanJournalier(LocalDate.now())); break;
                case 2: afficherBilan(service.getBilanMensuel(LocalDate.now().getMonthValue(), LocalDate.now().getYear())); break;
                case 3: afficherBilan(service.getBilanAnnuel(LocalDate.now().getYear())); break;
                case 0: back = true; break;
                default: System.out.println("❌ Choix invalide !");
            }
        }
    }

    private static void afficherBilan(BilanFinancierDTO b) {
        System.out.println("\n📝 --- BILAN : " + b.getPeriode() + " ---");
        System.out.println("💰 Recettes Patients : " + b.getTotalRecettesPatients() + " DH");
        System.out.println("💵 Autres Revenus    : " + b.getTotalAutresRevenus() + " DH");
        System.out.println("💸 Charges (Dépenses): " + b.getTotalCharges() + " DH");
        System.out.println("-----------------------------------");
        System.out.println("📈 RÉSULTAT NET      : " + b.getResultatNet() + " DH");
        System.out.println("-----------------------------------");
        System.out.println("📅 Nombre de RDV     : " + b.getNombreRDV());
        System.out.println("🆕 Nouveaux Patients : " + b.getNouveauxPatients());
    }
}