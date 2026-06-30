package vn.edu.eaut.lab5.config;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBHelper {
    // Defaults - can be overridden by db.properties, system properties, or environment variables
    private static final String DEFAULT_URL = "jdbc:sqlite:database/minishop.db";
    private static final String DEFAULT_USER = "";
    private static final String DEFAULT_PASSWORD = "";
    private static final Properties CONFIG = loadProperties();
    private static volatile boolean sqliteInitialized = false;

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = DBHelper.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException ignored) {
            // no action; fallback to defaults
        }
        return props;
    }

    private static String resolve(String sysProp, String envVar, String defaultValue) {
        String v = System.getProperty(sysProp);
        if (v != null && !v.isEmpty()) return v;
        v = System.getenv(envVar);
        if (v != null && !v.isEmpty()) return v;
        v = CONFIG.getProperty(sysProp);
        if (v != null && !v.isEmpty()) return v;
        return defaultValue;
    }

    private static boolean isSqliteUrl(String url) {
        return url != null && url.startsWith("jdbc:sqlite:");
    }

    public static Connection getConnection() throws SQLException {
        String url = resolve("db.url", "DB_URL", DEFAULT_URL);
        String user = resolve("db.user", "DB_USER", DEFAULT_USER);
        String password = resolve("db.password", "DB_PASSWORD", DEFAULT_PASSWORD);

        if (isSqliteUrl(url)) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ignored) {
                // driver auto-registered if on classpath
            }
            Connection conn = DriverManager.getConnection(url);
            initializeSqlite(conn);
            return conn;
        }

        // MySQL compatibility fallback if needed
        if (!url.contains("serverTimezone=")) {
            if (url.contains("?")) url += "&serverTimezone=UTC";
            else url += "?serverTimezone=UTC";
        }
        if (!url.contains("useSSL=")) {
            url += "&useSSL=false";
        }
        if (!url.contains("allowPublicKeyRetrieval=")) {
            url += "&allowPublicKeyRetrieval=true";
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            // Driver should be provided by the dependency; ignore if auto-registered
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            System.err.println("DB connection failed with URL: " + url);
            System.err.println("DB user: " + user);
            throw ex;
        }
    }

    private static void initializeSqlite(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("PRAGMA foreign_keys = ON;");
        }
        if (sqliteInitialized) {
            return;
        }
        synchronized (DBHelper.class) {
            if (sqliteInitialized) {
                return;
            }
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS danh_muc (" +
                        "ma_dm INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ten_dm TEXT NOT NULL" +
                        ");");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS san_pham (" +
                        "ma_sp INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ten_sp TEXT NOT NULL, " +
                        "don_gia NUMERIC(12,2) NOT NULL, " +
                        "so_luong INTEGER NOT NULL DEFAULT 0, " +
                        "ma_dm INTEGER, " +
                        "FOREIGN KEY (ma_dm) REFERENCES danh_muc(ma_dm)" +
                        ");");
                if (!columnExists(conn, "san_pham", "ma_dm")) {
                    st.executeUpdate("ALTER TABLE san_pham ADD COLUMN ma_dm INTEGER;");
                }
                st.executeUpdate("CREATE TABLE IF NOT EXISTS khach_hang (" +
                        "ma_kh INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ten_kh TEXT NOT NULL, " +
                        "sdt TEXT NOT NULL, " +
                        "dia_chi TEXT" +
                        ");");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS hoa_don (" +
                        "ma_hd INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ngay_lap TEXT NOT NULL, " +
                        "ma_kh INTEGER NOT NULL, " +
                        "tong_tien NUMERIC(12,2) DEFAULT 0, " +
                        "FOREIGN KEY (ma_kh) REFERENCES khach_hang(ma_kh)" +
                        ");");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS chi_tiet_hoa_don (" +
                        "ma_hd INTEGER NOT NULL, " +
                        "ma_sp INTEGER NOT NULL, " +
                        "so_luong INTEGER NOT NULL, " +
                        "don_gia NUMERIC(12,2) NOT NULL, " +
                        "thanh_tien NUMERIC(12,2) NOT NULL, " +
                        "PRIMARY KEY (ma_hd, ma_sp), " +
                        "FOREIGN KEY (ma_hd) REFERENCES hoa_don(ma_hd), " +
                        "FOREIGN KEY (ma_sp) REFERENCES san_pham(ma_sp)" +
                        ");");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS tai_khoan (" +
                        "username TEXT PRIMARY KEY, " +
                        "password TEXT NOT NULL, " +
                        "ho_ten TEXT NOT NULL, " +
                        "vai_tro TEXT NOT NULL" +
                        ");");
                st.executeUpdate("INSERT OR IGNORE INTO tai_khoan(username, password, ho_ten, vai_tro) VALUES " +
                        "('admin', 'admin123', 'Quan tri vien', 'ADMIN'), " +
                        "('nhanvien', 'nv123', 'Nhan vien ban hang', 'NHANVIEN'), " +
                        "('ketoan', 'kt123', 'Ke toan', 'KETOAN');");
            }
            sqliteInitialized = true;
        }
    }

    private static boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("PRAGMA table_info(" + tableName + ");")) {
            while (rs.next()) {
                if (columnName.equalsIgnoreCase(rs.getString("name"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Ket noi CSDL thanh cong!");
            } else {
                System.out.println("Ket noi CSDL that bai: connection is closed or null");
            }
        } catch (SQLException e) {
            System.out.println("Ket noi CSDL that bai: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
