package ma.dentaluxe.service.testaya;

import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.entities.enums.StatutConsultation;
import ma.dentaluxe.mvc.dto.dossier.AntecedentDTO;
import ma.dentaluxe.mvc.dto.dossier.ConsultationDTO;
import ma.dentaluxe.mvc.dto.dossier.DossierMedicalDTO;
import ma.dentaluxe.service.dossierMedical.baseImplementation.DossierMedicalServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TestDossierMedical {

    private static final DossierMedicalServiceImpl service = new DossierMedicalServiceImpl();
    private static Long currentDossierId = null;

    public static void menu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║    🩺 TEST MODULE DOSSIER MEDICAL      ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Créer un Dossier pour un Patient");
            System.out.println("2. Ajouter un Antécédent");
            System.out.println("3. Planifier une Consultation");
            System.out.println("4. Terminer une Consultation");
            System.out.println("5. Afficher les Consultations du Dossier");
            System.out.println("0. Retour au Menu Principal");
            System.out.print("👉 Choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le saut de ligne

            switch (choix) {
                case 1: testCreerDossier(scanner); break;
                case 2: testAjouterAntecedent(scanner); break;
                case 3: testPlanifierConsultation(scanner); break;
                case 4: testTerminerConsultation(scanner); break;
                case 5: testListerConsultations(); break;
                case 0: back = true; break;
                default: System.out.println("❌ Choix invalide !");
            }
        }
    }

    private static void testCreerDossier(Scanner scanner) {
        System.out.println("\n--- 1. Création Dossier ---");
        System.out.print("Entrez l'ID du Patient (ex: 1) : ");
        Long idPatient = scanner.nextLong();

        if (service.hasDossier(idPatient)) {
            System.out.println("⚠️ Ce patient a déjà un dossier !");
            currentDossierId = service.getDossierByPatientId(idPatient).getIdDM();
        } else {
            DossierMedicalDTO dm = service.createDossier(idPatient);
            currentDossierId = dm.getIdDM();
            System.out.println("✅ Dossier créé avec succès ! (ID: " + currentDossierId + ")");
        }
    }

    private static void testAjouterAntecedent(Scanner scanner) {
        if (currentDossierId == null) {
            System.out.println("❌ Veuillez d'abord créer/sélectionner un dossier (Option 1).");
            return;
        }
        System.out.println("\n--- 2. Ajout Antécédent ---");
        System.out.print("Nom (ex: Diabète) : ");
        String nom = scanner.nextLine();

        AntecedentDTO ant = new AntecedentDTO();
        ant.setIdDM(currentDossierId);
        ant.setNom(nom);
        ant.setCategorie(CategorieAntecedent.ALLERGIE); // Valeur test
        ant.setNiveauDeRisque(NiveauRisque.FAIBLE);       // Valeur test

        service.addAntecedent(ant);
        System.out.println("✅ Antécédent ajouté à la base.");
    }

    private static void testPlanifierConsultation(Scanner scanner) {
        if (currentDossierId == null) {
            System.out.println("❌ Aucun dossier actif.");
            return;
        }
        System.out.println("\n--- 3. Planifier Consultation ---");
        System.out.print("Observation initiale : ");
        String obs = scanner.nextLine();

        ConsultationDTO cons = new ConsultationDTO();
        cons.setIdDM(currentDossierId);
        cons.setIdMedecin(1L); // Médecin par défaut
        cons.setDateConsultation(LocalDate.now());
        cons.setObservation(obs);
        cons.setStatut(StatutConsultation.PLANIFIEE);

        ConsultationDTO created = service.planifierConsultation(cons);
        System.out.println("✅ Consultation planifiée (ID: " + created.getIdConsultation() + ")");
    }

    private static void testTerminerConsultation(Scanner scanner) {
        System.out.println("\n--- 4. Terminer Consultation ---");
        System.out.print("ID Consultation à terminer : ");
        Long idCons = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Rapport final : ");
        String rapport = scanner.nextLine();

        service.terminerConsultation(idCons, rapport);
        System.out.println("✅ Consultation marquée comme TERMINEE.");
    }

    private static void testListerConsultations() {
        if (currentDossierId == null) return;
        System.out.println("\n--- 5. Historique Consultations ---");
        List<ConsultationDTO> liste = service.getConsultationsByDossier(currentDossierId);
        for (ConsultationDTO c : liste) {
            System.out.println("📄 ID: " + c.getIdConsultation() +
                    " | Date: " + c.getDateConsultation() +
                    " | Statut: " + c.getStatut());
        }
    }
}