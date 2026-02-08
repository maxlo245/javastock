package JavaStocks;
import java.sql.Connection;
import java.util.Scanner;

public class TypeEpreuveMenu {
    public static void afficher(Connection connection, Scanner scanner) {
        int choix;
        do {
            System.out.println("\n--- Menu Type d'épreuve ---");
            System.out.println("1- Ajouter un type d'épreuve");
            System.out.println("2- Revenir au menu");
            System.out.println("3- Quitter");
            System.out.print("Votre choix : ");
            choix = Integer.parseInt(scanner.nextLine());
            switch (choix) {
                case 1:
                    // À compléter : ajout d'un type d'épreuve
                    System.out.println("Ajout de type d'épreuve (à compléter)");
                    break;
                case 2:
                    return;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        } while (choix != 2);
    }
}
