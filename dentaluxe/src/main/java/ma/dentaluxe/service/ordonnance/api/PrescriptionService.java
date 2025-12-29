package ma.dentaluxe.service.ordonnance.api;

import ma.dentaluxe.mvc.dto.ordonnance.*; // Import de tous les DTO du package ordonnance
import java.time.LocalDate;
import java.util.List;

/**
 * Interface épurée du service de gestion des prescriptions.
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
     * RECTIFIÉ : Utilise maintenant le DTO dédié pour la mise à jour
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
     * Génère un rapport détaillé des prescriptions (PrescriptionReportDTO doit être dans mvc.dto)
     */
    PrescriptionReportDTO generatePrescriptionReport(Long idOrdo);

    /**
     * Vérifie la validité d'une prescription selon les règles métier
     */
    ValidationResultDTO validatePrescription(PrescriptionCreateDTO prescriptionDTO);

    /**
     * Calcule la date de fin de traitement d'une prescription
     */
    LocalDate calculateDateFinTraitement(Long idPrescription);
}