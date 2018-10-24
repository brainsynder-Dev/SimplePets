package simplepets.brainsynder.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.errors.SimplePetsException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQL {
    private MysqlDataSource source;

    public MySQL(String host, String port, String database, String user, String pass) {
        source = new MysqlDataSource();
        source.setPort(Integer.parseInt(port));
        source.setPassword(pass);
        source.setUser(user);
        source.setDatabaseName(database);
        source.setServerName(host);
        source.setAutoReconnect(PetCore.get().getConfiguration().getBoolean("MySQL.Options.AutoReconnect"));
        source.setUseSSL(PetCore.get().getConfiguration().getBoolean("MySQL.Options.UseSSL"));
    }

    public MysqlDataSource getSource() {
        return source;
    }

    public boolean hasColumn(Connection connection, String column) {
        try {
            DatabaseMetaData md2 = connection.getMetaData();
            ResultSet rsTables = md2.getColumns(null, null, "SimplePets", column);
            return rsTables.next();
        } catch (Exception e) {
            PetCore.get().debug("Unable to check if '" + column + "' exists in the database");
        }
        return true;
    }

    public void addColumn(Connection connection, String column, String type) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("ALTER TABLE `SimplePets` ADD " + column + " " + type + " NOT NULL");
        } catch (Exception e) {
            throw new SimplePetsException("Unable to add '" + column + "' to the database", e);
        }
    }
}