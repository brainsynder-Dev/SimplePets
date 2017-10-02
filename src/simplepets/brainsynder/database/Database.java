package simplepets.brainsynder.database;

import java.sql.Connection;

abstract class Database {
    Connection connection;

    Database() {
        this.connection = null;
    }

    boolean checkConnection() throws Exception {
        return (connection != null && !connection.isClosed());
    }
}