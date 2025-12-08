// AUTEUR : AYA BAKIR

package ma.dentaluxe.service.DossierMedical.api;

import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.consultation.InterventionMedecin;
import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.entities.enums.StatutConsultation;

import java.time.LocalDate;
import java.util.List;

public interface DossierMedicalService {

    // ========== Gestion du Dossier ==========
    void createDossierForPatient(Long idPatient);
    DossierMedical getDossierById(Long id);
    DossierMedical getDossierByPatientId(Long idPatient);
    boolean hasDossier(Long idPatient);
    void deleteDossier(Long idDM);

    // ========== Gestion des Antécédents ==========
    void addAntecedent(Antecedents antecedent);
    List<Antecedents> getAntecedentsByDossier(Long idDM);
    void removeAntecedent(Long idAntecedent);

    // ========== Gestion des Consultations ==========
    void planifierConsultation(Consultation consultation);
    void terminerConsultation(Long idConsultation, String observation);
    void annulerConsultation(Long idConsultation);
    Consultation getConsultationById(Long id);
    List<Consultation> getConsultationsByDossier(Long idDM);
    List<Consultation> getConsultationsByDate(LocalDate date);
    List<Consultation> getConsultationsDuJour();

    // ========== Gestion des Interventions ==========
    void addInterventionToConsultation(InterventionMedecin intervention);
    List<InterventionMedecin> getInterventionsByConsultation(Long idConsultation);
    double getTotalPrixInterventionsByConsultation(Long idConsultation);
    void removeIntervention(Long idIM);
}