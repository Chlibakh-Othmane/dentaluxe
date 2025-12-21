package ma.dentaluxe.mvc.controllers.modules.Agenda.batch_implementation;

import ma.dentaluxe.mvc.controllers.modules.Agenda.api.AgendaController;
import ma.dentaluxe.service.agenda.api.AgendaService;
import ma.dentaluxe.service.agenda.dto.RDVDTO;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.service.agenda.exception.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendaControllerImpl implements AgendaController {

    private final AgendaService agendaService;

    public AgendaControllerImpl(AgendaService agendaService) {
        if (agendaService == null) {
            throw new IllegalArgumentException("Le service de l'agenda ne peut pas être null");
        }
        this.agendaService = agendaService;
    }

    // ========== CRUD de base ==========

    @Override
    public RDVDTO createRDV(RDVDTO rdvDTO) {
        try {
            System.out.println(" [CONTROLLER] Création d'un nouveau rendez-vous...");
            RDVDTO created = agendaService.createRDV(rdvDTO);
            System.out.println(" [CONTROLLER] RDV créé avec succès (ID: " + created.getIdRDV() + ")");
            System.out.println("   Date: " + created.getDateRDV() + " à " + created.getHeureRDV());
            return created;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (RDVConflictException e) {
            System.err.println(" [CONTROLLER] Conflit de rendez-vous : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la création : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la création du rendez-vous", e);
        }
    }

    @Override
    public RDVDTO getRDVById(Long id) {
        try {
            System.out.println(" [CONTROLLER] Recherche du RDV ID: " + id);
            RDVDTO rdv = agendaService.getRDVById(id);
            System.out.println(" [CONTROLLER] RDV trouvé - Date: " + rdv.getDateRDV() +
                    " | Statut: " + rdv.getStatut());
            return rdv;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] ID invalide : " + e.getMessage());
            throw e;
        } catch (RDVNotFoundException e) {
            System.err.println(" [CONTROLLER] RDV non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération du rendez-vous", e);
        }
    }

    @Override
    public List<RDVDTO> getAllRDV() {
        try {
            System.out.println(" [CONTROLLER] Récupération de tous les rendez-vous...");
            List<RDVDTO> rdvs = agendaService.getAllRDV();
            System.out.println(" [CONTROLLER] " + rdvs.size() + " rendez-vous récupéré(s)");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la récupération : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des rendez-vous", e);
        }
    }

    @Override
    public RDVDTO updateRDV(RDVDTO rdvDTO) {
        try {
            System.out.println(" [CONTROLLER] Mise à jour du RDV ID: " + rdvDTO.getIdRDV());
            RDVDTO updated = agendaService.updateRDV(rdvDTO);
            System.out.println(" [CONTROLLER] RDV mis à jour avec succès");
            return updated;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (RDVNotFoundException e) {
            System.err.println(" [CONTROLLER] RDV non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la mise à jour : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la mise à jour du rendez-vous", e);
        }
    }

    @Override
    public void deleteRDV(Long id) {
        try {
            System.out.println(" [CONTROLLER] Suppression du RDV ID: " + id);
            agendaService.deleteRDV(id);
            System.out.println(" [CONTROLLER] RDV supprimé avec succès");
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] ID invalide : " + e.getMessage());
            throw e;
        } catch (RDVNotFoundException e) {
            System.err.println(" [CONTROLLER] RDV non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la suppression : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression du rendez-vous", e);
        }
    }

    @Override
    public void cancelRDV(Long id) {
        try {
            System.out.println(" [CONTROLLER] Annulation du RDV ID: " + id);
            agendaService.cancelRDV(id);
            System.out.println(" [CONTROLLER] RDV annulé avec succès");
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de l'annulation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'annulation du rendez-vous", e);
        }
    }

    // ========== Recherche et filtres ==========

    @Override
    public List<RDVDTO> getRDVByDate(LocalDate date) {
        try {
            System.out.println(" [CONTROLLER] Recherche des RDV du " + date);
            List<RDVDTO> rdvs = agendaService.getRDVByDate(date);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV trouvé(s)");
            return rdvs;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] Date invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par date", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVByMedecin(Long idMedecin) {
        try {
            System.out.println(" [CONTROLLER] Recherche des RDV du médecin ID: " + idMedecin);
            List<RDVDTO> rdvs = agendaService.getRDVByMedecin(idMedecin);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV trouvé(s)");
            return rdvs;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] ID médecin invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par médecin", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVByPatient(Long idDM) {
        try {
            System.out.println(" [CONTROLLER] Recherche des RDV du patient (Dossier ID: " + idDM + ")");
            List<RDVDTO> rdvs = agendaService.getRDVByPatient(idDM);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV trouvé(s)");
            return rdvs;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] ID dossier invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par patient", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVByStatut(StatutRDV statut) {
        try {
            System.out.println(" [CONTROLLER] Recherche des RDV avec statut : " + statut);
            List<RDVDTO> rdvs = agendaService.getRDVByStatut(statut);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV trouvé(s)");
            return rdvs;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] Statut invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par statut", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVByDateRange(LocalDate debut, LocalDate fin) {
        try {
            System.out.println(" [CONTROLLER] Recherche des RDV entre " + debut + " et " + fin);
            List<RDVDTO> rdvs = agendaService.getRDVByDateRange(debut, fin);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV trouvé(s)");
            return rdvs;
        } catch (InvalidAgendaDataException e) {
            System.err.println(" [CONTROLLER] Période invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par période", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVByMedecinAndDate(Long idMedecin, LocalDate date) {
        try {
            System.out.println(" [CONTROLLER] Recherche des RDV du médecin " + idMedecin +
                    " le " + date);
            List<RDVDTO> rdvs = agendaService.getRDVByMedecinAndDate(idMedecin, date);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV trouvé(s)");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche", e);
        }
    }

    // ========== Statistiques ==========

    @Override
    public int countRDVToday() {
        try {
            System.out.println(" [CONTROLLER] Comptage des RDV d'aujourd'hui...");
            int count = agendaService.countRDVToday();
            System.out.println(" [CONTROLLER] Nombre de RDV aujourd'hui : " + count);
            return count;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage", e);
        }
    }

    @Override
    public int countRDVByDate(LocalDate date) {
        try {
            System.out.println(" [CONTROLLER] Comptage des RDV du " + date);
            int count = agendaService.countRDVByDate(date);
            System.out.println(" [CONTROLLER] Nombre de RDV : " + count);
            return count;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage", e);
        }
    }

    @Override
    public int countRDVByStatut(StatutRDV statut) {
        try {
            System.out.println(" [CONTROLLER] Comptage des RDV avec statut : " + statut);
            int count = agendaService.countRDVByStatut(statut);
            System.out.println(" [CONTROLLER] Nombre de RDV : " + count);
            return count;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage", e);
        }
    }

    @Override
    public int countRDVByMedecin(Long idMedecin) {
        try {
            System.out.println(" [CONTROLLER] Comptage des RDV du médecin ID: " + idMedecin);
            int count = agendaService.countRDVByMedecin(idMedecin);
            System.out.println(" [CONTROLLER] Nombre de RDV : " + count);
            return count;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage", e);
        }
    }

    @Override
    public int countRDVByPatient(Long idDM) {
        try {
            System.out.println(" [CONTROLLER] Comptage des RDV du patient (Dossier ID: " + idDM + ")");
            int count = agendaService.countRDVByPatient(idDM);
            System.out.println(" [CONTROLLER] Nombre de RDV : " + count);
            return count;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage", e);
        }
    }

    @Override
    public List<AgendaService.MedecinStatDTO> getStatistiquesByMedecin() {
        try {
            System.out.println("[CONTROLLER] Génération des statistiques par médecin...");
            List<AgendaService.MedecinStatDTO> stats = agendaService.getStatistiquesByMedecin();
            System.out.println("[CONTROLLER] Statistiques générées pour " + stats.size() + " médecin(s)");
            return stats;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la génération : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la génération des statistiques", e);
        }
    }

    @Override
    public List<AgendaService.StatutStatDTO> getStatistiquesByStatut() {
        try {
            System.out.println(" [CONTROLLER] Génération des statistiques par statut...");
            List<AgendaService.StatutStatDTO> stats = agendaService.getStatistiquesByStatut();
            System.out.println("[CONTROLLER] Statistiques générées pour " + stats.size() + " statut(s)");
            return stats;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la génération : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la génération des statistiques", e);
        }
    }

    // ========== Gestion du statut ==========

    @Override
    public RDVDTO confirmerRDV(Long idRDV) {
        try {
            System.out.println("[CONTROLLER] Confirmation du RDV ID: " + idRDV);
            RDVDTO confirmed = agendaService.confirmerRDV(idRDV);
            System.out.println(" [CONTROLLER] RDV confirmé avec succès");
            return confirmed;
        } catch (RDVNotFoundException e) {
            System.err.println(" [CONTROLLER] RDV non trouvé : " + e.getMessage());
            throw e;
        } catch (RDVStatusException e) {
            System.err.println(" [CONTROLLER] Changement de statut impossible : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la confirmation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la confirmation du rendez-vous", e);
        }
    }

    @Override
    public RDVDTO annulerRDV(Long idRDV) {
        try {
            System.out.println(" [CONTROLLER] Annulation du RDV ID: " + idRDV);
            RDVDTO annule = agendaService.annulerRDV(idRDV);
            System.out.println(" [CONTROLLER] RDV annulé avec succès");
            return annule;
        } catch (RDVNotFoundException e) {
            System.err.println(" [CONTROLLER] RDV non trouvé : " + e.getMessage());
            throw e;
        } catch (RDVStatusException e) {
            System.err.println(" [CONTROLLER] Changement de statut impossible : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Err lors de l'annulation : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'annulation du rendez-vous", e);
        }
    }

    @Override
    public RDVDTO terminerRDV(Long idRDV) {
        try {
            System.out.println(" [CONTROLLER] Marquage du RDV ID: " + idRDV + " comme terminé");
            RDVDTO termine = agendaService.terminerRDV(idRDV);
            System.out.println(" [CONTROLLER] RDV marqué comme terminé");
            return termine;
        } catch (RDVNotFoundException e) {
            System.err.println(" [CONTROLLER] RDV non trouvé : " + e.getMessage());
            throw e;
        } catch (RDVStatusException e) {
            System.err.println(" [CONTROLLER] Changement de statut impossible : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la finalisation du rendez-vous", e);
        }
    }

    @Override
    public RDVDTO reporterRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure) {
        try {
            System.out.println(" [CONTROLLER] Report du RDV ID: " + idRDV);
            System.out.println("    Nouvelle date: " + nouvelleDate + " à " + nouvelleHeure);
            RDVDTO reporte = agendaService.reporterRDV(idRDV, nouvelleDate, nouvelleHeure);
            System.out.println(" [CONTROLLER] RDV reporté avec succès");
            return reporte;
        } catch (RDVNotFoundException e) {
            System.err.println(" [CONTROLLER] RDV non trouvé : " + e.getMessage());
            throw e;
        } catch (RDVStatusException e) {
            System.err.println(" [CONTROLLER] Report impossible : " + e.getMessage());
            throw e;
        } catch (RDVConflictException e) {
            System.err.println(" [CONTROLLER] Conflit de rendez-vous : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors du report : " + e.getMessage());
            throw new RuntimeException("Erreur lors du report du rendez-vous", e);
        }
    }

// ========== Validation métier ==========

    @Override
    public boolean isCreneauDisponible(Long idMedecin, LocalDate date, LocalTime heure) {
        try {
            boolean disponible = agendaService.isCreneauDisponible(idMedecin, date, heure);
            if (disponible) {
                System.out.println(" [CONTROLLER] Créneau disponible : " + date + " à " + heure);
            } else {
                System.out.println("️ [CONTROLLER] Créneau occupé : " + date + " à " + heure);
            }
            return disponible;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la vérification : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hasConflict(Long idMedecin, LocalDate date, LocalTime heure) {
        try {
            boolean conflit = agendaService.hasConflict(idMedecin, date, heure);
            if (conflit) {
                System.out.println(" [CONTROLLER] Conflit détecté pour le " + date + " à " + heure);
            } else {
                System.out.println(" [CONTROLLER] Pas de conflit");
            }
            return conflit;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la vérification : " + e.getMessage());
            return true; // En cas d'erreur, on considère qu'il y a conflit par sécurité
        }
    }

    @Override
    public boolean validateRDV(RDVDTO rdvDTO) {
        try {
            boolean valid = agendaService.validateRDV(rdvDTO);
            if (valid) {
                System.out.println(" [CONTROLLER] RDV valide");
            } else {
                System.out.println(" [CONTROLLER] RDV invalide");
            }
            return valid;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la validation : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rdvExists(Long id) {
        try {
            boolean exists = agendaService.rdvExists(id);
            if (exists) {
                System.out.println(" [CONTROLLER] Le RDV existe (ID: " + id + ")");
            } else {
                System.out.println(" [CONTROLLER] Le RDV n'existe pas (ID: " + id + ")");
            }
            return exists;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la vérification : " + e.getMessage());
            return false;
        }
    }

// ========== Recherches avancées ==========

    @Override
    public List<RDVDTO> getRDVDuJour() {
        try {
            System.out.println(" [CONTROLLER] Récupération des RDV d'aujourd'hui...");
            List<RDVDTO> rdvs = agendaService.getRDVDuJour();
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV aujourd'hui");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des RDV du jour", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVDeLaSemaine() {
        try {
            System.out.println(" [CONTROLLER] Récupération des RDV de la semaine...");
            List<RDVDTO> rdvs = agendaService.getRDVDeLaSemaine();
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV cette semaine");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des RDV de la semaine", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVDuMois() {
        try {
            System.out.println(" [CONTROLLER] Récupération des RDV du mois...");
            List<RDVDTO> rdvs = agendaService.getRDVDuMois();
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV ce mois-ci");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des RDV du mois", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVAVenir(Long idMedecin) {
        try {
            System.out.println(" [CONTROLLER] Récupération des RDV à venir du médecin ID: " + idMedecin);
            List<RDVDTO> rdvs = agendaService.getRDVAVenir(idMedecin);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV à venir");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des RDV à venir", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVPasses(Long idMedecin) {
        try {
            System.out.println(" [CONTROLLER] Récupération des RDV passés du médecin ID: " + idMedecin);
            List<RDVDTO> rdvs = agendaService.getRDVPasses(idMedecin);
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV passés");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des RDV passés", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVEnAttente() {
        try {
            System.out.println(" [CONTROLLER] Récupération des RDV en attente...");
            List<RDVDTO> rdvs = agendaService.getRDVEnAttente();
            System.out.println(" [CONTROLLER] " + rdvs.size() + " RDV en attente");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des RDV en attente", e);
        }
    }

    @Override
    public List<RDVDTO> getRDVAnnules() {
        try {
            System.out.println(" [CONTROLLER] Récupération des RDV annulés...");
            List<RDVDTO> rdvs = agendaService.getRDVAnnules();
            System.out.println("[CONTROLLER] " + rdvs.size() + " RDV annulés");
            return rdvs;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des RDV annulés", e);
        }
    }

    @Override
    public List<LocalTime> getCreneauxDisponibles(Long idMedecin, LocalDate date) {
        try {
            System.out.println(" [CONTROLLER] Recherche des créneaux disponibles pour le médecin " +
                    idMedecin + " le " + date);
            List<LocalTime> creneaux = agendaService.getCreneauxDisponibles(idMedecin, date);
            System.out.println(" [CONTROLLER] " + creneaux.size() + " créneau(x) disponible(s)");
            return creneaux;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des créneaux", e);
        }
    }

    @Override
    public RDVDTO getProchainRDV(Long idMedecin) {
        try {
            System.out.println("[CONTROLLER] Recherche du prochain RDV du médecin ID: " + idMedecin);
            RDVDTO prochain = agendaService.getProchainRDV(idMedecin);
            if (prochain != null) {
                System.out.println(" [CONTROLLER] Prochain RDV : " + prochain.getDateRDV() +
                        " à " + prochain.getHeureRDV());
            } else {
                System.out.println(" [CONTROLLER] Aucun RDV à venir");
            }
            return prochain;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche du prochain RDV", e);
        }
    }
}