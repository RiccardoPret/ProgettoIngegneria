package controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import util.UserFilter;
import model.Configurazione;
import model.User;

@ManagedBean
@SessionScoped
public class GestioneUserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1808122569173838766L;

	private DataSource ds;

	private User userVisited;
	private Configurazione configUserVisited;
	private List<User> users;
	private UserFilter filtro;
	private String password;

	public GestioneUserBean() {
		ds = new DataSource();		
	}
	
	@PostConstruct
	public void init(){
		filtro= new UserFilter();
		aggiornaUserList();
	}

	public void aggiornaUserList(){
		this.users = ds.getUsers(this.filtro);
	}
	public List<User> getUserList() {
		return this.users;
	}

	public User getUser() {
		return this.userVisited;
	}

	public Configurazione getCurrentUserConfigurazione() {
		return this.configUserVisited;
	}

	public String recuperaUser(String username) {
		System.out.println("prerecupero");
		this.userVisited = ds.getUser(username);
		System.out.println("post recupero: "+userVisited.getUsername());
		this.configUserVisited = ds.getConfigurazione(userVisited
				.getDispositivo());
		return "dettaglio_utente";
	}
	
	public UserFilter getUserFilter(){
		return this.filtro;
	}
	public String getPassword(){
		return "";
	}
	public void setPassword(String s){
		this.password=s;
	}
	public void aggiornaPassword(){
		ds.changePassword(password, userVisited.getDispositivo().getId());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Valori aggiornati"));

	}
}
