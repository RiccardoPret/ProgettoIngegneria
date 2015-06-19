package controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import util.UserFilter;
import model.Admin;
import model.PersonaleAzienda;
import model.Configurazione;
import model.Dispositivo;
import model.User;

/**
 * Questa classe mette a disposizione i metodi per effettuare interrogazioni
 * sulla base di dati, sfruttabile dai bean
 */
public class DataSource implements Serializable {

	// === Properties
	private DatabaseDriver driver;

	// -- definizione delle query
	private String css = "SELECT DISTINCT CS.id, CS.codice, CS.nome "
			+ "FROM InsErogato IE, CorsoStudi CS, Docente D, Facolta F, Persona P "
			+ "WHERE IE.id_corsostudi=CS.id AND IE.id_facolta=F.id AND D.id_inserogato=IE.id AND D.id_persona=P.id ";
	// +"AND IE.annoaccademico='2013/2014' AND IE.hamoduli='0' AND P.cognome='Segala' AND CS.nome ILIKE '%i%' AND F.nome='Scienze matematiche fisiche e naturali'";

	private String cs = "SELECT id,nome,codice,abbreviazione,durataanni,sede,informativa "
			+ "FROM corsostudi " + "WHERE id=?";

	private String csf = "SELECT DISTINCT f.nome "
			+ "FROM facolta f INNER JOIN corsoinfacolta csf "
			+ "ON (f.id=csf.id_facolta) " + "WHERE csf.id_corsostudi=?";

	// === Methods

	public DataSource() {
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

}
