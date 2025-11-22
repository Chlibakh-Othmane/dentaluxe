package ma.dentaluxe.repository.modules.statistiques.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.enums.CategorieStatistique;
import ma.dentaluxe.entities.statistique.Statistiques;
import ma.dentaluxe.repository.modules.statistiques.api.StatistiqueRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatistiqueRepositoryImpl implements StatistiqueRepository {

    @Override
    public List<Statistiques> findAll() {
        List<Statistiques> statistiquesList = new ArrayList<>();
        String sql = "SELECT * FROM statistiques";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                statistiquesList.add(mapResultSetToStatistiques(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiquesList;
    }

    @Override
    public Statistiques findById(Long id) {
        String sql = "SELECT * FROM statistiques WHERE idStat = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStatistiques(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Statistiques statistiques) {
        String sql = "INSERT INTO statistiques (idCabinet, nom, categorie, chiffre, dateCalcul) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, statistiques.getIdCabinet());
            pstmt.setString(2, statistiques.getNom());
            pstmt.setString(3, statistiques.getCategorie().name());
            pstmt.setDouble(4, statistiques.getChiffre());
            pstmt.setDate(5, Date.valueOf(statistiques.getDateCalcul()));
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                statistiques.setIdStat(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Statistiques statistiques) {
        String sql = "UPDATE statistiques SET idCabinet = ?, nom = ?, categorie = ?, chiffre = ?, dateCalcul = ? WHERE idStat = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, statistiques.getIdCabinet());
            pstmt.setString(2, statistiques.getNom());
            pstmt.setString(3, statistiques.getCategorie().name());
            pstmt.setDouble(4, statistiques.getChiffre());
            pstmt.setDate(5, Date.valueOf(statistiques.getDateCalcul()));
            pstmt.setLong(6, statistiques.getIdStat());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Statistiques statistiques) {
        deleteById(statistiques.getIdStat());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM statistiques WHERE idStat = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Statistiques> findByIdCabinet(Long idCabinet) {
        List<Statistiques> statistiquesList = new ArrayList<>();
        String sql = "SELECT * FROM statistiques WHERE idCabinet = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idCabinet);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                statistiquesList.add(mapResultSetToStatistiques(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiquesList;
    }

    @Override
    public List<Statistiques> findByCategorie(CategorieStatistique categorie) {
        List<Statistiques> statistiquesList = new ArrayList<>();
        String sql = "SELECT * FROM statistiques WHERE categorie = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categorie.name());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                statistiquesList.add(mapResultSetToStatistiques(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiquesList;
    }

    @Override
    public List<Statistiques> findByDateCalculBetween(LocalDate startDate, LocalDate endDate) {
        List<Statistiques> statistiquesList = new ArrayList<>();
        String sql = "SELECT * FROM statistiques WHERE dateCalcul BETWEEN ? AND ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                statistiquesList.add(mapResultSetToStatistiques(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiquesList;
    }

    @Override
    public List<Statistiques> findByNomContaining(String nom) {
        List<Statistiques> statistiquesList = new ArrayList<>();
        String sql = "SELECT * FROM statistiques WHERE nom LIKE ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                statistiquesList.add(mapResultSetToStatistiques(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiquesList;
    }

    @Override
    public Double sumChiffreByCategorieAndDateRange(CategorieStatistique categorie, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(chiffre) FROM statistiques WHERE categorie = ? AND dateCalcul BETWEEN ? AND ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categorie.name());
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public Long countByCategorieAndDateRange(CategorieStatistique categorie, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM statistiques WHERE categorie = ? AND dateCalcul BETWEEN ? AND ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categorie.name());
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private Statistiques mapResultSetToStatistiques(ResultSet rs) throws SQLException {
        return Statistiques.builder()
                .idStat(rs.getLong("idStat"))
                .idCabinet(rs.getLong("idCabinet"))
                .nom(rs.getString("nom"))
                .categorie(CategorieStatistique.valueOf(rs.getString("categorie")))
                .chiffre(rs.getDouble("chiffre"))
                .dateCalcul(rs.getDate("dateCalcul").toLocalDate())
                .build();
    }
}