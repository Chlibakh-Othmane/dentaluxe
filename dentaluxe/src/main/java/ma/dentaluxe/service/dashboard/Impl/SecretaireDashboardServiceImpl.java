package ma.dentaluxe.service.dashboard.Impl;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.service.dashboard.api.DashboardService;
import ma.dentaluxe.repository.modules.dashboard.api.DashboardRepository;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.repository.modules.caisse.api.FactureRepository;
import java.time.LocalDate;
import java.util.*;

public class SecretaireDashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final PatientRepository patientRepository;
    private final RDVRepository rdvRepository;
    private final FactureRepository factureRepository;

    public SecretaireDashboardServiceImpl(DashboardRepository dashboardRepository,
                                          PatientRepository patientRepository,
                                          RDVRepository rdvRepository,
                                          FactureRepository factureRepository) {
        this.dashboardRepository = dashboardRepository;
        this.patientRepository = patientRepository;
        this.rdvRepository = rdvRepository;
        this.factureRepository = factureRepository;
    }

    @Override
    public void displayDashboard(Utilisateur user) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          💼 DASHBOARD SECRÉTAIRE                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue " + user.getPrenom() + " " + user.getNom());
        System.out.println();

        Map<String, Object> stats = getStatistics(user);

        System.out.println("   📊 STATISTIQUES :");
        System.out.println("      • Patients totaux         : " + stats.get("totalPatients"));
        System.out.println("      • RDV aujourd'hui         : " + stats.get("rdvToday"));
        System.out.println("      • Factures impayées       : " + stats.get("unpaidInvoices"));
        System.out.println("      • Montant impayé          : " + stats.get("unpaidAmount") + " DH");

        System.out.println("\n   📅 RENDEZ-VOUS DU JOUR :");
        List<String> todayRDV = (List<String>) stats.get("todayAppointments");
        if (todayRDV.isEmpty()) {
            System.out.println("      • Aucun RDV aujourd'hui");
        } else {
            todayRDV.forEach(rdv -> System.out.println("      • " + rdv));
        }

        System.out.println("\n   📞 APPELS EN ATTENTE :");
        List<String> pendingCalls = getPendingTasks(user);
        if (pendingCalls.isEmpty()) {
            System.out.println("      • Aucun appel en attente");
        } else {
            pendingCalls.forEach(call -> System.out.println("      • " + call));
        }
    }

    @Override
    public Map<String, Object> getStatistics(Utilisateur user) {
        Map<String, Object> stats = new HashMap<>();

        // Patients totaux
        long totalPatients = patientRepository.findAll().size();

        // RDV aujourd'hui
        long rdvToday = rdvRepository.findByDate(LocalDate.now()).size();

        // Factures impayées
        long unpaidInvoices = factureRepository.findByStatut(
                ma.dentaluxe.entities.enums.StatutFacture.EN_ATTENTE).size();

        // Montant impayé
        double unpaidAmount = factureRepository.calculateTotalFacturesImpayees();

        // RDV du jour détaillés
        List<String> todayAppointments = rdvRepository.findByDate(LocalDate.now())
                .stream()
                .sorted((r1, r2) -> r1.getHeureRDV().compareTo(r2.getHeureRDV()))
                .limit(5)
                .map(r -> r.getHeureRDV() + " - " + r.getMotif())
                .toList();

        stats.put("totalPatients", totalPatients);
        stats.put("rdvToday", rdvToday);
        stats.put("unpaidInvoices", unpaidInvoices);
        stats.put("unpaidAmount", unpaidAmount);
        stats.put("todayAppointments", todayAppointments);

        return stats;
    }

    @Override
    public List<String> getNotifications(Utilisateur user) {
        List<String> notifications = new ArrayList<>();

        // Notifications administratives
        notifications.add("Administratif: Documents à archiver");
        notifications.add("Accueil: Nouveaux brochures disponibles");

        return notifications;
    }

    @Override
    public List<String> getPendingTasks(Utilisateur user) {
        List<String> tasks = new ArrayList<>();

        // Appels à rappeler
        tasks.add("Rappeler M. Dupont pour confirmation RDV");
        tasks.add("Contacter Mme Martin pour paiement");

        // Documents à préparer
        tasks.add("Préparer les dossiers pour demain");
        tasks.add("Archiver les anciens dossiers");

        // Factures à envoyer
        long invoicesToSend = factureRepository.findByStatut(
                ma.dentaluxe.entities.enums.StatutFacture.EN_ATTENTE).size();

        if (invoicesToSend > 0) {
            tasks.add(invoicesToSend + " facture(s) à envoyer");
        }

        return tasks;
    }

    @Override
    public List<String> getAlerts(Utilisateur user) {
        List<String> alerts = new ArrayList<>();

        // Alertes paiements
        double unpaidAmount = factureRepository.calculateTotalFacturesImpayees();
        if (unpaidAmount > 10000) {
            alerts.add("Montant impayé élevé: " + unpaidAmount + " DH");
        }

        // Alertes RDV
        long cancelledAppointments = rdvRepository.findByStatut(
                ma.dentaluxe.entities.enums.StatutRDV.ANNULE).size();

        if (cancelledAppointments > 3) {
            alerts.add(cancelledAppointments + " RDV annulés ce mois");
        }

        return alerts;
    }

    @Override
    public void refreshDashboardData(Utilisateur user) {
        System.out.println("🔄 Rafraîchissement des données du dashboard secrétaire...");
        // Implémenter la logique de rafraîchissement
    }

    @Override
    public void exportDashboardData(Utilisateur user, String format) {
        System.out.println("📤 Export des données secrétaire au format " + format + "...");
        // Implémenter l'export
    }
}