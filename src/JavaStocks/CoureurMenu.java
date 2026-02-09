package JavaStocks;
import java.sql.Connection;
import java.util.Scanner;

public class CoureurMenu {
    public static void afficher(Connection connection, Scanner scanner) {
        int choix;
        do {
            System.out.println("\n--- Menu Coureur ---");
            System.out.println("1- Ajouter un coureur");
            System.out.println("2- Revenir au menu");
            System.out.println("3- Quitter");
            System.out.print("Votre choix : ");
            choix = Integer.parseInt(scanner.nextLine());
            switch (choix) {
                case 1:
                    // À compléter : ajout d'un coureur
                    System.out.println("Ajout de coureur (à compléter)");
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
        // ...déplacé dans JavaStocks...
}
