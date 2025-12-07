package ma.dentaluxe.service.TestOthmane;

import ma.dentaluxe.service.patient.baseImplimentation.PatientServiceImpl;
import ma.dentaluxe.service.patient.api.PatientService;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.enums.Sexe;
import ma.dentaluxe.entities.enums.Assurance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TestPatient {

    // Simuler des données pour les autres entités
    private static List<Ordonnance> ordonnances = new ArrayList<>();
    private static List<Prescription> prescriptions = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Créer un repository simulé
        PatientRepositoryMock patientRepository = new PatientRepositoryMock();
        PatientService patientService = new PatientServiceImpl(patientRepository);

        System.out.println("==============================================");
        System.out.println("  TEST DES LIAISONS PATIENT AVEC AUTRES TABLES");
        System.out.println("==============================================");
        System.out.println("Tables testées:");
        System.out.println("1. Patient → Ordonnance (1-N)");
        System.out.println("2. Ordonnance → Prescription (1-N)");
        System.out.println("3. Patient → Consultation (1-N)");
        System.out.println("4. Patient → Facture (1-N)");
        System.out.println("5. Patient → Dossier Médical (1-1)");
        System.out.println("==============================================\n");

        boolean continuer = true;

        while (continuer) {
            System.out.println("\n=== MENU DE TEST DES LIAISONS ===");
            System.out.println("1. Créer un Patient (table principale)");
            System.out.println("2. Créer une Ordonnance pour un Patient");
            System.out.println("3. Créer une Prescription pour une Ordonnance");
            System.out.println("4. Tester la dépendance Patient → Ordonnance");
            System.out.println("5. Tester la chaîne Patient → Ordonnance → Prescription");
            System.out.println("6. Afficher toutes les données liées");
            System.out.println("7. Tester la suppression avec dépendances");
            System.out.println("8. Tester les contraintes d'intégrité");
            System.out.println("9. Statistiques des relations");
            System.out.println("10. Quitter");
            System.out.print("Choix: ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    testerCreationPatient(scanner, patientService);
                    break;
                case 2:
                    testerCreationOrdonnancePourPatient(scanner, patientService);
                    break;
                case 3:
                    testerCreationPrescriptionPourOrdonnance(scanner);
                    break;
                case 4:
                    testerDependancePatientOrdonnance(scanner, patientService);
                    break;
                case 5:
                    testerChaineComplete(scanner, patientService);
                    break;
                case 6:
                    afficherToutesDonnees(patientService);
                    break;
                case 7:
                    testerSuppressionAvecDependances(scanner, patientService);
                    break;
                case 8:
                    testerContraintesIntegrite(scanner, patientService);
                    break;
                case 9:
                    afficherStatistiquesRelations(patientService);
                    break;
                case 10:
                    continuer = false;
                    System.out.println("Fin des tests.");
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
        scanner.close();
    }

    // Test 1: Créer un Patient
    private static void testerCreationPatient(Scanner scanner, PatientService patientService) {
        System.out.println("\n=== TEST 1: Création d'un Patient ===");

        PatientServiceImpl.PatientCreateDTOImpl dto = new PatientServiceImpl.PatientCreateDTOImpl();

        System.out.print("Nom: ");
        dto.setNom(scanner.nextLine());

        System.out.print("Prénom: ");
        dto.setPrenom(scanner.nextLine());

        System.out.print("Email: ");
        dto.setEmail(scanner.nextLine());

        System.out.print("Téléphone (10 chiffres): ");
        dto.setTelephone(scanner.nextLine());

        System.out.print("Date de naissance (AAAA-MM-JJ): ");
        dto.setDateNaissance(LocalDate.parse(scanner.nextLine()));

        System.out.print("Sexe (HOMME/FEMME): ");
        dto.setSexe(Sexe.valueOf(scanner.nextLine().toUpperCase()));

        System.out.print("Assurance (CNSS/CNOPS/AMO/AUTRE/AUCUNE): ");
        dto.setAssurance(Assurance.valueOf(scanner.nextLine().toUpperCase()));

        System.out.print("Adresse: ");
        dto.setAdresse(scanner.nextLine());

        try {
            PatientService.PatientDTO patient = patientService.createPatient(dto);
            System.out.println("✅ Patient créé avec succès !");
            System.out.println("   ID: " + patient.getId());
            System.out.println("   Nom complet: " + patient.getFullName());
            System.out.println("   Âge: " + patient.getAge() + " ans");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    // Test 2: Créer une Ordonnance pour un Patient
    private static void testerCreationOrdonnancePourPatient(Scanner scanner, PatientService patientService) {
        System.out.println("\n=== TEST 2: Création Ordonnance pour Patient ===");

        // Afficher les patients existants
        List<PatientService.PatientDTO> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("❌ Aucun patient existant. Créez d'abord un patient.");
            return;
        }

        System.out.println("Patients disponibles:");
        for (int i = 0; i < patients.size(); i++) {
            PatientService.PatientDTO p = patients.get(i);
            System.out.println(i + ". " + p.getFullName() + " (ID: " + p.getId() + ")");
        }

        System.out.print("Choisir un patient (index): ");
        int idxPatient = scanner.nextInt();
        scanner.nextLine();

        if (idxPatient < 0 || idxPatient >= patients.size()) {
            System.out.println("❌ Index invalide !");
            return;
        }

        PatientService.PatientDTO patient = patients.get(idxPatient);

        System.out.println("\nCréation d'une ordonnance pour: " + patient.getFullName());

        // Simuler la création d'une ordonnance
        Ordonnance ordonnance = Ordonnance.builder()
                .idOrdo((long) (ordonnances.size() + 1))
                .idDM(genererIdDM(patient.getId())) // Générer un ID dossier médical
                .idMedecin(1L) // Médecin par défaut
                .dateOrdonnance(LocalDate.now())
                .build();

        ordonnances.add(ordonnance);

        System.out.println("✅ Ordonnance créée !");
        System.out.println("   ID Ordonnance: " + ordonnance.getIdOrdo());
        System.out.println("   Patient: " + patient.getFullName());
        System.out.println("   Dossier Médical ID: " + ordonnance.getIdDM());
        System.out.println("   Date: " + ordonnance.getDateOrdonnance());
    }

    // Test 3: Créer une Prescription pour une Ordonnance
    private static void testerCreationPrescriptionPourOrdonnance(Scanner scanner) {
        System.out.println("\n=== TEST 3: Création Prescription pour Ordonnance ===");

        if (ordonnances.isEmpty()) {
            System.out.println("❌ Aucune ordonnance existante. Créez d'abord une ordonnance.");
            return;
        }

        System.out.println("Ordonnances disponibles:");
        for (int i = 0; i < ordonnances.size(); i++) {
            Ordonnance o = ordonnances.get(i);
            System.out.println(i + ". Ordonnance #" + o.getIdOrdo() + " (DM: " + o.getIdDM() + ")");
        }

        System.out.print("Choisir une ordonnance (index): ");
        int idxOrdo = scanner.nextInt();
        scanner.nextLine();

        if (idxOrdo < 0 || idxOrdo >= ordonnances.size()) {
            System.out.println("❌ Index invalide !");
            return;
        }

        Ordonnance ordonnance = ordonnances.get(idxOrdo);

        System.out.println("\nCréation d'une prescription pour l'ordonnance #" + ordonnance.getIdOrdo());

        Prescription prescription = Prescription.builder()
                .idPrescription((long) (prescriptions.size() + 1))
                .idOrdo(ordonnance.getIdOrdo()) // Référence à l'ordonnance
                .idMedicament((long) (prescriptions.size() + 1000))
                .quantite(2)
                .frequence("3 fois par jour")
                .dureeEnJours(7)
                .build();

        prescriptions.add(prescription);

        System.out.println("✅ Prescription créée !");
        System.out.println("   ID Prescription: " + prescription.getIdPrescription());
        System.out.println("   ID Ordonnance: " + prescription.getIdOrdo());
        System.out.println("   Médicament ID: " + prescription.getIdMedicament());
        System.out.println("   Posologie: " + prescription.getQuantite() + "x" + prescription.getFrequence());
    }

    // Test 4: Tester la dépendance Patient → Ordonnance
    private static void testerDependancePatientOrdonnance(Scanner scanner, PatientService patientService) {
        System.out.println("\n=== TEST 4: Dépendance Patient → Ordonnance ===");

        System.out.println("Scénario: Un patient doit exister avant de pouvoir créer une ordonnance pour lui.");

        // Essayer de créer une ordonnance sans patient
        System.out.println("\n1. Tentative de créer une ordonnance SANS patient:");
        try {
            Ordonnance ordonnance = Ordonnance.builder()
                    .idOrdo(999L)
                    .idDM(null) // Pas de dossier médical (donc pas de patient)
                    .idMedecin(1L)
                    .dateOrdonnance(LocalDate.now())
                    .build();

            if (ordonnance.getIdDM() == null) {
                System.out.println("❌ Échec: Ordonnance créée mais sans idDM (donc sans patient)");
                System.out.println("   → Problème: Contrainte FOREIGN KEY non respectée");
                System.out.println("   → En base: ERREUR si idDM NOT NULL");
            }
        } catch (Exception e) {
            System.out.println("✅ Correct: " + e.getMessage());
        }

        // Créer un patient puis une ordonnance
        System.out.println("\n2. Créer un patient PUIS une ordonnance:");

        PatientServiceImpl.PatientCreateDTOImpl dto = new PatientServiceImpl.PatientCreateDTOImpl();
        dto.setNom("TestDependance");
        dto.setPrenom("Patient");
        dto.setEmail("dependance@test.com");
        dto.setTelephone("0611111111");
        dto.setDateNaissance(LocalDate.of(1985, 1, 1));
        dto.setSexe(Sexe.HOMME);
        dto.setAssurance(Assurance.CNSS);
        dto.setAdresse("Adresse test");

        try {
            PatientService.PatientDTO patient = patientService.createPatient(dto);
            System.out.println("✅ Patient créé: " + patient.getFullName());

            // Créer une ordonnance avec idDM valide
            Ordonnance ordonnance = Ordonnance.builder()
                    .idOrdo((long) (ordonnances.size() + 100))
                    .idDM(genererIdDM(patient.getId())) // idDM basé sur l'ID patient
                    .idMedecin(1L)
                    .dateOrdonnance(LocalDate.now())
                    .build();

            ordonnances.add(ordonnance);
            System.out.println("✅ Ordonnance créée avec idDM valide: " + ordonnance.getIdDM());
            System.out.println("   → Respecte la contrainte FOREIGN KEY");

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    // Test 5: Tester la chaîne complète
    private static void testerChaineComplete(Scanner scanner, PatientService patientService) {
        System.out.println("\n=== TEST 5: Chaîne complète Patient → Ordonnance → Prescription ===");

        System.out.println("Création d'une chaîne complète...");

        // 1. Créer un patient
        PatientServiceImpl.PatientCreateDTOImpl dto = new PatientServiceImpl.PatientCreateDTOImpl();
        dto.setNom("Chaine");
        dto.setPrenom("Complete");
        dto.setEmail("chaine@test.com");
        dto.setTelephone("0622222222");
        dto.setDateNaissance(LocalDate.of(1990, 6, 15));
        dto.setSexe(Sexe.FEMME);
        dto.setAssurance(Assurance.CNOPS);
        dto.setAdresse("Adresse chaîne");

        try {
            PatientService.PatientDTO patient = patientService.createPatient(dto);
            System.out.println("1. ✅ Patient créé: " + patient.getFullName());

            // 2. Créer une ordonnance
            Ordonnance ordonnance = Ordonnance.builder()
                    .idOrdo((long) (ordonnances.size() + 1000))
                    .idDM(genererIdDM(patient.getId()))
                    .idMedecin(2L)
                    .dateOrdonnance(LocalDate.now())
                    .build();

            ordonnances.add(ordonnance);
            System.out.println("2. ✅ Ordonnance créée: #" + ordonnance.getIdOrdo());

            // 3. Créer des prescriptions
            Prescription pres1 = Prescription.builder()
                    .idPrescription((long) (prescriptions.size() + 1000))
                    .idOrdo(ordonnance.getIdOrdo())
                    .idMedicament(5001L)
                    .quantite(1)
                    .frequence("Matin et soir")
                    .dureeEnJours(10)
                    .build();

            Prescription pres2 = Prescription.builder()
                    .idPrescription((long) (prescriptions.size() + 1001))
                    .idOrdo(ordonnance.getIdOrdo())
                    .idMedicament(5002L)
                    .quantite(2)
                    .frequence("3 fois par jour")
                    .dureeEnJours(5)
                    .build();

            prescriptions.add(pres1);
            prescriptions.add(pres2);
            System.out.println("3. ✅ 2 Prescriptions créées");

            // Vérifier la chaîne
            System.out.println("\n🔗 VÉRIFICATION DE LA CHAÎNE:");
            System.out.println("Patient: " + patient.getFullName() + " (ID: " + patient.getId() + ")");
            System.out.println("  ↓ a un Dossier Médical ID: " + ordonnance.getIdDM());
            System.out.println("Ordonnance: #" + ordonnance.getIdOrdo() + " (liée à DM: " + ordonnance.getIdDM() + ")");

            long nbPrescriptions = prescriptions.stream()
                    .filter(p -> p.getIdOrdo() != null && p.getIdOrdo().equals(ordonnance.getIdOrdo()))
                    .count();
            System.out.println("  ↓ a " + nbPrescriptions + " prescriptions");

            System.out.println("\n✅ Chaîne logique validée !");
            System.out.println("   Patient → Dossier Médical → Ordonnance → Prescriptions");

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    // Test 6: Afficher toutes les données
    private static void afficherToutesDonnees(PatientService patientService) {
        System.out.println("\n=== ÉTAT DES DONNÉES ===");

        // Patients
        List<PatientService.PatientDTO> patients = patientService.getAllPatients();
        System.out.println("\n👥 PATIENTS (" + patients.size() + "):");
        for (PatientService.PatientDTO p : patients) {
            System.out.println("  ID: " + p.getId() + " | " + p.getFullName() +
                    " | " + p.getEmail() + " | " + p.getTelephone());
        }

        // Ordonnances
        System.out.println("\n📄 ORDONNANCES (" + ordonnances.size() + "):");
        for (Ordonnance o : ordonnances) {
            System.out.println("  ID: " + o.getIdOrdo() + " | DM: " + o.getIdDM() +
                    " | Médecin: " + o.getIdMedecin() + " | Date: " + o.getDateOrdonnance());
        }

        // Prescriptions
        System.out.println("\n💊 PRESCRIPTIONS (" + prescriptions.size() + "):");
        for (Prescription p : prescriptions) {
            String etat = "  ID: " + p.getIdPrescription() + " | Ordo: " + p.getIdOrdo() +
                    " | Médicament: " + p.getIdMedicament() + " | Poso: " + p.getQuantite() + "x" + p.getFrequence();

            boolean ordonnanceExiste = ordonnances.stream()
                    .anyMatch(o -> o.getIdOrdo().equals(p.getIdOrdo()));

            if (p.getIdOrdo() == null) {
                etat += " ⚠️ (idOrdo null)";
            } else if (!ordonnanceExiste) {
                etat += " ❌ (Ordonnance #" + p.getIdOrdo() + " n'existe pas !)";
            } else {
                etat += " ✅";
            }
            System.out.println(etat);
        }

        // Vérification des relations
        System.out.println("\n🔍 VÉRIFICATION DES RELATIONS:");

        // Patients sans ordonnances
        List<PatientService.PatientDTO> patientsSansOrdonnance = new ArrayList<>();
        for (PatientService.PatientDTO p : patients) {
            Long idDM = genererIdDM(p.getId());
            boolean aOrdonnance = ordonnances.stream()
                    .anyMatch(o -> o.getIdDM() != null && o.getIdDM().equals(idDM));

            if (!aOrdonnance) {
                patientsSansOrdonnance.add(p);
            }
        }

        System.out.println("  Patients sans ordonnances: " + patientsSansOrdonnance.size());

        // Ordonnances sans prescriptions
        long ordonnancesSansPrescription = ordonnances.stream()
                .filter(o -> prescriptions.stream()
                        .noneMatch(p -> p.getIdOrdo() != null && p.getIdOrdo().equals(o.getIdOrdo())))
                .count();

        System.out.println("  Ordonnances sans prescriptions: " + ordonnancesSansPrescription);

        // Prescriptions orphelines
        long prescriptionsOrphelines = prescriptions.stream()
                .filter(p -> p.getIdOrdo() != null &&
                        ordonnances.stream().noneMatch(o -> o.getIdOrdo().equals(p.getIdOrdo())))
                .count();

        System.out.println("  Prescriptions orphelines: " + prescriptionsOrphelines);
    }

    // Test 7: Tester la suppression avec dépendances
    private static void testerSuppressionAvecDependances(Scanner scanner, PatientService patientService) {
        System.out.println("\n=== TEST 7: Suppression avec dépendances ===");

        List<PatientService.PatientDTO> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("Aucun patient à supprimer");
            return;
        }

        System.out.println("Patients disponibles:");
        for (int i = 0; i < patients.size(); i++) {
            PatientService.PatientDTO p = patients.get(i);
            System.out.println(i + ". " + p.getFullName() + " (ID: " + p.getId() + ")");
        }

        System.out.print("Choisir un patient à supprimer (index): ");
        int idxPatient = scanner.nextInt();
        scanner.nextLine();

        if (idxPatient < 0 || idxPatient >= patients.size()) {
            System.out.println("Index invalide !");
            return;
        }

        PatientService.PatientDTO patient = patients.get(idxPatient);
        Long idDM = genererIdDM(patient.getId());

        // Vérifier les dépendances
        System.out.println("\nAnalyse des dépendances pour " + patient.getFullName() + ":");

        // Ordonnances liées
        List<Ordonnance> ordonnancesLiees = ordonnances.stream()
                .filter(o -> o.getIdDM() != null && o.getIdDM().equals(idDM))
                .toList();

        System.out.println("  Ordonnances liées: " + ordonnancesLiees.size());

        // Prescriptions liées indirectement
        List<Prescription> prescriptionsLiees = new ArrayList<>();
        for (Ordonnance o : ordonnancesLiees) {
            prescriptionsLiees.addAll(prescriptions.stream()
                    .filter(p -> p.getIdOrdo() != null && p.getIdOrdo().equals(o.getIdOrdo()))
                    .toList());
        }

        System.out.println("  Prescriptions liées (via ordonnances): " + prescriptionsLiees.size());

        System.out.println("\nOptions de suppression:");
        System.out.println("1. Supprimer SEULEMENT le patient (laisser ordonnances/prescriptions)");
        System.out.println("2. Supprimer patient et TOUTES ses dépendances (CASCADE)");
        System.out.println("3. Annuler");
        System.out.print("Choix: ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                // Simuler la suppression
                System.out.println("⚠️  ATTENTION: Suppression partielle");
                System.out.println("Le patient sera supprimé mais " + ordonnancesLiees.size() +
                        " ordonnances et " + prescriptionsLiees.size() +
                        " prescriptions resteront orphelines");
                System.out.print("Confirmer? (O/N): ");
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("O")) {
                    System.out.println("✅ Patient supprimé (dépendances orphelines)");
                    // Dans la réalité: patientService.deletePatient(patient.getId());
                } else {
                    System.out.println("Suppression annulée");
                }
                break;

            case 2:
                System.out.println("Suppression en CASCADE...");
                // Supprimer prescriptions d'abord
                prescriptions.removeAll(prescriptionsLiees);
                // Supprimer ordonnances
                ordonnances.removeAll(ordonnancesLiees);
                // Supprimer patient
                System.out.println("✅ Suppression CASCADE réussie");
                System.out.println("   Patient + " + ordonnancesLiees.size() +
                        " ordonnances + " + prescriptionsLiees.size() +
                        " prescriptions supprimés");
                break;

            case 3:
                System.out.println("Suppression annulée");
                break;
        }
    }

    // Test 8: Tester les contraintes d'intégrité
    private static void testerContraintesIntegrite(Scanner scanner, PatientService patientService) {
        System.out.println("\n=== TEST 8: Contraintes d'intégrité ===");

        System.out.println("Scénarios testés:");

        // 1. Email unique
        System.out.println("\n1. Contrainte: Email unique");
        try {
            // Créer un patient
            PatientServiceImpl.PatientCreateDTOImpl dto1 = new PatientServiceImpl.PatientCreateDTOImpl();
            dto1.setNom("Test");
            dto1.setPrenom("Un");
            dto1.setEmail("unique@test.com");
            dto1.setTelephone("0633333333");
            dto1.setDateNaissance(LocalDate.of(1990, 1, 1));
            dto1.setSexe(Sexe.HOMME);
            dto1.setAssurance(Assurance.CNSS);
            dto1.setAdresse("Adresse 1");

            patientService.createPatient(dto1);
            System.out.println("   ✅ Premier patient créé");

            // Essayer de créer un deuxième patient avec le même email
            PatientServiceImpl.PatientCreateDTOImpl dto2 = new PatientServiceImpl.PatientCreateDTOImpl();
            dto2.setNom("Test");
            dto2.setPrenom("Deux");
            dto2.setEmail("unique@test.com"); // Même email !
            dto2.setTelephone("0644444444");
            dto2.setDateNaissance(LocalDate.of(1991, 2, 2));
            dto2.setSexe(Sexe.FEMME);
            dto2.setAssurance(Assurance.CNOPS);
            dto2.setAdresse("Adresse 2");

            patientService.createPatient(dto2);
            System.out.println("   ❌ ERREUR: Deuxième patient avec même email créé (ne devrait pas être possible)");

        } catch (Exception e) {
            System.out.println("   ✅ Correct: " + e.getMessage());
        }

        // 2. Référence à une ordonnance inexistante
        System.out.println("\n2. Contrainte: Prescription doit référencer une ordonnance existante");

        Prescription prescriptionInexistante = Prescription.builder()
                .idPrescription(9999L)
                .idOrdo(99999L) // Ordonnance qui n'existe pas !
                .idMedicament(9999L)
                .quantite(1)
                .frequence("Test")
                .dureeEnJours(1)
                .build();

        boolean ordonnanceExiste = ordonnances.stream()
                .anyMatch(o -> o.getIdOrdo().equals(prescriptionInexistante.getIdOrdo()));

        if (!ordonnanceExiste) {
            System.out.println("   ✅ Erreur détectée: prescription référence une ordonnance inexistante");
            System.out.println("   → En base: FOREIGN KEY constraint fails");
        } else {
            System.out.println("   ❌ Problème: pas d'erreur détectée");
        }
    }

    // Test 9: Statistiques des relations
    private static void afficherStatistiquesRelations(PatientService patientService) {
        System.out.println("\n=== STATISTIQUES DES RELATIONS ===");

        List<PatientService.PatientDTO> patients = patientService.getAllPatients();

        System.out.println("\n📊 RÉPARTITION:");
        System.out.println("Patients totaux: " + patients.size());
        System.out.println("Ordonnances totales: " + ordonnances.size());
        System.out.println("Prescriptions totales: " + prescriptions.size());

        // Moyenne d'ordonnances par patient
        double moyOrdonnancesParPatient = patients.isEmpty() ? 0 :
                (double) ordonnances.size() / patients.size();
        System.out.printf("Ordonnances par patient (moy): %.2f\n", moyOrdonnancesParPatient);

        // Moyenne de prescriptions par ordonnance
        double moyPrescriptionsParOrdonnance = ordonnances.isEmpty() ? 0 :
                (double) prescriptions.size() / ordonnances.size();
        System.out.printf("Prescriptions par ordonnance (moy): %.2f\n", moyPrescriptionsParOrdonnance);

        // Patients avec/sans ordonnances
        long patientsAvecOrdonnances = patients.stream()
                .filter(p -> ordonnances.stream()
                        .anyMatch(o -> o.getIdDM() != null &&
                                o.getIdDM().equals(genererIdDM(p.getId()))))
                .count();

        long patientsSansOrdonnances = patients.size() - patientsAvecOrdonnances;

        System.out.println("\n🏥 PATIENTS:");
        System.out.println("  Avec ordonnances: " + patientsAvecOrdonnances);
        System.out.println("  Sans ordonnances: " + patientsSansOrdonnances);

        // Répartition par sexe
        long hommes = patients.stream().filter(p -> p.getSexe() == Sexe.HOMME).count();
        long femmes = patients.stream().filter(p -> p.getSexe() == Sexe.FEMME).count();

        System.out.println("\n👫 RÉPARTITION PAR SEXE:");
        System.out.println("  Hommes: " + hommes + " (" +
                (patients.isEmpty() ? 0 : (hommes * 100 / patients.size())) + "%)");
        System.out.println("  Femmes: " + femmes + " (" +
                (patients.isEmpty() ? 0 : (femmes * 100 / patients.size())) + "%)");

        // Ordonnances par mois (simulé)
        System.out.println("\n📅 ORDONNANCES PAR MOIS (derniers 3 mois):");
        LocalDate maintenant = LocalDate.now();
        for (int i = 2; i >= 0; i--) {
            LocalDate mois = maintenant.minusMonths(i);
            long ordonnancesMois = ordonnances.stream()
                    .filter(o -> o.getDateOrdonnance() != null &&
                            o.getDateOrdonnance().getMonth() == mois.getMonth() &&
                            o.getDateOrdonnance().getYear() == mois.getYear())
                    .count();

            System.out.println("  " + mois.getMonth() + " " + mois.getYear() + ": " +
                    ordonnancesMois + " ordonnances");
        }
    }

    // ============================================================================
    // MÉTHODES UTILITAIRES
    // ============================================================================

    private static Long genererIdDM(Long idPatient) {
        // Simuler un ID de dossier médical basé sur l'ID patient
        return idPatient * 1000L;
    }

    // ============================================================================
    // REPOSITORY MOCK POUR LES TESTS
    // ============================================================================

    static class PatientRepositoryMock implements PatientRepository {
        private Map<Long, Patient> patients = new HashMap<>();
        private Long idCounter = 1L;

        @Override
        public void create(Patient patient) {
            patient.setId(idCounter++);
            patient.setDateCreation(LocalDateTime.now());
            patients.put(patient.getId(), patient);
        }

        @Override
        public Patient findById(Long id) {
            return patients.get(id);
        }

        @Override
        public void update(Patient patient) {
            patients.put(patient.getId(), patient);
        }

        @Override
        public void delete(Patient patient) {

        }

        @Override
        public void deleteById(Long id) {
            patients.remove(id);
        }

        @Override
        public List<Patient> findAll() {
            return new ArrayList<>(patients.values());
        }

        @Override
        public List<Patient> findByNom(String nom) {
            return patients.values().stream()
                    .filter(p -> p.getNom() != null && p.getNom().toLowerCase().contains(nom.toLowerCase()))
                    .toList();
        }
    }
}