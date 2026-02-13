package JavaStocks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
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
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainMenu {

    // === Palette de couleurs partagée ===
    public static final Color PRIMARY      = new Color(41, 128, 185);   // Bleu
    public static final Color PRIMARY_DARK = new Color(31, 97, 141);
    public static final Color SUCCESS      = new Color(39, 174, 96);    // Vert
    public static final Color SUCCESS_DARK = new Color(30, 132, 73);
    public static final Color WARNING      = new Color(243, 156, 18);   // Orange
    public static final Color DANGER       = new Color(231, 76, 60);    // Rouge
    public static final Color DARK         = new Color(44, 62, 80);     // Fond sombre
    public static final Color DARK_LIGHT   = new Color(52, 73, 94);
    public static final Color LIGHT_BG     = new Color(236, 240, 241);  // Fond clair
    public static final Color TEXT_LIGHT   = new Color(248, 249, 250);
    public static final Color TEXT_DARK    = new Color(44, 62, 80);
    public static final Color CARD_BG      = Color.WHITE;

    public static final Font TITLE_FONT    = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BTN_FONT      = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font LABEL_FONT    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font TABLE_FONT    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font TABLE_HEADER  = new Font("Segoe UI", Font.BOLD, 13);

    /** Crée un bouton stylisé avec effet hover */
    public static JButton createStyledButton(String text, Color bg, Color bgHover) {
        JButton btn = new JButton(text);
        btn.setFont(BTN_FONT);
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgHover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    /** Crée un bouton de menu (grand, avec icône emoji et description) */
    private static JButton createMenuButton(String emoji, String title, String desc, Color bg, Color bgHover) {
        JButton btn = new JButton("<html><center>" + emoji + "<br><b>" + title + "</b><br><font size='2' color='#dcdde1'>" + desc + "</font></center></html>");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(180, 120));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgHover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    public static void main(String[] args) {
        // Appliquer le Look & Feel Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DatabaseConnection.getConnection();

                JFrame frame = new JFrame("JavaStock - Gestion de Stock");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(650, 520);
                frame.setLocationRelativeTo(null);
                frame.setMinimumSize(new Dimension(600, 480));

                // Panel principal avec fond sombre
                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBackground(DARK);

                // === Header ===
                JPanel header = new JPanel();
                header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
                header.setBackground(DARK);
                header.setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));

                JLabel lblTitle = new JLabel("\u26A1 JavaStock");
                lblTitle.setFont(TITLE_FONT);
                lblTitle.setForeground(TEXT_LIGHT);
                lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel lblSubtitle = new JLabel("Système de gestion de stock pour épreuves sportives");
                lblSubtitle.setFont(SUBTITLE_FONT);
                lblSubtitle.setForeground(new Color(149, 165, 166));
                lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                header.add(lblTitle);
                header.add(Box.createRigidArea(new Dimension(0, 5)));
                header.add(lblSubtitle);

                // === Grille de boutons ===
                JPanel grid = new JPanel(new GridLayout(2, 3, 15, 15));
                grid.setBackground(DARK);
                grid.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

                JButton btnArticles = createMenuButton("\uD83D\uDCE6", "Articles", "Gérer le stock", PRIMARY, PRIMARY_DARK);
                JButton btnCoureurs = createMenuButton("\uD83C\uDFC3", "Coureurs", "Gérer les coureurs", new Color(142, 68, 173), new Color(113, 54, 138));
                JButton btnTypes    = createMenuButton("\uD83C\uDFC6", "Épreuves", "Types d'épreuve", WARNING, new Color(211, 136, 16));
                JButton btnReserv   = createMenuButton("\uD83D\uDCCB", "Réservations", "Gérer les résa", SUCCESS, SUCCESS_DARK);
                JButton btnRupture  = createMenuButton("\u26A0\uFE0F", "Alertes", "Ruptures de stock", DANGER, new Color(192, 57, 43));
                JButton btnHistorique = createMenuButton("\uD83D\uDCC8", "Historique", "Consulter les logs", DARK_LIGHT, new Color(66, 89, 114));

                grid.add(btnArticles);
                grid.add(btnCoureurs);
                grid.add(btnTypes);
                grid.add(btnReserv);
                grid.add(btnRupture);
                grid.add(btnHistorique);

                // === Footer ===
                JPanel footer = new JPanel(new BorderLayout());
                footer.setBackground(DARK);
                footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

                JButton btnQuitter = createStyledButton("  Quitter  ", DANGER, new Color(192, 57, 43));
                btnQuitter.setPreferredSize(new Dimension(120, 38));

                JLabel lblStatus = new JLabel("\u2705 Connecté à la base de données");
                lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                lblStatus.setForeground(SUCCESS);

                footer.add(lblStatus, BorderLayout.WEST);
                footer.add(btnQuitter, BorderLayout.EAST);

                // === Actions ===
                btnArticles.addActionListener(e -> ArticleMenu.afficher(connection));
                btnCoureurs.addActionListener(e -> CoureurMenu.afficher(connection));
                btnTypes.addActionListener(e -> TypeEpreuveMenu.afficher(connection));
                btnReserv.addActionListener(e -> ReservationMenu.afficher(connection));
                btnRupture.addActionListener(e -> AlerteMenu.afficher(connection));
                btnHistorique.addActionListener(e -> HistoriqueMenu.afficher());
                btnQuitter.addActionListener(e -> {
                    DatabaseConnection.closeConnection();
                    frame.dispose();
                    System.exit(0);
                });

                // === Raccourcis clavier ===
                frame.getRootPane().registerKeyboardAction(
                    e -> { DatabaseConnection.closeConnection(); System.exit(0); },
                    KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);

                mainPanel.add(header, BorderLayout.NORTH);
                mainPanel.add(grid, BorderLayout.CENTER);
                mainPanel.add(footer, BorderLayout.SOUTH);

                frame.setContentPane(mainPanel);
                frame.setVisible(true);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                    "Erreur de connexion à la base de données:\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
