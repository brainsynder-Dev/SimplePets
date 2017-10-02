package simplepets.brainsynder.database;

import java.sql.DriverManager;

public class MySQL extends Database {
    private String user = null,
			database = null,
			pass = null,
			port = null,
			host = null;

    public MySQL(String host, String port, String database, String user, String pass) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}
	
	public void connect(Connector connector) throws Exception {
		if (!checkConnection()) {
			String u = "jdbc:mysql://" + this.host + ":" + this.port;
			if (database != null) {
				u = u + "/" + this.database;
			}

			connection = DriverManager.getConnection(u, this.user, this.pass);
			connector.run(connection);
		}
	}

	/**
	 * Once the
	 *
	 * @param connector
	 * @throws Exception
	 */
	public void connectAutoClose(Connector connector) throws Exception {
        if (!checkConnection()) {
            String u = "jdbc:mysql://" + this.host + ":" + this.port;
            if (database != null) {
                u = u + "/" + this.database;
            }

            connection = DriverManager.getConnection(u, this.user, this.pass);
            connector.run(connection);
            connection.close();
        }
    }
}