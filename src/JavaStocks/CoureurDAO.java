package JavaStocks;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoureurDAO {
    private static final String DAO = "CoureurDAO";
    private Connection connection;

    public CoureurDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(Coureur coureur) throws SQLException {
        String sql = "INSERT INTO Coureur (nom, prenom) VALUES (?, ?)";
        DbLogger.sql(DAO, "creer", sql);
        DbLogger.params(coureur.getNom(), coureur.getPrenom());
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, coureur.getNom());
            stmt.setString(2, coureur.getPrenom());
            stmt.executeUpdate();
            DbLogger.success(DAO, "creer", "Coureur '" + coureur.getNom() + " " + coureur.getPrenom() + "' créé");
        } catch (SQLException e) {
            DbLogger.error(DAO, "creer", e);
            throw e;
        }
    }

    public Coureur consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Coureur WHERE id = ?";
        DbLogger.sql(DAO, "consulter", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Coureur c = new Coureur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                );
                DbLogger.success(DAO, "consulter", "Trouvé: " + c.getNom() + " " + c.getPrenom());
                return c;
            }
            DbLogger.success(DAO, "consulter", "Aucun coureur trouvé pour id=" + id);
        } catch (SQLException e) {
            DbLogger.error(DAO, "consulter", e);
            throw e;
        }
        return null;
    }

    public void modifier(Coureur coureur) throws SQLException {
        String sql = "UPDATE Coureur SET nom = ?, prenom = ? WHERE id = ?";
        DbLogger.sql(DAO, "modifier", sql);
        DbLogger.params(coureur.getNom(), coureur.getPrenom(), String.valueOf(coureur.getId()));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, coureur.getNom());
            stmt.setString(2, coureur.getPrenom());
            stmt.setInt(3, coureur.getId());
            stmt.executeUpdate();
            DbLogger.success(DAO, "modifier", "Coureur id=" + coureur.getId() + " modifié");
        } catch (SQLException e) {
            DbLogger.error(DAO, "modifier", e);
            throw e;
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM Coureur WHERE id = ?";
        DbLogger.sql(DAO, "supprimer", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            DbLogger.success(DAO, "supprimer", "Coureur id=" + id + " supprimé");
        } catch (SQLException e) {
            DbLogger.error(DAO, "supprimer", e);
            throw e;
        }
    }

    public List<Coureur> lister() throws SQLException {
        List<Coureur> coureurs = new ArrayList<>();
        String sql = "SELECT * FROM Coureur";
        DbLogger.sql(DAO, "lister", sql);
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                coureurs.add(new Coureur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                ));
            }
            DbLogger.result(DAO, "lister", coureurs.size());
        } catch (SQLException e) {
            DbLogger.error(DAO, "lister", e);
            throw e;
        }
        return coureurs;
    }
}
