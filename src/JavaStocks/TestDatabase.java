package JavaStocks;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import JavaStocks.CoureurDAO;
import JavaStocks.Coureur;
import JavaStocks.ArticleDAO;
import JavaStocks.Article;
import JavaStocks.TypeEpreuveDAO;
import JavaStocks.TypeEpreuve;
public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("=== Test de la base de donnees javastock ===\n");
        try {
            System.out.println("[Test 1] Connexion a la base...");
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("OK - Connexion reussie!\n");
            System.out.println("[Test 2] Test CRUD Coureur...");
            CoureurDAO coureurDAO = new CoureurDAO(conn);
            Coureur c = new Coureur(0, "Dupont", "Jean");
            coureurDAO.creer(c);
            System.out.println("  - Coureur cree");
            List<Coureur> coureurs = coureurDAO.lister();
            System.out.println("  - Nombre de coureurs: " + coureurs.size());
            if (!coureurs.isEmpty()) {
                Coureur premier = coureurs.get(0);
                System.out.println("  - Premier coureur: " + premier.getNom() + " " + premier.getPrenom());
                premier.setNom("Martin");
                coureurDAO.modifier(premier);
                System.out.println("  - Coureur modifie");
                Coureur consulte = coureurDAO.consulter(premier.getId());
                System.out.println("  - Coureur consulte: " + consulte.getNom());
            }
            System.out.println("OK - CRUD Coureur fonctionnel!\n");
            System.out.println("[Test 3] Test CRUD Article...");
            ArticleDAO articleDAO = new ArticleDAO(conn);
            Article a = new Article(0, "Chaussures de course", "Textile", 10, false);
            articleDAO.creer(a);
            System.out.println("  - Article cree");
            List<Article> articles = articleDAO.lister();
            System.out.println("  - Nombre d'articles: " + articles.size());
            System.out.println("OK - CRUD Article fonctionnel!\n");
            System.out.println("[Test 4] Test CRUD TypeEpreuve...");
            TypeEpreuveDAO typeDAO = new TypeEpreuveDAO(conn);
            TypeEpreuve t = new TypeEpreuve(0, "Marathon");
            typeDAO.creer(t);
            System.out.println("  - Type epreuve cree");
            List<TypeEpreuve> types = typeDAO.lister();
            System.out.println("  - Nombre de types: " + types.size());
            System.out.println("OK - CRUD TypeEpreuve fonctionnel!\n");
            System.out.println("=== Tous les tests sont reussis! ===");
            DatabaseConnection.closeConnection();
        } catch (SQLException e) {
            System.err.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
