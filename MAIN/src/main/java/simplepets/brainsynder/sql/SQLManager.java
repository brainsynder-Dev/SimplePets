package simplepets.brainsynder.sql;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * This class was provided by {@link https://github.com/Thatsmusic99}
 * From the resource {@link https://github.com/Niestrat99/AT-Rewritten}
 * Fat chance, i didn't use MysqlDataSource lmao
 */
public abstract class SQLManager {
    private Connection connection;
    protected String tablePrefix;
    private final String databaseName;
    protected boolean usingSqlite;

    public SQLManager() {
        this(false);
    }

    public SQLManager(boolean forceSqlite) {
        PetCore plugin = PetCore.getInstance();
        tablePrefix = plugin.getConfiguration().getString("MySQL.Table");
        if (!tablePrefix.matches("^[_A-Za-z0-9]+$")) {
            SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Table prefix " + tablePrefix + " is not alphanumeric. Using simplepets...", true);
            tablePrefix = "simplepets";
        }
        boolean enabled = plugin.getConfiguration().getBoolean("MySQL.Enabled", false);
        String host = plugin.getConfiguration().getString("MySQL.Host");
        int port = plugin.getConfiguration().getInt("MySQL.Port");
        databaseName = plugin.getConfiguration().getString("MySQL.DatabaseName");
        String user = plugin.getConfiguration().getString("MySQL.Login.Username");
        String pass = plugin.getConfiguration().getString("MySQL.Login.Password");
        boolean ssl = plugin.getConfiguration().getBoolean("MySQL.Options.UseSSL", false);

        if (forceSqlite) {
            //Debug.debug(DebugLevel.DEBUG, getClass().getSimpleName()+" Using SQLite - forced", true);
            loadSqlite();
            createTable();
            return;
        }
        if (!enabled) {
            //Debug.debug(DebugLevel.DEBUG, getClass().getSimpleName()+" Using SQLite - sql disabled", true);
            loadSqlite();
            createTable();
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //Debug.debug(DebugLevel.DEBUG, getClass().getSimpleName()+" Error using SQLite", true);
            loadSqlite();
            createTable();
        }

        CompletableFuture.runAsync(() -> {
            StringBuilder url = new StringBuilder();
            url.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(databaseName);
            url.append("?useSSL=").append(ssl);
            url.append("&autoReconnect=true");
            try {
                connection = DriverManager.getConnection(url.toString(), user, pass);
                usingSqlite = false;
                PetCore.getInstance().getDebugLogger().debug(getClass().getSimpleName() + " Using MySQL");
                createTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void disconnect() {
        if (connection == null) return;

        try {
            if (connection.isClosed()) return;
            connection.close();
            connection = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // This method should keep SQLite connections open, while closing regular SQL connections
    public void handleConnection (Consumer<Connection> consumer) {
        consumer.accept(connection);
    }

    public String getTable(String suffix) {
        if (usingSqlite) return tablePrefix + suffix;
        return databaseName + "." + (tablePrefix + suffix);
    }

    private void loadSqlite() {
        // Load JDBC
        try {
            Class.forName("org.sqlite.JDBC");
            File file = new File(PetCore.getInstance().getDataFolder(), "storage.db");
            if (!file.exists()) file.createNewFile();
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            usingSqlite = true;
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    //SHOW COLUMNS FROM `tbl_name`; // Lists columns
    public void hasColumn(String table, String column, SQLCallback<Boolean> callback) {
        CompletableFuture.runAsync(() -> {
            handleConnection(connection -> {
                try {
                    DatabaseMetaData md2 = connection.getMetaData();
                    ResultSet rsTables = md2.getColumns(null, null, tablePrefix + table, column);
                    callback.onSuccess(rsTables.next());
                } catch (Exception e) {
                    SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to check if '" + column + "' exists in the database");
                    callback.onSuccess(false);
                }
            });
        });
    }

    public void modifyColumn(String table, String column, String type) {
        CompletableFuture.runAsync(() -> {
            handleConnection(connection -> {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("ALTER TABLE `" + tablePrefix + table + "` MODIFY COLUMN " + column + " " + type + " NOT NULL");
                } catch (SQLException throwables) {
                    SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to add '" + column + "' to the database");
                }
            });
        });
    }

    public void addColumn(String table, String column, String type) {
        CompletableFuture.runAsync(() -> {
            handleConnection(connection -> {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("ALTER TABLE `" + tablePrefix + table + "` ADD " + column + " " + type + " NOT NULL");
                } catch (SQLException throwables) {
                    SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to add '" + column + "' to the database");
                }
            });
        });
    }

    public abstract void createTable();

    public abstract void transferOldData();

    public String getStupidAutoIncrementThing() {
        return usingSqlite ? "AUTOINCREMENT" : "AUTO_INCREMENT";
    }

    public String getStupidTextThing() {
        return usingSqlite ? "TEXT" : "LONGTEXT";
    }

    public boolean isUsingSqlite() {
        return usingSqlite;
    }

    public interface SQLCallback<D> {
        void onSuccess(D data);

        default void onSuccess() {}

        default void onFail() {}
    }


}