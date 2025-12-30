package ma.dentaluxe.service.ordonnance.api;

import ma.dentaluxe.mvc.dto.ordonnance.*; // On importe les vrais DTO séparés
import java.time.LocalDate;
import java.util.List;

/**
 * Interface épurée du service Ordonnance.
 */
public interface OrdonnanceService {

    // === GESTION DES ORDONNANCES ===
    OrdonnanceDTO createOrdonnance(OrdonnanceCreateDTO ordonnanceCreateDTO, List<PrescriptionCreateDTO> prescriptions);

    OrdonnanceDTO getOrdonnanceById(Long id);

    // C'est cette méthode qui posait problème !
    OrdonnanceDTO updateOrdonnance(Long id, OrdonnanceUpdateDTO ordonnanceUpdateDTO);

    void deleteOrdonnance(Long id);

    List<OrdonnanceDTO> getAllOrdonnances();
    List<OrdonnanceDTO> getOrdonnancesByDossierMedical(Long idDM);
    List<OrdonnanceDTO> getOrdonnancesByMedecin(Long idMedecin);
    List<OrdonnanceDTO> searchOrdonnancesByDateRange(LocalDate dateDebut, LocalDate dateFin);

    // === GESTION DES PRESCRIPTIONS ===
    PrescriptionDTO addPrescriptionToOrdonnance(Long idOrdo, PrescriptionCreateDTO prescriptionCreateDTO);
    PrescriptionDTO updatePrescription(Long idPrescription, Integer quantite, String frequence, Integer dureeEnJours);
    void deletePrescription(Long idPrescription);
    List<PrescriptionDTO> getPrescriptionsByOrdonnance(Long idOrdo);

    // === FONCTIONNALITÉS AVANCÉES ===
    String generateOrdonnancePDF(Long idOrdo) throws java.io.IOException;
    List<String> checkMedicationInteractions(Long idOrdo);
    OrdonnanceDTO duplicateOrdonnance(Long idOrdo, LocalDate nouvelleDate);
    OrdonnanceStatsDTO getOrdonnanceStatistics();

    void updatePrescription(PrescriptionUpdateDTO build);

    // NOTE : TOUTES LES INTERFACES INTERNES (OrdonnanceDTO, etc.)
    // ET LES EXCEPTIONS ONT ÉTÉ SUPPRIMÉES D'ICI.
}