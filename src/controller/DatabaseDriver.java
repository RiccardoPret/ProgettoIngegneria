package controller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Admin;
import model.Configurazione;
import model.Dispositivo;
import model.Posizione;
import model.User;

import org.postgresql.geometric.PGpoint;

import util.UserFilter;

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
	 * aprire e chiudere la connessione. Questo metodo, apre la connessione e poi la ritorna
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
	
	public Connection getOpenedConnection(){
		if(this.connection!=null){
			return this.connection;
		}
		System.out.println("Connessione non aperta!");
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

			if (rs.next()) { // User exist with the given user name and password
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String userExistsSH(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = Query.getInstance().getQuery("query_getHash");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				System.out.println(rs.getString(1));
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
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
			e.printStackTrace();
		}
		return setting;
	}

	public User getUser(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		User user = null;
		String sql = Query.getInstance().getQuery("query_getUser");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();
			if (rs.next()) {
				user=getUserByResultSet(rs);
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

	public void updateProfile(User client) {
		PreparedStatement stmt = null;
		String sql = Query.getInstance().getQuery("update_profile");

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
		String sql = Query.getInstance().getQuery("update_setting");

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

	public Admin getAdmin(String username) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Admin admin = null;
		String sql = Query.getInstance().getQuery("query_getAdmin");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				admin= new Admin(username);
				admin.setNome(rs.getString("nome"));
				admin.setCognome(rs.getString("cognome"));
				admin.setRuolo("admin");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admin;
	}
	
	public List<User> getUsers(){
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<User> users = new ArrayList<User>();
		String sql = Query.getInstance().getQuery("query_getUserList");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				users.add(getUserByResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	private User getUserByResultSet(ResultSet rs) {
		User user= null;
		try {
			user=new User(rs.getString("username"));

			user.setDispositivo(getDispositivoFromId(rs.getInt("dispositivo")));
			user.setEmail(rs.getString("mail"));
			user.setTelefono(rs.getString("numTelefono"));
			user.setVideo(rs.getString("linkStreaming"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public List<User> getFilteredUsers(UserFilter filtro) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<User> users = new ArrayList<User>();
		String sql = Query.getInstance().getQuery("query_getUserList")+" where TRUE=TRUE";
		System.out.println("Inizio costruzione query");
		if(!filtro.getUsername().isEmpty())
			sql+=" AND username='"+filtro.getUsername()+"'";
		if(filtro.getIdDispositivo()!=null)
			sql+=" AND dispositivo="+filtro.getIdDispositivo();
		if(!filtro.getEmail().isEmpty())
			sql+=" AND mail='"+filtro.getEmail()+"'";
		if(!filtro.getTelefono().isEmpty())
			sql+=" AND numTelefono='"+filtro.getTelefono()+"'";
		
		System.out.println(sql);
		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				users.add(getUserByResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public List<Posizione> getPosizioni(Integer id) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Posizione> posizioni = new ArrayList<Posizione>();
		String sql = Query.getInstance().getQuery("query_getPosizioni");

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				posizioni.add(getPosizioneByResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return posizioni;
	}

	private Posizione getPosizioneByResultSet(ResultSet rs) {
		Posizione pos= null;
		try {
			pos=new Posizione(getDispositivoFromId(rs.getInt("dispositivo")));
			pos.setTimestamp(rs.getTimestamp(2));
			pos.setCoordinate((PGpoint)rs.getObject("coordinate"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pos;
	}

}
