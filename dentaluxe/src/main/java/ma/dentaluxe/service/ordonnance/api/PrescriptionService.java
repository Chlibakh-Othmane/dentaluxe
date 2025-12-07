package ma.dentaluxe.service.ordonnance.api;

import java.util.List;

/**
 * Service dédié à la gestion spécifique des prescriptions médicamenteuses.
 * Une prescription représente un médicament prescrit dans une ordonnance.
 *
 * @author OTHMANE CHLIBAKH
 */
public interface PrescriptionService {

    // ==================== CRUD PRESCRIPTIONS ====================

    /**
     * Crée une nouvelle prescription
     */
    PrescriptionDTO createPrescription(PrescriptionCreateDTO prescriptionCreateDTO);

    /**
     * Récupère une prescription par son ID
     */
    PrescriptionDTO getPrescriptionById(Long id);

    /**
     * Met à jour une prescription existante
     */
    PrescriptionDTO updatePrescription(PrescriptionUpdateDTO prescriptionUpdateDTO);

    /**
     * Supprime une prescription
     */
    void deletePrescription(Long id);

    /**
     * Récupère toutes les prescriptions
     */
    List<PrescriptionDTO> getAllPrescriptions();

    // ==================== RECHERCHE ET FILTRES ====================

    /**
     * Récupère les prescriptions d'une ordonnance
     */
    List<PrescriptionDTO> getPrescriptionsByOrdonnance(Long idOrdo);

    /**
     * Récupère les prescriptions d'un médicament spécifique
     */
    List<PrescriptionDTO> getPrescriptionsByMedicament(Long idMedicament);

    /**
     * Recherche les prescriptions par fréquence d'administration
     */
    List<PrescriptionDTO> searchByFrequence(String frequence);

    /**
     * Recherche les prescriptions par durée de traitement
     */
    List<PrescriptionDTO> searchByDuree(int minJours, int maxJours);

    /**
     * Recherche les prescriptions avec une quantité supérieure à une valeur
     */
    List<PrescriptionDTO> searchByQuantiteSuperieure(int quantiteMin);

    // ==================== ANALYSE ET STATISTIQUES ====================

    /**
     * Calcule la quantité totale prescrite d'un médicament
     */
    int getQuantiteTotalePrescrite(Long idMedicament);

    /**
     * Vérifie si un médicament est déjà prescrit dans une ordonnance
     */
    boolean isMedicamentPrescritDansOrdonnance(Long idOrdo, Long idMedicament);

    /**
     * Récupère les médicaments les plus fréquemment prescrits
     */
    List<MedicamentPrescriptionDTO> getMedicamentsLesPlusPrescrits(int limit);

    /**
     * Génère un rapport détaillé des prescriptions
     */
    PrescriptionReportDTO generatePrescriptionReport(Long idOrdo);

    /**
     * Vérifie la validité d'une prescription selon les règles métier
     */
    ValidationResultDTO validatePrescription(PrescriptionCreateDTO prescriptionDTO);

    /**
     * Calcule la date de fin de traitement d'une prescription
     */
    java.time.LocalDate calculateDateFinTraitement(Long idPrescription);

    // ==================== INTERFACES DES DTOs ====================

    /**
     * Interface pour la création d'une prescription
     */
    interface PrescriptionCreateDTO {
        Long getIdOrdo();
        void setIdOrdo(Long idOrdo);

        Long getIdMedicament();
        void setIdMedicament(Long idMedicament);

        Integer getQuantite();
        void setQuantite(Integer quantite);

        String getFrequence();
        void setFrequence(String frequence);

        Integer getDureeEnJours();
        void setDureeEnJours(Integer dureeEnJours);

        void setPosologie(String posologie);

        String getInstructions();
        void setInstructions(String instructions);
    }

    /**
     * Interface pour la mise à jour d'une prescription
     */
    interface PrescriptionUpdateDTO {
        Long getIdPrescription();
        void setIdPrescription(Long idPrescription);

        Integer getQuantite();
        void setQuantite(Integer quantite);

        String getFrequence();
        void setFrequence(String frequence);

        Integer getDureeEnJours();
        void setDureeEnJours(Integer dureeEnJours);


        void setPosologie(String posologie);

        String getInstructions();
        void setInstructions(String instructions);
    }

    /**
     * Interface pour le DTO complet d'une prescription
     */
    interface PrescriptionDTO {
        Long getIdPrescription();
        void setIdPrescription(Long idPrescription);

        Long getIdOrdo();
        void setIdOrdo(Long idOrdo);

        Long getIdMedicament();
        void setIdMedicament(Long idMedicament);

        Integer getQuantite();
        void setQuantite(Integer quantite);

        String getFrequence();
        void setFrequence(String frequence);

        Integer getDureeEnJours();
        void setDureeEnJours(Integer dureeEnJours);


        void setPosologie(String posologie);

        String getInstructions();
        void setInstructions(String instructions);

        String getNomMedicament();
        void setNomMedicament(String nomMedicament);

        String getFormePharmaceutique();
        void setFormePharmaceutique(String formePharmaceutique);

        String getDosage();
        void setDosage(String dosage);

        java.time.LocalDate getDateOrdonnance();
        void setDateOrdonnance(java.time.LocalDate dateOrdonnance);

        String getNomMedecin();
        void setNomMedecin(String nomMedecin);

        String getNomPatient();
        void setNomPatient(String nomPatient);

        // Méthodes de calcul
        int getQuantiteTotale();
        String formatPosologieComplete();
        boolean isTraitementEnCours();
        int getJoursRestants();
    }

    /**
     * Interface pour le rapport des médicaments prescrits
     */
    interface MedicamentPrescriptionDTO {
        Long getIdMedicament();
        void setIdMedicament(Long idMedicament);

        String getNomMedicament();
        void setNomMedicament(String nomMedicament);

        int getNombrePrescriptions();
        void setNombrePrescriptions(int nombrePrescriptions);

        int getQuantiteTotalePrescrite();
        void setQuantiteTotalePrescrite(int quantiteTotalePrescrite);

        String getFrequenceMoyenne();
        void setFrequenceMoyenne(String frequenceMoyenne);
    }

    /**
     * Interface pour le rapport détaillé des prescriptions
     */
    interface PrescriptionReportDTO {
        Long getIdOrdo();
        void setIdOrdo(Long idOrdo);

        int getNombrePrescriptions();
        void setNombrePrescriptions(int nombrePrescriptions);

        int getDureeMoyenneTraitement();
        void setDureeMoyenneTraitement(int dureeMoyenneTraitement);

        java.util.List<String> getCategoriesMedicaments();
        void setCategoriesMedicaments(java.util.List<String> categoriesMedicaments);

        java.util.Map<String, Integer> getRepartitionParFrequence();
        void setRepartitionParFrequence(java.util.Map<String, Integer> repartitionParFrequence);

        java.util.List<PrescriptionDTO> getPrescriptions();
        void setPrescriptions(java.util.List<PrescriptionDTO> prescriptions);
    }

    /**
     * Interface pour le résultat de validation
     */
    interface ValidationResultDTO {
        boolean isValid();
        void setValid(boolean valid);

        java.util.List<String> getMessages();
        void setMessages(java.util.List<String> messages);

        java.util.List<String> getWarnings();
        void setWarnings(java.util.List<String> warnings);

        java.util.List<String> getErrors();
        void setErrors(java.util.List<String> errors);
    }

    // ==================== EXCEPTIONS ====================

    class PrescriptionNotFoundException extends RuntimeException {
        public PrescriptionNotFoundException(String message) {
            super(message);
        }
    }

    class PrescriptionValidationException extends RuntimeException {
        public PrescriptionValidationException(String message) {
            super(message);
        }
    }

    class MedicamentNotFoundException extends RuntimeException {
        public MedicamentNotFoundException(String message) {
            super(message);
        }
    }
}