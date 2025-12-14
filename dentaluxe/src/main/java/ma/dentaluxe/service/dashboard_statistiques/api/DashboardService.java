package ma.dentaluxe.service.dashboard_statistiques.api;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import java.util.Map;
import java.util.List;

public interface DashboardService {

    /**
     * Afficher le dashboard selon le rôle
     */
    void displayDashboard(Utilisateur user);

    /**
     * Obtenir les statistiques du dashboard
     */
    Map<String, Object> getStatistics(Utilisateur user);

    /**
     * Obtenir les notifications
     */
    List<String> getNotifications(Utilisateur user);

    /**
     * Obtenir les tâches en attente
     */
    List<String> getPendingTasks(Utilisateur user);

    /**
     * Obtenir les alertes
     */
    List<String> getAlerts(Utilisateur user);

    /**
     * Rafraîchir les données du dashboard
     */
    void refreshDashboardData(Utilisateur user);

    /**
     * Exporter les données du dashboard
     */
    void exportDashboardData(Utilisateur user, String format);
}