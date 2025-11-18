package ma.dentaluxe.repository.modules.certificat.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.certificat.Certificat;
import ma.dentaluxe.repository.modules.certificat.api.CertificatRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CertificatRepositoryImpl implements CertificatRepository {

    @Override
    public List<Certificat> findAll() {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM certificat";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                certificats.add(mapResultSetToCertificat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certificats;
    }

    @Override
    public Certificat findById(Long id) {
        String sql = "SELECT * FROM certificat WHERE idCertif = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCertificat(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Certificat certificat) {
        String sql = "INSERT INTO certificat (idDM, idMedecin, dateDebut, dateFin, duree, noteMedecin) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, certificat.getIdDM());
            pstmt.setLong(2, certificat.getIdMedecin());
            pstmt.setDate(3, Date.valueOf(certificat.getDateDebut()));
            pstmt.setDate(4, Date.valueOf(certificat.getDateFin()));
            pstmt.setInt(5, certificat.getDuree());
            pstmt.setString(6, certificat.getNoteMedecin());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                certificat.setIdCertif(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Certificat certificat) {
        String sql = "UPDATE certificat SET idDM = ?, idMedecin = ?, dateDebut = ?, dateFin = ?, duree = ?, noteMedecin = ? WHERE idCertif = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, certificat.getIdDM());
            pstmt.setLong(2, certificat.getIdMedecin());
            pstmt.setDate(3, Date.valueOf(certificat.getDateDebut()));
            pstmt.setDate(4, Date.valueOf(certificat.getDateFin()));
            pstmt.setInt(5, certificat.getDuree());
            pstmt.setString(6, certificat.getNoteMedecin());
            pstmt.setLong(7, certificat.getIdCertif());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Certificat certificat) {
        deleteById(certificat.getIdCertif());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM certificat WHERE idCertif = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Certificat> findByIdDM(Long idDM) {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM certificat WHERE idDM = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idDM);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                certificats.add(mapResultSetToCertificat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certificats;
    }

    @Override
    public List<Certificat> findByIdMedecin(Long idMedecin) {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM certificat WHERE idMedecin = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idMedecin);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                certificats.add(mapResultSetToCertificat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certificats;
    }

    @Override
    public List<Certificat> findByDateDebutBetween(LocalDate startDate, LocalDate endDate) {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM certificat WHERE dateDebut BETWEEN ? AND ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                certificats.add(mapResultSetToCertificat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certificats;
    }

    @Override
    public List<Certificat> findByDateFinBetween(LocalDate startDate, LocalDate endDate) {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM certificat WHERE dateFin BETWEEN ? AND ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                certificats.add(mapResultSetToCertificat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return certificats;
    }

    private Certificat mapResultSetToCertificat(ResultSet rs) throws SQLException {
        return Certificat.builder()
                .idCertif(rs.getLong("idCertif"))
                .idDM(rs.getLong("idDM"))
                .idMedecin(rs.getLong("idMedecin"))
                .dateDebut(rs.getDate("dateDebut").toLocalDate())
                .dateFin(rs.getDate("dateFin").toLocalDate())
                .duree(rs.getInt("duree"))
                .noteMedecin(rs.getString("noteMedecin"))
                .build();
    }
}