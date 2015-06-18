package controller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Configurazione;
import util.Dispositivo;
import util.User;

/*
 * Classe che si occupa di interfacciare le query disponibili in modo semplice e leggibile
 */

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

	/*
	 * L'utene ha la possibilità di aprirsi e chiudersi la connessione a
	 * piacere. In questo modo se deve eseguire più query non deve continuamente
	 * aprire e chiudere la connessione
	 */
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

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			while (e.getNextException() instanceof SQLException)
				e.printStackTrace();
		}
	}
	
/*
	public String getTelefono(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String telefono = null;
		String sql = Query.getInstance().getQuery("query_getTelefono");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				telefono = rs.getString("numTelefono");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return telefono;
	}

	public String getEmail(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String email = null;
		String sql = Query.getInstance().getQuery("query_getEmail");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				email = rs.getString("mail");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return email;
	}

	public Dispositivo getDispositivo(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Dispositivo device = null;
		String sql = Query.getInstance().getQuery("query_getUserDevice");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				device = new Dispositivo(rs.getInt("id_dispositivo"),
						rs.getString("modello"), rs.getBoolean("attivo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return device;
	}
*/
	
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

	public Configurazione getConfigurazione(Dispositivo dispositivo) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Configurazione setting = new Configurazione(dispositivo);
		String sql = Query.getInstance().getQuery("query_getConfig");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, dispositivo.getId());

			rs = stmt.executeQuery();

			if (rs.next()) {
				setting.setfPos(rs.getInt("freq_ril"));
				setting.setfSms(rs.getInt("freq_mex"));
				setting.setspeedAlarm(rs.getInt("limiteVelocita"));
				setting.setSmsEnabled(rs.getBoolean("messaggi"));
				setting.setEmailEnabled(rs.getBoolean("mail"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return setting;
	}

	public User getUser(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		User user = new User(username);
		System.out.println(user.getUsername());
		String sql = Query.getInstance().getQuery("query_getUser");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			// Se trova la stringa setta la sua roba
			if (rs.next()) {
				Dispositivo device = getDispositivoFromId(rs
						.getInt("dispositivo"));

				user.setDispositivo(device);
				user.setEmail(rs.getString("mail"));
				user.setTelefono("numTelefono");
				user.setVideo(rs.getString("linkStreaming"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	private Dispositivo getDispositivoFromId(int id) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Dispositivo device = null;
		String sql = Query.getInstance().getQuery("query_getDevice");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id);

			rs = stmt.executeQuery();

			if (rs.next()) {
				device = new Dispositivo(id, rs.getString("modello"),
						rs.getBoolean("attivo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return device;
	}
	/*
	public void updateUserProfile(User utente) {
		PreparedStatement stmt = null;
		String sql = getUpdateQuery(attributo);
		
		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, valore);
			stmt.setString(2, username);

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getUpdateQuery(String attributo) {
		String sql = null;
		switch (attributo) {
		case "mail":
			sql = Query.getInstance().getQuery("update_mail");
			break;
		case "numTelefono":
			sql = Query.getInstance().getQuery("update_numTelefono");
			break;
		case "linkStreaming":
			sql= Query.getInstance().getQuery("update_linkStreaming");
			break;
		default:
			System.out.println("Attributo non esistente!!!");
			break;
		}
		return sql;
	}
*/
	public void updateProfile(User client) {
		PreparedStatement stmt = null;
		String sql=Query.getInstance().getQuery("update_profile");
		
		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, client.getEmail());
			stmt.setString(2, client.getTelefono());
			stmt.setString(3, client.getVideo());
			
			stmt.setString(4, client.getUsername());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSetting(Configurazione config) {
		PreparedStatement stmt = null;
		String sql=Query.getInstance().getQuery("update_setting");
		
		checkInstantiation();
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, config.getfPos());
			stmt.setInt(2, config.getfSms());
			stmt.setInt(3, config.getspeedAlarm());
			stmt.setBoolean(4, config.isSmsEnabled());
			stmt.setBoolean(5, config.isEmailEnabled());
			
			stmt.setInt(6, config.getDispositivo().getId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
