package ma.dentaluxe.service.agenda.baseImplementation;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.repository.modules.agenda.inMemDB_implementation.RDVRepositoryImpl;
import ma.dentaluxe.service.agenda.api.AgendaService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


// AUTEUR : AYA LEZREGUE

public class AgendaServiceImpl implements AgendaService {

    private final RDVRepository rdvRepository;

    public AgendaServiceImpl() {
        this.rdvRepository = new RDVRepositoryImpl();
    }

    // ========== CRUD de base ==========

    @Override
    public void createRDV(RDV rdv) {
        // Validation
        if (!validateRDV(rdv)) {
            throw new IllegalArgumentException("RDV invalide : données manquantes");
        }

        // Vérifier disponibilité du créneau
        if (hasConflict(rdv.getIdMedecin(), rdv.getDateRDV(), rdv.getHeureRDV())) {
            throw new IllegalStateException("Créneau déjà occupé pour ce médecin");
        }

        // Créer le RDV
        rdvRepository.create(rdv);
        System.out.println(" RDV créé avec succès (ID: " + rdv.getIdRDV() + ")");
    }
    @Override
    public RDV getRDVById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        return rdvRepository.findById(id);
    }
    @Override
    public List<RDV> getAllRDV() {
        return rdvRepository.findAll();
    }
    @Override
    public void updateRDV(RDV rdv) {
        if (rdv == null || rdv.getIdRDV() == null) {
            throw new IllegalArgumentException("RDV ou ID invalide");
        }

        // Vérifier que le RDV existe
        RDV existing = rdvRepository.findById(rdv.getIdRDV());
        if (existing == null) {
            throw new IllegalStateException("RDV introuvable");
        }

        rdvRepository.update(rdv);
        System.out.println(" RDV mis à jour (ID: " + rdv.getIdRDV() + ")");
    }
    @Override
    public void deleteRDV(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        rdvRepository.deleteById(id);
        System.out.println(" RDV supprimé (ID: " + id + ")");
    }
    @Override
    public void cancelRDV(Long id) {
        annulerRDV(id);
    }
    // ========== Recherche et filtres ==========

    @Override
    public List<RDV> getRDVByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }
        return rdvRepository.findByDate(date);
    }
    @Override
    public List<RDV> getRDVByMedecin(Long idMedecin) {
        if (idMedecin == null) {
            throw new IllegalArgumentException("L'ID médecin ne peut pas être null");
        }
        return rdvRepository.findByMedecinId(idMedecin);
    }
    @Override
    public List<RDV> getRDVByPatient(Long idDM) {
        if (idDM == null) {
            throw new IllegalArgumentException("L'ID dossier médical ne peut pas être null");
        }
        return rdvRepository.findByPatientDossierId(idDM);
    }
    @Override
    public List<RDV> getRDVByStatut(StatutRDV statut) {
        if (statut == null) {
            throw new IllegalArgumentException("Le statut ne peut pas être null");
        }
        return rdvRepository.findByStatut(statut);
    }
    @Override
    public List<RDV> getRDVByDateRange(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être null");
        }
        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        return rdvRepository.findByDateRange(debut, fin);
    }
    // ========== Statistiques ==========

    @Override
    public int countRDVToday() {
        return getRDVByDate(LocalDate.now()).size();
    }
    @Override
    public int countRDVByDate(LocalDate date) {
        return getRDVByDate(date).size();
    }
    @Override
    public int countRDVByStatut(StatutRDV statut) {
        return getRDVByStatut(statut).size();
    }

    @Override
    public int countRDVByMedecin(Long idMedecin) {
        return getRDVByMedecin(idMedecin).size();
    }
    // ========== Gestion du statut ==========

    @Override
    public void confirmerRDV(Long idRDV) {
        RDV rdv = getRDVById(idRDV);
        if (rdv == null) {
            throw new IllegalStateException("RDV introuvable");
        }

        if (rdv.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Impossible de confirmer un RDV annulé");
        }

        rdv.setStatut(StatutRDV.CONFIRME);
        rdvRepository.update(rdv);
        System.out.println(" RDV confirmé (ID: " + idRDV + ")");
    }
    @Override
    public void annulerRDV(Long idRDV) {
        RDV rdv = getRDVById(idRDV);
        if (rdv == null) {
            throw new IllegalStateException("RDV introuvable");
        }

        if (rdv.getStatut() == StatutRDV.TERMINE) {
            throw new IllegalStateException("Impossible d'annuler un RDV terminé");
        }

        rdv.setStatut(StatutRDV.ANNULE);
        rdvRepository.update(rdv);
        System.out.println(" RDV annulé (ID: " + idRDV + ")");
    }
    @Override
    public void terminerRDV(Long idRDV) {
        RDV rdv = getRDVById(idRDV);
        if (rdv == null) {
            throw new IllegalStateException("RDV introuvable");
        }

        if (rdv.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Impossible de terminer un RDV annulé");
        }

        rdv.setStatut(StatutRDV.TERMINE);
        rdvRepository.update(rdv);
        System.out.println(" RDV terminé (ID: " + idRDV + ")");
    }
    @Override
    public void reporterRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure) {
        RDV rdv = getRDVById(idRDV);
        if (rdv == null) {
            throw new IllegalStateException("RDV introuvable");
        }

        if (rdv.getStatut() == StatutRDV.TERMINE || rdv.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Impossible de reporter un RDV terminé ou annulé");
        }

        // Vérifier disponibilité du nouveau créneau
        if (hasConflict(rdv.getIdMedecin(), nouvelleDate, nouvelleHeure)) {
            throw new IllegalStateException("Le nouveau créneau est déjà occupé");
        }

        rdv.setDateRDV(nouvelleDate);
        rdv.setHeureRDV(nouvelleHeure);
        rdvRepository.update(rdv);
        System.out.println(" RDV reporté au " + nouvelleDate + " à " + nouvelleHeure);
    }
    // ========== Validation métier ==========

    @Override
    public boolean isCreneauDisponible(Long idMedecin, LocalDate date, LocalTime heure) {
        return !hasConflict(idMedecin, date, heure);
    }
    @Override
    public boolean hasConflict(Long idMedecin, LocalDate date, LocalTime heure) {
        List<RDV> rdvsDuJour = rdvRepository.findByDate(date).stream()
                .filter(rdv -> rdv.getIdMedecin().equals(idMedecin))
                .filter(rdv -> rdv.getStatut() != StatutRDV.ANNULE)
                .collect(Collectors.toList());

        for (RDV rdv : rdvsDuJour) {
            // Vérifier si le créneau est dans une plage de 30 minutes
            long minutesDiff = Math.abs(ChronoUnit.MINUTES.between(rdv.getHeureRDV(), heure));
            if (minutesDiff < 30) {
                return true; // Conflit détecté
            }
        }

        return false; // Pas de conflit
    }
    @Override
    public boolean validateRDV(RDV rdv) {
        if (rdv == null) return false;
        if (rdv.getIdDM() == null) return false;
        if (rdv.getIdMedecin() == null) return false;
        if (rdv.getDateRDV() == null) return false;
        if (rdv.getHeureRDV() == null) return false;

        // Vérifier que la date n'est pas dans le passé
        //if (rdv.getDateRDV().isBefore(LocalDate.now())) {
           // return false;
       // }

        // Si c'est aujourd'hui, vérifier que l'heure n'est pas passée
       // if (rdv.getDateRDV().equals(LocalDate.now()) &&
               // rdv.getHeureRDV().isBefore(LocalTime.now())) {
          //  return false;
      //  }

        return true;
    }
    // ========== Recherches avancées ==========

    @Override
    public List<RDV> getRDVDuJour() {
        return getRDVByDate(LocalDate.now());
    }
    @Override
    public List<RDV> getRDVDeLaSemaine() {
        LocalDate debut = LocalDate.now();
        LocalDate fin = debut.plusDays(7);
        return getRDVByDateRange(debut, fin);
    }
    @Override
    public List<RDV> getRDVAVenir(Long idMedecin) {
        return getRDVByMedecin(idMedecin).stream()
                .filter(rdv -> rdv.getDateRDV().isAfter(LocalDate.now()) ||
                        rdv.getDateRDV().equals(LocalDate.now()))
                .filter(rdv -> rdv.getStatut() != StatutRDV.TERMINE &&
                        rdv.getStatut() != StatutRDV.ANNULE)
                .collect(Collectors.toList());
    }
    @Override
    public List<RDV> getRDVEnAttente() {
        return getRDVByStatut(StatutRDV.PLANIFIE);
    }

    @Override
    public List<RDV> getRDVAnnules() {
        return getRDVByStatut(StatutRDV.ANNULE);
    }
}


