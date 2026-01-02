package ma.dentaluxe.repository.modules.patient.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AntecedentRepositoryImpl implements AntecedentRepository {

    // ==================== CRUD DE BASE ====================

    @Override
    public List<Antecedent> findAll() {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedents ORDER BY nom";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans findAll: " + e.getMessage());
        }
        return antecedents;
    }

    @Override
    public Antecedent findById(Long id) {
        String sql = "SELECT * FROM antecedents WHERE idAntecedent = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAntecedent(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans findById: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void create(Antecedent antecedent) {
        String sql = "INSERT INTO antecedents (nom, categorie, niveauDeRisque, idDM) VALUES (?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, antecedent.getNom());
            pstmt.setString(2, antecedent.getCategorie().name());
            pstmt.setString(3, antecedent.getNiveauRisque().name());
            pstmt.setNull(4, Types.BIGINT);

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    // Utilise le nom exact du setter de ton entité Antecedent.java
                    // Si le champ s'appelle 'id', utilise setId().
                    antecedent.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            // TRÈS IMPORTANT : printStackTrace pour voir l'erreur dans le terminal
            e.printStackTrace();
            throw new RuntimeException("Erreur SQL lors du CREATE Antecedent", e);
        }
    }


    @Override
    public void update(Antecedent antecedent) {
        String sql = "UPDATE antecedents SET nom = ?, categorie = ?, niveauDeRisque = ? WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, antecedent.getNom());
            pstmt.setString(2, antecedent.getCategorie().name());
            pstmt.setString(3, antecedent.getNiveauRisque().name());
            pstmt.setLong(4, antecedent.getId());

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Antécédent mis à jour: " + affectedRows + " ligne(s) affectée(s)");

        } catch (SQLException e) {
            System.err.println("Erreur dans update: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM antecedents WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Antécédent supprimé: " + affectedRows + " ligne(s) affectée(s)");

        } catch (SQLException e) {
            System.err.println("Erreur dans deleteById: " + e.getMessage());
        }
    }

    @Override
    public void delete(Antecedent antecedent) {
        deleteById(antecedent.getId());
    }

    // ==================== MÉTHODES SPÉCIFIQUES SIMPLIFIÉES ====================

    @Override
    public List<Antecedent> findByNom(String nom) {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedents WHERE nom LIKE ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans findByNom: " + e.getMessage());
        }
        return antecedents;
    }

    @Override
    public List<Antecedent> findByCategorie(CategorieAntecedent categorie) {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedents WHERE categorie = ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categorie.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans findByCategorie: " + e.getMessage());
        }
        return antecedents;
    }

    @Override
    public List<Antecedent> findByNiveauRisque(NiveauRisque niveauRisque) {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedents WHERE niveauDeRisque = ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, niveauRisque.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans findByNiveauRisque: " + e.getMessage());
        }
        return antecedents;
    }

    // ==================== MÉTHODES AVEC TABLES DE JOINTURE ====================
    // Si vous n'avez pas les tables de jointure, ces méthodes ne fonctionneront pas
    // Il faut soit les créer, soit modifier votre modèle de données

    @Override
    public List<Antecedent> findByPatientId(Long patientId) {
        System.err.println("ATTENTION: Méthode findByPatientId non implémentée - table patient_antecedent manquante");
        System.err.println("Soit créer la table patient_antecedent, soit modifier votre modèle de données");
        return new ArrayList<>();
    }

    @Override
    public void addAntecedentToPatient(Long patientId, Long antecedentId, String notes) {
        System.err.println("ATTENTION: Méthode addAntecedentToPatient non implémentée - table patient_antecedent manquante");
        System.err.println("Soit créer la table patient_antecedent, soit modifier votre modèle de données");
    }

    @Override
    public void removeAntecedentFromPatient(Long patientId, Long antecedentId) {
        System.err.println("ATTENTION: Méthode removeAntecedentFromPatient non implémentée - table patient_antecedent manquante");
        System.err.println("Soit créer la table patient_antecedent, soit modifier votre modèle de données");
    }

    @Override
    public String getNotesForPatient(Long patientId, Long antecedentId) {
        System.err.println("ATTENTION: Méthode getNotesForPatient non implémentée - table patient_antecedent manquante");
        return "";
    }

    @Override
    public void updateNotesForPatient(Long patientId, Long antecedentId, String notes) {
        System.err.println("ATTENTION: Méthode updateNotesForPatient non implémentée - table patient_antecedent manquante");
    }

    // ==================== MÉTHODE UTILITAIRE ====================

    private Antecedent mapResultSetToAntecedent(ResultSet rs) throws SQLException {
        return Antecedent.builder()
                .idAntecedent(rs.getLong("idAntecedent"))
                .idDM(rs.getLong("idDM"))
                .nom(rs.getString("nom"))
                .categorie(CategorieAntecedent.valueOf(rs.getString("categorie")))
                .niveauRisque(NiveauRisque.valueOf(rs.getString("niveauDeRisque")))
                .build();
    }

    // ==================== IMPLÉMENTATION DES MÉTHODES GÉNÉRIQUES ====================


}