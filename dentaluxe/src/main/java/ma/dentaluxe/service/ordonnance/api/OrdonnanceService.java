package ma.dentaluxe.service.ordonnance.api;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface du service de gestion des ordonnances médicales.
 * Contient uniquement les signatures des méthodes (pas d'implémentation).
 *
 * @author OTHMANE CHLIBAKH
 */
public interface OrdonnanceService {

    // ==================== GESTION DES ORDONNANCES ====================

    /**
     * Crée une nouvelle ordonnance avec prescriptions associées
     */
    OrdonnanceDTO createOrdonnance(OrdonnanceCreateDTO ordonnanceCreateDTO,
                                   List<PrescriptionCreateDTO> prescriptions);

    /**
     * Récupère une ordonnance par son ID
     */
    OrdonnanceDTO getOrdonnanceById(Long id);

    /**
     * Met à jour une ordonnance existante
     */
    OrdonnanceDTO updateOrdonnance(Long id, OrdonnanceUpdateDTO ordonnanceUpdateDTO);

    /**
     * Supprime une ordonnance
     */
    void deleteOrdonnance(Long id);

    /**
     * Récupère toutes les ordonnances
     */
    List<OrdonnanceDTO> getAllOrdonnances();

    /**
     * Récupère les ordonnances d'un dossier médical
     */
    List<OrdonnanceDTO> getOrdonnancesByDossierMedical(Long idDM);

    /**
     * Récupère les ordonnances d'un médecin
     */
    List<OrdonnanceDTO> getOrdonnancesByMedecin(Long idMedecin);

    /**
     * Recherche des ordonnances par période
     */
    List<OrdonnanceDTO> searchOrdonnancesByDateRange(LocalDate dateDebut, LocalDate dateFin);

    // ==================== GESTION DES PRESCRIPTIONS ====================

    /**
     * Ajoute une prescription à une ordonnance
     */
    PrescriptionDTO addPrescriptionToOrdonnance(Long idOrdo, PrescriptionCreateDTO prescriptionCreateDTO);

    /**
     * Met à jour une prescription
     */
    PrescriptionDTO updatePrescription(Long idPrescription, Integer quantite,
                                       String frequence, Integer dureeEnJours);

    /**
     * Supprime une prescription
     */
    void deletePrescription(Long idPrescription);

    /**
     * Récupère les prescriptions d'une ordonnance
     */
    List<PrescriptionDTO> getPrescriptionsByOrdonnance(Long idOrdo);

    // ==================== FONCTIONNALITÉS AVANCÉES ====================

    /**
     * Génère un PDF d'une ordonnance
     */
    String generateOrdonnancePDF(Long idOrdo) throws java.io.IOException;

    /**
     * Vérifie les interactions médicamenteuses
     */
    List<String> checkMedicationInteractions(Long idOrdo);

    /**
     * Duplique une ordonnance
     */
    OrdonnanceDTO duplicateOrdonnance(Long idOrdo, LocalDate nouvelleDate);

    /**
     * Récupère les statistiques
     */
    OrdonnanceStatsDTO getOrdonnanceStatistics();

    // ==================== INTERFACES DES DTOs ====================
    // Seulement les déclarations d'interfaces, PAS d'implémentation

    /**
     * Interface pour le DTO de création d'ordonnance
     */
    interface OrdonnanceCreateDTO {
        Long getIdDM();
        void setIdDM(Long idDM);

        Long getIdMedecin();
        void setIdMedecin(Long idMedecin);

        LocalDate getDateOrdonnance();
        void setDateOrdonnance(LocalDate dateOrdonnance);

        String getRemarques();
        void setRemarques(String remarques);
    }

    /**
     * Interface pour le DTO de mise à jour d'ordonnance
     */
    interface OrdonnanceUpdateDTO {
        LocalDate getDateOrdonnance();
        void setDateOrdonnance(LocalDate dateOrdonnance);

        String getRemarques();
        void setRemarques(String remarques);
    }

    /**
     * Interface pour le DTO complet d'ordonnance
     */
    interface OrdonnanceDTO {
        Long getIdOrdo();
        void setIdOrdo(Long idOrdo);

        Long getIdDM();
        void setIdDM(Long idDM);

        Long getIdMedecin();
        void setIdMedecin(Long idMedecin);

        LocalDate getDateOrdonnance();
        void setDateOrdonnance(LocalDate dateOrdonnance);

        String getRemarques();
        void setRemarques(String remarques);

        List<PrescriptionDTO> getPrescriptions();
        void setPrescriptions(List<PrescriptionDTO> prescriptions);

        String getNomMedecin();
        void setNomMedecin(String nomMedecin);

        String getNomPatient();
        void setNomPatient(String nomPatient);

        int getNombreMedicaments();
        int getDureeTotaleTraitement();
    }

    /**
     * Interface pour le DTO de création de prescription
     */
    interface PrescriptionCreateDTO {
        Long getIdMedicament();
        void setIdMedicament(Long idMedicament);

        Integer getQuantite();
        void setQuantite(Integer quantite);

        String getFrequence();
        void setFrequence(String frequence);

        Integer getDureeEnJours();
        void setDureeEnJours(Integer dureeEnJours);

        String getPosologie();
        void setPosologie(String posologie);
    }

    /**
     * Interface pour le DTO complet de prescription
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

        String getPosologie();
        void setPosologie(String posologie);

        String getNomMedicament();
        void setNomMedicament(String nomMedicament);

        String getForme();
        void setForme(String forme);

        String getDosage();
        void setDosage(String dosage);

        String formatPrescription();
    }

    /**
     * Interface pour le DTO de statistiques
     */
    interface OrdonnanceStatsDTO {
        int getTotalOrdonnances();
        void setTotalOrdonnances(int totalOrdonnances);

        int getOrdonnancesCeMois();
        void setOrdonnancesCeMois(int ordonnancesCeMois);

        double getMoyenneMedicamentsParOrdonnance();
        void setMoyenneMedicamentsParOrdonnance(double moyenneMedicamentsParOrdonnance);

        String getMedicamentLePlusPrescrit();
        void setMedicamentLePlusPrescrit(String medicamentLePlusPrescrit);

        String getMedecinLePlusActif();
        void setMedecinLePlusActif(String medecinLePlusActif);

        java.util.Map<String, Integer> getRepartitionParMois();
        void setRepartitionParMois(java.util.Map<String, Integer> repartitionParMois);

        void ajouterRepartitionMois(String mois, int nombre);
    }

    // ==================== EXCEPTIONS ====================

    /**
     * Exception pour ordonnance non trouvée
     */
    class OrdonnanceNotFoundException extends RuntimeException {
        public OrdonnanceNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Exception pour prescription non trouvée
     */
    class PrescriptionNotFoundException extends RuntimeException {
        public PrescriptionNotFoundException(String message) {
            super(message);
        }
    }
}