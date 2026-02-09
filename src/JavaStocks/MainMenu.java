package JavaStocks;

import java.sql.Connection;
import java.sql.SQLException;

public class MainMenu {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DatabaseConnection.getConnection();

                javax.swing.JFrame frame = new javax.swing.JFrame("Menu Principal");
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 350);
                frame.setLocationRelativeTo(null);

                String[] options = {
                    "Gestion des articles",
                    "Gestion des coureurs",
                    "Gestion des types d'épreuve",
                    "Gestion des réservations",
                    "Article en rupture / réservation en attente",
                    "Consulter l'historique",
                    "Quitter"
                };

                javax.swing.JList<String> menuList = new javax.swing.JList<>(options);
                menuList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                menuList.setSelectedIndex(0);

                javax.swing.JButton btnValider = new javax.swing.JButton("Valider");

                btnValider.addActionListener(e -> {
                    int choix = menuList.getSelectedIndex() + 1;
                    switch (choix) {
                        case 1:
                            ArticleMenu.afficher(connection);
                            break;
                        case 2:
                            CoureurMenu.afficher(connection);
                            break;
                        case 3:
                            TypeEpreuveMenu.afficher(connection);
                            break;
                        case 4:
                            ReservationMenu.afficher(connection);
                            break;
                        case 5:
                            javax.swing.JOptionPane.showMessageDialog(frame, "Fonctionnalité à venir.");
                            break;
                        case 6:
                            javax.swing.JOptionPane.showMessageDialog(frame, "Fonctionnalité à venir.");
                            break;
                        case 7:
                            DatabaseConnection.closeConnection();
                            frame.dispose();
                            System.exit(0);
                            break;
                        default:
                            javax.swing.JOptionPane.showMessageDialog(frame, "Choix invalide.");
                    }
                });

                javax.swing.JPanel panel = new javax.swing.JPanel();
                panel.setLayout(new java.awt.BorderLayout());
                panel.add(new javax.swing.JLabel("\n--- Menu Principal ---", javax.swing.SwingConstants.CENTER), java.awt.BorderLayout.NORTH);
                panel.add(menuList, java.awt.BorderLayout.CENTER);
                panel.add(btnValider, java.awt.BorderLayout.SOUTH);

                frame.setContentPane(panel);
                frame.setVisible(true);

            } catch (SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Erreur de connexion à la base de données: " + ex.getMessage(),
                    "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
