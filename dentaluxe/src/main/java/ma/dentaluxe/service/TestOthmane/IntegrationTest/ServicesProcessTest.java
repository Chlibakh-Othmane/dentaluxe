package ma.dentaluxe.service.TestOthmane.IntegrationTest;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.enums.*;
import ma.dentaluxe.mvc.dto.patient.*;
import ma.dentaluxe.mvc.dto.antecedent.*;
import ma.dentaluxe.mvc.dto.ordonnance.*;
import ma.dentaluxe.mvc.dto.medicament.*;
import ma.dentaluxe.mvc.dto.dossier.DossierMedicalDTO;
import ma.dentaluxe.service.patient.api.*;
import ma.dentaluxe.service.ordonnance.api.*;
import ma.dentaluxe.service.medicament.api.*;
import ma.dentaluxe.service.dossierMedical.api.DossierMedicalService;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class ServicesProcessTest {

    public static void main(String[] args) {
        System.out.println("========================================================================================================================");
        System.out.println("                                 DENTALUXE : VÉRIFICATION DES LIAISONS DU PROCESSUS                                     ");
        System.out.println("========================================================================================================================\n");

        try {
            // --- 1. CHARGEMENT DES SERVICES ---
            PatientService patientService = (PatientService) ApplicationContext.getBean("patientService");
            DossierMedicalService dossierService = (DossierMedicalService) ApplicationContext.getBean("dossierMedicalService");
            AntecedentService antCatalogueService = (AntecedentService) ApplicationContext.getBean("antecedentService");
            AntecedentPatientService antPatientService = (AntecedentPatientService) ApplicationContext.getBean("antecedentPatientService");
            MedicamentService medicamentService = (MedicamentService) ApplicationContext.getBean("medicamentService");
            OrdonnanceService ordonnanceService = (OrdonnanceService) ApplicationContext.getBean("ordonnanceService");

            // --- 2. ÉTAPE 0.5 : CRÉATION DYNAMIQUE DU MÉDECIN (INFAILLIBLE) ---
            System.out.println("[RUN] Création d'un médecin de test...");
            Long idMedecinTest = null;

            try (Connection conn = Db.getConnection()) {
                conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");

                // On crée un utilisateur unique
                String emailMed = "doc." + System.currentTimeMillis() + "@test.ma";
                PreparedStatement psUser = conn.prepareStatement(
                        "INSERT INTO utilisateur (nom, prenom, email, login, password_hash, sexe) VALUES ('Dr', 'Test', ?, ?, 'hash', 'HOMME')",
                        Statement.RETURN_GENERATED_KEYS);
                psUser.setString(1, emailMed);
                psUser.setString(2, "login." + System.currentTimeMillis());
                psUser.executeUpdate();

                ResultSet rs = psUser.getGeneratedKeys();
                if (rs.next()) {
                    idMedecinTest = rs.getLong(1);
                    // On l'ajoute en tant que Staff
                    PreparedStatement psStaff = conn.prepareStatement("INSERT INTO staff (id, salaire, date_recrutement) VALUES (?, 20000, CURDATE())");
                    psStaff.setLong(1, idMedecinTest);
                    psStaff.executeUpdate();

                    // On l'ajoute en tant que Médecin
                    PreparedStatement psMed = conn.prepareStatement("INSERT INTO medecin (id, specialite) VALUES (?, 'Chirurgien')");
                    psMed.setLong(1, idMedecinTest);
                    psMed.executeUpdate();
                }
                conn.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
            }

            if (idMedecinTest == null) throw new RuntimeException("Impossible de créer le médecin de test.");
            System.out.println(" => OK : Médecin créé avec l'ID réel : " + idMedecinTest);

            // --- 3. ETAPE 0 : PRÉPARATION DES CATALOGUES ---
            System.out.println("[RUN] Préparation des catalogues...");
            AntecedentDTO catAnt = antCatalogueService.create(AntecedentDTO.builder()
                    .nom("Hypertension").categorie(CategorieAntecedent.MALADIE).niveauRisque(NiveauRisque.MODERE).build());

            MedicamentDTO catMed = medicamentService.create(MedicamentDTO.builder()
                    .nom("Amlodipine").type("Cœur").forme("Gélule").prixUnitaire(60.0).remboursable(true).build());

            // --- 4. ETAPE 1 : CRÉATION DU PATIENT ET DOSSIER ---
            System.out.println("[RUN] Création du patient et de son dossier...");
            PatientDTO patient = patientService.createPatient(PatientCreateDTO.builder()
                    .nom("Berrada").prenom("Amine").email("a."+System.currentTimeMillis()+"@test.com")
                    .telephone("0611223344").dateNaissance(LocalDate.of(1985, 5, 10))
                    .sexe(Sexe.HOMME).assurance(Assurance.CNOPS).build());

            DossierMedicalDTO dossier = dossierService.createDossier(patient.getId());

            // Liaison Antécédent
            antPatientService.addAntecedentToPatient(AntecedentPatientCreateDTO.builder()
                    .idPatient(patient.getId()).idAntecedent(catAnt.getId()).notes("Suivi cardio").actif(true).build());

            // --- 5. ETAPE 3 : CRÉATION ORDONNANCE (AVEC LE BON MÉDECIN) ---
            System.out.println("[RUN] Création de l'ordonnance...");
            PrescriptionCreateDTO p1 = PrescriptionCreateDTO.builder()
                    .idMedicament(catMed.getIdMedicament()).quantite(1).frequence("1 soir").dureeEnJours(30).build();

            OrdonnanceDTO ordo = ordonnanceService.createOrdonnance(
                    OrdonnanceCreateDTO.builder()
                            .idDM(dossier.getIdDM())
                            .idMedecin(idMedecinTest) // <--- ON UTILISE L'ID RÉEL RÉCUPÉRÉ
                            .dateOrdonnance(LocalDate.now())
                            .build(),
                    List.of(p1));

            // --- 6. AFFICHAGE DU TABLEAU ---
            System.out.println("\n>>> VUE SYNTHÉTIQUE DU PROCESSUS RÉUSSI :");
            printProcessTable(patient, antPatientService.getPatientMedicalHistory(patient.getId()), ordo);

            // --- 7. ETAPE 4 : MISE À JOUR ---
            System.out.println("\n[RUN] Mise à jour des données...");
            patientService.updatePatient(patient.getId(), PatientUpdateDTO.builder().nom("BERRADA (MAJ)").prenom("Amine").telephone("0600000000").build());

            ordonnanceService.updatePrescription(PrescriptionUpdateDTO.builder()
                    .idPrescription(ordo.getPrescriptions().get(0).getIdPrescription())
                    .quantite(2).frequence("2 soirs").dureeEnJours(60).build());

            // Rechargement et affichage final
            PatientDTO updatedP = patientService.getPatientById(patient.getId());
            OrdonnanceDTO updatedO = ordonnanceService.getOrdonnanceById(ordo.getIdOrdo());
            printProcessTable(updatedP, antPatientService.getPatientMedicalHistory(patient.getId()), updatedO);

            // --- 8. NETTOYAGE ---
            ordonnanceService.deleteOrdonnance(ordo.getIdOrdo());
            patientService.deletePatient(patient.getId());
            System.out.println("\n>>> BASE NETTOYÉE. TEST RÉUSSI !");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printProcessTable(PatientDTO p, List<AntecedentPatientDTO> ants, OrdonnanceDTO ordo) {
        String format = "| %-18s | %-15s | %-18s | %-15s | %-10s |%n";
        String separator = "+--------------------+-----------------+--------------------+-----------------+------------+";
        System.out.println(separator);
        System.out.printf(format, "PATIENT", "ANTECEDENT", "MEDICAMENT", "POSOLOGIE", "DUREE");
        System.out.println(separator);
        String pName = p.getNom() + " " + p.getPrenom();
        String aName = (ants.isEmpty()) ? "Aucun" : ants.get(0).getNomAntecedent();
        if (ordo != null && ordo.getPrescriptions() != null) {
            for (PrescriptionDTO pr : ordo.getPrescriptions()) {
                System.out.printf(format, pName, aName, pr.getNomMedicament(), pr.getFrequence(), pr.getDureeEnJours() + " j");
                pName = ""; aName = "";
            }
        }
        System.out.println(separator);
    }
}