package controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import Socket.Gestore_connessioniUscita;
import model.Configurazione;
import model.User;

@ManagedBean
@SessionScoped
public class ClientBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7231438830346457810L;

	@ManagedProperty(value="#{securityBacking}")
	private SecurityBacking securityBacking;
	
	private DataSource ds;
	private User client;
	private Configurazione config;
	
	public ClientBean() {
		this.ds = new DataSource();
	}
	
	@PostConstruct
	public void init(){
		//Creo l'utente coi valori settati
		this.client=ds.getUser(this.securityBacking.getWelcome());
		//Creo la configurazione coi valori settati
		this.config=ds.getConfigurazione(this.client.getDispositivo());
	}
	
	//Metodo obbligatorio per il ManagedProperty
	public void setSecurityBacking(SecurityBacking s){
		this.securityBacking=s;
	}
	
	//Metodo obbligatorio per il ManagedProperty
	public SecurityBacking getSecurityBacking(){
		return this.securityBacking;
	}

	public User getUser(){
		return this.client;
	}
	
	public Configurazione getConfigurazione(){
		return this.config;
	}

	public void updateDb(){
		ds.updateDbInstance(this.client, this.config);
		Gestore_connessioniUscita.getInstance().updateConfig(client.getDispositivo().getId());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Valori aggiornati"));
		   
	}
}