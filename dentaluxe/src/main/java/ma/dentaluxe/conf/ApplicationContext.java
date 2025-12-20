package ma.dentaluxe.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationContext {
    private static final Properties props = new Properties();

    static {
        System.out.println(" Chargement de beans.properties...");
        try {
            // On essaie de charger le fichier depuis la racine du classpath
            InputStream input = ApplicationContext.class.getResourceAsStream("/config/beans.properties");

            if (input == null) {
                System.err.println(" ERREUR CRITIQUE : Le fichier '/config/beans.properties' est introuvable !");
                System.err.println(" Vérifiez que le dossier 'resources' est bien marqué comme 'Resources Root'.");
                System.err.println(" Faites 'Build > Rebuild Project' pour copier le fichier dans target/classes.");
            } else {
                props.load(input);
                System.out.println(" beans.properties chargé. Nombre de beans : " + props.size());
                input.close();
            }
        } catch (IOException e) {
            System.err.println(" Erreur I/O lors du chargement : " + e.getMessage());
        }
    }

    public static Object getBean(String beanName) {
        String className = props.getProperty(beanName);

        if (className == null) {
            throw new RuntimeException("⚠ Le bean ID '" + beanName + "' n'existe pas dans beans.properties !");
        }

        try {
            Class<?> cl = Class.forName(className);
            return cl.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(" Impossible de créer l'instance pour : " + beanName + " (Classe: " + className + ")", e);
        }
    }
}