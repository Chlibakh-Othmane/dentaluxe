package ma.dentaluxe.conf;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Db {
    private static Connection connection;
    private static Properties properties;

    static {
        properties = new Properties();
        //pour acceder a ressource (properties)
        // getResourceAsStream:pour lire db.properties
        try (InputStream input = Db.class.getClassLoader()
                .getResourceAsStream("config/db.properties"))
        {
            if (input == null) {
                System.out.println("Désolé, impossible de trouver db.properties");
            } else {
                //pour la lecture
                properties.load(input);
            }
            // Charger le pilote JDBC MySQL
            Class.forName(properties.getProperty("driver"));//le nom de lien de driver
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password")
            );
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}