package simplepets.brainsynder.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.errors.SimplePetsException;

import java.sql.*;

public class MySQL {
    private MysqlDataSource source;
    private ConnectionPool pool;

    public MySQL(String host, String port, String database, String user, String pass) {
        source = new MysqlDataSource();
        source.setPort(Integer.parseInt(port));
        source.setPassword(pass);
        source.setUser(user);
        source.setDatabaseName(database);
        source.setServerName(host);
        source.setAutoReconnect(PetCore.get().getConfiguration().getBoolean("MySQL.Options.AutoReconnect"));
        source.setUseSSL(PetCore.get().getConfiguration().getBoolean("MySQL.Options.UseSSL"));

        try {
            pool = new ConnectionPool(10, 10, source);
        } catch (SQLException e) {
            System.out.println("Unable to initiate the ConnectionPool... Error:");
            e.printStackTrace();
        }
    }

    public boolean hasColumn (String column) {
        ConnectionPool pool = getPool();
        try {
            Connection connection = pool.borrowConnection();
            PreparedStatement query = connection.prepareStatement("SHOW COLUMNS FROM `"+source.getDatabaseName()+"` LIKE '"+column+"'");
            ResultSet set = query.executeQuery();
            pool.surrenderConnection(connection);
            return set.next();
        }catch (Exception e) {
            throw new SimplePetsException("Unable to check if '"+column+"' exists in the database", e);
        }
    }

    public void addColumn (String column, String type) {
        ConnectionPool pool = getPool();
        try {
            Connection connection = pool.borrowConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("ALTER TABLE `"+source.getDatabaseName()+"` ADD '"+column+"' "+type);
            pool.surrenderConnection(connection);
        }catch (Exception e) {
            throw new SimplePetsException("Unable to add '"+column+"' to the database", e);
        }
    }

    public ConnectionPool getPool() {
        return pool;
    }
}