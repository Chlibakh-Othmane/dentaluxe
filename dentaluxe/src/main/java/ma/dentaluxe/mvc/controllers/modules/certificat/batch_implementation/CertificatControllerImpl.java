package ma.dentaluxe.mvc.controllers.modules.certificat.batch_implementation;

import ma.dentaluxe.mvc.controllers.modules.certificat.api.CertificatController;
import ma.dentaluxe.service.certificat.api.CertificatService;
import ma.dentaluxe.service.certificat.dto.CertificatDTO;
import ma.dentaluxe.service.certificat.exception.*;

import java.time.LocalDate;
import java.util.List;

public class CertificatControllerImpl implements CertificatController {

    private final CertificatService certificatService;

    public CertificatControllerImpl(CertificatService certificatService) {
        if (certificatService == null) {
            throw new IllegalArgumentException("Le service des certificats ne peut pas être null");
        }
        this.certificatService = certificatService;
    }

    // ========== CRUD de base ==========

    @Override
    public List<CertificatDTO> getAllCertificats() {
        try {
            System.out.println(" [CONTROLLER] Récupération de tous les certificats...");
            List<CertificatDTO> certificats = certificatService.getAllCertificats();
            System.out.println(" [CONTROLLER] " + certificats.size() + " certificat(s) récupéré(s)");
            return certificats;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la récupération : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des certificats", e);
        }
    }

    @Override
    public CertificatDTO getCertificatById(Long id) {
        try {
            System.out.println(" [CONTROLLER] Recherche du certificat ID: " + id);
            CertificatDTO certificat = certificatService.getCertificatById(id);
            System.out.println(" [CONTROLLER] Certificat trouvé - Du " + certificat.getDateDebut() +
                    " au " + certificat.getDateFin() + " (" + certificat.getDuree() + " jours)");
            return certificat;
        } catch (InvalidCertificatDataException e) {
            System.err.println(" [CONTROLLER] ID invalide : " + e.getMessage());
            throw e;
        } catch (CertificatNotFoundException e) {
            System.err.println("[CONTROLLER] Certificat non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération du certificat", e);
        }
    }

    @Override
    public CertificatDTO createCertificat(CertificatDTO certificatDTO) {
        try {
            System.out.println(" [CONTROLLER] Création d'un nouveau certificat...");
            System.out.println("   Période: " + certificatDTO.getDateDebut() + " au " + certificatDTO.getDateFin());
            CertificatDTO created = certificatService.createCertificat(certificatDTO);
            System.out.println(" [CONTROLLER] Certificat créé avec succès (ID: " + created.getIdCertif() + ")");
            System.out.println("   Durée: " + created.getDuree() + " jours");
            return created;
        } catch (InvalidCertificatDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (InvalidCertificatDatesException e) {
            System.err.println(" [CONTROLLER] Dates invalides : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la création : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la création du certificat", e);
        }
    }

    @Override
    public CertificatDTO updateCertificat(CertificatDTO certificatDTO) {
        try {
            System.out.println(" [CONTROLLER] Mise à jour du certificat ID: " + certificatDTO.getIdCertif());
            CertificatDTO updated = certificatService.updateCertificat(certificatDTO);
            System.out.println(" [CONTROLLER] Certificat mis à jour avec succès");
            System.out.println("   Nouvelle période: " + updated.getDateDebut() + " au " + updated.getDateFin());
            return updated;
        } catch (InvalidCertificatDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (CertificatNotFoundException e) {
            System.err.println(" [CONTROLLER] Certificat non trouvé : " + e.getMessage());
            throw e;
        } catch (InvalidCertificatDatesException e) {
            System.err.println(" [CONTROLLER] Dates invalides : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la mise à jour : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la mise à jour du certificat", e);
        }
    }

    @Override
    public void deleteCertificatById(Long id) {
        try {
            System.out.println("[CONTROLLER] Suppression du certificat ID: " + id);
            certificatService.deleteCertificatById(id);
            System.out.println(" [CONTROLLER] Certificat supprimé avec succès");
        } catch (InvalidCertificatDataException e) {
            System.err.println(" [CONTROLLER] ID invalide : " + e.getMessage());
            throw e;
        } catch (CertificatNotFoundException e) {
            System.err.println(" [CONTROLLER] Certificat non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la suppression : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression du certificat", e);
        }
    }

    // ========== Recherche et filtres ==========

    @Override
    public List<CertificatDTO> getCertificatsByDossierMedical(Long idDM) {
        try {
            System.out.println(" [CONTROLLER] Recherche des certificats du dossier médical ID: " + idDM);
            List<CertificatDTO> certificats = certificatService.getCertificatsByDossierMedical(idDM);
            System.out.println(" [CONTROLLER] " + certificats.size() + " certificat(s) trouvé(s)");
            return certificats;
        } catch (InvalidCertificatDataException e) {
            System.err.println(" [CONTROLLER] ID dossier médical invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par dossier médical", e);
        }
    }

    @Override
    public List<CertificatDTO> getCertificatsByMedecin(Long idMedecin) {
        try {
            System.out.println("[CONTROLLER] Recherche des certificats du médecin ID: " + idMedecin);
            List<CertificatDTO> certificats = certificatService.getCertificatsByMedecin(idMedecin);
            System.out.println("[CONTROLLER] " + certificats.size() + " certificat(s) trouvé(s)");
            return certificats;
        } catch (InvalidCertificatDataException e) {
            System.err.println("[CONTROLLER] ID médecin invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par médecin", e);
        }
    }

    @Override
    public List<CertificatDTO> getCertificatsByDateDebutBetween(LocalDate startDate, LocalDate endDate) {
        try {
            System.out.println("[CONTROLLER] Recherche des certificats avec date de début entre " +
                    startDate + " et " + endDate);
            List<CertificatDTO> certificats = certificatService.getCertificatsByDateDebutBetween(startDate, endDate);
            System.out.println("[CONTROLLER] " + certificats.size() + " certificat(s) trouvé(s)");
            return certificats;
        } catch (InvalidCertificatDatesException e) {
            System.err.println("[CONTROLLER] Période invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par date de début", e);
        }
    }

    @Override
    public List<CertificatDTO> getCertificatsByDateFinBetween(LocalDate startDate, LocalDate endDate) {
        try {
            System.out.println("[CONTROLLER] Recherche des certificats avec date de fin entre " +
                    startDate + " et " + endDate);
            List<CertificatDTO> certificats = certificatService.getCertificatsByDateFinBetween(startDate, endDate);
            System.out.println("[CONTROLLER] " + certificats.size() + " certificat(s) trouvé(s)");
            return certificats;
        } catch (InvalidCertificatDatesException e) {
            System.err.println("[CONTROLLER] Période invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par date de fin", e);
        }
    }

    @Override
    public List<CertificatDTO> getCertificatsActifsAtDate(LocalDate date) {
        try {
            System.out.println("[CONTROLLER] Recherche des certificats actifs le " + date);
            List<CertificatDTO> certificats = certificatService.getCertificatsActifsAtDate(date);
            System.out.println("[CONTROLLER] " + certificats.size() + " certificat(s) actif(s)");
            return certificats;
        } catch (InvalidCertificatDataException e) {
            System.err.println("[CONTROLLER] Date invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche des certificats actifs", e);
        }
    }

    @Override
    public List<CertificatDTO> getCertificatsActifs() {
        try {
            System.out.println("[CONTROLLER] Recherche des certificats actuellement actifs...");
            List<CertificatDTO> certificats = certificatService.getCertificatsActifs();
            System.out.println("[CONTROLLER] " + certificats.size() + " certificat(s) actif(s) aujourd'hui");
            return certificats;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche des certificats actifs", e);
        }
    }

    @Override
    public List<CertificatDTO> getCertificatsExpires() {
        try {
            System.out.println("[CONTROLLER] Recherche des certificats expirés...");
            List<CertificatDTO> certificats = certificatService.getCertificatsExpires();
            System.out.println("[CONTROLLER] " + certificats.size() + " certificat(s) expiré(s)");
            return certificats;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche des certificats expirés", e);
        }
    }

    @Override
    public List<CertificatDTO> getCertificatsAVenir() {
        try {
            System.out.println("[CONTROLLER] Recherche des certificats à venir...");
            List<CertificatDTO> certificats = certificatService.getCertificatsAVenir();
            System.out.println("[CONTROLLER] " + certificats.size() + " certificat(s) à venir");
            return certificats;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche des certificats à venir", e);
        }
    }

    // ========== Validation métier ==========

    @Override
    public boolean isCertificatValide(Long idCertificat) {
        try {
            System.out.println("[CONTROLLER] Vérification de la validité du certificat ID: " + idCertificat);
            boolean valide = certificatService.isCertificatValide(idCertificat);
            if (valide) {
                System.out.println("[CONTROLLER] Le certificat est valide");
            } else {
                System.out.println("[CONTROLLER] Le certificat n'est pas valide");
            }
            return valide;
        } catch (InvalidCertificatDataException e) {
            System.err.println("[CONTROLLER] ID invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la vérification : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isCertificatActifAtDate(Long idCertificat, LocalDate date) {
        try {
            System.out.println("[CONTROLLER] Vérification si le certificat ID: " + idCertificat +
                    " est actif le " + date);
            boolean actif = certificatService.isCertificatActifAtDate(idCertificat, date);
            if (actif) {
                System.out.println("[CONTROLLER] Le certificat est actif à cette date");
            } else {
                System.out.println("[CONTROLLER] Le certificat n'est pas actif à cette date");
            }
            return actif;
        } catch (InvalidCertificatDataException e) {
            System.err.println("[CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de la vérification : " + e.getMessage());
            return false;
        }
    }

    @Override
    public int calculateDuree(LocalDate dateDebut, LocalDate dateFin) {
        try {
            System.out.println(" [CONTROLLER] Calcul de la durée entre " + dateDebut + " et " + dateFin);
            int duree = certificatService.calculateDuree(dateDebut, dateFin);
            System.out.println(" [CONTROLLER] Durée calculée : " + duree + " jour(s)");
            return duree;
        } catch (InvalidCertificatDatesException e) {
            System.err.println(" [CONTROLLER] Dates invalides : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du calcul : " + e.getMessage());
            throw new RuntimeException("Erreur lors du calcul de la durée", e);
        }
    }

    // ========== Statistiques ==========

    @Override
    public long countAllCertificats() {
        try {
            System.out.println("[CONTROLLER] Comptage de tous les certificats...");
            long count = certificatService.countAllCertificats();
            System.out.println("[CONTROLLER] Nombre total de certificats : " + count);
            return count;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage des certificats", e);
        }
    }

    @Override
    public long countCertificatsByDossierMedical(Long idDM) {
        try {
            System.out.println(" [CONTROLLER] Comptage des certificats du dossier médical ID: " + idDM);
            long count = certificatService.countCertificatsByDossierMedical(idDM);
            System.out.println("[CONTROLLER] Nombre de certificats : " + count);
            return count;
        } catch (InvalidCertificatDataException e) {
            System.err.println(" [CONTROLLER] ID dossier médical invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage par dossier médical", e);
        }
    }

    @Override
    public long countCertificatsByMedecin(Long idMedecin) {
        try {
            System.out.println("[CONTROLLER] Comptage des certificats du médecin ID: " + idMedecin);
            long count = certificatService.countCertificatsByMedecin(idMedecin);
            System.out.println("[CONTROLLER] Nombre de certificats : " + count);
            return count;
        } catch (InvalidCertificatDataException e) {
            System.err.println(" [CONTROLLER] ID médecin invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage par médecin", e);
        }
    }
}