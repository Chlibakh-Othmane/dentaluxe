package ma.dentaluxe.service.agenda.api;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.enums.StatutRDV;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// AUTEUR : AYA LEZREGUE

public interface AgendaService {

    // ========== CRUD de base ==========
    void createRDV(RDV rdv);
    RDV getRDVById(Long id);
    List<RDV> getAllRDV();
    void updateRDV(RDV rdv);
    void deleteRDV(Long id);
    void cancelRDV(Long id);

    // ========== Recherche et filtres ==========
    List<RDV> getRDVByDate(LocalDate date);
    List<RDV> getRDVByMedecin(Long idMedecin);
    List<RDV> getRDVByPatient(Long idDM);
    List<RDV> getRDVByStatut(StatutRDV statut);
    List<RDV> getRDVByDateRange(LocalDate debut, LocalDate fin);

    // ========== Statistiques ==========
    int countRDVToday();
    int countRDVByDate(LocalDate date);
    int countRDVByStatut(StatutRDV statut);
    int countRDVByMedecin(Long idMedecin);

    // ========== Gestion du statut ==========
    void confirmerRDV(Long idRDV);
    void annulerRDV(Long idRDV);
    void terminerRDV(Long idRDV);
    void reporterRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure);

    // ========== Validation métier ==========
    boolean isCreneauDisponible(Long idMedecin, LocalDate date, LocalTime heure);
    boolean hasConflict(Long idMedecin, LocalDate date, LocalTime heure);
    boolean validateRDV(RDV rdv);

    // ========== Recherches avancées ==========
    List<RDV> getRDVDuJour();
    List<RDV> getRDVDeLaSemaine();
    List<RDV> getRDVAVenir(Long idMedecin);
    List<RDV> getRDVEnAttente();
    List<RDV> getRDVAnnules();
}