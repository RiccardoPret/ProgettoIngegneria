package controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ClientBean implements Serializable {

	@ManagedProperty(value="#{securityBacking}")
	private SecurityBacking securityBacking;
	
	private ClientDataSource ds;
	
	private Dispositivo dispositivo;
	private String username = "";
	private String email = "";
	private String telefono = "";
	private String video="";
	private Configurazione config;
	
	public ClientBean() {
		this.ds = new ClientDataSource();
	}
	
	@PostConstruct
	public void init(){
		//Prendo lo username
		this.username=this.securityBacking.getWelcome();

		//Prendo su il resto dell'utente
		this.dispositivo=ds.getDispositivoFromUser(this.username);
		this.email=ds.getEmail(this.username);
		this.telefono=ds.getTelefono(this.username);
		this.video=ds.getVideo(this.username);
		this.config=ds.getConfigurazione(this.dispositivo);
	}
	
	public void setSecurityBacking(SecurityBacking s){
		this.securityBacking=s;
	}
	
	public SecurityBacking getSecurityBacking(){
		return this.securityBacking;
	}

	//Valori non modificabili
	public String getIdDispositivo() {
		return this.dispositivo.getId().toString();
	}
	
	public String getModelloDispositivo(){
		return this.dispositivo.getModello();
	}

	public String getUsername() {
		return this.username;
	}
	
	public String getVideo() {
		return video;
	}

	//Valori modificabili
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
		//aggiorno il db
		ds.updateEmail(this.username, email);
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String tel) {
		this.telefono = tel;
		//aggiorno il db
		ds.updateTelefono(this.username, tel);
	}
	
	public Configurazione getConfigurazione(){
		return this.config;
	}

}