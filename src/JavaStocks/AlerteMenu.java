package JavaStocks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Menu Alertes : affiche les articles en rupture ou stock bas,
 * permet de modifier le stock directement.
 */
public class AlerteMenu {
    private static ArticleDAO articleDAO;

    /** Seuil en dessous duquel un article est en "stock bas" */
    private static final int SEUIL_STOCK_BAS = 5;

    public static void afficher(Connection connection) {
        articleDAO = new ArticleDAO(connection);

        JFrame frame = new JFrame("JavaStock - Alertes de Stock");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(750, 520);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(650, 450));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MainMenu.LIGHT_BG);

        // === Header ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainMenu.DANGER);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel lblTitle = new JLabel("\u26A0\uFE0F  Alertes de Stock");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(MainMenu.TEXT_LIGHT);
        header.add(lblTitle, BorderLayout.WEST);

        // === Panneau de statistiques ===
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statsPanel.setBackground(MainMenu.LIGHT_BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 5, 15));

        JLabel lblRupture = createStatLabel("0", "Ruptures", MainMenu.DANGER);
        JLabel lblStockBas = createStatLabel("0", "Stock bas", MainMenu.WARNING);
        JLabel lblOk = createStatLabel("0", "OK", MainMenu.SUCCESS);
        JLabel lblTotal = createStatLabel("0", "Total", MainMenu.PRIMARY);

        statsPanel.add(lblRupture);
        statsPanel.add(lblStockBas);
        statsPanel.add(lblOk);
        statsPanel.add(lblTotal);

        // === Tableau ===
        JPanel panelTable = new JPanel(new BorderLayout(0, 10));
        panelTable.setBackground(MainMenu.LIGHT_BG);
        panelTable.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        String[] colonnes = {"ID", "Libellé", "Catégorie", "Quantité", "Statut", "Priorité"};
        DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
            public Class<?> getColumnClass(int col) {
                if (col == 3 || col == 5) return Integer.class;
                return String.class;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFont(MainMenu.TABLE_FONT);
        table.setRowHeight(30);
        table.setSelectionBackground(MainMenu.DANGER);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(MainMenu.TABLE_HEADER);
        table.getTableHeader().setBackground(MainMenu.DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);

        // Cacher la colonne ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Renderer coloré pour le statut
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String statut = value != null ? value.toString() : "";
                    if (statut.contains("Rupture")) {
                        c.setForeground(MainMenu.DANGER);
                        c.setBackground(new Color(253, 237, 236));
                    } else if (statut.contains("Stock bas")) {
                        c.setForeground(new Color(180, 120, 0));
                        c.setBackground(new Color(254, 249, 231));
                    } else {
                        c.setForeground(MainMenu.SUCCESS);
                        c.setBackground(Color.WHITE);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // Renderer centré pour la quantité
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // === Barre d'actions ===
        JPanel barreActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barreActions.setBackground(MainMenu.LIGHT_BG);

        JButton btnRefresh = MainMenu.createStyledButton("\u21BB Actualiser", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        JButton btnRestock = MainMenu.createStyledButton("\u2795 Réapprovisionner", MainMenu.SUCCESS, MainMenu.SUCCESS_DARK);
        JButton btnRuptureOnly = MainMenu.createStyledButton("\u26A0 Ruptures seules", MainMenu.DANGER, new Color(192, 57, 43));
        JButton btnTousArticles = MainMenu.createStyledButton("\uD83D\uDCCB Tous les articles", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);

        barreActions.add(btnRefresh);
        barreActions.add(btnRestock);
        barreActions.add(btnRuptureOnly);
        barreActions.add(btnTousArticles);

        panelTable.add(barreActions, BorderLayout.NORTH);
        panelTable.add(scrollTable, BorderLayout.CENTER);

        // === Panel central qui contient stats + table ===
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(MainMenu.LIGHT_BG);
        center.add(statsPanel, BorderLayout.NORTH);
        center.add(panelTable, BorderLayout.CENTER);

        // === Footer ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(MainMenu.LIGHT_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        JButton btnFermer = MainMenu.createStyledButton("  Fermer  ", MainMenu.DARK_LIGHT, MainMenu.DARK);
        btnFermer.setPreferredSize(new Dimension(100, 35));
        footer.add(btnFermer);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        // === Chargement des données ===
        final boolean[] ruptureSeule = {false};

        Runnable chargerDonnees = () -> {
            try {
                List<Article> articles = articleDAO.lister();
                tableModel.setRowCount(0);
                int countRupture = 0, countBas = 0, countOk = 0;

                for (Article a : articles) {
                    String statut;
                    int priorite;
                    if (a.getQuantite() == 0) {
                        statut = "\u274C Rupture";
                        priorite = 1;
                        countRupture++;
                    } else if (a.getQuantite() < SEUIL_STOCK_BAS) {
                        statut = "\u26A0 Stock bas";
                        priorite = 2;
                        countBas++;
                    } else {
                        statut = "\u2705 OK";
                        priorite = 3;
                        countOk++;
                    }

                    if (!ruptureSeule[0] || priorite <= 2) {
                        tableModel.addRow(new Object[]{
                            a.getId(), a.getLibelle(), a.getCategorie(),
                            a.getQuantite(), statut, priorite
                        });
                    }
                }

                lblRupture.setText(buildStatHtml(String.valueOf(countRupture), "Ruptures", MainMenu.DANGER));
                lblStockBas.setText(buildStatHtml(String.valueOf(countBas), "Stock bas", MainMenu.WARNING));
                lblOk.setText(buildStatHtml(String.valueOf(countOk), "OK", MainMenu.SUCCESS));
                lblTotal.setText(buildStatHtml(String.valueOf(articles.size()), "Total", MainMenu.PRIMARY));

                // Trier par priorité (ruptures en premier)
                if (table.getRowSorter() != null) {
                    table.getRowSorter().toggleSortOrder(5);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        };
        chargerDonnees.run();

        // === Actions ===
        btnRefresh.addActionListener(e -> chargerDonnees.run());

        btnRuptureOnly.addActionListener(e -> {
            ruptureSeule[0] = true;
            chargerDonnees.run();
        });

        btnTousArticles.addActionListener(e -> {
            ruptureSeule[0] = false;
            chargerDonnees.run();
        });

        btnRestock.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame,
                    "Sélectionnez un article à réapprovisionner.", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            int id = (int) tableModel.getValueAt(modelRow, 0);
            String nom = (String) tableModel.getValueAt(modelRow, 1);
            int qteActuelle = (int) tableModel.getValueAt(modelRow, 3);

            // Dialogue de réapprovisionnement
            JPanel reappPanel = new JPanel();
            reappPanel.setLayout(new BoxLayout(reappPanel, BoxLayout.Y_AXIS));

            JLabel lblInfo = new JLabel("<html><b>" + nom + "</b><br>Stock actuel : " + qteActuelle + "</html>");
            lblInfo.setFont(MainMenu.LABEL_FONT);
            lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
            reappPanel.add(lblInfo);
            reappPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            JLabel lblQte = new JLabel("Quantité à ajouter :");
            lblQte.setFont(MainMenu.LABEL_FONT);
            lblQte.setAlignmentX(Component.LEFT_ALIGNMENT);
            reappPanel.add(lblQte);

            JSpinner spinAdd = new JSpinner(new SpinnerNumberModel(10, 1, 99999, 1));
            spinAdd.setFont(MainMenu.LABEL_FONT);
            spinAdd.setMaximumSize(new Dimension(200, 30));
            spinAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
            reappPanel.add(spinAdd);

            int result = JOptionPane.showConfirmDialog(frame, reappPanel,
                "Réapprovisionner", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int ajout = (int) spinAdd.getValue();
                    Article article = articleDAO.consulter(id);
                    if (article != null) {
                        article.setQuantite(article.getQuantite() + ajout);
                        articleDAO.modifier(article);
                        chargerDonnees.run();
                        JOptionPane.showMessageDialog(frame,
                            "+" + ajout + " ajouté à \"" + nom + "\"\nNouveau stock : " + article.getQuantite(),
                            "Réapprovisionnement effectué", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnFermer.addActionListener(e -> frame.dispose());

        frame.getRootPane().registerKeyboardAction(e -> frame.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /** Crée un label de statistique stylisé */
    private static JLabel createStatLabel(String value, String label, Color color) {
        JLabel lbl = new JLabel();
        lbl.setText(buildStatHtml(value, label, color));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        return lbl;
    }

    /** Construit le HTML pour un label de stat */
    private static String buildStatHtml(String value, String label, Color color) {
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return "<html><center><b style='font-size:16pt;color:" + hex + "'>" + value
                + "</b><br><span style='font-size:8pt;color:#7f8c8d'>" + label + "</span></center></html>";
    }
}
