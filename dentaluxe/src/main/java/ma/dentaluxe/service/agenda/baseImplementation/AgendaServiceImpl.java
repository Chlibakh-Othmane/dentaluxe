// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.agenda.baseImplementation;
import ma.dentaluxe.mvc.dto.RDVDTO;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.service.agenda.api.AgendaService;

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
            throw new IllegalArgumentException("RDV invalide : données manquantes");
        }

        // Vérifier disponibilité du créneau
        if (hasConflict(rdvDTO.getIdMedecin(), rdvDTO.getDateRDV(), rdvDTO.getHeureRDV())) {
            throw new IllegalStateException("Créneau déjà occupé pour ce médecin");
        }

        // Par défaut, le statut est PLANIFIE
        if (rdvDTO.getStatut() == null) {
            rdvDTO.setStatut(StatutRDV.PLANIFIE);
        }

        // Convertir DTO -> Entity
        RDV rdv = convertToEntity(rdvDTO);

        // Créer le RDV
        rdvRepository.create(rdv);

        System.out.println("✅ RDV créé avec succès (ID: " + rdv.getIdRDV() + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO getRDVById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        RDV rdv = rdvRepository.findById(id);
        if (rdv == null) {
            throw new IllegalStateException("RDV introuvable (ID: " + id + ")");
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
            throw new IllegalArgumentException("RDV ou ID invalide");
        }

        // Vérifier que le RDV existe
        RDV existing = rdvRepository.findById(rdvDTO.getIdRDV());
        if (existing == null) {
            throw new IllegalStateException("RDV introuvable (ID: " + rdvDTO.getIdRDV() + ")");
        }

        // Validation
        if (!validateRDV(rdvDTO)) {
            throw new IllegalArgumentException("RDV invalide : données manquantes");
        }

        // Convertir et mettre à jour
        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println("✅ RDV mis à jour (ID: " + rdv.getIdRDV() + ")");

        return convertToDTO(rdv);
    }

    @Override
    public void deleteRDV(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        if (!rdvExists(id)) {
            throw new IllegalStateException("RDV introuvable (ID: " + id + ")");
        }

        rdvRepository.deleteById(id);
        System.out.println("🗑️ RDV supprimé (ID: " + id + ")");
    }

    @Override
    public void cancelRDV(Long id) {
        annulerRDV(id);
    }

    // ========== Recherche et filtres ==========

    @Override
    public List<RDVDTO> getRDVByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }

        return rdvRepository.findByDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByMedecin(Long idMedecin) {
        if (idMedecin == null) {
            throw new IllegalArgumentException("L'ID médecin ne peut pas être null");
        }

        return rdvRepository.findByMedecinId(idMedecin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByPatient(Long idDM) {
        if (idDM == null) {
            throw new IllegalArgumentException("L'ID dossier médical ne peut pas être null");
        }

        return rdvRepository.findByPatientDossierId(idDM).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByStatut(StatutRDV statut) {
        if (statut == null) {
            throw new IllegalArgumentException("Le statut ne peut pas être null");
        }

        return rdvRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByDateRange(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être null");
        }
        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }

        return rdvRepository.findByDateRange(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVByMedecinAndDate(Long idMedecin, LocalDate date) {
        if (idMedecin == null || date == null) {
            throw new IllegalArgumentException("L'ID médecin et la date ne peuvent pas être null");
        }

        return rdvRepository.findByDate(date).stream()
                .filter(rdv -> rdv.getIdMedecin().equals(idMedecin))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

    @Override
    public int countRDVByPatient(Long idDM) {
        return getRDVByPatient(idDM).size();
    }

    @Override
    public List<MedecinStatDTO> getStatistiquesByMedecin() {
        Map<Long, List<RDVDTO>> rdvsByMedecin = getAllRDV().stream()
                .collect(Collectors.groupingBy(RDVDTO::getIdMedecin));

        List<MedecinStatDTO> stats = new ArrayList<>();

        for (Map.Entry<Long, List<RDVDTO>> entry : rdvsByMedecin.entrySet()) {
            Long idMedecin = entry.getKey();
            List<RDVDTO> rdvs = entry.getValue();

            int total = rdvs.size();
            int confirmes = (int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.CONFIRME).count();
            int enAttente = (int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.PLANIFIE).count();
            int annules = (int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.ANNULE).count();
            int termines = (int) rdvs.stream().filter(r -> r.getStatut() == StatutRDV.TERMINE).count();
            double tauxConfirmation = total > 0 ? (confirmes * 100.0 / total) : 0.0;

            MedecinStatDTO stat = new MedecinStatDTO();
            stat.setIdMedecin(idMedecin);
            stat.setNomComplet("Médecin " + idMedecin); // À enrichir avec les vraies données
            stat.setTotalRDV(total);
            stat.setRdvConfirmes(confirmes);
            stat.setRdvEnAttente(enAttente);
            stat.setRdvAnnules(annules);
            stat.setRdvTermines(termines);
            stat.setTauxConfirmation(tauxConfirmation);

            stats.add(stat);
        }

        return stats;
    }

    @Override
    public List<StatutStatDTO> getStatistiquesByStatut() {
        List<RDVDTO> allRdv = getAllRDV();
        int total = allRdv.size();

        Map<StatutRDV, Long> countByStatut = allRdv.stream()
                .collect(Collectors.groupingBy(RDVDTO::getStatut, Collectors.counting()));

        List<StatutStatDTO> stats = new ArrayList<>();

        for (Map.Entry<StatutRDV, Long> entry : countByStatut.entrySet()) {
            StatutStatDTO stat = new StatutStatDTO();
            stat.setStatut(entry.getKey());
            stat.setNombre(entry.getValue().intValue());
            stat.setPourcentage(total > 0 ? (entry.getValue() * 100.0 / total) : 0.0);
            stats.add(stat);
        }

        return stats;
    }

    // ========== Gestion du statut ==========

    @Override
    public RDVDTO confirmerRDV(Long idRDV) {
        RDVDTO rdvDTO = getRDVById(idRDV);

        if (rdvDTO.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Impossible de confirmer un RDV annulé");
        }

        rdvDTO.setStatut(StatutRDV.CONFIRME);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println("✅ RDV confirmé (ID: " + idRDV + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO annulerRDV(Long idRDV) {
        RDVDTO rdvDTO = getRDVById(idRDV);

        if (rdvDTO.getStatut() == StatutRDV.TERMINE) {
            throw new IllegalStateException("Impossible d'annuler un RDV terminé");
        }

        rdvDTO.setStatut(StatutRDV.ANNULE);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println("❌ RDV annulé (ID: " + idRDV + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO terminerRDV(Long idRDV) {
        RDVDTO rdvDTO = getRDVById(idRDV);

        if (rdvDTO.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Impossible de terminer un RDV annulé");
        }

        rdvDTO.setStatut(StatutRDV.TERMINE);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println("✅ RDV terminé (ID: " + idRDV + ")");

        return convertToDTO(rdv);
    }

    @Override
    public RDVDTO reporterRDV(Long idRDV, LocalDate nouvelleDate, LocalTime nouvelleHeure) {
        RDVDTO rdvDTO = getRDVById(idRDV);

        if (rdvDTO.getStatut() == StatutRDV.TERMINE || rdvDTO.getStatut() == StatutRDV.ANNULE) {
            throw new IllegalStateException("Impossible de reporter un RDV terminé ou annulé");
        }

        // Vérifier disponibilité du nouveau créneau
        if (hasConflict(rdvDTO.getIdMedecin(), nouvelleDate, nouvelleHeure)) {
            throw new IllegalStateException("Le nouveau créneau est déjà occupé");
        }

        rdvDTO.setDateRDV(nouvelleDate);
        rdvDTO.setHeureRDV(nouvelleHeure);

        RDV rdv = convertToEntity(rdvDTO);
        rdvRepository.update(rdv);

        System.out.println("📅 RDV reporté au " + nouvelleDate + " à " + nouvelleHeure);

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
            // Vérifier si le créneau est dans une plage de 30 minutes
            long minutesDiff = Math.abs(ChronoUnit.MINUTES.between(rdv.getHeureRDV(), heure));
            if (minutesDiff < 30) {
                return true; // Conflit détecté
            }
        }

        return false; // Pas de conflit
    }

    @Override
    public boolean validateRDV(RDVDTO rdvDTO) {
        if (rdvDTO == null) return false;
        if (rdvDTO.getIdDM() == null) return false;
        if (rdvDTO.getIdMedecin() == null) return false;
        if (rdvDTO.getDateRDV() == null) return false;
        if (rdvDTO.getHeureRDV() == null) return false;

        return true;
    }

    @Override
    public boolean rdvExists(Long id) {
        if (id == null) return false;
        try {
            return rdvRepository.findById(id) != null;
        } catch (Exception e) {
            return false;
        }
    }

    // ========== Recherches avancées ==========

    @Override
    public List<RDVDTO> getRDVDuJour() {
        return getRDVByDate(LocalDate.now());
    }

    @Override
    public List<RDVDTO> getRDVDeLaSemaine() {
        LocalDate debut = LocalDate.now();
        LocalDate fin = debut.plusDays(7);
        return getRDVByDateRange(debut, fin);
    }

    @Override
    public List<RDVDTO> getRDVDuMois() {
        LocalDate debut = LocalDate.now().withDayOfMonth(1);
        LocalDate fin = debut.plusMonths(1).minusDays(1);
        return getRDVByDateRange(debut, fin);
    }

    @Override
    public List<RDVDTO> getRDVAVenir(Long idMedecin) {
        LocalDate today = LocalDate.now();

        return getRDVByMedecin(idMedecin).stream()
                .filter(rdv -> rdv.getDateRDV().isAfter(today) ||
                        rdv.getDateRDV().equals(today))
                .filter(rdv -> rdv.getStatut() != StatutRDV.TERMINE &&
                        rdv.getStatut() != StatutRDV.ANNULE)
                .sorted(Comparator.comparing(RDVDTO::getDateRDV)
                        .thenComparing(RDVDTO::getHeureRDV))
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVPasses(Long idMedecin) {
        LocalDate today = LocalDate.now();

        return getRDVByMedecin(idMedecin).stream()
                .filter(rdv -> rdv.getDateRDV().isBefore(today))
                .sorted(Comparator.comparing(RDVDTO::getDateRDV)
                        .thenComparing(RDVDTO::getHeureRDV).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<RDVDTO> getRDVEnAttente() {
        return getRDVByStatut(StatutRDV.PLANIFIE);
    }

    @Override
    public List<RDVDTO> getRDVAnnules() {
        return getRDVByStatut(StatutRDV.ANNULE);
    }

    @Override
    public List<LocalTime> getCreneauxDisponibles(Long idMedecin, LocalDate date) {
        List<LocalTime> creneaux = new ArrayList<>();

        // Horaires de travail : 9h à 18h, créneaux de 30 minutes
        LocalTime debut = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(18, 0);

        LocalTime current = debut;
        while (current.isBefore(fin)) {
            if (isCreneauDisponible(idMedecin, date, current)) {
                creneaux.add(current);
            }
            current = current.plusMinutes(30);
        }

        return creneaux;
    }

    @Override
    public RDVDTO getProchainRDV(Long idMedecin) {
        List<RDVDTO> rdvsAVenir = getRDVAVenir(idMedecin);

        if (rdvsAVenir.isEmpty()) {
            return null;
        }

        return rdvsAVenir.get(0); // Déjà trié par date/heure dans getRDVAVenir
    }

    // ========== Méthodes de conversion DTO <-> Entity ==========

    private RDVDTO convertToDTO(RDV rdv) {
        if (rdv == null) {
            return null;
        }

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

        // Enrichir le DTO avec des informations calculées
        LocalDate today = LocalDate.now();
        dto.setIsPasse(rdv.getDateRDV().isBefore(today));
        dto.setIsAujourdhui(rdv.getDateRDV().equals(today));
        dto.setIsAVenir(rdv.getDateRDV().isAfter(today));
        dto.setStatutLibelle(rdv.getStatut() != null ? rdv.getStatut().name() : null);

        // Durée estimée par défaut : 30 minutes
        dto.setDureeEstimee(30);
        dto.setHeureFinEstimee(rdv.getHeureRDV().plusMinutes(30));

        return dto;
    }

    private RDV convertToEntity(RDVDTO dto) {
        if (dto == null) {
            return null;
        }

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
}