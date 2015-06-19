package controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.Admin;

@ManagedBean
@SessionScoped
public class AdminBean implements Serializable{
	
	@ManagedProperty(value="#{securityBacking}")
	private SecurityBacking securityBacking;
	
	private DataSource ds;
	private Admin admin;
	
	public AdminBean() {
		this.ds= new DataSource();
	}
	
	@PostConstruct
	public void init(){
		//Creo l'utente coi valori settati
		this.admin=ds.getAdmin(this.securityBacking.getWelcome());
	}
	
	//Metodo obbligatorio per il ManagedProperty
	public void setSecurityBacking(SecurityBacking s){
		this.securityBacking=s;
	}
	
	//Metodo obbligatorio per il ManagedProperty
	public SecurityBacking getSecurityBacking(){
		return this.securityBacking;
	}
	
	public Admin getAdmin(){
		return this.admin;
	}
}
