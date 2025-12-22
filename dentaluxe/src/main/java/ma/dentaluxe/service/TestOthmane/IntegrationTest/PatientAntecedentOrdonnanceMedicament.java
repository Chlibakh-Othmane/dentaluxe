/*
package ma.dentaluxe.service.TestOthmane.IntegrationTest;

import ma.dentaluxe.service.ordonnance.api.OrdonnanceService;
import ma.dentaluxe.service.patient.baseImplimentation.PatientServiceImpl;
import ma.dentaluxe.service.patient.baseImplimentation.AntecedentServiceImpl;
import ma.dentaluxe.service.ordonnance.baseImplementation.OrdonnanceServiceImpl;
import ma.dentaluxe.service.ordonnance.baseImplementation.PrescriptionServiceImpl;
import ma.dentaluxe.service.medicament.baseImplimentation.MedicamentServiceImpl;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.ordonnance.Medicament;
import ma.dentaluxe.entities.enums.*;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;
import ma.dentaluxe.repository.modules.patient.inMemDB_implementation.PatientRepositoryImpl;
import ma.dentaluxe.repository.modules.patient.inMemDB_implementation.AntecedentRepositoryImpl;
import ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation.OrdonnanceRepositoryImpl;
import ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation.PrescriptionRepositoryImpl;
import ma.dentaluxe.repository.modules.medicament.inMemDB_implementation.MedicamentRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Test interactif avec menu pour Patient -> Antecedent -> Ordonnance -> Prescription -> Medicament

public class PatientAntecedentOrdonnanceMedicament {

    // Services
    private static PatientServiceImpl patientService;
    private static AntecedentServiceImpl antecedentService;
    private static OrdonnanceServiceImpl ordonnanceService;
    private static PrescriptionServiceImpl prescriptionService;
    private static MedicamentServiceImpl medicamentService;

    // Données temporaires
    private static Patient currentPatient;
    private static Antecedent currentAntecedent;
    private static Medicament currentMedicament;
    private static Ordonnance currentOrdonnance;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🚀 TEST D'INTÉGRATION INTERACTIF");
        System.out.println("==================================\n");

        // Initialisation
        initializeServices();

        // Menu principal
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Votre choix: ");

            switch (choice) {
                case 1:
                    createProcess();
                    break;
                case 2:
                    insertProcess();
                    break;
                case 3:
                    updateProcess();
                    break;
                case 4:
                    deleteProcess();
                    break;
                case 5:
                    viewAllData();
                    break;
                case 6:
                    testCompleteFlow();
                    break;
                case 0:
                    running = false;
                    System.out.println("\nAu revoir !");
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }

        scanner.close();
    }

    private static void initializeServices() {
        System.out.println("⚙️ Initialisation des services...\n");

        PatientRepository patientRepo = new PatientRepositoryImpl();
        AntecedentRepository antecedentRepo = new AntecedentRepositoryImpl();
        OrdonnanceRepository ordonnanceRepo = new OrdonnanceRepositoryImpl();
        PrescriptionRepository prescriptionRepo = new PrescriptionRepositoryImpl();
        MedicamentRepository medicamentRepo = new MedicamentRepositoryImpl();

        patientService = new PatientServiceImpl();
        antecedentService = new AntecedentServiceImpl();
        ordonnanceService = new OrdonnanceServiceImpl(ordonnanceRepo, prescriptionRepo);
        prescriptionService = new PrescriptionServiceImpl(prescriptionRepo, ordonnanceRepo);
        medicamentService = new MedicamentServiceImpl(medicamentRepo);

        System.out.println("✅ Services initialisés avec succès !\n");
    }

    private static void displayMainMenu() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("              MENU PRINCIPAL");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("1.  Processus de création (CREATE)");
        System.out.println("2.  Processus d'insertion (INSERT)");
        System.out.println("3.  Processus de mise à jour (UPDATE)");
        System.out.println("4.  Processus de suppression (DELETE)");
        System.out.println("5.  Voir toutes les données");
        System.out.println("6.  Tester le flux complet");
        System.out.println("0.  Quitter");
        System.out.println("═══════════════════════════════════════════");
    }

    // ==================== OPTION 1: CREATE PROCESS ====================
    private static void createProcess() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("         PROCESSUS DE CRÉATION");
        System.out.println("═══════════════════════════════════════════");

        boolean creating = true;
        while (creating) {
            System.out.println("\nQuelle entité voulez-vous créer ?");
            System.out.println("1.  Patient");
            System.out.println("2.  Antécédent");
            System.out.println("3.  Médicament");
            System.out.println("4.  Ordonnance");
            System.out.println("5.  Associer antécédent à patient");
            System.out.println("6.  Créer une prescription");
            System.out.println("0.  Retour au menu principal");

            int choice = getIntInput("Votre choix: ");

            switch (choice) {
                case 1:
                    createPatient();
                    break;
                case 2:
                    createAntecedent();
                    break;
                case 3:
                    createMedicament();
                    break;
                case 4:
                    createOrdonnance();
                    break;
                case 5:
                    associateAntecedentToPatient();
                    break;
                case 6:
                    createPrescription();
                    break;
                case 0:
                    creating = false;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private static void createPatient() {
        System.out.println("\n─── CRÉATION D'UN PATIENT ───");

        PatientServiceImpl.PatientCreateDTOImpl patientDTO =
                new PatientServiceImpl.PatientCreateDTOImpl();

        System.out.print("Nom: ");
        patientDTO.setNom(scanner.nextLine());

        System.out.print("Prénom: ");
        patientDTO.setPrenom(scanner.nextLine());

        System.out.print("Adresse: ");
        patientDTO.setAdresse(scanner.nextLine());

        System.out.print("Téléphone: ");
        patientDTO.setTelephone(scanner.nextLine());

        System.out.print("Email: ");
        patientDTO.setEmail(scanner.nextLine());

        System.out.print("Date de naissance (AAAA-MM-JJ): ");
        patientDTO.setDateNaissance(LocalDate.parse(scanner.nextLine()));

        System.out.println("Sexe (1-HOMME, 2-FEMME): ");
        int sexeChoice = getIntInput("Votre choix: ");
        patientDTO.setSexe(sexeChoice == 1 ? Sexe.HOMME : Sexe.FEMME);

        System.out.println("Assurance (1-CNOPS, 2-CNSS, 3-AUTRE): ");
        int assuranceChoice = getIntInput("Votre choix: ");
        switch (assuranceChoice) {
            case 1: patientDTO.setAssurance(Assurance.CNOPS); break;
            case 2: patientDTO.setAssurance(Assurance.CNSS); break;
            default: patientDTO.setAssurance(Assurance.Autre); break;
        }

        try {
            PatientServiceImpl.PatientDTOImpl createdPatient =
                    (PatientServiceImpl.PatientDTOImpl) patientService.createPatient(patientDTO);
            currentPatient = convertToPatient(createdPatient);

            System.out.println("\n✅ Patient créé avec succès !");
            System.out.println("ID: " + createdPatient.getId());
            System.out.println("Nom complet: " + createdPatient.getFullName());
            System.out.println("Age: " + createdPatient.getAge() + " ans");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void createAntecedent() {
        System.out.println("\n─── CRÉATION D'UN ANTÉCÉDENT ───");

        Antecedent antecedent = new Antecedent();

        System.out.print("Nom de l'antécédent: ");
        antecedent.setNom(scanner.nextLine());

        System.out.println("Catégorie (1-MEDICAL, 2-CHIRURGICAL, 3-FAMILIAL, 4-AUTRE): ");
        int catChoice = getIntInput("Votre choix: ");
        switch (catChoice) {
            case 1: antecedent.setCategorie(CategorieAntecedent.MEDICAL); break;
            case 2: antecedent.setCategorie(CategorieAntecedent.CHIRURGICAL); break;
            case 3: antecedent.setCategorie(CategorieAntecedent.FAMILIAL); break;
            default: antecedent.setCategorie(CategorieAntecedent.AUTRE); break;
        }

        System.out.println("Niveau de risque (1-FAIBLE, 2-MOYEN, 3-ELEVE): ");
        int riskChoice = getIntInput("Votre choix: ");
        switch (riskChoice) {
            case 1: antecedent.setNiveauRisque(NiveauRisque.FAIBLE); break;
            case 2: antecedent.setNiveauRisque(NiveauRisque.MOYEN); break;
            case 3: antecedent.setNiveauRisque(NiveauRisque.ELEVE); break;
            default: antecedent.setNiveauRisque(NiveauRisque.MOYEN); break;
        }

        try {
            Antecedent created = antecedentService.create(antecedent);
            currentAntecedent = created;

            System.out.println("\n✅ Antécédent créé avec succès !");
            System.out.println("ID: " + created.getId());
            System.out.println("Nom: " + created.getNom());
            System.out.println("Catégorie: " + created.getCategorie());
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void createMedicament() {
        System.out.println("\n─── CRÉATION D'UN MÉDICAMENT ───");

        Medicament medicament = new Medicament();

        System.out.print("Nom du médicament: ");
        medicament.setNom(scanner.nextLine());

        System.out.print("Type (ex: Analgésique, Antibiotique): ");
        medicament.setType(scanner.nextLine());

        System.out.print("Forme (ex: Comprimé, Sirop): ");
        medicament.setForme(scanner.nextLine());

        System.out.print("Prix unitaire: ");
        medicament.setPrixUnitaire(scanner.nextDouble());
        scanner.nextLine(); // Consommer la nouvelle ligne

        System.out.print("Remboursable (true/false): ");
        medicament.setRemboursable(scanner.nextBoolean());
        scanner.nextLine();

        System.out.print("Description: ");
        medicament.setDescription(scanner.nextLine());

        try {
            Medicament created = medicamentService.create(medicament);
            currentMedicament = created;

            System.out.println("\n✅ Médicament créé avec succès !");
            System.out.println("ID: " + created.getIdMedicament());
            System.out.println("Nom: " + created.getNom());
            System.out.println("Prix: " + created.getPrixUnitaire() + " DH");
            System.out.println("Remboursable: " + (created.getRemboursable() ? "Oui" : "Non"));
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void createOrdonnance() {
        System.out.println("\n─── CRÉATION D'UNE ORDONNANCE ───");

        if (currentPatient == null) {
            System.out.println("❌ Aucun patient sélectionné ! Créez d'abord un patient.");
            return;
        }

        OrdonnanceServiceImpl.OrdonnanceCreateDTOImpl ordonnanceDTO =
                new OrdonnanceServiceImpl.OrdonnanceCreateDTOImpl();

        ordonnanceDTO.setIdDM(currentPatient.getId());

        System.out.print("ID du médecin: ");
        ordonnanceDTO.setIdMedecin(scanner.nextLong());
        scanner.nextLine();

        System.out.print("Date de l'ordonnance (AAAA-MM-JJ) [laissez vide pour aujourd'hui]: ");
        String dateStr = scanner.nextLine();
        if (dateStr.isEmpty()) {
            ordonnanceDTO.setDateOrdonnance(LocalDate.now());
        } else {
            ordonnanceDTO.setDateOrdonnance(LocalDate.parse(dateStr));
        }

        System.out.print("Remarques: ");
        ordonnanceDTO.setRemarques(scanner.nextLine());

        // Demander combien de prescriptions
        System.out.print("Nombre de prescriptions pour cette ordonnance: ");
        int nbPrescriptions = getIntInput("");

        List<OrdonnanceService.PrescriptionCreateDTO> prescriptions =
                new ArrayList<>();

        for (int i = 1; i <= nbPrescriptions; i++) {
            System.out.println("\n📝 Prescription #" + i);
            prescriptions.add(createPrescriptionDTO());
        }

        try {
            OrdonnanceServiceImpl.OrdonnanceDTOImpl createdOrdonnance =
                    (OrdonnanceServiceImpl.OrdonnanceDTOImpl)
                            ordonnanceService.createOrdonnance(ordonnanceDTO, prescriptions);
            currentOrdonnance = convertToOrdonnance(createdOrdonnance);

            System.out.println("\n✅ Ordonnance créée avec succès !");
            System.out.println("ID: " + createdOrdonnance.getIdOrdo());
            System.out.println("Date: " + createdOrdonnance.getDateOrdonnance());
            System.out.println("Nombre de médicaments: " + createdOrdonnance.getNombreMedicaments());
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static OrdonnanceServiceImpl.PrescriptionCreateDTOImpl createPrescriptionDTO() {
        OrdonnanceServiceImpl.PrescriptionCreateDTOImpl prescriptionDTO =
                new OrdonnanceServiceImpl.PrescriptionCreateDTOImpl();

        if (currentMedicament == null) {
            System.out.print("ID du médicament: ");
            prescriptionDTO.setIdMedicament(scanner.nextLong());
            scanner.nextLine();
        } else {
            prescriptionDTO.setIdMedicament(currentMedicament.getIdMedicament());
            System.out.println("Médicament: " + currentMedicament.getNom());
        }

        System.out.print("Quantité: ");
        prescriptionDTO.setQuantite(getIntInput(""));

        System.out.print("Fréquence (ex: 3 fois par jour): ");
        prescriptionDTO.setFrequence(scanner.nextLine());

        System.out.print("Durée en jours: ");
        prescriptionDTO.setDureeEnJours(getIntInput(""));

        return prescriptionDTO;
    }

    private static void associateAntecedentToPatient() {
        System.out.println("\n─── ASSOCIATION ANTÉCÉDENT-PATIENT ───");

        if (currentPatient == null || currentAntecedent == null) {
            System.out.println("❌ Patient ou antécédent manquant !");
            return;
        }

        System.out.print("Notes pour cette association: ");
        String notes = scanner.nextLine();

        try {
            antecedentService.addAntecedentToPatient(
                    currentPatient.getId(),
                    currentAntecedent.getId(),
                    notes
            );

            System.out.println("\n✅ Association créée avec succès !");
            System.out.println("Patient: " + currentPatient.getNom() + " " + currentPatient.getPrenom());
            System.out.println("Antécédent: " + currentAntecedent.getNom());
            System.out.println("Notes: " + notes);
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void createPrescription() {
        System.out.println("\n─── CRÉATION D'UNE PRESCRIPTION ───");

        if (currentOrdonnance == null) {
            System.out.println("❌ Aucune ordonnance sélectionnée !");
            return;
        }

        PrescriptionServiceImpl.PrescriptionCreateDTOImpl prescriptionDTO =
                new PrescriptionServiceImpl.PrescriptionCreateDTOImpl();

        prescriptionDTO.setIdOrdo(currentOrdonnance.getIdOrdo());

        if (currentMedicament == null) {
            System.out.print("ID du médicament: ");
            prescriptionDTO.setIdMedicament(scanner.nextLong());
            scanner.nextLine();
        } else {
            prescriptionDTO.setIdMedicament(currentMedicament.getIdMedicament());
            System.out.println("Médicament: " + currentMedicament.getNom());
        }

        System.out.print("Quantité: ");
        prescriptionDTO.setQuantite(getIntInput(""));

        System.out.print("Fréquence: ");
        prescriptionDTO.setFrequence(scanner.nextLine());

        System.out.print("Durée en jours: ");
        prescriptionDTO.setDureeEnJours(getIntInput(""));

        System.out.print("Instructions supplémentaires: ");
        prescriptionDTO.setInstructions(scanner.nextLine());

        try {
            PrescriptionServiceImpl.PrescriptionDTOImpl created =
                    (PrescriptionServiceImpl.PrescriptionDTOImpl)
                            prescriptionService.createPrescription(prescriptionDTO);

            System.out.println("\n✅ Prescription créée avec succès !");
            System.out.println("ID: " + created.getIdPrescription());
            System.out.println("Ordonnance: " + created.getIdOrdo());
            System.out.println("Quantité totale: " + created.getQuantiteTotale());
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== OPTION 2: INSERT PROCESS ====================
    private static void insertProcess() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("         PROCESSUS D'INSERTION");
        System.out.println("═══════════════════════════════════════════");

        System.out.println("\n📋 Ce processus va insérer un jeu de données complet:");
        System.out.println("1. Un patient");
        System.out.println("2. Un antécédent");
        System.out.println("3. Un médicament");
        System.out.println("4. Une ordonnance avec prescription");
        System.out.println("5. Association patient-antécédent");

        System.out.print("\nVoulez-vous continuer ? (oui/non): ");
        String response = scanner.nextLine();

        if (!response.equalsIgnoreCase("oui")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            System.out.println("\n--- INSERTION EN COURS ---");

            // 1. Insertion Patient
            System.out.println("1. Insertion du patient...");
            PatientServiceImpl.PatientCreateDTOImpl patientDTO =
                    new PatientServiceImpl.PatientCreateDTOImpl();
            patientDTO.setNom("BENNOUNA");
            patientDTO.setPrenom("Karim");
            patientDTO.setAdresse("45 Rue Moulay Ismail, Marrakech");
            patientDTO.setTelephone("0678901234");
            patientDTO.setEmail("karim.bennouna@email.com");
            patientDTO.setDateNaissance(LocalDate.of(1990, 8, 22));
            patientDTO.setSexe(Sexe.HOMME);
            patientDTO.setAssurance(Assurance.CNSS);

            PatientServiceImpl.PatientDTOImpl insertedPatient =
                    (PatientServiceImpl.PatientDTOImpl) patientService.createPatient(patientDTO);
            currentPatient = convertToPatient(insertedPatient);
            System.out.println("   ✅ Patient inséré: " + insertedPatient.getFullName());

            // 2. Insertion Antécédent
            System.out.println("2. Insertion de l'antécédent...");
            Antecedent antecedent = new Antecedent();
            antecedent.setNom("Diabète de type 2");
            antecedent.setCategorie(CategorieAntecedent.MEDICAL);
            antecedent.setNiveauRisque(NiveauRisque.MOYEN);

            Antecedent insertedAntecedent = antecedentService.create(antecedent);
            currentAntecedent = insertedAntecedent;
            System.out.println("   ✅ Antécédent inséré: " + insertedAntecedent.getNom());

            // 3. Insertion Médicament
            System.out.println("3. Insertion du médicament...");
            Medicament medicament = new Medicament();
            medicament.setNom("Metformine 850mg");
            medicament.setType("Antidiabétique");
            medicament.setForme("Comprimé");
            medicament.setPrixUnitaire(45.75);
            medicament.setRemboursable(true);
            medicament.setDescription("Médicament pour le traitement du diabète de type 2");

            Medicament insertedMedicament = medicamentService.create(medicament);
            currentMedicament = insertedMedicament;
            System.out.println("   ✅ Médicament inséré: " + insertedMedicament.getNom());

            // 4. Association patient-antécédent
            System.out.println("4. Association patient-antécédent...");
            antecedentService.addAntecedentToPatient(
                    currentPatient.getId(),
                    currentAntecedent.getId(),
                    "Diagnostiqué en 2021, régime alimentaire contrôlé"
            );
            System.out.println("   ✅ Association créée");

            // 5. Insertion Ordonnance avec Prescription
            System.out.println("5. Insertion de l'ordonnance...");
            OrdonnanceServiceImpl.OrdonnanceCreateDTOImpl ordonnanceDTO =
                    new OrdonnanceServiceImpl.OrdonnanceCreateDTOImpl();
            ordonnanceDTO.setIdDM(currentPatient.getId());
            ordonnanceDTO.setIdMedecin(2L);
            ordonnanceDTO.setDateOrdonnance(LocalDate.now());
            ordonnanceDTO.setRemarques("Traitement pour diabète de type 2");

            OrdonnanceServiceImpl.PrescriptionCreateDTOImpl prescriptionDTO =
                    new OrdonnanceServiceImpl.PrescriptionCreateDTOImpl();
            prescriptionDTO.setIdMedicament(currentMedicament.getIdMedicament());
            prescriptionDTO.setQuantite(60);
            prescriptionDTO.setFrequence("2 fois par jour");
            prescriptionDTO.setDureeEnJours(30);

            List<OrdonnanceService.PrescriptionCreateDTO> prescriptions =
                    new ArrayList<>();
            prescriptions.add(prescriptionDTO);

            OrdonnanceServiceImpl.OrdonnanceDTOImpl insertedOrdonnance =
                    (OrdonnanceServiceImpl.OrdonnanceDTOImpl)
                            ordonnanceService.createOrdonnance(ordonnanceDTO, prescriptions);
            currentOrdonnance = convertToOrdonnance(insertedOrdonnance);
            System.out.println("   ✅ Ordonnance insérée: ID " + insertedOrdonnance.getIdOrdo());

            System.out.println("\n🎉 INSERTION TERMINÉE AVEC SUCCÈS !");
            System.out.println("-----------------------------------");
            System.out.println("Patient: " + currentPatient.getNom() + " " + currentPatient.getPrenom());
            System.out.println("Antécédent: " + currentAntecedent.getNom());
            System.out.println("Médicament: " + currentMedicament.getNom());
            System.out.println("Ordonnance: #" + currentOrdonnance.getIdOrdo());

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'insertion: " + e.getMessage());
        }
    }

    // ==================== OPTION 3: UPDATE PROCESS ====================
    private static void updateProcess() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("      PROCESSUS DE MISE À JOUR");
        System.out.println("═══════════════════════════════════════════");

        boolean updating = true;
        while (updating) {
            System.out.println("\nQuelle entité voulez-vous mettre à jour ?");
            System.out.println("1.  Mettre à jour le patient");
            System.out.println("2.  Mettre à jour l'antécédent");
            System.out.println("3.  Mettre à jour le médicament");
            System.out.println("4.  Mettre à jour les notes d'association");
            System.out.println("5.  Mettre à jour une prescription");
            System.out.println("0.  Retour au menu principal");

            int choice = getIntInput("Votre choix: ");

            switch (choice) {
                case 1:
                    updatePatient();
                    break;
                case 2:
                    updateAntecedent();
                    break;
                case 3:
                    updateMedicament();
                    break;
                case 4:
                    updateAssociationNotes();
                    break;
                case 5:
                    updatePrescription();
                    break;
                case 0:
                    updating = false;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private static void updatePatient() {
        if (currentPatient == null) {
            System.out.println("❌ Aucun patient sélectionné !");
            return;
        }

        System.out.println("\n─── MISE À JOUR DU PATIENT ───");
        System.out.println("Patient actuel: " + currentPatient.getNom() + " " + currentPatient.getPrenom());

        PatientServiceImpl.PatientUpdateDTOImpl updateDTO =
                new PatientServiceImpl.PatientUpdateDTOImpl();

        System.out.print("Nouveau téléphone (laissez vide pour ne pas changer): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) updateDTO.setTelephone(phone);

        System.out.print("Nouvelle adresse (laissez vide pour ne pas changer): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) updateDTO.setAdresse(address);

        System.out.print("Nouvel email (laissez vide pour ne pas changer): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) updateDTO.setEmail(email);

        try {
            PatientServiceImpl.PatientDTOImpl updated =
                    (PatientServiceImpl.PatientDTOImpl) patientService.updatePatient(
                            currentPatient.getId(), updateDTO);
            currentPatient = convertToPatient(updated);

            System.out.println("\n✅ Patient mis à jour avec succès !");
            System.out.println("Nouveau téléphone: " + updated.getTelephone());
            System.out.println("Nouvelle adresse: " + updated.getAdresse());
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void updateAntecedent() {
        if (currentAntecedent == null) {
            System.out.println("❌ Aucun antécédent sélectionné !");
            return;
        }

        System.out.println("\n─── MISE À JOUR DE L'ANTÉCÉDENT ───");
        System.out.println("Antécédent actuel: " + currentAntecedent.getNom());

        Antecedent update = new Antecedent();

        System.out.print("Nouveau nom (laissez vide pour ne pas changer): ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) update.setNom(nom);

        System.out.println("Nouvelle catégorie (1-MEDICAL, 2-CHIRURGICAL, 3-FAMILIAL, 4-AUTRE, 0-ne pas changer): ");
        int catChoice = getIntInput("Votre choix: ");
        if (catChoice > 0) {
            switch (catChoice) {
                case 1: update.setCategorie(CategorieAntecedent.MEDICAL); break;
                case 2: update.setCategorie(CategorieAntecedent.CHIRURGICAL); break;
                case 3: update.setCategorie(CategorieAntecedent.FAMILIAL); break;
                case 4: update.setCategorie(CategorieAntecedent.AUTRE); break;
            }
        }

        try {
            Antecedent updated = antecedentService.update(currentAntecedent.getId(), update);
            currentAntecedent = updated;

            System.out.println("\n✅ Antécédent mis à jour avec succès !");
            System.out.println("Nouveau nom: " + updated.getNom());
            System.out.println("Catégorie: " + updated.getCategorie());
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void updateMedicament() {
        if (currentMedicament == null) {
            System.out.println("❌ Aucun médicament sélectionné !");
            return;
        }

        System.out.println("\n─── MISE À JOUR DU MÉDICAMENT ───");
        System.out.println("Médicament actuel: " + currentMedicament.getNom());

        Medicament update = new Medicament();

        System.out.print("Nouveau prix (0 pour ne pas changer): ");
        double prix = scanner.nextDouble();
        scanner.nextLine();
        if (prix > 0) update.setPrixUnitaire(prix);

        System.out.print("Nouvelle description (laissez vide pour ne pas changer): ");
        String desc = scanner.nextLine();
        if (!desc.isEmpty()) update.setDescription(desc);

        try {
            Medicament updated = medicamentService.update(currentMedicament.getIdMedicament(), update);
            currentMedicament = updated;

            System.out.println("\n✅ Médicament mis à jour avec succès !");
            System.out.println("Nouveau prix: " + updated.getPrixUnitaire() + " DH");
            System.out.println("Description: " + updated.getDescription());
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void updateAssociationNotes() {
        if (currentPatient == null || currentAntecedent == null) {
            System.out.println("❌ Patient ou antécédent manquant !");
            return;
        }

        System.out.println("\n─── MISE À JOUR DES NOTES D'ASSOCIATION ───");
        System.out.println("Patient: " + currentPatient.getNom() + " " + currentPatient.getPrenom());
        System.out.println("Antécédent: " + currentAntecedent.getNom());

        try {
            // Récupérer les notes actuelles
            String currentNotes = antecedentService.getNotesForPatient(
                    currentPatient.getId(), currentAntecedent.getId());
            System.out.println("Notes actuelles: " + currentNotes);

            System.out.print("\nNouvelles notes: ");
            String newNotes = scanner.nextLine();

            antecedentService.updateNotesForPatient(
                    currentPatient.getId(), currentAntecedent.getId(), newNotes);

            System.out.println("\n✅ Notes mises à jour avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void updatePrescription() {
        System.out.println("\n─── MISE À JOUR D'UNE PRESCRIPTION ───");

        System.out.print("ID de la prescription à mettre à jour: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Nouvelle quantité (0 pour ne pas changer): ");
        int quantite = getIntInput("");

        System.out.print("Nouvelle fréquence (laissez vide pour ne pas changer): ");
        String frequence = scanner.nextLine();

        System.out.print("Nouvelle durée en jours (0 pour ne pas changer): ");
        int duree = getIntInput("");

        try {
            OrdonnanceServiceImpl.PrescriptionDTOImpl updated =
                    (OrdonnanceServiceImpl.PrescriptionDTOImpl)
                            ordonnanceService.updatePrescription(
                                    id,
                                    quantite > 0 ? quantite : null,
                                    !frequence.isEmpty() ? frequence : null,
                                    duree > 0 ? duree : null
                            );

            System.out.println("\n✅ Prescription mise à jour avec succès !");
            System.out.println("Nouvelle quantité: " + updated.getQuantite());
            System.out.println("Nouvelle fréquence: " + updated.getFrequence());
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== OPTION 4: DELETE PROCESS ====================
    private static void deleteProcess() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("         PROCESSUS DE SUPPRESSION");
        System.out.println("═══════════════════════════════════════════");

        boolean deleting = true;
        while (deleting) {
            System.out.println("\nQue voulez-vous supprimer ?");
            System.out.println("1.  Supprimer l'association patient-antécédent");
            System.out.println("2.  Supprimer une ordonnance (cascade sur prescriptions)");
            System.out.println("3.  Supprimer une prescription");
            System.out.println("4.  Supprimer l'antécédent");
            System.out.println("5.  Supprimer le médicament");
            System.out.println("6.  Supprimer le patient (attention!)");
            System.out.println("7.  Supprimer TOUTES les données");
            System.out.println("0.  Retour au menu principal");

            int choice = getIntInput("Votre choix: ");

            switch (choice) {
                case 1:
                    deleteAssociation();
                    break;
                case 2:
                    deleteOrdonnance();
                    break;
                case 3:
                    deletePrescription();
                    break;
                case 4:
                    deleteAntecedent();
                    break;
                case 5:
                    deleteMedicament();
                    break;
                case 6:
                    deletePatient();
                    break;
                case 7:
                    deleteAllData();
                    break;
                case 0:
                    deleting = false;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private static void deleteAssociation() {
        if (currentPatient == null || currentAntecedent == null) {
            System.out.println("❌ Patient ou antécédent manquant !");
            return;
        }

        System.out.print("\nÊtes-vous sûr de vouloir supprimer cette association ? (oui/non): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("oui")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            antecedentService.removeAntecedentFromPatient(
                    currentPatient.getId(), currentAntecedent.getId());
            System.out.println("✅ Association supprimée avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void deleteOrdonnance() {
        if (currentOrdonnance == null) {
            System.out.println("❌ Aucune ordonnance sélectionnée !");
            return;
        }

        System.out.print("\nÊtes-vous sûr de vouloir supprimer l'ordonnance #" +
                currentOrdonnance.getIdOrdo() + " ? (oui/non): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("oui")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            ordonnanceService.deleteOrdonnance(currentOrdonnance.getIdOrdo());
            currentOrdonnance = null;
            System.out.println("✅ Ordonnance supprimée avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void deletePrescription() {
        System.out.print("\nID de la prescription à supprimer: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Êtes-vous sûr ? (oui/non): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("oui")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            ordonnanceService.deletePrescription(id);
            System.out.println("✅ Prescription supprimée avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void deleteAntecedent() {
        if (currentAntecedent == null) {
            System.out.println("❌ Aucun antécédent sélectionné !");
            return;
        }

        System.out.print("\nÊtes-vous sûr de vouloir supprimer l'antécédent '" +
                currentAntecedent.getNom() + "' ? (oui/non): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("oui")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            antecedentService.delete(currentAntecedent.getId());
            currentAntecedent = null;
            System.out.println("✅ Antécédent supprimé avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void deleteMedicament() {
        if (currentMedicament == null) {
            System.out.println("❌ Aucun médicament sélectionné !");
            return;
        }

        System.out.print("\nÊtes-vous sûr de vouloir supprimer le médicament '" +
                currentMedicament.getNom() + "' ? (oui/non): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("oui")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            medicamentService.delete(currentMedicament.getIdMedicament());
            currentMedicament = null;
            System.out.println("✅ Médicament supprimé avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void deletePatient() {
        if (currentPatient == null) {
            System.out.println("❌ Aucun patient sélectionné !");
            return;
        }

        System.out.print("\n⚠️  ATTENTION: La suppression du patient peut affecter les données liées !");
        System.out.print("\nÊtes-vous ABSOLUMENT sûr ? (écrivez 'SUPPRIMER' pour confirmer): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("SUPPRIMER")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            patientService.deletePatient(currentPatient.getId());
            currentPatient = null;
            System.out.println("✅ Patient supprimé avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void deleteAllData() {
        System.out.print("\n⚠️  ⚠️  ATTENTION: Cela supprimera TOUTES les données !");
        System.out.print("\nÊtes-vous ABSOLUMENT sûr ? (écrivez 'TOUT SUPPRIMER' pour confirmer): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("TOUT SUPPRIMER")) {
            System.out.println("Opération annulée.");
            return;
        }

        try {
            System.out.println("\n🧹 Suppression de toutes les données...");

            // Supprimer dans l'ordre inverse des dépendances
            if (currentOrdonnance != null) {
                ordonnanceService.deleteOrdonnance(currentOrdonnance.getIdOrdo());
                System.out.println("Ordonnances supprimées");
            }

            if (currentPatient != null && currentAntecedent != null) {
                try {
                    antecedentService.removeAntecedentFromPatient(
                            currentPatient.getId(), currentAntecedent.getId());
                } catch (Exception e) {}
            }

            if (currentAntecedent != null) {
                try {
                    antecedentService.delete(currentAntecedent.getId());
                    System.out.println("Antécédents supprimés");
                } catch (Exception e) {}
            }

            if (currentMedicament != null) {
                try {
                    medicamentService.delete(currentMedicament.getIdMedicament());
                    System.out.println("Médicaments supprimés");
                } catch (Exception e) {}
            }

            if (currentPatient != null) {
                try {
                    patientService.deletePatient(currentPatient.getId());
                    System.out.println("Patients supprimés");
                } catch (Exception e) {}
            }

            // Réinitialiser les références
            currentPatient = null;
            currentAntecedent = null;
            currentMedicament = null;
            currentOrdonnance = null;

            System.out.println("\n✅ Toutes les données ont été supprimées !");

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la suppression: " + e.getMessage());
        }
    }

    // ==================== OPTION 5: VIEW ALL DATA ====================
    private static void viewAllData() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("           TOUTES LES DONNÉES");
        System.out.println("═══════════════════════════════════════════");

        try {
            // Patients
            System.out.println("\n👤 PATIENTS:");
            System.out.println("─────────────");
            List<PatientServiceImpl.PatientDTOImpl> patients =
                    (List<PatientServiceImpl.PatientDTOImpl>)
                            (List<?>) patientService.getAllPatients();

            if (patients.isEmpty()) {
                System.out.println("Aucun patient trouvé.");
            } else {
                for (PatientServiceImpl.PatientDTOImpl p : patients) {
                    System.out.println("ID: " + p.getId() + " | " + p.getFullName() +
                            " | " + p.getEmail() + " | " + p.getTelephone());
                }
                System.out.println("Total: " + patients.size() + " patient(s)");
            }

            // Antécédents
            System.out.println("\n🏥 ANTÉCÉDENTS:");
            System.out.println("───────────────");
            List<Antecedent> antecedents = antecedentService.findAll();

            if (antecedents.isEmpty()) {
                System.out.println("Aucun antécédent trouvé.");
            } else {
                for (Antecedent a : antecedents) {
                    System.out.println("ID: " + a.getId() + " | " + a.getNom() +
                            " | " + a.getCategorie() + " | Risque: " + a.getNiveauRisque());
                }
                System.out.println("Total: " + antecedents.size() + " antécédent(s)");
            }

            // Médicaments
            System.out.println("\n💊 MÉDICAMENTS:");
            System.out.println("───────────────");
            List<Medicament> medicaments = medicamentService.findAll();

            if (medicaments.isEmpty()) {
                System.out.println("Aucun médicament trouvé.");
            } else {
                for (Medicament m : medicaments) {
                    System.out.println("ID: " + m.getIdMedicament() + " | " + m.getNom() +
                            " | " + m.getType() + " | " + m.getPrixUnitaire() + " DH");
                }
                System.out.println("Total: " + medicaments.size() + " médicament(s)");
            }

            // Ordonnances
            System.out.println("\n📄 ORDONNANCES:");
            System.out.println("───────────────");
            List<OrdonnanceServiceImpl.OrdonnanceDTOImpl> ordonnances =
                    (List<OrdonnanceServiceImpl.OrdonnanceDTOImpl>)
                            (List<?>) ordonnanceService.getAllOrdonnances();

            if (ordonnances.isEmpty()) {
                System.out.println("Aucune ordonnance trouvée.");
            } else {
                for (OrdonnanceServiceImpl.OrdonnanceDTOImpl o : ordonnances) {
                    System.out.println("ID: " + o.getIdOrdo() + " | Patient ID: " + o.getIdDM() +
                            " | Date: " + o.getDateOrdonnance() +
                            " | Médicaments: " + o.getNombreMedicaments());
                }
                System.out.println("Total: " + ordonnances.size() + " ordonnance(s)");
            }

            // Données actuelles en mémoire
            System.out.println("\n💾 DONNÉES ACTUELLES EN MÉMOIRE:");
            System.out.println("─────────────────────────────────");
            System.out.println("Patient: " + (currentPatient != null ?
                    currentPatient.getNom() + " " + currentPatient.getPrenom() : "Aucun"));
            System.out.println("Antécédent: " + (currentAntecedent != null ?
                    currentAntecedent.getNom() : "Aucun"));
            System.out.println("Médicament: " + (currentMedicament != null ?
                    currentMedicament.getNom() : "Aucun"));
            System.out.println("Ordonnance: " + (currentOrdonnance != null ?
                    "#" + currentOrdonnance.getIdOrdo() : "Aucune"));

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la récupération des données: " + e.getMessage());
        }
    }

    // ==================== OPTION 6: TEST COMPLETE FLOW ====================
    private static void testCompleteFlow() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("          TEST DU FLUX COMPLET");
        System.out.println("═══════════════════════════════════════════");

        System.out.println("\nCe test va exécuter un scénario complet:");
        System.out.println("1. Créer un patient");
        System.out.println("2. Créer un antécédent");
        System.out.println("3. Créer un médicament");
        System.out.println("4. Associer l'antécédent au patient");
        System.out.println("5. Créer une ordonnance avec prescription");
        System.out.println("6. Mettre à jour certaines données");
        System.out.println("7. Afficher les statistiques");
        System.out.println("8. Nettoyer les données (optionnel)");

        System.out.print("\nVoulez-vous exécuter ce test ? (oui/non): ");
        String response = scanner.nextLine();

        if (!response.equalsIgnoreCase("oui")) {
            System.out.println("Test annulé.");
            return;
        }

        try {
            System.out.println("\nDÉBUT DU TEST DU FLUX COMPLET\n");

            // ========== PHASE 1: CRÉATION ==========
            System.out.println(" PHASE 1: CRÉATION DES DONNÉES");
            System.out.println("────────────────────────────────");

            // 1. Création Patient
            System.out.println("1. Création d'un patient test...");
            PatientServiceImpl.PatientCreateDTOImpl patientDTO =
                    new PatientServiceImpl.PatientCreateDTOImpl();
            patientDTO.setNom("TEST");
            patientDTO.setPrenom("FluxComplet");
            patientDTO.setAdresse("Adresse test");
            patientDTO.setTelephone("0600000000");
            patientDTO.setEmail("test.flux@email.com");
            patientDTO.setDateNaissance(LocalDate.of(1985, 1, 1));
            patientDTO.setSexe(Sexe.HOMME);
            patientDTO.setAssurance(Assurance.CNOPS);

            PatientServiceImpl.PatientDTOImpl patient =
                    (PatientServiceImpl.PatientDTOImpl) patientService.createPatient(patientDTO);
            currentPatient = convertToPatient(patient);
            System.out.println("   ✅ Patient créé: " + patient.getFullName());

            // 2. Création Antécédent
            System.out.println("2. Création d'un antécédent test...");
            Antecedent antecedent = new Antecedent();
            antecedent.setNom("Allergie test");
            antecedent.setCategorie(CategorieAntecedent.MEDICAL);
            antecedent.setNiveauRisque(NiveauRisque.FAIBLE);

            Antecedent createdAntecedent = antecedentService.create(antecedent);
            currentAntecedent = createdAntecedent;
            System.out.println("   ✅ Antécédent créé: " + createdAntecedent.getNom());

            // 3. Création Médicament
            System.out.println("3. Création d'un médicament test...");
            Medicament medicament = new Medicament();
            medicament.setNom("TestMedic");
            medicament.setType("Test");
            medicament.setForme("Comprimé test");
            medicament.setPrixUnitaire(10.0);
            medicament.setRemboursable(false);
            medicament.setDescription("Médicament de test");

            Medicament createdMedicament = medicamentService.create(medicament);
            currentMedicament = createdMedicament;
            System.out.println("   ✅ Médicament créé: " + createdMedicament.getNom());

            // 4. Association
            System.out.println("4. Association patient-antécédent...");
            antecedentService.addAntecedentToPatient(
                    currentPatient.getId(),
                    currentAntecedent.getId(),
                    "Note de test pour l'association"
            );
            System.out.println("   ✅ Association créée");

            // 5. Création Ordonnance
            System.out.println("5. Création d'une ordonnance test...");
            OrdonnanceServiceImpl.OrdonnanceCreateDTOImpl ordonnanceDTO =
                    new OrdonnanceServiceImpl.OrdonnanceCreateDTOImpl();
            ordonnanceDTO.setIdDM(currentPatient.getId());
            ordonnanceDTO.setIdMedecin(999L);
            ordonnanceDTO.setDateOrdonnance(LocalDate.now());

            OrdonnanceServiceImpl.PrescriptionCreateDTOImpl prescriptionDTO =
                    new OrdonnanceServiceImpl.PrescriptionCreateDTOImpl();
            prescriptionDTO.setIdMedicament(currentMedicament.getIdMedicament());
            prescriptionDTO.setQuantite(5);
            prescriptionDTO.setFrequence("1 fois par jour");
            prescriptionDTO.setDureeEnJours(3);

            List<OrdonnanceService.PrescriptionCreateDTO> prescriptions =
                    new ArrayList<>();
            prescriptions.add(prescriptionDTO);

            OrdonnanceServiceImpl.OrdonnanceDTOImpl ordonnance =
                    (OrdonnanceServiceImpl.OrdonnanceDTOImpl)
                            ordonnanceService.createOrdonnance(ordonnanceDTO, prescriptions);
            currentOrdonnance = convertToOrdonnance(ordonnance);
            System.out.println("   ✅ Ordonnance créée: ID " + ordonnance.getIdOrdo());

            System.out.println("\n✅ PHASE 1 TERMINÉE - Données créées avec succès !\n");

            // ========== PHASE 2: VÉRIFICATION ==========
            System.out.println(" PHASE 2: VÉRIFICATION DES DONNÉES");
            System.out.println("────────────────────────────────────");

            // Vérifier que tout existe
            System.out.println("Vérification des entités créées...");

            PatientServiceImpl.PatientDTOImpl verifyPatient =
                    (PatientServiceImpl.PatientDTOImpl) patientService.getPatientById(currentPatient.getId());
            System.out.println("✅ Patient vérifié: " + verifyPatient.getFullName());

            List<Antecedent> verifyAntecedents = antecedentService.findByPatientId(currentPatient.getId());
            System.out.println("✅ Antécédents du patient: " + verifyAntecedents.size());

            List<OrdonnanceServiceImpl.OrdonnanceDTOImpl> verifyOrdonnances =
                    (List<OrdonnanceServiceImpl.OrdonnanceDTOImpl>)
                            (List<?>) ordonnanceService.getOrdonnancesByDossierMedical(currentPatient.getId());
            System.out.println("✅ Ordonnances du patient: " + verifyOrdonnances.size());

            Medicament verifyMedicament = medicamentService.findById(currentMedicament.getIdMedicament());
            System.out.println("✅ Médicament vérifié: " + verifyMedicament.getNom());

            System.out.println("\n✅ PHASE 2 TERMINÉE - Toutes les vérifications OK !\n");

            // ========== PHASE 3: MISE À JOUR ==========
            System.out.println("  PHASE 3: MISE À JOUR DES DONNÉES");
            System.out.println("────────────────────────────────────");

            // Mettre à jour le patient
            System.out.println("1. Mise à jour du téléphone du patient...");
            PatientServiceImpl.PatientUpdateDTOImpl updatePatientDTO =
                    new PatientServiceImpl.PatientUpdateDTOImpl();
            updatePatientDTO.setTelephone("0611111111");

            PatientServiceImpl.PatientDTOImpl updatedPatient =
                    (PatientServiceImpl.PatientDTOImpl) patientService.updatePatient(
                            currentPatient.getId(), updatePatientDTO);
            System.out.println("   ✅ Téléphone mis à jour: " + updatedPatient.getTelephone());

            // Mettre à jour le médicament
            System.out.println("2. Mise à jour du prix du médicament...");
            Medicament updateMed = new Medicament();
            updateMed.setPrixUnitaire(12.5);

            Medicament updatedMed = medicamentService.update(
                    currentMedicament.getIdMedicament(), updateMed);
            System.out.println("   ✅ Prix mis à jour: " + updatedMed.getPrixUnitaire() + " DH");

            System.out.println("\n✅ PHASE 3 TERMINÉE - Mises à jour effectuées !\n");

            // ========== PHASE 4: STATISTIQUES ==========
            System.out.println(" PHASE 4: STATISTIQUES");
            System.out.println("────────────────────────");

            System.out.println("Statistiques globales...");

            PatientServiceImpl.PatientStatisticsDTOImpl patientStats =
                    (PatientServiceImpl.PatientStatisticsDTOImpl) patientService.getStatistics();
            System.out.println(" Patients: " + patientStats.getTotalPatients());

            MedicamentServiceImpl.MedicamentStatisticsImpl medStats =
                    (MedicamentServiceImpl.MedicamentStatisticsImpl) medicamentService.getStatistics();
            System.out.println(" Médicaments: " + medStats.getTotalMedicaments());

            System.out.println("\n✅ PHASE 4 TERMINÉE - Statistiques générées !\n");

            // ========== PHASE 5: NETTOYAGE ==========
            System.out.println("🧹 PHASE 5: NETTOYAGE (optionnel)");
            System.out.println("─────────────────────────────────");

            System.out.print("Voulez-vous nettoyer les données de test ? (oui/non): ");
            String clean = scanner.nextLine();

            if (clean.equalsIgnoreCase("oui")) {
                System.out.println("Nettoyage en cours...");

                // Supprimer dans l'ordre
                if (currentOrdonnance != null) {
                    ordonnanceService.deleteOrdonnance(currentOrdonnance.getIdOrdo());
                    System.out.println("   Ordonnance supprimée");
                }

                if (currentPatient != null && currentAntecedent != null) {
                    try {
                        antecedentService.removeAntecedentFromPatient(
                                currentPatient.getId(), currentAntecedent.getId());
                        System.out.println("   Association supprimée");
                    } catch (Exception e) {}
                }

                if (currentAntecedent != null) {
                    antecedentService.delete(currentAntecedent.getId());
                    System.out.println("   Antécédent supprimé");
                }

                if (currentMedicament != null) {
                    medicamentService.delete(currentMedicament.getIdMedicament());
                    System.out.println("   Médicament supprimé");
                }

                if (currentPatient != null) {
                    patientService.deletePatient(currentPatient.getId());
                    System.out.println("   Patient supprimé");
                }

                // Réinitialiser
                currentPatient = null;
                currentAntecedent = null;
                currentMedicament = null;
                currentOrdonnance = null;

                System.out.println("\n✅ Données de test nettoyées !");
            } else {
                System.out.println("⚠️  Les données de test ont été conservées.");
                System.out.println("   Vous pouvez les visualiser via l'option 5 du menu.");
            }

            System.out.println("\n🎉 TEST DU FLUX COMPLET TERMINÉ AVEC SUCCÈS !");
            System.out.println("==============================================");

        } catch (Exception e) {
            System.out.println("❌ Erreur pendant le test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    private static int getIntInput(String message) {
        System.out.print(message);
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre: ");
            }
        }
    }

    private static Patient convertToPatient(PatientServiceImpl.PatientDTOImpl dto) {
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setAdresse(dto.getAdresse());
        patient.setTelephone(dto.getTelephone());
        patient.setEmail(dto.getEmail());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setDateCreation(dto.getDateCreation());
        patient.setSexe(dto.getSexe());
        patient.setAssurance(dto.getAssurance());
        return patient;
    }

    private static Ordonnance convertToOrdonnance(OrdonnanceServiceImpl.OrdonnanceDTOImpl dto) {
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setIdOrdo(dto.getIdOrdo());
        ordonnance.setIdDM(dto.getIdDM());
        ordonnance.setIdMedecin(dto.getIdMedecin());
        ordonnance.setDateOrdonnance(dto.getDateOrdonnance());
        return ordonnance;
    }
}
 */