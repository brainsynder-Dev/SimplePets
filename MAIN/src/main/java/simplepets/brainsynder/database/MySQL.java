package simplepets.brainsynder.database;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.errors.SimplePetsException;

import java.sql.*;

public class MySQL {
    private Connection connection;

    public MySQL(String host, String port, String database, String user, String pass) {
        String autoReconnect = String.valueOf(PetCore.get().getConfiguration().getBoolean("MySQL.Options.AutoReconnect"));
        String useSSL = String.valueOf(PetCore.get().getConfiguration().getBoolean("MySQL.Options.UseSSL"));
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&autoReconnect=%s", host, port, database, useSSL, autoReconnect);
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
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