package ma.dentaluxe.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap; // Utilisation de ConcurrentHashMap
import java.util.Map;
import java.util.Properties;

public class ApplicationContext {
    private static final Properties props = new Properties();

    // Amélioration 1 : ConcurrentHashMap pour éviter les bugs si deux threads
    // appellent getBean en même temps (ex: chargement arrière-plan)
    private static final Map<String, Object> instances = new ConcurrentHashMap<>();

    static {
        try (InputStream input = ApplicationContext.class.getResourceAsStream("/config/beans.properties")) {
            if (input == null) {
                System.err.println(" ERREUR CRITIQUE : '/config/beans.properties' introuvable !");
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println(" Erreur I/O : " + e.getMessage());
        }
    }

    // Amélioration 2 : Utilisation des "Generics" <T>
    // Cela permet d'écrire : PatientService s = getBean("patientService", PatientService.class);
    // Au lieu de : PatientService s = (PatientService) getBean("patientService");
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName, Class<T> requiredType) {
        Object bean = getBean(beanName);
        if (requiredType.isInstance(bean)) {
            return (T) bean;
        }
        throw new RuntimeException("Le bean '" + beanName + "' n'est pas de type " + requiredType.getName());
    }

    public static Object getBean(String beanName) {
        if (instances.containsKey(beanName)) {
            return instances.get(beanName);
        }

        synchronized (instances) {
            if (instances.containsKey(beanName)) return instances.get(beanName);

            String className = props.getProperty(beanName);
            if (className == null) {
                throw new RuntimeException("⚠ Le bean ID '" + beanName + "' n'existe pas dans beans.properties !");
            }

            try {
                // --- RECTIFICATION : AJOUT DE .trim() ---
                Class<?> cl = Class.forName(className.trim());

                Object instance = cl.getDeclaredConstructor().newInstance();
                instances.put(beanName, instance);
                return instance;
            } catch (Exception e) {
                // On affiche la cause réelle pour aider au débug
                e.printStackTrace();
                throw new RuntimeException(" Erreur lors de la création de : " + beanName + " (Classe: " + className + ")", e);
            }
        }
    }
}