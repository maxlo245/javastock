package JavaStocks;

public class MainMenu {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            java.util.List<String> articles = new java.util.ArrayList<>();
            articles.add("T-shirt L vert (Textile) - 10");
            articles.add("Bouteille 50cl (Boisson) - 6");
            articles.add("Barre énergétique 250g (Denrée sèche) - 12");

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
                        // Gestion des articles en mémoire
                        javax.swing.JTextArea area = new javax.swing.JTextArea();
                        StringBuilder sb = new StringBuilder();
                        for (String art : articles) sb.append(art).append("\n");
                        area.setText(sb.toString());
                        area.setEditable(false);
                        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
                        javax.swing.JOptionPane.showMessageDialog(frame, scroll, "Articles (démo hors-ligne)", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 2:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Gestion des coureurs (démo hors-ligne)");
                        break;
                    case 3:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Gestion des types d'épreuve (démo hors-ligne)");
                        break;
                    case 4:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Gestion des réservations (démo hors-ligne)");
                        break;
                    case 5:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Aucune rupture détectée (démo hors-ligne)");
                        break;
                    case 6:
                        javax.swing.JOptionPane.showMessageDialog(frame, "Historique non disponible en mode démo");
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
}
