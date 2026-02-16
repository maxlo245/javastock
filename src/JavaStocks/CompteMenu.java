package JavaStocks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Écran "Mon Compte" – consultation, modification, changement de mot de passe
 * et suppression du compte utilisateur.
 */
public class CompteMenu {

    /** Callback déclenché quand le compte est supprimé (pour retour au login) */
    public interface CompteCallback {
        void onCompteSupprime();
    }

    public static void afficher(Connection connection, Utilisateur utilisateur, CompteCallback callback) {
        UtilisateurDAO dao = new UtilisateurDAO(connection);

        JFrame frame = new JFrame("Mon Compte – " + utilisateur.getNomComplet());
        frame.setSize(540, 750);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(480, 600));

        // === Panel principal (scrollable) ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(MainMenu.DARK);

        // === Header ===
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(MainMenu.DARK);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel lblIcon = new JLabel("\uD83D\uDC64");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("Mon Compte");
        lblTitle.setFont(MainMenu.TITLE_FONT);
        lblTitle.setForeground(MainMenu.TEXT_LIGHT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        String roleText = utilisateur.isAdmin() ? "Administrateur \uD83D\uDC51" : "Utilisateur";
        JLabel lblRole = new JLabel(roleText);
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblRole.setForeground(new Color(149, 165, 166));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblIcon);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(lblTitle);
        header.add(Box.createRigidArea(new Dimension(0, 4)));
        header.add(lblRole);

        // === Carte centrale ===
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setBackground(MainMenu.DARK);
        cardWrapper.setBorder(BorderFactory.createEmptyBorder(5, 35, 15, 35));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(MainMenu.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 0, 4, 0);

        // --- Section Informations ---
        JLabel lblInfoHeader = new JLabel("\uD83D\uDCCB Informations personnelles");
        lblInfoHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfoHeader.setForeground(MainMenu.PRIMARY);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(lblInfoHeader, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 0, 4, 8);

        // Nom
        JLabel lblNomLabel = createFieldLabel("Nom");
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.3;
        card.add(lblNomLabel, gbc);

        JTextField txtNom = createField(utilisateur.getNom());
        gbc.gridx = 1; gbc.weightx = 0.7;
        card.add(txtNom, gbc);

        // Prénom
        JLabel lblPrenomLabel = createFieldLabel("Prénom");
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.3;
        card.add(lblPrenomLabel, gbc);

        JTextField txtPrenom = createField(utilisateur.getPrenom());
        gbc.gridx = 1; gbc.weightx = 0.7;
        card.add(txtPrenom, gbc);

        // Nom d'utilisateur (lecture seule)
        JLabel lblUserLabel = createFieldLabel("Identifiant");
        gbc.gridy = 3; gbc.gridx = 0;
        card.add(lblUserLabel, gbc);

        JTextField txtUser = createField(utilisateur.getNomUtilisateur());
        txtUser.setEditable(false);
        txtUser.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        card.add(txtUser, gbc);

        // Date de création (lecture seule)
        JLabel lblDateLabel = createFieldLabel("Membre depuis");
        gbc.gridy = 4; gbc.gridx = 0;
        card.add(lblDateLabel, gbc);

        String dateStr = utilisateur.getDateCreation() != null
            ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(utilisateur.getDateCreation())
            : "—";
        JTextField txtDate = createField(dateStr);
        txtDate.setEditable(false);
        txtDate.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        card.add(txtDate, gbc);

        // Bouton sauvegarder les infos
        JButton btnSaveInfo = MainMenu.createStyledButton("  Enregistrer les modifications  ", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 0, 16, 0);
        card.add(btnSaveInfo, gbc);

        // --- Séparateur ---
        JPanel sep1 = new JPanel();
        sep1.setBackground(new Color(220, 220, 220));
        sep1.setPreferredSize(new Dimension(0, 1));
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        gbc.gridy = 6; gbc.gridwidth = 2; gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(sep1, gbc);

        // --- Section Mot de passe ---
        JLabel lblPassHeader = new JLabel("\uD83D\uDD12 Changer le mot de passe");
        lblPassHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassHeader.setForeground(MainMenu.WARNING);
        gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(lblPassHeader, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 0, 4, 8);

        JLabel lblOldPass = createFieldLabel("Ancien mdp");
        gbc.gridy = 8; gbc.gridx = 0; gbc.weightx = 0.3;
        card.add(lblOldPass, gbc);

        JPasswordField txtOldPass = createPasswordField();
        gbc.gridx = 1; gbc.weightx = 0.7;
        card.add(txtOldPass, gbc);

        JLabel lblNewPass = createFieldLabel("Nouveau mdp");
        gbc.gridy = 9; gbc.gridx = 0;
        card.add(lblNewPass, gbc);

        JPasswordField txtNewPass = createPasswordField();
        gbc.gridx = 1;
        card.add(txtNewPass, gbc);

        JLabel lblConfirmPass = createFieldLabel("Confirmer");
        gbc.gridy = 10; gbc.gridx = 0;
        card.add(lblConfirmPass, gbc);

        JPasswordField txtConfirmPass = createPasswordField();
        gbc.gridx = 1;
        card.add(txtConfirmPass, gbc);

        JButton btnChangePass = MainMenu.createStyledButton("  Changer le mot de passe  ", MainMenu.WARNING, new Color(211, 136, 16));
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(btnChangePass, gbc);

        cardWrapper.add(card, BorderLayout.CENTER);
        cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        // === Footer : Zone danger ===
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(MainMenu.DARK);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 35, 15, 35));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel dangerZone = new JPanel(new BorderLayout());
        dangerZone.setBackground(new Color(60, 30, 30));
        dangerZone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainMenu.DANGER, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JPanel dangerLeft = new JPanel();
        dangerLeft.setLayout(new BoxLayout(dangerLeft, BoxLayout.Y_AXIS));
        dangerLeft.setOpaque(false);

        JLabel lblDangerTitle = new JLabel("\u26A0\uFE0F Zone danger");
        lblDangerTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDangerTitle.setForeground(MainMenu.DANGER);

        JLabel lblDangerDesc = new JLabel("Supprimer définitivement votre compte");
        lblDangerDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDangerDesc.setForeground(new Color(180, 150, 150));

        dangerLeft.add(lblDangerTitle);
        dangerLeft.add(Box.createRigidArea(new Dimension(0, 2)));
        dangerLeft.add(lblDangerDesc);

        JButton btnDelete = new JButton("Supprimer mon compte");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBackground(MainMenu.DANGER);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setOpaque(true);
        btnDelete.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnDelete.setBackground(new Color(192, 57, 43)); }
            public void mouseExited(MouseEvent e)  { btnDelete.setBackground(MainMenu.DANGER); }
        });

        JPanel dangerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        dangerRight.setOpaque(false);
        dangerRight.add(btnDelete);

        dangerZone.add(dangerLeft, BorderLayout.CENTER);
        dangerZone.add(dangerRight, BorderLayout.EAST);

        // Bouton retour
        JPanel footerBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        footerBottom.setOpaque(false);

        JButton btnRetour = MainMenu.createStyledButton("  \u2190 Retour au dashboard  ", MainMenu.DARK_LIGHT, new Color(66, 89, 114));
        footerBottom.add(btnRetour);

        JPanel footerContent = new JPanel();
        footerContent.setLayout(new BoxLayout(footerContent, BoxLayout.Y_AXIS));
        footerContent.setOpaque(false);
        footerContent.add(dangerZone);
        footerContent.add(footerBottom);

        footer.add(footerContent, BorderLayout.CENTER);

        // === Actions ===

        // Sauvegarder les informations
        btnSaveInfo.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Le nom et le prénom sont obligatoires.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                utilisateur.setNom(nom);
                utilisateur.setPrenom(prenom);
                dao.modifier(utilisateur);
                frame.setTitle("Mon Compte – " + utilisateur.getNomComplet());
                JOptionPane.showMessageDialog(frame, "Informations mises à jour avec succès !",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Changer le mot de passe
        btnChangePass.addActionListener(e -> {
            String oldPass = new String(txtOldPass.getPassword());
            String newPass = new String(txtNewPass.getPassword());
            String confirmPass = new String(txtConfirmPass.getPassword());

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Tous les champs sont obligatoires.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!oldPass.equals(utilisateur.getMotDePasse())) {
                JOptionPane.showMessageDialog(frame, "L'ancien mot de passe est incorrect.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
                txtOldPass.setText("");
                txtOldPass.requestFocus();
                return;
            }
            if (newPass.length() < 4) {
                JOptionPane.showMessageDialog(frame, "Le nouveau mot de passe doit faire au moins 4 caractères.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(frame, "Les mots de passe ne correspondent pas.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
                txtConfirmPass.setText("");
                txtConfirmPass.requestFocus();
                return;
            }
            try {
                dao.changerMotDePasse(utilisateur.getId(), newPass);
                utilisateur.setMotDePasse(newPass);
                txtOldPass.setText("");
                txtNewPass.setText("");
                txtConfirmPass.setText("");
                JOptionPane.showMessageDialog(frame, "Mot de passe changé avec succès !",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Supprimer le compte
        btnDelete.addActionListener(e -> {
            // Protection admin
            if (utilisateur.isAdmin()) {
                JOptionPane.showMessageDialog(frame,
                    "Le compte administrateur ne peut pas être supprimé.",
                    "Action interdite", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Double confirmation
            int confirm1 = JOptionPane.showConfirmDialog(frame,
                "Êtes-vous sûr de vouloir supprimer votre compte ?\n\n"
                + "Cette action est IRRÉVERSIBLE.\n"
                + "Toutes vos données seront définitivement perdues.",
                "Confirmer la suppression",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm1 != JOptionPane.YES_OPTION) return;

            // Deuxième confirmation : saisir le nom d'utilisateur
            String saisie = JOptionPane.showInputDialog(frame,
                "Pour confirmer, tapez votre nom d'utilisateur :\n\n"
                + "→ " + utilisateur.getNomUtilisateur(),
                "Confirmation finale", JOptionPane.WARNING_MESSAGE);

            if (saisie == null || !saisie.equals(utilisateur.getNomUtilisateur())) {
                JOptionPane.showMessageDialog(frame,
                    "Suppression annulée. Le nom d'utilisateur saisi ne correspond pas.",
                    "Annulé", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                dao.supprimer(utilisateur.getId());
                JOptionPane.showMessageDialog(frame,
                    "Votre compte a été supprimé.\nVous allez être redirigé vers l'écran de connexion.",
                    "Compte supprimé", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                if (callback != null) {
                    callback.onCompteSupprime();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Retour
        btnRetour.addActionListener(e -> frame.dispose());

        // === Assemblage ===
        mainPanel.add(header);
        mainPanel.add(cardWrapper);
        mainPanel.add(footer);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(MainMenu.DARK);
        scrollPane.getViewport().setBackground(MainMenu.DARK);

        frame.setContentPane(scrollPane);
        frame.setVisible(true);

        // Scroll en haut au chargement
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    private static JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(100, 100, 100));
        return lbl;
    }

    private static JTextField createField(String value) {
        JTextField tf = new JTextField(value);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(220, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    private static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setPreferredSize(new Dimension(220, 32));
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return pf;
    }
}
