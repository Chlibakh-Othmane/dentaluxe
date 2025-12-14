package ma.dentaluxe.repository.modules.caisse.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.caisse.Revenu;
import ma.dentaluxe.repository.modules.caisse.api.RevenuRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RevenuRepositoryImpl implements RevenuRepository {
    @Override
    public void create(Revenu r) {
        String sql = "INSERT INTO Revenu (titre, description, montant, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getTitre());
            ps.setString(2, r.getDescription());
            ps.setDouble(3, r.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(r.getDate()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) r.setIdRevenu(rs.getLong(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Revenu> findAll() {
        List<Revenu> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Revenu ORDER BY date DESC")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override public Revenu findById(Long id) { return null; }
    @Override public void update(Revenu revenu) {}
    @Override public void delete(Revenu revenu) {}
    @Override public void deleteById(Long id) {}

    private Revenu map(ResultSet rs) throws SQLException {
        return Revenu.builder()
                .idRevenu(rs.getLong("idRevenu"))
                .titre(rs.getString("titre"))
                .description(rs.getString("description"))
                .montant(rs.getDouble("montant"))
                .date(rs.getTimestamp("date").toLocalDateTime())
                .build();
    }
}