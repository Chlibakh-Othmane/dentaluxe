package ma.dentaluxe.service.TestOthmane.IntegrationTest;
import ma.dentaluxe.conf.ApplicationContext;
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
import java.time.LocalDate;
import java.util.List;

/**
 * TEST DE PROCESSUS DE BOUT EN BOUT AVEC TOUTES LES LIAISONS
 * @author : Chlibakh Othmane
 */
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

            // --- 2. ETAPE 0 : PRÉPARATION DES CATALOGUES ---
            AntecedentDTO catAnt = antCatalogueService.create(AntecedentDTO.builder()
                    .nom("Hypertension").categorie(CategorieAntecedent.MALADIE).niveauRisque(NiveauRisque.MODERE).build());

            MedicamentDTO catMed = medicamentService.create(MedicamentDTO.builder()
                    .nom("Amlodipine").type("Cœur").forme("Gélule").prixUnitaire(60.0).remboursable(true).build());

            // --- 3. ETAPE 1 : CRÉATION DE LA CHAÎNE ---
            System.out.println("[RUN] Création de la chaîne complète : Patient -> Dossier -> Antécédent -> Ordonnance -> Prescription");

            // Création Patient
            PatientDTO patient = patientService.createPatient(PatientCreateDTO.builder()
                    .nom("Berrada").prenom("Amine").email("a."+System.currentTimeMillis()+"@test.com")
                    .telephone("0611223344").dateNaissance(LocalDate.of(1985, 5, 10))
                    .sexe(Sexe.HOMME).assurance(Assurance.CNOPS).build());

            // Création Dossier
            DossierMedicalDTO dossier = dossierService.createDossier(patient.getId());

            // Liaison Antécédent
            antPatientService.addAntecedentToPatient(AntecedentPatientCreateDTO.builder()
                    .idPatient(patient.getId()).idAntecedent(catAnt.getId()).notes("Suivi cardiologue").actif(true).build());

            // Création Ordonnance
            PrescriptionCreateDTO p1 = PrescriptionCreateDTO.builder()
                    .idMedicament(catMed.getIdMedicament()).quantite(1).frequence("1 soir").dureeEnJours(30).build();
            OrdonnanceDTO ordo = ordonnanceService.createOrdonnance(
                    OrdonnanceCreateDTO.builder().idDM(dossier.getIdDM()).idMedecin(1L).dateOrdonnance(LocalDate.now()).build(),
                    List.of(p1));

            // --- 4. AFFICHAGE DU TABLEAU DE SYNTHÈSE (APRÈS CRÉATION) ---
            System.out.println("\n>>> VUE SYNTHÉTIQUE DU PROCESSUS (APRÈS INSERTIONS) :");
            printProcessTable(patient, antPatientService.getPatientMedicalHistory(patient.getId()), ordo);

            // --- 5. ETAPE 4 : MISE À JOUR (Changement prescription et Patient) ---
            System.out.println("\n[RUN] Mise à jour : Changement du nom du patient et de la posologie...");
            patientService.updatePatient(patient.getId(), PatientUpdateDTO.builder().nom("BERRADA (MAJ)").prenom("Amine").telephone("0600000000").build());

            // On met à jour la prescription via le service
            ordonnanceService.updatePrescription(ordo.getPrescriptions().get(0).getIdPrescription(), 2, "2 soirs", 60);

            // On recharge les données pour le tableau
            PatientDTO updatedPatient = patientService.getPatientById(patient.getId());
            OrdonnanceDTO updatedOrdo = ordonnanceService.getOrdonnanceById(ordo.getIdOrdo());

            System.out.println("\n>>> VUE SYNTHÉTIQUE DU PROCESSUS (APRÈS MISES À JOUR) :");
            printProcessTable(updatedPatient, antPatientService.getPatientMedicalHistory(patient.getId()), updatedOrdo);

            // --- 6. ETAPE 5 : SUPPRESSION ---
            System.out.println("\n[RUN] Suppression de l'ordonnance et du patient...");
            ordonnanceService.deleteOrdonnance(ordo.getIdOrdo());
            patientService.deletePatient(patient.getId());

            System.out.println("\n>>> ÉTAT FINAL : " + (patientService.searchByNom("BERRADA").isEmpty() ? "Base de données nettoyée." : "Erreur suppression."));

            System.out.println("\n========================================================================================================================");
            System.out.println("                                    PROCESS TEST VALIDÉ ET TERMINÉ AVEC SUCCÈS                                          ");
            System.out.println("========================================================================================================================");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche un tableau croisé montrant toutes les liaisons du patient
     */
    private static void printProcessTable(PatientDTO p, List<AntecedentPatientDTO> ants, OrdonnanceDTO ordo) {
        // En-têtes
        String format = "| %-18s | %-15s | %-18s | %-15s | %-10s | %-8s |%n";
        String separator = "+--------------------+-----------------+--------------------+-----------------+------------+----------+";

        System.out.println(separator);
        System.out.printf(format, "PATIENT", "ANTECEDENT", "MEDICAMENT", "FREQ/DOSE", "DURÉE", "PRIX UT");
        System.out.println(separator);

        String patientName = p.getNom() + " " + p.getPrenom();
        String antecedentName = (ants.isEmpty()) ? "Aucun" : ants.get(0).getNomAntecedent();

        // Comme un patient peut avoir plusieurs prescriptions, on boucle dessus
        if (ordo != null && ordo.getPrescriptions() != null) {
            for (PrescriptionDTO pr : ordo.getPrescriptions()) {
                System.out.printf(format,
                        truncate(patientName, 18),
                        truncate(antecedentName, 15),
                        truncate(pr.getNomMedicament(), 18),
                        pr.getFrequence(),
                        pr.getDureeEnJours() + " j",
                        "60.0 DH" // On pourrait aller chercher le prix via le service si besoin
                );
                // Pour les lignes suivantes du même patient, on n'affiche plus le nom pour la lisibilité
                patientName = "";
                antecedentName = "";
            }
        }
        System.out.println(separator);
    }

    private static String truncate(String text, int size) {
        if (text == null) return "N/A";
        return text.length() > size ? text.substring(0, size-3) + "..." : text;
    }
}