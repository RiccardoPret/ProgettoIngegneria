package login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

import controller.DatabaseDriverC3P0;

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
			if (isValidUserSH()) {
				loginSucceeded = true;
				// Il login ha avuto successo: il dispositivo va attivato
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

	// TODO testare che attivi il dispositivo
	private void activateDevice(String username) {
		DatabaseDriverC3P0 driver = DatabaseDriverC3P0.getInstance();
		driver.activateDevice(username);
		
	}



	private boolean isValidUserSH() {

		DatabaseDriverC3P0 driver = DatabaseDriverC3P0.getInstance();
		String hash = driver.userExistsSH(this.username);
		System.out.println(this.password);
		System.out.println(hash);

		try {
			return PasswordHash.validatePassword(new String(this.password),
					new String(hash));
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

		DatabaseDriverC3P0 driver = DatabaseDriverC3P0.getInstance();
		roleList = driver.getRoles(this.username);

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