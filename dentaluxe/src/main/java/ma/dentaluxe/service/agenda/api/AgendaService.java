// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.agenda.api;

import ma.dentaluxe.service.agenda.dto.RDVDTO;
import ma.dentaluxe.entities.enums.StatutRDV;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service pour la gestion de l'agenda et des rendez-vous
 * Toutes les méthodes travaillent avec RDVDTO
 */
public interface AgendaService {

    // ========== CRUD de base ==========

    RDVDTO createRDV(RDVDTO rdvDTO);

    RDVDTO getRDVById(Long id);

    List<RDVDTO> getAllRDV();

    RDVDTO updateRDV(RDVDTO rdvDTO);

    void deleteRDV(Long id);

    void cancelRDV(Long id);

    // ========== Recherche et filtres ==========

    List<RDVDTO> getRDVByDate(LocalDate date);

    List<RDVDTO> getRDVByMedecin(Long idMedecin);

    List<RDVDTO> getRDVByPatient(Long idDM);

    List<RDVDTO> getRDVByStatut(StatutRDV statut);

    List<RDVDTO> getRDVByDateRange(LocalDate debut, LocalDate fin);

    List<RDVDTO> getRDVByMedecinAndDate(Long idMedecin, LocalDate date);

    // ========== Statistiques ==========

    int countRDVToday();

    int countRDVByDate(LocalDate date);

    int countRDVByStatut(StatutRDV statut);

    int countRDVByMedecin(Long idMedecin);

    int countRDVByPatient(Long idDM);

    List<MedecinStatDTO> getStatistiquesByMedecin();

    List<StatutStatDTO> getStatistiquesByStatut();

    // ========== Gestion du statut ==========

    RDVDTO confirmerRDV(Long idRDV);

    RDVDTO annulerRDV(Long idRDV);

    RDVDTO terminerRDV(Long idRDV);

    RDVDTO reporterRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure);

    // ========== Validation métier ==========

    boolean isCreneauDisponible(Long idMedecin, LocalDate date, LocalTime heure);

    boolean hasConflict(Long idMedecin, LocalDate date, LocalTime heure);

    boolean validateRDV(RDVDTO rdvDTO);

    boolean rdvExists(Long id);

    // ========== Recherches avancées ==========

    List<RDVDTO> getRDVDuJour();

    List<RDVDTO> getRDVDeLaSemaine();

    List<RDVDTO> getRDVDuMois();

    List<RDVDTO> getRDVAVenir(Long idMedecin);

    List<RDVDTO> getRDVPasses(Long idMedecin);

    List<RDVDTO> getRDVEnAttente();

    List<RDVDTO> getRDVAnnules();

    List<LocalTime> getCreneauxDisponibles(Long idMedecin, LocalDate date);

    RDVDTO getProchainRDV(Long idMedecin);

    // ========== Classes internes pour les statistiques ==========

    /**
     * DTO pour les statistiques par médecin
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    class MedecinStatDTO {
        private Long idMedecin;
        private String nomComplet;
        private int totalRDV;
        private int rdvConfirmes;
        private int rdvEnAttente;
        private int rdvAnnules;
        private int rdvTermines;
        private double tauxConfirmation;
    }

    /**
     * DTO pour les statistiques par statut
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    class StatutStatDTO {
        private StatutRDV statut;
        private int nombre;
        private double pourcentage;
    }
}