package simplepets.brainsynder.sql;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.debug.DebugLevel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * This class was provided by {@link https://github.com/Thatsmusic99}
 * From the resource {@link https://github.com/Niestrat99/AT-Rewritten}
 * Fat chance, i didn't use MysqlDataSource lmao
 */
public abstract class SQLManager {

    protected String tablePrefix;
    private final String databaseName;
    protected volatile boolean usingSqlite;
    private Connection sqliteConnection;

    public SQLManager() {
        this(false);
    }

    public SQLManager(boolean forceSqlite) {
        tablePrefix = ConfigOption.INSTANCE.MYSQL_TABLE.getValue();
        if (!tablePrefix.matches("^[_A-Za-z0-9]+$")) {
            SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Table prefix " + tablePrefix + " is not alphanumeric. Using simplepets...", true);
            tablePrefix = "simplepets";
        }
        boolean enabled = ConfigOption.INSTANCE.MYSQL_ENABLED.getValue();
        databaseName = ConfigOption.INSTANCE.MYSQL_DATABASE.getValue();

        if (forceSqlite || !enabled) {
            //Debug.debug(DebugLevel.DEBUG, getClass().getSimpleName()+" Using SQLite - forced", true);
            usingSqlite = true;
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                //Debug.debug(DebugLevel.DEBUG, getClass().getSimpleName()+" Error using SQLite", true);
                usingSqlite = true;
            }
        }

        // This ended up being missed in the code leading SQLSyntaxErrorException
        createTable();
    }

    // Forgot about thread safety and got told off for it in AT
    public Connection implementConnection() {
        if (usingSqlite) {
            if (sqliteConnection != null) return sqliteConnection;
            return sqliteConnection = loadSqlite();
        } else {
            Connection connection = null;
            StringBuilder url = new StringBuilder();
            url.append("jdbc:mysql://").append(ConfigOption.INSTANCE.MYSQL_HOST.getValue()).append(":")
                    .append(ConfigOption.INSTANCE.MYSQL_PORT.getValue()).append("/").append(databaseName);
            url.append("?useSSL=").append(ConfigOption.INSTANCE.MYSQL_SSL.getValue());
            url.append("&autoReconnect=true");

            try {
                connection = DriverManager.getConnection(url.toString(),
                        ConfigOption.INSTANCE.MYSQL_USERNAME.getValue(), ConfigOption.INSTANCE.MYSQL_PASSWORD.getValue());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return connection;
        }
    }

    public void fetchConnection (Consumer<Connection> consumer) {
        if (usingSqlite) {
            if (sqliteConnection != null) sqliteConnection = loadSqlite();
            consumer.accept(sqliteConnection);
            return;
        }

        try (Connection connection = implementConnection()) {
            consumer.accept(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTable(String suffix) {
        if (usingSqlite) return tablePrefix + suffix;
        return databaseName + "." + (tablePrefix + suffix);
    }

    private Connection loadSqlite() {
        // Load JDBC
        try {
            Class.forName("org.sqlite.JDBC");
            File file = new File(PetCore.getInstance().getDataFolder(), "storage.db");
            if (!file.exists()) file.createNewFile();
            return DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
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
}