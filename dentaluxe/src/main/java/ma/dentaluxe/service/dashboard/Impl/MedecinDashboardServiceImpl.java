package ma.dentaluxe.service.dashboard.Impl;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.ConsultationRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.InterventionMedecinRepository;
import ma.dentaluxe.service.dashboard.api.DashboardService;
import ma.dentaluxe.repository.modules.dashboard.api.DashboardRepository;


import java.time.LocalDate;
import java.util.*;

public class MedecinDashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final ConsultationRepository consultationRepository;
    private final RDVRepository rdvRepository;
    private final InterventionMedecinRepository interventionRepository;

    public MedecinDashboardServiceImpl(DashboardRepository dashboardRepository,
                                       ConsultationRepository consultationRepository,
                                       RDVRepository rdvRepository,
                                       InterventionMedecinRepository interventionRepository) {
        this.dashboardRepository = dashboardRepository;
        this.consultationRepository = consultationRepository;
        this.rdvRepository = rdvRepository;
        this.interventionRepository = interventionRepository;
    }

    @Override
    public void displayDashboard(Utilisateur user) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║            🩺 DASHBOARD MÉDECIN                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue Dr. " + user.getNom() + " " + user.getPrenom());
        System.out.println();

        Map<String, Object> stats = getStatistics(user);

        System.out.println("   📊 STATISTIQUES DU JOUR :");
        System.out.println("      • Consultations du jour    : " + stats.get("consultationsToday"));
        System.out.println("      • RDV du jour              : " + stats.get("rdvToday"));
        System.out.println("      • Revenu estimé           : " + stats.get("revenueToday") + " DH");

        System.out.println("\n   📅 PROCHAINS RENDEZ-VOUS :");
        List<String> nextRDV = (List<String>) stats.get("nextAppointments");
        if (nextRDV.isEmpty()) {
            System.out.println("      • Aucun RDV à venir");
        } else {
            nextRDV.forEach(rdv -> System.out.println("      • " + rdv));
        }

        System.out.println("\n   💊 ORDONNANCES EN ATTENTE :");
        List<String> pendingPrescriptions = getPendingTasks(user);
        if (pendingPrescriptions.isEmpty()) {
            System.out.println("      • Aucune ordonnance en attente");
        } else {
            pendingPrescriptions.forEach(pres -> System.out.println("      • " + pres));
        }
    }

    @Override
    public Map<String, Object> getStatistics(Utilisateur user) {
        Map<String, Object> stats = new HashMap<>();

        Long medecinId = user.getId();

        // Consultations du jour
        long consultationsToday = consultationRepository.findByMedecinId(medecinId)
                .stream()
                .filter(c -> c.getDateConsultation().equals(LocalDate.now()))
                .count();

        // RDV du jour
        long rdvToday = rdvRepository.findByMedecinId(medecinId)
                .stream()
                .filter(r -> r.getDateRDV().equals(LocalDate.now()))
                .count();

        // Revenu estimé (basé sur les interventions)
        double revenueToday = interventionRepository.findByIdMedecin(medecinId)
                .stream()
                .mapToDouble(i -> i.getPrixIntervention())
                .sum();

        // Prochains RDV
        List<String> nextAppointments = rdvRepository.findByMedecinId(medecinId)
                .stream()
                .filter(r -> r.getDateRDV().isEqual(LocalDate.now()) || r.getDateRDV().isAfter(LocalDate.now()))
                .sorted((r1, r2) -> {
                    int dateCompare = r1.getDateRDV().compareTo(r2.getDateRDV());
                    return dateCompare != 0 ? dateCompare : r1.getHeureRDV().compareTo(r2.getHeureRDV());
                })
                .limit(5)
                .map(r -> r.getDateRDV() + " " + r.getHeureRDV() + " - " + r.getMotif())
                .toList();

        stats.put("consultationsToday", consultationsToday);
        stats.put("rdvToday", rdvToday);
        stats.put("revenueToday", revenueToday);
        stats.put("nextAppointments", nextAppointments);

        return stats;
    }

    @Override
    public List<String> getNotifications(Utilisateur user) {
        List<String> notifications = new ArrayList<>();

        // Notifications médicales
        notifications.add("Médical: Nouvelles directives sanitaires disponibles");
        notifications.add("Formation: Nouveau module de formation disponible");

        return notifications;
    }

    @Override
    public List<String> getPendingTasks(Utilisateur user) {
        List<String> tasks = new ArrayList<>();

        Long medecinId = user.getId();

        // Ordonnances à rédiger
        tasks.add("Rédiger les ordonnances pour les consultations terminées");

        // Certificats en attente
        tasks.add("Compléter les certificats médicaux");

        // Rapports à finaliser
        tasks.add("Finaliser les rapports de consultations");

        return tasks;
    }

    @Override
    public List<String> getAlerts(Utilisateur user) {
        List<String> alerts = new ArrayList<>();

        Long medecinId = user.getId();

        // Alertes patients
        long urgentCases = 0; // À implémenter
        if (urgentCases > 0) {
            alerts.add(urgentCases + " cas(s) urgent(s) nécessitant votre attention");
        }

        // Alertes rendez-vous
        long lateAppointments = rdvRepository.findByMedecinId(medecinId)
                .stream()
                .filter(r -> r.getDateRDV().isBefore(LocalDate.now()) &&
                        r.getStatut().toString().equals("PLANIFIE"))
                .count();

        if (lateAppointments > 0) {
            alerts.add(lateAppointments + " RDV en retard");
        }

        return alerts;
    }

    @Override
    public void refreshDashboardData(Utilisateur user) {
        System.out.println("🔄 Rafraîchissement des données du dashboard médecin...");
        // Implémenter la logique de rafraîchissement
    }

    @Override
    public void exportDashboardData(Utilisateur user, String format) {
        System.out.println("📤 Export des données médecin au format " + format + "...");
        // Implémenter l'export
    }
}