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
public class DataSource implements Serializable {

	private DatabaseDriverC3P0 driver;


	public DataSource() {
		driver = DatabaseDriverC3P0.getInstance();
	}
	
	/*
	 * Restituisce la configurazione del dispositivo passato come parametro
	 */
	public Configurazione getConfigurazione(Dispositivo dispositivo) {
		Configurazione conf= driver.getConfigurazione(dispositivo);
		return conf;
	}

	/*
	 * Restituisce lo User data bean con un certo username passato come parametro
	 */
	public User getUser(String utente) {
		User user= driver.getUser(utente);
		return user;
	}
	
	public Admin getAdmin(String username) {
		Admin admin= driver.getAdmin(username);
		return admin;
	}

	/*
	 * Aggiorna i dati dell'user e la configurazione del suo dispositivo nel database
	 * a seguito della modifica di alcuni parametri
	 */
	public void updateDbInstance(User client, Configurazione config) {
		driver.updateProfile(client);
		driver.updateSetting(config);
	}

	/*
	 * Restituisce la lista degli utenti
	 */
	public List<User> getUsers(UserFilter filtro) {
		List<User> users=null;
		users=filtro.isSetted()?driver.getFilteredUsers(filtro):driver.getUsers();
		return users;
	}

	/*
	 * Ritorna le posizioni del dispositivo passato come parametro
	 */
	public List<Posizione> getPosizioni(Dispositivo dispositivo) {
		List<Posizione> posizioni=null;
		posizioni=driver.getPosizioni(dispositivo);
		return posizioni;
	}
	
	/*
	 * ritorna la ultima posizione del dispositivo passato come parametro
	 * */
	public Posizione getUltimaPosizione(Dispositivo dispositivo){
		Posizione posizione=null;
		posizione=driver.getUltimaPosizione(dispositivo);
		return posizione;
	}

	public String getMail(int id) {
		// TODO Auto-generated method stub
		String mail= null;
		mail=driver.getMail(id);
		return mail;
	}

}
