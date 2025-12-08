// AUTEUR : AYA BAKIR
package ma.dentaluxe.service.caisse.baseImplementation;

import ma.dentaluxe.entities.finance.Facture;
import ma.dentaluxe.entities.finance.SituationFinanciere;
import ma.dentaluxe.entities.enums.StatutFacture;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;
import ma.dentaluxe.repository.modules.caisse.api.FactureRepository;
import ma.dentaluxe.repository.modules.caisse.api.SituationFinanciereRepository;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.FactureRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.SituationFinanciereRepositoryImpl;
import ma.dentaluxe.service.caisse.api.CaisseService;

import java.time.LocalDateTime;
import java.util.List;

public class CaisseServiceImpl implements CaisseService {

    private final FactureRepository factureRepository;
    private final SituationFinanciereRepository sfRepository;

    public CaisseServiceImpl() {
        this.factureRepository = new FactureRepositoryImpl();
        this.sfRepository = new SituationFinanciereRepositoryImpl();
    }

    // ========== Gestion de la Situation Financière ==========

    @Override
    public void initSituationFinanciere(Long idDossierMedical) {
        if (sfRepository.findByDossierMedicalId(idDossierMedical) != null) {
            System.out.println("Situation financière déjà existante pour ce dossier.");
            return;
        }

        SituationFinanciere sf = SituationFinanciere.builder()
                .idDM(idDossierMedical)
                .totalDesActes(0.0)
                .totalPaye(0.0)
                .resteDu(0.0)
                .creance(0.0)
                .statut(StatutSituationFinanciere.SOLDE) // Commence à l'équilibre
                .enPromo(false)
                .build();

        sfRepository.create(sf);
        System.out.println(" Situation financière initialisée pour le dossier ID: " + idDossierMedical);
    }

    @Override
    public SituationFinanciere getSituationByDossier(Long idDossierMedical) {
        return sfRepository.findByDossierMedicalId(idDossierMedical);
    }

    @Override
    public void updateSituation(SituationFinanciere sf) {
        sfRepository.update(sf);
    }

    // ========== Gestion des Factures ==========

    @Override
    public void creerFacture(Facture facture) {
        if (facture == null || facture.getIdConsultation() == null) {
            throw new IllegalArgumentException("Données de facture invalides");
        }

        // 1. Initialisation des calculs
        facture.setDateCreation(LocalDateTime.now());
        if (facture.getMontantPaye() == null) facture.setMontantPaye(0.0);

        // Calcul du reste
        double reste = facture.getTotalFacture() - facture.getMontantPaye();
        facture.setReste(reste);

        // Définition du statut
        if (reste <= 0) {
            facture.setStatut(StatutFacture.PAYEE);
            facture.setReste(0.0); // Pas de reste négatif
        } else if (facture.getMontantPaye() > 0) {
            facture.setStatut(StatutFacture.PARTIELLEMENT_PAYEE);
        } else {
            facture.setStatut(StatutFacture.EN_ATTENTE);
        }

        // 2. Sauvegarde de la facture
        factureRepository.create(facture);
        System.out.println(" Facture créée (ID: " + facture.getIdFacture() + ") - Reste: " + reste + " DH");

        // 3. Mise à jour de la Situation Financière globale
        SituationFinanciere sf = sfRepository.findById(facture.getIdSF());
        if (sf != null) {
            sf.setTotalDesActes(sf.getTotalDesActes() + facture.getTotalDesActes());
            // Utilisation de la méthode dédiée du repo pour mettre à jour le paiement
            sfRepository.updateAfterPayment(sf.getIdSF(), facture.getMontantPaye());
        }
    }

    @Override
    public Facture getFactureById(Long id) {
        Facture f = factureRepository.findById(id);
        if (f == null) throw new IllegalArgumentException("Facture introuvable");
        return f;
    }

    @Override
    public Facture getFactureByConsultation(Long idConsultation) {
        return factureRepository.findByConsultationId(idConsultation);
    }

    @Override
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    @Override
    public List<Facture> getFacturesByStatut(StatutFacture statut) {
        return factureRepository.findByStatut(statut);
    }

    // ========== Traitement des Paiements ==========

    @Override
    public void traiterPaiement(Long idFacture, double montantVerse) {
        if (montantVerse <= 0) {
            throw new IllegalArgumentException("Le montant versé doit être positif");
        }

        Facture facture = getFactureById(idFacture);

        if (facture.getStatut() == StatutFacture.PAYEE) {
            throw new IllegalStateException("Cette facture est déjà payée en totalité");
        }

        // Mise à jour de la facture
        double nouveauMontantPaye = facture.getMontantPaye() + montantVerse;
        double nouveauReste = facture.getTotalFacture() - nouveauMontantPaye;

        facture.setMontantPaye(nouveauMontantPaye);
        facture.setReste(nouveauReste);

        if (nouveauReste <= 0.01) { // Tolérance pour les flottants
            facture.setReste(0.0);
            facture.setStatut(StatutFacture.PAYEE);
        } else {
            facture.setStatut(StatutFacture.PARTIELLEMENT_PAYEE);
        }

        factureRepository.update(facture);
        System.out.println(" Paiement de " + montantVerse + " DH enregistré pour la facture " + idFacture);

        // Mise à jour de la Situation Financière
        sfRepository.updateAfterPayment(facture.getIdSF(), montantVerse);
    }

    // ========== Statistiques Financières ==========

    @Override
    public double getChiffreAffairesTotal() {
        return getAllFactures().stream()
                .mapToDouble(Facture::getMontantPaye)
                .sum();
    }

    @Override
    public double getTotalImpayes() {
        return factureRepository.calculateTotalFacturesImpayees();
    }
}