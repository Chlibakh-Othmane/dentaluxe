package ma.dentaluxe.service.patient.api;

import ma.dentaluxe.entities.enums.Assurance;
import ma.dentaluxe.entities.enums.Sexe;
import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des patients.
 *
 * @author OTHMANE CHLIBAKH
 */
public interface PatientService {

    // ==================== CRUD PATIENTS ====================

    /**
     * Crée un nouveau patient
     */
    PatientDTO createPatient(PatientCreateDTO patientCreateDTO);

    /**
     * Récupère un patient par son ID
     */
    PatientDTO getPatientById(Long id);

    /**
     * Met à jour un patient existant
     */
    PatientDTO updatePatient(Long id, PatientUpdateDTO patientUpdateDTO);

    /**
     * Supprime un patient
     */
    void deletePatient(Long id);

    /**
     * Récupère tous les patients
     */
    List<PatientDTO> getAllPatients();

    // ==================== RECHERCHE ET FILTRES ====================

    /**
     * Recherche des patients par nom
     */
    List<PatientDTO> searchByNom(String nom);

    /**
     * Recherche des patients par prénom
     */
    List<PatientDTO> searchByPrenom(String prenom);

    /**
     * Recherche des patients par téléphone
     */
    List<PatientDTO> searchByTelephone(String telephone);

    /**
     * Recherche des patients par email
     */
    PatientDTO searchByEmail(String email);

    /**
     * Recherche des patients par assurance
     */
    List<PatientDTO> searchByAssurance(Assurance assurance);

    /**
     * Recherche des patients par sexe
     */
    List<PatientDTO> searchBySexe(Sexe sexe);

    /**
     * Recherche des patients par tranche d'âge
     */
    List<PatientDTO> searchByAgeRange(int minAge, int maxAge);

    /**
     * Recherche avancée sur plusieurs critères
     */
    List<PatientDTO> advancedSearch(PatientSearchCriteriaDTO criteria);

    // ==================== STATISTIQUES ET RAPPORTS ====================

    /**
     * Compte le nombre total de patients
     */
    long countPatients();

    /**
     * Récupère les statistiques démographiques
     */
    PatientStatisticsDTO getStatistics();

    /**
     * Génère un rapport patient détaillé
     */
    PatientReportDTO generateReport(Long id);

    /**
     * Exporte les patients vers CSV
     */
    String exportToCSV(String filePath) throws java.io.IOException;

    // ==================== VALIDATION ET UTILITAIRES ====================

    /**
     * Valide les données d'un patient
     */
    boolean validatePatient(PatientCreateDTO patientDTO);

    /**
     * Calcule l'âge d'un patient
     */
    Integer calculateAge(LocalDate dateNaissance);

    /**
     * Vérifie si un email existe déjà
     */
    boolean emailExists(String email);

    // ==================== INTERFACES DES DTOs ====================

    /**
     * DTO pour la création d'un patient
     */
    interface PatientCreateDTO {
        String getNom();
        void setNom(String nom);

        String getPrenom();
        void setPrenom(String prenom);

        String getAdresse();
        void setAdresse(String adresse);

        String getTelephone();
        void setTelephone(String telephone);

        String getEmail();
        void setEmail(String email);

        LocalDate getDateNaissance();
        void setDateNaissance(LocalDate dateNaissance);

        Sexe getSexe();
        void setSexe(Sexe sexe);

        Assurance getAssurance();
        void setAssurance(Assurance assurance);
    }

    /**
     * DTO pour la mise à jour d'un patient
     */
    interface PatientUpdateDTO {
        String getNom();
        void setNom(String nom);

        String getPrenom();
        void setPrenom(String prenom);

        String getAdresse();
        void setAdresse(String adresse);

        String getTelephone();
        void setTelephone(String telephone);

        String getEmail();
        void setEmail(String email);

        LocalDate getDateNaissance();
        void setDateNaissance(LocalDate dateNaissance);

        Sexe getSexe();
        void setSexe(Sexe sexe);

        Assurance getAssurance();
        void setAssurance(Assurance assurance);
    }

    /**
     * DTO complet d'un patient
     */
    interface PatientDTO {
        Long getId();
        void setId(Long id);

        String getNom();
        void setNom(String nom);

        String getPrenom();
        void setPrenom(String prenom);

        String getAdresse();
        void setAdresse(String adresse);

        String getTelephone();
        void setTelephone(String telephone);

        String getEmail();
        void setEmail(String email);

        LocalDate getDateNaissance();
        void setDateNaissance(LocalDate dateNaissance);

        java.time.LocalDateTime getDateCreation();
        void setDateCreation(java.time.LocalDateTime dateCreation);

        Sexe getSexe();
        void setSexe(Sexe sexe);

        Assurance getAssurance();
        void setAssurance(Assurance assurance);

        Integer getAge();
        void setAge(Integer age);

        String getFullName();
        String getFormattedInfo();
    }

    /**
     * DTO pour les critères de recherche avancée
     */
    interface PatientSearchCriteriaDTO {
        String getNom();
        void setNom(String nom);

        String getPrenom();
        void setPrenom(String prenom);

        String getTelephone();
        void setTelephone(String telephone);

        String getEmail();
        void setEmail(String email);

        Sexe getSexe();
        void setSexe(Sexe sexe);

        Assurance getAssurance();
        void setAssurance(Assurance assurance);

        Integer getMinAge();
        void setMinAge(Integer minAge);

        Integer getMaxAge();
        void setMaxAge(Integer maxAge);

        String getAdresse();
        void setAdresse(String adresse);
    }

    /**
     * DTO pour les statistiques patients
     */
    interface PatientStatisticsDTO {
        long getTotalPatients();
        void setTotalPatients(long totalPatients);

        long getPatientsHommes();
        void setPatientsHommes(long patientsHommes);

        long getPatientsFemmes();
        void setPatientsFemmes(long patientsFemmes);

        double getAgeMoyen();
        void setAgeMoyen(double ageMoyen);

        java.util.Map<Assurance, Long> getRepartitionAssurance();
        void setRepartitionAssurance(java.util.Map<Assurance, Long> repartitionAssurance);

        long getNouveauxPatientsCeMois();
        void setNouveauxPatientsCeMois(long nouveauxPatientsCeMois);

        java.util.Map<Integer, Long> getRepartitionParAge();
        void setRepartitionParAge(java.util.Map<Integer, Long> repartitionParAge);
    }

    /**
     * DTO pour le rapport patient
     */
    interface PatientReportDTO {
        Long getId();
        void setId(Long id);

        String getNomComplet();
        void setNomComplet(String nomComplet);

        String getInformationsPersonnelles();
        void setInformationsPersonnelles(String informationsPersonnelles);

        String getInformationsMedicales();
        void setInformationsMedicales(String informationsMedicales);

        String getHistoriqueConsultations();
        void setHistoriqueConsultations(String historiqueConsultations);

        String getAssurancesDetails();
        void setAssurancesDetails(String assurancesDetails);

        java.time.LocalDate getDateGeneration();
        void setDateGeneration(java.time.LocalDate dateGeneration);
    }

    // ==================== EXCEPTIONS ====================

    class PatientNotFoundException extends RuntimeException {
        public PatientNotFoundException(String message) {
            super(message);
        }
    }

    class PatientValidationException extends RuntimeException {
        public PatientValidationException(String message) {
            super(message);
        }
    }

    class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String message) {
            super(message);
        }
    }
}