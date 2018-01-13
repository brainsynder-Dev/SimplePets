package simplepets.brainsynder.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import simplepets.brainsynder.PetCore;

import java.sql.SQLException;

public class MySQL {
    private ConnectionPool pool;

    public MySQL(String host, String port, String database, String user, String pass) {
        MysqlDataSource source = new MysqlDataSource();
        source.setPort(Integer.parseInt(port));
        source.setPassword(pass);
        source.setUser(user);
        source.setDatabaseName(database);
        source.setServerName(host);
        source.setAutoReconnect(PetCore.get().getConfiguration().getBoolean("MySQL.Options.AutoReconnect"));
        source.setUseSSL(PetCore.get().getConfiguration().getBoolean("MySQL.Options.UseSSL"));

        try {
            pool = new ConnectionPool(10, 10, source);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Unable to initiate the ConnectionPool... Error:");
            e.printStackTrace();
        }
    }

    public ConnectionPool getPool() {
        return pool;
    }
}