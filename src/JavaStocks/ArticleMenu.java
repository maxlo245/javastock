package JavaStocks;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ArticleMenu {
    private static ArticleDAO articleDAO;

    public static void afficher(Connection connection) {
        articleDAO = new ArticleDAO(connection);

        JFrame frame = new JFrame("Formulaire Article");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Titre
        JLabel titre = new JLabel("Nouveau Article");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titre);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Champ Libellé
        JPanel panelLibelle = new JPanel(new BorderLayout(10, 5));
        panelLibelle.setMaximumSize(new Dimension(400, 60));
        JLabel lblLibelle = new JLabel("Libellé:");
        lblLibelle.setPreferredSize(new Dimension(100, 25));
        JTextField txtLibelle = new JTextField();
        txtLibelle.setPreferredSize(new Dimension(250, 25));
        panelLibelle.add(lblLibelle, BorderLayout.WEST);
        panelLibelle.add(txtLibelle, BorderLayout.CENTER);
        mainPanel.add(panelLibelle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Champ Catégorie
        JPanel panelCategorie = new JPanel(new BorderLayout(10, 5));
        panelCategorie.setMaximumSize(new Dimension(400, 60));
        JLabel lblCategorie = new JLabel("Catégorie:");
        lblCategorie.setPreferredSize(new Dimension(100, 25));
        String[] categories = {"Textile", "Boisson", "Denrée Sèche"};
        JComboBox<String> cboCategorie = new JComboBox<>(categories);
        cboCategorie.setPreferredSize(new Dimension(250, 25));
        panelCategorie.add(lblCategorie, BorderLayout.WEST);
        panelCategorie.add(cboCategorie, BorderLayout.CENTER);
        mainPanel.add(panelCategorie);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Champ Quantité
        JPanel panelQuantite = new JPanel(new BorderLayout(10, 5));
        panelQuantite.setMaximumSize(new Dimension(400, 60));
        JLabel lblQuantite = new JLabel("Quantité:");
        lblQuantite.setPreferredSize(new Dimension(100, 25));
        JTextField txtQuantite = new JTextField("0");
        txtQuantite.setPreferredSize(new Dimension(250, 25));
        panelQuantite.add(lblQuantite, BorderLayout.WEST);
        panelQuantite.add(txtQuantite, BorderLayout.CENTER);
        mainPanel.add(panelQuantite);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton btnEnvoyer = new JButton("Envoyer");
        btnEnvoyer.setPreferredSize(new Dimension(120, 35));
        btnEnvoyer.setBackground(new Color(34, 139, 34));
        btnEnvoyer.setForeground(Color.WHITE);
        btnEnvoyer.setFocusPainted(false);

        JButton btnReinitialiser = new JButton("Réinitialiser");
        btnReinitialiser.setPreferredSize(new Dimension(120, 35));

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setPreferredSize(new Dimension(120, 35));

        panelBoutons.add(btnEnvoyer);
        panelBoutons.add(btnReinitialiser);
        panelBoutons.add(btnFermer);
        mainPanel.add(panelBoutons);

        // Action Envoyer
        btnEnvoyer.addActionListener(e -> {
            String libelle = txtLibelle.getText().trim();
            String categorie = (String) cboCategorie.getSelectedItem();
            String quantiteStr = txtQuantite.getText().trim();

            if (libelle.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Le libellé est obligatoire!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int quantite = Integer.parseInt(quantiteStr);
                if (quantite < 0) {
                    JOptionPane.showMessageDialog(frame, "La quantité doit être positive!",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Article article = new Article(0, libelle, categorie, quantite, false);
                articleDAO.creer(article);

                JOptionPane.showMessageDialog(frame,
                    "Article ajouté avec succès!\n\nLibellé: " + libelle +
                    "\nCatégorie: " + categorie + "\nQuantité: " + quantite,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser le formulaire
                txtLibelle.setText("");
                cboCategorie.setSelectedIndex(0);
                txtQuantite.setText("0");
                txtLibelle.requestFocus();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "La quantité doit être un nombre!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'enregistrement: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Réinitialiser
        btnReinitialiser.addActionListener(e -> {
            txtLibelle.setText("");
            cboCategorie.setSelectedIndex(0);
            txtQuantite.setText("0");
            txtLibelle.requestFocus();
        });

        // Action Fermer
        btnFermer.addActionListener(e -> frame.dispose());

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
