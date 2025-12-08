// AUTEUR : AYA BAKIR
package ma.dentaluxe.service.DossierMedical.baseImplementation;

import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.consultation.InterventionMedecin;
import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.entities.enums.StatutConsultation;
import ma.dentaluxe.repository.modules.dossierMedical.api.AntecedentsRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.ConsultationRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.DossierMedicalRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.InterventionMedecinRepository;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.AntecedentsRepositoryImpl;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.ConsultationRepositoryImpl;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.DossierMedicalRepositoryImpl;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.InterventionMedecinRepositoryImpl;
import ma.dentaluxe.service.DossierMedical.api.DossierMedicalService;

import java.time.LocalDate;
import java.util.List;

public class DossierMedicalServiceImpl implements DossierMedicalService {

    private final DossierMedicalRepository dossierRepository;
    private final AntecedentsRepository antecedentsRepository;
    private final ConsultationRepository consultationRepository;
    private final InterventionMedecinRepository interventionRepository;

    public DossierMedicalServiceImpl() {
        this.dossierRepository = new DossierMedicalRepositoryImpl();
        this.antecedentsRepository = new AntecedentsRepositoryImpl();
        this.consultationRepository = new ConsultationRepositoryImpl();
        this.interventionRepository = new InterventionMedecinRepositoryImpl();
    }

    // ========== Gestion du Dossier ==========

    @Override
    public void createDossierForPatient(Long idPatient) {
        if (idPatient == null) {
            throw new IllegalArgumentException("L'ID patient est requis");
        }
        if (hasDossier(idPatient)) {
            throw new IllegalStateException("Ce patient possède déjà un dossier médical");
        }

        DossierMedical dm = DossierMedical.builder()
                .idPatient(idPatient)
                .dateDeCreation(LocalDate.now())
                .build();

        dossierRepository.create(dm);
        System.out.println(" Dossier médical créé pour le patient ID: " + idPatient);
    }

    @Override
    public DossierMedical getDossierById(Long id) {
        DossierMedical dm = dossierRepository.findById(id);
        if (dm == null) {
            throw new IllegalArgumentException("Dossier introuvable (ID: " + id + ")");
        }
        return dm;
    }

    @Override
    public DossierMedical getDossierByPatientId(Long idPatient) {
        return dossierRepository.findByPatientId(idPatient);
    }

    @Override
    public boolean hasDossier(Long idPatient) {
        return getDossierByPatientId(idPatient) != null;
    }

    @Override
    public void deleteDossier(Long idDM) {
        if (getDossierById(idDM) != null) {
            dossierRepository.deleteById(idDM);
            System.out.println(" Dossier supprimé (ID: " + idDM + ")");
        }
    }

    // ========== Gestion des Antécédents ==========

    @Override
    public void addAntecedent(Antecedents antecedent) {
        if (antecedent == null || antecedent.getIdDM() == null) {
            throw new IllegalArgumentException("Antécédent invalide ou Dossier manquant");
        }
        // Vérifier si le dossier existe
        getDossierById(antecedent.getIdDM());

        antecedentsRepository.create(antecedent);
        System.out.println(" Antécédent ajouté : " + antecedent.getNom());
    }

    @Override
    public List<Antecedents> getAntecedentsByDossier(Long idDM) {
        return antecedentsRepository.findByDossierMedicalId(idDM);
    }

    @Override
    public void removeAntecedent(Long idAntecedent) {
        antecedentsRepository.deleteById(idAntecedent);
        System.out.println(" Antécédent supprimé (ID: " + idAntecedent + ")");
    }

    // ========== Gestion des Consultations ==========

    @Override
    public void planifierConsultation(Consultation consultation) {
        if (consultation == null || consultation.getIdDM() == null) {
            throw new IllegalArgumentException("Données de consultation incomplètes");
        }
        if (consultation.getDateConsultation().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("On ne peut pas planifier une consultation dans le passé");
        }

        consultation.setStatut(StatutConsultation.PLANIFIEE); // Valeur par défaut
        consultationRepository.create(consultation);
        System.out.println(" Consultation planifiée le : " + consultation.getDateConsultation());
    }

    @Override
    public void terminerConsultation(Long idConsultation, String observation) {
        Consultation c = getConsultationById(idConsultation);
        c.setStatut(StatutConsultation.TERMINEE);
        c.setObservation(observation);
        consultationRepository.update(c);
        System.out.println(" Consultation terminée (ID: " + idConsultation + ")");
    }

    @Override
    public void annulerConsultation(Long idConsultation) {
        Consultation c = getConsultationById(idConsultation);
        c.setStatut(StatutConsultation.ANNULEE);
        consultationRepository.update(c);
        System.out.println(" Consultation annulée (ID: " + idConsultation + ")");
    }

    @Override
    public Consultation getConsultationById(Long id) {
        Consultation c = consultationRepository.findById(id);
        if (c == null) throw new IllegalArgumentException("Consultation introuvable (ID: " + id + ")");
        return c;
    }

    @Override
    public List<Consultation> getConsultationsByDossier(Long idDM) {
        return consultationRepository.findByDossierMedicalId(idDM);
    }

    @Override
    public List<Consultation> getConsultationsByDate(LocalDate date) {
        return consultationRepository.findByDate(date);
    }

    @Override
    public List<Consultation> getConsultationsDuJour() {
        return getConsultationsByDate(LocalDate.now());
    }

    // ========== Gestion des Interventions ==========

    @Override
    public void addInterventionToConsultation(InterventionMedecin intervention) {
        if (intervention == null || intervention.getIdConsultation() == null) {
            throw new IllegalArgumentException("Intervention invalide");
        }
        // Vérifier si la consultation existe
        getConsultationById(intervention.getIdConsultation());

        if (intervention.getPrixIntervention() == null || intervention.getPrixIntervention() < 0) {
            throw new IllegalArgumentException("Le prix de l'intervention est invalide");
        }

        interventionRepository.create(intervention);
        System.out.println(" Intervention ajoutée sur la dent N°" + intervention.getNumDent());
    }

    @Override
    public List<InterventionMedecin> getInterventionsByConsultation(Long idConsultation) {
        return interventionRepository.findByIdConsultation(idConsultation);
    }

    @Override
    public double getTotalPrixInterventionsByConsultation(Long idConsultation) {
        return getInterventionsByConsultation(idConsultation).stream()
                .mapToDouble(InterventionMedecin::getPrixIntervention)
                .sum();
    }

    @Override
    public void removeIntervention(Long idIM) {
        interventionRepository.deleteById(idIM);
    }
}