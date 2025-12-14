package ma.dentaluxe.service.dashboard_statistiques.Impl;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.service.dashboard_statistiques.api.DashboardService;
import ma.dentaluxe.repository.modules.dashboard.api.DashboardRepository;
import ma.dentaluxe.repository.modules.auth.api.AuthRepository;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import java.util.*;

public class AdminDashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final AuthRepository authRepository;
    private final PatientRepository patientRepository;

    public AdminDashboardServiceImpl(DashboardRepository dashboardRepository,
                                     AuthRepository authRepository,
                                     PatientRepository patientRepository) {
        this.dashboardRepository = dashboardRepository;
        this.authRepository = authRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public void displayDashboard(Utilisateur user) {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║            ⚙ DASHBOARD ADMIN                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("   Bienvenue Administrateur " + user.getNom());
        System.out.println();

        Map<String, Object> stats = getStatistics(user);

        System.out.println("   📊 STATISTIQUES GLOBALES :");
        System.out.println("      • Utilisateurs actifs    : " + stats.get("activeUsers"));
        System.out.println("      • Médecins              : " + stats.get("medecins"));
        System.out.println("      • Secrétaires           : " + stats.get("secretaires"));
        System.out.println("      • Patients              : " + stats.get("patients"));
        System.out.println("      • CA total              : " + stats.get("caTotal") + " DH");
        System.out.println("      • Créances              : " + stats.get("creances") + " DH");

        System.out.println("\n   🚨 ALERTES :");
        List<String> alerts = getAlerts(user);
        if (alerts.isEmpty()) {
            System.out.println("      • Aucune alerte");
        } else {
            alerts.forEach(alert -> System.out.println("      • " + alert));
        }

        System.out.println("\n   📋 TÂCHES EN ATTENTE :");
        List<String> tasks = getPendingTasks(user);
        if (tasks.isEmpty()) {
            System.out.println("      • Aucune tâche en attente");
        } else {
            tasks.forEach(task -> System.out.println("      • " + task));
        }
    }

    @Override
    public Map<String, Object> getStatistics(Utilisateur user) {
        Map<String, Object> stats = new HashMap<>();

        // Compter les utilisateurs actifs
        List<Utilisateur> allUsers = authRepository.findAll();
        long activeUsers = allUsers.stream()
                .filter(Utilisateur::getActif)
                .count();

        // Compter par rôle (méthode basique)
        long medecins = allUsers.stream()
                .filter(u -> {
                    String role = authRepository.getUserRole(u.getId());
                    return "MEDECIN".equals(role);
                })
                .count();

        long secretaires = allUsers.stream()
                .filter(u -> {
                    String role = authRepository.getUserRole(u.getId());
                    return "SECRETAIRE".equals(role);
                })
                .count();

        stats.put("activeUsers", activeUsers);
        stats.put("medecins", medecins);
        stats.put("secretaires", secretaires);
        stats.put("patients", patientRepository.findAll().size());
        stats.put("caTotal", dashboardRepository.calculateCATotal());
        stats.put("creances", 0.0); // À implémenter

        return stats;
    }

    @Override
    public List<String> getNotifications(Utilisateur user) {
        List<String> notifications = new ArrayList<>();

        // Notifications système
        notifications.add("Système: Toutes les fonctionnalités opérationnelles");
        notifications.add("Sécurité: Aucune tentative de connexion suspecte");

        return notifications;
    }

    @Override
    public List<String> getPendingTasks(Utilisateur user) {
        List<String> tasks = new ArrayList<>();

        // Tâches administratives
        List<Utilisateur> users = authRepository.findAll();
        long inactiveUsers = users.stream()
                .filter(u -> !u.getActif())
                .count();

        if (inactiveUsers > 0) {
            tasks.add(inactiveUsers + " utilisateur(s) inactif(s) à examiner");
        }

        // Vérifier les comptes sans rôle
        long usersWithoutRole = users.stream()
                .filter(u -> {
                    String role = authRepository.getUserRole(u.getId());
                    return role == null || role.isEmpty();
                })
                .count();

        if (usersWithoutRole > 0) {
            tasks.add(usersWithoutRole + " utilisateur(s) sans rôle assigné");
        }

        return tasks;
    }

    @Override
    public List<String> getAlerts(Utilisateur user) {
        List<String> alerts = new ArrayList<>();

        // Alertes de sécurité
        long failedLogins = 0; // À implémenter avec un système de logs
        if (failedLogins > 5) {
            alerts.add(failedLogins + " tentatives de connexion échouées");
        }

        // Alertes système
        double systemLoad = 0.0; // À implémenter
        if (systemLoad > 80.0) {
            alerts.add("Charge système élevée: " + systemLoad + "%");
        }

        return alerts;
    }

    @Override
    public void refreshDashboardData(Utilisateur user) {
        System.out.println("🔄 Rafraîchissement des données du dashboard admin...");
        // Implémenter la logique de rafraîchissement
    }

    @Override
    public void exportDashboardData(Utilisateur user, String format) {
        System.out.println("📤 Export des données dashboard au format " + format + "...");
        // Implémenter l'export
    }
}