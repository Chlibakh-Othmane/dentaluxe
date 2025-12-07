package ma.dentaluxe.service.patient.baseImplimentation;

import ma.dentaluxe.service.patient.api.PatientService;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.enums.Assurance;
import ma.dentaluxe.entities.enums.Sexe;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

/**
 * Implémentation du service de gestion des patients.
 *
 * @author OTHMANE CHLIBAKH
 */
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // ==================== IMPLÉMENTATION DES DTOs ====================

    public static class PatientCreateDTOImpl implements PatientCreateDTO {
        private String nom;
        private String prenom;
        private String adresse;
        private String telephone;
        private String email;
        private LocalDate dateNaissance;
        private Sexe sexe;
        private Assurance assurance;

        public PatientCreateDTOImpl() {}

        @Override public String getNom() { return nom; }
        @Override public void setNom(String nom) { this.nom = nom; }

        @Override public String getPrenom() { return prenom; }
        @Override public void setPrenom(String prenom) { this.prenom = prenom; }

        @Override public String getAdresse() { return adresse; }
        @Override public void setAdresse(String adresse) { this.adresse = adresse; }

        @Override public String getTelephone() { return telephone; }
        @Override public void setTelephone(String telephone) { this.telephone = telephone; }

        @Override public String getEmail() { return email; }
        @Override public void setEmail(String email) { this.email = email; }

        @Override public LocalDate getDateNaissance() { return dateNaissance; }
        @Override public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

        @Override public Sexe getSexe() { return sexe; }
        @Override public void setSexe(Sexe sexe) { this.sexe = sexe; }

        @Override public Assurance getAssurance() { return assurance; }
        @Override public void setAssurance(Assurance assurance) { this.assurance = assurance; }
    }

    public static class PatientUpdateDTOImpl implements PatientUpdateDTO {
        private String nom;
        private String prenom;
        private String adresse;
        private String telephone;
        private String email;
        private LocalDate dateNaissance;
        private Sexe sexe;
        private Assurance assurance;

        public PatientUpdateDTOImpl() {}

        @Override public String getNom() { return nom; }
        @Override public void setNom(String nom) { this.nom = nom; }

        @Override public String getPrenom() { return prenom; }
        @Override public void setPrenom(String prenom) { this.prenom = prenom; }

        @Override public String getAdresse() { return adresse; }
        @Override public void setAdresse(String adresse) { this.adresse = adresse; }

        @Override public String getTelephone() { return telephone; }
        @Override public void setTelephone(String telephone) { this.telephone = telephone; }

        @Override public String getEmail() { return email; }
        @Override public void setEmail(String email) { this.email = email; }

        @Override public LocalDate getDateNaissance() { return dateNaissance; }
        @Override public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

        @Override public Sexe getSexe() { return sexe; }
        @Override public void setSexe(Sexe sexe) { this.sexe = sexe; }

        @Override public Assurance getAssurance() { return assurance; }
        @Override public void setAssurance(Assurance assurance) { this.assurance = assurance; }
    }

    public static class PatientDTOImpl implements PatientDTO {
        private Long id;
        private String nom;
        private String prenom;
        private String adresse;
        private String telephone;
        private String email;
        private LocalDate dateNaissance;
        private LocalDateTime dateCreation;
        private Sexe sexe;
        private Assurance assurance;
        private Integer age;

        public PatientDTOImpl() {}

        @Override public Long getId() { return id; }
        @Override public void setId(Long id) { this.id = id; }

        @Override public String getNom() { return nom; }
        @Override public void setNom(String nom) { this.nom = nom; }

        @Override public String getPrenom() { return prenom; }
        @Override public void setPrenom(String prenom) { this.prenom = prenom; }

        @Override public String getAdresse() { return adresse; }
        @Override public void setAdresse(String adresse) { this.adresse = adresse; }

        @Override public String getTelephone() { return telephone; }
        @Override public void setTelephone(String telephone) { this.telephone = telephone; }

        @Override public String getEmail() { return email; }
        @Override public void setEmail(String email) { this.email = email; }

        @Override public LocalDate getDateNaissance() { return dateNaissance; }
        @Override public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

        @Override public LocalDateTime getDateCreation() { return dateCreation; }
        @Override public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

        @Override public Sexe getSexe() { return sexe; }
        @Override public void setSexe(Sexe sexe) { this.sexe = sexe; }

        @Override public Assurance getAssurance() { return assurance; }
        @Override public void setAssurance(Assurance assurance) { this.assurance = assurance; }

        @Override public Integer getAge() { return age; }
        @Override public void setAge(Integer age) { this.age = age; }

        @Override
        public String getFullName() {
            return prenom + " " + nom.toUpperCase();
        }

        @Override
        public String getFormattedInfo() {
            return String.format("%s | %s | %s ans | %s | %s",
                    getFullName(),
                    telephone != null ? telephone : "Pas de téléphone",
                    age != null ? age : "?",
                    sexe != null ? sexe.toString() : "Non spécifié",
                    assurance != null ? assurance.toString() : "Aucune"
            );
        }
    }

    public static class PatientSearchCriteriaDTOImpl implements PatientSearchCriteriaDTO {
        private String nom;
        private String prenom;
        private String telephone;
        private String email;
        private Sexe sexe;
        private Assurance assurance;
        private Integer minAge;
        private Integer maxAge;
        private String adresse;

        public PatientSearchCriteriaDTOImpl() {}

        @Override public String getNom() { return nom; }
        @Override public void setNom(String nom) { this.nom = nom; }

        @Override public String getPrenom() { return prenom; }
        @Override public void setPrenom(String prenom) { this.prenom = prenom; }

        @Override public String getTelephone() { return telephone; }
        @Override public void setTelephone(String telephone) { this.telephone = telephone; }

        @Override public String getEmail() { return email; }
        @Override public void setEmail(String email) { this.email = email; }

        @Override public Sexe getSexe() { return sexe; }
        @Override public void setSexe(Sexe sexe) { this.sexe = sexe; }

        @Override public Assurance getAssurance() { return assurance; }
        @Override public void setAssurance(Assurance assurance) { this.assurance = assurance; }

        @Override public Integer getMinAge() { return minAge; }
        @Override public void setMinAge(Integer minAge) { this.minAge = minAge; }

        @Override public Integer getMaxAge() { return maxAge; }
        @Override public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

        @Override public String getAdresse() { return adresse; }
        @Override public void setAdresse(String adresse) { this.adresse = adresse; }
    }

    public static class PatientStatisticsDTOImpl implements PatientStatisticsDTO {
        private long totalPatients;
        private long patientsHommes;
        private long patientsFemmes;
        private double ageMoyen;
        private Map<Assurance, Long> repartitionAssurance;
        private long nouveauxPatientsCeMois;
        private Map<Integer, Long> repartitionParAge;

        public PatientStatisticsDTOImpl() {
            repartitionAssurance = new HashMap<>();
            repartitionParAge = new HashMap<>();
        }

        @Override public long getTotalPatients() { return totalPatients; }
        @Override public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }

        @Override public long getPatientsHommes() { return patientsHommes; }
        @Override public void setPatientsHommes(long patientsHommes) { this.patientsHommes = patientsHommes; }

        @Override public long getPatientsFemmes() { return patientsFemmes; }
        @Override public void setPatientsFemmes(long patientsFemmes) { this.patientsFemmes = patientsFemmes; }

        @Override public double getAgeMoyen() { return ageMoyen; }
        @Override public void setAgeMoyen(double ageMoyen) { this.ageMoyen = ageMoyen; }

        @Override public Map<Assurance, Long> getRepartitionAssurance() { return repartitionAssurance; }
        @Override public void setRepartitionAssurance(Map<Assurance, Long> repartitionAssurance) { this.repartitionAssurance = repartitionAssurance; }

        @Override public long getNouveauxPatientsCeMois() { return nouveauxPatientsCeMois; }
        @Override public void setNouveauxPatientsCeMois(long nouveauxPatientsCeMois) { this.nouveauxPatientsCeMois = nouveauxPatientsCeMois; }

        @Override public Map<Integer, Long> getRepartitionParAge() { return repartitionParAge; }
        @Override public void setRepartitionParAge(Map<Integer, Long> repartitionParAge) { this.repartitionParAge = repartitionParAge; }
    }

    public static class PatientReportDTOImpl implements PatientReportDTO {
        private Long id;
        private String nomComplet;
        private String informationsPersonnelles;
        private String informationsMedicales;
        private String historiqueConsultations;
        private String assurancesDetails;
        private LocalDate dateGeneration;

        public PatientReportDTOImpl() {}

        @Override public Long getId() { return id; }
        @Override public void setId(Long id) { this.id = id; }

        @Override public String getNomComplet() { return nomComplet; }
        @Override public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

        @Override public String getInformationsPersonnelles() { return informationsPersonnelles; }
        @Override public void setInformationsPersonnelles(String informationsPersonnelles) { this.informationsPersonnelles = informationsPersonnelles; }

        @Override public String getInformationsMedicales() { return informationsMedicales; }
        @Override public void setInformationsMedicales(String informationsMedicales) { this.informationsMedicales = informationsMedicales; }

        @Override public String getHistoriqueConsultations() { return historiqueConsultations; }
        @Override public void setHistoriqueConsultations(String historiqueConsultations) { this.historiqueConsultations = historiqueConsultations; }

        @Override public String getAssurancesDetails() { return assurancesDetails; }
        @Override public void setAssurancesDetails(String assurancesDetails) { this.assurancesDetails = assurancesDetails; }

        @Override public LocalDate getDateGeneration() { return dateGeneration; }
        @Override public void setDateGeneration(LocalDate dateGeneration) { this.dateGeneration = dateGeneration; }
    }

    // ==================== IMPLÉMENTATION DES MÉTHODES ====================

    @Override
    public PatientDTO createPatient(PatientCreateDTO patientCreateDTO) {
        System.out.println("Création d'un nouveau patient...");

        // Validation
        if (!validatePatient(patientCreateDTO)) {
            throw new PatientValidationException("Données patient invalides");
        }

        // Vérifier l'unicité de l'email
        if (emailExists(patientCreateDTO.getEmail())) {
            throw new DuplicateEmailException("Un patient avec cet email existe déjà: " + patientCreateDTO.getEmail());
        }

        // Création de l'entité Patient
        Patient patient = Patient.builder()
                .nom(patientCreateDTO.getNom())
                .prenom(patientCreateDTO.getPrenom())
                .adresse(patientCreateDTO.getAdresse())
                .telephone(patientCreateDTO.getTelephone())
                .email(patientCreateDTO.getEmail())
                .dateNaissance(patientCreateDTO.getDateNaissance())
                .dateCreation(LocalDateTime.now())
                .sexe(patientCreateDTO.getSexe())
                .assurance(patientCreateDTO.getAssurance())
                .build();

        // Sauvegarde
        patientRepository.create(patient);

        System.out.println("Patient créé avec ID: " + patient.getId());

        // Conversion en DTO
        PatientDTO patientDTO = convertToPatientDTO(patient);
        patientDTO.setAge(calculateAge(patient.getDateNaissance()));

        return patientDTO;
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID patient invalide");
        }

        Patient patient = patientRepository.findById(id);
        if (patient == null) {
            throw new PatientNotFoundException("Patient non trouvé avec ID: " + id);
        }

        PatientDTO patientDTO = convertToPatientDTO(patient);
        patientDTO.setAge(calculateAge(patient.getDateNaissance()));

        return patientDTO;
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientUpdateDTO patientUpdateDTO) {
        // Vérifier que le patient existe
        Patient patient = patientRepository.findById(id);
        if (patient == null) {
            throw new PatientNotFoundException("Patient non trouvé avec ID: " + id);
        }

        // Appliquer les modifications
        boolean modifications = false;

        if (patientUpdateDTO.getNom() != null && !patientUpdateDTO.getNom().equals(patient.getNom())) {
            patient.setNom(patientUpdateDTO.getNom());
            modifications = true;
        }

        if (patientUpdateDTO.getPrenom() != null && !patientUpdateDTO.getPrenom().equals(patient.getPrenom())) {
            patient.setPrenom(patientUpdateDTO.getPrenom());
            modifications = true;
        }

        if (patientUpdateDTO.getAdresse() != null && !patientUpdateDTO.getAdresse().equals(patient.getAdresse())) {
            patient.setAdresse(patientUpdateDTO.getAdresse());
            modifications = true;
        }

        if (patientUpdateDTO.getTelephone() != null && !patientUpdateDTO.getTelephone().equals(patient.getTelephone())) {
            patient.setTelephone(patientUpdateDTO.getTelephone());
            modifications = true;
        }

        if (patientUpdateDTO.getEmail() != null && !patientUpdateDTO.getEmail().equals(patient.getEmail())) {
            // Vérifier que le nouvel email n'est pas utilisé par un autre patient
            PatientDTO patientAvecEmail = searchByEmail(patientUpdateDTO.getEmail());
            if (patientAvecEmail != null && !patientAvecEmail.getId().equals(id)) {
                throw new DuplicateEmailException("Cet email est déjà utilisé par un autre patient");
            }
            patient.setEmail(patientUpdateDTO.getEmail());
            modifications = true;
        }

        if (patientUpdateDTO.getDateNaissance() != null && !patientUpdateDTO.getDateNaissance().equals(patient.getDateNaissance())) {
            patient.setDateNaissance(patientUpdateDTO.getDateNaissance());
            modifications = true;
        }

        if (patientUpdateDTO.getSexe() != null && patientUpdateDTO.getSexe() != patient.getSexe()) {
            patient.setSexe(patientUpdateDTO.getSexe());
            modifications = true;
        }

        if (patientUpdateDTO.getAssurance() != null && patientUpdateDTO.getAssurance() != patient.getAssurance()) {
            patient.setAssurance(patientUpdateDTO.getAssurance());
            modifications = true;
        }

        // Sauvegarder si modifications
        if (modifications) {
            patientRepository.update(patient);
            System.out.println("Patient mis à jour: " + id);
        }

        // Retourner le patient mis à jour
        return getPatientById(id);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id);
        if (patient == null) {
            throw new PatientNotFoundException("Patient non trouvé avec ID: " + id);
        }

        patientRepository.deleteById(id);
        System.out.println("Patient supprimé: " + id);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return getAllPatients();
        }

        List<Patient> patients = patientRepository.findByNom(nom);

        return patients.stream()
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchByPrenom(String prenom) {
        if (prenom == null || prenom.trim().isEmpty()) {
            return getAllPatients();
        }

        List<Patient> tousPatients = patientRepository.findAll();

        return tousPatients.stream()
                .filter(p -> p.getPrenom() != null && p.getPrenom().toLowerCase().contains(prenom.toLowerCase()))
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchByTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return getAllPatients();
        }

        List<Patient> tousPatients = patientRepository.findAll();

        return tousPatients.stream()
                .filter(p -> p.getTelephone() != null && p.getTelephone().contains(telephone))
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public PatientDTO searchByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        List<Patient> tousPatients = patientRepository.findAll();

        return tousPatients.stream()
                .filter(p -> p.getEmail() != null && p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<PatientDTO> searchByAssurance(Assurance assurance) {
        if (assurance == null) {
            return getAllPatients();
        }

        List<Patient> tousPatients = patientRepository.findAll();

        return tousPatients.stream()
                .filter(p -> p.getAssurance() == assurance)
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchBySexe(Sexe sexe) {
        if (sexe == null) {
            return getAllPatients();
        }

        List<Patient> tousPatients = patientRepository.findAll();

        return tousPatients.stream()
                .filter(p -> p.getSexe() == sexe)
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchByAgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            throw new IllegalArgumentException("Tranche d'âge invalide");
        }

        List<Patient> tousPatients = patientRepository.findAll();

        return tousPatients.stream()
                .filter(p -> {
                    if (p.getDateNaissance() == null) return false;
                    int age = calculateAge(p.getDateNaissance());
                    return age >= minAge && age <= maxAge;
                })
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<PatientDTO> advancedSearch(PatientSearchCriteriaDTO criteria) {
        if (criteria == null) {
            return getAllPatients();
        }

        List<Patient> tousPatients = patientRepository.findAll();

        return tousPatients.stream()
                .filter(p -> {
                    // Filtre par nom
                    if (criteria.getNom() != null && !criteria.getNom().trim().isEmpty()) {
                        if (p.getNom() == null || !p.getNom().toLowerCase().contains(criteria.getNom().toLowerCase())) {
                            return false;
                        }
                    }

                    // Filtre par prénom
                    if (criteria.getPrenom() != null && !criteria.getPrenom().trim().isEmpty()) {
                        if (p.getPrenom() == null || !p.getPrenom().toLowerCase().contains(criteria.getPrenom().toLowerCase())) {
                            return false;
                        }
                    }

                    // Filtre par téléphone
                    if (criteria.getTelephone() != null && !criteria.getTelephone().trim().isEmpty()) {
                        if (p.getTelephone() == null || !p.getTelephone().contains(criteria.getTelephone())) {
                            return false;
                        }
                    }

                    // Filtre par email
                    if (criteria.getEmail() != null && !criteria.getEmail().trim().isEmpty()) {
                        if (p.getEmail() == null || !p.getEmail().equalsIgnoreCase(criteria.getEmail())) {
                            return false;
                        }
                    }

                    // Filtre par sexe
                    if (criteria.getSexe() != null) {
                        if (p.getSexe() != criteria.getSexe()) {
                            return false;
                        }
                    }

                    // Filtre par assurance
                    if (criteria.getAssurance() != null) {
                        if (p.getAssurance() != criteria.getAssurance()) {
                            return false;
                        }
                    }

                    // Filtre par tranche d'âge
                    if (criteria.getMinAge() != null || criteria.getMaxAge() != null) {
                        if (p.getDateNaissance() == null) return false;

                        int age = calculateAge(p.getDateNaissance());

                        if (criteria.getMinAge() != null && age < criteria.getMinAge()) {
                            return false;
                        }

                        if (criteria.getMaxAge() != null && age > criteria.getMaxAge()) {
                            return false;
                        }
                    }

                    // Filtre par adresse
                    if (criteria.getAdresse() != null && !criteria.getAdresse().trim().isEmpty()) {
                        if (p.getAdresse() == null || !p.getAdresse().toLowerCase().contains(criteria.getAdresse().toLowerCase())) {
                            return false;
                        }
                    }

                    return true;
                })
                .map(patient -> {
                    PatientDTO dto = convertToPatientDTO(patient);
                    dto.setAge(calculateAge(patient.getDateNaissance()));
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public long countPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.size();
    }

    @Override
    public PatientStatisticsDTO getStatistics() {
        PatientStatisticsDTOImpl stats = new PatientStatisticsDTOImpl();

        List<Patient> patients = patientRepository.findAll();
        stats.setTotalPatients(patients.size());

        // Calcul par sexe
        long hommes = patients.stream().filter(p -> p.getSexe() == Sexe.HOMME).count();
        long femmes = patients.stream().filter(p -> p.getSexe() == Sexe.FEMME).count();
        stats.setPatientsHommes(hommes);
        stats.setPatientsFemmes(femmes);

        // Calcul âge moyen
        double totalAge = 0;
        int patientsAvecAge = 0;
        Map<Integer, Long> repartitionAge = new HashMap<>();

        for (Patient p : patients) {
            if (p.getDateNaissance() != null) {
                int age = calculateAge(p.getDateNaissance());
                totalAge += age;
                patientsAvecAge++;

                // Répartition par tranche d'âge de 10 ans
                int tranche = (age / 10) * 10;
                repartitionAge.put(tranche, repartitionAge.getOrDefault(tranche, 0L) + 1);
            }
        }

        if (patientsAvecAge > 0) {
            stats.setAgeMoyen(totalAge / patientsAvecAge);
        }

        stats.setRepartitionParAge(repartitionAge);

        // Répartition par assurance
        Map<Assurance, Long> repartitionAssurance = new HashMap<>();
        for (Patient p : patients) {
            Assurance assurance = p.getAssurance() != null ? p.getAssurance() : Assurance.Aucune;
            repartitionAssurance.put(assurance, repartitionAssurance.getOrDefault(assurance, 0L) + 1);
        }
        stats.setRepartitionAssurance(repartitionAssurance);

        // Nouveaux patients ce mois
        LocalDate debutMois = LocalDate.now().withDayOfMonth(1);
        long nouveauxCeMois = patients.stream()
                .filter(p -> p.getDateCreation() != null &&
                        p.getDateCreation().toLocalDate().isAfter(debutMois.minusDays(1)))
                .count();
        stats.setNouveauxPatientsCeMois(nouveauxCeMois);

        return stats;
    }

    @Override
    public PatientReportDTO generateReport(Long id) {
        PatientDTO patient = getPatientById(id);

        PatientReportDTOImpl report = new PatientReportDTOImpl();
        report.setId(id);
        report.setNomComplet(patient.getFullName());
        report.setDateGeneration(LocalDate.now());

        // Informations personnelles
        StringBuilder infosPerso = new StringBuilder();
        infosPerso.append("Nom: ").append(patient.getNom()).append("\n");
        infosPerso.append("Prénom: ").append(patient.getPrenom()).append("\n");
        infosPerso.append("Âge: ").append(patient.getAge() != null ? patient.getAge() + " ans" : "Non spécifié").append("\n");
        infosPerso.append("Sexe: ").append(patient.getSexe() != null ? patient.getSexe() : "Non spécifié").append("\n");
        infosPerso.append("Date de naissance: ").append(patient.getDateNaissance() != null ? patient.getDateNaissance() : "Non spécifié").append("\n");
        infosPerso.append("Email: ").append(patient.getEmail() != null ? patient.getEmail() : "Non spécifié").append("\n");
        infosPerso.append("Téléphone: ").append(patient.getTelephone() != null ? patient.getTelephone() : "Non spécifié").append("\n");
        infosPerso.append("Adresse: ").append(patient.getAdresse() != null ? patient.getAdresse() : "Non spécifié").append("\n");
        infosPerso.append("Date d'inscription: ").append(patient.getDateCreation() != null ? patient.getDateCreation().toLocalDate() : "Non spécifié").append("\n");
        infosPerso.append("Assurance: ").append(patient.getAssurance() != null ? patient.getAssurance() : "Aucune").append("\n");

        report.setInformationsPersonnelles(infosPerso.toString());

        // Informations médicales (à compléter avec vos données)
        report.setInformationsMedicales("À implémenter: dossier médical, allergies, antécédents...");

        // Historique consultations (à compléter)
        report.setHistoriqueConsultations("À implémenter: liste des consultations, traitements...");

        // Détails assurances (à compléter)
        report.setAssurancesDetails("À implémenter: détails des couvertures, mutuelles...");

        return report;
    }

    @Override
    public String exportToCSV(String filePath) throws java.io.IOException {
        List<PatientDTO> patients = getAllPatients();

        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            // En-tête
            writer.write("ID;Nom;Prénom;Âge;Sexe;Email;Téléphone;Adresse;Date Naissance;Assurance;Date Création");
            writer.newLine();

            // Données
            for (PatientDTO patient : patients) {
                StringBuilder line = new StringBuilder();
                line.append(patient.getId()).append(";");
                line.append(patient.getNom()).append(";");
                line.append(patient.getPrenom()).append(";");
                line.append(patient.getAge() != null ? patient.getAge() : "").append(";");
                line.append(patient.getSexe() != null ? patient.getSexe() : "").append(";");
                line.append(patient.getEmail() != null ? patient.getEmail() : "").append(";");
                line.append(patient.getTelephone() != null ? patient.getTelephone() : "").append(";");
                line.append(patient.getAdresse() != null ? patient.getAdresse() : "").append(";");
                line.append(patient.getDateNaissance() != null ? patient.getDateNaissance() : "").append(";");
                line.append(patient.getAssurance() != null ? patient.getAssurance() : "").append(";");
                line.append(patient.getDateCreation() != null ? patient.getDateCreation().toLocalDate() : "");

                writer.write(line.toString());
                writer.newLine();
            }
        }

        System.out.println("Export CSV terminé: " + patients.size() + " patients exportés vers " + filePath);
        return filePath;
    }

    @Override
    public boolean validatePatient(PatientCreateDTO patientDTO) {
        if (patientDTO == null) {
            return false;
        }

        // Validation du nom
        if (patientDTO.getNom() == null || patientDTO.getNom().trim().isEmpty()) {
            return false;
        }

        // Validation du prénom
        if (patientDTO.getPrenom() == null || patientDTO.getPrenom().trim().isEmpty()) {
            return false;
        }

        // Validation de l'email
        if (patientDTO.getEmail() == null || patientDTO.getEmail().trim().isEmpty()) {
            return false;
        }

        if (!patientDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }

        // Validation du téléphone
        if (patientDTO.getTelephone() == null || patientDTO.getTelephone().trim().isEmpty()) {
            return false;
        }

        if (!patientDTO.getTelephone().matches("^[0-9]{10}$")) {
            return false;
        }

        // Validation de la date de naissance
        if (patientDTO.getDateNaissance() == null) {
            return false;
        }

        if (patientDTO.getDateNaissance().isAfter(LocalDate.now())) {
            return false;
        }

        return true;
    }

    @Override
    public Integer calculateAge(LocalDate dateNaissance) {
        if (dateNaissance == null) {
            return null;
        }

        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    @Override
    public boolean emailExists(String email) {
        return searchByEmail(email) != null;
    }

    // ==================== MÉTHODES PRIVÉES ====================

    private PatientDTO convertToPatientDTO(Patient patient) {
        PatientDTOImpl dto = new PatientDTOImpl();
        dto.setId(patient.getId());
        dto.setNom(patient.getNom());
        dto.setPrenom(patient.getPrenom());
        dto.setAdresse(patient.getAdresse());
        dto.setTelephone(patient.getTelephone());
        dto.setEmail(patient.getEmail());
        dto.setDateNaissance(patient.getDateNaissance());
        dto.setDateCreation(patient.getDateCreation());
        dto.setSexe(patient.getSexe());
        dto.setAssurance(patient.getAssurance());
        return dto;
    }
}