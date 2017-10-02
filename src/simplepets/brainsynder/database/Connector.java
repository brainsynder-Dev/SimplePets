package simplepets.brainsynder.database;

import java.sql.Connection;

public interface Connector {
    void run(Connection connection) throws Exception;
}