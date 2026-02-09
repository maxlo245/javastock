package JavaStocks;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoureurDAO {
    private Connection connection;

    public CoureurDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(Coureur coureur) throws SQLException {
        String sql = "INSERT INTO Coureur (nom, prenom) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, coureur.getNom());
            stmt.setString(2, coureur.getPrenom());
            stmt.executeUpdate();
        }
    }

    public Coureur consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Coureur WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Coureur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                );
            }
        }
        return null;
    }

    public void modifier(Coureur coureur) throws SQLException {
        String sql = "UPDATE Coureur SET nom = ?, prenom = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, coureur.getNom());
            stmt.setString(2, coureur.getPrenom());
            stmt.setInt(3, coureur.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM Coureur WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Coureur> lister() throws SQLException {
        List<Coureur> coureurs = new ArrayList<>();
        String sql = "SELECT * FROM Coureur";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                coureurs.add(new Coureur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                ));
            }
        }
        return coureurs;
    }
}
