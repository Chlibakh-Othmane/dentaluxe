package ma.dentaluxe.service.ordonnance.baseImplementation;

import ma.dentaluxe.service.ordonnance.api.PrescriptionService;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import java.time.LocalDate;
import java.util.*;

/**
 * Implémentation du service de gestion des prescriptions médicamenteuses.
 *
 * @author OTHMANE CHLIBAKH
 */
public class PrescriptionServiceImpl implements PrescriptionService {

    // ==================== DÉPENDANCES ====================

    private final PrescriptionRepository prescriptionRepository;
    private final OrdonnanceRepository ordonnanceRepository;

    // ==================== IMPLÉMENTATION DES DTOs ====================

    public static class PrescriptionCreateDTOImpl implements PrescriptionCreateDTO {
        private Long idOrdo;
        private Long idMedicament;
        private Integer quantite;
        private String frequence;
        private Integer dureeEnJours;
        private String posologie;
        private String instructions;

        public PrescriptionCreateDTOImpl() {}

        public PrescriptionCreateDTOImpl(Long idOrdo, Long idMedicament, Integer quantite,
                                         String frequence, Integer dureeEnJours) {
            this.idOrdo = idOrdo;
            this.idMedicament = idMedicament;
            this.quantite = quantite;
            this.frequence = frequence;
            this.dureeEnJours = dureeEnJours;
        }

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


        @Override public void setPosologie(String posologie) { this.posologie = posologie; }

        @Override public String getInstructions() { return instructions; }
        @Override public void setInstructions(String instructions) { this.instructions = instructions; }
    }

    public static class PrescriptionUpdateDTOImpl implements PrescriptionUpdateDTO {
        private Long idPrescription;
        private Integer quantite;
        private String frequence;
        private Integer dureeEnJours;
        private String posologie;
        private String instructions;

        public PrescriptionUpdateDTOImpl() {}

        @Override public Long getIdPrescription() { return idPrescription; }
        @Override public void setIdPrescription(Long idPrescription) { this.idPrescription = idPrescription; }

        @Override public Integer getQuantite() { return quantite; }
        @Override public void setQuantite(Integer quantite) { this.quantite = quantite; }

        @Override public String getFrequence() { return frequence; }
        @Override public void setFrequence(String frequence) { this.frequence = frequence; }

        @Override public Integer getDureeEnJours() { return dureeEnJours; }
        @Override public void setDureeEnJours(Integer dureeEnJours) { this.dureeEnJours = dureeEnJours; }


        @Override public void setPosologie(String posologie) { this.posologie = posologie; }

        @Override public String getInstructions() { return instructions; }
        @Override public void setInstructions(String instructions) { this.instructions = instructions; }
    }

    public static class PrescriptionDTOImpl implements PrescriptionDTO {
        private Long idPrescription;
        private Long idOrdo;
        private Long idMedicament;
        private Integer quantite;
        private String frequence;
        private Integer dureeEnJours;
        private String posologie;
        private String instructions;
        private String nomMedicament;
        private String formePharmaceutique;
        private String dosage;
        private LocalDate dateOrdonnance;
        private String nomMedecin;
        private String nomPatient;

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


        @Override public void setPosologie(String posologie) { this.posologie = posologie; }

        @Override public String getInstructions() { return instructions; }
        @Override public void setInstructions(String instructions) { this.instructions = instructions; }

        @Override public String getNomMedicament() { return nomMedicament; }
        @Override public void setNomMedicament(String nomMedicament) { this.nomMedicament = nomMedicament; }

        @Override public String getFormePharmaceutique() { return formePharmaceutique; }
        @Override public void setFormePharmaceutique(String formePharmaceutique) { this.formePharmaceutique = formePharmaceutique; }

        @Override public String getDosage() { return dosage; }
        @Override public void setDosage(String dosage) { this.dosage = dosage; }

        @Override public LocalDate getDateOrdonnance() { return dateOrdonnance; }
        @Override public void setDateOrdonnance(LocalDate dateOrdonnance) { this.dateOrdonnance = dateOrdonnance; }

        @Override public String getNomMedecin() { return nomMedecin; }
        @Override public void setNomMedecin(String nomMedecin) { this.nomMedecin = nomMedecin; }

        @Override public String getNomPatient() { return nomPatient; }
        @Override public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }

        @Override
        public int getQuantiteTotale() {
            if (quantite == null || dureeEnJours == null) return 0;

            // Calcul selon la fréquence
            int multiplicateur = 1;
            if (frequence != null) {
                if (frequence.contains("fois/jour")) {
                    try {
                        String nb = frequence.split(" ")[0];
                        multiplicateur = Integer.parseInt(nb);
                    } catch (Exception e) {
                        multiplicateur = 1;
                    }
                }
            }

            return quantite * dureeEnJours * multiplicateur;
        }

        @Override
        public String formatPosologieComplete() {
            StringBuilder sb = new StringBuilder();
            if (nomMedicament != null) {
                sb.append(nomMedicament);
                if (dosage != null) {
                    sb.append(" ").append(dosage);
                }
                sb.append(" : ");
            }

            sb.append(quantite != null ? quantite : "?").append(" unité(s)");

            if (frequence != null) {
                sb.append(", ").append(frequence);
            }

            if (dureeEnJours != null) {
                sb.append(" pendant ").append(dureeEnJours).append(" jour(s)");
            }

            if (posologie != null) {
                sb.append(" (").append(posologie).append(")");
            }

            return sb.toString();
        }

        @Override
        public boolean isTraitementEnCours() {
            if (dateOrdonnance == null || dureeEnJours == null) {
                return false;
            }
            LocalDate dateFin = dateOrdonnance.plusDays(dureeEnJours);
            return LocalDate.now().isBefore(dateFin) || LocalDate.now().isEqual(dateFin);
        }

        @Override
        public int getJoursRestants() {
            if (!isTraitementEnCours()) {
                return 0;
            }
            LocalDate dateFin = dateOrdonnance.plusDays(dureeEnJours);
            return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateFin);
        }
    }

    public static class MedicamentPrescriptionDTOImpl implements MedicamentPrescriptionDTO {
        private Long idMedicament;
        private String nomMedicament;
        private int nombrePrescriptions;
        private int quantiteTotalePrescrite;
        private String frequenceMoyenne;

        public MedicamentPrescriptionDTOImpl() {}

        @Override public Long getIdMedicament() { return idMedicament; }
        @Override public void setIdMedicament(Long idMedicament) { this.idMedicament = idMedicament; }

        @Override public String getNomMedicament() { return nomMedicament; }
        @Override public void setNomMedicament(String nomMedicament) { this.nomMedicament = nomMedicament; }

        @Override public int getNombrePrescriptions() { return nombrePrescriptions; }
        @Override public void setNombrePrescriptions(int nombrePrescriptions) { this.nombrePrescriptions = nombrePrescriptions; }

        @Override public int getQuantiteTotalePrescrite() { return quantiteTotalePrescrite; }
        @Override public void setQuantiteTotalePrescrite(int quantiteTotalePrescrite) { this.quantiteTotalePrescrite = quantiteTotalePrescrite; }

        @Override public String getFrequenceMoyenne() { return frequenceMoyenne; }
        @Override public void setFrequenceMoyenne(String frequenceMoyenne) { this.frequenceMoyenne = frequenceMoyenne; }
    }

    public static class PrescriptionReportDTOImpl implements PrescriptionReportDTO {
        private Long idOrdo;
        private int nombrePrescriptions;
        private int dureeMoyenneTraitement;
        private List<String> categoriesMedicaments;
        private Map<String, Integer> repartitionParFrequence;
        private List<PrescriptionDTO> prescriptions;

        public PrescriptionReportDTOImpl() {
            categoriesMedicaments = new ArrayList<>();
            repartitionParFrequence = new HashMap<>();
            prescriptions = new ArrayList<>();
        }

        @Override public Long getIdOrdo() { return idOrdo; }
        @Override public void setIdOrdo(Long idOrdo) { this.idOrdo = idOrdo; }

        @Override public int getNombrePrescriptions() { return nombrePrescriptions; }
        @Override public void setNombrePrescriptions(int nombrePrescriptions) { this.nombrePrescriptions = nombrePrescriptions; }

        @Override public int getDureeMoyenneTraitement() { return dureeMoyenneTraitement; }
        @Override public void setDureeMoyenneTraitement(int dureeMoyenneTraitement) { this.dureeMoyenneTraitement = dureeMoyenneTraitement; }

        @Override public List<String> getCategoriesMedicaments() { return categoriesMedicaments; }
        @Override public void setCategoriesMedicaments(List<String> categoriesMedicaments) { this.categoriesMedicaments = categoriesMedicaments; }

        @Override public Map<String, Integer> getRepartitionParFrequence() { return repartitionParFrequence; }
        @Override public void setRepartitionParFrequence(Map<String, Integer> repartitionParFrequence) { this.repartitionParFrequence = repartitionParFrequence; }

        @Override public List<PrescriptionDTO> getPrescriptions() { return prescriptions; }
        @Override public void setPrescriptions(List<PrescriptionDTO> prescriptions) { this.prescriptions = prescriptions; }
    }

    public static class ValidationResultDTOImpl implements ValidationResultDTO {
        private boolean valid;
        private List<String> messages;
        private List<String> warnings;
        private List<String> errors;

        public ValidationResultDTOImpl() {
            messages = new ArrayList<>();
            warnings = new ArrayList<>();
            errors = new ArrayList<>();
        }

        @Override public boolean isValid() { return valid; }
        @Override public void setValid(boolean valid) { this.valid = valid; }

        @Override public List<String> getMessages() { return messages; }
        @Override public void setMessages(List<String> messages) { this.messages = messages; }

        @Override public List<String> getWarnings() { return warnings; }
        @Override public void setWarnings(List<String> warnings) { this.warnings = warnings; }

        @Override public List<String> getErrors() { return errors; }
        @Override public void setErrors(List<String> errors) { this.errors = errors; }

        public void addMessage(String message) {
            messages.add(message);
        }

        public void addWarning(String warning) {
            warnings.add(warning);
            valid = false;
        }

        public void addError(String error) {
            errors.add(error);
            valid = false;
        }
    }

    // ==================== CONSTRUCTEUR ====================

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository,
                                   OrdonnanceRepository ordonnanceRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.ordonnanceRepository = ordonnanceRepository;
    }

    // ==================== IMPLÉMENTATION DES MÉTHODES ====================

    @Override
    public PrescriptionDTO createPrescription(PrescriptionCreateDTO prescriptionCreateDTO) {
        System.out.println("Création d'une nouvelle prescription...");

        // Validation
        ValidationResultDTO validationResult = validatePrescription(prescriptionCreateDTO);
        if (!validationResult.isValid()) {
            throw new PrescriptionValidationException(
                    "Validation échouée: " + String.join(", ", validationResult.getErrors())
            );
        }

        // Vérifier si l'ordonnance existe
        Ordonnance ordonnance = ordonnanceRepository.findById(prescriptionCreateDTO.getIdOrdo());
        if (ordonnance == null) {
            throw new RuntimeException("Ordonnance non trouvée avec ID: " + prescriptionCreateDTO.getIdOrdo());
        }

        // Création de la prescription
        Prescription prescription = new Prescription();
        prescription.setIdOrdo(prescriptionCreateDTO.getIdOrdo());
        prescription.setIdMedicament(prescriptionCreateDTO.getIdMedicament());
        prescription.setQuantite(prescriptionCreateDTO.getQuantite());
        prescription.setFrequence(prescriptionCreateDTO.getFrequence());
        prescription.setDureeEnJours(prescriptionCreateDTO.getDureeEnJours());

        prescriptionRepository.create(prescription);

        System.out.println("Prescription créée avec ID: " + prescription.getIdPrescription());

        return convertToPrescriptionDTO(prescription);
    }

    @Override
    public PrescriptionDTO getPrescriptionById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de prescription invalide");
        }

        Prescription prescription = prescriptionRepository.findById(id);
        if (prescription == null) {
            throw new PrescriptionNotFoundException("Prescription non trouvée avec ID: " + id);
        }

        return convertToPrescriptionDTO(prescription);
    }

    @Override
    public PrescriptionDTO updatePrescription(PrescriptionUpdateDTO prescriptionUpdateDTO) {
        if (prescriptionUpdateDTO == null || prescriptionUpdateDTO.getIdPrescription() == null) {
            throw new IllegalArgumentException("PrescriptionUpdateDTO invalide");
        }

        Long id = prescriptionUpdateDTO.getIdPrescription();
        Prescription prescription = prescriptionRepository.findById(id);
        if (prescription == null) {
            throw new PrescriptionNotFoundException("Prescription non trouvée avec ID: " + id);
        }

        // Mise à jour des champs
        boolean modifications = false;

        if (prescriptionUpdateDTO.getQuantite() != null &&
                !prescriptionUpdateDTO.getQuantite().equals(prescription.getQuantite())) {
            prescription.setQuantite(prescriptionUpdateDTO.getQuantite());
            modifications = true;
        }

        if (prescriptionUpdateDTO.getFrequence() != null &&
                !prescriptionUpdateDTO.getFrequence().equals(prescription.getFrequence())) {
            prescription.setFrequence(prescriptionUpdateDTO.getFrequence());
            modifications = true;
        }

        if (prescriptionUpdateDTO.getDureeEnJours() != null &&
                !prescriptionUpdateDTO.getDureeEnJours().equals(prescription.getDureeEnJours())) {
            prescription.setDureeEnJours(prescriptionUpdateDTO.getDureeEnJours());
            modifications = true;
        }



        if (modifications) {
            prescriptionRepository.update(prescription);
            System.out.println("Prescription mise à jour: " + id);
        }

        return convertToPrescriptionDTO(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id);
        if (prescription == null) {
            throw new PrescriptionNotFoundException("Prescription non trouvée avec ID: " + id);
        }

        prescriptionRepository.deleteById(id);
        System.out.println("Prescription supprimée: " + id);
    }

    @Override
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(this::convertToPrescriptionDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByOrdonnance(Long idOrdo) {
        if (idOrdo == null || idOrdo <= 0) {
            throw new IllegalArgumentException("ID d'ordonnance invalide");
        }

        // Vérifier que l'ordonnance existe
        Ordonnance ordonnance = ordonnanceRepository.findById(idOrdo);
        if (ordonnance == null) {
            throw new RuntimeException("Ordonnance non trouvée avec ID: " + idOrdo);
        }

        return prescriptionRepository.findByOrdonnance(idOrdo).stream()
                .map(this::convertToPrescriptionDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByMedicament(Long idMedicament) {
        if (idMedicament == null || idMedicament <= 0) {
            throw new IllegalArgumentException("ID de médicament invalide");
        }

        // Note: Cette méthode nécessite une implémentation dans le repository
        // Pour l'instant, on filtre manuellement
        List<Prescription> toutesPrescriptions = prescriptionRepository.findAll();

        return toutesPrescriptions.stream()
                .filter(p -> idMedicament.equals(p.getIdMedicament()))
                .map(this::convertToPrescriptionDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> searchByFrequence(String frequence) {
        if (frequence == null || frequence.trim().isEmpty()) {
            throw new IllegalArgumentException("Fréquence invalide");
        }

        List<Prescription> toutesPrescriptions = prescriptionRepository.findAll();

        return toutesPrescriptions.stream()
                .filter(p -> frequence.equalsIgnoreCase(p.getFrequence()))
                .map(this::convertToPrescriptionDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> searchByDuree(int minJours, int maxJours) {
        if (minJours < 0 || maxJours < 0 || minJours > maxJours) {
            throw new IllegalArgumentException("Plage de durée invalide");
        }

        List<Prescription> toutesPrescriptions = prescriptionRepository.findAll();

        return toutesPrescriptions.stream()
                .filter(p -> p.getDureeEnJours() != null &&
                        p.getDureeEnJours() >= minJours &&
                        p.getDureeEnJours() <= maxJours)
                .map(this::convertToPrescriptionDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> searchByQuantiteSuperieure(int quantiteMin) {
        if (quantiteMin < 0) {
            throw new IllegalArgumentException("Quantité minimale invalide");
        }

        List<Prescription> toutesPrescriptions = prescriptionRepository.findAll();

        return toutesPrescriptions.stream()
                .filter(p -> p.getQuantite() != null && p.getQuantite() > quantiteMin)
                .map(this::convertToPrescriptionDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public int getQuantiteTotalePrescrite(Long idMedicament) {
        List<PrescriptionDTO> prescriptions = getPrescriptionsByMedicament(idMedicament);

        return prescriptions.stream()
                .mapToInt(PrescriptionDTO::getQuantiteTotale)
                .sum();
    }

    @Override
    public boolean isMedicamentPrescritDansOrdonnance(Long idOrdo, Long idMedicament) {
        List<PrescriptionDTO> prescriptions = getPrescriptionsByOrdonnance(idOrdo);

        return prescriptions.stream()
                .anyMatch(p -> idMedicament.equals(p.getIdMedicament()));
    }

    @Override
    public List<MedicamentPrescriptionDTO> getMedicamentsLesPlusPrescrits(int limit) {
        if (limit <= 0) {
            limit = 10;
        }

        // Simulation - dans un vrai projet, on ferait une requête SQL optimisée
        List<Prescription> toutesPrescriptions = prescriptionRepository.findAll();

        // Grouper par médicament
        Map<Long, MedicamentPrescriptionDTOImpl> medicamentsMap = new HashMap<>();

        for (Prescription p : toutesPrescriptions) {
            Long idMedicament = p.getIdMedicament();
            MedicamentPrescriptionDTOImpl dto = medicamentsMap.get(idMedicament);

            if (dto == null) {
                dto = new MedicamentPrescriptionDTOImpl();
                dto.setIdMedicament(idMedicament);
                dto.setNomMedicament("Médicament " + idMedicament); // À remplacer par vraie donnée
                medicamentsMap.put(idMedicament, dto);
            }

            dto.setNombrePrescriptions(dto.getNombrePrescriptions() + 1);
            dto.setQuantiteTotalePrescrite(dto.getQuantiteTotalePrescrite() +
                    (p.getQuantite() != null ? p.getQuantite() : 0));
        }

        // Trier par nombre de prescriptions décroissant
        List<MedicamentPrescriptionDTOImpl> result = new ArrayList<>(medicamentsMap.values());
        result.sort((a, b) -> Integer.compare(b.getNombrePrescriptions(), a.getNombrePrescriptions()));

        // Limiter le nombre de résultats
        if (result.size() > limit) {
            result = result.subList(0, limit);
        }

        return new ArrayList<>(result);
    }

    @Override
    public PrescriptionReportDTO generatePrescriptionReport(Long idOrdo) {
        List<PrescriptionDTO> prescriptions = getPrescriptionsByOrdonnance(idOrdo);

        PrescriptionReportDTOImpl report = new PrescriptionReportDTOImpl();
        report.setIdOrdo(idOrdo);
        report.setNombrePrescriptions(prescriptions.size());
        report.setPrescriptions(prescriptions);

        // Calculer la durée moyenne
        double dureeMoyenne = prescriptions.stream()
                .mapToInt(PrescriptionDTO::getDureeEnJours)
                .average()
                .orElse(0);
        report.setDureeMoyenneTraitement((int) dureeMoyenne);

        // Répartition par fréquence
        Map<String, Integer> repartition = new HashMap<>();
        for (PrescriptionDTO p : prescriptions) {
            String freq = p.getFrequence();
            repartition.put(freq, repartition.getOrDefault(freq, 0) + 1);
        }
        report.setRepartitionParFrequence(repartition);

        // Catégories de médicaments (simulation)
        report.getCategoriesMedicaments().add("Antibiotiques");
        report.getCategoriesMedicaments().add("Analgésiques");
        report.getCategoriesMedicaments().add("Anti-inflammatoires");

        return report;
    }

    @Override
    public ValidationResultDTO validatePrescription(PrescriptionCreateDTO prescriptionDTO) {
        ValidationResultDTOImpl result = new ValidationResultDTOImpl();
        result.setValid(true);

        if (prescriptionDTO == null) {
            result.addError("La prescription ne peut pas être nulle");
            return result;
        }

        // Validation de l'ordonnance
        if (prescriptionDTO.getIdOrdo() == null || prescriptionDTO.getIdOrdo() <= 0) {
            result.addError("ID d'ordonnance invalide");
        }

        // Validation du médicament
        if (prescriptionDTO.getIdMedicament() == null || prescriptionDTO.getIdMedicament() <= 0) {
            result.addError("ID de médicament invalide");
        }

        // Validation de la quantité
        if (prescriptionDTO.getQuantite() == null || prescriptionDTO.getQuantite() <= 0) {
            result.addError("La quantité doit être supérieure à 0");
        } else if (prescriptionDTO.getQuantite() > 100) {
            result.addWarning("Quantité élevée détectée: " + prescriptionDTO.getQuantite());
        }

        // Validation de la fréquence
        if (prescriptionDTO.getFrequence() == null || prescriptionDTO.getFrequence().trim().isEmpty()) {
            result.addError("La fréquence est obligatoire");
        } else {
            String freq = prescriptionDTO.getFrequence().toLowerCase();
            if (!freq.contains("jour") && !freq.contains("semaine") && !freq.contains("mois")) {
                result.addWarning("Format de fréquence non standard: " + prescriptionDTO.getFrequence());
            }
        }

        // Validation de la durée
        if (prescriptionDTO.getDureeEnJours() == null || prescriptionDTO.getDureeEnJours() <= 0) {
            result.addError("La durée doit être supérieure à 0 jour");
        } else if (prescriptionDTO.getDureeEnJours() > 365) {
            result.addWarning("Durée de traitement très longue: " + prescriptionDTO.getDureeEnJours() + " jours");
        } else if (prescriptionDTO.getDureeEnJours() < 3 &&
                prescriptionDTO.getFrequence() != null &&
                prescriptionDTO.getFrequence().contains("antibiotique")) {
            result.addError("Les antibiotiques doivent être prescrits pour au moins 5 jours");
        }

        // Vérification des interactions (simulation)
        if (prescriptionDTO.getIdMedicament() != null &&
                prescriptionDTO.getIdMedicament() == 123L) { // Exemple d'ID spécifique
            result.addWarning("Médicament à surveiller: interactions possibles avec d'autres traitements");
        }

        return result;
    }

    @Override
    public LocalDate calculateDateFinTraitement(Long idPrescription) {
        PrescriptionDTO prescription = getPrescriptionById(idPrescription);

        if (prescription.getDateOrdonnance() == null || prescription.getDureeEnJours() == null) {
            return null;
        }

        return prescription.getDateOrdonnance().plusDays(prescription.getDureeEnJours());
    }

    // ==================== MÉTHODES PRIVÉES ====================

    private PrescriptionDTO convertToPrescriptionDTO(Prescription prescription) {
        PrescriptionDTOImpl dto = new PrescriptionDTOImpl();
        dto.setIdPrescription(prescription.getIdPrescription());
        dto.setIdOrdo(prescription.getIdOrdo());
        dto.setIdMedicament(prescription.getIdMedicament());
        dto.setQuantite(prescription.getQuantite());
        dto.setFrequence(prescription.getFrequence());
        dto.setDureeEnJours(prescription.getDureeEnJours());

        // Récupérer les informations de l'ordonnance
        Ordonnance ordonnance = ordonnanceRepository.findById(prescription.getIdOrdo());
        if (ordonnance != null) {
            dto.setDateOrdonnance(ordonnance.getDateOrdonnance());
            dto.setNomMedecin("Dr. " + ordonnance.getIdMedecin()); // À remplacer par vraie donnée
            dto.setNomPatient("Patient " + ordonnance.getIdDM()); // À remplacer par vraie donnée
        }

        // Informations du médicament (à récupérer depuis un service médicament)
        dto.setNomMedicament("Médicament " + prescription.getIdMedicament());
        dto.setFormePharmaceutique("Comprimé");
        dto.setDosage("500mg");

        return dto;
    }
}