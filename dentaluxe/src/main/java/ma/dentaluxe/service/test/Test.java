package ma.dentaluxe.service.test;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.service.agenda.api.AgendaService;
import ma.dentaluxe.service.agenda.baseImplementation.AgendaServiceImpl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.service.actes.api.ActeService;
import ma.dentaluxe.service.actes.baseImplementation.ActeServiceImpl;


// AUTEUR : AYA LEZREGUE


public class Test {
    public static void main(String[] args) {

        //test de agenda
        AgendaService agendaService = new AgendaServiceImpl();

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║      TEST SERVICE AGENDA - DENTALUXE          ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        // TEST 1 : Créer des RDV
        System.out.println("========== TEST 1 : Création de RDV ==========");

        RDV rdv1 = RDV.builder()
                .idDM(1L)
                .idMedecin(1L)
                .dateRDV(LocalDate.now())
                .heureRDV(LocalTime.of(9, 0))
                .motif("Consultation de contrôle")
                .statut(StatutRDV.PLANIFIE)
                .noteMedecin("yyyy")
                .build();
        agendaService.createRDV(rdv1);

        RDV rdv2 = RDV.builder()
                .idDM(2L)
                .idMedecin(1L)
                .dateRDV(LocalDate.now())
                .heureRDV(LocalTime.of(10, 0))
                .motif("Extraction dentaire")
                .statut(StatutRDV.PLANIFIE)
                .noteMedecin("yyy")
                .build();
        agendaService.createRDV(rdv2);

        // TEST 2 : Lister les RDV du jour
        System.out.println("\n========== TEST 2 : RDV du jour ==========");
        List<RDV> rdvsDuJour = agendaService.getRDVDuJour();
        System.out.println("📅 Nombre de RDV aujourd'hui : " + rdvsDuJour.size());
        for (RDV rdv : rdvsDuJour) {
            System.out.println("   - " + rdv.getHeureRDV() + " : " + rdv.getMotif() +
                    " (" + rdv.getStatut() + ")");
        }

        // TEST 3 : Confirmer un RDV
        System.out.println("\n========== TEST 3 : Confirmer RDV ==========");
        agendaService.confirmerRDV(rdv1.getIdRDV());

        // TEST 4 : Annuler un RDV
        System.out.println("\n========== TEST 4 : Annuler RDV ==========");
        agendaService.annulerRDV(rdv2.getIdRDV());

        // TEST 5 : Statistiques
        System.out.println("\n========== TEST 5 : Statistiques ==========");
        System.out.println("📊 RDV aujourd'hui : " + agendaService.countRDVToday());
        System.out.println("📊 RDV planifiés : " + agendaService.countRDVByStatut(StatutRDV.PLANIFIE));
        System.out.println("📊 RDV confirmés : " + agendaService.countRDVByStatut(StatutRDV.CONFIRME));
        System.out.println("📊 RDV annulés : " + agendaService.countRDVByStatut(StatutRDV.ANNULE));

        // TEST 6 : Vérifier disponibilité
        System.out.println("\n========== TEST 6 : Vérifier disponibilité ==========");
        boolean dispo1 = agendaService.isCreneauDisponible(1L, LocalDate.now(), LocalTime.of(14, 0));
        System.out.println("   14h00 disponible ? " + (dispo1 ? "✅ Oui" : "❌ Non"));

        boolean dispo2 = agendaService.isCreneauDisponible(1L, LocalDate.now(), LocalTime.of(9, 15));
        System.out.println("   09h15 disponible ? " + (dispo2 ? "✅ Oui" : "❌ Non (trop proche de 9h00)"));

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║          ✅ TOUS LES TESTS TERMINÉS !          ║");
        System.out.println("╚════════════════════════════════════════════════╝");



        //test de acte

        ActeService acteService = new ActeServiceImpl();
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║      TEST SERVICE ACTE - DENTALUXE            ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        try {
            // TEST 1 : Créer des actes
            System.out.println("========== TEST 1 : Création d'actes ==========");
            Acte acte1 = Acte.builder()
                    .libelle("Consultation générale")
                    .description("Consultation dentaire de contrôle")
                    .prixDeBase(250.0)
                    .categorie(CategorieActe.CONSULTATION)
                    .build();
            acteService.createActe(acte1);

            Acte acte2 = Acte.builder()
                    .libelle("Extraction dentaire simple")
                    .description("Extraction d'une dent simple")
                    .prixDeBase(300.0)
                    .categorie(CategorieActe.EXTRACTION)
                    .build();
            acteService.createActe(acte2);

            Acte acte3 = Acte.builder()
                    .libelle("Détartrage complet")
                    .description("Nettoyage et détartrage des dents")
                    .prixDeBase(400.0)
                    .categorie(CategorieActe.DETARTRAGE)
                    .build();
            acteService.createActe(acte3);

            Acte acte4 = Acte.builder()
                    .libelle("Plombage composite")
                    .description("Plombage avec résine composite")
                    .prixDeBase(500.0)
                    .categorie(CategorieActe.PLOMBAGE)
                    .build();
            acteService.createActe(acte4);

            // TEST 2 : Lister tous les actes
            System.out.println("\n========== TEST 2 : Liste de tous les actes ==========");
            List<Acte> tousLesActes = acteService.getAllActes();
            System.out.println("📋 Nombre total d'actes : " + tousLesActes.size());
            for (Acte acte : tousLesActes) {
                System.out.println("   - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH (" + acte.getCategorie() + ")");
            }

            // TEST 3 : Rechercher par catégorie
            System.out.println("\n========== TEST 3 : Actes par catégorie ==========");
            List<Acte> consultations = acteService.getActesByCategorie(CategorieActe.CONSULTATION);
            System.out.println("📋 Consultations (" + consultations.size() + ") :");
            for (Acte acte : consultations) {
                System.out.println("   - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
            }

            List<Acte> extractions = acteService.getActesByCategorie(CategorieActe.EXTRACTION);
            System.out.println("📋 Extractions (" + extractions.size() + ") :");
            for (Acte acte : extractions) {
                System.out.println("   - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
            }

            // TEST 4 : Rechercher par mot-clé
            System.out.println("\n========== TEST 4 : Recherche par mot-clé ==========");
            List<Acte> resultats = acteService.searchActesByLibelle("dentaire");
            System.out.println("🔍 Résultats pour 'dentaire' (" + resultats.size() + ") :");
            for (Acte acte : resultats) {
                System.out.println("   - " + acte.getLibelle());
            }

            // TEST 5 : Statistiques
            System.out.println("\n========== TEST 5 : Statistiques ==========");
            System.out.println("📊 Nombre total d'actes : " + acteService.countActes());
            System.out.println("📊 Prix moyen : " + String.format("%.2f", acteService.getAveragePrixActes()) + " DH");
            System.out.println("📊 Total des prix : " + acteService.getTotalPrixActes() + " DH");

            Acte plusCher = acteService.getMostExpensiveActe();
            if (plusCher != null) {
                System.out.println("💰 Acte le plus cher : " + plusCher.getLibelle() + " (" + plusCher.getPrixDeBase() + " DH)");
            }

            Acte moinsCher = acteService.getCheapestActe();
            if (moinsCher != null) {
                System.out.println("💰 Acte le moins cher : " + moinsCher.getLibelle() + " (" + moinsCher.getPrixDeBase() + " DH)");
            }

            // TEST 6 : Modifier le prix d'un acte
            System.out.println("\n========== TEST 6 : Modification de prix ==========");
            acteService.updatePrix(acte1.getIdActe(), 280.0);

            // TEST 7 : Appliquer une remise
            System.out.println("\n========== TEST 7 : Appliquer une remise ==========");
            acteService.applyDiscount(acte2.getIdActe(), 10); // 10% de remise

            // TEST 8 : Recherche par plage de prix
            System.out.println("\n========== TEST 8 : Actes par plage de prix ==========");
            List<Acte> actesPrix = acteService.getActesByPriceRange(200, 350);
            System.out.println("💵 Actes entre 200 et 350 DH (" + actesPrix.size() + ") :");
            for (Acte acte : actesPrix) {
                System.out.println("   - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
            }

            // TEST 9 : Vérifier existence
            System.out.println("\n========== TEST 9 : Vérification d'existence ==========");
            boolean existe1 = acteService.acteExists(acte1.getIdActe());
            System.out.println("Acte ID " + acte1.getIdActe() + " existe ? " + (existe1 ? "✅ Oui" : "❌ Non"));

            boolean existe2 = acteService.acteExistsByLibelle("Consultation générale");
            System.out.println("Acte 'Consultation générale' existe ? " + (existe2 ? "✅ Oui" : "❌ Non"));

            boolean existe3 = acteService.acteExistsByLibelle("Acte inexistant");
            System.out.println("Acte 'Acte inexistant' existe ? " + (existe3 ? "✅ Oui" : "❌ Non"));

            // TEST 10 : Mettre à jour un acte
            System.out.println("\n========== TEST 10 : Mise à jour d'un acte ==========");
            Acte acteToUpdate = acteService.getActeById(acte3.getIdActe());
            acteToUpdate.setDescription("Détartrage complet avec polissage");
            acteService.updateActe(acteToUpdate);

            // TEST 11 : Supprimer un acte
            System.out.println("\n========== TEST 11 : Suppression d'un acte ==========");
            acteService.deleteActe(acte4.getIdActe());
            System.out.println("📊 Nombre d'actes après suppression : " + acteService.countActes());

            // TEST 12 : Statistiques par catégorie
            System.out.println("\n========== TEST 12 : Statistiques par catégorie ==========");
            System.out.println("📊 Consultations : " + acteService.countActesByCategorie(CategorieActe.CONSULTATION));
            System.out.println("📊 Extractions : " + acteService.countActesByCategorie(CategorieActe.EXTRACTION));
            System.out.println("📊 Détartrages : " + acteService.countActesByCategorie(CategorieActe.DETARTRAGE));
            System.out.println("📊 Plombages : " + acteService.countActesByCategorie(CategorieActe.PLOMBAGE));

            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║          ✅ TOUS LES TESTS TERMINÉS !          ║");
            System.out.println("╚════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR lors des tests : " + e.getMessage());
            e.printStackTrace();
        }
        }
    }

