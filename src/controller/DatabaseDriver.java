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
	
	private String dBUser = "eiwpodpcjchayi";
	private String dBPassword = "Ij5zl1Sj6EVkm0xcC1qKgj8NsP";
	private String dBUrl = "jdbc:postgresql://ec2-54-247-79-142.eu-west-1.compute.amazonaws.com/de6rbt0qvpr0u2?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	private String dBDriver = "org.postgresql.Driver";

	private DatabaseDriver(){
		super();
	}
	
	public static DatabaseDriver getInstance(){
		if(instance==null){
			instance=new DatabaseDriver();
		}
		return instance;
	}

	private void checkInstantiation(){
		if(instance==null){
			System.out.println("Non aprire connessioni senza prima avere istanziato l'oggetto!");
			instance=new DatabaseDriver();
		}
	}
	
	public void openConnection() {
		checkInstantiation();
		try {
			Driver myDriver = (Driver) Class.forName(dBDriver).newInstance();
			DriverManager.registerDriver(myDriver);
			// creazione della connessione
			connection = DriverManager.getConnection(dBUrl, dBUser, dBPassword);
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

	public boolean userExists(String username, char[] password) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String sql = "select username from user_list where user_list.username=? and user_list.password=?";

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
		String sql = "select user_role.role from user_list, user_role where user_list.username=user_role.username and user_list.username=?";

		checkInstantiation();
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			rs = stmt.executeQuery();

			if (rs.next()) {
				roleList.add(rs.getString("role"));
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
