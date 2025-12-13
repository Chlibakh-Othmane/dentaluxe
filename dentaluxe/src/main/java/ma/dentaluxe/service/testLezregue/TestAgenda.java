package ma.dentaluxe.service.testLezregue;

import ma.dentaluxe.mvc.dto.RDVDTO;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.repository.modules.agenda.inMemDB_implementation.RDVRepositoryImpl;
import ma.dentaluxe.service.agenda.api.AgendaService;
import ma.dentaluxe.service.agenda.baseImplementation.AgendaServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * AUTEUR : AYA LEZREGUE
 * Classe de test complète pour AgendaService
 */
public class TestAgenda {

    private AgendaService agendaService;
    private RDVRepository rdvRepository;

    public TestAgenda() {
        this.rdvRepository = new RDVRepositoryImpl();
        this.agendaService = new AgendaServiceImpl(rdvRepository);
    }

    public void runAllTests() {
        System.out.println("\n========================================");
        System.out.println("🧪 TESTS DU SERVICE AGENDA");
        System.out.println("========================================\n");

        testCreateRDV();
        testGetRDVById();
        testGetAllRDV();
        testUpdateRDV();
        testGetRDVByDate();
        testGetRDVByMedecin();
        testGetRDVByPatient();
        testGetRDVByStatut();
        testGetRDVByDateRange();
        testConfirmerRDV();
        testAnnulerRDV();
        testTerminerRDV();
        testReporterRDV();
        testCreneauDisponible();
        testRDVDuJour();
        testRDVDeLaSemaine();
        testRDVAVenir();
        testCreneauxDisponibles();
        testStatistiques();
        testDeleteRDV();

        System.out.println("\n========================================");
        System.out.println("✅ TOUS LES TESTS AGENDA TERMINÉS");
        System.out.println("========================================\n");
    }

    private void testCreateRDV() {
        System.out.println("📝 Test 1: Création de RDV");
        try {
            // RDV aujourd'hui
            RDVDTO rdv1 = RDVDTO.builder()
                    .idDM(1L)
                    .idMedecin(1L)
                    .dateRDV(LocalDate.now())
                    .heureRDV(LocalTime.of(10, 0))
                    .motif("Consultation dentaire")
                    .statut(StatutRDV.PLANIFIE)
                    .build();
            agendaService.createRDV(rdv1);

            // RDV demain
            RDVDTO rdv2 = RDVDTO.builder()
                    .idDM(2L)
                    .idMedecin(1L)
                    .dateRDV(LocalDate.now().plusDays(1))
                    .heureRDV(LocalTime.of(14, 0))
                    .motif("Détartrage")
                    .statut(StatutRDV.CONFIRME)
                    .build();
            agendaService.createRDV(rdv2);

            // RDV dans 3 jours
            RDVDTO rdv3 = RDVDTO.builder()
                    .idDM(1L)
                    .idMedecin(2L)
                    .dateRDV(LocalDate.now().plusDays(3))
                    .heureRDV(LocalTime.of(9, 30))
                    .motif("Extraction")
                    .statut(StatutRDV.PLANIFIE)
                    .build();
            agendaService.createRDV(rdv3);

            // RDV la semaine prochaine
            RDVDTO rdv4 = RDVDTO.builder()
                    .idDM(3L)
                    .idMedecin(1L)
                    .dateRDV(LocalDate.now().plusDays(7))
                    .heureRDV(LocalTime.of(11, 0))
                    .motif("Pose couronne")
                    .statut(StatutRDV.PLANIFIE)
                    .build();
            agendaService.createRDV(rdv4);

            System.out.println("✅ Test réussi : 4 RDV créés\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetRDVById() {
        System.out.println("📝 Test 2: Récupération d'un RDV par ID");
        try {
            List<RDVDTO> rdvs = agendaService.getAllRDV();
            if (!rdvs.isEmpty()) {
                Long id = rdvs.get(0).getIdRDV();
                RDVDTO rdv = agendaService.getRDVById(id);
                System.out.println("   RDV trouvé : " + rdv.getMotif() + " le " + rdv.getDateRDV() + " à " + rdv.getHeureRDV());
                System.out.println("   Statut : " + rdv.getStatut());
                System.out.println("   Patient (DM) : " + rdv.getIdDM());
                System.out.println("   Médecin : " + rdv.getIdMedecin());
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetAllRDV() {
        System.out.println("📝 Test 3: Récupération de tous les RDV");
        try {
            List<RDVDTO> rdvs = agendaService.getAllRDV();
            System.out.println("   Nombre total de RDV : " + rdvs.size());
            rdvs.forEach(r -> System.out.println("   - " + r.getMotif() + " le " + r.getDateRDV() +
                    " à " + r.getHeureRDV() + " (" + r.getStatut() + ")"));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testUpdateRDV() {
        System.out.println("📝 Test 4: Mise à jour d'un RDV");
        try {
            List<RDVDTO> rdvs = agendaService.getAllRDV();
            if (!rdvs.isEmpty()) {
                RDVDTO rdv = rdvs.get(0);
                System.out.println("   Avant : " + rdv.getMotif() + " - " + rdv.getHeureRDV());

                rdv.setMotif("Consultation de contrôle");
                rdv.setNotes("Patient anxieux - Prévoir plus de temps");
                agendaService.updateRDV(rdv);

                RDVDTO rdvUpdated = agendaService.getRDVById(rdv.getIdRDV());
                System.out.println("   Après : " + rdvUpdated.getMotif());
                System.out.println("   Notes : " + rdvUpdated.getNotes());
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetRDVByDate() {
        System.out.println("📝 Test 5: Filtrage par date");
        try {
            LocalDate today = LocalDate.now();
            List<RDVDTO> rdvs = agendaService.getRDVByDate(today);
            System.out.println("   RDV du " + today + " : " + rdvs.size());
            rdvs.forEach(r -> System.out.println("   - " + r.getHeureRDV() + " : " + r.getMotif() + " (Médecin " + r.getIdMedecin() + ")"));

            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<RDVDTO> rdvsTomorrow = agendaService.getRDVByDate(tomorrow);
            System.out.println("\n   RDV du " + tomorrow + " : " + rdvsTomorrow.size());

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetRDVByMedecin() {
        System.out.println("📝 Test 6: Filtrage par médecin");
        try {
            List<RDVDTO> rdvsMedecin1 = agendaService.getRDVByMedecin(1L);
            System.out.println("   RDV du médecin 1 : " + rdvsMedecin1.size());
            rdvsMedecin1.forEach(r -> System.out.println("   - " + r.getDateRDV() + " à " + r.getHeureRDV() + " : " + r.getMotif()));

            List<RDVDTO> rdvsMedecin2 = agendaService.getRDVByMedecin(2L);
            System.out.println("\n   RDV du médecin 2 : " + rdvsMedecin2.size());
            rdvsMedecin2.forEach(r -> System.out.println("   - " + r.getDateRDV() + " à " + r.getHeureRDV() + " : " + r.getMotif()));

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetRDVByPatient() {
        System.out.println("📝 Test 7: Filtrage par patient");
        try {
            List<RDVDTO> rdvsPatient1 = agendaService.getRDVByPatient(1L);
            System.out.println("   RDV du patient (DM=1) : " + rdvsPatient1.size());
            rdvsPatient1.forEach(r -> System.out.println("   - " + r.getDateRDV() + " à " + r.getHeureRDV() + " : " + r.getMotif()));

            List<RDVDTO> rdvsPatient2 = agendaService.getRDVByPatient(2L);
            System.out.println("\n   RDV du patient (DM=2) : " + rdvsPatient2.size());

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetRDVByStatut() {
        System.out.println("📝 Test 8: Filtrage par statut");
        try {
            System.out.println("   Répartition par statut :");
            for (StatutRDV statut : StatutRDV.values()) {
                List<RDVDTO> rdvs = agendaService.getRDVByStatut(statut);
                if (!rdvs.isEmpty()) {
                    System.out.println("   - " + statut + " : " + rdvs.size() + " RDV");
                    rdvs.forEach(r -> System.out.println("      • " + r.getDateRDV() + " : " + r.getMotif()));
                }
            }
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetRDVByDateRange() {
        System.out.println("📝 Test 9: Filtrage par plage de dates");
        try {
            LocalDate debut = LocalDate.now();
            LocalDate fin = LocalDate.now().plusDays(7);
            List<RDVDTO> rdvs = agendaService.getRDVByDateRange(debut, fin);
            System.out.println("   RDV entre " + debut + " et " + fin + " : " + rdvs.size());
            rdvs.forEach(r -> System.out.println("   - " + r.getDateRDV() + " : " + r.getMotif() + " (" + r.getStatut() + ")"));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testConfirmerRDV() {
        System.out.println("📝 Test 10: Confirmation de RDV");
        try {
            List<RDVDTO> rdvs = agendaService.getRDVByStatut(StatutRDV.PLANIFIE);
            if (!rdvs.isEmpty()) {
                RDVDTO rdv = rdvs.get(0);
                System.out.println("   RDV : " + rdv.getMotif() + " le " + rdv.getDateRDV());
                System.out.println("   Avant : Statut = " + rdv.getStatut());

                RDVDTO rdvConfirme = agendaService.confirmerRDV(rdv.getIdRDV());
                System.out.println("   Après : Statut = " + rdvConfirme.getStatut());
                System.out.println("✅ Test réussi\n");
            } else {
                System.out.println("   ⚠️ Aucun RDV planifié à confirmer\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testAnnulerRDV() {
        System.out.println("📝 Test 11: Annulation de RDV");
        try {
            List<RDVDTO> rdvs = agendaService.getAllRDV();
            if (rdvs.size() > 1) {
                RDVDTO rdv = rdvs.get(rdvs.size() - 1);
                if (rdv.getStatut() != StatutRDV.TERMINE && rdv.getStatut() != StatutRDV.ANNULE) {
                    System.out.println("   RDV : " + rdv.getMotif() + " le " + rdv.getDateRDV());
                    System.out.println("   Avant : Statut = " + rdv.getStatut());

                    RDVDTO rdvAnnule = agendaService.annulerRDV(rdv.getIdRDV());
                    System.out.println("   Après : Statut = " + rdvAnnule.getStatut());
                    System.out.println("✅ Test réussi\n");
                } else {
                    System.out.println("   ⚠️ Le RDV ne peut pas être annulé\n");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testTerminerRDV() {
        System.out.println("📝 Test 12: Terminer un RDV");
        try {
            List<RDVDTO> rdvs = agendaService.getRDVByStatut(StatutRDV.CONFIRME);
            if (!rdvs.isEmpty()) {
                RDVDTO rdv = rdvs.get(0);
                System.out.println("   RDV : " + rdv.getMotif() + " le " + rdv.getDateRDV());
                System.out.println("   Avant : Statut = " + rdv.getStatut());

                RDVDTO rdvTermine = agendaService.terminerRDV(rdv.getIdRDV());
                System.out.println("   Après : Statut = " + rdvTermine.getStatut());
                System.out.println("✅ Test réussi\n");
            } else {
                System.out.println("   ⚠️ Aucun RDV confirmé à terminer\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testReporterRDV() {
        System.out.println("📝 Test 13: Reporter un RDV");
        try {
            List<RDVDTO> rdvs = agendaService.getRDVByStatut(StatutRDV.PLANIFIE);
            if (!rdvs.isEmpty()) {
                RDVDTO rdv = rdvs.get(0);
                System.out.println("   RDV : " + rdv.getMotif());
                System.out.println("   Avant : " + rdv.getDateRDV() + " à " + rdv.getHeureRDV());

                LocalDate nouvelleDate = LocalDate.now().plusDays(10);
                LocalTime nouvelleHeure = LocalTime.of(15, 30);

                RDVDTO rdvReporte = agendaService.reporterRDV(rdv.getIdRDV(), nouvelleDate, nouvelleHeure);
                System.out.println("   Après : " + rdvReporte.getDateRDV() + " à " + rdvReporte.getHeureRDV());
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testCreneauDisponible() {
        System.out.println("📝 Test 14: Vérification de disponibilité");
        try {
            LocalDate date = LocalDate.now().plusDays(20);
            LocalTime heure = LocalTime.of(11, 0);

            boolean disponible = agendaService.isCreneauDisponible(1L, date, heure);
            System.out.println("   Créneau " + date + " à " + heure + " (Médecin 1)");
            System.out.println("   Disponible : " + (disponible ? "✓ OUI" : "✗ NON"));

            // Tester un créneau occupé
            List<RDVDTO> rdvs = agendaService.getAllRDV();
            if (!rdvs.isEmpty()) {
                RDVDTO rdv = rdvs.get(0);
                boolean occupe = agendaService.isCreneauDisponible(
                        rdv.getIdMedecin(), rdv.getDateRDV(), rdv.getHeureRDV()
                );
                System.out.println("\n   Créneau " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() + " (Médecin " + rdv.getIdMedecin() + ")");
                System.out.println("   Disponible : " + (occupe ? "✓ OUI" : "✗ NON (occupé)"));
            }

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testRDVDuJour() {
        System.out.println("📝 Test 15: RDV du jour");
        try {
            List<RDVDTO> rdvs = agendaService.getRDVDuJour();
            System.out.println("   RDV aujourd'hui (" + LocalDate.now() + ") : " + rdvs.size());
            rdvs.forEach(r -> System.out.println("   - " + r.getHeureRDV() + " : " + r.getMotif() +
                    " (" + r.getStatut() + ") - Médecin " + r.getIdMedecin()));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testRDVDeLaSemaine() {
        System.out.println("📝 Test 16: RDV de la semaine");
        try {
            List<RDVDTO> rdvs = agendaService.getRDVDeLaSemaine();
            System.out.println("   RDV cette semaine (7 prochains jours) : " + rdvs.size());
            rdvs.forEach(r -> System.out.println("   - " + r.getDateRDV() + " à " + r.getHeureRDV() +
                    " : " + r.getMotif() + " (" + r.getStatut() + ")"));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testRDVAVenir() {
        System.out.println("📝 Test 17: RDV à venir");
        try {
            List<RDVDTO> rdvs = agendaService.getRDVAVenir(1L);
            System.out.println("   RDV à venir pour médecin 1 : " + rdvs.size());
            rdvs.forEach(r -> System.out.println("   - " + r.getDateRDV() + " à " + r.getHeureRDV() +
                    " : " + r.getMotif() + " (" + r.getStatut() + ")"));

            RDVDTO prochain = agendaService.getProchainRDV(1L);
            if (prochain != null) {
                System.out.println("\n   🔔 Prochain RDV : " + prochain.getDateRDV() + " à " +
                        prochain.getHeureRDV() + " - " + prochain.getMotif());
            } else {
                System.out.println("\n   Aucun RDV à venir");
            }

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testCreneauxDisponibles() {
        System.out.println("📝 Test 18: Créneaux disponibles");
        try {
            LocalDate date = LocalDate.now().plusDays(15);
            List<LocalTime> creneaux = agendaService.getCreneauxDisponibles(1L, date);
            System.out.println("   Créneaux disponibles le " + date + " (Médecin 1) : " + creneaux.size());
            System.out.println("   Horaires de 9h à 18h (créneaux de 30 min) :");
            System.out.println("   Premiers 10 créneaux disponibles :");
            creneaux.stream().limit(10).forEach(h -> System.out.println("   - " + h));
            System.out.println("   ...");
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testStatistiques() {
        System.out.println("📝 Test 19: Statistiques");
        try {
            System.out.println("   📊 STATISTIQUES GÉNÉRALES");
            System.out.println("   ─────────────────────────");
            System.out.println("   Nombre total de RDV : " + agendaService.getAllRDV().size());
            System.out.println("   RDV aujourd'hui : " + agendaService.countRDVToday());
            System.out.println("   RDV de la semaine : " + agendaService.getRDVDeLaSemaine().size());

            System.out.println("\n   📊 STATISTIQUES PAR MÉDECIN");
            System.out.println("   ───────────────────────────");
            System.out.println("   RDV médecin 1 : " + agendaService.countRDVByMedecin(1L));
            System.out.println("   RDV médecin 2 : " + agendaService.countRDVByMedecin(2L));

            System.out.println("\n   📊 STATISTIQUES PAR PATIENT");
            System.out.println("   ───────────────────────────");
            System.out.println("   RDV patient 1 : " + agendaService.countRDVByPatient(1L));
            System.out.println("   RDV patient 2 : " + agendaService.countRDVByPatient(2L));

            System.out.println("\n   📊 STATISTIQUES PAR STATUT");
            System.out.println("   ──────────────────────────");
            List<AgendaService.StatutStatDTO> statsStatut = agendaService.getStatistiquesByStatut();
            statsStatut.forEach(s -> System.out.println("   " + String.format("%-15s", s.getStatut()) +
                    " : " + s.getNombre() + " RDV (" + String.format("%.1f", s.getPourcentage()) + "%)"));

            System.out.println("\n   📊 STATISTIQUES DÉTAILLÉES PAR MÉDECIN");
            System.out.println("   ──────────────────────────────────────");
            List<AgendaService.MedecinStatDTO> statsMedecin = agendaService.getStatistiquesByMedecin();
            statsMedecin.forEach(s -> {
                System.out.println("   Médecin " + s.getIdMedecin() + " :");
                System.out.println("     • Total RDV : " + s.getTotalRDV());
                System.out.println("     • Confirmés : " + s.getRdvConfirmes());
                System.out.println("     • En attente : " + s.getRdvEnAttente());
                System.out.println("     • Annulés : " + s.getRdvAnnules());
                System.out.println("     • Terminés : " + s.getRdvTermines());
                System.out.println("     • Taux confirmation : " + String.format("%.1f", s.getTauxConfirmation()) + "%");
            });

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testDeleteRDV() {
        System.out.println("📝 Test 20: Suppression de RDV");
        try {
            int countAvant = agendaService.getAllRDV().size();
            System.out.println("   Nombre de RDV avant suppression : " + countAvant);

            List<RDVDTO> rdvs = agendaService.getAllRDV();
            if (!rdvs.isEmpty()) {
                RDVDTO rdvToDelete = rdvs.get(rdvs.size() - 1);
                System.out.println("   Suppression du RDV : " + rdvToDelete.getMotif() +
                        " du " + rdvToDelete.getDateRDV());

                agendaService.deleteRDV(rdvToDelete.getIdRDV());

                int countApres = agendaService.getAllRDV().size();
                System.out.println("   Nombre de RDV après suppression : " + countApres);
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        TestAgenda test = new TestAgenda();
        test.runAllTests();
    }
}