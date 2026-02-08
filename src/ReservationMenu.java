package JavaStocks;
import java.sql.Connection;
import java.util.Scanner;

public class ReservationMenu {
    public static void afficher(Connection connection, Scanner scanner) {
        int choix;
        do {
            System.out.println("\n--- Menu Réservation ---");
            System.out.println("1- Créer une réservation");
            System.out.println("2- Modifier une réservation");
            System.out.println("3- Consulter une réservation");
            System.out.println("4- Annuler une réservation");
            System.out.println("5- Revenir au menu");
            System.out.println("6- Quitter");
            System.out.print("Votre choix : ");
            choix = Integer.parseInt(scanner.nextLine());
            switch (choix) {
                case 1:
                    // À compléter : création de réservation
                    System.out.println("Création de réservation (à compléter)");
                    break;
                case 2:
                    // À compléter : modification de réservation
                    System.out.println("Modification de réservation (à compléter)");
                    break;
                case 3:
                    // À compléter : consultation de réservation
                    System.out.println("Consultation de réservation (à compléter)");
                    break;
                case 4:
                    // À compléter : annulation de réservation
                    System.out.println("Annulation de réservation (à compléter)");
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
