// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.agenda.baseImplementation;

import ma.dentaluxe.service.agenda.dto.RDVDTO;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.service.agenda.api.AgendaService;
// Importation des nouvelles exceptions
import ma.dentaluxe.service.agenda.exception.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class AgendaServiceImpl implements AgendaService {

    private final RDVRepository rdvRepository;

    public AgendaServiceImpl(RDVRepository rdvRepository) {
        this.rdvRepository = rdvRepository;
    }

    // ========== CRUD de base ==========

    @Override
    public RDVDTO createRDV(RDVDTO rdvDTO) {
        // Validation
        if (!validateRDV(rdvDTO)) {
            throw new InvalidAgendaDataException("RDV invalide : données obligatoires manquantes (Patient, Médecin, Date ou Heure)");
        }

        // Vérifier disponibilité du créneau
        if (hasConflict(rdvDTO.getIdMedecin(), rdvDTO.getDateRDV(), rdvDTO.getHeureRDV())) {
            throw new RDVConflictException("Le médecin est déjà occupé le " + rdvDTO.getDateRDV() + " à " + rdvDTO.getHeureRDV());
        }

        // Par défaut, le statut est PLANIFIE
        if (rdvDTO.getStatut() == null) {
            rdvDTO.setStatut(StatutRDV.PLANIFIE);
        }

        // Convertir DTO -> Entity
        RDV rdv = convertToEntity(rdvDTO);

        // Créer le RDV
        rdvRepository.create(rdv);

        System.out.println(" RDV créé avec succès (ID: " + rdv.getIdRDV() + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO getRDVById(Long id) {
        if (id == null) {
            throw new InvalidAgendaDataException("L'ID du rendez-vous ne peut pas être null");
        }

        RDV rdv = rdvRepository.findById(id);
        if (rdv == null) {
            throw new RDVNotFoundException(id);
        }

        return convertToDTO(rdv);
    }

    @Override
    public List<RDVDTO> getAllRDV() {
        return rdvRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RDVDTO updateRDV(RDVDTO rdvDTO) {
        if (rdvDTO == null || rdvDTO.getIdRDV() == null) {
            throw new InvalidAgendaDataException("RDV ou ID invalide pour la mise à jour");
        }

        // Vérifier que le RDV existe
        if (!rdvExists(rdvDTO.getIdRDV())) {
            throw new RDVNotFoundException(rdvDTO.getIdRDV());
        }

        // Validation des données
        if (!validateRDV(rdvDTO)) {
            throw new InvalidAgendaDataException("Données de mise à jour du RDV incomplètes");
        }

        // Convertir et mettre à jour
        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println(" RDV mis à jour (ID: " + rdv.getIdRDV() + ")");

        return convertToDTO(rdv);
    }

    @Override
    public void deleteRDV(Long id) {
        if (id == null) {
            throw new InvalidAgendaDataException("L'ID ne peut pas être null");
        }

        if (!rdvExists(id)) {
            throw new RDVNotFoundException(id);
        }

        rdvRepository.deleteById(id);
        System.out.println("🗑 RDV supprimé (ID: " + id + ")");
    }

    @Override
    public void cancelRDV(Long id) {
        annulerRDV(id);
    }

    // ========== Recherche et filtres ==========

    @Override
    public List<RDVDTO> getRDVByDate(LocalDate date) {
        if (date == null) {
            throw new InvalidAgendaDataException("La date de recherche ne peut pas être null");
        }

        return rdvRepository.findByDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByMedecin(Long idMedecin) {
        if (idMedecin == null) {
            throw new InvalidAgendaDataException("L'ID médecin est requis pour le filtrage");
        }

        return rdvRepository.findByMedecinId(idMedecin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByPatient(Long idDM) {
        if (idDM == null) {
            throw new InvalidAgendaDataException("L'ID du dossier médical est requis pour le filtrage");
        }

        return rdvRepository.findByPatientDossierId(idDM).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByStatut(StatutRDV statut) {
        if (statut == null) {
            throw new InvalidAgendaDataException("Le statut est requis pour le filtrage");
        }

        return rdvRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByDateRange(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null) {
            throw new InvalidAgendaDataException("Les dates de début et de fin sont requises");
        }
        if (debut.isAfter(fin)) {
            throw new InvalidAgendaDataException("La date de début doit être antérieure à la date de fin");
        }

        return rdvRepository.findByDateRange(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ========== Gestion du statut ==========

    @Override
    public RDVDTO confirmerRDV(Long idRDV) {
        RDVDTO rdvDTO = getRDVById(idRDV); // Lancera RDVNotFoundException si inexistant

        if (rdvDTO.getStatut() == StatutRDV.ANNULE) {
            throw new RDVStatusException("Impossible de confirmer un rendez-vous déjà annulé.");
        }

        rdvDTO.setStatut(StatutRDV.CONFIRME);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println(" RDV confirmé (ID: " + idRDV + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO annulerRDV(Long idRDV) {
        RDVDTO rdvDTO = getRDVById(idRDV);

        if (rdvDTO.getStatut() == StatutRDV.TERMINE) {
            throw new RDVStatusException("Impossible d'annuler un rendez-vous déjà terminé.");
        }

        rdvDTO.setStatut(StatutRDV.ANNULE);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println(" RDV annulé (ID: " + idRDV + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO terminerRDV(Long idRDV) {
        RDVDTO rdvDTO = getRDVById(idRDV);

        if (rdvDTO.getStatut() == StatutRDV.ANNULE) {
            throw new RDVStatusException("Impossible de marquer comme terminé un rendez-vous annulé.");
        }

        rdvDTO.setStatut(StatutRDV.TERMINE);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println(" RDV terminé (ID: " + idRDV + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO reporterRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure) {
        RDVDTO rdvDTO = getRDVById(idRDV);

        if (rdvDTO.getStatut() == StatutRDV.TERMINE || rdvDTO.getStatut() == StatutRDV.ANNULE) {
            throw new RDVStatusException("Impossible de reporter un rendez-vous terminé ou annulé.");
        }

        // Vérifier disponibilité du nouveau créneau
        if (hasConflict(rdvDTO.getIdMedecin(), nouvelleDate, nouvelleHeure)) {
            throw new RDVConflictException("Le nouveau créneau (" + nouvelleDate + " à " + nouvelleHeure + ") est déjà occupé.");
        }

        rdvDTO.setDateRDV(nouvelleDate);
        rdvDTO.setHeureRDV(nouvelleHeure);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println(" RDV reporté au " + nouvelleDate + " à " + nouvelleHeure);

        return convertToDTO(rdv);
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
            // Seuil de 30 minutes entre rendez-vous
            long minutesDiff = Math.abs(ChronoUnit.MINUTES.between(rdv.getHeureRDV(), heure));
            if (minutesDiff < 30) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateRDV(RDVDTO rdvDTO) {
        if (rdvDTO == null) return false;
        return rdvDTO.getIdDM() != null &&
                rdvDTO.getIdMedecin() != null &&
                rdvDTO.getDateRDV() != null &&
                rdvDTO.getHeureRDV() != null;
    }

    @Override
    public boolean rdvExists(Long id) {
        if (id == null) return false;
        return rdvRepository.findById(id) != null;
    }

    // ========== Méthodes de conversion DTO <-> Entity ==========

    private RDVDTO convertToDTO(RDV rdv) {
        if (rdv == null) return null;

        RDVDTO dto = RDVDTO.builder()
                .idRDV(rdv.getIdRDV())
                .idDM(rdv.getIdDM())
                .idMedecin(rdv.getIdMedecin())
                .dateRDV(rdv.getDateRDV())
                .heureRDV(rdv.getHeureRDV())
                .statut(rdv.getStatut())
                .motif(rdv.getMotif())
                .notes(rdv.getNoteMedecin())
                .build();

        LocalDate today = LocalDate.now();
        dto.setIsPasse(rdv.getDateRDV().isBefore(today));
        dto.setIsAujourdhui(rdv.getDateRDV().equals(today));
        dto.setIsAVenir(rdv.getDateRDV().isAfter(today));
        dto.setStatutLibelle(rdv.getStatut() != null ? rdv.getStatut().name() : null);
        dto.setDureeEstimee(30);
        dto.setHeureFinEstimee(rdv.getHeureRDV().plusMinutes(30));

        return dto;
    }

    private RDV convertToEntity(RDVDTO dto) {
        if (dto == null) return null;
        return RDV.builder()
                .idRDV(dto.getIdRDV())
                .idDM(dto.getIdDM())
                .idMedecin(dto.getIdMedecin())
                .dateRDV(dto.getDateRDV())
                .heureRDV(dto.getHeureRDV())
                .statut(dto.getStatut())
                .motif(dto.getMotif())
                .noteMedecin(dto.getNotes())
                .build();
    }

    // Les méthodes statistiques (count, getStatistiques...) restent identiques
    // car elles utilisent déjà les méthodes de recherche sécurisées ci-dessus.
    @Override public int countRDVToday() { return getRDVByDate(LocalDate.now()).size(); }
    @Override public int countRDVByDate(LocalDate date) { return getRDVByDate(date).size(); }
    @Override public int countRDVByStatut(StatutRDV statut) { return getRDVByStatut(statut).size(); }
    @Override public int countRDVByMedecin(Long idMedecin) { return getRDVByMedecin(idMedecin).size(); }
    @Override public int countRDVByPatient(Long idDM) { return getRDVByPatient(idDM).size(); }
    @Override public List<RDVDTO> getRDVDuJour() { return getRDVByDate(LocalDate.now()); }

    @Override
    public List<RDVDTO> getRDVDeLaSemaine() {
        LocalDate debut = LocalDate.now();
        return getRDVByDateRange(debut, debut.plusDays(7));
    }

    @Override
    public List<RDVDTO> getRDVDuMois() {
        LocalDate debut = LocalDate.now().withDayOfMonth(1);
        return getRDVByDateRange(debut, debut.plusMonths(1).minusDays(1));
    }

    @Override
    public List<RDVDTO> getRDVAVenir(Long idMedecin) {
        LocalDate today = LocalDate.now();
        return getRDVByMedecin(idMedecin).stream()
                .filter(rdv -> !rdv.getDateRDV().isBefore(today))
                .filter(rdv -> rdv.getStatut() != StatutRDV.TERMINE && rdv.getStatut() != StatutRDV.ANNULE)
                .sorted(Comparator.comparing(RDVDTO::getDateRDV).thenComparing(RDVDTO::getHeureRDV))
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVPasses(Long idMedecin) {
        LocalDate today = LocalDate.now();
        return getRDVByMedecin(idMedecin).stream()
                .filter(rdv -> rdv.getDateRDV().isBefore(today))
                .sorted(Comparator.comparing(RDVDTO::getDateRDV).thenComparing(RDVDTO::getHeureRDV).reversed())
                .collect(Collectors.toList());
    }

    @Override public List<RDVDTO> getRDVEnAttente() { return getRDVByStatut(StatutRDV.PLANIFIE); }
    @Override public List<RDVDTO> getRDVAnnules() { return getRDVByStatut(StatutRDV.ANNULE); }
    @Override public List<RDVDTO> getRDVByMedecinAndDate(Long idMedecin, LocalDate date) {
        return getRDVByDate(date).stream().filter(r -> r.getIdMedecin().equals(idMedecin)).collect(Collectors.toList());
    }

    @Override
    public List<LocalTime> getCreneauxDisponibles(Long idMedecin, LocalDate date) {
        List<LocalTime> creneaux = new ArrayList<>();
        LocalTime current = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(18, 0);
        while (current.isBefore(fin)) {
            if (isCreneauDisponible(idMedecin, date, current)) { creneaux.add(current); }
            current = current.plusMinutes(30);
        }
        return creneaux;
    }

    @Override
    public RDVDTO getProchainRDV(Long idMedecin) {
        List<RDVDTO> aVenir = getRDVAVenir(idMedecin);
        return aVenir.isEmpty() ? null : aVenir.get(0);
    }

    @Override
    public List<MedecinStatDTO> getStatistiquesByMedecin() {
        Map<Long, List<RDVDTO>> rdvsByMedecin = getAllRDV().stream().collect(Collectors.groupingBy(RDVDTO::getIdMedecin));
        List<MedecinStatDTO> stats = new ArrayList<>();
        for (Map.Entry<Long, List<RDVDTO>> entry : rdvsByMedecin.entrySet()) {
            List<RDVDTO> rdvs = entry.getValue();
            int total = rdvs.size();
            MedecinStatDTO stat = new MedecinStatDTO();
            stat.setIdMedecin(entry.getKey());
            stat.setNomComplet("Médecin " + entry.getKey());
            stat.setTotalRDV(total);
            stat.setRdvConfirmes((int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.CONFIRME).count());
            stat.setRdvEnAttente((int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.PLANIFIE).count());
            stat.setRdvAnnules((int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.ANNULE).count());
            stat.setRdvTermines((int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.TERMINE).count());
            stat.setTauxConfirmation(total > 0 ? (stat.getRdvConfirmes() * 100.0 / total) : 0.0);
            stats.add(stat);
        }
        return stats;
    }

    @Override
    public List<StatutStatDTO> getStatistiquesByStatut() {
        List<RDVDTO> all = getAllRDV();
        int total = all.size();
        return all.stream()
                .collect(Collectors.groupingBy(RDVDTO::getStatut, Collectors.counting()))
                .entrySet().stream().map(e -> {
                    StatutStatDTO s = new StatutStatDTO();
                    s.setStatut(e.getKey());
                    s.setNombre(e.getValue().intValue());
                    s.setPourcentage(total > 0 ? (e.getValue() * 100.0 / total) : 0.0);
                    return s;
                }).collect(Collectors.toList());
    }
}