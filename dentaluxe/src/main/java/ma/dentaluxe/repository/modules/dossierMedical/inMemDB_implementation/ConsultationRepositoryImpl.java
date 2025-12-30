package ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.enums.StatutConsultation;
import ma.dentaluxe.repository.modules.dossierMedical.api.ConsultationRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultationRepositoryImpl implements ConsultationRepository {

    @Override
    public List<Consultation> findAll() {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public Consultation findById(Long id) {
        String sql = "SELECT * FROM Consultation WHERE idConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToConsultation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Consultation consultation) {
        // On utilise les noms exacts de ta capture : idDM, idMedecin, dateConsultation, statut, observation, idRDV
        String sql = "INSERT INTO consultation (idDM, idMedecin, dateConsultation, statut, observation, idRDV) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1. idDM
            pstmt.setLong(1, consultation.getIdDM());

            // 2. idMedecin
            pstmt.setLong(2, consultation.getIdMedecin());

            // 3. dateConsultation
            pstmt.setDate(3, Date.valueOf(consultation.getDateConsultation()));

            // 4. statut
            pstmt.setString(4, consultation.getStatut().name());

            // 5. observation (C'est ici qu'il ne faut PAS mettre 'motif')
            pstmt.setString(5, consultation.getObservation());

            // 6. idRDV (On utilise setObject pour accepter la valeur NULL si pas de RDV lié)
            if (consultation.getIdRDV() != null) {
                pstmt.setLong(6, consultation.getIdRDV());
            } else {
                pstmt.setNull(6, Types.BIGINT);
            }

            pstmt.executeUpdate();

            // RÉCUPÉRATION DE L'ID : Crucial pour que l'ÉTAPE 10 (Intervention) ne plante pas
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consultation.setIdConsultation(rs.getLong(1));
                    System.out.println("DEBUG : Consultation créée avec ID : " + consultation.getIdConsultation());
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du CREATE Consultation : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Consultation consultation) {

        String sql = "UPDATE consultation " +
                "SET idRDV = ?, idDM = ?, idMedecin = ?, dateConsultation = ?, " +
                "statut = ?, observation = ? " +
                "WHERE idConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, consultation.getIdRDV(), Types.BIGINT);
            pstmt.setLong(2, consultation.getIdDM());
            pstmt.setLong(3, consultation.getIdMedecin());
            pstmt.setDate(4, Date.valueOf(consultation.getDateConsultation()));
            pstmt.setString(5, consultation.getStatut().name());
            pstmt.setString(6, consultation.getObservation());
            pstmt.setLong(7, consultation.getIdConsultation());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Consultation consultation) {
        deleteById(consultation.getIdConsultation());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Consultation WHERE idConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Consultation> findByDossierMedicalId(Long idDM) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE idDM = ? ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idDM);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByMedecinId(Long idMedecin) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE idMedecin = ? ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idMedecin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByDate(LocalDate date) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE dateConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByRDVId(Long idRDV) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE idRDV = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idRDV);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByStatut(StatutConsultation statut) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE statut = ? ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    private Consultation mapResultSetToConsultation(ResultSet rs) throws SQLException {
        return Consultation.builder()
                .idConsultation(rs.getLong("idConsultation"))
                .idRDV((Long) rs.getObject("idRDV"))
                .idDM(rs.getLong("idDM"))
                .idMedecin(rs.getLong("idMedecin"))
                .dateConsultation(rs.getDate("dateConsultation").toLocalDate())
                .statut(StatutConsultation.valueOf(rs.getString("statut")))
                .observation(rs.getString("observation"))
                .build();
    }
}