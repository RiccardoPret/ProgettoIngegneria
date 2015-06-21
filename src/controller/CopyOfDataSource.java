package controller;

import java.io.Serializable;
import java.util.List;

import model.Admin;
import model.Configurazione;
import model.Dispositivo;
import model.Posizione;
import model.User;
import util.UserFilter;

/**
 * Questa classe mette a disposizione i metodi per effettuare interrogazioni
 * sulla base di dati, sfruttabile dai bean
 */
public class CopyOfDataSource implements Serializable {

	private DatabaseDriver driver;


	public CopyOfDataSource() {
		driver = DatabaseDriver.getInstance();
	}
	
	/*
	 * Restituisce la configurazione del dispositivo passato come parametro
	 */
	public Configurazione getConfigurazione(Dispositivo dispositivo) {
		driver.openConnection();
		Configurazione conf= driver.getConfigurazione(dispositivo);
		driver.closeConnection();
		return conf;
	}

	/*
	 * Restituisce lo User data bean con un certo username passato come parametro
	 */
	public User getUser(String utente) {
		driver.openConnection();
		User user= driver.getUser(utente);
		driver.closeConnection();
		return user;
	}
	
	public Admin getAdmin(String username) {
		driver.openConnection();
		Admin admin= driver.getAdmin(username);
		driver.closeConnection();
		return admin;
	}

	/*
	 * Aggiorna i dati dell'user e la configurazione del suo dispositivo nel database
	 * a seguito della modifica di alcuni parametri
	 */
	public void updateDbInstance(User client, Configurazione config) {
		driver.openConnection();
		driver.updateProfile(client);
		driver.updateSetting(config);
		driver.closeConnection();
	}

	/*
	 * Restituisce la lista degli utenti
	 */
	public List<User> getUsers(UserFilter filtro) {
		List<User> users=null;
		driver.openConnection();
		users=filtro.isSetted()?driver.getFilteredUsers(filtro):driver.getUsers();
		driver.closeConnection();
		return users;
	}

	/*
	 * Ritorna le posizioni del dispositivo passato come parametro
	 */
	public List<Posizione> getPosizioni(Dispositivo dispositivo) {
		List<Posizione> posizioni=null;
		driver.openConnection();
		posizioni=driver.getPosizioni(dispositivo.getId());
		driver.closeConnection();
		return posizioni;
	}
	
	/*
	 * ritorna la ultima posizione del dispositivo passato come parametro
	 * */
	public Posizione getUltimaPosizione(Dispositivo dispositivo){
		Posizione posizione=null;
		driver.openConnection();
		posizione=driver.getUltimaPosizione(dispositivo.getId());
		driver.closeConnection();
		return posizione;
	}

}
