package JavaStocks;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
    private static final String DAO = "ArticleDAO";
    private Connection connection;

    public ArticleDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(Article article) throws SQLException {
        String sql = "INSERT INTO Article (libelle, categorie, quantite, sl) VALUES (?, ?, ?, ?)";
        DbLogger.sql(DAO, "creer", sql);
        DbLogger.params(article.getLibelle(), article.getCategorie(),
                String.valueOf(article.getQuantite()), String.valueOf(article.isSuppressionLogique()));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, article.getLibelle());
            stmt.setString(2, article.getCategorie());
            stmt.setInt(3, article.getQuantite());
            stmt.setBoolean(4, article.isSuppressionLogique());
            stmt.executeUpdate();
            DbLogger.success(DAO, "creer", "Article '" + article.getLibelle() + "' créé");
        } catch (SQLException e) {
            DbLogger.error(DAO, "creer", e);
            throw e;
        }
    }

    public Article consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Article WHERE id = ?";
        DbLogger.sql(DAO, "consulter", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Article a = new Article(
                    rs.getInt("id"),
                    rs.getString("libelle"),
                    rs.getString("categorie"),
                    rs.getInt("quantite"),
                    rs.getBoolean("sl")
                );
                DbLogger.success(DAO, "consulter", "Trouvé: " + a.getLibelle());
                return a;
            }
            DbLogger.success(DAO, "consulter", "Aucun article trouvé pour id=" + id);
        } catch (SQLException e) {
            DbLogger.error(DAO, "consulter", e);
            throw e;
        }
        return null;
    }

    public void modifier(Article article) throws SQLException {
        String sql = "UPDATE Article SET libelle = ?, categorie = ?, quantite = ?, sl = ? WHERE id = ?";
        DbLogger.sql(DAO, "modifier", sql);
        DbLogger.params(article.getLibelle(), article.getCategorie(),
                String.valueOf(article.getQuantite()), String.valueOf(article.isSuppressionLogique()),
                String.valueOf(article.getId()));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, article.getLibelle());
            stmt.setString(2, article.getCategorie());
            stmt.setInt(3, article.getQuantite());
            stmt.setBoolean(4, article.isSuppressionLogique());
            stmt.setInt(5, article.getId());
            stmt.executeUpdate();
            DbLogger.success(DAO, "modifier", "Article id=" + article.getId() + " modifié");
        } catch (SQLException e) {
            DbLogger.error(DAO, "modifier", e);
            throw e;
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "UPDATE Article SET sl = TRUE WHERE id = ?";
        DbLogger.sql(DAO, "supprimer", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            DbLogger.success(DAO, "supprimer", "Article id=" + id + " supprimé (logique)");
        } catch (SQLException e) {
            DbLogger.error(DAO, "supprimer", e);
            throw e;
        }
    }

    public List<Article> lister() throws SQLException {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM Article WHERE sl = FALSE";
        DbLogger.sql(DAO, "lister", sql);
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                articles.add(new Article(
                    rs.getInt("id"),
                    rs.getString("libelle"),
                    rs.getString("categorie"),
                    rs.getInt("quantite"),
                    rs.getBoolean("sl")
                ));
            }
            DbLogger.result(DAO, "lister", articles.size());
        } catch (SQLException e) {
            DbLogger.error(DAO, "lister", e);
            throw e;
        }
        return articles;
    }
}
