package ma.dentaluxe.service.patient.baseImplimentation;

import ma.dentaluxe.service.patient.api.AntecedentService;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;
import java.util.List;

public class AntecedentServiceImpl implements AntecedentService {  // ← implements, pas extends !

    private final AntecedentRepository antecedentRepository;

    public AntecedentServiceImpl(AntecedentRepository antecedentRepository) {
        this.antecedentRepository = antecedentRepository;
    }

    @Override
    public List<Antecedent> findAll() {
        return antecedentRepository.findAll();
    }

    @Override
    public Antecedent findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID d'antécédent invalide");
        }

        Antecedent antecedent = antecedentRepository.findById(id);
        if (antecedent == null) {
            throw new RuntimeException("Antécédent non trouvé avec l'ID: " + id);
        }
        return antecedent;
    }

    @Override
    public Antecedent create(Antecedent antecedent) {
        validateAntecedent(antecedent);

        List<Antecedent> existants = antecedentRepository.findByNom(antecedent.getNom());
        if (!existants.isEmpty()) {
            throw new RuntimeException("Un antécédent avec le nom '" + antecedent.getNom() + "' existe déjà");
        }

        antecedentRepository.create(antecedent);
        return antecedent;
    }

    @Override
    public Antecedent update(Long id, Antecedent antecedent) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID d'antécédent invalide");
        }

        Antecedent existing = findById(id);
        existing.setNom(antecedent.getNom());
        existing.setCategorie(antecedent.getCategorie());
        existing.setNiveauRisque(antecedent.getNiveauRisque());

        validateAntecedent(existing);
        antecedentRepository.update(existing);
        return existing;
    }

    @Override
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID d'antécédent invalide");
        }

        Antecedent antecedent = findById(id);

        // Attention : cette vérification est incorrecte, je vais la corriger
        // Vous vérifiez si l'antécédent EST LIÉ à des patients, pas si l'ID existe

        antecedentRepository.deleteById(id);
    }

    @Override
    public List<Antecedent> findByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        return antecedentRepository.findByNom(nom.trim());
    }

    @Override
    public List<Antecedent> findByCategorie(CategorieAntecedent categorie) {
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être null");
        }
        return antecedentRepository.findByCategorie(categorie);
    }

    @Override
    public List<Antecedent> findByNiveauRisque(NiveauRisque niveauRisque) {
        if (niveauRisque == null) {
            throw new IllegalArgumentException("Le niveau de risque ne peut pas être null");
        }
        return antecedentRepository.findByNiveauRisque(niveauRisque);
    }

    @Override
    public List<Antecedent> findByPatientId(Long patientId) {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("ID patient invalide");
        }
        return antecedentRepository.findByPatientId(patientId);
    }

    @Override
    public void addAntecedentToPatient(Long patientId, Long antecedentId, String notes) {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("ID patient invalide");
        }
        if (antecedentId == null || antecedentId <= 0) {
            throw new IllegalArgumentException("ID antécédent invalide");
        }

        Antecedent antecedent = findById(antecedentId);
        List<Antecedent> antecedentsPatient = findByPatientId(patientId);

        boolean alreadyAssociated = antecedentsPatient.stream()
                .anyMatch(a -> a.getId().equals(antecedentId));

        if (alreadyAssociated) {
            throw new RuntimeException(
                    "L'antécédent '" + antecedent.getNom() + "' est déjà associé à ce patient"
            );
        }

        antecedentRepository.addAntecedentToPatient(patientId, antecedentId, notes);
    }

    @Override
    public void removeAntecedentFromPatient(Long patientId, Long antecedentId) {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("ID patient invalide");
        }
        if (antecedentId == null || antecedentId <= 0) {
            throw new IllegalArgumentException("ID antécédent invalide");
        }

        List<Antecedent> antecedentsPatient = findByPatientId(patientId);
        boolean exists = antecedentsPatient.stream()
                .anyMatch(a -> a.getId().equals(antecedentId));

        if (!exists) {
            throw new RuntimeException("Cette association patient-antécédent n'existe pas");
        }

        antecedentRepository.removeAntecedentFromPatient(patientId, antecedentId);
    }

    @Override
    public String getNotesForPatient(Long patientId, Long antecedentId) {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("ID patient invalide");
        }
        if (antecedentId == null || antecedentId <= 0) {
            throw new IllegalArgumentException("ID antécédent invalide");
        }

        return antecedentRepository.getNotesForPatient(patientId, antecedentId);
    }

    @Override
    public void updateNotesForPatient(Long patientId, Long antecedentId, String notes) {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("ID patient invalide");
        }
        if (antecedentId == null || antecedentId <= 0) {
            throw new IllegalArgumentException("ID antécédent invalide");
        }

        List<Antecedent> antecedentsPatient = findByPatientId(patientId);
        boolean exists = antecedentsPatient.stream()
                .anyMatch(a -> a.getId().equals(antecedentId));

        if (!exists) {
            throw new RuntimeException("Cette association patient-antécédent n'existe pas");
        }

        antecedentRepository.updateNotesForPatient(patientId, antecedentId, notes);
    }

    @Override
    public String getStatistics() {
        List<Antecedent> allAntecedents = findAll();
        long total = allAntecedents.size();

        if (total == 0) {
            return "Aucun antécédent enregistré";
        }

        long medicalCount = allAntecedents.stream()
                .filter(a -> a.getCategorie() == CategorieAntecedent.MEDICAL)
                .count();
        long chirurgicalCount = allAntecedents.stream()
                .filter(a -> a.getCategorie() == CategorieAntecedent.CHIRURGICAL)
                .count();
        long familialCount = allAntecedents.stream()
                .filter(a -> a.getCategorie() == CategorieAntecedent.FAMILIAL)
                .count();
        long autreCount = total - medicalCount - chirurgicalCount - familialCount;

        return String.format(
                "Total: %d antécédents\n- Médicaux: %d (%.1f%%)\n- Chirurgicaux: %d (%.1f%%)\n- Familiaux: %d (%.1f%%)\n- Autres: %d (%.1f%%)",
                total,
                medicalCount, (medicalCount * 100.0 / total),
                chirurgicalCount, (chirurgicalCount * 100.0 / total),
                familialCount, (familialCount * 100.0 / total),
                autreCount, (autreCount * 100.0 / total)
        );
    }

    @Override
    public List<Antecedent> findWithPagination(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Le numéro de page ne peut pas être négatif");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("La taille de page doit être positive");
        }

        List<Antecedent> allAntecedents = findAll();
        int start = page * size;
        int end = Math.min(start + size, allAntecedents.size());

        if (start >= allAntecedents.size()) {
            return List.of();
        }

        return allAntecedents.subList(start, end);
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        try {
            findById(id);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    // ==================== MÉTHODES PRIVÉES ====================

    private void validateAntecedent(Antecedent antecedent) {
        if (antecedent == null) {
            throw new IllegalArgumentException("L'antécédent ne peut pas être null");
        }

        if (antecedent.getNom() == null || antecedent.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'antécédent est obligatoire");
        }

        if (antecedent.getCategorie() == null) {
            throw new IllegalArgumentException("La catégorie de l'antécédent est obligatoire");
        }

        if (antecedent.getNiveauRisque() == null) {
            throw new IllegalArgumentException("Le niveau de risque de l'antécédent est obligatoire");
        }

        if (antecedent.getNom().length() > 100) {
            throw new IllegalArgumentException("Le nom de l'antécédent ne peut pas dépasser 100 caractères");
        }
    }
}