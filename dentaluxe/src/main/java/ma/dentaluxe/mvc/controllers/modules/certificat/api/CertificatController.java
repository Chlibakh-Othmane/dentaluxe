package ma.dentaluxe.mvc.controllers.modules.certificat.api;

import ma.dentaluxe.service.certificat.dto.CertificatDTO;
import java.time.LocalDate;
import java.util.List;

public interface CertificatController {

    // ========== CRUD de base ==========

    List<CertificatDTO> getAllCertificats();

    CertificatDTO getCertificatById(Long id);

    CertificatDTO createCertificat(CertificatDTO certificatDTO);

    CertificatDTO updateCertificat(CertificatDTO certificatDTO);

    void deleteCertificatById(Long id);

    // ========== Recherche et filtres ==========

    List<CertificatDTO> getCertificatsByDossierMedical(Long idDM);

    List<CertificatDTO> getCertificatsByMedecin(Long idMedecin);

    List<CertificatDTO> getCertificatsByDateDebutBetween(LocalDate startDate, LocalDate endDate);

    List<CertificatDTO> getCertificatsByDateFinBetween(LocalDate startDate, LocalDate endDate);

    List<CertificatDTO> getCertificatsActifsAtDate(LocalDate date);

    List<CertificatDTO> getCertificatsActifs();

    List<CertificatDTO> getCertificatsExpires();

    List<CertificatDTO> getCertificatsAVenir();

    // ========== Validation métier ==========

    boolean isCertificatValide(Long idCertificat);

    boolean isCertificatActifAtDate(Long idCertificat, LocalDate date);

    int calculateDuree(LocalDate dateDebut, LocalDate dateFin);

    // ========== Statistiques ==========

    long countAllCertificats();

    long countCertificatsByDossierMedical(Long idDM);

    long countCertificatsByMedecin(Long idMedecin);
}