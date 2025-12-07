package ma.dentaluxe.service.TestOthmane;

import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.enums.Sexe;
import ma.dentaluxe.entities.enums.Assurance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TestOrdonnancePrescription {

    // Stockage des données pour simuler la base
    private static List<Patient> patients = new ArrayList<>();
    private static List<Ordonnance> ordonnances = new ArrayList<>();
    private static List<Prescription> prescriptions = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================");
        System.out.println("  TEST DES CONTRAINTES DE DÉPENDANCE");
        System.out.println("==========================================");
        System.out.println("Règles à tester:");
        System.out.println("1. ❌ Pas de Prescription sans Ordonnance");
        System.out.println("2. ❌ Pas d'Ordonnance sans Patient/Dossier");
        System.out.println("3. ✅ Patient peut exister seul");
        System.out.println("==========================================\n");

        boolean continuer = true;

        while (continuer) {
            System.out.println("\n=== TEST DES CONTRAINTES ===");
            System.out.println("1. Tester création Patient seul (devrait réussir)");
            System.out.println("2. Tester création Ordonnance SANS Patient (devrait échouer)");
            System.out.println("3. Tester création Ordonnance AVEC Patient (devrait réussir)");
            System.out.println("4. Tester création Prescription SANS Ordonnance (devrait échouer)");
            System.out.println("5. Tester création Prescription AVEC Ordonnance (devrait réussir)");
            System.out.println("6. Tester chaîne complète Patient→Ordonnance→Prescription");
            System.out.println("7. Afficher l'état actuel des données");
            System.out.println("8. Tester suppression avec dépendances");
            System.out.println("9. Quitter");
            System.out.print("Choix: ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    testerPatientSeul(scanner);
                    break;
                case 2:
                    testerOrdonnanceSansPatient(scanner);
                    break;
                case 3:
                    testerOrdonnanceAvecPatient(scanner);
                    break;
                case 4:
                    testerPrescriptionSansOrdonnance(scanner);
                    break;
                case 5:
                    testerPrescriptionAvecOrdonnance(scanner);
                    break;
                case 6:
                    testerChaineComplete(scanner);
                    break;
                case 7:
                    afficherEtatDonnees();
                    break;
                case 8:
                    testerSuppressionAvecDependances(scanner);
                    break;
                case 9:
                    continuer = false;
                    System.out.println("Fin du test des contraintes.");
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
        scanner.close();
    }

    // Test 1: Patient seul (devrait toujours réussir)
    private static void testerPatientSeul(Scanner scanner) {
        System.out.println("\n=== TEST 1: Création Patient seul ===");
        System.out.println("Attendu: ✅ SUCCÈS (Patient indépendant)");

        Patient patient = Patient.builder()
                .nom("Test")
                .prenom("Patient")
                .telephone("0600000000")
                .email("test@test.com")
                .dateNaissance(LocalDate.of(1990, 1, 1))
                .dateCreation(LocalDateTime.now())
                .sexe(Sexe.HOMME)
                .assurance(Assurance.CNSS)
                .build();

        patients.add(patient);
        System.out.println("✅ Patient créé avec succès !");
        System.out.println("   Nom: " + patient.getNom() + " " + patient.getPrenom());
        System.out.println("   ID simulé: Patient#" + (patients.size() - 1));
    }

    // Test 2: Ordonnance sans Patient (devrait échouer)
    private static void testerOrdonnanceSansPatient(Scanner scanner) {
        System.out.println("\n=== TEST 2: Création Ordonnance SANS Patient ===");
        System.out.println("Attendu: ❌ ÉCHEC (besoin d'un idDM qui vient d'un Patient)");

        if (patients.isEmpty()) {
            System.out.println("⚠️  Aucun patient créé. Tentative avec idDM=null...");

            try {
                Ordonnance ordonnance = Ordonnance.builder()
                        .idOrdo(1L)
                        .idDM(null)  // Problème ici !
                        .idMedecin(1L)
                        .dateOrdonnance(LocalDate.now())
                        .build();

                if (ordonnance.getIdDM() == null) {
                    System.out.println("❌ ÉCHEC: Ordonnance créée mais avec idDM=null");
                    System.out.println("   → Dans la réalité, ça causerait une erreur en base");
                    System.out.println("   → Contrainte FOREIGN KEY non respectée");
                } else {
                    ordonnances.add(ordonnance);
                    System.out.println("✅ Ordonnance créée (mais c'est étrange sans patient)");
                }
            } catch (Exception e) {
                System.out.println("❌ Exception: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️  Des patients existent, test non applicable");
        }
    }

    // Test 3: Ordonnance avec Patient (devrait réussir)
    private static void testerOrdonnanceAvecPatient(Scanner scanner) {
        System.out.println("\n=== TEST 3: Création Ordonnance AVEC Patient ===");
        System.out.println("Attendu: ✅ SUCCÈS (patient existe)");

        if (patients.isEmpty()) {
            System.out.println("⚠️  Création d'un patient d'abord...");
            testerPatientSeul(scanner);
        }

        // Simuler que le patient a un dossier médical (idDM)
        Long idDM = (long) (patients.size() * 1000); // ID fictif du dossier

        Ordonnance ordonnance = Ordonnance.builder()
                .idOrdo((long) (ordonnances.size() + 1))
                .idDM(idDM)  // Référence au dossier médical du patient
                .idMedecin(50L)
                .dateOrdonnance(LocalDate.now())
                .build();

        ordonnances.add(ordonnance);
        System.out.println("✅ Ordonnance créée avec succès !");
        System.out.println("   ID Ordonnance: " + ordonnance.getIdOrdo());
        System.out.println("   ID Dossier Médical (lié à patient): " + ordonnance.getIdDM());
        System.out.println("   Patient associé: " + patients.get(patients.size() - 1).getNom());
    }

    // Test 4: Prescription sans Ordonnance (devrait échouer)
    private static void testerPrescriptionSansOrdonnance(Scanner scanner) {
        System.out.println("\n=== TEST 4: Création Prescription SANS Ordonnance ===");
        System.out.println("Attendu: ❌ ÉCHEC (idOrdo est requis)");

        System.out.println("Tentative 1: Prescription avec idOrdo=null");
        try {
            Prescription prescription = Prescription.builder()
                    .idPrescription(1L)
                    .idOrdo(null)  // PROBLEME ICI !
                    .idMedicament(100L)
                    .quantite(2)
                    .frequence("3x/jour")
                    .dureeEnJours(7)
                    .build();

            if (prescription.getIdOrdo() == null) {
                System.out.println("❌ Problème: Prescription créée avec idOrdo=null");
                System.out.println("   → En base de données: ERREUR de contrainte FOREIGN KEY");
                System.out.println("   → En Java: Possible, mais illogique métier");
            } else {
                prescriptions.add(prescription);
                System.out.println("✅ Prescription créée (mais devrait échouer)");
            }
        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
        }

        System.out.println("\nTentative 2: Prescription avec idOrdo invalide");
        Long idOrdoInexistant = 9999L;
        Prescription prescription2 = Prescription.builder()
                .idPrescription(2L)
                .idOrdo(idOrdoInexistant)  // ID qui n'existe pas !
                .idMedicament(100L)
                .quantite(2)
                .frequence("3x/jour")
                .dureeEnJours(7)
                .build();

        // Vérifier si cet idOrdo existe dans les ordonnances
        boolean ordonnanceExiste = ordonnances.stream()
                .anyMatch(o -> o.getIdOrdo().equals(idOrdoInexistant));

        if (!ordonnanceExiste) {
            System.out.println("❌ ERREUR: idOrdo=" + idOrdoInexistant + " n'existe pas !");
            System.out.println("   → Violation d'intégrité référentielle");
            System.out.println("   → En SQL: FOREIGN KEY constraint fails");
        } else {
            prescriptions.add(prescription2);
            System.out.println("✅ Prescription créée");
        }
    }

    // Test 5: Prescription avec Ordonnance (devrait réussir)
    private static void testerPrescriptionAvecOrdonnance(Scanner scanner) {
        System.out.println("\n=== TEST 5: Création Prescription AVEC Ordonnance ===");
        System.out.println("Attendu: ✅ SUCCÈS (ordonnance existe)");

        if (ordonnances.isEmpty()) {
            System.out.println("⚠️  Création d'une ordonnance d'abord...");
            testerOrdonnanceAvecPatient(scanner);
        }

        // Prendre la dernière ordonnance créée
        Ordonnance derniereOrdonnance = ordonnances.get(ordonnances.size() - 1);

        Prescription prescription = Prescription.builder()
                .idPrescription((long) (prescriptions.size() + 1))
                .idOrdo(derniereOrdonnance.getIdOrdo())  // CORRECT: idOrdo valide
                .idMedicament(200L)
                .quantite(1)
                .frequence("Matin et soir")
                .dureeEnJours(10)
                .build();

        prescriptions.add(prescription);
        System.out.println("✅ Prescription créée avec succès !");
        System.out.println("   ID Prescription: " + prescription.getIdPrescription());
        System.out.println("   ID Ordonnance parente: " + prescription.getIdOrdo());
        System.out.println("   Vérification: idOrdo existe ? " +
                ordonnances.stream().anyMatch(o -> o.getIdOrdo().equals(prescription.getIdOrdo())));
    }

    // Test 6: Chaîne complète Patient → Ordonnance → Prescription
    private static void testerChaineComplete(Scanner scanner) {
        System.out.println("\n=== TEST 6: Chaîne complète Patient→Ordonnance→Prescription ===");
        System.out.println("Attendu: ✅ SUCCÈS si on respecte l'ordre");

        System.out.println("\nÉtape 1: Création du Patient");
        Patient patient = Patient.builder()
                .nom("Chaine")
                .prenom("Complete")
                .telephone("0611111111")
                .email("chaine@test.com")
                .dateNaissance(LocalDate.of(1985, 5, 15))
                .dateCreation(LocalDateTime.now())
                .sexe(Sexe.FEMME)
                .assurance(Assurance.CNOPS)
                .build();
        patients.add(patient);
        System.out.println("✅ Patient créé: " + patient.getNom());

        System.out.println("\nÉtape 2: Création Ordonnance pour ce patient");
        Long idDMPatient = 5001L; // Simuler ID dossier médical
        Ordonnance ordonnance = Ordonnance.builder()
                .idOrdo(1001L)
                .idDM(idDMPatient)  // Lié au patient
                .idMedecin(75L)
                .dateOrdonnance(LocalDate.now())
                .build();
        ordonnances.add(ordonnance);
        System.out.println("✅ Ordonnance créée: #" + ordonnance.getIdOrdo());
        System.out.println("   Liée au dossier: " + idDMPatient);

        System.out.println("\nÉtape 3: Création Prescriptions pour cette ordonnance");

        // Prescription 1
        Prescription pres1 = Prescription.builder()
                .idPrescription(2001L)
                .idOrdo(ordonnance.getIdOrdo())  // Référence l'ordonnance
                .idMedicament(301L)
                .quantite(2)
                .frequence("3x/jour")
                .dureeEnJours(7)
                .build();
        prescriptions.add(pres1);

        // Prescription 2
        Prescription pres2 = Prescription.builder()
                .idPrescription(2002L)
                .idOrdo(ordonnance.getIdOrdo())  // Même ordonnance
                .idMedicament(302L)
                .quantite(1)
                .frequence("Matin")
                .dureeEnJours(14)
                .build();
        prescriptions.add(pres2);

        System.out.println("✅ 2 Prescriptions créées pour l'ordonnance #" + ordonnance.getIdOrdo());

        System.out.println("\n📊 VÉRIFICATION DE LA CHAÎNE:");
        System.out.println("Patient: " + patient.getNom() + " " + patient.getPrenom());
        System.out.println("  ↓ a un dossier médical ID: " + idDMPatient);
        System.out.println("Ordonnance: #" + ordonnance.getIdOrdo() + " (liée à dossier " + ordonnance.getIdDM() + ")");
        System.out.println("  ↓ a " + prescriptions.stream()
                .filter(p -> p.getIdOrdo().equals(ordonnance.getIdOrdo()))
                .count() + " prescriptions");

        System.out.println("\n🎯 RÉSULTAT: Chaîne logique respectée !");
        System.out.println("   Patient → Dossier Médical → Ordonnance → Prescriptions");
    }

    // Test 7: Afficher l'état
    private static void afficherEtatDonnees() {
        System.out.println("\n=== ÉTAT ACTUEL DES DONNÉES ===");

        System.out.println("\n👥 PATIENTS (" + patients.size() + "):");
        if (patients.isEmpty()) {
            System.out.println("   Aucun patient");
        } else {
            for (int i = 0; i < patients.size(); i++) {
                Patient p = patients.get(i);
                System.out.println("   " + i + ". " + p.getNom() + " " + p.getPrenom() +
                        " (Dossier simulé: " + (i * 1000) + ")");
            }
        }

        System.out.println("\n📄 ORDONNANCES (" + ordonnances.size() + "):");
        if (ordonnances.isEmpty()) {
            System.out.println("   Aucune ordonnance");
        } else {
            for (Ordonnance o : ordonnances) {
                System.out.println("   ID: " + o.getIdOrdo() +
                        " | Dossier: " + o.getIdDM() +
                        " | Date: " + o.getDateOrdonnance());
            }
        }

        System.out.println("\n💊 PRESCRIPTIONS (" + prescriptions.size() + "):");
        if (prescriptions.isEmpty()) {
            System.out.println("   Aucune prescription");
        } else {
            for (Prescription p : prescriptions) {
                String etat = "   ID: " + p.getIdPrescription() +
                        " | Ordonnance: " + p.getIdOrdo() +
                        " | Médicament: " + p.getIdMedicament();

                // Vérifier si l'ordonnance référencée existe
                boolean ordonnanceExiste = ordonnances.stream()
                        .anyMatch(o -> o.getIdOrdo().equals(p.getIdOrdo()));

                if (!ordonnanceExiste && p.getIdOrdo() != null) {
                    etat += " ❌ (Ordonnance #" + p.getIdOrdo() + " n'existe pas !)";
                } else if (p.getIdOrdo() == null) {
                    etat += " ⚠️  (idOrdo est null !)";
                } else {
                    etat += " ✅";
                }
                System.out.println(etat);
            }
        }
    }

    // Test 8: Suppression avec dépendances
    private static void testerSuppressionAvecDependances(Scanner scanner) {
        System.out.println("\n=== TEST 8: Suppression avec dépendances ===");

        if (ordonnances.isEmpty() || prescriptions.isEmpty()) {
            System.out.println("⚠️  Créez d'abord des données avec le test 6");
            return;
        }

        System.out.println("\nScénario: Supprimer une ordonnance qui a des prescriptions");

        // Trouver une ordonnance avec prescriptions
        Ordonnance ordonnanceAvecPrescriptions = null;
        for (Ordonnance o : ordonnances) {
            long nbPrescriptions = prescriptions.stream()
                    .filter(p -> p.getIdOrdo() != null && p.getIdOrdo().equals(o.getIdOrdo()))
                    .count();

            if (nbPrescriptions > 0) {
                ordonnanceAvecPrescriptions = o;
                break;
            }
        }

        if (ordonnanceAvecPrescriptions == null) {
            System.out.println("⚠️  Aucune ordonnance avec prescriptions trouvée");
            return;
        }

        System.out.println("\nOrdonnance à supprimer: #" + ordonnanceAvecPrescriptions.getIdOrdo());

        // Compter les prescriptions liées
        Ordonnance finalOrdonnanceAvecPrescriptions = ordonnanceAvecPrescriptions;
        List<Prescription> prescriptionsLiees = prescriptions.stream()
                .filter(p -> Objects.equals(p.getIdOrdo(), finalOrdonnanceAvecPrescriptions.getIdOrdo()))
                .toList();

        System.out.println("Cette ordonnance a " + prescriptionsLiees.size() + " prescriptions liées");

        System.out.println("\nQue faire des prescriptions ?");
        System.out.println("1. Supprimer l'ordonnance ET ses prescriptions (CASCADE)");
        System.out.println("2. Supprimer seulement l'ordonnance (laisser prescriptions orphelines)");
        System.out.println("3. Annuler");
        System.out.print("Choix: ");

        int choixSuppression = scanner.nextInt();
        scanner.nextLine();

        switch (choixSuppression) {
            case 1:
                // Suppression en cascade
                Ordonnance finalOrdonnanceAvecPrescriptions1 = ordonnanceAvecPrescriptions;
                prescriptions.removeIf(p -> p.getIdOrdo() != null &&
                        p.getIdOrdo().equals(finalOrdonnanceAvecPrescriptions1.getIdOrdo()));
                ordonnances.remove(ordonnanceAvecPrescriptions);
                System.out.println("✅ Suppression CASCADE: Ordonnance et " +
                        prescriptionsLiees.size() + " prescriptions supprimées");
                break;

            case 2:
                // Supprimer seulement l'ordonnance (problème d'intégrité)
                ordonnances.remove(ordonnanceAvecPrescriptions);
                System.out.println("⚠️  Ordonnance supprimée, mais " +
                        prescriptionsLiees.size() + " prescriptions sont maintenant orphelines");
                System.out.println("   → En base: ERREUR si FOREIGN KEY avec RESTRICT");
                System.out.println("   → En base: SUCCÈS si FOREIGN KEY avec SET NULL");
                break;

            case 3:
                System.out.println("Suppression annulée");
                break;
        }
    }
}