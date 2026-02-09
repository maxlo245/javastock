package JavaStocks;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class CoureurMenu {
    private static CoureurDAO coureurDAO;

    public static void afficher(Connection connection) {
        coureurDAO = new CoureurDAO(connection);

        JFrame frame = new JFrame("Formulaire Coureur");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Titre
        JLabel titre = new JLabel("Nouveau Coureur");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titre);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Champ Nom
        JPanel panelNom = new JPanel(new BorderLayout(10, 5));
        panelNom.setMaximumSize(new Dimension(400, 60));
        JLabel lblNom = new JLabel("Nom:");
        lblNom.setPreferredSize(new Dimension(100, 25));
        JTextField txtNom = new JTextField();
        txtNom.setPreferredSize(new Dimension(250, 25));
        panelNom.add(lblNom, BorderLayout.WEST);
        panelNom.add(txtNom, BorderLayout.CENTER);
        mainPanel.add(panelNom);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Champ Prénom
        JPanel panelPrenom = new JPanel(new BorderLayout(10, 5));
        panelPrenom.setMaximumSize(new Dimension(400, 60));
        JLabel lblPrenom = new JLabel("Prénom:");
        lblPrenom.setPreferredSize(new Dimension(100, 25));
        JTextField txtPrenom = new JTextField();
        txtPrenom.setPreferredSize(new Dimension(250, 25));
        panelPrenom.add(lblPrenom, BorderLayout.WEST);
        panelPrenom.add(txtPrenom, BorderLayout.CENTER);
        mainPanel.add(panelPrenom);
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
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Le nom et le prénom sont obligatoires!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Coureur coureur = new Coureur(0, nom, prenom);
                coureurDAO.creer(coureur);

                JOptionPane.showMessageDialog(frame,
                    "Coureur ajouté avec succès!\n\nNom: " + nom + "\nPrénom: " + prenom,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser le formulaire
                txtNom.setText("");
                txtPrenom.setText("");
                txtNom.requestFocus();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'enregistrement: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Réinitialiser
        btnReinitialiser.addActionListener(e -> {
            txtNom.setText("");
            txtPrenom.setText("");
            txtNom.requestFocus();
        });

        // Action Fermer
        btnFermer.addActionListener(e -> frame.dispose());

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
