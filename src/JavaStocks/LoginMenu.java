package JavaStocks;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Écran de connexion et d'inscription.
 * S'affiche au lancement de l'application avant le MainMenu.
 */
public class LoginMenu {

    private static final Color BG_GRADIENT_TOP = new Color(44, 62, 80);
    private static final Color BG_GRADIENT_BOT = new Color(52, 73, 94);
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color INPUT_BORDER = new Color(189, 195, 199);
    private static final Color INPUT_FOCUS = new Color(41, 128, 185);
    private static final Color LINK_COLOR = new Color(41, 128, 185);

    /** Callback quand l'authentification réussit */
    public interface LoginCallback {
        void onLoginSuccess(Utilisateur utilisateur);
    }

    public static void afficher(Connection connection, LoginCallback callback) {
        UtilisateurDAO userDAO = new UtilisateurDAO(connection);

        // Créer la table si elle n'existe pas
        try {
            userDAO.creerTableSiAbsente();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erreur d'initialisation de la table utilisateurs:\n" + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        JFrame frame = new JFrame("JavaStock - Authentification");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 560);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(450, 500));

        // Panel principal avec fond sombre
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_GRADIENT_TOP);

        // === Header ===
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_GRADIENT_TOP);
        header.setBorder(BorderFactory.createEmptyBorder(35, 20, 20, 20));

        JLabel lblLogo = new JLabel("\u26A1");
        lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAppName = new JLabel("JavaStock");
        lblAppName.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblAppName.setForeground(MainMenu.TEXT_LIGHT);
        lblAppName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTagline = new JLabel("Gestion de stock pour épreuves sportives");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTagline.setForeground(new Color(149, 165, 166));
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblLogo);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(lblAppName);
        header.add(Box.createRigidArea(new Dimension(0, 3)));
        header.add(lblTagline);

        // === Card container avec CardLayout pour switch login/register ===
        CardLayout cardLayout = new CardLayout();
        JPanel cardContainer = new JPanel(cardLayout);
        cardContainer.setOpaque(false);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));

        // ----- CARTE CONNEXION -----
        JPanel loginCard = createCard();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;

        JLabel lblLoginTitle = new JLabel("Connexion", SwingConstants.CENTER);
        lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLoginTitle.setForeground(MainMenu.TEXT_DARK);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        loginCard.add(lblLoginTitle, gbc);

        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel lblUser = new JLabel("Nom d'utilisateur");
        lblUser.setFont(MainMenu.LABEL_FONT);
        lblUser.setForeground(MainMenu.TEXT_DARK);
        gbc.gridy = 1;
        loginCard.add(lblUser, gbc);

        JTextField txtLoginUser = createStyledInput();
        gbc.gridy = 2;
        loginCard.add(txtLoginUser, gbc);

        JLabel lblPass = new JLabel("Mot de passe");
        lblPass.setFont(MainMenu.LABEL_FONT);
        lblPass.setForeground(MainMenu.TEXT_DARK);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 4, 0);
        loginCard.add(lblPass, gbc);

        JPasswordField txtLoginPass = createStyledPassword();
        gbc.gridy = 4;
        gbc.insets = new Insets(4, 0, 4, 0);
        loginCard.add(txtLoginPass, gbc);

        JLabel lblLoginError = new JLabel(" ");
        lblLoginError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLoginError.setForeground(MainMenu.DANGER);
        lblLoginError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 5;
        gbc.insets = new Insets(2, 0, 2, 0);
        loginCard.add(lblLoginError, gbc);

        JButton btnLogin = MainMenu.createStyledButton("  Se connecter  ", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        btnLogin.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 10, 0);
        loginCard.add(btnLogin, gbc);

        JLabel lblGoRegister = createLink("Pas encore de compte ? Créer un compte");
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 0, 0, 0);
        loginCard.add(lblGoRegister, gbc);

        // ----- CARTE INSCRIPTION -----
        JPanel registerCard = createCard();
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.gridx = 0;

        JLabel lblRegTitle = new JLabel("Créer un compte", SwingConstants.CENTER);
        lblRegTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblRegTitle.setForeground(MainMenu.TEXT_DARK);
        gbc2.gridy = 0;
        gbc2.insets = new Insets(0, 0, 15, 0);
        registerCard.add(lblRegTitle, gbc2);

        gbc2.insets = new Insets(3, 0, 3, 0);

        JLabel lblRegNom = new JLabel("Nom");
        lblRegNom.setFont(MainMenu.LABEL_FONT);
        gbc2.gridy = 1;
        registerCard.add(lblRegNom, gbc2);

        JTextField txtRegNom = createStyledInput();
        gbc2.gridy = 2;
        registerCard.add(txtRegNom, gbc2);

        JLabel lblRegPrenom = new JLabel("Prénom");
        lblRegPrenom.setFont(MainMenu.LABEL_FONT);
        gbc2.gridy = 3;
        gbc2.insets = new Insets(8, 0, 3, 0);
        registerCard.add(lblRegPrenom, gbc2);

        JTextField txtRegPrenom = createStyledInput();
        gbc2.gridy = 4;
        gbc2.insets = new Insets(3, 0, 3, 0);
        registerCard.add(txtRegPrenom, gbc2);

        JLabel lblRegUser = new JLabel("Nom d'utilisateur");
        lblRegUser.setFont(MainMenu.LABEL_FONT);
        gbc2.gridy = 5;
        gbc2.insets = new Insets(8, 0, 3, 0);
        registerCard.add(lblRegUser, gbc2);

        JTextField txtRegUser = createStyledInput();
        gbc2.gridy = 6;
        gbc2.insets = new Insets(3, 0, 3, 0);
        registerCard.add(txtRegUser, gbc2);

        JLabel lblRegPass = new JLabel("Mot de passe");
        lblRegPass.setFont(MainMenu.LABEL_FONT);
        gbc2.gridy = 7;
        gbc2.insets = new Insets(8, 0, 3, 0);
        registerCard.add(lblRegPass, gbc2);

        JPasswordField txtRegPass = createStyledPassword();
        gbc2.gridy = 8;
        gbc2.insets = new Insets(3, 0, 3, 0);
        registerCard.add(txtRegPass, gbc2);

        JLabel lblRegPassConfirm = new JLabel("Confirmer le mot de passe");
        lblRegPassConfirm.setFont(MainMenu.LABEL_FONT);
        gbc2.gridy = 9;
        gbc2.insets = new Insets(8, 0, 3, 0);
        registerCard.add(lblRegPassConfirm, gbc2);

        JPasswordField txtRegPassConfirm = createStyledPassword();
        gbc2.gridy = 10;
        gbc2.insets = new Insets(3, 0, 3, 0);
        registerCard.add(txtRegPassConfirm, gbc2);

        JLabel lblRegError = new JLabel(" ");
        lblRegError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRegError.setForeground(MainMenu.DANGER);
        lblRegError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc2.gridy = 11;
        gbc2.insets = new Insets(2, 0, 2, 0);
        registerCard.add(lblRegError, gbc2);

        JButton btnRegister = MainMenu.createStyledButton("  Créer le compte  ", MainMenu.SUCCESS, MainMenu.SUCCESS_DARK);
        btnRegister.setPreferredSize(new Dimension(300, 40));
        gbc2.gridy = 12;
        gbc2.insets = new Insets(8, 0, 8, 0);
        registerCard.add(btnRegister, gbc2);

        JLabel lblGoLogin = createLink("Déjà un compte ? Se connecter");
        gbc2.gridy = 13;
        gbc2.insets = new Insets(3, 0, 0, 0);
        registerCard.add(lblGoLogin, gbc2);

        // Ajouter les cartes au container
        cardContainer.add(loginCard, "login");
        cardContainer.add(registerCard, "register");

        // === Actions de navigation ===
        lblGoRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lblLoginError.setText(" ");
                cardLayout.show(cardContainer, "register");
                frame.setSize(480, 720);
                frame.setLocationRelativeTo(null);
                txtRegNom.requestFocus();
            }
        });

        lblGoLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lblRegError.setText(" ");
                cardLayout.show(cardContainer, "login");
                frame.setSize(480, 560);
                frame.setLocationRelativeTo(null);
                txtLoginUser.requestFocus();
            }
        });

        // === Action Connexion ===
        Runnable actionLogin = () -> {
            String username = txtLoginUser.getText().trim();
            String password = new String(txtLoginPass.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                lblLoginError.setText("Veuillez remplir tous les champs.");
                return;
            }

            try {
                Utilisateur user = userDAO.authentifier(username, password);
                if (user != null) {
                    frame.dispose();
                    callback.onLoginSuccess(user);
                } else {
                    lblLoginError.setText("Nom d'utilisateur ou mot de passe incorrect.");
                    txtLoginPass.setText("");
                    txtLoginPass.requestFocus();
                }
            } catch (SQLException ex) {
                lblLoginError.setText("Erreur: " + ex.getMessage());
            }
        };

        btnLogin.addActionListener(e -> actionLogin.run());
        txtLoginPass.addActionListener(e -> actionLogin.run());
        txtLoginUser.addActionListener(e -> txtLoginPass.requestFocus());

        // === Action Inscription ===
        Runnable actionRegister = () -> {
            String nom = txtRegNom.getText().trim();
            String prenom = txtRegPrenom.getText().trim();
            String username = txtRegUser.getText().trim();
            String password = new String(txtRegPass.getPassword());
            String passwordConfirm = new String(txtRegPassConfirm.getPassword());

            // Validations
            if (nom.isEmpty() || prenom.isEmpty() || username.isEmpty() || password.isEmpty()) {
                lblRegError.setText("Tous les champs sont obligatoires.");
                return;
            }
            if (username.length() < 3) {
                lblRegError.setText("Le nom d'utilisateur doit faire au moins 3 caractères.");
                return;
            }
            if (password.length() < 4) {
                lblRegError.setText("Le mot de passe doit faire au moins 4 caractères.");
                return;
            }
            if (!password.equals(passwordConfirm)) {
                lblRegError.setText("Les mots de passe ne correspondent pas.");
                txtRegPassConfirm.setText("");
                txtRegPassConfirm.requestFocus();
                return;
            }

            try {
                if (userDAO.existeNomUtilisateur(username)) {
                    lblRegError.setText("Ce nom d'utilisateur est déjà pris.");
                    txtRegUser.requestFocus();
                    return;
                }

                Utilisateur newUser = new Utilisateur(username, password, nom, prenom);
                userDAO.creer(newUser);

                JOptionPane.showMessageDialog(frame,
                    "Compte créé avec succès !\n\nVous pouvez maintenant vous connecter avec :\n"
                    + "Utilisateur : " + username,
                    "Inscription réussie", JOptionPane.INFORMATION_MESSAGE);

                // Revenir à l'écran de connexion
                txtLoginUser.setText(username);
                txtLoginPass.setText("");
                cardLayout.show(cardContainer, "login");
                frame.setSize(480, 560);
                frame.setLocationRelativeTo(null);
                txtLoginPass.requestFocus();

                // Scroll en haut
                SwingUtilities.invokeLater(() -> {
                    if (frame.getContentPane() instanceof JScrollPane sp) {
                        sp.getVerticalScrollBar().setValue(0);
                    }
                });

                // Reset form inscription
                txtRegNom.setText("");
                txtRegPrenom.setText("");
                txtRegUser.setText("");
                txtRegPass.setText("");
                txtRegPassConfirm.setText("");
                lblRegError.setText(" ");

            } catch (SQLException ex) {
                lblRegError.setText("Erreur: " + ex.getMessage());
            }
        };

        btnRegister.addActionListener(e -> actionRegister.run());
        txtRegPassConfirm.addActionListener(e -> actionRegister.run());

        // === Assemblage ===
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(cardContainer, BorderLayout.CENTER);

        // Raccourci Escape
        frame.getRootPane().registerKeyboardAction(
            e -> { DatabaseConnection.closeConnection(); System.exit(0); },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(BG_GRADIENT_TOP);
        scrollPane.getViewport().setBackground(BG_GRADIENT_TOP);

        frame.setContentPane(scrollPane);
        frame.setVisible(true);

        // Focus initial
        SwingUtilities.invokeLater(() -> {
            txtLoginUser.requestFocusInWindow();
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }

    /** Crée un panel "carte" blanche avec coins arrondis */
    private static JPanel createCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        return card;
    }

    /** Crée un champ de texte stylisé */
    private static JTextField createStyledInput() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(300, 36));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    /** Crée un champ mot de passe stylisé */
    private static JPasswordField createStyledPassword() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pf.setPreferredSize(new Dimension(300, 36));
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    /** Crée un label cliquable (lien) */
    private static JLabel createLink(String text) {
        JLabel lbl = new JLabel("<html><u>" + text + "</u></html>", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(LINK_COLOR);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                lbl.setForeground(MainMenu.PRIMARY_DARK);
            }
            public void mouseExited(MouseEvent e) {
                lbl.setForeground(LINK_COLOR);
            }
        });
        return lbl;
    }
}
