package controller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {
	private static DatabaseDriver instance;
	private Connection connection;

	private static ReservedReader DBCredential; // Evito le credenziali
												// hard-coded. Esse sono in un
												// file inaccessibile
	private static String dbFile = "credential_DBMS.txt";
	private static String separator = "=>";

	private DatabaseDriver() {
		super();
	}

	public static DatabaseDriver getInstance() {
		if (instance == null) {
			instance = new DatabaseDriver();
			DBCredential = new ReservedReader(instance, dbFile, separator);

			System.out.println(DBCredential.getValue("driver"));
			System.out.println(DBCredential.getValue("url"));
			System.out.println(DBCredential.getValue("username"));
			System.out.println(DBCredential.getValue("password"));
		}
		return instance;
	}

	private void checkInstantiation() {
		if (instance == null) {
			System.out
					.println("Non aprire connessioni senza prima avere istanziato l'oggetto!");
			instance = new DatabaseDriver();
		}
	}

	public void openConnection() {
		checkInstantiation();
		try {
			Driver myDriver = (Driver) Class.forName(
					DBCredential.getValue("driver")).newInstance();
			DriverManager.registerDriver(myDriver);
			// creazione della connessione
			connection = DriverManager.getConnection(
					DBCredential.getValue("url"),
					DBCredential.getValue("username"),
					DBCredential.getValue("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("Driver non trovato");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Conessione al database non riuscita");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Dispositivo getDispositivo(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = Query.getInstance().getQuery("query_getDevice");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				return new Dispositivo(rs.getInt("id_dispositivo"),
						rs.getString("modello"), rs.getBoolean("attivo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			while (e.getNextException() instanceof SQLException)
				e.printStackTrace();
		}
	}

	public boolean userExists(String username, char[] password) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = Query.getInstance().getQuery("query_checkUser");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, new String(password));

			rs = stmt.executeQuery();

			if (rs.next()) { // User exist with the given user name and
								// password.
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public List<String> getRoles(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<String> roleList = new ArrayList<String>();
		String sql = Query.getInstance().getQuery("query_role");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				roleList.add(rs.getString("ruolo"));
			}
		} catch (Exception e) {
			// LOGGER.error("Error when loading user from the database " + e);
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// LOGGER.error("Error when closing result set." + e);
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				// LOGGER.error("Error when closing statement." + e);
			}
		}
		return roleList;
	}
}
