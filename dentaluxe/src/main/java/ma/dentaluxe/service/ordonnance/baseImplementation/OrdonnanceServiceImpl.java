package ma.dentaluxe.service.ordonnance.baseImplementation;

import ma.dentaluxe.service.ordonnance.api.OrdonnanceService;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.*;

/**
 * Implémentation complète du service de gestion des ordonnances.
 * Contient toutes les classes internes et l'implémentation des méthodes.
 *
 * @author OTHMANE CHLIBAKH
 */
public class OrdonnanceServiceImpl implements OrdonnanceService {

    // ==================== DÉPENDANCES ====================

    private final OrdonnanceRepository ordonnanceRepository;
    private final PrescriptionRepository prescriptionRepository;

    // ==================== IMPLÉMENTATION DES DTOs ====================

    /**
     * Implémentation concrète de OrdonnanceCreateDTO
     */
    public static class OrdonnanceCreateDTOImpl implements OrdonnanceCreateDTO {
        private Long idDM;
        private Long idMedecin;
        private LocalDate dateOrdonnance;
        private String remarques;

        // Constructeurs
        public OrdonnanceCreateDTOImpl() {}

        public OrdonnanceCreateDTOImpl(Long idDM, Long idMedecin, LocalDate dateOrdonnance) {
            this.idDM = idDM;
            this.idMedecin = idMedecin;
            this.dateOrdonnance = dateOrdonnance;
        }

        // Implémentation des méthodes de l'interface
        @Override public Long getIdDM() { return idDM; }
        @Override public void setIdDM(Long idDM) { this.idDM = idDM; }

        @Override public Long getIdMedecin() { return idMedecin; }
        @Override public void setIdMedecin(Long idMedecin) { this.idMedecin = idMedecin; }

        @Override public LocalDate getDateOrdonnance() { return dateOrdonnance; }
        @Override public void setDateOrdonnance(LocalDate dateOrdonnance) { this.dateOrdonnance = dateOrdonnance; }

        @Override public String getRemarques() { return remarques; }
        @Override public void setRemarques(String remarques) { this.remarques = remarques; }
    }

    /**
     * Implémentation concrète de OrdonnanceUpdateDTO
     */
    public static class OrdonnanceUpdateDTOImpl implements OrdonnanceUpdateDTO {
        private LocalDate dateOrdonnance;
        private String remarques;

        // Constructeurs
        public OrdonnanceUpdateDTOImpl() {}

        public OrdonnanceUpdateDTOImpl(LocalDate dateOrdonnance, String remarques) {
            this.dateOrdonnance = dateOrdonnance;
            this.remarques = remarques;
        }

        // Implémentation des méthodes de l'interface
        @Override public LocalDate getDateOrdonnance() { return dateOrdonnance; }
        @Override public void setDateOrdonnance(LocalDate dateOrdonnance) { this.dateOrdonnance = dateOrdonnance; }

        @Override public String getRemarques() { return remarques; }
        @Override public void setRemarques(String remarques) { this.remarques = remarques; }
    }

    /**
     * Implémentation concrète de OrdonnanceDTO
     */
    public static class OrdonnanceDTOImpl implements OrdonnanceDTO {
        private Long idOrdo;
        private Long idDM;
        private Long idMedecin;
        private LocalDate dateOrdonnance;
        private String remarques;
        private List<PrescriptionDTO> prescriptions;
        private String nomMedecin;
        private String nomPatient;

        // Constructeurs
        public OrdonnanceDTOImpl() {
            this.prescriptions = new ArrayList<>();
        }

        public OrdonnanceDTOImpl(Long idOrdo, Long idDM, Long idMedecin, LocalDate dateOrdonnance) {
            this();
            this.idOrdo = idOrdo;
            this.idDM = idDM;
            this.idMedecin = idMedecin;
            this.dateOrdonnance = dateOrdonnance;
        }

        // Implémentation des méthodes de l'interface
        @Override public Long getIdOrdo() { return idOrdo; }
        @Override public void setIdOrdo(Long idOrdo) { this.idOrdo = idOrdo; }

        @Override public Long getIdDM() { return idDM; }
        @Override public void setIdDM(Long idDM) { this.idDM = idDM; }

        @Override public Long getIdMedecin() { return idMedecin; }
        @Override public void setIdMedecin(Long idMedecin) { this.idMedecin = idMedecin; }

        @Override public LocalDate getDateOrdonnance() { return dateOrdonnance; }
        @Override public void setDateOrdonnance(LocalDate dateOrdonnance) { this.dateOrdonnance = dateOrdonnance; }

        @Override public String getRemarques() { return remarques; }
        @Override public void setRemarques(String remarques) { this.remarques = remarques; }

        @Override public List<PrescriptionDTO> getPrescriptions() { return prescriptions; }
        @Override public void setPrescriptions(List<PrescriptionDTO> prescriptions) { this.prescriptions = prescriptions; }

        @Override public String getNomMedecin() { return nomMedecin; }
        @Override public void setNomMedecin(String nomMedecin) { this.nomMedecin = nomMedecin; }

        @Override public String getNomPatient() { return nomPatient; }
        @Override public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }

        // Implémentation des méthodes de calcul
        @Override
        public int getNombreMedicaments() {
            return prescriptions != null ? prescriptions.size() : 0;
        }

        @Override
        public int getDureeTotaleTraitement() {
            if (prescriptions == null || prescriptions.isEmpty()) {
                return 0;
            }
            return prescriptions.stream()
                    .mapToInt(PrescriptionDTO::getDureeEnJours)
                    .max()
                    .orElse(0);
        }
    }

    /**
     * Implémentation concrète de PrescriptionCreateDTO
     */
    public static class PrescriptionCreateDTOImpl implements PrescriptionCreateDTO {
        private Long idMedicament;
        private Integer quantite;
        private String frequence;
        private Integer dureeEnJours;
        private String posologie;

        // Constructeurs
        public PrescriptionCreateDTOImpl() {}

        public PrescriptionCreateDTOImpl(Long idMedicament, Integer quantite, String frequence, Integer dureeEnJours) {
            this.idMedicament = idMedicament;
            this.quantite = quantite;
            this.frequence = frequence;
            this.dureeEnJours = dureeEnJours;
        }

        // Implémentation des méthodes de l'interface
        @Override public Long getIdMedicament() { return idMedicament; }
        @Override public void setIdMedicament(Long idMedicament) { this.idMedicament = idMedicament; }

        @Override public Integer getQuantite() { return quantite; }
        @Override public void setQuantite(Integer quantite) { this.quantite = quantite; }

        @Override public String getFrequence() { return frequence; }
        @Override public void setFrequence(String frequence) { this.frequence = frequence; }

        @Override public Integer getDureeEnJours() { return dureeEnJours; }
        @Override public void setDureeEnJours(Integer dureeEnJours) { this.dureeEnJours = dureeEnJours; }

        @Override public String getPosologie() { return posologie; }
        @Override public void setPosologie(String posologie) { this.posologie = posologie; }
    }

    /**
     * Implémentation concrète de PrescriptionDTO
     */
    public static class PrescriptionDTOImpl implements PrescriptionDTO {
        private Long idPrescription;
        private Long idOrdo;
        private Long idMedicament;
        private Integer quantite;
        private String frequence;
        private Integer dureeEnJours;
        private String posologie;
        private String nomMedicament;
        private String forme;
        private String dosage;

        // Constructeurs
        public PrescriptionDTOImpl() {}

        public PrescriptionDTOImpl(Long idPrescription, Long idOrdo, Long idMedicament,
                                   Integer quantite, String frequence, Integer dureeEnJours) {
            this.idPrescription = idPrescription;
            this.idOrdo = idOrdo;
            this.idMedicament = idMedicament;
            this.quantite = quantite;
            this.frequence = frequence;
            this.dureeEnJours = dureeEnJours;
        }

        // Implémentation des méthodes de l'interface
        @Override public Long getIdPrescription() { return idPrescription; }
        @Override public void setIdPrescription(Long idPrescription) { this.idPrescription = idPrescription; }

        @Override public Long getIdOrdo() { return idOrdo; }
        @Override public void setIdOrdo(Long idOrdo) { this.idOrdo = idOrdo; }

        @Override public Long getIdMedicament() { return idMedicament; }
        @Override public void setIdMedicament(Long idMedicament) { this.idMedicament = idMedicament; }

        @Override public Integer getQuantite() { return quantite; }
        @Override public void setQuantite(Integer quantite) { this.quantite = quantite; }

        @Override public String getFrequence() { return frequence; }
        @Override public void setFrequence(String frequence) { this.frequence = frequence; }

        @Override public Integer getDureeEnJours() { return dureeEnJours; }
        @Override public void setDureeEnJours(Integer dureeEnJours) { this.dureeEnJours = dureeEnJours; }

        @Override public String getPosologie() { return posologie; }
        @Override public void setPosologie(String posologie) { this.posologie = posologie; }

        @Override public String getNomMedicament() { return nomMedicament; }
        @Override public void setNomMedicament(String nomMedicament) { this.nomMedicament = nomMedicament; }

        @Override public String getForme() { return forme; }
        @Override public void setForme(String forme) { this.forme = forme; }

        @Override public String getDosage() { return dosage; }
        @Override public void setDosage(String dosage) { this.dosage = dosage; }

        @Override
        public String formatPrescription() {
            return String.format("%s - %d comprimé(s) - %s - %d jour(s)",
                    nomMedicament != null ? nomMedicament : "Médicament " + idMedicament,
                    quantite != null ? quantite : 0,
                    frequence != null ? frequence : "N/A",
                    dureeEnJours != null ? dureeEnJours : 0);
        }
    }

    /**
     * Implémentation concrète de OrdonnanceStatsDTO
     */
    public static class OrdonnanceStatsDTOImpl implements OrdonnanceStatsDTO {
        private int totalOrdonnances;
        private int ordonnancesCeMois;
        private double moyenneMedicamentsParOrdonnance;
        private String medicamentLePlusPrescrit;
        private String medecinLePlusActif;
        private Map<String, Integer> repartitionParMois;

        // Constructeurs
        public OrdonnanceStatsDTOImpl() {
            this.repartitionParMois = new HashMap<>();
        }

        public OrdonnanceStatsDTOImpl(int totalOrdonnances, int ordonnancesCeMois) {
            this();
            this.totalOrdonnances = totalOrdonnances;
            this.ordonnancesCeMois = ordonnancesCeMois;
        }

        // Implémentation des méthodes de l'interface
        @Override public int getTotalOrdonnances() { return totalOrdonnances; }
        @Override public void setTotalOrdonnances(int totalOrdonnances) { this.totalOrdonnances = totalOrdonnances; }

        @Override public int getOrdonnancesCeMois() { return ordonnancesCeMois; }
        @Override public void setOrdonnancesCeMois(int ordonnancesCeMois) { this.ordonnancesCeMois = ordonnancesCeMois; }

        @Override public double getMoyenneMedicamentsParOrdonnance() { return moyenneMedicamentsParOrdonnance; }
        @Override public void setMoyenneMedicamentsParOrdonnance(double moyenneMedicamentsParOrdonnance) {
            this.moyenneMedicamentsParOrdonnance = moyenneMedicamentsParOrdonnance;
        }

        @Override public String getMedicamentLePlusPrescrit() { return medicamentLePlusPrescrit; }
        @Override public void setMedicamentLePlusPrescrit(String medicamentLePlusPrescrit) {
            this.medicamentLePlusPrescrit = medicamentLePlusPrescrit;
        }

        @Override public String getMedecinLePlusActif() { return medecinLePlusActif; }
        @Override public void setMedecinLePlusActif(String medecinLePlusActif) {
            this.medecinLePlusActif = medecinLePlusActif;
        }

        @Override public Map<String, Integer> getRepartitionParMois() { return repartitionParMois; }
        @Override public void setRepartitionParMois(Map<String, Integer> repartitionParMois) {
            this.repartitionParMois = repartitionParMois;
        }

        @Override
        public void ajouterRepartitionMois(String mois, int nombre) {
            repartitionParMois.put(mois, nombre);
        }
    }

    // ==================== CONSTRUCTEUR ====================

    public OrdonnanceServiceImpl(OrdonnanceRepository ordonnanceRepository,
                                 PrescriptionRepository prescriptionRepository) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    // ==================== IMPLÉMENTATION DES MÉTHODES ====================

    @Override
    public OrdonnanceDTO createOrdonnance(OrdonnanceCreateDTO ordonnanceCreateDTO,
                                          List<PrescriptionCreateDTO> prescriptions) {
        System.out.println("Création d'une nouvelle ordonnance...");

        // 1. Validation
        if (ordonnanceCreateDTO == null) {
            throw new IllegalArgumentException("OrdonnanceCreateDTO ne peut pas être null");
        }

        if (ordonnanceCreateDTO.getIdDM() == null || ordonnanceCreateDTO.getIdDM() <= 0) {
            throw new IllegalArgumentException("ID de dossier médical invalide");
        }

        if (ordonnanceCreateDTO.getIdMedecin() == null || ordonnanceCreateDTO.getIdMedecin() <= 0) {
            throw new IllegalArgumentException("ID de médecin invalide");
        }

        if (prescriptions == null || prescriptions.isEmpty()) {
            throw new IllegalArgumentException("Une ordonnance doit contenir au moins une prescription");
        }

        // 2. Création de l'ordonnance
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setIdDM(ordonnanceCreateDTO.getIdDM());
        ordonnance.setIdMedecin(ordonnanceCreateDTO.getIdMedecin());
        ordonnance.setDateOrdonnance(
                ordonnanceCreateDTO.getDateOrdonnance() != null ?
                        ordonnanceCreateDTO.getDateOrdonnance() : LocalDate.now()
        );

        // 3. Sauvegarde
        ordonnanceRepository.create(ordonnance);

        // 4. Création des prescriptions
        List<PrescriptionDTO> prescriptionsCreees = new ArrayList<>();
        for (PrescriptionCreateDTO prescriptionDTO : prescriptions) {
            validatePrescriptionDTO(prescriptionDTO);

            Prescription prescription = new Prescription();
            prescription.setIdOrdo(ordonnance.getIdOrdo());
            prescription.setIdMedicament(prescriptionDTO.getIdMedicament());
            prescription.setQuantite(prescriptionDTO.getQuantite());
            prescription.setFrequence(prescriptionDTO.getFrequence());
            prescription.setDureeEnJours(prescriptionDTO.getDureeEnJours());

            prescriptionRepository.create(prescription);
            prescriptionsCreees.add(convertToPrescriptionDTO(prescription));
        }

        // 5. Retour du DTO
        OrdonnanceDTO result = convertToOrdonnanceDTO(ordonnance);
        result.setPrescriptions(prescriptionsCreees);

        System.out.println("Ordonnance créée avec ID: " + result.getIdOrdo());
        return result;
    }

    @Override
    public OrdonnanceDTO getOrdonnanceById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        Ordonnance ordonnance = ordonnanceRepository.findById(id);
        if (ordonnance == null) {
            throw new OrdonnanceNotFoundException("Ordonnance non trouvée avec ID: " + id);
        }

        List<Prescription> prescriptions = prescriptionRepository.findByOrdonnance(id);
        OrdonnanceDTO ordonnanceDTO = convertToOrdonnanceDTO(ordonnance);

        List<PrescriptionDTO> prescriptionDTOs = prescriptions.stream()
                .map(this::convertToPrescriptionDTO)
                .collect(Collectors.toList());

        ordonnanceDTO.setPrescriptions(prescriptionDTOs);

        return ordonnanceDTO;
    }

    @Override
    public OrdonnanceDTO updateOrdonnance(Long id, OrdonnanceUpdateDTO ordonnanceUpdateDTO) {
        Ordonnance ordonnance = ordonnanceRepository.findById(id);
        if (ordonnance == null) {
            throw new OrdonnanceNotFoundException("Ordonnance non trouvée avec ID: " + id);
        }

        boolean modifications = false;

        if (ordonnanceUpdateDTO.getDateOrdonnance() != null &&
                !ordonnanceUpdateDTO.getDateOrdonnance().equals(ordonnance.getDateOrdonnance())) {
            ordonnance.setDateOrdonnance(ordonnanceUpdateDTO.getDateOrdonnance());
            modifications = true;
        }

        if (modifications) {
            ordonnanceRepository.update(ordonnance);
        }

        return getOrdonnanceById(id);
    }

    @Override
    public void deleteOrdonnance(Long id) {
        Ordonnance ordonnance = ordonnanceRepository.findById(id);
        if (ordonnance == null) {
            throw new OrdonnanceNotFoundException("Ordonnance non trouvée avec ID: " + id);
        }

        // Suppression en cascade des prescriptions
        prescriptionRepository.deleteByOrdonnance(id);
        ordonnanceRepository.deleteById(id);

        System.out.println("Ordonnance supprimée: " + id);
    }

    @Override
    public List<OrdonnanceDTO> getAllOrdonnances() {
        return ordonnanceRepository.findAll().stream()
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdonnanceDTO> getOrdonnancesByDossierMedical(Long idDM) {
        if (idDM == null || idDM <= 0) {
            throw new IllegalArgumentException("ID de dossier médical invalide");
        }

        return ordonnanceRepository.findByDossierMedical(idDM).stream()
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdonnanceDTO> getOrdonnancesByMedecin(Long idMedecin) {
        if (idMedecin == null || idMedecin <= 0) {
            throw new IllegalArgumentException("ID de médecin invalide");
        }

        return ordonnanceRepository.findByMedecin(idMedecin).stream()
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdonnanceDTO> searchOrdonnancesByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être nulles");
        }

        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("Date début doit être avant date fin");
        }

        List<Ordonnance> toutesOrdonnances = ordonnanceRepository.findAll();

        return toutesOrdonnances.stream()
                .filter(o -> !o.getDateOrdonnance().isBefore(dateDebut) &&
                        !o.getDateOrdonnance().isAfter(dateFin))
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionDTO addPrescriptionToOrdonnance(Long idOrdo, PrescriptionCreateDTO prescriptionCreateDTO) {
        Ordonnance ordonnance = ordonnanceRepository.findById(idOrdo);
        if (ordonnance == null) {
            throw new OrdonnanceNotFoundException("Ordonnance non trouvée avec ID: " + idOrdo);
        }

        validatePrescriptionDTO(prescriptionCreateDTO);

        Prescription prescription = new Prescription();
        prescription.setIdOrdo(idOrdo);
        prescription.setIdMedicament(prescriptionCreateDTO.getIdMedicament());
        prescription.setQuantite(prescriptionCreateDTO.getQuantite());
        prescription.setFrequence(prescriptionCreateDTO.getFrequence());
        prescription.setDureeEnJours(prescriptionCreateDTO.getDureeEnJours());

        prescriptionRepository.create(prescription);

        return convertToPrescriptionDTO(prescription);
    }

    @Override
    public PrescriptionDTO updatePrescription(Long idPrescription, Integer quantite,
                                              String frequence, Integer dureeEnJours) {
        Prescription prescription = prescriptionRepository.findById(idPrescription);
        if (prescription == null) {
            throw new PrescriptionNotFoundException("Prescription non trouvée avec ID: " + idPrescription);
        }

        boolean modifications = false;

        if (quantite != null && !quantite.equals(prescription.getQuantite())) {
            prescription.setQuantite(quantite);
            modifications = true;
        }

        if (frequence != null && !frequence.equals(prescription.getFrequence())) {
            prescription.setFrequence(frequence);
            modifications = true;
        }

        if (dureeEnJours != null && !dureeEnJours.equals(prescription.getDureeEnJours())) {
            prescription.setDureeEnJours(dureeEnJours);
            modifications = true;
        }

        if (modifications) {
            prescriptionRepository.update(prescription);
        }

        return convertToPrescriptionDTO(prescription);
    }

    @Override
    public void deletePrescription(Long idPrescription) {
        Prescription prescription = prescriptionRepository.findById(idPrescription);
        if (prescription == null) {
            throw new PrescriptionNotFoundException("Prescription non trouvée avec ID: " + idPrescription);
        }

        prescriptionRepository.deleteById(idPrescription);
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByOrdonnance(Long idOrdo) {
        Ordonnance ordonnance = ordonnanceRepository.findById(idOrdo);
        if (ordonnance == null) {
            throw new OrdonnanceNotFoundException("Ordonnance non trouvée avec ID: " + idOrdo);
        }

        return prescriptionRepository.findByOrdonnance(idOrdo).stream()
                .map(this::convertToPrescriptionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String generateOrdonnancePDF(Long idOrdo) throws java.io.IOException {
        OrdonnanceDTO ordonnance = getOrdonnanceById(idOrdo);

        // Simulation de génération PDF
        String nomFichier = "ordonnance_" + idOrdo + "_" + LocalDate.now() + ".pdf";

        // Ici, vous intégreriez une vraie bibliothèque PDF comme iText
        System.out.println("Génération PDF pour ordonnance: " + idOrdo);
        System.out.println("Fichier: " + nomFichier);

        return "/tmp/" + nomFichier;
    }

    @Override
    public List<String> checkMedicationInteractions(Long idOrdo) {
        List<PrescriptionDTO> prescriptions = getPrescriptionsByOrdonnance(idOrdo);
        List<String> interactions = new ArrayList<>();

        // Simulation de vérification d'interactions
        if (prescriptions.size() > 1) {
            interactions.add("⚠️ Attention: " + prescriptions.size() + " médicaments prescrits simultanément");
            interactions.add("🔍 Recommandation: Vérifier les interactions possibles");
        }

        // Vérification de médicaments spécifiques (exemple)
        for (PrescriptionDTO p : prescriptions) {
            if (p.getNomMedicament() != null &&
                    p.getNomMedicament().toLowerCase().contains("warfarin")) {
                interactions.add("🚨 Warfarin détecté - Vérifier interactions avec autres médicaments");
            }
        }

        return interactions;
    }

    @Override
    public OrdonnanceDTO duplicateOrdonnance(Long idOrdo, LocalDate nouvelleDate) {
        OrdonnanceDTO originale = getOrdonnanceById(idOrdo);

        // Création de la nouvelle ordonnance
        OrdonnanceCreateDTOImpl nouvelleOrdoDTO = new OrdonnanceCreateDTOImpl();
        nouvelleOrdoDTO.setIdDM(originale.getIdDM());
        nouvelleOrdoDTO.setIdMedecin(originale.getIdMedecin());
        nouvelleOrdoDTO.setDateOrdonnance(nouvelleDate != null ? nouvelleDate : LocalDate.now());
        nouvelleOrdoDTO.setRemarques("Copie de l'ordonnance #" + idOrdo);

        // Copie des prescriptions
        List<PrescriptionCreateDTO> nouvellesPrescriptions = new ArrayList<>();
        for (PrescriptionDTO p : originale.getPrescriptions()) {
            PrescriptionCreateDTOImpl nouvellePrescription = new PrescriptionCreateDTOImpl();
            nouvellePrescription.setIdMedicament(p.getIdMedicament());
            nouvellePrescription.setQuantite(p.getQuantite());
            nouvellePrescription.setFrequence(p.getFrequence());
            nouvellePrescription.setDureeEnJours(p.getDureeEnJours());
            nouvellesPrescriptions.add(nouvellePrescription);
        }

        return createOrdonnance(nouvelleOrdoDTO, nouvellesPrescriptions);
    }

    @Override
    public OrdonnanceStatsDTO getOrdonnanceStatistics() {
        OrdonnanceStatsDTOImpl stats = new OrdonnanceStatsDTOImpl();

        List<Ordonnance> toutesOrdonnances = ordonnanceRepository.findAll();
        stats.setTotalOrdonnances(toutesOrdonnances.size());

        // Calcul des ordonnances ce mois
        LocalDate debutMois = LocalDate.now().withDayOfMonth(1);
        LocalDate finMois = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        long ordonnancesCeMois = toutesOrdonnances.stream()
                .filter(o -> !o.getDateOrdonnance().isBefore(debutMois) &&
                        !o.getDateOrdonnance().isAfter(finMois))
                .count();
        stats.setOrdonnancesCeMois((int) ordonnancesCeMois);

        // Calcul moyenne médicaments par ordonnance
        double totalMedicaments = 0;
        for (Ordonnance o : toutesOrdonnances) {
            List<Prescription> prescriptions = prescriptionRepository.findByOrdonnance(o.getIdOrdo());
            totalMedicaments += prescriptions.size();
        }

        if (toutesOrdonnances.size() > 0) {
            stats.setMoyenneMedicamentsParOrdonnance(totalMedicaments / toutesOrdonnances.size());
        }

        // Répartition par mois (simulation)
        stats.ajouterRepartitionMois("Janvier", 15);
        stats.ajouterRepartitionMois("Février", 22);
        stats.ajouterRepartitionMois("Mars", 18);

        return stats;
    }

    // ==================== MÉTHODES PRIVÉES AUXILIAIRES ====================

    private void validatePrescriptionDTO(PrescriptionCreateDTO prescriptionDTO) {
        if (prescriptionDTO == null) {
            throw new IllegalArgumentException("PrescriptionDTO ne peut pas être null");
        }

        if (prescriptionDTO.getIdMedicament() == null || prescriptionDTO.getIdMedicament() <= 0) {
            throw new IllegalArgumentException("ID de médicament invalide");
        }

        if (prescriptionDTO.getQuantite() == null || prescriptionDTO.getQuantite() <= 0) {
            throw new IllegalArgumentException("Quantité invalide");
        }

        if (prescriptionDTO.getFrequence() == null || prescriptionDTO.getFrequence().trim().isEmpty()) {
            throw new IllegalArgumentException("Fréquence invalide");
        }

        if (prescriptionDTO.getDureeEnJours() == null || prescriptionDTO.getDureeEnJours() <= 0) {
            throw new IllegalArgumentException("Durée en jours invalide");
        }
    }

    private OrdonnanceDTO convertToOrdonnanceDTO(Ordonnance ordonnance) {
        OrdonnanceDTOImpl dto = new OrdonnanceDTOImpl();
        dto.setIdOrdo(ordonnance.getIdOrdo());
        dto.setIdDM(ordonnance.getIdDM());
        dto.setIdMedecin(ordonnance.getIdMedecin());
        dto.setDateOrdonnance(ordonnance.getDateOrdonnance());
        // Note: Vous devrez récupérer les noms depuis la base de données
        dto.setNomMedecin("Dr. Medecin " + ordonnance.getIdMedecin());
        dto.setNomPatient("Patient Dossier " + ordonnance.getIdDM());
        return dto;
    }

    private PrescriptionDTO convertToPrescriptionDTO(Prescription prescription) {
        PrescriptionDTOImpl dto = new PrescriptionDTOImpl();
        dto.setIdPrescription(prescription.getIdPrescription());
        dto.setIdOrdo(prescription.getIdOrdo());
        dto.setIdMedicament(prescription.getIdMedicament());
        dto.setQuantite(prescription.getQuantite());
        dto.setFrequence(prescription.getFrequence());
        dto.setDureeEnJours(prescription.getDureeEnJours());
        // Note: Vous devrez récupérer le nom du médicament depuis la base
        dto.setNomMedicament("Médicament " + prescription.getIdMedicament());
        return dto;
    }
}