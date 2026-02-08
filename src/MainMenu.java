package JavaStocks;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Connexion à la base PostgreSQL
            Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/javastocks", "postgres", "votre_mot_de_passe"
            );
            int choix;
            do {
                System.out.println("\n--- Menu Principal ---");
                System.out.println("1- Gestion des articles");
                System.out.println("2- Gestion des coureurs");
                System.out.println("3- Gestion des types d'épreuve");
                System.out.println("4- Gestion des réservations");
                System.out.println("5- Article en rupture / réservation en attente");
                System.out.println("6- Consulter l'historique");
                System.out.println("7- Quitter");
                System.out.print("Votre choix : ");
                choix = Integer.parseInt(scanner.nextLine());
                switch (choix) {
                    case 1:
                        ArticleMenu.afficher(connection, scanner);
                        break;
                    case 2:
                        CoureurMenu.afficher(connection, scanner);
                        break;
                    case 3:
                        TypeEpreuveMenu.afficher(connection, scanner);
                        break;
                    case 4:
                        ReservationMenu.afficher(connection, scanner);
                        break;
                    case 5:
                        // À compléter : gestion des ruptures et réservations en attente
                        System.out.println("Fonctionnalité à venir.");
                        break;
                    case 6:
                        // À compléter : historique
                        System.out.println("Fonctionnalité à venir.");
                        break;
                    case 7:
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } while (choix != 7);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.close();
    }
}
