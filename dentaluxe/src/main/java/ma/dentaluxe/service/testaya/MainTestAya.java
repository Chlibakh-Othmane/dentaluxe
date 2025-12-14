package ma.dentaluxe.service.testaya;

import java.util.Scanner;

public class MainTestAya {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================");
        System.out.println("   APPLICATION DENTALSOFT - TESTS AYA");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. 🩺 Module Dossier Médical");
            System.out.println("2. 💰 Module Caisse & Finance");
            System.out.println("3. 📊 Module Statistiques");
            System.out.println("0. 🚪 Quitter");
            System.out.print("👉 Votre choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Nettoyage buffer

            switch (choix) {
                case 1:
                    TestDossierMedical.menu(scanner);
                    break;
                case 2:
                    TestCaisse.menu(scanner);
                    break;
                case 3:
                    TestStatistiques.menu(scanner);
                    break;
                case 0:
                    running = false;
                    System.out.println("👋 Fin des tests. Au revoir !");
                    break;
                default:
                    System.out.println("❌ Choix invalide.");
            }
        }
        scanner.close();
    }
}