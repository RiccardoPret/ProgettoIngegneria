package login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import model.Configurazione;
import controller.DatabaseDriver;
import controller.Query;

public class MyLoginModule implements LoginModule {

	private CallbackHandler callbackHandler;
	private Subject subject;

	private UserPrincipal userPrincipal;
	private RolePrincipal rolePrincipal;

	private String username;
	private char[] password = null;
	private boolean debug;
	
	private boolean loginSucceeded;
	private boolean commitSucceeded;

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;

		debug = "true".equalsIgnoreCase((String) options.get("debug"));
	}

	@Override
	public boolean login() throws LoginException {

		if (callbackHandler == null) {
			throw new LoginException("Error: no CallbackHandler available "
					+ "to garner authentication information from the user");
		}

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("username");
		callbacks[1] = new PasswordCallback("password: ", false);

		try {
			callbackHandler.handle(callbacks);
			username = ((NameCallback) callbacks[0]).getName();
			password = ((PasswordCallback) callbacks[1]).getPassword();

			if (debug) {
				// LOGGER.debug("Username :" + username);
				// LOGGER.debug("Password : " + password);
			}
			if (username == null || password == null) {
				// LOGGER.error("Callback handler does not return login data properly");
				throw new LoginException(
						"Callback handler does not return login data properly");
			}
			if (isValidUserSH()){
				loginSucceeded = true;
				//Il login ha avuto successo: il dispositivo va attivato
				activateDevice(username);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//TODO testare che attivi il dispositivo
	private void activateDevice(String username){
		DatabaseDriver driver= DatabaseDriver.getInstance();
		PreparedStatement stmt = null;
		String sql = "select D.id_dispositivo from dispositivo d, utente u where u.dispositivo=d.id_dispositivo AND u.username=?";
		String sql2= "update dispositivo set attivo=TRUE where id_dispositivo=?";
		driver.openConnection();
		
		try {
			stmt = driver.getOpenedConnection().prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int id= rs.getInt("id_dispositivo");
				stmt = driver.getOpenedConnection().prepareStatement(sql2);
				stmt.setInt(1, id);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		driver.closeConnection();
	}

	//Non usato. Può essere usato al posto di quello che cripta le password: isValidUserSH
	private boolean isValidUser() {
		boolean userExists;

		DatabaseDriver driver = DatabaseDriver.getInstance();
		driver.openConnection();
		userExists = driver.userExists(this.username, this.password);
		driver.closeConnection();
		return userExists;
	}
	
	private boolean isValidUserSH() {

		DatabaseDriver driver = DatabaseDriver.getInstance();
		driver.openConnection();
		String hash = driver.userExistsSH(this.username);
		driver.closeConnection();
		System.out.println(this.password);
		System.out.println(hash);
		
		try {
			return PasswordHash.validatePassword(new String(this.password),new String(hash));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean commit() throws LoginException {
		if (loginSucceeded == false) {
			return false;
		} else {
			userPrincipal = new UserPrincipal(username);
			if (!subject.getPrincipals().contains(userPrincipal)) {
				subject.getPrincipals().add(userPrincipal);
				// LOGGER.debug("User principal added:" + userPrincipal);
			}

			// populate subject with roles.
			List<String> roles = getRoles();
			for (String role : roles) {
				RolePrincipal rolePrincipal = new RolePrincipal(role);
				if (!subject.getPrincipals().contains(rolePrincipal)) {
					subject.getPrincipals().add(rolePrincipal);
					// LOGGER.debug("Role principal added: " + rolePrincipal);
				}
			}

			commitSucceeded = true;
			// LOGGER.info("Login subject were successfully populated with principals and roles");
			return true;
		}

	}

	private List<String> getRoles() {
		List<String> roleList = new ArrayList<String>();

		DatabaseDriver driver = DatabaseDriver.getInstance();
		driver.openConnection();
		roleList = driver.getRoles(this.username);
		driver.closeConnection();

		return roleList;
	}

	@Override
	public boolean abort() throws LoginException {
		if (this.loginSucceeded == false) {
			return false;
		} else if (this.loginSucceeded == true && commitSucceeded == false) {
			this.loginSucceeded = false;
			username = null;
			if (password != null) {
				password = null;
			}
			userPrincipal = null;
		} else {
			this.logout();
		}
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(userPrincipal);
		subject.getPrincipals().remove(rolePrincipal);

		loginSucceeded = false;
		commitSucceeded = loginSucceeded;

		username = null;
		password = null;

		userPrincipal = null;
		rolePrincipal = null;

		return true;
	}

}