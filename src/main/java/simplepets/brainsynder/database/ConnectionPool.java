package simplepets.brainsynder.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * This is a little class I made to be a roughed out, but... it gets the job done...
 *
 * so far...
 *
 */
public class ConnectionPool {
    private BlockingQueue<Connection> pool;
    private int maxPoolSize;
    private int initialPoolSize;
    private int currentPoolSize;
    private MysqlDataSource source;

    public ConnectionPool(int maxPoolSize, int initialPoolSize, MysqlDataSource source) throws ClassNotFoundException, SQLException {

        if ((initialPoolSize > maxPoolSize) || initialPoolSize < 1 || maxPoolSize < 1)
            throw new IllegalArgumentException("Invalid pool size parameters");

        this.maxPoolSize = maxPoolSize > 0 ? maxPoolSize : 10;
        this.initialPoolSize = initialPoolSize;
        this.source = source;
        this.pool = new LinkedBlockingQueue<>(maxPoolSize);

        initPooledConnections();
    }

    private void initPooledConnections() throws SQLException {
        for (int i = 0; i < initialPoolSize; i++) {
            openAndPoolConnection();
        }
    }

    private synchronized void openAndPoolConnection() throws SQLException {
        if (currentPoolSize == maxPoolSize) {
            return;
        }

        pool.offer(source.getConnection());
        currentPoolSize++;
    }

    public Connection borrowConnection() throws InterruptedException, SQLException {
        if (pool.peek()==null && currentPoolSize < maxPoolSize) {
            openAndPoolConnection();
        }

        return pool.take();
    }

    public void surrenderConnection(Connection conn) {
        pool.offer(conn);
    }

    public void dumpPool () {
        pool.forEach(connection -> {
            try {
                if (!connection.isClosed()) connection.close();
            } catch (SQLException ignored) {}
        });

        pool.clear();
        pool = null;
    }
}