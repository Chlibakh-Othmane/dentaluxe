package ma.dentaluxe.service.certificat.api;

import ma.dentaluxe.service.certificat.dto.CertificatDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des certificats médicaux
 * Toutes les méthodes travaillent avec CertificatDTO
 */
public interface CertificatService {

    /**
     * Récupère tous les certificats
     */
    List<CertificatDTO> getAllCertificats();

    /**
     * Récupère un certificat par son ID
     */
    CertificatDTO getCertificatById(Long id);

    /**
     * Crée un nouveau certificat
     */
    CertificatDTO createCertificat(CertificatDTO certificatDTO);

    /**
     * Met à jour un certificat existant
     */
    CertificatDTO updateCertificat(CertificatDTO certificatDTO);

    /**
     * Supprime un certificat par son ID
     */
    void deleteCertificatById(Long id);

    /**
     * Récupère tous les certificats d'un dossier médical
     */
    List<CertificatDTO> getCertificatsByDossierMedical(Long idDM);

    /**
     * Récupère tous les certificats émis par un médecin
     */
    List<CertificatDTO> getCertificatsByMedecin(Long idMedecin);

    /**
     * Récupère les certificats dont la date de début est entre deux dates
     */
    List<CertificatDTO> getCertificatsByDateDebutBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Récupère les certificats dont la date de fin est entre deux dates
     */
    List<CertificatDTO> getCertificatsByDateFinBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Récupère les certificats actifs à une date donnée
     */
    List<CertificatDTO> getCertificatsActifsAtDate(LocalDate date);

    /**
     * Récupère les certificats actifs actuellement
     */
    List<CertificatDTO> getCertificatsActifs();

    /**
     * Récupère les certificats expirés
     */
    List<CertificatDTO> getCertificatsExpires();

    /**
     * Récupère les certificats à venir
     */
    List<CertificatDTO> getCertificatsAVenir();

    /**
     * Vérifie si un certificat est encore valide
     */
    boolean isCertificatValide(Long idCertificat);

    /**
     * Vérifie si un certificat est actif à une date donnée
     */
    boolean isCertificatActifAtDate(Long idCertificat, LocalDate date);

    /**
     * Calcule la durée d'un certificat en jours
     */
    int calculateDuree(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Compte le nombre total de certificats
     */
    long countAllCertificats();

    /**
     * Compte les certificats d'un dossier médical
     */
    long countCertificatsByDossierMedical(Long idDM);

    /**
     * Compte les certificats émis par un médecin
     */
    long countCertificatsByMedecin(Long idMedecin);
}
