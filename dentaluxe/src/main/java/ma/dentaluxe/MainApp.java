package ma.dentaluxe;


import ma.dentaluxe.conf.Db;

import java.sql.Connection;
import java.sql.SQLException;

public class MainApp
{
    public static void main( String[] args )
    {
        //ApplicationContext.getBean(PatientController.class).showRecentPatients();

        Connection connection = null;
        try {
            connection = Db.getConnection(); // Appel de votre méthode de connexion
            if (connection != null) {
                System.out.println("Connexion à la base de données établie avec succès !");

            } else {
                System.out.println("Échec de la connexion à la base de données.");
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue lors de la connexion à la base de données.");
            e.printStackTrace();
        } finally {
            Db.closeConnection();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
