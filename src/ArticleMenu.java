package JavaStocks;
import java.sql.Connection;
import java.util.Scanner;

public class ArticleMenu {
    public static void afficher(Connection connection, Scanner scanner) {
        int choix;
        do {
            System.out.println("\n--- Menu Article ---");
            System.out.println("1- Ajouter un article");
            System.out.println("2- Modifier un article");
            System.out.println("3- Consulter un article");
            System.out.println("4- Suppression logique");
            System.out.println("5- Revenir au menu");
            System.out.println("6- Quitter");
            System.out.print("Votre choix : ");
            choix = Integer.parseInt(scanner.nextLine());
            switch (choix) {
                case 1:
                    // À compléter : ajout d'un article
                    System.out.println("Ajout d'article (à compléter)");
                    break;
                case 2:
                    // À compléter : modification d'un article
                    System.out.println("Modification d'article (à compléter)");
                    break;
                case 3:
                    // À compléter : consultation d'un article
                    System.out.println("Consultation d'article (à compléter)");
                    break;
                case 4:
                    // À compléter : suppression logique
                    System.out.println("Suppression logique (à compléter)");
                    break;
                case 5:
                    return;
                case 6:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        } while (choix != 5);
    }
}
