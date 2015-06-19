package controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import util.UserFilter;
import model.Configurazione;
import model.User;

@ManagedBean
@SessionScoped
public class GestioneUserBean implements Serializable {

	private DataSource ds;

	private User userVisited;
	private Configurazione configUserVisited;
	private List<User> users;
	private UserFilter filtro;

	public GestioneUserBean() {
		ds = new DataSource();
		init();
	}
	
	public void init(){
		filtro= new UserFilter();
	}

	public List<User> getUserList() {
		this.users = ds.getUsers(this.filtro);
		return this.users;
	}

	public User getUser() {
		return this.userVisited;
	}

	public Configurazione getCurrentUserConfigurazione() {
		this.configUserVisited = ds.getConfigurazione(userVisited
				.getDispositivo());
		return this.configUserVisited;
	}

	public String recuperaUser(String username) {
		System.out.println("prerecupero");
		this.userVisited = ds.getUser(username);
		System.out.println("post recupero: "+userVisited.getUsername());
		return "dettaglio_utente";
	}
	
	public UserFilter getUserFilter(){
		return this.filtro;
	}
}
