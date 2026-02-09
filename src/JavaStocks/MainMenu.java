package JavaStocks;

public class MainMenu {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
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
                        javax.swing.JOptionPane.showMessageDialog(frame, "Gestion des articles (à implémenter)");
                        break;
                    case 2:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Gestion des coureurs (à implémenter)");
                        break;
                    case 3:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Gestion des types d'épreuve (à implémenter)");
                        break;
                    case 4:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Gestion des réservations (à implémenter)");
                        break;
                    case 5:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Fonctionnalité à venir.");
                        break;
                    case 6:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Fonctionnalité à venir.");
                        break;
                    case 7:
                        frame.dispose();
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
        });
    }
        // ...déplacé dans JavaStocks...
}
