package ma.dentaluxe.mvc.controllers.modules.Agenda.api;

import ma.dentaluxe.service.agenda.dto.RDVDTO;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.service.agenda.api.AgendaService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
public interface AgendaController {
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

    /**
     * Récupère les rendez-vous par statut
     * @param statut Le statut recherché
     * @return Liste des rendez-vous avec ce statut
     */
    List<RDVDTO> getRDVByStatut(StatutRDV statut);

    List<RDVDTO> getRDVByDateRange(LocalDate debut, LocalDate fin);

    List<RDVDTO> getRDVByMedecinAndDate(Long idMedecin, LocalDate date);

    // ========== Statistiques ==========


    int countRDVToday();

    int countRDVByDate(LocalDate date);

    int countRDVByStatut(StatutRDV statut);

    int countRDVByMedecin(Long idMedecin);

    int countRDVByPatient(Long idDM);

    List<AgendaService.MedecinStatDTO> getStatistiquesByMedecin();

    List<AgendaService.StatutStatDTO> getStatistiquesByStatut();

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
}

