// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.certificat.baseImplementation;
import ma.dentaluxe.service.certificat.dto.CertificatDTO;
import ma.dentaluxe.entities.certificat.Certificat;
import ma.dentaluxe.repository.modules.certificat.api.CertificatRepository;
import ma.dentaluxe.service.certificat.api.CertificatService;
import ma.dentaluxe.service.certificat.exception.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class CertificatServiceImpl implements CertificatService {

    private final CertificatRepository certificatRepository;

    public CertificatServiceImpl(CertificatRepository certificatRepository) {
        this.certificatRepository = certificatRepository;
    }

    @Override
    public List<CertificatDTO> getAllCertificats() {
        return certificatRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CertificatDTO getCertificatById(Long id) {
        if (id == null) {
            throw new InvalidCertificatDataException("L'ID du certificat ne peut pas être null");
        }

        Certificat certificat = certificatRepository.findById(id);
        if (certificat == null) {
            throw new CertificatNotFoundException(id);
        }

        return convertToDTO(certificat);
    }

    @Override
    public CertificatDTO createCertificat(CertificatDTO certificatDTO) {
        // Validation complète via la méthode privée
        validateCertificatDTO(certificatDTO);

        // Calculer la durée automatiquement
        int duree = calculateDuree(certificatDTO.getDateDebut(), certificatDTO.getDateFin());
        certificatDTO.setDuree(duree);

        Certificat certificat = convertToEntity(certificatDTO);
        certificatRepository.create(certificat);

        System.out.println(" Certificat créé avec succès pour le DM ID: " + certificat.getIdDM());

        return convertToDTO(certificat);
    }

    @Override
    public CertificatDTO updateCertificat(CertificatDTO certificatDTO) {
        if (certificatDTO == null || certificatDTO.getIdCertif() == null) {
            throw new InvalidCertificatDataException("L'ID du certificat est obligatoire pour une mise à jour");
        }

        // Vérifier que le certificat existe avant de valider le reste
        Certificat existingCertificat = certificatRepository.findById(certificatDTO.getIdCertif());
        if (existingCertificat == null) {
            throw new CertificatNotFoundException(certificatDTO.getIdCertif());
        }

        validateCertificatDTO(certificatDTO);

        // Recalculer la durée
        int duree = calculateDuree(certificatDTO.getDateDebut(), certificatDTO.getDateFin());
        certificatDTO.setDuree(duree);

        Certificat certificat = convertToEntity(certificatDTO);
        certificatRepository.update(certificat);

        System.out.println("Certificat ID: " + certificat.getIdCertif() + " mis à jour");

        return convertToDTO(certificat);
    }

    @Override
    public void deleteCertificatById(Long id) {
        if (id == null) {
            throw new InvalidCertificatDataException("L'ID du certificat ne peut pas être null");
        }

        Certificat certificat = certificatRepository.findById(id);
        if (certificat == null) {
            throw new CertificatNotFoundException(id);
        }

        certificatRepository.deleteById(id);
        System.out.println("Certificat supprimé (ID: " + id + ")");
    }

    @Override
    public List<CertificatDTO> getCertificatsByDossierMedical(Long idDM) {
        if (idDM == null) {
            throw new InvalidCertificatDataException("L'ID du dossier médical est requis");
        }

        return certificatRepository.findByIdDM(idDM).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatDTO> getCertificatsByMedecin(Long idMedecin) {
        if (idMedecin == null) {
            throw new InvalidCertificatDataException("L'ID du médecin est requis");
        }

        return certificatRepository.findByIdMedecin(idMedecin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatDTO> getCertificatsByDateDebutBetween(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        return certificatRepository.findByDateDebutBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatDTO> getCertificatsByDateFinBetween(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        return certificatRepository.findByDateFinBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatDTO> getCertificatsActifsAtDate(LocalDate date) {
        if (date == null) {
            throw new InvalidCertificatDataException("La date de référence ne peut pas être null");
        }

        return certificatRepository.findAll().stream()
                .filter(c -> !c.getDateDebut().isAfter(date) && !c.getDateFin().isBefore(date))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatDTO> getCertificatsActifs() {
        return getCertificatsActifsAtDate(LocalDate.now());
    }

    @Override
    public List<CertificatDTO> getCertificatsExpires() {
        LocalDate today = LocalDate.now();
        return certificatRepository.findAll().stream()
                .filter(c -> c.getDateFin().isBefore(today))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificatDTO> getCertificatsAVenir() {
        LocalDate today = LocalDate.now();
        return certificatRepository.findAll().stream()
                .filter(c -> c.getDateDebut().isAfter(today))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCertificatValide(Long idCertificat) {
        if (idCertificat == null) {
            throw new InvalidCertificatDataException("L'ID ne peut pas être null");
        }

        Certificat certificat = certificatRepository.findById(idCertificat);
        if (certificat == null) return false;

        LocalDate today = LocalDate.now();
        return !certificat.getDateDebut().isAfter(today) && !certificat.getDateFin().isBefore(today);
    }

    @Override
    public boolean isCertificatActifAtDate(Long idCertificat, LocalDate date) {
        if (idCertificat == null || date == null) {
            throw new InvalidCertificatDataException("L'ID et la date sont requis");
        }

        Certificat certificat = certificatRepository.findById(idCertificat);
        if (certificat == null) return false;

        return !certificat.getDateDebut().isAfter(date) && !certificat.getDateFin().isBefore(date);
    }

    @Override
    public int calculateDuree(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new InvalidCertificatDatesException("Les dates de début et de fin sont obligatoires");
        }

        if (dateDebut.isAfter(dateFin)) {
            throw new InvalidCertificatDatesException("La date de début (" + dateDebut + ") ne peut pas être après la date de fin (" + dateFin + ")");
        }

        return (int) ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
    }

    @Override
    public long countAllCertificats() {
        return certificatRepository.findAll().size();
    }

    @Override
    public long countCertificatsByDossierMedical(Long idDM) {
        if (idDM == null) {
            throw new InvalidCertificatDataException("L'ID du dossier médical est requis");
        }
        return certificatRepository.findByIdDM(idDM).size();
    }

    @Override
    public long countCertificatsByMedecin(Long idMedecin) {
        if (idMedecin == null) {
            throw new InvalidCertificatDataException("L'ID du médecin est requis");
        }
        return certificatRepository.findByIdMedecin(idMedecin).size();
    }

    // ========== Méthodes de conversion DTO <-> Entity ==========

    private CertificatDTO convertToDTO(Certificat certificat) {
        if (certificat == null) return null;

        return CertificatDTO.builder()
                .idCertif(certificat.getIdCertif())
                .idDM(certificat.getIdDM())
                .idMedecin(certificat.getIdMedecin())
                .dateDebut(certificat.getDateDebut())
                .dateFin(certificat.getDateFin())
                .duree(certificat.getDuree())
                .noteMedecin(certificat.getNoteMedecin())
                .build();
    }

    private Certificat convertToEntity(CertificatDTO dto) {
        if (dto == null) return null;

        return Certificat.builder()
                .idCertif(dto.getIdCertif())
                .idDM(dto.getIdDM())
                .idMedecin(dto.getIdMedecin())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .duree(dto.getDuree())
                .noteMedecin(dto.getNoteMedecin())
                .build();
    }

    // ========== Méthodes privées de validation ==========

    private void validateCertificatDTO(CertificatDTO dto) {
        if (dto == null) {
            throw new InvalidCertificatDataException("Les données du certificat sont manquantes");
        }

        if (dto.getIdDM() == null) {
            throw new InvalidCertificatDataException("L'ID du dossier médical est obligatoire");
        }

        if (dto.getIdMedecin() == null) {
            throw new InvalidCertificatDataException("L'ID du médecin est obligatoire");
        }

        // Utilise calculateDuree qui contient déjà la validation de cohérence des dates
        calculateDuree(dto.getDateDebut(), dto.getDateFin());
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidCertificatDatesException("La plage de dates est incomplète");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidCertificatDatesException("La date de début doit être antérieure à la date de fin");
        }
    }
}