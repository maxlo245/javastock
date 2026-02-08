package JavaStocks;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
    private Connection connection;

    public ArticleDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(Article article) throws SQLException {
        String sql = "INSERT INTO Article (libelle, categorie, quantite, suppression_logique) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, article.getLibelle());
            stmt.setString(2, article.getCategorie());
            stmt.setInt(3, article.getQuantite());
            stmt.setBoolean(4, article.isSuppressionLogique());
            stmt.executeUpdate();
        }
    }

    public Article consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Article WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Article(
                    rs.getInt("id"),
                    rs.getString("libelle"),
                    rs.getString("categorie"),
                    rs.getInt("quantite"),
                    rs.getBoolean("suppression_logique")
                );
            }
        }
        return null;
    }

    public void modifier(Article article) throws SQLException {
        String sql = "UPDATE Article SET libelle = ?, categorie = ?, quantite = ?, suppression_logique = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, article.getLibelle());
            stmt.setString(2, article.getCategorie());
            stmt.setInt(3, article.getQuantite());
            stmt.setBoolean(4, article.isSuppressionLogique());
            stmt.setInt(5, article.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "UPDATE Article SET suppression_logique = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Article> lister() throws SQLException {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM Article WHERE suppression_logique = FALSE";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                articles.add(new Article(
                    rs.getInt("id"),
                    rs.getString("libelle"),
                    rs.getString("categorie"),
                    rs.getInt("quantite"),
                    rs.getBoolean("suppression_logique")
                ));
            }
        }
        return articles;
    }
}
