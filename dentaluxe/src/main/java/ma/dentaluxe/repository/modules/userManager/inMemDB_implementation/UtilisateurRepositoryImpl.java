package ma.dentaluxe.repository.modules.userManager.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.userManager.api.UtilisateurRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    // ========================
    // MAP ResultSet → Utilisateur
    // ========================
    private Utilisateur map(ResultSet rs) throws SQLException {
        return Utilisateur.builder()
                .id(rs.getLong("id"))
                .nom(rs.getString("nom"))
                .prenom(rs.getString("prenom"))
                .email(rs.getString("email"))
                .tel(rs.getString("tel"))
                .login(rs.getString("login"))
                .actif(rs.getBoolean("actif"))
                .creationDate(rs.getTimestamp("creation_date") != null ? rs.getTimestamp("creation_date").toLocalDateTime() : null)
                .lastModificationDate(rs.getTimestamp("last_modification_date") != null ? rs.getTimestamp("last_modification_date").toLocalDateTime() : null)
                .build();
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Utilisateur findById(Long id) {
        String sql = "SELECT * FROM Utilisateur WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void create(Utilisateur u) {
        String sql = "INSERT INTO Utilisateur (nom, prenom, email, login, password_hash, actif) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, u.getNom());
            pstmt.setString(2, u.getPrenom());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getLogin());
            pstmt.setString(5, u.getPasswordHash());
            pstmt.setBoolean(6, u.getActif() != null ? u.getActif() : true);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) u.setId(rs.getLong(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void update(Utilisateur u) {
        String sql = "UPDATE Utilisateur SET nom=?, prenom=?, email=?, tel=?, actif=? WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.getNom());
            pstmt.setString(2, u.getPrenom());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getTel());
            pstmt.setBoolean(5, u.getActif() != null ? u.getActif() : true);
            pstmt.setLong(6, u.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(Utilisateur u) { deleteById(u.getId()); }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Utilisateur WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public Utilisateur findByLogin(String login) {
        String sql = "SELECT * FROM Utilisateur WHERE login=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Utilisateur findByEmail(String email) {
        String sql = "SELECT * FROM Utilisateur WHERE email=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Utilisateur> findAllActive() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur WHERE actif=true";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}