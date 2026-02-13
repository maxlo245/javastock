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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class ReservationMenu {
    private static ReservationDAO reservationDAO;
    private static CoureurDAO coureurDAO;
    private static TypeEpreuveDAO typeEpreuveDAO;
    private static ArticleDAO articleDAO;

    public static void afficher(Connection connection) {
        reservationDAO = new ReservationDAO(connection);
        coureurDAO = new CoureurDAO(connection);
        typeEpreuveDAO = new TypeEpreuveDAO(connection);
        articleDAO = new ArticleDAO(connection);

        JFrame frame = new JFrame("JavaStock - R\u00E9servations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(700, 500));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MainMenu.LIGHT_BG);

        // === Header ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainMenu.SUCCESS);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel lblTitle = new JLabel("\uD83D\uDCCB  Gestion des R\u00E9servations");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(MainMenu.TEXT_LIGHT);
        header.add(lblTitle, BorderLayout.WEST);

        // === Onglets ===
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(MainMenu.BTN_FONT);

        // =============================================
        //  ONGLET LISTE
        // =============================================
        JPanel panelListe = new JPanel(new BorderLayout(0, 10));
        panelListe.setBackground(MainMenu.LIGHT_BG);
        panelListe.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] colonnes = {"ID", "Date", "Coureur", "\u00C9preuve", "Nb Articles"};
        DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(MainMenu.TABLE_FONT);
        table.setRowHeight(28);
        table.setSelectionBackground(MainMenu.SUCCESS);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(MainMenu.TABLE_HEADER);
        table.getTableHeader().setBackground(MainMenu.DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPanel barreActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barreActions.setBackground(MainMenu.LIGHT_BG);
        JButton btnRefresh = MainMenu.createStyledButton("\u21BB Actualiser", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        JButton btnVoirDetail = MainMenu.createStyledButton("\uD83D\uDD0D D\u00E9tail", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        JButton btnSupprimerRes = MainMenu.createStyledButton("\uD83D\uDDD1 Supprimer", MainMenu.DANGER, new Color(192, 57, 43));
        JLabel lblCount = new JLabel("0 r\u00E9servations");
        lblCount.setFont(MainMenu.LABEL_FONT);
        lblCount.setForeground(MainMenu.TEXT_DARK);
        barreActions.add(btnRefresh);
        barreActions.add(btnVoirDetail);
        barreActions.add(btnSupprimerRes);
        barreActions.add(Box.createHorizontalStrut(20));
        barreActions.add(lblCount);

        panelListe.add(barreActions, BorderLayout.NORTH);
        panelListe.add(scrollTable, BorderLayout.CENTER);

        // =============================================
        //  ONGLET CR\u00C9ER
        // =============================================
        JPanel panelCreer = new JPanel();
        panelCreer.setLayout(new BoxLayout(panelCreer, BoxLayout.Y_AXIS));
        panelCreer.setBackground(Color.WHITE);
        panelCreer.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titreForm = new JLabel("Nouvelle R\u00E9servation");
        titreForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titreForm.setForeground(MainMenu.TEXT_DARK);
        titreForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCreer.add(titreForm);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Date
        JPanel pDate = ArticleMenu.createFormField("Date");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JLabel lblDateValue = new JLabel(sdf.format(new Date()));
        lblDateValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDateValue.setForeground(MainMenu.PRIMARY);
        pDate.add(lblDateValue, BorderLayout.CENTER);
        panelCreer.add(pDate);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 10)));

        // Coureur
        JPanel pCoureur = ArticleMenu.createFormField("Coureur *");
        JComboBox<Coureur> cboCoureur = new JComboBox<>();
        cboCoureur.setFont(MainMenu.LABEL_FONT);
        pCoureur.add(cboCoureur, BorderLayout.CENTER);
        panelCreer.add(pCoureur);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 10)));

        // Type d'\u00E9preuve
        JPanel pType = ArticleMenu.createFormField("Type *");
        JComboBox<TypeEpreuve> cboType = new JComboBox<>();
        cboType.setFont(MainMenu.LABEL_FONT);
        pType.add(cboType, BorderLayout.CENTER);
        panelCreer.add(pType);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 10)));

        // Article + Quantit\u00E9 (sur la m\u00EAme ligne)
        JPanel pArtRow = new JPanel(new BorderLayout(10, 5));
        pArtRow.setOpaque(false);
        pArtRow.setMaximumSize(new Dimension(600, 45));
        JLabel lblArt = new JLabel("Article");
        lblArt.setFont(MainMenu.LABEL_FONT);
        lblArt.setPreferredSize(new Dimension(110, 25));
        JComboBox<Article> cboArticle = new JComboBox<>();
        cboArticle.setFont(MainMenu.LABEL_FONT);
        JSpinner spinQuantite = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinQuantite.setPreferredSize(new Dimension(65, 28));
        spinQuantite.setFont(MainMenu.LABEL_FONT);
        JButton btnAjouterArt = MainMenu.createStyledButton(" + ", MainMenu.SUCCESS, MainMenu.SUCCESS_DARK);
        btnAjouterArt.setPreferredSize(new Dimension(45, 28));
        JPanel artRight = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        artRight.setOpaque(false);
        artRight.add(cboArticle);
        artRight.add(new JLabel(" x"));
        artRight.add(spinQuantite);
        artRight.add(btnAjouterArt);
        pArtRow.add(lblArt, BorderLayout.WEST);
        pArtRow.add(artRight, BorderLayout.CENTER);
        panelCreer.add(pArtRow);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 10)));

        // Liste articles s\u00E9lectionn\u00E9s
        JPanel pListeArt = new JPanel(new BorderLayout(5, 5));
        pListeArt.setOpaque(false);
        pListeArt.setMaximumSize(new Dimension(600, 120));
        JLabel lblSelArt = new JLabel("Articles s\u00E9lectionn\u00E9s :");
        lblSelArt.setFont(MainMenu.LABEL_FONT);
        lblSelArt.setForeground(MainMenu.TEXT_DARK);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listeArticles = new JList<>(listModel);
        listeArticles.setFont(MainMenu.TABLE_FONT);
        listeArticles.setBackground(new Color(250, 250, 250));
        JScrollPane scrollListeArt = new JScrollPane(listeArticles);
        scrollListeArt.setPreferredSize(new Dimension(480, 80));
        scrollListeArt.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JButton btnRetirerArt = MainMenu.createStyledButton(" Retirer ", MainMenu.DANGER, new Color(192, 57, 43));
        btnRetirerArt.setPreferredSize(new Dimension(80, 28));
        JPanel artFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        artFooter.setOpaque(false);
        artFooter.add(btnRetirerArt);

        pListeArt.add(lblSelArt, BorderLayout.NORTH);
        pListeArt.add(scrollListeArt, BorderLayout.CENTER);
        pListeArt.add(artFooter, BorderLayout.SOUTH);
        panelCreer.add(pListeArt);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 15)));

        List<ReservationArticle> articlesSelectionnes = new ArrayList<>();

        // Boutons formulaire
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pBtns.setOpaque(false);
        pBtns.setMaximumSize(new Dimension(500, 45));
        JButton btnEnvoyer = MainMenu.createStyledButton("  \u2714 Enregistrer  ", MainMenu.SUCCESS, MainMenu.SUCCESS_DARK);
        JButton btnReset = MainMenu.createStyledButton("  \u21BB R\u00E9initialiser  ", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        btnEnvoyer.setPreferredSize(new Dimension(160, 38));
        btnReset.setPreferredSize(new Dimension(160, 38));
        pBtns.add(btnEnvoyer);
        pBtns.add(btnReset);
        panelCreer.add(pBtns);

        JScrollPane scrollCreer = new JScrollPane(panelCreer);
        scrollCreer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCreer.getVerticalScrollBar().setUnitIncrement(16);
        scrollCreer.setBorder(null);

        tabs.addTab("\uD83D\uDCCB Liste", panelListe);
        tabs.addTab("\u2795 Cr\u00E9er", scrollCreer);

        // === Footer ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(MainMenu.LIGHT_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        JButton btnFermer = MainMenu.createStyledButton("  Fermer  ", MainMenu.DARK_LIGHT, MainMenu.DARK);
        btnFermer.setPreferredSize(new Dimension(100, 35));
        footer.add(btnFermer);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        // === Charger les donn\u00E9es des ComboBox ===
        try {
            for (Coureur c : coureurDAO.lister()) cboCoureur.addItem(c);
            for (TypeEpreuve t : typeEpreuveDAO.lister()) cboType.addItem(t);
            for (Article a : articleDAO.lister()) cboArticle.addItem(a);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Erreur de chargement: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Renderers ComboBox
        cboCoureur.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int idx, boolean sel, boolean foc) {
                super.getListCellRendererComponent(list, value, idx, sel, foc);
                if (value instanceof Coureur c) setText(c.getNom() + " " + c.getPrenom());
                return this;
            }
        });
        cboType.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int idx, boolean sel, boolean foc) {
                super.getListCellRendererComponent(list, value, idx, sel, foc);
                if (value instanceof TypeEpreuve t) setText(t.getLibelle());
                return this;
            }
        });
        cboArticle.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int idx, boolean sel, boolean foc) {
                super.getListCellRendererComponent(list, value, idx, sel, foc);
                if (value instanceof Article a) setText(a.getLibelle() + " (" + a.getCategorie() + ") \u2014 Stock: " + a.getQuantite());
                return this;
            }
        });

        // === Charger la liste des r\u00E9servations ===
        SimpleDateFormat sdfTable = new SimpleDateFormat("dd/MM/yyyy");
        Runnable chargerDonnees = () -> {
            try {
                List<Reservation> reservations = reservationDAO.lister();
                tableModel.setRowCount(0);
                for (Reservation r : reservations) {
                    String coureurStr = r.getCoureur() != null ? r.getCoureur().getNom() + " " + r.getCoureur().getPrenom() : "?";
                    String typeStr = r.getTypeEpreuve() != null ? r.getTypeEpreuve().getLibelle() : "?";
                    tableModel.addRow(new Object[]{
                        r.getId(),
                        sdfTable.format(r.getDate()),
                        coureurStr,
                        typeStr,
                        r.getArticles().size()
                    });
                }
                lblCount.setText(reservations.size() + " r\u00E9servation" + (reservations.size() > 1 ? "s" : ""));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        };
        chargerDonnees.run();

        // === Actions ===
        btnRefresh.addActionListener(e -> chargerDonnees.run());

        btnAjouterArt.addActionListener(e -> {
            Article sel = (Article) cboArticle.getSelectedItem();
            int qte = (Integer) spinQuantite.getValue();
            if (sel != null) {
                articlesSelectionnes.add(new ReservationArticle(sel, qte));
                listModel.addElement(sel.getLibelle() + "  \u00D7  " + qte);
                spinQuantite.setValue(1);
            }
        });

        btnRetirerArt.addActionListener(e -> {
            int idx = listeArticles.getSelectedIndex();
            if (idx >= 0) {
                articlesSelectionnes.remove(idx);
                listModel.remove(idx);
            }
        });

        btnVoirDetail.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "S\u00E9lectionnez une r\u00E9servation.", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(row), 0);
            try {
                Reservation res = reservationDAO.consulter(id);
                if (res == null) return;
                StringBuilder sb = new StringBuilder();
                sb.append("R\u00E9servation #").append(res.getId()).append("\n");
                sb.append("Date : ").append(sdfTable.format(res.getDate())).append("\n");
                sb.append("Coureur : ").append(res.getCoureur().getNom()).append(" ").append(res.getCoureur().getPrenom()).append("\n");
                sb.append("\u00C9preuve : ").append(res.getTypeEpreuve().getLibelle()).append("\n\n");
                sb.append("Articles :\n");
                for (ReservationArticle ra : res.getArticles()) {
                    sb.append("  \u2022 ").append(ra.getArticle().getLibelle()).append(" \u00D7 ").append(ra.getQuantite()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, sb.toString(), "D\u00E9tail R\u00E9servation", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSupprimerRes.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "S\u00E9lectionnez une r\u00E9servation.", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(row), 0);
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Supprimer la r\u00E9servation #" + id + " ?",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    reservationDAO.supprimer(id);
                    chargerDonnees.run();
                    JOptionPane.showMessageDialog(frame, "R\u00E9servation supprim\u00E9e.", "Succ\u00E8s", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEnvoyer.addActionListener(e -> {
            Coureur coureur = (Coureur) cboCoureur.getSelectedItem();
            TypeEpreuve typeEpreuve = (TypeEpreuve) cboType.getSelectedItem();
            if (coureur == null) { JOptionPane.showMessageDialog(frame, "S\u00E9lectionnez un coureur !", "Champ manquant", JOptionPane.WARNING_MESSAGE); return; }
            if (typeEpreuve == null) { JOptionPane.showMessageDialog(frame, "S\u00E9lectionnez un type d'\u00E9preuve !", "Champ manquant", JOptionPane.WARNING_MESSAGE); return; }
            if (articlesSelectionnes.isEmpty()) { JOptionPane.showMessageDialog(frame, "Ajoutez au moins un article !", "Champ manquant", JOptionPane.WARNING_MESSAGE); return; }

            try {
                Reservation reservation = new Reservation(0, new Date(), coureur, typeEpreuve, new ArrayList<>(articlesSelectionnes));
                reservationDAO.creer(reservation);

                StringBuilder msg = new StringBuilder("R\u00E9servation cr\u00E9\u00E9e !\n\n");
                msg.append("Coureur : ").append(coureur.getNom()).append(" ").append(coureur.getPrenom()).append("\n");
                msg.append("\u00C9preuve : ").append(typeEpreuve.getLibelle()).append("\n");
                msg.append("Articles : ").append(articlesSelectionnes.size()).append("\n");
                JOptionPane.showMessageDialog(frame, msg.toString(), "Succ\u00E8s", JOptionPane.INFORMATION_MESSAGE);

                articlesSelectionnes.clear();
                listModel.clear();
                if (cboCoureur.getItemCount() > 0) cboCoureur.setSelectedIndex(0);
                if (cboType.getItemCount() > 0) cboType.setSelectedIndex(0);
                if (cboArticle.getItemCount() > 0) cboArticle.setSelectedIndex(0);
                spinQuantite.setValue(1);
                chargerDonnees.run();
                tabs.setSelectedIndex(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReset.addActionListener(e -> {
            articlesSelectionnes.clear();
            listModel.clear();
            if (cboCoureur.getItemCount() > 0) cboCoureur.setSelectedIndex(0);
            if (cboType.getItemCount() > 0) cboType.setSelectedIndex(0);
            if (cboArticle.getItemCount() > 0) cboArticle.setSelectedIndex(0);
            spinQuantite.setValue(1);
        });

        btnFermer.addActionListener(e -> frame.dispose());
        frame.getRootPane().registerKeyboardAction(e -> frame.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
