package ma.dentaluxe.repository.modules.patient.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepositoryImpl implements PatientRepository {

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient"; // CORRIGÉ: patient au lieu de patients

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Patient findById(Long id) {
        String sql = "SELECT * FROM patient WHERE idPatient = ?"; // CORRIGÉ: patient au lieu de patients

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPatient(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Patient patient) {
        String sql = "INSERT INTO patient (nom, prenom, dateDeNaissance, sexe, adresse, telephone, email, assurance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, patient.getNom());
            pstmt.setString(2, patient.getPrenom());
            pstmt.setDate(3, patient.getDateNaissance() != null ? Date.valueOf(patient.getDateNaissance()) : null);
            pstmt.setString(4, patient.getSexe() != null ? patient.getSexe().name() : "HOMME");
            pstmt.setString(5, patient.getAdresse());
            pstmt.setString(6, patient.getTelephone());
            pstmt.setString(7, patient.getEmail());
            pstmt.setString(8, patient.getAssurance() != null ? patient.getAssurance().name() : "Aucune");

            int affectedRows = pstmt.executeUpdate();
            System.out.println("DEBUG - Lignes affectées: " + affectedRows);

            // Récupérer l'ID généré
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                long generatedId = rs.getLong(1);
                patient.setId(generatedId);
                System.out.println("DEBUG - ID généré: " + generatedId);
            } else {
                System.out.println("DEBUG - Aucun ID généré!");
            }

        } catch (SQLException e) {
            System.err.println("Erreur création patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Patient patient) {
        String sql = "UPDATE patient SET nom = ?, prenom = ?, dateDeNaissance = ?, sexe = ?, adresse = ?, telephone = ?, email = ?, assurance = ? WHERE idPatient = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getNom());
            pstmt.setString(2, patient.getPrenom());
            pstmt.setDate(3, patient.getDateNaissance() != null ? Date.valueOf(patient.getDateNaissance()) : null);
            pstmt.setString(4, patient.getSexe() != null ? patient.getSexe().name() : "HOMME");
            pstmt.setString(5, patient.getAdresse());
            pstmt.setString(6, patient.getTelephone());
            pstmt.setString(7, patient.getEmail());
            pstmt.setString(8, patient.getAssurance() != null ? patient.getAssurance().name() : "Aucune");
            pstmt.setLong(9, patient.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Patient patient) {
        if (patient != null && patient.getId() != null) {
            deleteById(patient.getId());
        } else {
            System.err.println("Impossible de supprimer: patient ou ID null");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            System.err.println("ID null pour la suppression");
            return;
        }

        String sql = "DELETE FROM patient WHERE idPatient = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Lignes affectées par la suppression: " + affectedRows); // DEBUG

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Patient> findByNom(String nom) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient WHERE nom LIKE ?"; // CORRIGÉ: patient au lieu de patients

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        return Patient.builder()
                .id(rs.getLong("idPatient"))
                .nom(rs.getString("nom"))
                .prenom(rs.getString("prenom"))
                .adresse(rs.getString("adresse"))
                .telephone(rs.getString("telephone"))
                .email(rs.getString("email"))
                .dateNaissance(rs.getDate("dateDeNaissance") != null ? rs.getDate("dateDeNaissance").toLocalDate() : null)
                .dateCreation(null) // Votre table n'a pas cette colonne
                .sexe(rs.getString("sexe") != null ? ma.dentaluxe.entities.enums.Sexe.valueOf(rs.getString("sexe")) : null)
                .assurance(rs.getString("assurance") != null ? ma.dentaluxe.entities.enums.Assurance.valueOf(rs.getString("assurance")) : null)
                .build();
    }
}