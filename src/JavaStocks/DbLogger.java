package JavaStocks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Logger centralisé pour les opérations base de données.
 * Affiche les requêtes SQL dans la console ET stocke un historique en mémoire
 * consultable via l'écran Historique.
 */
public class DbLogger {

    // Couleurs ANSI
    private static final String RESET  = "\u001B[0m";
    private static final String BLUE   = "\u001B[34m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GRAY   = "\u001B[90m";
    private static final String BOLD   = "\u001B[1m";

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FMT_FULL = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static boolean enabled = true;

    /** Catégories de logs */
    public static final String CAT_ARTICLES     = "Articles";
    public static final String CAT_COUREURS     = "Coureurs";
    public static final String CAT_EPREUVES     = "\u00c9preuves";
    public static final String CAT_RESERVATIONS = "R\u00e9servations";
    public static final String CAT_UTILISATEURS = "Utilisateurs";
    public static final String CAT_SYSTEME      = "Syst\u00e8me";

    /** Entrée de log stockée en mémoire */
    public static class LogEntry {
        public final LocalDateTime timestamp;
        public final String level;      // SQL, CONN, INFO, ERROR, OK, DISC
        public final String category;   // Articles, Coureurs, etc.
        public final String source;     // ex: ArticleDAO.creer
        public final String message;    // texte brut
        public final String detail;     // paramètres ou résultat

        public LogEntry(String level, String source, String message, String detail) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.source = source;
            this.message = message;
            this.detail = detail;
            this.category = resolveCategory(source);
        }

        public LogEntry(String level, String source, String message, String detail, String category) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.source = source;
            this.message = message;
            this.detail = detail;
            this.category = category;
        }

        public String getTimestampStr() {
            return timestamp.format(FMT_FULL);
        }
    }

    /** Résout la catégorie à partir du nom de source (DAO) */
    private static String resolveCategory(String source) {
        if (source == null) return CAT_SYSTEME;
        String s = source.toLowerCase();
        if (s.contains("articledao"))      return CAT_ARTICLES;
        if (s.contains("coureurdao"))      return CAT_COUREURS;
        if (s.contains("typeepreuvedao"))  return CAT_EPREUVES;
        if (s.contains("reservationdao"))  return CAT_RESERVATIONS;
        if (s.contains("utilisateurdao"))  return CAT_UTILISATEURS;
        if (s.contains("database"))        return CAT_SYSTEME;
        return CAT_SYSTEME;
    }

    /** Retourne toutes les catégories disponibles */
    public static String[] getCategories() {
        return new String[]{ CAT_ARTICLES, CAT_COUREURS, CAT_EPREUVES, CAT_RESERVATIONS, CAT_UTILISATEURS, CAT_SYSTEME };
    }

    /** Historique en mémoire (max 500 entrées) */
    private static final List<LogEntry> history = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_HISTORY = 500;

    private static void addToHistory(LogEntry entry) {
        history.add(entry);
        if (history.size() > MAX_HISTORY) {
            history.remove(0);
        }
    }

    /** Retourne une copie de l'historique */
    public static List<LogEntry> getHistory() {
        return new ArrayList<>(history);
    }

    /** Vide l'historique */
    public static void clearHistory() {
        history.clear();
    }

    /** Nombre d'entrées dans l'historique */
    public static int getHistorySize() {
        return history.size();
    }

    public static void setEnabled(boolean on) {
        enabled = on;
    }

    private static String timestamp() {
        return GRAY + "[" + LocalDateTime.now().format(FMT) + "]" + RESET;
    }

    /** Log une requête SQL avant exécution */
    public static void sql(String dao, String operation, String sql) {
        addToHistory(new LogEntry("SQL", dao + "." + operation, sql, null));
        if (!enabled) return;
        System.out.println(timestamp() + BLUE + BOLD + " [SQL] " + RESET
                + CYAN + dao + "." + operation + RESET
                + GRAY + " → " + RESET + sql);
    }

    /** Log les paramètres d'une requête (masque les données sensibles) */
    public static void params(String... params) {
        StringBuilder paramsStr = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) paramsStr.append(", ");
            paramsStr.append("[").append(i + 1).append("]=").append(params[i]);
        }
        // Ajouter les params au dernier log SQL
        if (!history.isEmpty()) {
            LogEntry last = history.get(history.size() - 1);
            if ("SQL".equals(last.level)) {
                history.set(history.size() - 1,
                    new LogEntry(last.level, last.source, last.message, paramsStr.toString(), last.category));
            }
        }
        if (!enabled) return;
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp()).append(YELLOW).append("  ├─ Params: ").append(RESET);
        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(GRAY).append(", ").append(RESET);
            sb.append(CYAN).append("[").append(i + 1).append("]").append(RESET)
              .append("=").append(params[i]);
        }
        System.out.println(sb);
    }

    /** Log un résultat réussi */
    public static void success(String dao, String operation, String detail) {
        addToHistory(new LogEntry("OK", dao + "." + operation, detail, null));
        if (!enabled) return;
        System.out.println(timestamp() + GREEN + "  └─ ✓ " + RESET
                + dao + "." + operation + " → " + GREEN + detail + RESET);
    }

    /** Log un résultat avec nombre de lignes */
    public static void result(String dao, String operation, int rowCount) {
        addToHistory(new LogEntry("OK", dao + "." + operation, rowCount + " ligne(s)", null));
        if (!enabled) return;
        System.out.println(timestamp() + GREEN + "  └─ ✓ " + RESET
                + dao + "." + operation + " → " + GREEN + rowCount + " ligne(s)" + RESET);
    }

    /** Log une erreur SQL */
    public static void error(String dao, String operation, Exception e) {
        addToHistory(new LogEntry("ERROR", dao + "." + operation, e.getMessage(), null));
        if (!enabled) return;
        System.err.println(timestamp() + RED + BOLD + "  └─ ✗ ERREUR " + RESET
                + RED + dao + "." + operation + " → " + e.getMessage() + RESET);
    }

    /** Log une connexion */
    public static void connection(String message) {
        addToHistory(new LogEntry("CONN", "DatabaseConnection", message, null));
        if (!enabled) return;
        System.out.println(timestamp() + GREEN + BOLD + " [CONN] " + RESET + message);
    }

    /** Log une déconnexion */
    public static void disconnection(String message) {
        addToHistory(new LogEntry("DISC", "DatabaseConnection", message, null));
        if (!enabled) return;
        System.out.println(timestamp() + YELLOW + BOLD + " [CONN] " + RESET + message);
    }

    /** Log une info générale */
    public static void info(String message) {
        addToHistory(new LogEntry("INFO", "System", message, null));
        if (!enabled) return;
        System.out.println(timestamp() + BLUE + " [INFO] " + RESET + message);
    }
}
