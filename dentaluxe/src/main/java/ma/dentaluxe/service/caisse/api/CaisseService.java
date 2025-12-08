// AUTEUR : AYA BAKIR
package ma.dentaluxe.service.caisse.api;

import ma.dentaluxe.entities.finance.Facture;
import ma.dentaluxe.entities.finance.SituationFinanciere;
import ma.dentaluxe.entities.enums.StatutFacture;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;

import java.util.List;

public interface CaisseService {

    // ========== Gestion de la Situation Financière ==========
    void initSituationFinanciere(Long idDossierMedical);
    SituationFinanciere getSituationByDossier(Long idDossierMedical);
    void updateSituation(SituationFinanciere sf);

    // ========== Gestion des Factures ==========
    void creerFacture(Facture facture);
    Facture getFactureById(Long id);
    Facture getFactureByConsultation(Long idConsultation);
    List<Facture> getAllFactures();
    List<Facture> getFacturesByStatut(StatutFacture statut);

    // ========== Traitement des Paiements ==========
    /**
     * Enregistre un paiement pour une facture donnée.
     * Met à jour la facture (montant payé, reste, statut)
     * ET la situation financière globale du patient.
     */
    void traiterPaiement(Long idFacture, double montantVerse);

    // ========== Statistiques Financières ==========
    double getChiffreAffairesTotal();
    double getTotalImpayes();
}